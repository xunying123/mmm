package src.optmize;

import src.IR.basic.*;
import src.IR.instructments.*;
import src.IR.irtype.IRBoolConst;
import src.IR.irtype.IRCondConst;
import src.IR.irtype.IRConst;
import src.ast.BuiltIn;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class ConstPropagation implements BuiltIn {
    IRFileAnalyze program;

    HashMap<IRRegister, HashSet<IROrders>> useList = new HashMap<>();
    LinkedList<IROrders> workList = new LinkedList<>();
    HashSet<IROrders> inWorkList = new HashSet<>();
    IRFunction curFunc;

    public ConstPropagation(IRFileAnalyze program) {
        this.program = program;
    }

    public void work() {
        program.fuc.forEach(this::workOnFunc);
    }

    void workOnFunc(IRFunction func) {
        useList = func.list;
        workList.clear();
        curFunc = func;
        for (var block : func.blocks) {
            for (var inst : block.insts)
                if (canBeReplaced(inst) != null) {
                    workList.add(inst);
                    inWorkList.add(inst);
                }
            workList.add(block.ter);
            inWorkList.add(block.ter);
        }

        while (!workList.isEmpty()) {
            IROrders inst = workList.removeFirst();
            inWorkList.remove(inst);
            if (inst.delete)
                continue;
            IRBasic c = canBeReplaced(inst), def = inst.getD();
            if (c != null) {
                inst.delete = true;
                for (var use : useList.get(def)) {
                    use.replaceU(def, c);
                    if (c instanceof IRRegister reg)
                        useList.get(reg).add(use);
                    if (!inWorkList.contains(use)) {
                        workList.add(use);
                        inWorkList.add(use);
                    }
                }
            } else if (inst instanceof IRBranch brInst && brInst.cond instanceof IRCondConst cond) {
                IRBlock atBlock = brInst.parent;
                IRBlock toBlock = cond.value ? brInst.trueB : brInst.falseB;
                IRBlock deleteBlock = cond.value ? brInst.falseB : brInst.trueB;
                atBlock.ter = new IRJump(atBlock, toBlock);
                atBlock.succ.remove(deleteBlock);
                deleteBlock.pred.remove(atBlock);
                for (var phiInst : deleteBlock.insts) {
                    if (!(phiInst instanceof IRPhi phi))
                        break;
                    boolean found = false;
                    for (int i = 0; i < phi.vv.size(); ++i)
                        if (phi.bb.get(i) == atBlock) {
                            phi.bb.remove(i);
                            phi.vv.remove(i);
                            found = true;
                            break;
                        }
                    if (found && !inWorkList.contains(phiInst)) {
                        workList.add(phiInst);
                        inWorkList.add(phiInst);
                    }
                }
                if (deleteBlock.pred.size() == 0)
                    deleteBlock(deleteBlock);
            }
        }

        for (var block : func.blocks)
            block.insts.removeIf(inst -> inst.delete);
    }

    void deleteBlock(IRBlock block) {
        curFunc.blocks.remove(block);
        for (var inst : block.insts) {
            inst.delete = true;
            for (var use : inst.getU())
                if (useList.get(use) != null)
                    useList.get(use).remove(inst);
        }
        block.ter.delete = true;
        for (var use : block.ter.getU())
            useList.get(use).remove(block.ter);
        for (var succ : block.succ) {
            succ.pred.remove(block);
            for (var phiInst : succ.insts) {
                if (!(phiInst instanceof IRPhi phi))
                    break;
                boolean found = false;
                for (int i = 0; i < phi.vv.size(); ++i)
                    if (phi.bb.get(i) == block) {
                        phi.bb.remove(i);
                        phi.vv.remove(i);
                        found = true;
                        break;
                    }
                if (found && !inWorkList.contains(phiInst)) {
                    workList.add(phiInst);
                    inWorkList.add(phiInst);
                }
            }
            if (succ.pred.size() == 0)
                deleteBlock(succ);
        }
    }

    IRBasic canBeReplaced(IROrders inst) {
        if (inst instanceof IRPhi phiInst) {
            if (phiInst.vv.size() == 1)
                return phiInst.vv.get(0);
            else if (phiInst.vv.get(0) instanceof IRConst constVal) {
                for (int i = 1; i < phiInst.vv.size(); ++i)
                    if (!(phiInst.vv.get(i) instanceof IRConst other) || !constVal.equals(other))
                        return null;
                return constVal;
            }
            return null;
        }
        if (inst instanceof IRZext castInst) {
            if (castInst.value instanceof IRCondConst condConst)
                return condConst.value ? irBoolTrue : irBoolFalse;
            return null;
        }
        if (inst instanceof IRTrunc castInst) {
            if (castInst.value instanceof IRBoolConst condConst)
                return condConst.value ? irBoolTrue : irBoolFalse;
            return null;
        }
        if (inst instanceof IRCalc calcInst)
            return calcInst.calcConst();
        if (inst instanceof IRIcmp icmpInst)
            return icmpInst.calc();
        return null;
    }
}
