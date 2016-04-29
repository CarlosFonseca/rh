/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.faces;

import pg.faces.util.QPtoPDF;
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
import pg.entities.InfoEmpresas;
import pg.faces.util.QpTotal;
import pg.session.InfoEmpresasFacade;

/**
 *
 * @author bnf02107
 */
@ManagedBean
@SessionScoped
public class QPDireccaoEstabController {
    
    private Tuple current;
    private List<InfoEmpresas> listInfoEmpresas ;
    private LazyDataModel items = null;
    @EJB
    private pg.session.InfoEmpresasFacade ejbFacade;
    QpTotal qpt;
    

    /** Creates a new instance of QPDireccaoEstab */
    public QPDireccaoEstabController() {
    }
    
    private InfoEmpresasFacade getFacade() {
        return ejbFacade;
 
    }
    
       public LazyDataModel getItems() {
        if (items == null) {
            items = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad = getFacade().findDirecEstab(new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            items.setPageSize(10);
            items.setRowCount(getFacade().countDirecEstab());
        }
        return items;
    }

     public String doPDF(){

       current = (Tuple) getItems().getRowData();
       listInfoEmpresas = getFacade().findByDirecEstab( current.get("siglaE").toString(), current.get("cdSbu").toString(), current.get("cdEstab").toString());
       QPtoPDF qp = new QPtoPDF();
        try {
            qp.exportaQP(FacesContext.getCurrentInstance() , "qp",  current.get("dsSocial").toString(), current.get("dsDireccao").toString(), listInfoEmpresas );
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
      listInfoEmpresas = getFacade().findByDirecEstab( current.get("siglaE").toString(), current.get("cdSbu").toString(), current.get("cdEstab").toString());
         qpt = new QpTotal(current.get("dsAbrv").toString() + " - " + current.get("dsEstab").toString(), listInfoEmpresas);
    }

    public QpTotal getQpt() {
        return qpt;
    }    
     
     
    
}
