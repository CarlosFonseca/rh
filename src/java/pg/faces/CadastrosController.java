/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.faces;

import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import pg.entities.Cadastros;

/**
 *
 * @author bnf02107
 */
@ManagedBean
@SessionScoped
public class CadastrosController {

    @EJB
    private pg.session.CadastrosFacade ejbFacade;
    /**
     * Creates a new instance of CadastrosController
     */
    public CadastrosController() {
    }
        
        public List<Cadastros> getChefiasDemitidos(){
        
          return ejbFacade.findSemHab();
        
    }
    
}
