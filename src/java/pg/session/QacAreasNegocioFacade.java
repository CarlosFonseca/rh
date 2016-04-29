/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pg.entities.QacAreasNegocio;

/**
 *
 * @author bnf02107
 */
@Stateless
public class QacAreasNegocioFacade extends AbstractFacade<QacAreasNegocio> {
    @PersistenceContext(unitName = "RHSPUPG")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public QacAreasNegocioFacade() {
        super(QacAreasNegocio.class);
    }
    
}
