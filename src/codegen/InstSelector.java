package src.codegen;

import src.IR.basic.*;
import src.IR.instructments.*;
import src.IR.irtype.IRConst;
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
        if (node.initFunc != null) {
            curFunc = new AsmFunction(node.initFunc.name);
            fileA.fun.add(curFunc);
            node.initFunc.accept(this);
        }
        node.fuc.forEach(func -> {
            curFunc = new AsmFunction(func.name);
            fileA.fun.add(curFunc);
            func.accept(this);
        });
    }

    public void visit(IRFunction node) {
        blockMap.clear();
        AsmVirtualReg.cnt = 0;
        int MaxC = 0;
        for (IRBlock blk : node.blocks) {
            blockMap.put(blk, new AsmBlock(".L" + blockCnt++));
            for (IROrders inst : blk.insts)
                if (inst instanceof IRCall)
                    MaxC = Math.max(MaxC, ((IRCall) inst).args.size());
        }
        curFunc.paraU = (MaxC > 8 ? MaxC - 8 : 0) << 2;
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

        AsmBlock enB = curFunc.block.get(0), exB = curFunc.block.get(curFunc.block.size() - 1);
        enB.insts.addFirst(new AsmBinary("add", AsmRealReg.regMap.get("sp"), AsmRealReg.regMap.get("sp"),
                new AsmVirtualImm(-curFunc.all)));
        exB.insts.add(new AsmBinary("add", AsmRealReg.regMap.get("sp"), AsmRealReg.regMap.get("sp"),
                new AsmVirtualImm(curFunc.all)));
        exB.insts.add(new AsmRet());
    }

    public void visit(IRBlock node) {
        node.insts.forEach(inst -> inst.accept(this));
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

    public void visit(IRPhi node) {
        AsmVirtualReg tt = new AsmVirtualReg(node.dest.type.size);
        curBlock.add(new AsmMv(getReg(node.dest),tt));
        for(int i=0;i<node.vv.size();i++) {
            IRBasic vv = node.vv.get(i);
            if(vv instanceof IRConst cc) {
                blockMap.get(node.bb.get(i).)
            }
        }
    }

    AsmReg getReg(IRBasic bb) {
        if (bb.reg == null) {
            if (bb instanceof IRRegister) {
                bb.reg = new AsmVirtualReg(bb.type.size);
            } else if (bb instanceof IRConst) {
                bb.reg = new AsmVirtualImm((IRConst) bb);
            }
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
}
