package src.optmize;

import src.asm.basic.*;
import src.asm.instructions.*;

import java.util.*;

public class PremAllocator {
    public AsmFile asmF;

    public static final int kCnt = 27;
    AsmRealReg RegSp = AsmRealReg.regMap.get("sp");

    public LinkedHashSet<AsmReg> preColored = new LinkedHashSet<>(AsmRealReg.regMap.values());
    public LinkedHashSet<AsmReg> initial = new LinkedHashSet<>();
    public LinkedList<AsmReg> simplifyWorkList = new LinkedList<>();
    public LinkedList<AsmReg> freezeWorkList = new LinkedList<>();
    public LinkedList<AsmReg> spillWorkList = new LinkedList<>();
    public LinkedHashSet<AsmReg> spilledNodes = new LinkedHashSet<>();
    public LinkedHashSet<AsmReg> coalescedNodes = new LinkedHashSet<>();
    public LinkedHashSet<AsmReg> coloredNodes = new LinkedHashSet<>();
    public Stack<AsmReg> selectStack = new Stack<>();

    public LinkedHashSet<AsmMv> coalescedMoves = new LinkedHashSet<>();
    public LinkedHashSet<AsmMv> constrainedMoves = new LinkedHashSet<>();
    public LinkedHashSet<AsmMv> frozenMoves = new LinkedHashSet<>();
    public LinkedHashSet<AsmMv> workListMoves = new LinkedHashSet<>();
    public LinkedHashSet<AsmMv> activeMoves = new LinkedHashSet<>();

    public static class Edge {
        public AsmReg u, v;

        public Edge(AsmReg u, AsmReg v) {
            this.u = u;
            this.v = v;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof Edge))
                return false;
            Edge e = (Edge) obj;
            return (u == e.u && v == e.v) || (u == e.v && v == e.u);
        }

        @Override
        public int hashCode() {
            return u.hashCode() ^ v.hashCode();
        }
    }

    public HashSet<Edge> adjSet = new HashSet<>();
    public HashMap<AsmReg, HashSet<AsmReg>> adjList = new HashMap<>();
    public HashMap<AsmReg, Integer> degree = new HashMap<>();
    public HashMap<AsmReg, HashSet<AsmMv>> moveList = new HashMap<>();
    public HashMap<AsmReg, AsmReg> alias = new HashMap<>();
    public HashMap<AsmReg, Integer> color = new HashMap<>();
    public HashSet<AsmReg> spillTemp = new HashSet<>();

    public PremAllocator(AsmFile aa) {
        this.asmF = aa;
    }

    public void work() {
        for (var func : asmF.fun)
            workOnFunc(func);
    }

    LinkedList<AsmInstruction> newInsts;
    AsmFunction curFunc;

    void workOnFunc(AsmFunction func) {
        curFunc = func;
        spillTemp.clear();
        while (true) {
            new LiveAnalyze(func).work();
            initAll(func);
            build(func);
            makeWorkList();
            do {
                if (!simplifyWorkList.isEmpty())
                    stepSimplify();
                else if (!workListMoves.isEmpty())
                    stepCoalesce();
                else if (!freezeWorkList.isEmpty())
                    stepFreeze();
                else if (!spillWorkList.isEmpty())
                    stepSelectSpill();
            } while (!simplifyWorkList.isEmpty() || !workListMoves.isEmpty() ||
                    !freezeWorkList.isEmpty() || !spillWorkList.isEmpty());
            assignColors();
            if (spilledNodes.isEmpty())
                break;
            rewriteProgram(func);
        }

        for (var block : func.block) {
            newInsts = new LinkedList<>();
            for (AsmInstruction inst : block.insts) {
                if (inst instanceof AsmLi && ((AsmLi) inst).imm instanceof AsmStackImm)
                    ((AsmStackImm) ((AsmLi) inst).imm).cal();
                if (inst.rd instanceof AsmVirtualReg)
                    inst.rd = AsmRealReg.idR.get(color.get(inst.rd));
                if (inst.rs1 instanceof AsmVirtualReg)
                    inst.rs1 = AsmRealReg.idR.get(color.get(inst.rs1));
                if (inst.rs2 instanceof AsmVirtualReg)
                    inst.rs2 = AsmRealReg.idR.get(color.get(inst.rs2));
                if (!(inst instanceof AsmMv) || inst.rd != inst.rs1)
                    newInsts.add(inst);
            }
            block.insts = newInsts;
        }
    }

    void addEdge(AsmReg u, AsmReg v) {
        Edge e = new Edge(u, v);
        if (u == v || adjSet.contains(e))
            return;
        adjSet.add(e);
        if (!preColored.contains(u)) {
            adjList.get(u).add(v);
            degree.put(u, degree.get(u) + 1);
        }
        if (!preColored.contains(v)) {
            adjList.get(v).add(u);
            degree.put(v, degree.get(v) + 1);
        }
    }

    void initAll(AsmFunction func) {
        preColored.clear();
        initial.clear();
        simplifyWorkList.clear();
        freezeWorkList.clear();
        spillWorkList.clear();
        spilledNodes.clear();
        coalescedNodes.clear();
        coloredNodes.clear();
        selectStack.clear();

        coalescedMoves.clear();
        constrainedMoves.clear();
        frozenMoves.clear();
        workListMoves.clear();
        activeMoves.clear();

        adjSet.clear();
        adjList.clear();
        degree.clear();
        moveList.clear();
        alias.clear();
        color.clear();

        for (var reg : AsmRealReg.regMap.values()) {
            preColored.add(reg);
            adjList.put(reg, new HashSet<>());
            degree.put(reg, Integer.MAX_VALUE);
            moveList.put(reg, new HashSet<>());
            alias.put(reg, null);
            color.put(reg, reg.id);
            reg.weight = 0;
        }
        for (var block : func.block)
            for (var inst : block.insts) {
                initial.addAll(inst.getD());
                initial.addAll(inst.getU());
            }
        initial.removeAll(preColored);
        for (var reg : initial) {
            adjList.put(reg, new HashSet<>());
            degree.put(reg, 0);
            moveList.put(reg, new HashSet<>());
            alias.put(reg, null);
            color.put(reg, null);
            reg.weight = 0;
        }

        for (var block : func.block) {
            double weight = Math.pow(10, block.depth);
            for (var inst : block.insts) {
                for (var reg : inst.getD())
                    reg.weight += weight;
                for (var reg : inst.getU())
                    reg.weight += weight;
            }
        }
    }

    void build(AsmFunction func) {
        for (var block : func.block) {
            LinkedHashSet<AsmReg> live = new LinkedHashSet<>(block.out);
            for (int i = block.insts.size() - 1; i >= 0; i--) {
                AsmInstruction inst = block.insts.get(i);
                if (inst instanceof AsmMv) {
                    live.removeAll(inst.getU());
                    for (var reg : inst.getD())
                        moveList.get(reg).add((AsmMv) inst);
                    for (var reg : inst.getU())
                        moveList.get(reg).add((AsmMv) inst);
                    workListMoves.add((AsmMv) inst);
                }
                live.addAll(inst.getD());
                for (AsmReg def : inst.getD())
                    for (var l : live)
                        addEdge(def, l);
                live.removeAll(inst.getD());
                live.addAll(inst.getU());
            }
        }
    }

    LinkedHashSet<AsmMv> nodeMoves(AsmReg reg) {
        LinkedHashSet<AsmMv> ret = new LinkedHashSet<>(activeMoves);
        ret.addAll(workListMoves);
        ret.retainAll(moveList.get(reg));
        return ret;
    }

    boolean moveRelated(AsmReg reg) {
        return nodeMoves(reg).size() > 0;
    }

    void makeWorkList() {
        for (var reg : initial) {
            if (degree.get(reg) >= kCnt)
                spillWorkList.add(reg);
            else if (moveRelated(reg))
                freezeWorkList.add(reg);
            else
                simplifyWorkList.add(reg);
        }
        initial.clear();
    }

    LinkedHashSet<AsmReg> adjacent(AsmReg reg) {
        LinkedHashSet<AsmReg> ret = new LinkedHashSet<>(adjList.get(reg));
        ret.removeAll(selectStack);
        ret.removeAll(coalescedNodes);
        return ret;
    }

    void decrementDegree(AsmReg reg) {
        int d = degree.get(reg);
        degree.put(reg, d - 1);
        if (d == kCnt) {
            LinkedHashSet<AsmReg> nodes = adjacent(reg);
            nodes.add(reg);
            enableMoves(nodes);
            spillWorkList.remove(reg);
            if (moveRelated(reg))
                freezeWorkList.add(reg);
            else
                simplifyWorkList.add(reg);
        }
    }

    void enableMoves(LinkedHashSet<AsmReg> nodes) {
        for (var reg : nodes) {
            var nodeMoves = nodeMoves(reg);
            for (var mv : nodeMoves)
                if (activeMoves.contains(mv)) {
                    activeMoves.remove(mv);
                    workListMoves.add(mv);
                }
        }
    }

    void stepSimplify() {
        while (!simplifyWorkList.isEmpty()) {
            AsmReg reg = simplifyWorkList.removeFirst();
            selectStack.push(reg);
            for (var adj : adjacent(reg))
                decrementDegree(adj);
        }
    }

    AsmReg getAlias(AsmReg reg) {
        if (coalescedNodes.contains(reg)) {
            AsmReg regAlias = getAlias(alias.get(reg));
            alias.put(reg, regAlias);
            return regAlias;
        } else
            return reg;
    }

    void addWorkList(AsmReg reg) {
        if (!preColored.contains(reg) && !moveRelated(reg) && degree.get(reg) < kCnt) {
            freezeWorkList.remove(reg);
            simplifyWorkList.add(reg);
        }
    }

    boolean George(AsmReg t, AsmReg r) {
        return degree.get(t) < kCnt || preColored.contains(t) || adjSet.contains(new Edge(t, r));
    }

    boolean Briggs(LinkedHashSet<AsmReg> uv) {
        int k = 0;
        for (AsmReg reg : uv)
            if (degree.get(reg) >= kCnt)
                k++;
        return k < kCnt;
    }

    void combine(AsmReg u, AsmReg v) {
        if (freezeWorkList.contains(v))
            freezeWorkList.remove(v);
        else
            spillWorkList.remove(v);
        coalescedNodes.add(v);
        alias.put(v, u);
        moveList.get(u).addAll(moveList.get(v));
        enableMoves(new LinkedHashSet<AsmReg>() {{ add(v); }});
        for (var t : adjacent(v)) {
            addEdge(t, u);
            decrementDegree(t);
        }
        if (degree.get(u) >= kCnt && freezeWorkList.contains(u)) {
            freezeWorkList.remove(u);
            spillWorkList.add(u);
        }
    }

    void stepCoalesce() {
        AsmMv mv = workListMoves.iterator().next();
        AsmReg x = getAlias(mv.rd), y = getAlias(mv.rs1);
        Edge e = preColored.contains(y) ? new Edge(y, x) : new Edge(x, y);
        workListMoves.remove(mv);
        if (e.u == e.v) {
            coalescedMoves.add(mv);
            addWorkList(e.u);
        } else if (preColored.contains(e.v) || adjSet.contains(e) || e.u == AsmRealReg.get("zero") || e.v == AsmRealReg.get("zero")) {

            constrainedMoves.add(mv);
            addWorkList(e.u);
            addWorkList(e.v);
        } else {
            boolean flag = true;
            for (AsmReg reg : adjacent(e.v))
                flag &= George(reg, e.u);
            LinkedHashSet<AsmReg> uv = new LinkedHashSet<>(adjacent(e.u));
            uv.addAll(adjacent(e.v));
            if (preColored.contains(e.u) && flag || !preColored.contains(e.u) && Briggs(uv)) {
                coalescedMoves.add(mv);
                combine(e.u, e.v);
                addWorkList(e.u);
            } else {
                activeMoves.add(mv);
            }
        }
    }

    void freezeMoves(AsmReg reg) {
        for (var mv : nodeMoves(reg)) {
            AsmReg x = mv.rd, y = mv.rs1, v;
            v = getAlias(y) == getAlias(reg) ? getAlias(x) : getAlias(y);
            activeMoves.remove(mv);
            frozenMoves.add(mv);
            if (nodeMoves(v).isEmpty() && degree.get(v) < kCnt) {
                // then v is not move related anymore
                freezeWorkList.remove(v);
                simplifyWorkList.add(v);
            }
        }
    }

    void stepFreeze() {
        AsmReg reg = freezeWorkList.getFirst();
        freezeWorkList.remove(reg);
        simplifyWorkList.add(reg);
        freezeMoves(reg);
    }

    void stepSelectSpill() {
        AsmReg m = null;
        for (AsmReg reg : spillWorkList)
            if (m == null || reg.weight / degree.get(reg) < m.weight / degree.get(m) && !spillTemp.contains(reg))
                m = reg;
        spillWorkList.remove(m);
        simplifyWorkList.add(m);
        freezeMoves(m);
    }

    void assignColors() {
        while (!selectStack.isEmpty()) {
            AsmReg reg = selectStack.pop();
            LinkedHashSet<Integer> okColors = new LinkedHashSet<>();
            for (int i = 5; i < 32; i++)
                okColors.add(i);
            for (var adj : adjList.get(reg)) {
                AsmReg adjAlias = getAlias(adj);
                if (coloredNodes.contains(adjAlias) || preColored.contains(adjAlias))
                    okColors.remove(color.get(adjAlias));
            }
            if (okColors.isEmpty())
                spilledNodes.add(reg);
            else {
                coloredNodes.add(reg);
                color.put(reg, okColors.iterator().next());
            }
        }
        for (AsmReg reg : coalescedNodes)
            color.put(reg, color.get(getAlias(reg)));
    }

    void rewriteProgram(AsmFunction func) {
        for (AsmReg reg : spilledNodes) {
            ((AsmVirtualReg) reg).off = func.paraU + func.alloca + func.spillU;
            func.spillU += 4;
        }

        for (var block : func.block) {
            newInsts = new LinkedList<>();
            for (AsmInstruction inst : block.insts) {
                AsmVirtualReg same = null;
                if (inst.rs1 != null && inst.rs1.off != null) {
                    AsmVirtualReg newReg = new AsmVirtualReg(4);
                    spillTemp.add(newReg);
                    allocateUse(newReg, (AsmVirtualReg) inst.rs1);
                    if (inst.rs1 == inst.rs2)
                        inst.rs2 = newReg;
                    if (inst.rs1 == inst.rd)
                        same = newReg;
                    inst.rs1 = newReg;
                }
                if (inst.rs2 != null && inst.rs2.off != null) {
                    AsmVirtualReg newReg = new AsmVirtualReg(4);
                    spillTemp.add(newReg);
                    allocateUse(newReg, (AsmVirtualReg) inst.rs2);
                    if (inst.rs2 == inst.rd)
                        same = newReg;
                    inst.rs2 = newReg;
                }
                newInsts.add(inst);
                if (inst.rd != null && inst.rd.off != null) {
                    AsmVirtualReg newReg = same == null ? new AsmVirtualReg(4) : same;
                    spillTemp.add(newReg);
                    allocateDef(newReg, (AsmVirtualReg) inst.rd);
                    inst.rd = newReg;
                }
            }
            block.insts = newInsts;
        }
    }

    void allocateUse(AsmVirtualReg newReg, AsmVirtualReg reg) {
        if (reg.off < 1 << 11)
            newInsts.add(new AsmLoad(reg.size, newReg, RegSp, new Immediate(reg.off)));
        else {
            newInsts.add(new AsmLi(newReg, new AsmVirtualImm(reg.off)));
            newInsts.add(new AsmBinary("add", newReg, newReg, RegSp));
            newInsts.add(new AsmLoad(reg.size, newReg, newReg));
        }
    }

    void allocateDef(AsmVirtualReg newReg, AsmVirtualReg reg) {
        if (reg.off < 1 << 11)
            newInsts.add(new AsmStore(reg.size, RegSp, newReg, new Immediate(reg.off)));
        else {
            AsmVirtualReg addr = new AsmVirtualReg(4);
            spillTemp.add(addr);
            newInsts.add(new AsmLi(addr, new AsmVirtualImm(reg.off)));
            newInsts.add(new AsmBinary("add", addr, addr, RegSp));
            newInsts.add(new AsmStore(reg.size, addr, newReg));
        }
    }
}