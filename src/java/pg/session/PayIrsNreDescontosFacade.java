/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.session;

import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CompoundSelection;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import org.primefaces.model.SortOrder;
import pg.entities.Cadastros;
import pg.entities.Cadastros_;
import pg.entities.GruposEd;
import pg.entities.GruposEd_;
import pg.entities.ParEd_;
import pg.entities.PayIrsNre;
import pg.entities.PayIrsNreDescontos;
import pg.entities.PayIrsNreDescontos_;
import pg.entities.PayIrsNreTrend;
import pg.entities.PayIrsNreTrend_;
import pg.entities.PayIrsNre_;

/**
 *
 * @author bnf02107
 */
@Stateless
public class PayIrsNreDescontosFacade extends AbstractFacade<PayIrsNreDescontos> {
    @PersistenceContext(unitName = "RHSPUPG")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PayIrsNreDescontosFacade() {
        super(PayIrsNreDescontos.class);
    }
    
 
       public List<Object[]> dmrValidacaoPorAnoMes(Integer ano, Integer mes){
       
       String sql = "SELECT PAY_IRS_NRE.NRE, " +
                    "     PAY_IRS_NRE.NOME, " +
                    "     PAY_IRS_NRE.NIF, " +
                    "     PAY_IRS_NRE_TREND.TREND, " +
                    "     PAY_IRS_NRE_TREND.VALOR_RENDIMENTO, " +
                    "     PAY_IRS_NRE_TREND.VALOR_RETIDO, " +
                    "     DECODE(PAR_ED.CD_GRUPO_ED, 'FP','SEG', 'SS','SEG' ,'SA','SEG',PAR_ED.CD_GRUPO_ED) GRP, " +
                    "     SUM(PAY_IRS_NRE_DESCONTOS.VALOR) AS VAL " +
                    "FROM  " +
                    "     PAY_IRS_NRE_TREND LEFT OUTER JOIN PAY_IRS_NRE_DESCONTOS ON PAY_IRS_NRE_TREND.ID = PAY_IRS_NRE_DESCONTOS.PAY_IRS_NRE_TREND_ID " +
                    "     INNER JOIN PAY_IRS_NRE ON PAY_IRS_NRE_TREND.PAY_IRS_NRE_ID = PAY_IRS_NRE.ID " +
                    "     LEFT OUTER JOIN PAR_ED ON PAY_IRS_NRE_DESCONTOS.CD_ED = PAR_ED.CD_ED " +
                    "WHERE PAY_IRS_NRE.ANO = ?1 " +
                    "AND PAY_IRS_NRE.MES = ?2 " +         
                    "GROUP BY  " +
                    "     PAY_IRS_NRE.NRE, " +
                    "     PAY_IRS_NRE.NOME, " +
                    "     PAY_IRS_NRE.NIF, " +
                    "     PAY_IRS_NRE_TREND.TREND, " +
                    "     PAY_IRS_NRE_TREND.VALOR_RENDIMENTO, " +
                    "     PAY_IRS_NRE_TREND.VALOR_RETIDO, " +
                    "     DECODE(PAR_ED.CD_GRUPO_ED, 'FP','SEG', 'SS','SEG' ,'SA','SEG',PAR_ED.CD_GRUPO_ED) " +
                    "HAVING (SUM(PAY_IRS_NRE_DESCONTOS.VALOR) < 0 " +
                    "  		OR SUM(PAY_IRS_NRE_DESCONTOS.BASE_INCIDENCIA) < 0 " +
                    "  		OR PAY_IRS_NRE_TREND.VALOR_RENDIMENTO <= 0 " +
                    "  		OR PAY_IRS_NRE_TREND.VALOR_RETIDO < 0 " +
                    "  		OR SUM(PAY_IRS_NRE_DESCONTOS.VALOR) > (PAY_IRS_NRE_TREND.VALOR_RENDIMENTO*0.20)   " +
                    "  		) " +
                    "  AND SUM(PAY_IRS_NRE_DESCONTOS.VALOR)  " +
                    "  	+ SUM(PAY_IRS_NRE_DESCONTOS.BASE_INCIDENCIA) " + 
                    "  	+ PAY_IRS_NRE_TREND.VALOR_RENDIMENTO " +
                    "  	+ PAY_IRS_NRE_TREND.VALOR_RETIDO <> 0 " +
                    "ORDER BY PAY_IRS_NRE.NRE ";
       
        Query createNativeQuery = getEntityManager().createNativeQuery(sql);
        
        createNativeQuery.setParameter(1, ano);
        createNativeQuery.setParameter(2, mes);
        List<Object[]> lista = createNativeQuery.getResultList();
        return lista;
        
   }
       
       
   public List<Object[]> dmrErrosPorNreAnoMes(Integer ano, Integer mes){
       
       
       
     String sql = "SELECT PAY_IRS_NRE.NRE, " +
                  "     PAY_IRS_NRE.NOME, " + 
                  "     PAY_IRS_NRE.NIF,  " +
                  "     PAY_IRS_NRE_TREND.TREND, " +
                  "      PAY_IRS_NRE_TREND.VALOR_RENDIMENTO, " +
                  "      PAY_IRS_NRE_TREND.VALOR_RETIDO, " +
                  "     DECODE(PAR_ED.CD_GRUPO_ED, 'FP','SEG', 'SS','SEG' ,'SA','SEG',PAR_ED.CD_GRUPO_ED) GRP, " +
                  "     SUM(PAY_IRS_NRE_DESCONTOS.VALOR) AS VAL " +
                  "  FROM  " +
                  "     PAY_IRS_NRE_TREND " +
                  "     LEFT OUTER JOIN PAY_IRS_NRE_DESCONTOS ON PAY_IRS_NRE_TREND.ID = PAY_IRS_NRE_DESCONTOS.PAY_IRS_NRE_TREND_ID " +
                  "     INNER JOIN PAY_IRS_NRE ON PAY_IRS_NRE_TREND.PAY_IRS_NRE_ID = PAY_IRS_NRE.ID " +
                  "      LEFT OUTER JOIN PAR_ED ON PAY_IRS_NRE_DESCONTOS.CD_ED = PAR_ED.CD_ED " +
                  "  WHERE PAY_IRS_NRE.ANO = ?1 " +
                  "    AND PAY_IRS_NRE.MES = ?2 " +
                  "    AND PAY_IRS_NRE.NRE IN(  " +
                  "                         SELECT DISTINCT PAY_IRS_NRE.NRE " +
                  "                         FROM  " +
                  "                         PAY_IRS_NRE_TREND LEFT OUTER JOIN PAY_IRS_NRE_DESCONTOS ON PAY_IRS_NRE_TREND.ID = PAY_IRS_NRE_DESCONTOS.PAY_IRS_NRE_TREND_ID " +
                  "                         INNER JOIN PAY_IRS_NRE ON PAY_IRS_NRE_TREND.PAY_IRS_NRE_ID = PAY_IRS_NRE.ID " +
                  "                         LEFT OUTER JOIN PAR_ED ON PAY_IRS_NRE_DESCONTOS.CD_ED = PAR_ED.CD_ED " +
                  "                         WHERE PAY_IRS_NRE.ANO = ?1 " +
                  "                          AND PAY_IRS_NRE.MES = ?2 " +         
                  "                         GROUP BY  " +
                  "                                  PAY_IRS_NRE.NRE, " +
                  "                                  PAY_IRS_NRE.NOME, " +
                  "                                  PAY_IRS_NRE.NIF, " +
                  "                                  PAY_IRS_NRE_TREND.TREND, " +
                  "                                  PAY_IRS_NRE_TREND.VALOR_RENDIMENTO, " +
                  "                                  PAY_IRS_NRE_TREND.VALOR_RETIDO, " +
                  "                                  DECODE(PAR_ED.CD_GRUPO_ED, 'FP','SEG', 'SS','SEG' ,'SA','SEG',PAR_ED.CD_GRUPO_ED) " +
                  "                         HAVING (SUM(PAY_IRS_NRE_DESCONTOS.VALOR) < 0 " +
                  "                             OR SUM(PAY_IRS_NRE_DESCONTOS.BASE_INCIDENCIA) < 0 " +
                  "                             OR PAY_IRS_NRE_TREND.VALOR_RENDIMENTO <= 0 " +
                  "                             OR PAY_IRS_NRE_TREND.VALOR_RETIDO < 0 " +
                  "                             OR SUM(PAY_IRS_NRE_DESCONTOS.VALOR) > (PAY_IRS_NRE_TREND.VALOR_RENDIMENTO*0.20)   " +
                  "                            ) " +
                  "                          AND SUM(PAY_IRS_NRE_DESCONTOS.VALOR)  " +
                  "                          + SUM(PAY_IRS_NRE_DESCONTOS.BASE_INCIDENCIA) " + 
                  "                          + PAY_IRS_NRE_TREND.VALOR_RENDIMENTO " +
                  "                          + PAY_IRS_NRE_TREND.VALOR_RETIDO <> 0 " +
                  ")" +
                  " GROUP BY " +
                  "     PAY_IRS_NRE.NRE, " +
                  "     PAY_IRS_NRE.NOME, " +
                  "     PAY_IRS_NRE.NIF, " +
                  "     PAY_IRS_NRE_TREND.TREND, " +
                  "     PAY_IRS_NRE_TREND.VALOR_RENDIMENTO, " +
                  "     PAY_IRS_NRE_TREND.VALOR_RETIDO, " +
                  "     DECODE(PAR_ED.CD_GRUPO_ED, 'FP','SEG', 'SS','SEG' ,'SA','SEG',PAR_ED.CD_GRUPO_ED) " +
                  " ORDER BY PAY_IRS_NRE.NRE, " +
                  "          PAY_IRS_NRE_TREND.TREND, " +
                  "          DECODE(PAR_ED.CD_GRUPO_ED, 'FP','SEG', 'SS','SEG' ,'SA','SEG',PAR_ED.CD_GRUPO_ED) "             
             ;
       
        Query createNativeQuery = getEntityManager().createNativeQuery(sql);
        
        createNativeQuery.setParameter(1, ano);
        createNativeQuery.setParameter(2, mes);
        List<Object[]> lista = createNativeQuery.getResultList();
        return lista;
        
   }
    
       
       

       
   private List<Tuple> dmrValidacao(int[] range, String sortField, SortOrder sortOder, Map filters) {
       
       ///--------Não usar porque o eclipselink não suporta Group by com select case 
       
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        javax.persistence.criteria.Root root = cq.from(entityClass);
   
        Predicate condicao = null;
        Predicate[] predicate = new Predicate[filters.size()];
        if (!filters.isEmpty() 
           || (filters.containsKey("globalFilter") && "".equals(filters.get("globalFilter").toString()) && filters.containsValue("%")) ){
             condicao = addChoicePredicate(predicate, filters, cb, root);
        }             
        
        Path<PayIrsNre> pin = root.join(PayIrsNreDescontos_.payIrsNreTrendId).join(PayIrsNreTrend_.payIrsNreId);
        Path<Cadastros> c = root.join(PayIrsNreDescontos_.payIrsNreTrendId).join(PayIrsNreTrend_.payIrsNreId).join(PayIrsNre_.cadastros);
        Path<PayIrsNreTrend> pint = root.join(PayIrsNreDescontos_.payIrsNreTrendId);
        Path<GruposEd> gred = root.join(PayIrsNreDescontos_.parEd).join(ParEd_.cdGrupoEd);
        //Expression grped = cb.function("DECODE", String.class, gred.get(GruposEd_.cdGrupo), cb.literal("FP"),cb.literal("SEG"),cb.literal("SS"),cb.literal("SEG") ,cb.literal("SA"),cb.literal("SEG"),gred.get(GruposEd_.cdGrupo));
        Expression grped = cb.selectCase(gred.get(GruposEd_.cdGrupo)).when("FP", "SEG").when("SS", "SEG").when("SA", "SEG").otherwise(gred.get(GruposEd_.cdGrupo));
        
        CompoundSelection<Tuple> compoundSelection = cb.tuple(pin.get(PayIrsNre_.ano).alias("ano"),
                                                              pin.get(PayIrsNre_.mes).alias("mes"),  
                                                              c.get(Cadastros_.nre).alias("nre"),
                                                              pin.get(PayIrsNre_.nome).alias("nome"),
                                                              pin.get(PayIrsNre_.nif).alias("nif"),
                                                              pint.get(PayIrsNreTrend_.valorRendimento).alias("rendimento"),
                                                              pint.get(PayIrsNreTrend_.valorRetido).alias("valorRetido"), 
                                                              grped.alias("grped"),
                                                              //cb.selectCase(gred.get(GruposEd_.cdGrupo)).when("FP", "SEG").when("SS", "SEG").when("SA", "SEG").otherwise(gred.get(GruposEd_.cdGrupo)).alias("grped"),
                                                              cb.sum(root.get(PayIrsNreDescontos_.valor)).alias("desconto"));
         
        Expression[] compoundGroup =  {pin.get(PayIrsNre_.ano),
                                                              pin.get(PayIrsNre_.mes),  
                                                              c.get(Cadastros_.nre),
                                                              pin.get(PayIrsNre_.nome),
                                                              pin.get(PayIrsNre_.nif),
                                                              pint.get(PayIrsNreTrend_.valorRendimento),
                                                              pint.get(PayIrsNreTrend_.valorRetido),
                                                              grped
        };

         
         Predicate condicaoHaving =cb.and(cb.or(cb.lessThan(cb.sum(root.get(PayIrsNreDescontos_.valor)), 0),
                                   cb.or(cb.lessThan(root.get(PayIrsNreDescontos_.payIrsNreTrendId).get(PayIrsNreTrend_.valorRendimento), 0),
                                  cb.or(cb.lessThan(root.get(PayIrsNreDescontos_.payIrsNreTrendId).get(PayIrsNreTrend_.valorRetido),0),
                                         cb.greaterThan(cb.sum(root.get(PayIrsNreDescontos_.valor)), cb.prod(root.get(PayIrsNreDescontos_.payIrsNreTrendId).get(PayIrsNreTrend_.valorRendimento),0.20))
                                    ))),
                            cb.notEqual(cb.sum(cb.sum(root.get(PayIrsNreDescontos_.valor)), 
                                        cb.sum(root.get(PayIrsNreDescontos_.payIrsNreTrendId).get(PayIrsNreTrend_.valorRendimento),
                                               root.get(PayIrsNreDescontos_.payIrsNreTrendId).get(PayIrsNreTrend_.valorRetido))),0)    
         ); 

//         Predicate condicaoHaving =cb.lessThan(cb.sum(root.get(PayIrsNreDescontos_.valor)), 0); 
         
         
        Order[] ordenar = {cb.asc(c.get(Cadastros_.nre))};
        
        makeOrder(ordenar, sortField, sortOder, cb, root);

            return findCriteria(range, compoundSelection, condicao, ordenar, compoundGroup, condicaoHaving);
        
    }
       

    
    
}
