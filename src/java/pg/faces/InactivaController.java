/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.faces;

import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author bnf02107
 */
@ManagedBean
@SessionScoped
public class InactivaController {

    private LazyDataModel items = null;
    @EJB
    private pg.session.InfoEmpresasFacade infoEmpresasFacade;
    
    
    /**
     * Creates a new instance of InactivaController
     */
    public InactivaController() {
    }
    
            public LazyDataModel getSemEstruturaActiva() {
        if (items == null) {
            items = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad = infoEmpresasFacade.semEstruturaActiva(new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            items.setPageSize(10);
            items.setRowCount(infoEmpresasFacade.countSemEstruturaActiva());
        }
        return items;
    }
    
}
