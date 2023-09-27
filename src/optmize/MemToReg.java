package src.optmize;

import src.IR.basic.*;
import src.IR.instructments.*;
import src.IR.irtype.IRPtr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class MemToReg {
    IRFileAnalyze irF;
    IRFunction currentF;
    LinkedHashSet<IRRegister> pro = new LinkedHashSet<>();
    HashMap<IRRegister, HashSet<IRBlock>> allo = new HashMap<>();
    HashMap<IRRegister, IRBasic> dd = new HashMap<>();

    public MemToReg(IRFileAnalyze f) {
        this.irF=f;
    }

    public void work() {
        new DomTree(irF).work();
        irF.fuc.forEach(this::workF);
    }

    void colllect (){
        pro.clear();
        for(var in : currentF.entry.insts) {
            if(!(in instanceof IRAlloca)) break;
            IRRegister re = ((IRAlloca) in).alloca;
            allo.put(re,new HashSet<>());
            if(isAllca((IRAlloca) in) ) pro.add(re);
        }
    }

    boolean isAllca(IRAlloca in) {
        if(in.index>=8) return false;
        IRRegister rr = in.alloca;
        for(var bb : currentF.blocks) {
            for(var uu : bb.insts) {
                if(!(uu instanceof IRLoad) && !(uu instanceof IRStore) && uu.getU().contains(rr)) return false;
                if(uu instanceof IRStore ss && ss.dest == rr) allo.get(rr).add(bb);
            }
        }
        return true;
    }

    void proMem(IRRegister re) {
        HashSet<IRBlock> ph = new HashSet<>();
        HashSet<IRBlock> in = new HashSet<>(allo.get(re));
        LinkedList<IRBlock> wo = new LinkedList<>(in);
        while (!wo.isEmpty()) {
            IRBlock bb = wo.removeFirst();
            in.remove(bb);
            for(IRBlock cd : bb.domF) {
                if(!ph.contains(cd)) {
                    cd.add(new IRPhi(cd,re,new IRRegister("",((IRPtr) re.type).PointTo())));
                    ph.add(cd);
                    if(!in.contains(cd)) {
                        wo.add(cd);
                        in.add(cd);
                    }
                }
            }
        }
    }

    void rename(IRBlock bb) {
        var old = new HashMap<>(dd);
        LinkedList<IROrders> newO = new LinkedList<>();
        for(var ii : bb.phi) {
            dd.put(ii.src,ii.dest);
        }
        for(int i=0;i<bb.insts.size();i++) {
            var ii = bb.insts.get(i);
            if(ii instanceof IRAlloca aa && pro.contains(aa.alloca)) continue;;
            if(ii instanceof IRLoad ll && pro.contains(ll.src)) {
                for(int j=i+1;j<bb.insts.size();j++) bb.insts.get(j).replaceU(ll.reg,dd.get(ll.src));
                if(bb.ter !=null) bb.ter.replaceU(ll.reg,dd.get(ll.src));
            } else if(ii instanceof IRStore ss && pro.contains(ss.dest)) dd.put(ss.dest,ss.value);
            else newO.add(ii);
        }
        bb.insts=newO;
        bb.succ.forEach(su -> {
            su.phi.forEach(pp -> pp.add(dd.get(pp.src),bb));
        });
        bb.domC.forEach(cc -> rename(cc));
        dd = old;
    }

    public void simplify(IRBlock bb) {
        bb.phi.forEach(pp -> {
            IRBasic vv = pp.vv.get(0);
            boolean flag = true;
            for(int j=1;j<pp.vv.size();j++) {
                if(pp.vv.get(j)!=vv) {
                    flag=false;
                    break;
                }
            }
            if(flag) {
                bb.insts.forEach(ii -> ii.replaceU(pp.dest,vv));
                pp.delete=true;
            }
        });
        for(int i=bb.phi.size()-1;i>=0;i--) {
            IRPhi p = bb.phi.get(i);
            if(!p.delete) bb.insts.addFirst(p);
        }
        bb.domC.forEach(this::simplify);
    }
    public void workF(IRFunction func) {
        currentF=func;
        colllect();
        for(var all : pro) {
            proMem(all);
        }
        dd.clear();
        rename(func.entry);
        simplify(func.entry);
    }
}
