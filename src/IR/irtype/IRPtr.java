package src.IR.irtype;

import src.IR.basic.IRBasic;

public class IRPtr extends IRType{
    IRType ptrFrom;
    public int dim;

    public IRPtr(IRType tt) {
        super(tt.name+"*",4);
        this.ptrFrom=tt;
        if(tt instanceof IRPtr) {
            this.ptrFrom=((IRPtr)tt).ptrFrom;
            this.dim=((IRPtr)tt).dim+1;
        } else {
            this.dim=1;
        }
    }

    public IRPtr(IRType tt,int dim) {
        super(tt.name+"*".repeat(dim),4);
        if(tt instanceof IRPtr) {
            this.ptrFrom = ((IRPtr)tt).ptrFrom;
            this.dim=((IRPtr)tt).dim+dim;
        } else {
            this.dim=dim;
            this.ptrFrom=tt;
        }
    }

    public IRType PointTo() {
        if(dim==1) return ptrFrom;
        return new IRPtr(ptrFrom,dim-1);
    }

    public boolean equals(Object obj) {
        if(obj instanceof IRPtr ii) {
            return ptrFrom.equals(ii.ptrFrom) && dim==ii.dim;
        } else return false;
    }

    @Override
    public String toString() {
        return ptrFrom.toString() + "*".repeat(dim);
    }

    @Override
    public IRBasic defaultValue() {
        return new IRNullConst(this);
    }
}
