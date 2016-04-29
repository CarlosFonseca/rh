/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.session;

import java.sql.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.CompoundSelection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import pg.entities.*;

/**
 *
 * @author bnf02107
 */
@Stateless
public class UserInfoContratosFacade extends AbstractFacade<UserInfoContratos> {
    @PersistenceContext(unitName = "RHSPUPG")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public UserInfoContratosFacade() {
        super(UserInfoContratos.class);
    }
    
    public List<UserInfoContratos> DemitidosVinculo() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        Root rt = cb.createQuery().from(entityClass);
        
        javax.persistence.criteria.Subquery<Integer> sbNRE =  cq.subquery(Integer.class);
        javax.persistence.criteria.Root sbQuery= sbNRE.from(Cadastros.class); 
        
                sbNRE.select(sbQuery.get(Cadastros_.nre))
                .where(cb.greaterThan(sbQuery.get(Cadastros_.cdSituacao).get(Situacoes_.cdSituacao) , 59));
        
       Predicate[] predicate = new Predicate[4]  ;       
         predicate[0] = cb.lessThan(rt.get(UserInfoContratos_.userInfoContratosPK).get(UserInfoContratosPK_.cdTpContrato), 5);
         predicate[1] = cb.isNull(rt.get(UserInfoContratos_.dtFim));
         predicate[2] = cb.notEqual(rt.get(UserInfoContratos_.cadastros).get(Cadastros_.nre), 0);
         predicate[3] = cb.in(rt.get(UserInfoContratos_.cadastros).get((Cadastros_.nre))).value(sbNRE);        
        
       return findCriteria(null, predicate, null);  
    }
    
    
    public List<Tuple> EmpregSemVinculo(){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        Root rt = cb.createQuery().from(entityClass);
        
        javax.persistence.criteria.Subquery<Integer> sbNRE =  cq.subquery(Integer.class);
        javax.persistence.criteria.Root sbQuery= sbNRE.from(UserInfoContratos.class); 
        
                sbNRE.select(sbQuery.get(UserInfoContratos_.cadastros).get(Cadastros_.nre))
                .where(cb.and(cb.or(cb.isNull(sbQuery.get(UserInfoContratos_.dtFim)),
                              cb.equal(sbQuery.get(UserInfoContratos_.estado), "A")),
                              cb.notEqual(sbQuery.get(UserInfoContratos_.cadastros).get(Cadastros_.nre), 0)));
             
       
       Predicate[] predicate = new Predicate[6]  ;       
       predicate[0] = cb.lessThan(rt.get(UserInfoContratos_.userInfoContratosPK).get(UserInfoContratosPK_.cdTpContrato), 6);
       predicate[1] = cb.like(rt.get(UserInfoContratos_.estado), "I"); 
       predicate[2] = cb.isNotNull(rt.get(UserInfoContratos_.dtFim));
       predicate[3] = cb.notEqual(rt.get(UserInfoContratos_.cadastros).get(Cadastros_.nre), 0);
       predicate[4] = cb.lessThan(rt.get(UserInfoContratos_.cadastros).get(Cadastros_.cdSituacao).get(Situacoes_.cdSituacao), 59);       
       predicate[5] = cb.not(rt.get(UserInfoContratos_.cadastros).get((Cadastros_.nre))).in(sbNRE);               
       
       CompoundSelection<Tuple> compoundSelection = cb.tuple(rt.get(UserInfoContratos_.cadastros).get(Cadastros_.nre).alias("nre"),
                                                             rt.get(UserInfoContratos_.cadastros).get(Cadastros_.nomeRedz).alias("nomeRedz"));
      
       return findCriteria(null, compoundSelection, predicate, null, true);
    }

    
    public List<UserInfoContratos> ContratadosSemVinculo(){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        Root rt = cb.createQuery().from(entityClass);
        
       Predicate[] predicate = new Predicate[5]  ;       
       predicate[0] = cb.lessThan(rt.get(UserInfoContratos_.userInfoContratosPK).get(UserInfoContratosPK_.cdTpContrato), 5);
       predicate[1] = cb.like(rt.get(UserInfoContratos_.estado), "A"); 
       predicate[2] = cb.lessThan(rt.get(UserInfoContratos_.dtFim), cb.currentDate());
       predicate[3] = cb.notEqual(rt.get(UserInfoContratos_.cadastros).get(Cadastros_.nre), 0);
       predicate[4] = cb.like(rt.get(UserInfoContratos_.cadastros).get(Cadastros_.cdSituacao).get(Situacoes_.mapaMess), "S");         

       return findCriteria(null, predicate, null);  
    }
    
   public List<UserInfoContratos> Demitidos(Integer ano, Integer mes){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        Root rt = cb.createQuery().from(entityClass);

       javax.persistence.criteria.Subquery<String> sbNRE =  cq.subquery(String.class);
        javax.persistence.criteria.Root sbQuery= sbNRE.from(UserInfoContratos.class); 
        
                sbNRE.select(cb.concat(sbQuery.get(UserInfoContratos_.cadastros).get(Cadastros_.nre),
                                                    cb.sum(sbQuery.get(UserInfoContratos_.userInfoContratosPK).get(UserInfoContratosPK_.dtIni),-1)
                        ))
                .where(cb.and(cb.equal(sbQuery.get(UserInfoContratos_.continuo), "S"),
                              cb.notEqual(sbQuery.get(UserInfoContratos_.cadastros).get(Cadastros_.nre), 0)));
              
        
        Path<InfoEmpresas> ie = rt.join(UserInfoContratos_.cadastros).join(Cadastros_.infoEmpresasCollection, JoinType.LEFT);
        
       Expression<Date> dtMaisUm = cb.sum(rt.get(UserInfoContratos_.dtFim),1);        
       Expression<Integer> anoExpression = cb.function("TO_CHAR",Integer.class,dtMaisUm, cb.literal("YYYY"));
       Expression<Integer> mesExpression = cb.function("TO_CHAR",Integer.class,dtMaisUm, cb.literal("MM"));
 
        Expression<String> filtroExpression = cb.concat(rt.get(UserInfoContratos_.cadastros).get(Cadastros_.nre),
                                                        rt.get(UserInfoContratos_.dtFim));      
       
       Predicate[] predicate = new Predicate[7]  ;       
       predicate[0] = cb.lessThan(rt.get(UserInfoContratos_.userInfoContratosPK).get(UserInfoContratosPK_.cdTpContrato), 5);
       //predicate[1] = cb.like(rt.get(UserInfoContratos_.continuo),"N"); 
       predicate[1] = cb.not(filtroExpression.in(sbNRE));        
       predicate[2] = cb.isNotNull(rt.get(UserInfoContratos_.dtFim));
       predicate[3] = cb.notEqual(rt.join(UserInfoContratos_.cadastros).get(Cadastros_.nre), 0);
       predicate[4] = cb.equal(ie.get(InfoEmpresas_.estado), 'A');
       predicate[5] = cb.equal(anoExpression, ano); 
       predicate[6] = cb.equal(mesExpression, mes); 
       
       Order[] ordenar ={cb.asc(ie.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE)),
                         cb.asc(rt.get(UserInfoContratos_.dtFim))};
                       //cb.asc(rt.join(UserInfoContratos_.cadastros).get(Cadastros_.nre))
       
       return findCriteria(null, predicate, ordenar);  
    } 
    
        public List<UserInfoContratos> Admitidos(Integer ano, Integer mes){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
 //       javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        Root rt = cb.createQuery().from(entityClass);
        
  /*     javax.persistence.criteria.Subquery<String> sbNRE =  cq.subquery(String.class);
        javax.persistence.criteria.Root sbQuery= sbNRE.from(UserInfoContratos.class); 
        
                sbNRE.select(cb.concat(sbQuery.get(UserInfoContratos_.cadastros).get(Cadastros_.nre),
                                                    cb.sum(sbQuery.get(UserInfoContratos_.dtFim),1)
                        ))
                .where(cb.and(cb.equal(sbQuery.get(UserInfoContratos_.continuo), "S"),
                              cb.notEqual(sbQuery.get(UserInfoContratos_.cadastros).get(Cadastros_.nre), 0)));
    */        
       Path<InfoEmpresas> ie = rt.join(UserInfoContratos_.cadastros).join(Cadastros_.infoEmpresasCollection, JoinType.LEFT);
            
       Expression<Integer> anoExpression = cb.function("TO_CHAR",Integer.class,rt.get(UserInfoContratos_.userInfoContratosPK).get(UserInfoContratosPK_.dtIni), cb.literal("YYYY"));
       Expression<Integer> mesExpression = cb.function("TO_CHAR",Integer.class,rt.get(UserInfoContratos_.userInfoContratosPK).get(UserInfoContratosPK_.dtIni), cb.literal("MM"));
                
   //    Expression<String> filtroExpression = cb.concat(rt.get(UserInfoContratos_.cadastros).get(Cadastros_.nre),
   //                                                 rt.get(UserInfoContratos_.userInfoContratosPK).get(UserInfoContratosPK_.dtIni));
       
       Predicate[] predicate = new Predicate[6]  ;       
       predicate[0] = cb.lessThan(rt.get(UserInfoContratos_.userInfoContratosPK).get(UserInfoContratosPK_.cdTpContrato), 5);
       predicate[1] = cb.notEqual(rt.get(UserInfoContratos_.userInfoContratosPK).get(UserInfoContratosPK_.cdTpContrato), "N");
       //predicate[1] = cb.not(filtroExpression.in(sbNRE)); 
       predicate[2] = cb.notEqual(rt.join(UserInfoContratos_.cadastros).get(Cadastros_.nre), 0);
       predicate[3] = cb.equal(ie.get(InfoEmpresas_.estado), 'A');
       predicate[4] = cb.equal(anoExpression, ano); 
       predicate[5] = cb.equal(mesExpression, mes);          
       
       
       Order[] ordenar ={cb.asc(ie.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE)),
                         cb.asc(rt.join(UserInfoContratos_.cadastros).get(Cadastros_.nre))};
 
       return findCriteria(null, predicate, ordenar);  
       
    }

    
    
}
