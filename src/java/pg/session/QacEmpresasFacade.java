/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pg.entities.QacEmpresas;

/**
 *
 * @author bnf02107
 */
@Stateless
public class QacEmpresasFacade extends AbstractFacade<QacEmpresas> {
    @PersistenceContext(unitName = "RHSPUPG")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public QacEmpresasFacade() {
        super(QacEmpresas.class);
    }
    
}
