/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.faces;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Tuple;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import pg.entities.QacOrganizerDetails;
import pg.faces.util.HistQPtoPDF;
import pg.faces.util.HistQpTotal;
import pg.session.QacOrganizerDetailsFacade;

/**
 *
 * @author bnf02107
 */
@ManagedBean
@SessionScoped
public class HistQPEstruturaController {
    
    private Tuple current;
    private List<QacOrganizerDetails> listQacOrganizerDetails ;
    private LazyDataModel items = null;
    @EJB
    private pg.session.QacOrganizerDetailsFacade ejbFacade;
    HistQpTotal histQpt;

    /** Creates a new instance of QPEstruturaController */
    public HistQPEstruturaController() {
    }
    

    private QacOrganizerDetailsFacade getFacade() {
        return ejbFacade;
 
    }
    
       
    public LazyDataModel getItems() {
        if (items == null) {
            items = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad = getFacade().findEstrutura(new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            items.setPageSize(10);
            items.setRowCount(getFacade().countEstrutura());
        }
        return items;
    }
       
       
     public String doPDF(){

         
         
       current = (Tuple) getItems().getRowData();
       listQacOrganizerDetails = getFacade().findByEstrutura( current.get("siglaE").toString(),current.get("cdEstrutura").toString(), Integer.parseInt(current.get("ano").toString()), Integer.parseInt(current.get("mes").toString()));
       
       HistQPtoPDF histQp = new HistQPtoPDF();
       
       String direccao = "";
//       if (current.get("dsDireccao")!=null) direccao = current.get("dsDireccao").toString();
       String empresa = current.get("siglaE").toString();
       if (current.get("dsSocial")!= null) empresa = current.get("dsSocial").toString();
       
        try {
            histQp.exportaQP(FacesContext.getCurrentInstance() , "qp",  empresa, direccao, current.get("ano").toString(), current.get("mes").toString(), listQacOrganizerDetails );
        } catch (IOException ex) {
            Logger.getLogger(QPDireccaoController.class.getName()).log(Level.SEVERE, null, ex);
        }       
         return null;
     }        


         
    public Tuple getCurrent() {
        return current;
    }

    public void setCurrent(Tuple current) {
        this.current = current; 
       listQacOrganizerDetails = getFacade().findByEstrutura( current.get("siglaE").toString(), current.get("cdEstrutura").toString(), Integer.parseInt(current.get("ano").toString()), Integer.parseInt(current.get("mes").toString()));
        histQpt = new HistQpTotal(current.get("dsComp").toString(), listQacOrganizerDetails) ; 

    }
    public HistQpTotal getQpt() {
        return histQpt;
    }
     
     
}
