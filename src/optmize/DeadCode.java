package src.optmize;

import src.IR.basic.IRFileAnalyze;
import src.IR.basic.IRFunction;
import src.IR.basic.IRRegister;
import src.IR.instructments.IRCall;
import src.IR.instructments.IROrders;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class DeadCode {
    IRFileAnalyze irF;

    HashMap<IRRegister, HashSet<IROrders>> useList;
    HashMap<IRRegister, IROrders> defList = new HashMap<>();
    LinkedList<IRRegister> workList = new LinkedList<>();
    HashSet<IRRegister> inWorkList = new HashSet<>();

    public DeadCode(IRFileAnalyze program) {
        this.irF = program;
    }

    public void work() {
        irF.fuc.forEach(func -> workOnFunc(func));
    }

    void workOnFunc(IRFunction func) {
        useList = new HashMap<>();
        for (var block : func.blocks) {
            for (var inst : block.insts) {
                if (inst.getD() != null) {
                    defList.put(inst.getD(), inst);
                    workList.add(inst.getD());
                    inWorkList.add(inst.getD());
                }
                for (var use : inst.getU())
                    if (use instanceof IRRegister reg)
                        useList.computeIfAbsent(reg, k -> new HashSet<>()).add(inst);
            }
            var inst = block.ter;
            for (var use : inst.getU())
                if (use instanceof IRRegister reg)
                    useList.computeIfAbsent(reg, k -> new HashSet<>()).add(inst);
        }
        while (!workList.isEmpty()) {
            IRRegister reg = workList.removeFirst();
            inWorkList.remove(reg);
            if (useList.get(reg) == null || useList.get(reg).isEmpty()) {
                IROrders inst = defList.get(reg);
                if (inst instanceof IRCall || inst == null)
                    continue; // call inst has side effect
                inst.delete = true;
                for (var use : inst.getU())
                    if (use instanceof IRRegister useReg) {
                        useList.get(useReg).remove(inst);
                        if (!inWorkList.contains(useReg)) {
                            workList.add(useReg);
                            inWorkList.add(useReg);
                        }
                    }
            }
            func.list = useList;
        }

        for (var block : func.blocks)
            block.insts.removeIf(inst -> inst.delete);
    }
}
