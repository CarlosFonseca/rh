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
public class QPEstruturaController {
    
    private Tuple current;
    private List<InfoEmpresas> listInfoEmpresas ;
    private LazyDataModel items = null;
    @EJB
    private pg.session.InfoEmpresasFacade ejbFacade;
    QpTotal qpt;

    /** Creates a new instance of QPEstruturaController */
    public QPEstruturaController() {
    }
    

    private InfoEmpresasFacade getFacade() {
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
       listInfoEmpresas = getFacade().findByEstrutura( current.get("siglaE").toString(),Integer.parseInt(current.get("cdEstrutura").toString()));
       
       QPtoPDF qp = new QPtoPDF();
       
       String direccao = "";
//       if (current.get("dsDireccao")!=null) direccao = current.get("dsDireccao").toString();
       
        try {
            qp.exportaQP(FacesContext.getCurrentInstance() , "qp",  current.get("dsSocial").toString(), direccao, listInfoEmpresas );
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
       listInfoEmpresas = getFacade().findByEstrutura( current.get("siglaE").toString(),Integer.parseInt(current.get("cdEstrutura").toString()));
        qpt = new QpTotal(current.get("dsComp").toString(), listInfoEmpresas) ; 

    }
    public QpTotal getQpt() {
        return qpt;
    }
     
     
}
