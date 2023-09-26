package src.codegen;

import src.IR.basic.*;
import src.IR.instructments.*;
import src.IR.irtype.IRConst;
import src.IR.irtype.IRIntConst;
import src.asm.basic.*;
import src.asm.instructions.*;
import src.ast.BuiltIn;

import java.util.HashMap;

public class InstSelector implements IRVisitor, BuiltIn {
    AsmFile fileA;
    AsmFunction curFunc;
    AsmBlock curBlock;
    int blockCnt = 0;

    HashMap<IRBlock, AsmBlock> blockMap = new HashMap<>();

    public InstSelector(AsmFile mm) {
        this.fileA = mm;
    }

    public void visit(IRFileAnalyze node) {
        node.var.forEach(gg -> {
            gg.reg = new AsmVar(gg);
            fileA.var.add((AsmVar) gg.reg);
        });
        node.string.values().forEach(str -> {
            AsmString gs = new AsmString(".str." + (str.num), str.value);
            fileA.str.add(gs);
            str.reg = gs;
        });
        node.fuc.forEach(func -> {
            curFunc = new AsmFunction(func.name);
            fileA.fun.add(curFunc);
            func.accept(this);
        });
    }

    public void visit(IRFunction node) {
        AsmVirtualReg.cnt = 0;
        int MaxC = 0;
        for (IRBlock blk : node.blocks) {
            blockMap.put(blk, new AsmBlock(".L" + blockCnt++, blk.depth));
            for (IROrders inst : blk.insts)
                if (inst instanceof IRCall)
                    MaxC = Math.max(MaxC, ((IRCall) inst).args.size());
        }
        curFunc.paraU = (MaxC > 8 ? MaxC - 8 : 0) << 2;
        for (int i = 0; i < node.para.size() && i < 8; ++i)
            node.para.get(i).reg = new AsmVirtualReg(node.para.get(i).type.size);
        for (int i = 0; i < node.blocks.size(); ++i) {
            curBlock = blockMap.get(node.blocks.get(i));
            if (i == 0)
                storeReg(4, AsmRealReg.regMap.get("ra"), AsmRealReg.regMap.get("sp"), curFunc.paraU);
            node.blocks.get(i).accept(this);
            curFunc.add(curBlock);
        }
        AsmBlock enB = curFunc.block.get(0);
        AsmBlock exB = curFunc.block.get(curFunc.block.size() - 1);
        for (int i = 0; i < node.para.size() && i < 8; i++) {
            curFunc.entry.insts.addFirst(new AsmMv(node.para.get(i).reg, AsmRealReg.get("a" + i)));
        }

        if (!node.name.equals("main")) {
            for (var rr : AsmRealReg.callee) {
                AsmVirtualReg st = new AsmVirtualReg(4);
                curFunc.entry.insts.addFirst(new AsmMv(st, rr));
                curFunc.exit.insts.addLast(new AsmMv(rr, st));
            }
        }
        curFunc.regCnt = AsmVirtualReg.cnt;
        for (var bb : curFunc.block) {
            bb.insts.addAll(bb.phi);
            bb.insts.addAll(bb.jj);
        }
    }

    public void visit(IRBlock node) {
        for (var in : node.insts) {
            if (in != node.insts.getLast()) in.accept(this);
        }
        if (node.ter instanceof IRBranch bb && node.insts.getLast() instanceof IRIcmp cc && bb.getU().contains(cc.reg)) {
            combine(cc, bb);
        } else {
            if (!node.insts.isEmpty()) node.insts.getLast().accept(this);
            node.ter.accept(this);
        }
    }

    public void visit(IRAlloca node) {
        if (node.index < 8) {
            int off = curFunc.paraU + curFunc.alloca;
            if (off < 1 << 11)
                curBlock.add(new AsmUnary("addi", getReg(node.alloca), AsmRealReg.get("sp"), new Immediate(off)));
            else
                curBlock.add(new AsmBinary("add", getReg(node.alloca), AsmRealReg.get("sp"), immTo(new AsmVirtualImm(off))));
            curFunc.alloca += 4;
        } else {
            AsmVirtualReg rr = new AsmVirtualReg(4);
            curBlock.add(new AsmLi(rr, new AsmStackImm(curFunc, node.index - 8 << 2)));
            curBlock.add(new AsmBinary("add", getReg(node.alloca), AsmRealReg.get("sp"), rr));
        }
    }

    public void visit(IRBranch node) {
        curBlock.add(new AsmBeq(getReg(node.cond), blockMap.get(node.falseB)));
        curBlock.succ.add(blockMap.get(node.falseB));
        blockMap.get(node.falseB).pred.add(curBlock);
        curBlock.add(new AsmJump(blockMap.get(node.trueB)));
        curBlock.succ.add(blockMap.get(node.trueB));
        blockMap.get(node.trueB).pred.add(curBlock);
    }

    public void visit(IRCalc node) {
        switch (node.op) {
            case "add":
            case "and":
            case "or":
            case "xor":
                if (node.ll instanceof IRIntConst) {
                    IRBasic tmp = node.ll;
                    node.ll = node.rr;
                    node.rr = tmp;
                }
            case "shl":
            case "ashr":
                if (node.rr instanceof IRIntConst intConst && intConst.value < 1 << 11 && intConst.value >= -(1 << 11))
                    curBlock.add(new AsmUnary(node.op + "i", getReg(node.res), getReg(node.ll), new Immediate(intConst.value)));
                else
                    curBlock.add(new AsmBinary(node.op, getReg(node.res), getReg(node.ll), getReg(node.rr)));
                break;
            case "sub":
                if (node.rr instanceof IRIntConst intConst && intConst.value <= 1 << 11 && intConst.value > -(1 << 11))
                    curBlock.add(new AsmUnary("addi", getReg(node.res), getReg(node.ll), new Immediate(-intConst.value)));
                else
                    curBlock.add(new AsmBinary(node.op, getReg(node.res), getReg(node.ll), getReg(node.rr)));
                break;
            case "mul":
                if (node.ll instanceof IRIntConst intConst && log2.containsKey(intConst.value)) {
                    IRBasic tmp = node.ll;
                    node.ll = node.rr;
                    node.rr = tmp;
                }
                if (node.rr instanceof IRIntConst intConst && log2.containsKey(intConst.value))
                    curBlock.add(new AsmUnary("slli", getReg(node.res), getReg(node.ll), new Immediate(log2.get(intConst.value))));
                else
                    curBlock.add(new AsmBinary(node.op, getReg(node.res), getReg(node.ll), getReg(node.rr)));
                break;
            case "sdiv":
                if (node.rr instanceof IRIntConst intConst && log2.containsKey(intConst.value))
                    curBlock.add(new AsmUnary("srai", getReg(node.res), getReg(node.ll), new Immediate(log2.get(intConst.value))));
                else
                    curBlock.add(new AsmBinary(node.op, getReg(node.res), getReg(node.ll), getReg(node.rr)));
                break;
            default:
                curBlock.add(new AsmBinary(node.op, getReg(node.res), getReg(node.ll), getReg(node.rr)));
        }
    }

    public void visit(IRCall node) {
       AsmCall call = new AsmCall(node.name);
       for(int i=0;i<node.args.size();i++) {
           IRBasic aa = node.args.get(i);
           if(i<8) {
               curBlock.add(new AsmMv(AsmRealReg.get("a"+i),getReg(aa)));
               call.addU(AsmRealReg.get("a"+i));
           } else storeReg(aa.type.size,getReg(aa),AsmRealReg.get("sp"),i-8<<2);
       }
       curBlock.add(call);
       if(node.call!=null) curBlock.add(new AsmMv(getReg(node.call),AsmRealReg.get("a0")));
    }

    public void visit(IRCast node) {
        curBlock.add(new AsmMv(getReg(node.dest), getReg(node.value)));
    }

    public void visit(IRGetelementptr node) {
        if (node.type == irBool) {
            curBlock.add(new AsmBinary("add", getReg(node.res), getReg(node.ptr), getReg(node.index.get(0))));
        } else {
            AsmReg idx = node.type instanceof IRStruct ? getReg(node.index.get(1)) : getReg(node.index.get(0));
            AsmVirtualReg tmp = new AsmVirtualReg(4);
            if(idx == AsmRealReg.get("zero")) {
                curBlock.add(new AsmMv(getReg(node.res),getReg(node.ptr)));
            } else {
                curBlock.add(new AsmUnary("slli", tmp, idx, new Immediate(2)));
                curBlock.add(new AsmBinary("add", getReg(node.res), getReg(node.ptr), tmp));
            }
        }
    }

    public void visit(IRIcmp node) {
        AsmVirtualReg tmp = new AsmVirtualReg(4);
        switch (node.op) {
            case "eq" -> {
                curBlock.add(new AsmBinary("sub", tmp, getReg(node.ll), getReg(node.rr)));
                curBlock.add(new AsmUnary("seqz", getReg(node.reg), tmp));
            }
            case "ne" -> {
                curBlock.add(new AsmBinary("sub", tmp, getReg(node.ll), getReg(node.rr)));
                curBlock.add(new AsmUnary("snez", getReg(node.reg), tmp));
            }
            case "sgt" -> curBlock.add(new AsmBinary("slt", getReg(node.reg), getReg(node.rr), getReg(node.ll)));
            case "sge" -> {
                curBlock.add(new AsmBinary("slt", tmp, getReg(node.ll), getReg(node.rr)));
                curBlock.add(new AsmUnary("xori", getReg(node.reg), tmp, new Immediate(1)));
            }
            case "slt" -> curBlock.add(new AsmBinary("slt", getReg(node.reg), getReg(node.ll), getReg(node.rr)));
            case "sle" -> {
                curBlock.add(new AsmBinary("slt", tmp, getReg(node.rr), getReg(node.ll)));
                curBlock.add(new AsmUnary("xori", getReg(node.reg), tmp, new Immediate(1)));
            }
        }
    }

    public void visit(IRJump node) {
        curBlock.add(new AsmJump(blockMap.get(node.block)));
        curBlock.succ.add(blockMap.get(node.block));
        blockMap.get(node.block).pred.add(curBlock);
    }

    public void visit(IRLoad node) {
        if(node.src.reg instanceof Global gg) {
            String na = gg.name;
            AsmReg rr = getReg(node.reg);
            curBlock.add(new AsmLui(rr,new HiLoFun(HiLoFun.Type.hi,na)));
            curBlock.add(new AsmLoad(node.type.size,rr,rr,new HiLoFun(HiLoFun.Type.lo,na)));
        }
        loadReg(node.type.size, getReg(node.reg), getReg(node.src), 0);
    }

    public void visit(IRRet node) {
        if (node.value != irVoidConst)
            curBlock.add(new AsmMv(AsmRealReg.regMap.get("a0"), getReg(node.value)));
        loadReg(4, AsmRealReg.regMap.get("ra"), AsmRealReg.regMap.get("sp"), curFunc.paraU);
    }

    public void visit(IRStore node) {
        if(node.index>=8) return;
        if(node.dest.reg instanceof Global gg) {
            String na = gg.name;
            AsmVirtualReg rr = new AsmVirtualReg(4);
            curBlock.add(new AsmLui(rr,new HiLoFun(HiLoFun.Type.hi,na)));
            curBlock.add(new AsmStore(node.value.type.size,rr,getReg(node.value),new HiLoFun(HiLoFun.Type.lo,na)));
        } else {
            storeReg(node.value.type.size, getReg(node.value), getReg(node.dest), 0);
        }

    }

    public void visit(IRPhi node) {
        AsmVirtualReg tt = new AsmVirtualReg(node.dest.type.size);
        curBlock.add(new AsmMv(getReg(node.dest), tt));
        for (int i = 0; i < node.vv.size(); i++) {
            IRBasic vv = node.vv.get(i);
            if (vv instanceof IRConst cc) {
                blockMap.get(node.bb.get(i)).phi.add(new AsmLi(tt,new AsmVirtualImm(cc)));
            } else {
                blockMap.get(node.bb.get(i)).phi.add(new AsmMv(tt,getReg(node.vv.get(i))));
            }
        }
    }

    AsmReg getReg(IRBasic bb) {
        if (bb.reg == null) {
            if (bb instanceof IRRegister) {
                bb.reg = new AsmVirtualReg(bb.type.size);
            } else if (bb instanceof IRConst) {
                return ((IRConst) bb).is0() ? AsmRealReg.get("zero") : immTo(new AsmVirtualImm((IRConst) bb));
            }
        } else if (bb.reg instanceof Global) {
            AsmVirtualReg rr = new AsmVirtualReg(4);
            String na = ((Global) bb.reg).name;
            curBlock.add(new AsmLui(rr, new HiLoFun(HiLoFun.Type.hi, na)));
            curBlock.add(new AsmUnary("addi", rr, rr, new HiLoFun(HiLoFun.Type.lo, na)));
            return rr;
        }
        return bb.reg;
    }

    void storeReg(int size, AsmReg vv, AsmReg dest, int off) {
        if (off < 1 << 11) {
            curBlock.add(new AsmStore(size, dest, vv, new Immediate(off)));
        } else {
            AsmVirtualReg tt = new AsmVirtualReg(4);
            curBlock.add(new AsmBinary("add", tt, dest, immTo(new AsmVirtualImm(off))));
            curBlock.add(new AsmStore(size, tt, vv));
        }
    }

    void loadReg(int size, AsmReg dest, AsmReg src, int off) {
        if (off < 1 << 11) {
            curBlock.add(new AsmLoad(size, dest, src, new Immediate(off)));
        } else {
            AsmVirtualReg tt = new AsmVirtualReg(4);
            curBlock.add(new AsmBinary("add", tt, dest, immTo(new AsmVirtualImm(off))));
            curBlock.add(new AsmLoad(size, dest, tt));
        }
    }

    AsmReg immTo(AsmVirtualImm ii) {
        AsmVirtualReg rr = new AsmVirtualReg(4);
        curBlock.add(new AsmLi(rr, ii));
        return rr;
    }

    static HashMap<Integer, Integer> log2 = new HashMap<>() {
        {
            for (int i = 0; i < 31; i++) {
                put(1 << i, i);
            }
        }
    };

    void combine(IRIcmp cc, IRBranch bb) {
        String op = "";
        switch (cc.op) {
            case "eq" -> op = "bne";
            case "ne" -> op = "beq";
            case "sgt" -> op = "ble";
            case "sge" -> op = "blt";
            case "slt" -> op = "bge";
            case "sle" -> op = "bgt";
        }
        curBlock.add(new AsmBr(op, getReg(cc.ll), getReg(cc.rr), blockMap.get(bb.falseB)));
        curBlock.succ.add(blockMap.get(bb.falseB));
        blockMap.get(bb.falseB).pred.add(curBlock);
        curBlock.add(new AsmJump(blockMap.get(bb.trueB)));
        curBlock.succ.add(blockMap.get(bb.trueB));
        blockMap.get(bb.trueB).pred.add(curBlock);
    }
}
