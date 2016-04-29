/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.session;


import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import pg.entities.*;

/**
 *
 * @author bnf02107
 */
@Stateless
public class CadastrosFacade extends AbstractFacade<Cadastros> {
    @PersistenceContext(unitName = "RHSPUPG")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CadastrosFacade() {
        super(Cadastros.class);
    }
    
    public List<Cadastros> findSemHab(){
        
        
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        Root rt = cb.createQuery().from(entityClass);

            Predicate[] condicao = {cb.like(rt.get(Cadastros_.cdSituacao).get(Situacoes_.mapaMess), "S"),
                                    cb.isNull(rt.join(Cadastros_.cdHabLit,JoinType.LEFT ).get(HabsLiters_.cdHabLit))};
            
        return  findCriteria(null,condicao,null);
        
    }
 
    
    
}
 
