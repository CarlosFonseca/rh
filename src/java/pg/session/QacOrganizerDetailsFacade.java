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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.primefaces.model.SortOrder;
import pg.entities.*;
 
/**
 *
 * @author bnf02107
 */
@Stateless
public class QacOrganizerDetailsFacade extends AbstractFacade<QacOrganizerDetails> {
    @PersistenceContext(unitName = "RHSPUPG")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public QacOrganizerDetailsFacade() {
        super(QacOrganizerDetails.class);
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
        predicateFixo[0] = cb.like(root.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.cdOut),"9999");
        predicateFixo[1] = cb.like(root.get(QacOrganizerDetails_.estruturaAtomica),"1");

//        Predicate condicaoFixa = cb.and(predicateFixo);
         
//        Predicate condicaoDinamica = addChoicePredicate(new Predicate[filters.size()], 0, filters, cb, root);
         
        Predicate condicao = null;
        if (!filters.isEmpty() 
           || (filters.containsKey("globalFilter") && "".equals(filters.get("globalFilter").toString()) && filters.containsValue("%")) ){
             condicao = makePredicate(predicateFixo, filters, cb, root);
        }             

        
        Order[] ordenar = {cb.asc(cb.lower(root.get(QacOrganizerDetails_.vlr38))), // Empresa Esturtura
                           cb.desc(root.get(QacOrganizerDetails_.initState)),
                           cb.desc(root.get(QacOrganizerDetails_.level)),
                           cb.asc(root.get(QacOrganizerDetails_.vlr29))}; //Direcção

        makeOrder(ordenar, sortField, sortOder, cb, root);


        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(QacOrganizerDetails_.vlr38).alias("siglaE"), // Empresa Esturtura
                                                              root.get(QacOrganizerDetails_.initState).alias("ano"),
                                                              root.get(QacOrganizerDetails_.level).alias("mes"),
                                                              root.get(QacOrganizerDetails_.vlr28).alias("cdSbu"),
                                                              root.get(QacOrganizerDetails_.vlr33).alias("dsAbrv"),
                                                              root.get(QacOrganizerDetails_.vlr29).alias("dsComp"),
                                                              root.join(QacOrganizerDetails_.qacEmpresasEstrutura, JoinType.LEFT).get(QacEmpresas_.dsSocial).alias("dsSocial"),
                                                              root.get(QacOrganizerDetails_.vlr29).alias("dsDireccao"));

//          return findCriteria(range, compoundSelection, condicaoFixa, condicaoDinamica, ordenar, true);
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
        Predicate[] condicao = {cb.like(rt.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.cdOut),"9999"),
                                cb.like(rt.get(QacOrganizerDetails_.estruturaAtomica),"1")};
        cq.where(condicao);
        cq.select(cb.countDistinct(cb.concat(rt.get(QacOrganizerDetails_.empresa), 
                                             cb.concat(rt.get(QacOrganizerDetails_.vlr29), //direcção
                                                        cb.concat(rt.get(QacOrganizerDetails_.initState),
                                                                  rt.get(QacOrganizerDetails_.level))))));
        
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }   
    
    //public List findByDireccao(String Empresa, String Direccao, Integer ano, Integer mes ){
    public List findByDireccao(String Direccao, Integer ano, Integer mes ){
            
        
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        Root rt = cb.createQuery().from(entityClass);

        Predicate[] condicao = {cb.like(rt.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.cdOut),"9999"),
                                      // cb.like(rt.get(QacOrganizerDetails_.empresa), Empresa),
                                       cb.like(rt.get(QacOrganizerDetails_.vlr28), Direccao),
                                       cb.equal(rt.get(QacOrganizerDetails_.initState), ano),
                                       cb.equal(rt.get(QacOrganizerDetails_.level), mes) };
 
        return doFindBy(cb, rt, condicao);
        
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
         predicateFixo[0] = cb.like(root.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.cdOut),"9999");
         predicateFixo[1] = cb.like(root.get(QacOrganizerDetails_.estruturaAtomica),"1");

//        Predicate condicaoFixa = cb.and(predicateFixo);
         
//        Predicate condicaoDinamica = addChoicePredicate(new Predicate[filters.size()], 0, filters, cb, root);

        Predicate condicao = null;
        if (!filters.isEmpty() 
           || (filters.containsKey("globalFilter") && "".equals(filters.get("globalFilter").toString()) && filters.containsValue("%")) ){
             condicao = makePredicate(predicateFixo, filters, cb, root);
        }             
        
        
        Order[] ordenar = {cb.asc(cb.lower(root.get(QacOrganizerDetails_.empresa))),
                           cb.desc(root.get(QacOrganizerDetails_.initState)),
                           cb.desc(root.get(QacOrganizerDetails_.level)),
                           cb.asc(root.get(QacOrganizerDetails_.vlr32))}; // DS Estrutura
        
        makeOrder(ordenar, sortField, sortOder, cb, root);

        
         CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(QacOrganizerDetails_.empresa).alias("siglaE"),
                                                              root.get(QacOrganizerDetails_.initState).alias("ano"),
                                                              root.get(QacOrganizerDetails_.level).alias("mes"),
                                                              root.get(QacOrganizerDetails_.vlr30).alias("cdEstrutura"),
                                                              root.get(QacOrganizerDetails_.vlr33).alias("dsAbrv"),    // Estrutura Abreviada
                                                              root.get(QacOrganizerDetails_.vlr32).alias("dsComp"),  // Estrutura
                                                              root.join(QacOrganizerDetails_.qacEmpresas,JoinType.LEFT).get(QacEmpresas_.dsSocial).alias("dsSocial"),
                                                              root.get(QacOrganizerDetails_.vlr29).alias("dsDireccao"));

//          return findCriteria(range, compoundSelection, condicaoFixa, condicaoDinamica, ordenar, true);
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
        Predicate[] condicao = {cb.like(rt.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.cdOut),"9999"),
                                cb.like(rt.get(QacOrganizerDetails_.estruturaAtomica),"1")};

        cq.where(condicao);
        cq.select(cb.countDistinct(cb.concat(rt.get(QacOrganizerDetails_.empresa), 
                                             cb.concat(rt.get(QacOrganizerDetails_.vlr32), //ds_comp  Estrutura
                                                        cb.concat(rt.get(QacOrganizerDetails_.initState),
                                                                  rt.get(QacOrganizerDetails_.level))))));

        
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }  
    
   public List findByEstrutura(String Empresa, String cdEstrutura, Integer ano, Integer mes){
        
        
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        Root rt = cb.createQuery().from(entityClass);

        Predicate[] condicao = {cb.like(rt.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.cdOut),"9999"),
                                       cb.like(rt.get(QacOrganizerDetails_.empresa), Empresa),
                                       cb.like(rt.get(QacOrganizerDetails_.vlr30), cdEstrutura),
                                       cb.equal(rt.get(QacOrganizerDetails_.initState), ano),
                                       cb.equal(rt.get(QacOrganizerDetails_.level), mes)};
        
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
         predicateFixo[0] = cb.like(root.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.cdOut),"9999");
         predicateFixo[1] = cb.like(root.get(QacOrganizerDetails_.estruturaAtomica),"1");
         
//        Predicate condicaoFixa = cb.and(predicateFixo);
         
//        Predicate condicaoDinamica = addChoicePredicate(new Predicate[filters.size()], 0, filters, cb, root);
        
        Predicate condicao = null;
        if (!filters.isEmpty() 
           || (filters.containsKey("globalFilter") && "".equals(filters.get("globalFilter").toString()) && filters.containsValue("%")) ){
             condicao = makePredicate(predicateFixo, filters, cb, root);
        }             
        
        Order[] ordenar = {cb.asc(cb.lower(root.get(QacOrganizerDetails_.empresa))),
                           cb.desc(root.get(QacOrganizerDetails_.initState)),
                           cb.desc(root.get(QacOrganizerDetails_.level)),
                           cb.asc(root.get(QacOrganizerDetails_.vlr32)), // DS Estrutura
                           cb.asc(root.get(QacOrganizerDetails_.vlr26)) }; // cd estab

        makeOrder(ordenar, sortField, sortOder, cb, root);
        
        
          CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(QacOrganizerDetails_.empresa).alias("siglaE"),
                                                              root.get(QacOrganizerDetails_.initState).alias("ano"),
                                                              root.get(QacOrganizerDetails_.level).alias("mes"),
                                                              root.get(QacOrganizerDetails_.vlr28).alias("cdSbu"),
                                                              root.get(QacOrganizerDetails_.vlr33).alias("dsAbrv"),                                                              
                                                              root.get(QacOrganizerDetails_.vlr29).alias("dsComp"),
                                                              root.get(QacOrganizerDetails_.vlr25).alias("siglaEstab"),    // Sigla Estab
                                                              root.get(QacOrganizerDetails_.vlr26).alias("cdEstab"),  // cd Estab
                                                              root.get(QacOrganizerDetails_.vlr27).alias("dsEstab"),  // ds Estab
                                                              root.join(QacOrganizerDetails_.qacEmpresas,JoinType.LEFT).get(QacEmpresas_.dsSocial).alias("dsSocial"),
                                                              root.get(QacOrganizerDetails_.vlr29).alias("dsDireccao"));       

//          return findCriteria(range, compoundSelection, condicaoFixa, condicaoDinamica, ordenar, true);
          
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
       Predicate[] condicao = {cb.like(rt.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.cdOut),"9999"),
                               cb.like(rt.get(QacOrganizerDetails_.estruturaAtomica),"1")};

       cq.where(condicao);
       
       cq.where(condicao);
       cq.select(cb.countDistinct(cb.concat(rt.get(QacOrganizerDetails_.empresa), 
                                             cb.concat(rt.get(QacOrganizerDetails_.vlr29), //ds_comp  Estrutura
                                                        cb.concat(rt.get(QacOrganizerDetails_.initState),
                                                                 cb.concat(rt.get(QacOrganizerDetails_.level), 
                                                                           cb.concat(rt.get(QacOrganizerDetails_.vlr25),
                                                                                     rt.get(QacOrganizerDetails_.vlr26))))))));
       
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }  
    
       public List findByDirecEstab(String Empresa, String Direccao, String Estabelecimento, Integer ano, Integer mes){
        

        
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        Root rt = cb.createQuery().from(entityClass);

        Predicate[] condicao = {cb.like(rt.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.cdOut),"9999"),
                                cb.like(rt.get(QacOrganizerDetails_.estruturaAtomica),"1"),
                                cb.like(rt.get(QacOrganizerDetails_.vlr28), Direccao),
                                cb.like(rt.get(QacOrganizerDetails_.vlr26), Estabelecimento),
                                cb.equal(rt.get(QacOrganizerDetails_.initState), ano),
                                cb.equal(rt.get(QacOrganizerDetails_.level), mes)};
            


        return doFindBy(cb, rt, condicao);
        
    }

       private List doFindBy(CriteriaBuilder cb, Root rt,Predicate[] condicao){
   
        
        Order[] ordenar = {cb.asc(rt.get(QacOrganizerDetails_.vlr30)), // Estrutura
                           cb.asc(rt.get(QacOrganizerDetails_.vlr20)), //grupo
                           cb.asc(rt.get(QacOrganizerDetails_.vlr23)), //Nivel de qualificacao                          
                 //          cb.asc(rt.get(QacOrganizerDetails_.vlr16)),  // Funcao Interna                        
                           cb.desc(rt.get(QacOrganizerDetails_.vlr18)), // Nivel
                           cb.desc(rt.get(QacOrganizerDetails_.vlr21)) // IHT
                            };

        return findCriteria(null,condicao,ordenar);       
    }    
    
    
    
    //-----------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------
    //------------------------ por Empresa Histórico de Indicadores  --------------------------------------
    //-----------------------------------------------------------------------------------------------------

    public int countEmpresaAnoMes() {           
         javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root rt = cq.from(entityClass);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        Predicate[] condicao = {cb.like(rt.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.cdOut),"9999"),
                                cb.like(rt.get(QacOrganizerDetails_.estruturaAtomica),"1")};
        cq.where(condicao);
        cq.select(cb.countDistinct(cb.concat(rt.get(QacOrganizerDetails_.empresa),
                                             cb.concat(rt.get(QacOrganizerDetails_.initState),
                                                       rt.get(QacOrganizerDetails_.level)))));
        
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    
    
    
    
    public List<Tuple> aoServicoPorEmpresa(int[] range, String sortField, SortOrder sortOder, Map filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root root = cq.from(entityClass);
        
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(QacOrganizerDetails_.empresa).alias("empresa"),
                                                              root.get(QacOrganizerDetails_.initState).alias("ano"),
                                                              root.get(QacOrganizerDetails_.level).alias("mes"),
                                                              cb.count(root.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.nrLinha)).alias("total")); 
        
       return subDoTotalPorEmpresa(range, sortField, sortOder, filters, cq, cb, root, compoundSelection); 
   
    }
    
      

    public List<Tuple> porGeneroPorEmpresa(int[] range, String sortField, SortOrder sortOder, Map filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root root = cq.from(entityClass);
        
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(QacOrganizerDetails_.empresa).alias("empresa"),
                                                              root.get(QacOrganizerDetails_.initState).alias("ano"),
                                                              root.get(QacOrganizerDetails_.level).alias("mes"),
                                                              cb.sum(cb.selectCase(root.join(QacOrganizerDetails_.cadastros).get(Cadastros_.sexo)).when("M", 1).otherwise(0)).alias("M"),
                                                              cb.sum(cb.selectCase(root.join(QacOrganizerDetails_.cadastros).get(Cadastros_.sexo)).when( "F", 1).otherwise(0)).alias("F"),
                                                              cb.count(root.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.nrLinha)).alias("total")); 
        
        return subDoTotalPorEmpresa(range, sortField, sortOder, filters, cq, cb, root, compoundSelection); 
    }
    
       
    public List<Tuple> porMediaEtariaPorEmpresa(int[] range, String sortField, SortOrder sortOder, Map filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root root = cq.from(entityClass);
        
        
        Expression<Date> dtMes = cb.function("LAST_DAY",Date.class,cb.function("TO_DATE",Date.class,cb.concat(cb.concat(root.get(QacOrganizerDetails_.initState),"-"), cb.concat(root.get(QacOrganizerDetails_.level), "-28"))));
     
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(QacOrganizerDetails_.empresa).alias("empresa"),
                                                              root.get(QacOrganizerDetails_.initState).alias("ano"),
                                                              root.get(QacOrganizerDetails_.level).alias("mes"),
                                                              cb.avg(cb.function("floor",Integer.class,cb.quot(cb.function("months_between", Double.class, dtMes, root.join(QacOrganizerDetails_.cadastros).get(Cadastros_.dtNasc)),12))).alias("media")); 
        
    return subDoTotalPorEmpresa(range, sortField, sortOder, filters, cq, cb, root, compoundSelection); 
    }
 
    
    public List<Tuple> porNivelPorEmpresa(int[] range, String sortField, SortOrder sortOder, Map filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root root = cq.from(entityClass);
     
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(QacOrganizerDetails_.empresa).alias("empresa"),
                                                              root.get(QacOrganizerDetails_.initState).alias("ano"),
                                                              root.get(QacOrganizerDetails_.level).alias("mes"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr18)).when("1", 1).when("2", 1).when("3", 1).when("4", 1).otherwise(0)).alias("1_a_4"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr18)).when("5", 1).when("6", 1).when("7", 1).when("8", 1).otherwise(0)).alias("5_a_8"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr18)).when("9", 1).when("10", 1).when("11", 1).when("12", 1).otherwise(0)).alias("9_a_12"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr18)).when("13", 1).when("14", 1).when("15", 1).otherwise(0)).alias("13_a_15"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr18)).when("16", 1).when("17", 1).when("18", 1).otherwise(0)).alias("16_a_18"),
                                                              cb.count(root.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.nrLinha)).alias("total")); 
    
        return subDoTotalPorEmpresa(range, sortField, sortOder, filters, cq, cb, root, compoundSelection); 
        
    }

    
    public List<Tuple> porAntiguidadePorEmpresa(int[] range, String sortField, SortOrder sortOder, Map filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root root = cq.from(entityClass);       
        
        Expression<Date> dtMes = cb.function("LAST_DAY",Date.class,cb.function("TO_DATE",Date.class,cb.concat(cb.concat(root.get(QacOrganizerDetails_.initState),"-"), cb.concat(root.get(QacOrganizerDetails_.level), "-28"))));
        
        Expression<Date> dtAdm = cb.function("TO_DATE",Date.class,root.get(QacOrganizerDetails_.vlr12), cb.literal("DD-MM-YYYY")); //Data Admissão
        Expression<Number> anosDeAdmissao = cb.quot(cb.function("months_between",Double.class, dtMes, dtAdm),12); 
     
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(QacOrganizerDetails_.empresa).alias("empresa"),
                                                              root.get(QacOrganizerDetails_.initState).alias("ano"),
                                                              root.get(QacOrganizerDetails_.level).alias("mes"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeAdmissao, 1), 1).otherwise(0).as(Integer.class)).alias("ate_1_ano"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeAdmissao, 1), 0).when(cb.le(anosDeAdmissao, 5), 1).otherwise(0).as(Integer.class)).alias("de_1_Ate_5_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeAdmissao, 5), 0).when(cb.le(anosDeAdmissao, 10), 1).otherwise(0).as(Integer.class)).alias("de_5_Ate_10_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeAdmissao, 10), 0).when(cb.le(anosDeAdmissao, 15), 1).otherwise(0).as(Integer.class)).alias("de_10_Ate_15_anos"),
                                                              cb.sum(cb.selectCase().when(cb.gt(anosDeAdmissao, 15), 1).otherwise(0).as(Integer.class)).alias("mais_de_15_anos"),
                                                              cb.count(root.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.nrLinha)).alias("total")); 
        
        return subDoTotalPorEmpresa(range, sortField, sortOder, filters, cq, cb, root, compoundSelection); 
    }
 
    
    public List<Tuple> porAntiguidadeBancaPorEmpresa(int[] range, String sortField, SortOrder sortOder, Map filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root root = cq.from(entityClass);
        
        Expression<Date> dtMes = cb.function("LAST_DAY",Date.class,cb.function("TO_DATE",Date.class,cb.concat(cb.concat(root.get(QacOrganizerDetails_.initState),"-"), cb.concat(root.get(QacOrganizerDetails_.level), "-28"))));
   
        Expression<Date> dtAntiguidade = cb.function("TO_DATE",Date.class,root.get(QacOrganizerDetails_.vlr13), cb.literal("DD-MM-YYYY"));
        
        Expression<Number> anosDeAntiguidade = cb.quot(cb.function("months_between",Double.class, dtMes, dtAntiguidade),12); 
 
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(QacOrganizerDetails_.empresa).alias("empresa"),
                                                              root.get(QacOrganizerDetails_.initState).alias("ano"),
                                                              root.get(QacOrganizerDetails_.level).alias("mes"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeAntiguidade, 1), 1).otherwise(0).as(Integer.class)).alias("ate_1_ano"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeAntiguidade, 1), 0).when(cb.le(anosDeAntiguidade, 5), 1).otherwise(0).as(Integer.class)).alias("de_1_Ate_5_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeAntiguidade, 5), 0).when(cb.le(anosDeAntiguidade, 10), 1).otherwise(0).as(Integer.class)).alias("de_5_Ate_10_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeAntiguidade, 10), 0).when(cb.le(anosDeAntiguidade, 15), 1).otherwise(0).as(Integer.class)).alias("de_10_Ate_15_anos"),
                                                              cb.sum(cb.selectCase().when(cb.gt(anosDeAntiguidade, 15), 1).otherwise(0).as(Integer.class)).alias("mais_de_15_anos"),
                                                              cb.count(root.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.nrLinha)).alias("total")); 
        
        return subDoTotalPorEmpresa(range, sortField, sortOder, filters, cq, cb, root, compoundSelection); 
    }
    
    public List<Tuple> porEstruturaEtariaPorEmpresa(int[] range, String sortField, SortOrder sortOder, Map filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root root = cq.from(entityClass);
        
        Expression<Date> dtMes = cb.function("LAST_DAY",Date.class,cb.function("TO_DATE",Date.class,cb.concat(cb.concat(root.get(QacOrganizerDetails_.initState),"-"), cb.concat(root.get(QacOrganizerDetails_.level), "-28"))));

        Expression<Number> anosDeNascimento = cb.quot(cb.function("months_between",Double.class, dtMes, root.join(QacOrganizerDetails_.cadastros).get(Cadastros_.dtNasc)),12); 
 
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(QacOrganizerDetails_.empresa).alias("empresa"),
                                                              root.get(QacOrganizerDetails_.initState).alias("ano"),
                                                              root.get(QacOrganizerDetails_.level).alias("mes"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeNascimento, 24), 1).otherwise(0).as(Integer.class)).alias("ate_24_ano"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeNascimento, 24), 0).when(cb.le(anosDeNascimento, 34), 1).otherwise(0).as(Integer.class)).alias("de_25_Ate_34_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeNascimento, 34), 0).when(cb.le(anosDeNascimento, 44), 1).otherwise(0).as(Integer.class)).alias("de_35_Ate_44_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeNascimento, 44), 0).when(cb.le(anosDeNascimento, 54), 1).otherwise(0).as(Integer.class)).alias("de_45_Ate_54_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeNascimento, 54), 0).when(cb.le(anosDeNascimento, 65), 1).otherwise(0).as(Integer.class)).alias("de_55_Ate_65_anos"),
                                                              cb.sum(cb.selectCase().when(cb.gt(anosDeNascimento, 65), 1).otherwise(0).as(Integer.class)).alias("mais_de_65_anos"),
                                                              cb.count(root.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.nrLinha)).alias("total")); 

        return subDoTotalPorEmpresa(range, sortField, sortOder, filters, cq, cb, root, compoundSelection); 
    }   

    public List<Tuple> porHabilitacaoPorEmpresa(int[] range, String sortField, SortOrder sortOder, Map filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root root = cq.from(entityClass);
        
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(QacOrganizerDetails_.empresa).alias("empresa"),
                                                              root.get(QacOrganizerDetails_.initState).alias("ano"),
                                                              root.get(QacOrganizerDetails_.level).alias("mes"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr4)).when("1ºCl Ens B", 1).otherwise(0)).alias("1Cl_Ens_B"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr4)).when("2ºCl Ens B", 1).otherwise(0)).alias("2Cl_Ens_B"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr4)).when("3ºCl Ens B", 1).otherwise(0)).alias("3Cl_Ens_B"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr4)).when("Ens Sec", 1).otherwise(0)).alias("Ens_Sec"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr4)).when("Bacharel", 1).otherwise(0)).alias("Bacharel"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr4)).when("Licenciad", 1).otherwise(0)).alias("Licenciad"),
                                                              cb.count(root.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.nrLinha)).alias("total")); 
        
        return subDoTotalPorEmpresa(range, sortField, sortOder, filters, cq, cb, root, compoundSelection); 
        
    }

    
    
        public List<Tuple> porSitGeoPorEmpresa(int[] range, String sortField, SortOrder sortOder, Map filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root root = cq.from(entityClass);
        
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(QacOrganizerDetails_.empresa).alias("empresa"),
                                                              root.get(QacOrganizerDetails_.initState).alias("ano"),
                                                              root.get(QacOrganizerDetails_.level).alias("mes"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr36)).when("C", 1).otherwise(0)).alias("C"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr36)).when("M", 1).otherwise(0)).alias("M"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr36)).when("A", 1).otherwise(0)).alias("A"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr36)).when("C", 0).when("M", 0).when("A", 0).otherwise(1)).alias("O"),
                                                              cb.count(root.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.nrLinha)).alias("total")); 
        
        return subDoTotalPorEmpresa(range, sortField, sortOder, filters, cq, cb, root, compoundSelection); 
    }

    
    
   
        
    public List<Tuple> subDoTotalPorEmpresa(int[] range, String sortField, SortOrder sortOder, Map filters, CriteriaQuery<Tuple> cq, CriteriaBuilder cb, Root root, CompoundSelection<Tuple> compoundSelection) {

        
         int incialpredicate = 2;
         Predicate[] predicateFixo = new Predicate[incialpredicate]  ;       
         predicateFixo[0] = cb.like(root.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.cdOut),"9999");
         predicateFixo[1] = cb.like(root.get(QacOrganizerDetails_.estruturaAtomica),"1");

//         Predicate condicaoFixa = cb.and(predicateFixo);
         
////        Predicate condicaoDinamica = addChoicePredicate(new Predicate[filters.size()], 0, filters, cb, root);
        
        Predicate condicao = null;
        if (!filters.isEmpty() 
           || (filters.containsKey("globalFilter") && "".equals(filters.get("globalFilter").toString()) && filters.containsValue("%")) ){
             condicao = makePredicate(predicateFixo, filters, cb, root);
        }             

        
         
        Expression expression = null ; //para os filtros dinamicos
        Order[] ordenar = {cb.desc(root.get(QacOrganizerDetails_.initState)),
                           cb.desc(root.get(QacOrganizerDetails_.level)),
                           cb.asc(root.get(QacOrganizerDetails_.empresa)) };
   
        makeOrder(ordenar, sortField, sortOder, cb, root);
        
        Expression[] compoundGroup = {root.get(QacOrganizerDetails_.empresa),
                                      root.get(QacOrganizerDetails_.initState),
                                       root.get(QacOrganizerDetails_.level)};
        
//       return findCriteria(range, compoundSelection, condicaoFixa, condicaoDinamica, ordenar, compoundGroup);
       
       if (condicao!=null){
            return findCriteria(range, compoundSelection, condicao, ordenar, compoundGroup);    
       }else{   
            return findCriteria(range, compoundSelection, predicateFixo, ordenar, compoundGroup);
       }

        
    }
        
 
    
    
    //-----------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------
    //------------------------ por Direcao Histórico de Indicadores  --------------------------------------
    //-----------------------------------------------------------------------------------------------------
    
     public int countDirecaoAnoMes() {           
         javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root rt = cq.from(entityClass);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        Predicate[] condicao = {cb.like(rt.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.cdOut),"9999"),
                                cb.like(rt.get(QacOrganizerDetails_.estruturaAtomica),"1")};
        cq.where(condicao);
        cq.select(cb.countDistinct(cb.concat(rt.get(QacOrganizerDetails_.empresa), 
                                             cb.concat(rt.get(QacOrganizerDetails_.vlr29), //direcção
                                                        cb.concat(rt.get(QacOrganizerDetails_.initState),
                                                                  rt.get(QacOrganizerDetails_.level))))));
        
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    
    
    
    public List<Tuple> aoServicoPorDirecao(int[] range, String sortField, SortOrder sortOder, Map filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root root = cq.from(entityClass);
        
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(QacOrganizerDetails_.empresa).alias("empresa"),
                                                              root.get(QacOrganizerDetails_.initState).alias("ano"),
                                                              root.get(QacOrganizerDetails_.level).alias("mes"),
                                                              root.get(QacOrganizerDetails_.vlr28).alias("cdSbu"),
                                                              root.get(QacOrganizerDetails_.vlr33).alias("dsAbrv"),
                                                              root.get(QacOrganizerDetails_.vlr29).alias("dsComp"),
                                                              cb.count(root.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.nrLinha)).alias("total")); 
        
    return subDoTotalPorDirecao(range, sortField, sortOder, filters, cq, cb, root, compoundSelection); 

    
    }
    
      

    public List<Tuple> porGeneroPorDirecao(int[] range, String sortField, SortOrder sortOder, Map filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root root = cq.from(entityClass);
        
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(QacOrganizerDetails_.empresa).alias("empresa"),
                                                              root.get(QacOrganizerDetails_.initState).alias("ano"),
                                                              root.get(QacOrganizerDetails_.level).alias("mes"),
                                                              root.get(QacOrganizerDetails_.vlr28).alias("cdSbu"),
                                                              root.get(QacOrganizerDetails_.vlr33).alias("dsAbrv"),
                                                              root.get(QacOrganizerDetails_.vlr29).alias("dsComp"),
                                                              cb.sum(cb.selectCase(root.join(QacOrganizerDetails_.cadastros).get(Cadastros_.sexo)).when("M", 1).otherwise(0)).alias("M"),
                                                              cb.sum(cb.selectCase(root.join(QacOrganizerDetails_.cadastros).get(Cadastros_.sexo)).when( "F", 1).otherwise(0)).alias("F"),
                                                              cb.count(root.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.nrLinha)).alias("total")); 
        
        return subDoTotalPorDirecao(range, sortField, sortOder, filters, cq, cb, root, compoundSelection); 
    }
    
       
    public List<Tuple> porMediaEtariaPorDirecao(int[] range, String sortField, SortOrder sortOder, Map filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root root = cq.from(entityClass);
        
        
        Expression<Date> dtMes = cb.function("LAST_DAY",Date.class,cb.function("TO_DATE",Date.class,cb.concat(cb.concat(root.get(QacOrganizerDetails_.initState),"-"), cb.concat(root.get(QacOrganizerDetails_.level), "-28"))));
     
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(QacOrganizerDetails_.empresa).alias("empresa"),
                                                              root.get(QacOrganizerDetails_.initState).alias("ano"),
                                                              root.get(QacOrganizerDetails_.level).alias("mes"),
                                                              root.get(QacOrganizerDetails_.vlr28).alias("cdSbu"),
                                                              root.get(QacOrganizerDetails_.vlr33).alias("dsAbrv"),
                                                              root.get(QacOrganizerDetails_.vlr29).alias("dsComp"),
                                                              cb.avg(cb.function("floor",Integer.class,cb.quot(cb.function("months_between", Double.class, dtMes, root.join(QacOrganizerDetails_.cadastros).get(Cadastros_.dtNasc)),12))).alias("media")); 
        
    return subDoTotalPorDirecao(range, sortField, sortOder, filters, cq, cb, root, compoundSelection); 
    }
 
    
    public List<Tuple> porNivelPorDirecao(int[] range, String sortField, SortOrder sortOder, Map filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root root = cq.from(entityClass);
     
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(QacOrganizerDetails_.empresa).alias("empresa"),
                                                              root.get(QacOrganizerDetails_.initState).alias("ano"),
                                                              root.get(QacOrganizerDetails_.level).alias("mes"),
                                                              root.get(QacOrganizerDetails_.vlr28).alias("cdSbu"),
                                                              root.get(QacOrganizerDetails_.vlr33).alias("dsAbrv"),
                                                              root.get(QacOrganizerDetails_.vlr29).alias("dsComp"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr18)).when("1", 1).when("2", 1).when("3", 1).when("4", 1).otherwise(0)).alias("1_a_4"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr18)).when("5", 1).when("6", 1).when("7", 1).when("8", 1).otherwise(0)).alias("5_a_8"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr18)).when("9", 1).when("10", 1).when("11", 1).when("12", 1).otherwise(0)).alias("9_a_12"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr18)).when("13", 1).when("14", 1).when("15", 1).otherwise(0)).alias("13_a_15"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr18)).when("16", 1).when("17", 1).when("18", 1).otherwise(0)).alias("16_a_18"),
                                                              cb.count(root.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.nrLinha)).alias("total")); 
    
        return subDoTotalPorDirecao(range, sortField, sortOder, filters, cq, cb, root, compoundSelection); 
        
    }

    
    public List<Tuple> porAntiguidadePorDirecao(int[] range, String sortField, SortOrder sortOder, Map filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root root = cq.from(entityClass);       
        
        Expression<Date> dtMes = cb.function("LAST_DAY",Date.class,cb.function("TO_DATE",Date.class,cb.concat(cb.concat(root.get(QacOrganizerDetails_.initState),"-"), cb.concat(root.get(QacOrganizerDetails_.level), "-28"))));
        
        Expression<Date> dtAdm = cb.function("TO_DATE",Date.class,root.get(QacOrganizerDetails_.vlr12), cb.literal("DD-MM-YYYY")); //Data Admissão
        Expression<Number> anosDeAdmissao = cb.quot(cb.function("months_between",Double.class, dtMes, dtAdm),12); 
     
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(QacOrganizerDetails_.empresa).alias("empresa"),
                                                              root.get(QacOrganizerDetails_.initState).alias("ano"),
                                                              root.get(QacOrganizerDetails_.level).alias("mes"),
                                                              root.get(QacOrganizerDetails_.vlr28).alias("cdSbu"),
                                                              root.get(QacOrganizerDetails_.vlr33).alias("dsAbrv"),
                                                              root.get(QacOrganizerDetails_.vlr29).alias("dsComp"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeAdmissao, 1), 1).otherwise(0).as(Integer.class)).alias("ate_1_ano"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeAdmissao, 1), 0).when(cb.le(anosDeAdmissao, 5), 1).otherwise(0).as(Integer.class)).alias("de_1_Ate_5_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeAdmissao, 5), 0).when(cb.le(anosDeAdmissao, 10), 1).otherwise(0).as(Integer.class)).alias("de_5_Ate_10_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeAdmissao, 10), 0).when(cb.le(anosDeAdmissao, 15), 1).otherwise(0).as(Integer.class)).alias("de_10_Ate_15_anos"),
                                                              cb.sum(cb.selectCase().when(cb.gt(anosDeAdmissao, 15), 1).otherwise(0).as(Integer.class)).alias("mais_de_15_anos"),
                                                              cb.count(root.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.nrLinha)).alias("total")); 
        
        return subDoTotalPorDirecao(range, sortField, sortOder, filters, cq, cb, root, compoundSelection); 
    }
 
    
    public List<Tuple> porAntiguidadeBancaPorDirecao(int[] range, String sortField, SortOrder sortOder, Map filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root root = cq.from(entityClass);
        
        Expression<Date> dtMes = cb.function("LAST_DAY",Date.class,cb.function("TO_DATE",Date.class,cb.concat(cb.concat(root.get(QacOrganizerDetails_.initState),"-"), cb.concat(root.get(QacOrganizerDetails_.level), "-28"))));
   
        Expression<Date> dtAntiguidade = cb.function("TO_DATE",Date.class,root.get(QacOrganizerDetails_.vlr13), cb.literal("DD-MM-YYYY"));
        
        Expression<Number> anosDeAntiguidade = cb.quot(cb.function("months_between",Double.class, dtMes, dtAntiguidade),12); 
 
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(QacOrganizerDetails_.empresa).alias("empresa"),
                                                              root.get(QacOrganizerDetails_.initState).alias("ano"),
                                                              root.get(QacOrganizerDetails_.level).alias("mes"),
                                                              root.get(QacOrganizerDetails_.vlr28).alias("cdSbu"),
                                                              root.get(QacOrganizerDetails_.vlr33).alias("dsAbrv"),
                                                              root.get(QacOrganizerDetails_.vlr29).alias("dsComp"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeAntiguidade, 1), 1).otherwise(0).as(Integer.class)).alias("ate_1_ano"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeAntiguidade, 1), 0).when(cb.le(anosDeAntiguidade, 5), 1).otherwise(0).as(Integer.class)).alias("de_1_Ate_5_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeAntiguidade, 5), 0).when(cb.le(anosDeAntiguidade, 10), 1).otherwise(0).as(Integer.class)).alias("de_5_Ate_10_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeAntiguidade, 10), 0).when(cb.le(anosDeAntiguidade, 15), 1).otherwise(0).as(Integer.class)).alias("de_10_Ate_15_anos"),
                                                              cb.sum(cb.selectCase().when(cb.gt(anosDeAntiguidade, 15), 1).otherwise(0).as(Integer.class)).alias("mais_de_15_anos"),
                                                              cb.count(root.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.nrLinha)).alias("total")); 
        
        return subDoTotalPorDirecao(range, sortField, sortOder, filters, cq, cb, root, compoundSelection); 
    }
    
    public List<Tuple> porEstruturaEtariaPorDirecao(int[] range, String sortField, SortOrder sortOder, Map filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root root = cq.from(entityClass);
        
        Expression<Date> dtMes = cb.function("LAST_DAY",Date.class,cb.function("TO_DATE",Date.class,cb.concat(cb.concat(root.get(QacOrganizerDetails_.initState),"-"), cb.concat(root.get(QacOrganizerDetails_.level), "-28"))));

        Expression<Number> anosDeNascimento = cb.quot(cb.function("months_between",Double.class, dtMes, root.join(QacOrganizerDetails_.cadastros).get(Cadastros_.dtNasc)),12); 
 
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(QacOrganizerDetails_.empresa).alias("empresa"),
                                                              root.get(QacOrganizerDetails_.initState).alias("ano"),
                                                              root.get(QacOrganizerDetails_.level).alias("mes"),
                                                              root.get(QacOrganizerDetails_.vlr28).alias("cdSbu"),
                                                              root.get(QacOrganizerDetails_.vlr33).alias("dsAbrv"),
                                                              root.get(QacOrganizerDetails_.vlr29).alias("dsComp"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeNascimento, 24), 1).otherwise(0).as(Integer.class)).alias("ate_24_ano"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeNascimento, 24), 0).when(cb.le(anosDeNascimento, 34), 1).otherwise(0).as(Integer.class)).alias("de_25_Ate_34_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeNascimento, 34), 0).when(cb.le(anosDeNascimento, 44), 1).otherwise(0).as(Integer.class)).alias("de_35_Ate_44_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeNascimento, 44), 0).when(cb.le(anosDeNascimento, 54), 1).otherwise(0).as(Integer.class)).alias("de_45_Ate_54_anos"),
                                                              cb.sum(cb.selectCase().when(cb.le(anosDeNascimento, 54), 0).when(cb.le(anosDeNascimento, 65), 1).otherwise(0).as(Integer.class)).alias("de_55_Ate_65_anos"),
                                                              cb.sum(cb.selectCase().when(cb.gt(anosDeNascimento, 65), 1).otherwise(0).as(Integer.class)).alias("mais_de_65_anos"),
                                                              cb.count(root.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.nrLinha)).alias("total")); 

        return subDoTotalPorDirecao(range, sortField, sortOder, filters, cq, cb, root, compoundSelection); 
    }   

    public List<Tuple> porHabilitacaoPorDirecao(int[] range, String sortField, SortOrder sortOder, Map filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root root = cq.from(entityClass);
        
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(QacOrganizerDetails_.empresa).alias("empresa"),
                                                              root.get(QacOrganizerDetails_.initState).alias("ano"),
                                                              root.get(QacOrganizerDetails_.level).alias("mes"),
                                                              root.get(QacOrganizerDetails_.vlr28).alias("cdSbu"),
                                                              root.get(QacOrganizerDetails_.vlr33).alias("dsAbrv"),
                                                              root.get(QacOrganizerDetails_.vlr29).alias("dsComp"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr4)).when("1ºCl Ens B", 1).otherwise(0)).alias("1Cl_Ens_B"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr4)).when("2ºCl Ens B", 1).otherwise(0)).alias("2Cl_Ens_B"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr4)).when("3ºCl Ens B", 1).otherwise(0)).alias("3Cl_Ens_B"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr4)).when("Ens Sec", 1).otherwise(0)).alias("Ens_Sec"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr4)).when("Bacharel", 1).otherwise(0)).alias("Bacharel"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr4)).when("Licenciad", 1).otherwise(0)).alias("Licenciad"),
                                                              cb.count(root.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.nrLinha)).alias("total")); 
        
        return subDoTotalPorDirecao(range, sortField, sortOder, filters, cq, cb, root, compoundSelection); 
        
    }
    
    
        public List<Tuple> porSitGeoPorDirecao(int[] range, String sortField, SortOrder sortOder, Map filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root root = cq.from(entityClass);
        
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(QacOrganizerDetails_.empresa).alias("empresa"),
                                                              root.get(QacOrganizerDetails_.initState).alias("ano"),
                                                              root.get(QacOrganizerDetails_.level).alias("mes"),
                                                              root.get(QacOrganizerDetails_.vlr28).alias("cdSbu"),
                                                              root.get(QacOrganizerDetails_.vlr33).alias("dsAbrv"),
                                                              root.get(QacOrganizerDetails_.vlr29).alias("dsComp"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr36)).when("C", 1).otherwise(0)).alias("C"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr36)).when("M", 1).otherwise(0)).alias("M"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr36)).when("A", 1).otherwise(0)).alias("A"),
                                                              cb.sum(cb.selectCase(root.get(QacOrganizerDetails_.vlr36)).when("C", 0).when("M", 0).when("A", 0).otherwise(1)).alias("O"),                                                              
                                                              cb.count(root.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.nrLinha)).alias("total")); 
        
        return subDoTotalPorDirecao(range, sortField, sortOder, filters, cq, cb, root, compoundSelection); 
    }


   
        
    public List<Tuple> subDoTotalPorDirecao(int[] range, String sortField, SortOrder sortOder, Map filters, CriteriaQuery<Tuple> cq, CriteriaBuilder cb, Root root, CompoundSelection<Tuple> compoundSelection) {

        
        int incialpredicate = 2;
        Predicate[] predicateFixo = new Predicate[incialpredicate]  ;       
        predicateFixo[0] = cb.like(root.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.cdOut),"9999");
        predicateFixo[1] = cb.like(root.get(QacOrganizerDetails_.estruturaAtomica),"1");
         
         
        Predicate condicaoFixa = cb.and(predicateFixo);
         
        Predicate condicaoDinamica = addChoicePredicate(new Predicate[filters.size()], filters, cb, root);
        
        Predicate condicao = null;
        if (!filters.isEmpty() 
           || (filters.containsKey("globalFilter") && "".equals(filters.get("globalFilter").toString()) && filters.containsValue("%")) ){
             condicao = makePredicate(predicateFixo, filters, cb, root);
        }             

        

        Order[] ordenar = {cb.desc(root.get(QacOrganizerDetails_.initState)),
                           cb.desc(root.get(QacOrganizerDetails_.level)),
                           cb.asc(root.get(QacOrganizerDetails_.empresa)),
                           cb.asc(root.get(QacOrganizerDetails_.vlr29))};

        makeOrder(ordenar, sortField, sortOder, cb, root);
        
        Expression[] compoundGroup = {root.get(QacOrganizerDetails_.empresa),
                                      root.get(QacOrganizerDetails_.initState),
                                      root.get(QacOrganizerDetails_.level),
                                      root.get(QacOrganizerDetails_.vlr28),
                                      root.get(QacOrganizerDetails_.vlr33),
                                      root.get(QacOrganizerDetails_.vlr29)};

//        return findCriteria(range, compoundSelection, condicaoFixa, condicaoDinamica, ordenar, compoundGroup);
  
       if (condicao!=null){
            return findCriteria(range, compoundSelection, condicao, ordenar, compoundGroup);    
       }else{   
            return findCriteria(range, compoundSelection, predicateFixo, ordenar, compoundGroup);
       }

        
        
    }
    
   
     
      public List<Tuple> aoServicoPeriodos(boolean in, String... Empresas){
        
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root root = cq.from(entityClass);
        
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(QacOrganizerDetails_.empresa).alias("empresa"),
                                                              cb.concat(cb.concat(root.get(QacOrganizerDetails_.initState),"-"), root.get(QacOrganizerDetails_.level)).alias("legenda") ,
                                                              cb.count(root.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.nrLinha)).alias("valor")); 
        
       Expression[] compoundGroup = {root.get(QacOrganizerDetails_.empresa),
                                     root.get(QacOrganizerDetails_.initState),
                                     root.get(QacOrganizerDetails_.level)};

       Order[] ordenar = {cb.asc(root.get(QacOrganizerDetails_.empresa)),
                           cb.asc(root.get(QacOrganizerDetails_.initState)),
                           cb.asc(root.get(QacOrganizerDetails_.level))};   
             
        return subDoTotalPeriodos(cq, cb, root, compoundSelection, compoundGroup, ordenar, in, Empresas);        
    }
      
      public List<Tuple> porGeneroPeriodos(boolean in, String... Empresas){
        
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root root = cq.from(entityClass);

        CompoundSelection<Tuple> compoundSelection = cb.tuple(cb.concat(cb.concat(root.get(QacOrganizerDetails_.empresa),"-"),root.join(QacOrganizerDetails_.cadastros).get(Cadastros_.sexo)).alias("empresa"),
                                                              cb.concat(cb.concat(root.get(QacOrganizerDetails_.initState),"-"), root.get(QacOrganizerDetails_.level)).alias("legenda") ,
                                                              cb.count(root.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.nrLinha)).alias("valor")); 

        Expression[] compoundGroup = {root.get(QacOrganizerDetails_.empresa),
                                      root.get(QacOrganizerDetails_.initState),
                                      root.get(QacOrganizerDetails_.level),
                                      root.join(QacOrganizerDetails_.cadastros).get(Cadastros_.sexo)};

                Order[] ordenar = {cb.asc(root.get(QacOrganizerDetails_.empresa)),
                                   cb.desc(root.join(QacOrganizerDetails_.cadastros).get(Cadastros_.sexo)), 
                                   cb.asc(root.get(QacOrganizerDetails_.initState)),
                                   cb.asc(root.get(QacOrganizerDetails_.level))};       

                
        return subDoTotalPeriodos(cq, cb, root, compoundSelection,compoundGroup, ordenar, in, Empresas);        
    }
      
          public List<Tuple> porMediaEtariaPeriodos(boolean in, String... Empresas){
        
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root root = cq.from(entityClass);
  
              Expression<Date> dtMes = cb.function("LAST_DAY",Date.class,cb.function("TO_DATE",Date.class,cb.concat(cb.concat(root.get(QacOrganizerDetails_.initState),"-"), cb.concat(root.get(QacOrganizerDetails_.level), "-28"))));
//              Expression<Double> avg = cb.avg(cb.function("floor",Integer.class,cb.quot(cb.function("months_between", Double.class, dtMes, root.join(QacOrganizerDetails_.cadastros).get(Cadastros_.dtNasc)),12)));
  
        
        
        CompoundSelection<Tuple> compoundSelection = cb.tuple(root.get(QacOrganizerDetails_.empresa).alias("empresa"),
                                                              cb.concat(cb.concat(root.get(QacOrganizerDetails_.initState),"-"), root.get(QacOrganizerDetails_.level)).alias("legenda") ,
                                                              cb.avg(cb.function("floor",Integer.class,cb.quot(cb.function("months_between", Double.class, dtMes, root.join(QacOrganizerDetails_.cadastros).get(Cadastros_.dtNasc)),12))).alias("valor"));
                                                              //cb.function("round",Double.class,avg,cb.literal(2)).alias("valor")); 
        
       Expression[] compoundGroup = {root.get(QacOrganizerDetails_.empresa),
                                     root.get(QacOrganizerDetails_.initState),
                                     root.get(QacOrganizerDetails_.level)};

       Order[] ordenar = {cb.asc(root.get(QacOrganizerDetails_.empresa)),
                           cb.asc(root.get(QacOrganizerDetails_.initState)),
                           cb.asc(root.get(QacOrganizerDetails_.level))};   
             
        return subDoTotalPeriodos(cq, cb, root, compoundSelection, compoundGroup, ordenar, in, Empresas);        
    }
  
      
      
   public List<Tuple> subDoTotalPeriodos(CriteriaQuery<Tuple> cq, CriteriaBuilder cb, Root root, CompoundSelection<Tuple> compoundSelection, Expression[] compoundGroup,Order[] ordenar, boolean in, String... Empresas){
       
        Predicate[] predicate = new Predicate[5]  ;       
        predicate[0] = cb.like(root.get(QacOrganizerDetails_.qacOrganizerDetailsPK).get(QacOrganizerDetailsPK_.cdOut),"9999");
        predicate[1] = cb.like(root.get(QacOrganizerDetails_.estruturaAtomica),"1");
        predicate[2] = root.get(QacOrganizerDetails_.level).in(3,6,9,12);
        predicate[3] = cb.ge(root.get(QacOrganizerDetails_.initState), 2010);
        if (in==false) {
            predicate[4] = cb.not(root.get(QacOrganizerDetails_.empresa).in((Object[]) Empresas));
          }
        else{
            predicate[4] = root.get(QacOrganizerDetails_.empresa).in((Object[]) Empresas);
            
        }       
       
        return findCriteria(null, compoundSelection, predicate, ordenar, compoundGroup);
        
   
   }   
   
   
   public List<Object[]> movimentacoesPorEmpresas(String Empresa, Integer Ano){
       
       String sql ="SELECT ENTRADAS.EMPRESA, " +
                    "	   ENTRADAS.ANO, " +
                    "	   ENTRADAS.MES, " +
                    "	   ENTRADAS.QUADRO, " +
                    "	   ENTRADAS.ENTRADAS, " +
                    "	   ENTRADAS.MOV_ENTR, " +
                    "	   SAIDAS.MOV_SAI, " +
                    "	   SAIDAS.SAIDAS, " +
                    "      ENTRADAS.ENTRADAS + ENTRADAS.MOV_ENTR - SAIDAS.MOV_SAI - SAIDAS.SAIDAS VARIACAO " +
                    "FROM " +
                    "		(SELECT QOD1.EMPRESA, " +
                    "			   QOD1.INIT_STATE_ ANO, " + 	 
                    "			   QOD1.LEVEL_ MES, " +
                    "		 	   COUNT(QOD1.NR_LINHA) QUADRO, " + 
                    "			   SUM(CASE WHEN QOD2.NR_LINHA IS NOT NULL THEN 0 ELSE 1 END) ENTRADAS, " + 
                    "			   SUM(CASE WHEN QOD1.VLR_28 <> QOD2.VLR_28 AND QOD2.VLR_28 IS NOT NULL THEN 1 ELSE 0 END) MOV_ENTR " +
                    "		FROM QAC_ORGANIZER_DETAILS QOD1 LEFT JOIN QAC_ORGANIZER_DETAILS QOD2 " + 
                    "			ON (QOD1.CD_OUT = QOD2.CD_OUT) " +
                    "			AND (QOD1.NR_LINHA = QOD2.NR_LINHA) " +
                    "			AND (QOD1.INIT_STATE_ = CASE WHEN  QOD2.LEVEL_=12 THEN QOD2.INIT_STATE_+1 ELSE QOD2.INIT_STATE_ END) " +
                    "			AND (QOD1.LEVEL_ = CASE WHEN QOD2.LEVEL_ =12 THEN 1 ELSE QOD2.LEVEL_+1 END) " +
                    "		WHERE QOD1.CD_OUT = '9999' AND ESTRUTURA_ATOMICA = '1' " +
                    "		GROUP BY QOD1.EMPRESA,QOD1.INIT_STATE_, QOD1.LEVEL_) ENTRADAS " +
                    "INNER JOIN " +
                    "		(SELECT QOD1.EMPRESA, " + 
                    "			   CASE WHEN QOD1.LEVEL_=12 THEN QOD1.INIT_STATE_+1 ELSE QOD1.INIT_STATE_ END ANO, " +	
                    "			   CASE WHEN QOD1.LEVEL_ =12 THEN 1 ELSE QOD1.LEVEL_+1 END MES, " +
                    "			   SUM(CASE WHEN QOD1.VLR_28 <> QOD3.VLR_28 AND QOD3.VLR_28 IS NOT NULL THEN 1 ELSE 0 END) MOV_SAI, " +
                    "			   SUM(CASE WHEN QOD3.NR_LINHA IS NOT NULL THEN 0 ELSE 1 END) SAIDAS  " +
                    "		FROM QAC_ORGANIZER_DETAILS QOD3 RIGHT JOIN QAC_ORGANIZER_DETAILS QOD1  " +
                    "			ON (QOD3.CD_OUT = QOD1.CD_OUT) " +
                    "			AND (QOD3.NR_LINHA = QOD1.NR_LINHA) " +
                    "			AND (QOD3.INIT_STATE_ = CASE WHEN QOD1.LEVEL_=12 THEN QOD1.INIT_STATE_+1 ELSE QOD1.INIT_STATE_ END) " +
                    "			AND (QOD3.LEVEL_ = CASE WHEN QOD1.LEVEL_ =12 THEN 1 ELSE QOD1.LEVEL_+1 END) " +
                    "		WHERE QOD1.CD_OUT = '9999' AND ESTRUTURA_ATOMICA = '1' " +
                    "		GROUP BY QOD1.EMPRESA,  " +
                    "				 CASE WHEN QOD1.LEVEL_=12 THEN QOD1.INIT_STATE_+1 ELSE QOD1.INIT_STATE_ END, " +	
                    "				 CASE WHEN  QOD1.LEVEL_ =12 THEN 1 ELSE QOD1.LEVEL_+1 END) SAIDAS " +
                    "ON (ENTRADAS.EMPRESA = SAIDAS.EMPRESA) " +
                    " AND (ENTRADAS.ANO = SAIDAS.ANO) " +
                    " AND (ENTRADAS.MES = SAIDAS.MES) " +
                    "WHERE ENTRADAS.EMPRESA = ?1 " +
                    " AND ENTRADAS.ANO = ?2 " +
                    "ORDER BY ENTRADAS.EMPRESA,  " +
                    "	   	 ENTRADAS.ANO DESC, " +
                    "	     ENTRADAS.MES DESC ";
       
        Query createNativeQuery = getEntityManager().createNativeQuery(sql);
        
        createNativeQuery.setParameter(1, Empresa);
        createNativeQuery.setParameter(2, Ano);
        List<Object[]> lista = createNativeQuery.getResultList();
        return lista;
        
   }
       
   public List<Object[]> movimentacoesPorDirecao(String empresa, Integer ano, Integer mes){
       
       String sql ="SELECT MOVIMENT.EMPRESA, " +
                    "	   MOVIMENT.ANO, " +
                    "	   MAX(MOVIMENT.MES) , " +
                    "	   MOVIMENT.VLR_28, " +
                    "	   MOVIMENT.VLR_29, " +
                    "	   SUM(MOVIMENT.QUADRO) QUADROS, " +
                    "	   SUM(MOVIMENT.ENTRADAS) ENTRADAS, " +
                    "	   SUM(MOVIMENT.MOV_ENTR) MOV_ENTR, " +
                    "	   SUM(MOVIMENT.MOV_SAI) MOV_SAI, " +
                    "	   SUM(MOVIMENT.SAIDAS) SAIDAS, " +
                    "      SUM(MOVIMENT.ENTRADAS + MOVIMENT.MOV_ENTR - MOVIMENT.MOV_SAI - MOVIMENT.SAIDAS) variacao " +
                    "FROM (SELECT DISTINCT INIT_STATE_, LEVEL_ FROM QAC_ORGANIZER_DETAILS WHERE CD_OUT = '9999' AND ESTRUTURA_ATOMICA = '1') MESES " +
                    "LEFT JOIN " +
                    "(	   	 " +
                    "SELECT QOD1.EMPRESA, " +
                    "			   QOD1.INIT_STATE_ ANO, " + 	 
                    "			   QOD1.LEVEL_ MES, " +
                    "			   QOD1.VLR_28, " +
                    "			   QOD1.VLR_29, " +
                    "		 	   COUNT(QOD1.NR_LINHA) QUADRO,  " +
                    "			   SUM(CASE WHEN QOD2.NR_LINHA IS NOT NULL THEN 0 ELSE 1 END) ENTRADAS, " + 
                    "			   SUM(CASE WHEN QOD1.VLR_28 <> QOD2.VLR_28 AND QOD2.VLR_28 IS NOT NULL THEN 1 ELSE 0 END) MOV_ENTR, " +
                    "			   0 MOV_SAI, " +
                    "			   0 SAIDAS " +
                    "		FROM QAC_ORGANIZER_DETAILS QOD1 LEFT JOIN QAC_ORGANIZER_DETAILS QOD2 " + 
                    "			ON (QOD1.CD_OUT = QOD2.CD_OUT) " +
                    "			AND (QOD1.NR_LINHA = QOD2.NR_LINHA) " +
                    "			AND (QOD1.INIT_STATE_ = CASE WHEN  QOD2.LEVEL_=12 THEN QOD2.INIT_STATE_+1 ELSE QOD2.INIT_STATE_ END) " +
                    "			AND (QOD1.LEVEL_ = CASE WHEN QOD2.LEVEL_ =12 THEN 1 ELSE QOD2.LEVEL_+1 END) " +
                    "		WHERE QOD1.CD_OUT = '9999' AND ESTRUTURA_ATOMICA = '1' " +
                    "		GROUP BY QOD1.EMPRESA,QOD1.INIT_STATE_, QOD1.LEVEL_, QOD1.VLR_28, QOD1.VLR_29 " +
                    "UNION " +
                    "SELECT QOD1.EMPRESA,  " +
                    "			   CASE WHEN QOD1.LEVEL_=12 THEN QOD1.INIT_STATE_+1 ELSE QOD1.INIT_STATE_ END ANO, " +	
                    "			   CASE WHEN QOD1.LEVEL_ =12 THEN 1 ELSE QOD1.LEVEL_+1 END MES, " +
                    "			   QOD1.VLR_28, " +
                    "			   QOD1.VLR_29, " +
                    "			   0 QUADRO, " +
                    "			   0 ENTRADAS, " +
                    "			   0 MOV_ENTR, " + 			   
                    "			   SUM(CASE WHEN QOD1.VLR_28 <> QOD3.VLR_28 AND QOD3.VLR_28 IS NOT NULL THEN 1 ELSE 0 END) MOV_SAI, " +
                    "			   SUM(CASE WHEN QOD3.NR_LINHA IS NOT NULL THEN 0 ELSE 1 END) SAIDAS  " +
                    "		FROM QAC_ORGANIZER_DETAILS QOD3 RIGHT JOIN QAC_ORGANIZER_DETAILS QOD1  " +
                    "			ON (QOD3.CD_OUT = QOD1.CD_OUT) " +
                    "			AND (QOD3.NR_LINHA = QOD1.NR_LINHA) " +
                    "			AND (QOD3.INIT_STATE_ = CASE WHEN QOD1.LEVEL_=12 THEN QOD1.INIT_STATE_+1 ELSE QOD1.INIT_STATE_ END) " +
                    "			AND (QOD3.LEVEL_ = CASE WHEN QOD1.LEVEL_ =12 THEN 1 ELSE QOD1.LEVEL_+1 END) " +
                    "		WHERE QOD1.CD_OUT = '9999' AND ESTRUTURA_ATOMICA = '1' " +
                    "		GROUP BY QOD1.EMPRESA,  " +
                    "				 CASE WHEN QOD1.LEVEL_=12 THEN QOD1.INIT_STATE_+1 ELSE QOD1.INIT_STATE_ END, " +	
                    "				 CASE WHEN QOD1.LEVEL_ =12 THEN 1 ELSE QOD1.LEVEL_+1 END, " +
                    "				 QOD1.VLR_28, " +
                    "			   	 QOD1.VLR_29 " +
                    ") MOVIMENT " +
                    "ON (MESES.INIT_STATE_ = MOVIMENT.ANO) " +
                    "   AND (MESES.LEVEL_ = MOVIMENT.MES) " +
                    "WHERE MOVIMENT.EMPRESA = ?1 " +
                    " AND MOVIMENT.ANO =?2 " +
                    " AND MOVIMENT.MES <= ?3 " +                    
                    "GROUP BY EMPRESA, " +
                    " 		 MOVIMENT.ANO, " +
                    "  	   	 MOVIMENT.VLR_28, " +
                    "  	 	 MOVIMENT.VLR_29  " +
                    "ORDER BY EMPRESA,  " +
                    "	      MOVIMENT.ANO DESC,  " +
                    "	      CAST(MOVIMENT.VLR_28 AS NUMBER) " ;
       
        Query createNativeQuery = getEntityManager().createNativeQuery(sql);
        
        createNativeQuery.setParameter(1, empresa);
        createNativeQuery.setParameter(2, ano);
        createNativeQuery.setParameter(3, mes);
        List<Object[]> lista = createNativeQuery.getResultList();
        return lista;
        
   }

   


   
}
