/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.faces;

import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author bnf02107
 */
@ManagedBean
@SessionScoped
public class ChefiasDemitidasController {
    
    @EJB
    private pg.session.InfoEmpresasFacade ejbFacade;

    /**
     * Creates a new instance of ChefiasDemitidasController
     */
    public ChefiasDemitidasController() {
    }
    
        
    public List<Object[]> getChefiasDemitidos(){
        
          return ejbFacade.chefiasDemitidos();
        
    }
}
