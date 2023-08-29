package src.codegen;

import src.IR.basic.*;
import src.IR.instructments.*;
import src.IR.irtype.IRConst;
import src.asm.basic.*;
import src.asm.instructions.*;
import src.ast.BuiltIn;

import java.util.HashMap;

public class InstSelector implements IRVisitor, BuiltIn {
    AsmFile module;
    AsmFunction curFunc;
    AsmBlock curBlock;
    int blockCnt = 0;

    HashMap<IRBlock, AsmBlock> blockMap = new HashMap<>();

    public InstSelector(AsmFile module) {
        this.module = module;
    }

    AsmReg getReg(IRBasic entity) {
        if (entity.reg == null) {
            if (entity instanceof IRRegister) {
                entity.reg = new AsmVirtualReg(entity.type.size);
            } else if (entity instanceof IRConst) {
                entity.reg = new AsmVirtualImm((IRConst) entity);
            }
        }
        return entity.reg;
    }

    void storeReg(int size, AsmReg value, AsmReg dest, int offset) {
        if(offset<1<<11) {
            curBlock.add(new AsmStore(size,dest,value,new Immediate(offset)));
        } else {
            AsmVirtualReg tmp = new AsmVirtualReg(4);
            curBlock.add(new AsmBinary("add", tmp, dest, new AsmVirtualImm(offset)));
            curBlock.add(new AsmStore(size, tmp, value));
        }
    }

    void loadReg(int size, AsmReg dest, AsmReg src, int offset) {
        if(offset<1<<11) {
            curBlock.add(new AsmLoad(size,dest,src,new Immediate(offset)));
        } else {
            AsmVirtualReg tmp = new AsmVirtualReg(4);
            curBlock.add(new AsmBinary("add", tmp, src, new AsmVirtualImm(offset)));
            curBlock.add(new AsmLoad(size, dest, tmp));
        }
    }

    public void visit(IRFileAnalyze node) {
        node.var.forEach(globalVar -> {
            globalVar.reg = new AsmVar(globalVar);
            module.var.add((AsmVar) globalVar.reg);
        });
        node.string.values().forEach(str -> {
            AsmString globalStr = new AsmString(".str." + String.valueOf(str.num), str.value);
            module.str.add(globalStr);
            str.reg = globalStr;
        });
        if (node.initFunc != null) {
            curFunc = new AsmFunction(node.initFunc.name);
            module.fun.add(curFunc);
            node.initFunc.accept(this);
        }
        node.fuc.forEach(func -> {
            curFunc = new AsmFunction(func.name);
            module.fun.add(curFunc);
            func.accept(this);
        });
    }

    public void visit(IRFunction node) {
        blockMap.clear();
        AsmVirtualReg.cnt = 0;
        int maxArgCnt = 0;
        for (IRBlock blk : node.blocks) {
            blockMap.put(blk, new AsmBlock(".L" + blockCnt++));
            for (IROrders inst : blk.insts)
                if (inst instanceof IRCall)
                    maxArgCnt = Math.max(maxArgCnt, ((IRCall) inst).args.size());
        }
        curFunc.paraU = (maxArgCnt > 8 ? maxArgCnt - 8 : 0) << 2;
        for (int i = 0; i < node.para.size(); ++i)
            if (i < 8)
                node.para.get(i).reg = AsmRealReg.regMap.get("a" + i);
            else
                node.para.get(i).reg = new AsmVirtualReg(i);

        for (int i = 0; i < node.blocks.size(); ++i) {
            curBlock = blockMap.get(node.blocks.get(i));
            if (i == 0)
                storeReg(4, AsmRealReg.regMap.get("ra"), AsmRealReg.regMap.get("sp"), curFunc.paraU);
            node.blocks.get(i).accept(this);
            curFunc.add(curBlock);
        }
        curFunc.regCnt = AsmVirtualReg.cnt;
        curFunc.all = curFunc.paraU + curFunc.alloca + curFunc.regCnt * 4;

        AsmBlock entryBlock = curFunc.block.get(0), exitBlock = curFunc.block.get(curFunc.block.size() - 1);
        entryBlock.insts.addFirst(new AsmBinary("add", AsmRealReg.regMap.get("sp"), AsmRealReg.regMap.get("sp"),
                new AsmVirtualImm(-curFunc.all)));
        exitBlock.insts.add(new AsmBinary("add", AsmRealReg.regMap.get("sp"), AsmRealReg.regMap.get("sp"),
                new AsmVirtualImm(curFunc.all)));
        exitBlock.insts.add(new AsmRet());
    }

    public void visit(IRBlock node) {
        node.insts.forEach(inst -> {
            inst.accept(this);
        });
        node.ter.accept(this);
    }

    public void visit(IRAlloca node) {
        curBlock.add(new AsmBinary("add", getReg(node.alloca), AsmRealReg.regMap.get("sp"),
                new AsmVirtualImm(curFunc.paraU + curFunc.alloca)));
        curFunc.alloca += 4;
    }

    public void visit(IRBranch node) {
        curBlock.add(new AsmBeq(getReg(node.cond), blockMap.get(node.falseB)));
        curBlock.add(new AsmJump(blockMap.get(node.trueB)));
    }

    public void visit(IRCalc node) {
        curBlock.add(new AsmBinary(node.op, getReg(node.res), getReg(node.ll), getReg(node.rr)));
    }

    public void visit(IRCall node) {
        for (int i = 0; i < node.args.size(); ++i) {
            IRBasic arg = node.args.get(i);
            if (i < 8)
                curBlock.add(new AsmMv(AsmRealReg.regMap.get("a" + i), getReg(arg)));
            else
                storeReg(arg.type.size, getReg(arg), AsmRealReg.regMap.get("sp"), i - 8 << 2);
        }
        curBlock.add(new AsmCall(node.name));
        if (node.call != null)
            curBlock.add(new AsmMv(getReg(node.call), AsmRealReg.regMap.get("a0")));
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
            curBlock.add(new AsmUnary("slli", tmp, idx, new Immediate(2)));
            curBlock.add(new AsmBinary("add", getReg(node.res), getReg(node.ptr), tmp));
        }
    }

    public void visit(IRIcmp node) {
        AsmVirtualReg tmp = new AsmVirtualReg(4);
        switch (node.op) {
            case "eq":
                curBlock.add(new AsmBinary("sub", tmp, getReg(node.ll), getReg(node.rr)));
                curBlock.add(new AsmUnary("seqz", getReg(node.reg), tmp));
                break;
            case "ne":
                curBlock.add(new AsmBinary("sub", tmp, getReg(node.ll), getReg(node.rr)));
                curBlock.add(new AsmUnary("snez", getReg(node.reg), tmp));
                break;
            case "sgt":
                curBlock.add(new AsmBinary("slt", getReg(node.reg), getReg(node.rr), getReg(node.ll)));
                break;
            case "sge":
                curBlock.add(new AsmBinary("slt", tmp, getReg(node.ll), getReg(node.rr)));
                curBlock.add(new AsmUnary("xori", getReg(node.reg), tmp, new Immediate(1)));
                break;
            case "slt":
                curBlock.add(new AsmBinary("slt", getReg(node.reg), getReg(node.ll), getReg(node.rr)));
                break;
            case "sle":
                curBlock.add(new AsmBinary("slt", tmp, getReg(node.rr), getReg(node.ll)));
                curBlock.add(new AsmUnary("xori", getReg(node.reg), tmp, new Immediate(1)));
                break;
        }
    }

    public void visit(IRJump node) {
        curBlock.add(new AsmJump(blockMap.get(node.block)));
    }

    public void visit(IRLoad node) {
        loadReg(node.type.size, getReg(node.reg), getReg(node.src), 0);
    }

    public void visit(IRRet node) {
        if (node.value != irVoidConst)
            curBlock.add(new AsmMv(AsmRealReg.regMap.get("a0"), getReg(node.value)));
        loadReg(4, AsmRealReg.regMap.get("ra"), AsmRealReg.regMap.get("sp"), curFunc.paraU);
    }

    public void visit(IRStore node) {
        storeReg(node.value.type.size, getReg(node.value), getReg(node.dest), 0);
    }
}
