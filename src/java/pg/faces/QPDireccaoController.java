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
import pg.faces.util.QPDirecao;
import pg.faces.util.QPtoDirecaoPDF;
import pg.faces.util.QpTotalFicticia;
import pg.session.InfoEmpresasFacade;

/**
 *
 * @author bnf02107
 */
@ManagedBean
@SessionScoped
public class QPDireccaoController {
    
    private Tuple current;
    private List<QPDirecao> listQPDirecao ;
    private LazyDataModel items = null;
    @EJB
    private pg.session.InfoEmpresasFacade ejbFacade;

    QpTotalFicticia qpt;
    
    /** Creates a new instance of QPDireccaoController */
    public QPDireccaoController() {
    }
    
    
    private InfoEmpresasFacade getFacade() {
        return ejbFacade;
 
    }

    
    public LazyDataModel getItems() {
        if (items == null) {
            items = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad = getFacade().findDireccao(new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            items.setPageSize(10);
            items.setRowCount(getFacade().countDirecao());
        }
        return items;
    }
       
     public void doPDF(){

       current = (Tuple) getItems().getRowData();
       listQPDirecao = getFacade().findByDireccao(current.get("cdSbu").toString());
       QPtoDirecaoPDF qp = new QPtoDirecaoPDF();
       
       ///aqui
        try {
            qp.exportaQP(FacesContext.getCurrentInstance() , "qp", current.get("dsDireccao").toString(), listQPDirecao );
        } catch (IOException ex) {
            Logger.getLogger(QPDireccaoController.class.getName()).log(Level.SEVERE, null, ex);
        }       

     }

    public Tuple getCurrent() {
        return current;
    }

    public void setCurrent(Tuple current) {
        this.current = current; 
        listQPDirecao = getFacade().findByDireccao(current.get("cdSbu").toString());
        qpt = new QpTotalFicticia(current.get("dsAbrv").toString(), listQPDirecao) ; 

    }

    public QpTotalFicticia getQpt() {
        return qpt;
    } 
    
}
