/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.session;


import java.sql.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CompoundSelection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.primefaces.model.SortOrder;
import pg.entities.*;
import pg.faces.util.QPDirecao;

/**
 *
 * @author bnf02107
 */
@Stateless
public class InfoEmpresasFacade extends AbstractFacade<InfoEmpresas> {
    @PersistenceContext(unitName = "RHSPUPG")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public InfoEmpresasFacade() {
        super(InfoEmpresas.class);
    }
    
    
       public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root rt = cq.from(entityClass);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
       Predicate[] condicao = {cb.like(rt.get(InfoEmpresas_.estado), "A"), 
                                cb.like(rt.get(InfoEmpresas_.cadastros).get(Cadastros_.cdSituacao).get(Situacoes_.mapaMess), "S")};
         cq.where(condicao);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<InfoEmpresas> findRange(int[] range, String sortField, SortOrder sortOder, Map filters) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        javax.persistence.criteria.Root root = cq.from(entityClass);
   
        int incialpredicate = 2;
         javax.persistence.criteria.Predicate[] predicateFixo = new Predicate[incialpredicate]  ;       
         predicateFixo[0] = cb.like(root.get(InfoEmpresas_.estado), "A");
         predicateFixo[1] = cb.like(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdSituacao).get(Situacoes_.mapaMess), "S");

//         Predicate condicaoFixa = cb.and(predicateFixo);
         
//         Predicate condicaoDinamica = addChoicePredicate(new Predicate[filters.size()], 0, filters, cb, root);
         
         Predicate condicao = null;
         if (!filters.isEmpty() 
           || (filters.containsKey("globalFilter") && "".equals(filters.get("globalFilter").toString()) && filters.containsValue("%")) ){
             condicao = makePredicate(predicateFixo, filters, cb, root);
         }             

         
      
        cq.select(root);

        Order[] ordenar = {cb.asc(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE)),
                           cb.asc(root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.nrOrdem)),
                           cb.asc(root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.qacEstruturasPK).get(QacEstruturasPK_.cdEstrutura)), 
                           cb.asc(root.join(InfoEmpresas_.cadastros, JoinType.LEFT ).join(Cadastros_.infoGeral, JoinType.LEFT).get(InfoGeral_.cdGrupo)),
                           cb.asc(root.join(InfoEmpresas_.cadastros, JoinType.LEFT ).join(Cadastros_.infoProfs, JoinType.LEFT ).join(InfoProfs_.cdFuncInt, JoinType.LEFT ).get(FuncoesInternas_.preExperiencia)),                           
                           cb.asc(root.join(InfoEmpresas_.cadastros, JoinType.LEFT ).join(Cadastros_.infoProfs, JoinType.LEFT ).join(InfoProfs_.cdFuncInt, JoinType.LEFT ).get(FuncoesInternas_.cdFuncInt)),                           
                           cb.desc(root.join(InfoEmpresas_.cadastros, JoinType.LEFT ).join(Cadastros_.infoGeral, JoinType.LEFT ).get(InfoGeral_.cdNivel))
                            };        
        
        makeOrder(ordenar, sortField, sortOder, cb, root);
        
//       return findCriteria(range, condicaoFixa, condicaoDinamica, ordenar);
       
       if (condicao!=null){
            return findCriteria(range, condicao, ordenar);    
       }else{   
            return findCriteria(range, predicateFixo, ordenar);
       }

       
         
    }
    
    
           public List<InfoEmpresas> findAniversariantes(int[] range, String sortField, SortOrder sortOder, Map filters) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        javax.persistence.criteria.Root root = cq.from(entityClass);
        
        Expression<Integer> semanaAniversario = cb.function("TO_CHAR", Integer.class, root.get(InfoEmpresas_.cadastros).get(Cadastros_.dtNasc), cb.literal("WW") ); 
        Expression<Integer> dataActual = cb.function("TO_CHAR", Integer.class, cb.currentDate() , cb.literal("WW") ); 
        
        int incialpredicate = 3;
         javax.persistence.criteria.Predicate[] predicateFixo = new Predicate[incialpredicate]  ;       
         predicateFixo[0] = cb.like(root.get(InfoEmpresas_.estado), "A");
         predicateFixo[1] = cb.like(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdSituacao).get(Situacoes_.mapaMess), "S");
         predicateFixo[2] = cb.equal(semanaAniversario, dataActual);

//         Predicate condicaoFixa = cb.and(predicateFixo);
         
//         Predicate condicaoDinamica = addChoicePredicate(new Predicate[filters.size()], 0, filters, cb, root);

         Predicate condicao = null;
         if (!filters.isEmpty() 
           || (filters.containsKey("globalFilter") && "".equals(filters.get("globalFilter").toString()) && filters.containsValue("%")) ){
             condicao = makePredicate(predicateFixo, filters, cb, root);
         }             

         
         
         
         Order[] ordenar = {cb.asc(cb.function("TO_CHAR", Integer.class, root.get(InfoEmpresas_.cadastros).get(Cadastros_.dtNasc), cb.literal("DD"))),
                           cb.asc(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE)),
                           cb.asc(root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.nrOrdem)),
                           cb.asc(root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.qacEstruturasPK).get(QacEstruturasPK_.cdEstrutura)), 
                           cb.asc(root.join(InfoEmpresas_.cadastros, JoinType.LEFT ).join(Cadastros_.infoGeral, JoinType.LEFT).get(InfoGeral_.cdGrupo)),
                           cb.asc(root.join(InfoEmpresas_.cadastros, JoinType.LEFT ).join(Cadastros_.infoProfs, JoinType.LEFT ).join(InfoProfs_.cdFuncInt, JoinType.LEFT ).get(FuncoesInternas_.preExperiencia)),                           
                           cb.asc(root.join(InfoEmpresas_.cadastros, JoinType.LEFT ).join(Cadastros_.infoProfs, JoinType.LEFT ).join(InfoProfs_.cdFuncInt, JoinType.LEFT ).get(FuncoesInternas_.cdFuncInt)),                           
                           cb.desc(root.join(InfoEmpresas_.cadastros, JoinType.LEFT ).join(Cadastros_.infoGeral, JoinType.LEFT ).get(InfoGeral_.cdNivel))
                            };

        makeOrder(ordenar, sortField, sortOder, cb, root);
        
//        return findCriteria(range, condicaoFixa, condicaoDinamica, ordenar);
        
       if (condicao!=null){
            return findCriteria(range, condicao, ordenar);    
       }else{   
            return findCriteria(range, predicateFixo, ordenar);
       }

    }

    
       public List<InfoEmpresas> semEmail(int[] range, String sortField, SortOrder sortOder, Map filters) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        javax.persistence.criteria.Root root = cq.from(entityClass);
        
        
        int incialpredicate = 3;
         javax.persistence.criteria.Predicate[] predicateFixo = new Predicate[incialpredicate]  ;       
         predicateFixo[0] = cb.like(root.get(InfoEmpresas_.estado), "A");
         predicateFixo[1] = cb.or(cb.like(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdSituacao).get(Situacoes_.mapaMess), "S"),
                                  root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdSituacao).get(Situacoes_.cdSituacao).in(40,41));
         predicateFixo[2] = root.get(InfoEmpresas_.cadastros).get(Cadastros_.enderecos).get(Enderecos_.internet).isNull();

//         Predicate condicaoFixa = cb.and(predicateFixo);
         
//         Predicate condicaoDinamica = addChoicePredicate(new Predicate[filters.size()], 0, filters, cb, root);

         Predicate condicao = null;
         if (!filters.isEmpty() 
           || (filters.containsKey("globalFilter") && "".equals(filters.get("globalFilter").toString()) && filters.containsValue("%")) ){
             condicao = makePredicate(predicateFixo, filters, cb, root);
         }             

         
         
         
         Order[] ordenar = {cb.asc(cb.function("TO_CHAR", Integer.class, root.get(InfoEmpresas_.cadastros).get(Cadastros_.dtNasc), cb.literal("DD"))),
                           cb.asc(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE)),
                           cb.asc(root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.nrOrdem)),
                           cb.asc(root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.qacEstruturasPK).get(QacEstruturasPK_.cdEstrutura)), 
                           cb.asc(root.join(InfoEmpresas_.cadastros, JoinType.LEFT ).join(Cadastros_.infoGeral, JoinType.LEFT).get(InfoGeral_.cdGrupo)),
                           cb.asc(root.join(InfoEmpresas_.cadastros, JoinType.LEFT ).join(Cadastros_.infoProfs, JoinType.LEFT ).join(InfoProfs_.cdFuncInt, JoinType.LEFT ).get(FuncoesInternas_.preExperiencia)),                           
                           cb.desc(root.join(InfoEmpresas_.cadastros, JoinType.LEFT ).join(Cadastros_.infoProfs, JoinType.LEFT ).join(InfoProfs_.cdFuncInt, JoinType.LEFT ).get(FuncoesInternas_.cdFuncInt)),                           
                           cb.desc(root.join(InfoEmpresas_.cadastros, JoinType.LEFT ).join(Cadastros_.infoGeral, JoinType.LEFT ).get(InfoGeral_.cdNivel))
                            };

        makeOrder(ordenar, sortField, sortOder, cb, root);
        
//        return findCriteria(range, condicaoFixa, condicaoDinamica, ordenar);
        
       if (condicao!=null){
            return findCriteria(range, condicao, ordenar);    
       }else{   
            return findCriteria(range, predicateFixo, ordenar);
       }

    }
       
    public int countSemEmail() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root rt = cq.from(entityClass);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        
       Predicate[] condicao = {cb.like(rt.get(InfoEmpresas_.estado), "A"), 
                                cb.like(rt.get(InfoEmpresas_.cadastros).get(Cadastros_.cdSituacao).get(Situacoes_.mapaMess), "S"),
                                rt.get(InfoEmpresas_.cadastros).get(Cadastros_.enderecos).get(Enderecos_.internet).isNull()};
         cq.where(condicao);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
   
    }     
       
  
    public int countAniversariantes() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root rt = cq.from(entityClass);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        
        Expression<Integer> semanaAniversario = cb.function("TO_CHAR", Integer.class, rt.get(InfoEmpresas_.cadastros).get(Cadastros_.dtNasc), cb.literal("WW") ); 
        Expression<Integer> dataActual = cb.function("TO_CHAR", Integer.class, cb.currentDate() , cb.literal("WW") ); 

        
       Predicate[] condicao = {cb.like(rt.get(InfoEmpresas_.estado), "A"), 
                                cb.like(rt.get(InfoEmpresas_.cadastros).get(Cadastros_.cdSituacao).get(Situacoes_.mapaMess), "S"),
                                cb.equal(semanaAniversario, dataActual)};
         cq.where(condicao);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
//    ----------------------------------------------------------------------------------------------------------        
//    ----------------------------------------------------------------------------------------------------------    
//    ----------------------------------------------------------------------------------------------------------
       public List<Tuple> findDireccao(int[] range, String sortField, SortOrder sortOder, Map filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root root = cq.from(entityClass);
   
        int incialpredicate = 2;
         Predicate[] predicateFixo = new Predicate[incialpredicate]  ;       
         predicateFixo[0] = cb.like(root.get(InfoEmpresas_.estado), "A");
         predicateFixo[1] = cb.like(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdSituacao).get(Situacoes_.mapaMess), "S");

//         Predicate condicaoFixa = cb.and(predicateFixo);
         
//         Predicate condicaoDinamica = addChoicePredicate(new Predicate[filters.size()], 0, filters, cb, root);
         
         Predicate condicao = null;
         if (!filters.isEmpty() 
           || (filters.containsKey("globalFilter") && "".equals(filters.get("globalFilter").toString()) && filters.containsValue("%")) ){
             condicao = makePredicate(predicateFixo, filters, cb, root);
         }             

         
         
        Order[] ordenar = { cb.asc(root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.nrOrdem))};

        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.cdSbu).alias("cdSbu"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsAbrv).alias("dsAbrv"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsComp).alias("dsComp"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.nrOrdem).alias("nrOrdem"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsComp).alias("dsDireccao"));

         makeOrder(ordenar, sortField, sortOder, cb, root);

//        return findCriteria(range, compoundSelection, condicaoFixa, condicaoDinamica, ordenar, true);
       if (condicao!=null){
            return findCriteria(range, compoundSelection, condicao, ordenar, true);    
       }else{   
            return findCriteria(range, compoundSelection, predicateFixo, ordenar, true);
       }

       
       }
    
    public int countDirecao() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root rt = cq.from(entityClass);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        Predicate[] condicao = {cb.like(rt.get(InfoEmpresas_.estado), "A"), 
                                cb.like(rt.get(InfoEmpresas_.cadastros).get(Cadastros_.cdSituacao).get(Situacoes_.mapaMess), "S")};
        cq.where(condicao);
        cq.select(cb.countDistinct(cb.concat(rt.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE), 
                                             rt.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.cdSbu))));
        
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }   
    
    public List<QPDirecao> findByDireccao(String Direccao){

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<QPDirecao> cq = cb.createQuery(QPDirecao.class);
        Root rt = cq.from(entityClass);
     
        cq.select(cb.construct(QPDirecao.class,
                       rt.get(InfoEmpresas_.cadastros).get(Cadastros_.nre),
                       rt.get(InfoEmpresas_.cadastros).get(Cadastros_.nomeRedz),
                       rt.get(InfoEmpresas_.cadastros).get(Cadastros_.dtNasc),
                       rt.join(InfoEmpresas_.cadastros,JoinType.LEFT).join(Cadastros_.infoGeral,JoinType.LEFT).get(InfoGeral_.dtAdmiss),
                       rt.get(InfoEmpresas_.cadastros).get(Cadastros_.cdHabLit).get(HabsLiters_.dsRedz),
                       rt.join(InfoEmpresas_.cadastros,JoinType.LEFT).join(Cadastros_.infoGeral,JoinType.LEFT).get(InfoGeral_.cdGrupo),
                       rt.join(InfoEmpresas_.cadastros,JoinType.LEFT).join(Cadastros_.infoGeral,JoinType.LEFT).get(InfoGeral_.cdNivel),
                       rt.join(InfoEmpresas_.cadastros,JoinType.LEFT).join(Cadastros_.infoGeral,JoinType.LEFT).get(InfoGeral_.dtNivel),
                       rt.join(InfoEmpresas_.cadastros,JoinType.LEFT).join(Cadastros_.infoProfs,JoinType.LEFT).join(InfoProfs_.cdFuncao,JoinType.LEFT).get(FuncoesOficiais_.dsComp),
                       rt.join(InfoEmpresas_.cadastros,JoinType.LEFT).join(Cadastros_.infoProfs,JoinType.LEFT).join(InfoProfs_.cdFuncInt,JoinType.LEFT).get(FuncoesInternas_.dsComp),
                       rt.get(InfoEmpresas_.cadastros).get(Cadastros_.cdSituacao).get(Situacoes_.dsRedz),
                       rt.join(InfoEmpresas_.cadastros,JoinType.LEFT).join(Cadastros_.infoProfs,JoinType.LEFT).join(InfoProfs_.cdFuncao,JoinType.LEFT).get(FuncoesOficiais_.cdFuncao),
                       rt.get(InfoEmpresas_.cadastros).get(Cadastros_.cdSituacao).get(Situacoes_.cdSituacao),
                       rt.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.cdEstrutura),
                       rt.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE),
                       rt.join(InfoEmpresas_.qacEstruturasFicticia,JoinType.LEFT).get(QacEstruturasFicticia_.dsComp),
                       rt.join(InfoEmpresas_.qacEstruturasFicticia,JoinType.LEFT).get(QacEstruturasFicticia_.pontuacao))
                );
        
        Predicate[] condicao = {cb.like(rt.get(InfoEmpresas_.estado), "A"),
                                       cb.like(rt.get(InfoEmpresas_.cadastros).get(Cadastros_.cdSituacao).get(Situacoes_.mapaMess), "S"),
                                       cb.or(cb.notLike(rt.get(InfoEmpresas_.qacEstruturasFicticia).get(QacEstruturasFicticia_.pontuacao), "FICTICIA"),
                                       rt.get(InfoEmpresas_.qacEstruturasFicticia).get(QacEstruturasFicticia_.pontuacao).isNull()),
                                       cb.like(rt.get(InfoEmpresas_.qacEstruturasFicticia).get(QacEstruturasFicticia_.cdSbu).get(QacAreasNegocio_.cdSbu), Direccao)};
        
        Order[] ordenar = {cb.asc(rt.get(InfoEmpresas_.qacEstruturasFicticia).get(QacEstruturasFicticia_.nrOrdem)),
                           cb.asc(rt.get(InfoEmpresas_.qacEstruturasFicticia).get(QacEstruturasFicticia_.cdEstrutura)), 
                           cb.asc(cb.nullif(rt.join(InfoEmpresas_.cadastros, JoinType.LEFT ).join(Cadastros_.infoGeral, JoinType.LEFT).get(InfoGeral_.cdGrupo),"1")),
                           cb.asc(rt.join(InfoEmpresas_.cadastros, JoinType.LEFT ).join(Cadastros_.infoProfs, JoinType.LEFT ).join(InfoProfs_.cdFuncInt, JoinType.LEFT ).get(FuncoesInternas_.preExperiencia)),                           
                           cb.asc(rt.join(InfoEmpresas_.cadastros, JoinType.LEFT ).join(Cadastros_.infoProfs, JoinType.LEFT ).join(InfoProfs_.cdFuncInt, JoinType.LEFT ).get(FuncoesInternas_.cdFuncInt)),                           
                           cb.desc(rt.join(InfoEmpresas_.cadastros, JoinType.LEFT ).join(Cadastros_.infoGeral, JoinType.LEFT ).get(InfoGeral_.cdNivel))
                            };

        cq.where(condicao); 
        cq.orderBy(ordenar);
        
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return q.getResultList();
        
    }
    
  
//    ----------------------------------------------------------------------------------------------------------        
//    ----------------------------------------------------------------------------------------------------------    
//    ----------------------------------------------------------------------------------------------------------
       public List<Tuple> findEstrutura(int[] range, String sortField, SortOrder sortOder, Map filters) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        javax.persistence.criteria.Root root = cq.from(entityClass);
   
        int incialpredicate = 2;
         javax.persistence.criteria.Predicate[] predicateFixo = new Predicate[incialpredicate]  ;       
         predicateFixo[0] = cb.like(root.get(InfoEmpresas_.estado), "A");
         predicateFixo[1] = cb.like(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdSituacao).get(Situacoes_.mapaMess), "S");

//         Predicate condicaoFixa = cb.and(predicateFixo);
         
//         Predicate condicaoDinamica = addChoicePredicate(new Predicate[filters.size()], 0, filters, cb, root);
         
         Predicate condicao = null;
         if (!filters.isEmpty() 
           || (filters.containsKey("globalFilter") && "".equals(filters.get("globalFilter").toString()) && filters.containsValue("%")) ){
             condicao = makePredicate(predicateFixo, filters, cb, root);
         }             

         
         
        Order[] ordenar = {cb.asc(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE)),
                           cb.asc(root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.nrOrdem)) };
        
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE).alias("siglaE"),
                                                              root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.cdEstrutura).alias("cdEstrutura"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.dsAbrv).alias("dsAbrv"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.dsComp).alias("dsComp"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.nrOrdem).alias("nrOrdem"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.qacEmpresas).get(QacEmpresas_.dsSocial).alias("dsSocial"),
                                                              root.join(InfoEmpresas_.qacEstruturas,JoinType.LEFT).join(QacEstruturas_.cdSbu, JoinType.LEFT).get(QacAreasNegocio_.dsComp).alias("dsDireccao"));       

         makeOrder(ordenar, sortField, sortOder, cb, root);

//        return findCriteria(range, compoundSelection, condicaoFixa, condicaoDinamica, ordenar, true);
       if (condicao!=null){
            return findCriteria(range, compoundSelection, condicao, ordenar, true);    
       }else{   
            return findCriteria(range, compoundSelection, predicateFixo, ordenar, true);
       }
        
    }
    
    public int countEstrutura() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root rt = cq.from(entityClass);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        Predicate[] condicao = {cb.like(rt.get(InfoEmpresas_.estado), "A"), 
                                cb.like(rt.get(InfoEmpresas_.cadastros).get(Cadastros_.cdSituacao).get(Situacoes_.mapaMess), "S")};
        cq.where(condicao);
        cq.select(cb.countDistinct(cb.concat(rt.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE), 
                                             rt.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.cdEstrutura))
                 ));
        
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }  
    
   public List findByEstrutura(String Empresa, int cdEstrutura){
        
        
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        Root rt = cb.createQuery().from(entityClass);

            Predicate[] condicao = {cb.like(rt.get(InfoEmpresas_.estado), "A"),
                                       cb.like(rt.get(InfoEmpresas_.cadastros).get(Cadastros_.cdSituacao).get(Situacoes_.mapaMess), "S"),
                                       cb.like(rt.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.qacEstruturasPK).get(QacEstruturasPK_.siglaE), Empresa),
                                       cb.equal(rt.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.qacEstruturasPK).get(QacEstruturasPK_.cdEstrutura), cdEstrutura)};

        return doFindBy(cb, rt, condicao);
        
    }
       
//    ----------------------------------------------------------------------------------------------------------        
//    ----------------------------------------------------------------------------------------------------------    
//    ----------------------------------------------------------------------------------------------------------
   public List<Tuple> findDirecEstab(int[] range, String sortField, SortOrder sortOder, Map filters) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        javax.persistence.criteria.Root root = cq.from(entityClass);
   
        int incialpredicate = 2;
         javax.persistence.criteria.Predicate[] predicateFixo = new Predicate[incialpredicate]  ;       
         predicateFixo[0] = cb.like(root.get(InfoEmpresas_.estado), "A");
         predicateFixo[1] = cb.like(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdSituacao).get(Situacoes_.mapaMess), "S");

//         Predicate condicaoFixa = cb.and(predicateFixo);
         
//         Predicate condicaoDinamica = addChoicePredicate(new Predicate[filters.size()], 0, filters, cb, root);

         Predicate condicao = null;
         if (!filters.isEmpty() 
           || (filters.containsKey("globalFilter") && "".equals(filters.get("globalFilter").toString()) && filters.containsValue("%")) ){
             condicao = makePredicate(predicateFixo, filters, cb, root);
         }             

         
        Expression expression = null ;   
        Order[] ordenar = {cb.asc(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE)),
                           cb.asc(root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.nrOrdem)), 
                           cb.asc(root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.qacEstabelecimentos).get(QacEstabelecimentos_.qacEstabelecimentosPK).get(QacEstabelecimentosPK_.cdEstab)) };
        
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE).alias("siglaE"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.cdSbu).alias("cdSbu"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsAbrv).alias("dsAbrv"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsComp).alias("dsComp"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.nrOrdem).alias("nrOrdem"),
                                                              root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.qacEstabelecimentos).get(QacEstabelecimentos_.qacEstabelecimentosPK).get(QacEstabelecimentosPK_.siglaE).alias("siglaEstab"), 
                                                              root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.qacEstabelecimentos).get(QacEstabelecimentos_.qacEstabelecimentosPK).get(QacEstabelecimentosPK_.cdEstab).alias("cdEstab"),
                                                              root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.qacEstabelecimentos).get(QacEstabelecimentos_.dsComp).alias("dsEstab"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.qacEmpresas).get(QacEmpresas_.dsSocial).alias("dsSocial"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsComp).alias("dsDireccao"));

         makeOrder(ordenar, sortField, sortOder, cb, root);

//        return findCriteria(range, compoundSelection, condicaoFixa, condicaoDinamica, ordenar, true);
        
        if (condicao!=null){
            return findCriteria(range, compoundSelection, condicao, ordenar, true);    
       }else{   
            return findCriteria(range, compoundSelection, predicateFixo, ordenar, true);
       }

        
 
        
    }
    
    public int countDirecEstab() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root rt = cq.from(entityClass);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        Predicate[] condicao = {cb.like(rt.get(InfoEmpresas_.estado), "A"), 
                                cb.like(rt.get(InfoEmpresas_.cadastros).get(Cadastros_.cdSituacao).get(Situacoes_.mapaMess), "S")};
        cq.where(condicao);
        cq.select(cb.countDistinct(cb.concat(
                                   cb.concat(rt.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE), 
                                             rt.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.cdSbu)),
                                   cb.concat(rt.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.qacEstabelecimentos).get(QacEstabelecimentos_.qacEstabelecimentosPK).get(QacEstabelecimentosPK_.siglaE), 
                                             rt.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.qacEstabelecimentos).get(QacEstabelecimentos_.qacEstabelecimentosPK).get(QacEstabelecimentosPK_.cdEstab))
                                    )
                    ));
        
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }  
    
       public List findByDirecEstab(String Empresa, String Direccao, String Estabelecimento){
        
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        Root rt = cb.createQuery().from(entityClass);

        Predicate[] condicao = {cb.like(rt.get(InfoEmpresas_.estado), "A"),
                                       cb.like(rt.join(InfoEmpresas_.cadastros).join(Cadastros_.cdSituacao).get(Situacoes_.mapaMess), "S"),
                                       cb.like(rt.join(InfoEmpresas_.qacEstruturas).join(QacEstruturas_.cdSbu).get(QacAreasNegocio_.cdSbu), Direccao),
                                       cb.like(rt.join(InfoEmpresas_.cadastros).join(Cadastros_.infoGeral).get(InfoGeral_.qacEstabelecimentos).get(QacEstabelecimentos_.qacEstabelecimentosPK).get(QacEstabelecimentosPK_.cdEstab), Estabelecimento)};
 

        return doFindBy(cb, rt, condicao);
        
    }
    
    
    
    //-------------------------------------------------------------------------
   
     private List doFindBy(CriteriaBuilder cb, Root rt,Predicate[] condicao){
   
        
        Order[] ordenar = {cb.asc(rt.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.nrOrdem)),
                           cb.asc(rt.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.qacEstruturasPK).get(QacEstruturasPK_.cdEstrutura)), 
                           cb.asc(cb.nullif(rt.join(InfoEmpresas_.cadastros, JoinType.LEFT ).join(Cadastros_.infoGeral, JoinType.LEFT).get(InfoGeral_.cdGrupo),"1")),
                           cb.asc(rt.join(InfoEmpresas_.cadastros, JoinType.LEFT ).join(Cadastros_.infoProfs, JoinType.LEFT ).join(InfoProfs_.cdFuncInt, JoinType.LEFT ).get(FuncoesInternas_.preExperiencia)),                           
                           cb.asc(rt.join(InfoEmpresas_.cadastros, JoinType.LEFT ).join(Cadastros_.infoProfs, JoinType.LEFT ).join(InfoProfs_.cdFuncInt, JoinType.LEFT ).get(FuncoesInternas_.cdFuncInt)),                           
                           cb.desc(rt.join(InfoEmpresas_.cadastros, JoinType.LEFT ).join(Cadastros_.infoGeral, JoinType.LEFT ).get(InfoGeral_.cdNivel))
                            };

        return findCriteria(null,condicao,ordenar);       
    }
     
 
 //------------------------------------------------------------------------------     
//--------------------------QP Mensal Dashoard----------------------------------
//------------------------------------------------------------------------------     
     
 public List<Tuple> TotalAoServicoMensal(Integer ano, Integer mes){
     
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        javax.persistence.criteria.Root root = cq.from(entityClass);
   
        Path<UserInfoContratos> uic = root.join(InfoEmpresas_.cadastros).join(Cadastros_.userInfoContratosCollection); 
        
//        Expression<String> mesExpression = cb.function("TO_CHAR", String.class, cb.literal(mes+""), cb.literal("00"));
//        Expression<String> anoExpression = cb.function("TO_CHAR", String.class, cb.literal(ano+""), cb.literal("0000"));
        Expression<Date> dtExpression = cb.function("LAST_DAY",Date.class,cb.function("TO_DATE",Date.class,cb.concat(cb.concat(cb.literal(ano.toString()),"-"), cb.concat(cb.literal(mes.toString()), "-28"))));
        
         javax.persistence.criteria.Predicate[] predicate = new Predicate[5]  ;       
         predicate[0] = cb.like(root.get(InfoEmpresas_.estado), "A");
         predicate[1] = cb.notEqual(root.get(InfoEmpresas_.cadastros).get(Cadastros_.nre), 0);
         predicate[2] = cb.lessThan(uic.get(UserInfoContratos_.tiposDeContrato).get(TiposDeContrato_.cdTpContrato), 5);
         predicate[3] = cb.lessThanOrEqualTo(uic.get(UserInfoContratos_.userInfoContratosPK).get(UserInfoContratosPK_.dtIni), dtExpression);
         predicate[4] = cb.or(cb.greaterThanOrEqualTo(uic.get(UserInfoContratos_.dtFim), dtExpression),
                              cb.isNull(uic.get(UserInfoContratos_.dtFim)));      
//         predicate[5] = cb.greaterThan(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdSituacao).get(Situacoes_.cdSituacao), 9);
         
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE).alias("siglaE"),
                                                              cb.countDistinct(root.get(InfoEmpresas_.cadastros).get(Cadastros_.nre)).alias("total"));       
        Expression[] agrupar = new Expression[1];
        agrupar[0] = root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE) ;   
        
        Order[] ordenar = {cb.asc(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE))};
        
        return findCriteria(null, compoundSelection, predicate, ordenar, agrupar);
             
     }  

//------------------------------------------------------------------------------     
//-----------------------por Empresa Tableau de bord----------------------------
//------------------------------------------------------------------------------     
     
 public List<Tuple> totalAoServicoPorEmpresa(){
     
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        javax.persistence.criteria.Root root = cq.from(entityClass);

        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE).alias("siglaE"),
                                                              cb.count(root.get(InfoEmpresas_.cadastros).get(Cadastros_.nre)).alias("total"));       

        return subDoTotalporEmpresa(cq, cb, root, compoundSelection);
        
 }    
 
  public List<Tuple> totalporSexoPorEmpresa(){
     
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Tuple> cq = cb.createTupleQuery();         
        javax.persistence.criteria.Root root = cq.from(entityClass);
   

        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE).alias("siglaE"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.sexo)).when("M", 1).otherwise(0)).alias("M"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.sexo)).when( "F", 1).otherwise(0)).alias("F"),
                                                              cb.count(root.get(InfoEmpresas_.cadastros).get(Cadastros_.nre)).alias("total"));       
        
        return subDoTotalporEmpresa(cq, cb, root, compoundSelection);

 }    

   public List<Tuple> mediaEtariaPorEmpresa(){
     
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        javax.persistence.criteria.Root root = cq.from(entityClass);

        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE).alias("siglaE"),
                                                              cb.avg(cb.function("floor",Integer.class,cb.quot(cb.function("months_between",Double.class, cb.currentDate(), root.get(InfoEmpresas_.cadastros).get(Cadastros_.dtNasc)),12))).alias("media"));       
        
        return subDoTotalporEmpresa(cq, cb, root, compoundSelection);
 }    
  
    public List<Tuple> totalporNivelPorEmpresa(){
     
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Tuple> cq = cb.createTupleQuery();         
        javax.persistence.criteria.Root root = cq.from(entityClass);
         
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE).alias("siglaE"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.cdNivel)).when(1, 1).when(2, 1).when(3, 1).when(4, 1).otherwise(0)).alias("1_a_4"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.cdNivel)).when(5, 1).when(6, 1).when(7, 1).when(8, 1).otherwise(0)).alias("5_a_8"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.cdNivel)).when(9, 1).when(10, 1).when(11, 1).when(12, 1).otherwise(0)).alias("9_a_12"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.cdNivel)).when(13, 1).when(14, 1).when(15, 1).otherwise(0)).alias("13_a_15"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.cdNivel)).when(16, 1).when(17, 1).when(18, 1).otherwise(0)).alias("16_a_18"),
                                                              cb.count(root.get(InfoEmpresas_.cadastros).get(Cadastros_.nre)).alias("total"));       
            
        return subDoTotalporEmpresa(cq, cb, root, compoundSelection);

 }   
        
    
     public List<Tuple> totalPorAntiguidadePorEmpresa(){
        
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Tuple> cq = cb.createTupleQuery();        
        javax.persistence.criteria.Root root = cq.from(entityClass);
         
        Expression<Number> anosdeAdmissao = cb.quot(cb.function("months_between",Double.class, cb.currentDate(), root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.dtAdmiss)),12); 
         
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE).alias("siglaE"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosdeAdmissao, 1), 1).otherwise(0).as(Integer.class)).alias("ate_1_ano"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosdeAdmissao, 1), 0).when(cb.le(anosdeAdmissao, 5), 1).otherwise(0).as(Integer.class)).alias("de_1_Ate_5_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosdeAdmissao, 5), 0).when(cb.le(anosdeAdmissao, 10), 1).otherwise(0).as(Integer.class)).alias("de_5_Ate_10_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosdeAdmissao, 10), 0).when(cb.le(anosdeAdmissao, 15), 1).otherwise(0).as(Integer.class)).alias("de_10_Ate_15_anos"),
                                                              cb.sum(cb.selectCase().when(cb.gt(anosdeAdmissao, 15), 1).otherwise(0).as(Integer.class)).alias("mais_de_15_anos"),
                                                              cb.count(root.get(InfoEmpresas_.cadastros).get(Cadastros_.nre)).alias("total"));       
        
        return subDoTotalporEmpresa(cq, cb, root, compoundSelection);
        
        
 }      
 
      public List<Tuple> totalPorAntiguidadeBancaPorEmpresa(){
        
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Tuple> cq = cb.createTupleQuery();         
        javax.persistence.criteria.Root root = cq.from(entityClass);

        Expression<Number> AnoDeAntiguidade = cb.quot(cb.function("months_between",Double.class, cb.currentDate(), root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.dtAdmssAnt)),12); 
         
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE).alias("siglaE"),
                                                              cb.sum(cb.selectCase().when(cb.le(AnoDeAntiguidade, 1), 1).otherwise(0).as(Integer.class)).alias("ate_1_ano"),
                                                              cb.sum(cb.selectCase().when(cb.le(AnoDeAntiguidade, 1), 0).when(cb.le(AnoDeAntiguidade, 5), 1).otherwise(0).as(Integer.class)).alias("de_1_Ate_5_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(AnoDeAntiguidade, 5), 0).when(cb.le(AnoDeAntiguidade, 10), 1).otherwise(0).as(Integer.class)).alias("de_5_Ate_10_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(AnoDeAntiguidade, 10), 0).when(cb.le(AnoDeAntiguidade, 15), 1).otherwise(0).as(Integer.class)).alias("de_10_Ate_15_anos"),
                                                              cb.sum(cb.selectCase().when(cb.gt(AnoDeAntiguidade, 15), 1).otherwise(0).as(Integer.class)).alias("mais_de_15_anos"),
                                                              cb.count(root.get(InfoEmpresas_.cadastros).get(Cadastros_.nre)).alias("total"));       
        return subDoTotalporEmpresa(cq, cb, root, compoundSelection);
 }    
 
      
      public List<Tuple> totalPorEstruturaEtariaPorEmpresa(){         
        
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        javax.persistence.criteria.Root root = cq.from(entityClass);

        Expression<Number> AnosDeNascimento = cb.quot(cb.function("months_between",Double.class, cb.currentDate(), root.get(InfoEmpresas_.cadastros).get(Cadastros_.dtNasc)),12); 
         
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE).alias("siglaE"),
                                                              cb.sum(cb.selectCase().when(cb.le(AnosDeNascimento, 24), 1).otherwise(0).as(Integer.class)).alias("ate_24_ano"),
                                                              cb.sum(cb.selectCase().when(cb.le(AnosDeNascimento, 24), 0).when(cb.le(AnosDeNascimento, 34), 1).otherwise(0).as(Integer.class)).alias("de_25_Ate_34_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(AnosDeNascimento, 34), 0).when(cb.le(AnosDeNascimento, 44), 1).otherwise(0).as(Integer.class)).alias("de_35_Ate_44_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(AnosDeNascimento, 44), 0).when(cb.le(AnosDeNascimento, 54), 1).otherwise(0).as(Integer.class)).alias("de_45_Ate_54_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(AnosDeNascimento, 54), 0).when(cb.le(AnosDeNascimento, 65), 1).otherwise(0).as(Integer.class)).alias("de_55_Ate_65_anos"),
                                                              cb.sum(cb.selectCase().when(cb.gt(AnosDeNascimento, 65), 1).otherwise(0).as(Integer.class)).alias("mais_de_65_anos"),
                                                              cb.count(root.get(InfoEmpresas_.cadastros).get(Cadastros_.nre)).alias("total"));       
        
        return subDoTotalporEmpresa(cq, cb, root, compoundSelection);
 }      
      
       public List<Tuple> totalPorHabilitacaoPorEmpresa(){         
        
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Tuple> cq = cb.createTupleQuery(); 
        
        javax.persistence.criteria.Root root = cq.from(entityClass);
        
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE).alias("siglaE"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdHabLit).get(HabsLiters_.dsAbrv)).when("1ºCl Ens B", 1).otherwise(0)).alias("1Cl_Ens_B"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdHabLit).get(HabsLiters_.dsAbrv)).when("2ºCl Ens B", 1).otherwise(0)).alias("2Cl_Ens_B"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdHabLit).get(HabsLiters_.dsAbrv)).when("3ºCl Ens B", 1).otherwise(0)).alias("3Cl_Ens_B"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdHabLit).get(HabsLiters_.dsAbrv)).when("Ens Sec", 1).otherwise(0)).alias("Ens_Sec"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdHabLit).get(HabsLiters_.dsAbrv)).when("Bacharel", 1).otherwise(0)).alias("Bacharel"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdHabLit).get(HabsLiters_.dsAbrv)).when("Licenciad", 1).otherwise(0)).alias("Licenciad"),
                                                              cb.count(root.get(InfoEmpresas_.cadastros).get(Cadastros_.nre)).alias("total"));       
        
        return subDoTotalporEmpresa(cq, cb, root, compoundSelection);
        
 }  
       
     public List<Tuple> totalporSitGeoPorEmpresa(){
     
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Tuple> cq = cb.createTupleQuery();         
        javax.persistence.criteria.Root root = cq.from(entityClass);
   

        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE).alias("siglaE"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.qacEstabelecimentos).get(QacEstabelecimentos_.sitGeo)).when("C", 1).otherwise(0)).alias("C"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.qacEstabelecimentos).get(QacEstabelecimentos_.sitGeo)).when("M", 1).otherwise(0)).alias("M"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.qacEstabelecimentos).get(QacEstabelecimentos_.sitGeo)).when("A", 1).otherwise(0)).alias("A"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.qacEstabelecimentos).get(QacEstabelecimentos_.sitGeo)).when("C", 0).when("M", 0).when("A", 0).otherwise(1)).alias("O"),
                                                              cb.count(root.get(InfoEmpresas_.cadastros).get(Cadastros_.nre)).alias("total"));       
        
        return subDoTotalporEmpresa(cq, cb, root, compoundSelection);

 }    

       
       
       
  public List<Tuple> TableauDeBordPorEmpresa(){
     
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        javax.persistence.criteria.Root root = cq.from(entityClass);
   
         Expression<Number> anosdeAdmissao = cb.quot(cb.function("months_between",Double.class, cb.currentDate(), root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.dtAdmiss)),12); 

         Expression<Number> AnoDeAntiguidade = cb.quot(cb.function("months_between",Double.class, cb.currentDate(), root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.dtAdmssAnt)),12); 
 
        Expression<Number> AnosDeNascimento = cb.quot(cb.function("months_between",Double.class, cb.currentDate(), root.get(InfoEmpresas_.cadastros).get(Cadastros_.dtNasc)),12); 
         
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE).alias("siglaE"),
        /*Total Colaboradores*/                               cb.count(root.get(InfoEmpresas_.cadastros).get(Cadastros_.nre)).alias("total"),
        /*Por Sexo*/                                          cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.sexo)).when("M", 1).otherwise(0)).alias("M"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.sexo)).when( "F", 1).otherwise(0)).alias("F"),
       /*Por Nível*/                                          cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.cdNivel)).when(1, 1).when(2, 1).when(3, 1).when(4, 1).otherwise(0)).alias("1_a_4"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.cdNivel)).when(5, 1).when(6, 1).when(7, 1).when(8, 1).otherwise(0)).alias("5_a_8"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.cdNivel)).when(9, 1).when(10, 1).when(11, 1).when(12, 1).otherwise(0)).alias("9_a_12"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.cdNivel)).when(13, 1).when(14, 1).when(15, 1).otherwise(0)).alias("13_a_15"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.cdNivel)).when(16, 1).when(17, 1).when(18, 1).otherwise(0)).alias("16_a_18"),
      /*Antiguidade na empresa*/                              cb.sum(cb.selectCase().when(cb.le(anosdeAdmissao, 1), 1).otherwise(0).as(Integer.class)).alias("adm_ate_1_ano"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosdeAdmissao, 1), 0).when(cb.le(anosdeAdmissao, 5), 1).otherwise(0).as(Integer.class)).alias("adm_de_1_Ate_5_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosdeAdmissao, 5), 0).when(cb.le(anosdeAdmissao, 10), 1).otherwise(0).as(Integer.class)).alias("adm_de_5_Ate_10_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosdeAdmissao, 10), 0).when(cb.le(anosdeAdmissao, 15), 1).otherwise(0).as(Integer.class)).alias("adm_de_10_Ate_15_anos"),
                                                              cb.sum(cb.selectCase().when(cb.gt(anosdeAdmissao, 15), 1).otherwise(0).as(Integer.class)).alias("adm_mais_de_15_anos"),
     /*Antiguidade no Sector*/                                cb.sum(cb.selectCase().when(cb.le(AnoDeAntiguidade, 1), 1).otherwise(0).as(Integer.class)).alias("ant_ate_1_ano"),
                                                              cb.sum(cb.selectCase().when(cb.le(AnoDeAntiguidade, 1), 0).when(cb.le(AnoDeAntiguidade, 5), 1).otherwise(0).as(Integer.class)).alias("ant_de_1_Ate_5_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(AnoDeAntiguidade, 5), 0).when(cb.le(AnoDeAntiguidade, 10), 1).otherwise(0).as(Integer.class)).alias("ant_de_5_Ate_10_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(AnoDeAntiguidade, 10), 0).when(cb.le(AnoDeAntiguidade, 15), 1).otherwise(0).as(Integer.class)).alias("ant_de_10_Ate_15_anos"),
                                                              cb.sum(cb.selectCase().when(cb.gt(AnoDeAntiguidade, 15), 1).otherwise(0).as(Integer.class)).alias("ant_mais_de_15_anos"),
     /* Media Etária*/                                        cb.avg(cb.function("floor",Integer.class,cb.quot(cb.function("months_between",Double.class, cb.currentDate(), root.get(InfoEmpresas_.cadastros).get(Cadastros_.dtNasc)),12))).alias("media"),
     /*Estrutura Etária*/                                     cb.sum(cb.selectCase().when(cb.le(AnosDeNascimento, 24), 1).otherwise(0).as(Integer.class)).alias("ate_24_ano"),
                                                              cb.sum(cb.selectCase().when(cb.le(AnosDeNascimento, 24), 0).when(cb.le(AnosDeNascimento, 34), 1).otherwise(0).as(Integer.class)).alias("de_25_Ate_34_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(AnosDeNascimento, 34), 0).when(cb.le(AnosDeNascimento, 44), 1).otherwise(0).as(Integer.class)).alias("de_35_Ate_44_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(AnosDeNascimento, 44), 0).when(cb.le(AnosDeNascimento, 54), 1).otherwise(0).as(Integer.class)).alias("de_45_Ate_54_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(AnosDeNascimento, 54), 0).when(cb.le(AnosDeNascimento, 65), 1).otherwise(0).as(Integer.class)).alias("de_55_Ate_65_anos"),
                                                              cb.sum(cb.selectCase().when(cb.gt(AnosDeNascimento, 65), 1).otherwise(0).as(Integer.class)).alias("mais_de_65_anos"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdHabLit).get(HabsLiters_.dsAbrv)).when("1ºCl Ens B", 1).otherwise(0)).alias("1Cl_Ens_B"),
     /* Habilitações */                                       cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdHabLit).get(HabsLiters_.dsAbrv)).when("2ºCl Ens B", 1).otherwise(0)).alias("2Cl_Ens_B"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdHabLit).get(HabsLiters_.dsAbrv)).when("3ºCl Ens B", 1).otherwise(0)).alias("3Cl_Ens_B"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdHabLit).get(HabsLiters_.dsAbrv)).when("Ens Sec", 1).otherwise(0)).alias("Ens_Sec"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdHabLit).get(HabsLiters_.dsAbrv)).when("Bacharel", 1).otherwise(0)).alias("Bacharel"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdHabLit).get(HabsLiters_.dsAbrv)).when("Licenciad", 1).otherwise(0)).alias("Licenciad")                
                                                              );       
        
        return subDoTotalporEmpresa(cq, cb, root, compoundSelection);

 }         
       
       

 public List<Tuple> subDoTotalporEmpresa(CriteriaQuery<Tuple> cq, CriteriaBuilder cb, Root root, CompoundSelection<Tuple> compoundSelection){
   
       javax.persistence.criteria.Predicate[] predicate = new Predicate[2]  ;       
       predicate[0] = cb.like(root.get(InfoEmpresas_.estado), "A");
       predicate[1] = cb.like(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdSituacao).get(Situacoes_.mapaMess), "S");

       Expression[] agrupar = new Expression[1];
       agrupar[0] = root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE) ;   
        
       Order[] ordenar = {cb.asc(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE))};

       return findCriteria(null, compoundSelection, predicate, ordenar, agrupar);
 }    
 
 
 //------------------------------------------------------------------------------     
//-----------------------por Direcao Tableau de bord----------------------------
//------------------------------------------------------------------------------     
     
 public List<Tuple> aoServicoPorDirecao(int[] range, String sortField, SortOrder sortOder, Map filters){
     
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        javax.persistence.criteria.Root root = cq.from(entityClass);

        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE).alias("siglaE"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.cdSbu).alias("cdSbu"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsAbrv).alias("dsAbrv"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsComp).alias("dsComp"),                
                                                              cb.count(root.get(InfoEmpresas_.cadastros).get(Cadastros_.nre)).alias("total"));       

        return subDoTotalporDirecao(range, sortField, sortOder, filters, cq, cb, root, compoundSelection);
        
 }    
 
  public List<Tuple> porGeneroPorDirecao(int[] range, String sortField, SortOrder sortOder, Map filters){
     
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Tuple> cq = cb.createTupleQuery();         
        javax.persistence.criteria.Root root = cq.from(entityClass);
   

        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE).alias("siglaE"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.cdSbu).alias("cdSbu"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsAbrv).alias("dsAbrv"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsComp).alias("dsComp"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.sexo)).when("M", 1).otherwise(0)).alias("M"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.sexo)).when( "F", 1).otherwise(0)).alias("F"),
                                                              cb.count(root.get(InfoEmpresas_.cadastros).get(Cadastros_.nre)).alias("total"));       
        
        return subDoTotalporDirecao(range, sortField, sortOder, filters, cq, cb, root, compoundSelection);

 }    

   public List<Tuple> porMediaEtariaPorDirecao(int[] range, String sortField, SortOrder sortOder, Map filters){
     
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        javax.persistence.criteria.Root root = cq.from(entityClass);

        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE).alias("siglaE"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.cdSbu).alias("cdSbu"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsAbrv).alias("dsAbrv"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsComp).alias("dsComp"),
                                                              cb.avg(cb.function("floor",Integer.class,cb.quot(cb.function("months_between",Double.class, cb.currentDate(), root.get(InfoEmpresas_.cadastros).get(Cadastros_.dtNasc)),12))).alias("media"));       
        
       return subDoTotalporDirecao(range, sortField, sortOder, filters, cq, cb, root, compoundSelection);

 }    
  
    public List<Tuple> porNivelPorDirecao(int[] range, String sortField, SortOrder sortOder, Map filters){
     
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Tuple> cq = cb.createTupleQuery();         
        javax.persistence.criteria.Root root = cq.from(entityClass);
         
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE).alias("siglaE"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.cdSbu).alias("cdSbu"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsAbrv).alias("dsAbrv"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsComp).alias("dsComp"),  
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.cdNivel)).when(1, 1).when(2, 1).when(3, 1).when(4, 1).otherwise(0)).alias("1_a_4"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.cdNivel)).when(5, 1).when(6, 1).when(7, 1).when(8, 1).otherwise(0)).alias("5_a_8"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.cdNivel)).when(9, 1).when(10, 1).when(11, 1).when(12, 1).otherwise(0)).alias("9_a_12"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.cdNivel)).when(13, 1).when(14, 1).when(15, 1).otherwise(0)).alias("13_a_15"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.cdNivel)).when(16, 1).when(17, 1).when(18, 1).otherwise(0)).alias("16_a_18"),
                                                              cb.count(root.get(InfoEmpresas_.cadastros).get(Cadastros_.nre)).alias("total"));       
        
        return subDoTotalporDirecao(range, sortField, sortOder, filters, cq, cb, root, compoundSelection);

 }   
        
    
     public List<Tuple> porAntiguidadePorDirecao(int[] range, String sortField, SortOrder sortOder, Map filters){
        
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Tuple> cq = cb.createTupleQuery();        
        javax.persistence.criteria.Root root = cq.from(entityClass);
         
        Expression<Number> anosdeAdmissao = cb.quot(cb.function("months_between",Double.class, cb.currentDate(), root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.dtAdmiss)),12); 
         
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE).alias("siglaE"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.cdSbu).alias("cdSbu"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsAbrv).alias("dsAbrv"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsComp).alias("dsComp"),          
                                                              cb.sum(cb.selectCase().when(cb.le(anosdeAdmissao, 1), 1).otherwise(0).as(Integer.class)).alias("ate_1_ano"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosdeAdmissao, 1), 0).when(cb.le(anosdeAdmissao, 5), 1).otherwise(0).as(Integer.class)).alias("de_1_Ate_5_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosdeAdmissao, 5), 0).when(cb.le(anosdeAdmissao, 10), 1).otherwise(0).as(Integer.class)).alias("de_5_Ate_10_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosdeAdmissao, 10), 0).when(cb.le(anosdeAdmissao, 15), 1).otherwise(0).as(Integer.class)).alias("de_10_Ate_15_anos"),
                                                              cb.sum(cb.selectCase().when(cb.gt(anosdeAdmissao, 15), 1).otherwise(0).as(Integer.class)).alias("mais_de_15_anos"),
                                                              cb.count(root.get(InfoEmpresas_.cadastros).get(Cadastros_.nre)).alias("total"));       
        
        return subDoTotalporDirecao(range, sortField, sortOder, filters, cq, cb, root, compoundSelection);
        
        
 }      
 
      public List<Tuple> porAntiguidadeBancaPorDirecao(int[] range, String sortField, SortOrder sortOder, Map filters){
        
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Tuple> cq = cb.createTupleQuery();         
        javax.persistence.criteria.Root root = cq.from(entityClass);

        Expression<Number> AnoDeAntiguidade = cb.quot(cb.function("months_between",Double.class, cb.currentDate(), root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.dtAdmssAnt)),12); 
         
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE).alias("siglaE"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.cdSbu).alias("cdSbu"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsAbrv).alias("dsAbrv"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsComp).alias("dsComp"),                
                                                              cb.sum(cb.selectCase().when(cb.le(AnoDeAntiguidade, 1), 1).otherwise(0).as(Integer.class)).alias("ate_1_ano"),
                                                              cb.sum(cb.selectCase().when(cb.le(AnoDeAntiguidade, 1), 0).when(cb.le(AnoDeAntiguidade, 5), 1).otherwise(0).as(Integer.class)).alias("de_1_Ate_5_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(AnoDeAntiguidade, 5), 0).when(cb.le(AnoDeAntiguidade, 10), 1).otherwise(0).as(Integer.class)).alias("de_5_Ate_10_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(AnoDeAntiguidade, 10), 0).when(cb.le(AnoDeAntiguidade, 15), 1).otherwise(0).as(Integer.class)).alias("de_10_Ate_15_anos"),
                                                              cb.sum(cb.selectCase().when(cb.gt(AnoDeAntiguidade, 15), 1).otherwise(0).as(Integer.class)).alias("mais_de_15_anos"),
                                                              cb.count(root.get(InfoEmpresas_.cadastros).get(Cadastros_.nre)).alias("total"));       
        return subDoTotalporDirecao(range, sortField, sortOder, filters, cq, cb, root, compoundSelection);
 }    
 
      
      public List<Tuple> porEstruturaEtariaPorDirecao(int[] range, String sortField, SortOrder sortOder, Map filters){         
        
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        javax.persistence.criteria.Root root = cq.from(entityClass);

        Expression<Number> AnosDeNascimento = cb.quot(cb.function("months_between",Double.class, cb.currentDate(), root.get(InfoEmpresas_.cadastros).get(Cadastros_.dtNasc)),12); 
         
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE).alias("siglaE"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.cdSbu).alias("cdSbu"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsAbrv).alias("dsAbrv"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsComp).alias("dsComp"),                
                                                              cb.sum(cb.selectCase().when(cb.le(AnosDeNascimento, 24), 1).otherwise(0).as(Integer.class)).alias("ate_24_ano"),
                                                              cb.sum(cb.selectCase().when(cb.le(AnosDeNascimento, 24), 0).when(cb.le(AnosDeNascimento, 34), 1).otherwise(0).as(Integer.class)).alias("de_25_Ate_34_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(AnosDeNascimento, 34), 0).when(cb.le(AnosDeNascimento, 44), 1).otherwise(0).as(Integer.class)).alias("de_35_Ate_44_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(AnosDeNascimento, 44), 0).when(cb.le(AnosDeNascimento, 54), 1).otherwise(0).as(Integer.class)).alias("de_45_Ate_54_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(AnosDeNascimento, 54), 0).when(cb.le(AnosDeNascimento, 65), 1).otherwise(0).as(Integer.class)).alias("de_55_Ate_65_anos"),
                                                              cb.sum(cb.selectCase().when(cb.gt(AnosDeNascimento, 65), 1).otherwise(0).as(Integer.class)).alias("mais_de_65_anos"),
                                                              cb.count(root.get(InfoEmpresas_.cadastros).get(Cadastros_.nre)).alias("total"));       
        
        return subDoTotalporDirecao(range, sortField, sortOder, filters, cq, cb, root, compoundSelection);
 }      
      
       public List<Tuple> porHabilitacaoPorDirecao(int[] range, String sortField, SortOrder sortOder, Map filters){         
        
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Tuple> cq = cb.createTupleQuery(); 
        
        javax.persistence.criteria.Root root = cq.from(entityClass);
        
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE).alias("siglaE"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.cdSbu).alias("cdSbu"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsAbrv).alias("dsAbrv"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsComp).alias("dsComp"),                
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdHabLit).get(HabsLiters_.dsAbrv)).when("1ºCl Ens B", 1).otherwise(0)).alias("1Cl_Ens_B"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdHabLit).get(HabsLiters_.dsAbrv)).when("2ºCl Ens B", 1).otherwise(0)).alias("2Cl_Ens_B"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdHabLit).get(HabsLiters_.dsAbrv)).when("3ºCl Ens B", 1).otherwise(0)).alias("3Cl_Ens_B"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdHabLit).get(HabsLiters_.dsAbrv)).when("Ens Sec", 1).otherwise(0)).alias("Ens_Sec"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdHabLit).get(HabsLiters_.dsAbrv)).when("Bacharel", 1).otherwise(0)).alias("Bacharel"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdHabLit).get(HabsLiters_.dsAbrv)).when("Licenciad", 1).otherwise(0)).alias("Licenciad"),
                                                              cb.count(root.get(InfoEmpresas_.cadastros).get(Cadastros_.nre)).alias("total"));       
        
        return subDoTotalporDirecao(range, sortField, sortOder, filters, cq, cb, root, compoundSelection);
        
 }  
       
   public List<Tuple> porSitGeoPorDirecao(int[] range, String sortField, SortOrder sortOder, Map filters){
     
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Tuple> cq = cb.createTupleQuery();         
        javax.persistence.criteria.Root root = cq.from(entityClass);
   

        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE).alias("siglaE"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.cdSbu).alias("cdSbu"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsAbrv).alias("dsAbrv"),
                                                              root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsComp).alias("dsComp"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.qacEstabelecimentos).get(QacEstabelecimentos_.sitGeo)).when("C", 1).otherwise(0)).alias("C"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.qacEstabelecimentos).get(QacEstabelecimentos_.sitGeo)).when("M", 1).otherwise(0)).alias("M"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.qacEstabelecimentos).get(QacEstabelecimentos_.sitGeo)).when("A", 1).otherwise(0)).alias("A"),
                                                              cb.sum(cb.selectCase(root.get(InfoEmpresas_.cadastros).get(Cadastros_.infoGeral).get(InfoGeral_.qacEstabelecimentos).get(QacEstabelecimentos_.sitGeo)).when("C", 0).when("M", 0).when("A", 0).otherwise(1)).alias("O"),
                                                              cb.count(root.get(InfoEmpresas_.cadastros).get(Cadastros_.nre)).alias("total"));       
        
        return subDoTotalporDirecao(range, sortField, sortOder, filters, cq, cb, root, compoundSelection);

 }    
      
       
       

 public List<Tuple> subDoTotalporDirecao(int[] range, String sortField, SortOrder sortOder, Map filters, CriteriaQuery<Tuple> cq, CriteriaBuilder cb, Root root, CompoundSelection<Tuple> compoundSelection){
     
       int incialpredicate = 2;
       Predicate[] predicateFixo = new Predicate[incialpredicate]  ;     
       predicateFixo[0] = cb.like(root.get(InfoEmpresas_.estado), "A");
       predicateFixo[1] = cb.like(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdSituacao).get(Situacoes_.mapaMess), "S");

//       Predicate condicaoFixa = cb.and(predicateFixo);
         
//       Predicate condicaoDinamica = addChoicePredicate(new Predicate[filters.size()], 0, filters, cb, root);
       
         Predicate condicao = null;
         if (!filters.isEmpty() 
           || (filters.containsKey("globalFilter") && "".equals(filters.get("globalFilter").toString()) && filters.containsValue("%")) ){
             condicao = makePredicate(predicateFixo, filters, cb, root);
         }             

  
       Expression[] compoundGroup = {root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE),
                                     root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.cdSbu),
                                     root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsAbrv),
                                     root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.dsComp)} ;   
       
        Order[] ordenar = {cb.asc(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE)),
                           cb.asc(root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.cdSbu).get(QacAreasNegocio_.cdSbu))};
                
        makeOrder(ordenar, sortField, sortOder, cb, root);
        
//      return findCriteria(range, compoundSelection, condicaoFixa, condicaoDinamica, ordenar, compoundGroup);
      
       if (condicao!=null){
            return findCriteria(range, compoundSelection, condicao, ordenar, compoundGroup);    
       }else{   
            return findCriteria(range, compoundSelection, predicateFixo, ordenar, compoundGroup);
       }

      
      
 }
    
    public List<Object[]> chefiasDemitidos(){
 
/*        String sql ="SELECT " +
                    "     BANIF.INFO_EMPRESAS.CD_ESTRUTURA, " +
                    "     BANIF.QAC_ESTRUTURAS.DS_COMP, " +
                    "     BANIF.QAC_ESTRUTURAS.MANAGER, " +
                    "     CHEFIAS.NOME_COMP,\n" +
                    "     BANIF.QAC_ESTRUTURAS.SIGLA_E, " +
                    "     BANIF.INFO_EMPRESAS.SIGLA_E, " +
                    "     BANIF.INFO_EMPRESAS.CAD_NRE, " +
                    "     COLABORADORES.NOME_COMP " +
                    "FROM " +
                    "     BANIF.CADASTROS COLABORADORES LEFT OUTER JOIN BANIF.INFO_EMPRESAS ON COLABORADORES.NRE = BANIF.INFO_EMPRESAS.CAD_NRE " +
                    "     LEFT OUTER JOIN BANIF.QAC_ESTRUTURAS ON BANIF.INFO_EMPRESAS.CD_ESTRUTURA = BANIF.QAC_ESTRUTURAS.CD_ESTRUTURA " +
                    "     LEFT OUTER JOIN BANIF.CADASTROS CHEFIAS ON BANIF.QAC_ESTRUTURAS.MANAGER = CHEFIAS.NRE " +
                    "WHERE " +
                    "     (BANIF.QAC_ESTRUTURAS.PONTUACAO <> 'FICTICIA' " +
                    "  OR BANIF.QAC_ESTRUTURAS.PONTUACAO IS NULL) " +
                    " AND BANIF.INFO_EMPRESAS.ESTADO = 'A' " +
                    " AND COLABORADORES.CD_SITUACAO < 40 " +
                    " AND CHEFIAS.CD_SITUACAO > 58 " +
                    "ORDER BY " +
                    "     BANIF.INFO_EMPRESAS.CD_ESTRUTURA ASC";
  */
        
      String sql =      "SELECT\n" +
                        "    DISTINCT \n" +
                        "    CASE WHEN M1.CD_SITUACAO > 59 THEN M1.NRE ELSE NULL END AS M1, \n" +
                        "    CASE WHEN M1.CD_SITUACAO > 59 THEN M1.NOME_ABRV ELSE NULL END AS N1,\n" +
                        "    CASE WHEN M2.CD_SITUACAO > 59 THEN M2.NRE ELSE NULL END AS M2, \n" +
                        "    CASE WHEN M2.CD_SITUACAO > 59 THEN M2.NOME_ABRV ELSE NULL END AS N2,\n" +
                        "    CASE WHEN M3.CD_SITUACAO > 59 THEN M3.NRE ELSE NULL END AS M3, \n" +
                        "    CASE WHEN M3.CD_SITUACAO > 59 THEN M3.NOME_ABRV ELSE NULL END AS N3,\n" +
                        "    CASE WHEN M4.CD_SITUACAO > 59 THEN M4.NRE ELSE NULL END AS M4, \n" +
                        "    CASE WHEN M4.CD_SITUACAO > 59 THEN M4.NOME_ABRV ELSE NULL END AS N4\n" +
                        "FROM\n" +
                        "     BANIF.CADASTROS EMPS INNER JOIN BANIF.INFO_EMPRESAS ON EMPS.NRE = BANIF.INFO_EMPRESAS.CAD_NRE\n" +
                        "     INNER JOIN BANIF.QAC_ESTRUTURAS QE1 ON BANIF.INFO_EMPRESAS.CD_ESTRUTURA = QE1.CD_ESTRUTURA\n" +
                        "     AND QE1.SIGLA_E = BANIF.INFO_EMPRESAS.SIGLA_E\n" +
                        "     LEFT OUTER JOIN BANIF.CADASTROS M1 ON QE1.MANAGER = M1.NRE\n" +
                        "     LEFT OUTER JOIN BANIF.QAC_ESTRUTURAS QE2 ON QE1.SIGLA_ESTRUTURA_PAI = QE2.SIGLA_E\n" +
                        "     AND QE1.CD_ESTRUTURA_PAI = QE2.CD_ESTRUTURA\n" +
                        "     LEFT OUTER JOIN BANIF.CADASTROS M2 ON QE2.MANAGER = M2.NRE\n" +
                        "     LEFT OUTER JOIN BANIF.QAC_ESTRUTURAS QE3 ON QE2.SIGLA_ESTRUTURA_PAI = QE3.SIGLA_E\n" +
                        "     AND QE2.CD_ESTRUTURA_PAI = QE3.CD_ESTRUTURA\n" +
                        "     LEFT OUTER JOIN BANIF.CADASTROS M3 ON QE3.MANAGER = M3.NRE\n" +
                        "     LEFT OUTER JOIN BANIF.QAC_ESTRUTURAS QE4 ON QE3.SIGLA_ESTRUTURA_PAI = QE4.SIGLA_E\n" +
                        "     AND QE3.CD_ESTRUTURA_PAI = QE4.CD_ESTRUTURA\n" +
                        "     LEFT OUTER JOIN BANIF.CADASTROS M4 ON QE4.MANAGER = M4.NRE\n" +
                        "     LEFT OUTER JOIN BANIF.QAC_ESTRUTURAS QE5 ON QE4.SIGLA_ESTRUTURA_PAI = QE5.SIGLA_E\n" +
                        "     AND QE4.CD_ESTRUTURA_PAI = QE5.CD_ESTRUTURA\n" +
                        "     LEFT OUTER JOIN BANIF.CADASTROS M5 ON QE5.MANAGER = M5.NRE\n" +
                        "     LEFT OUTER JOIN BANIF.QAC_ESTRUTURAS QE6 ON QE5.SIGLA_ESTRUTURA_PAI = QE6.SIGLA_E\n" +
                        "     AND QE5.CD_ESTRUTURA_PAI = QE6.CD_ESTRUTURA\n" +
                        "     LEFT OUTER JOIN BANIF.CADASTROS M6 ON QE6.MANAGER = M6.NRE\n" +
                        "WHERE\n" +
                        "     EMPS.CD_SITUACAO < 40\n" +
                        " AND BANIF.INFO_EMPRESAS.ESTADO = 'A'\n" +
                        " AND ((M1.CD_SITUACAO > 59)\n" +
                        "  OR (M2.CD_SITUACAO > 59)\n" +
                        "  OR (M3.CD_SITUACAO > 59)\n" +
                        "  OR (M4.CD_SITUACAO > 59)\n" +
                        "  OR (M5.CD_SITUACAO > 59)\n" +
                        "  OR (M6.CD_SITUACAO > 59))";
        
        
        
        
        
        Query createNativeQuery = getEntityManager().createNativeQuery(sql);
        
//        createNativeQuery.setParameter(1, empresa);
//        createNativeQuery.setParameter(2, ano);
//        createNativeQuery.setParameter(3, mes);
        List<Object[]> lista = createNativeQuery.getResultList();
        return lista;
        
   }
 
    
   public List<InfoEmpresas> semEstruturaActiva(int[] range, String sortField, SortOrder sortOder, Map filters) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        javax.persistence.criteria.Root root = cq.from(entityClass);
        
        
        int incialpredicate = 3;
         javax.persistence.criteria.Predicate[] predicateFixo = new Predicate[incialpredicate]  ;       
         predicateFixo[0] = cb.like(root.get(InfoEmpresas_.estado), "A");
         predicateFixo[1] = cb.lessThan(root.get(InfoEmpresas_.cadastros).get(Cadastros_.cdSituacao).get(Situacoes_.cdSituacao), 60);
         predicateFixo[2] = cb.like(cb.upper(root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.pontuacao)),"INACTIVA");

//         Predicate condicaoFixa = cb.and(predicateFixo);
         
//         Predicate condicaoDinamica = addChoicePredicate(new Predicate[filters.size()], 0, filters, cb, root);

         Predicate condicao = null;
         if (!filters.isEmpty() 
           || (filters.containsKey("globalFilter") && "".equals(filters.get("globalFilter").toString()) && filters.containsValue("%")) ){
             condicao = makePredicate(predicateFixo, filters, cb, root);
         }             

         
         
         
         Order[] ordenar = {cb.asc(root.get(InfoEmpresas_.infoEmpresasPK).get(InfoEmpresasPK_.siglaE)),
                           cb.asc(root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.nrOrdem)),
                           cb.asc(root.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.qacEstruturasPK).get(QacEstruturasPK_.cdEstrutura)), 
                           cb.asc(root.join(InfoEmpresas_.cadastros, JoinType.LEFT ).join(Cadastros_.infoGeral, JoinType.LEFT).get(InfoGeral_.cdGrupo)),
                           cb.asc(root.join(InfoEmpresas_.cadastros, JoinType.LEFT ).join(Cadastros_.infoProfs, JoinType.LEFT ).join(InfoProfs_.cdFuncInt, JoinType.LEFT ).get(FuncoesInternas_.preExperiencia)),                           
                           cb.desc(root.join(InfoEmpresas_.cadastros, JoinType.LEFT ).join(Cadastros_.infoProfs, JoinType.LEFT ).join(InfoProfs_.cdFuncInt, JoinType.LEFT ).get(FuncoesInternas_.cdFuncInt)),                           
                           cb.desc(root.join(InfoEmpresas_.cadastros, JoinType.LEFT ).join(Cadastros_.infoGeral, JoinType.LEFT ).get(InfoGeral_.cdNivel))
                            };

        makeOrder(ordenar, sortField, sortOder, cb, root);
        
//        return findCriteria(range, condicaoFixa, condicaoDinamica, ordenar);
        
       if (condicao!=null){
            return findCriteria(range, condicao, ordenar);    
       }else{   
            return findCriteria(range, predicateFixo, ordenar);
       }

    }
       
    public int countSemEstruturaActiva() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root rt = cq.from(entityClass);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        
       Predicate[] condicao = {cb.like(rt.get(InfoEmpresas_.estado), "A"), 
                                cb.lessThan(rt.get(InfoEmpresas_.cadastros).get(Cadastros_.cdSituacao).get(Situacoes_.cdSituacao), 60),
                                cb.like(cb.upper(rt.get(InfoEmpresas_.qacEstruturas).get(QacEstruturas_.pontuacao)),"INACTIVA")};
         cq.where(condicao);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
   
    }   
       
     
  

   
 
    
    
    
}
