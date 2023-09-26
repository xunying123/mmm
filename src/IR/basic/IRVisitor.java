package src.IR.basic;

import src.IR.instructments.*;
import src.IR.instructments.IRRet;
import src.IR.instructments.IRStore;

public interface IRVisitor {
    public void visit(IRFileAnalyze node);
    public void visit(IRFunction node);
    public void visit(IRBlock node);

    public void visit(IRAlloca node);
    public void visit(IRCast node);
    public void visit(IRBranch node);
    public void visit(IRCalc node);
    public void visit(IRCall node);
    public void visit(IRGetelementptr node);
    public void visit(IRIcmp node);
    public void visit(IRJump node);
    public void visit(IRLoad node);
    public void visit(IRRet node);
    public void visit(IRStore node);
    public void visit(IRPhi node);
}
