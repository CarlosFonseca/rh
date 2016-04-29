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
public class DashBoardController1 {

    private LazyDataModel items = null;
    @EJB
    private pg.session.InfoEmpresasFacade infoEmpresasFacade;
 
    

    /** Creates a new instance of DashBoardController */
    public DashBoardController1() {
        
    }
//    public List<Tuple> getTotalAoServico(){
//        return infoEmpresasFacade.totalAoServicoPorEmpresa();
//    }
//
//   public List<Tuple> getTotalporSexo(){
//        return infoEmpresasFacade.totalporSexoPorEmpresa();
//    }
//
//    public List<Tuple> getMediaEtaria(){
//        return infoEmpresasFacade.mediaEtariaPorEmpresa();
//    }
//   
//    public List<Tuple> getTotalporNivel(){
//        return infoEmpresasFacade.totalporNivelPorEmpresa();
//    }   
//
//    public List<Tuple> getTotalporAntiguidade(){
//        return infoEmpresasFacade.totalPorAntiguidadePorEmpresa();
//    } 
//    
//    public List<Tuple> getTotalporAntiguidadeBanca(){
//        return infoEmpresasFacade.totalPorAntiguidadeBancaPorEmpresa();
//    } 
//
//    public List<Tuple> getTotalporEstruturaEtaria(){
//        return infoEmpresasFacade.totalPorEstruturaEtariaPorEmpresa();
//    } 
//
//     public List<Tuple> getTotalporhabilitacao(){
//        return infoEmpresasFacade.totalPorHabilitacaoPorEmpresa();
//    } 
//     
//     public List<Tuple> getTableauDeBordPorEmpresa(){
//        return infoEmpresasFacade.TableauDeBordPorEmpresa();
//    } 
    
    public LazyDataModel getAniversariantes() {
        if (items == null) {
            items = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad = infoEmpresasFacade.findAniversariantes(new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            items.setPageSize(10);
            items.setRowCount(infoEmpresasFacade.countAniversariantes());
        }
        return items;
    }
    
    
        public LazyDataModel getSemEmail() {
        if (items == null) {
            items = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad = infoEmpresasFacade.semEmail(new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            items.setPageSize(10);
            items.setRowCount(infoEmpresasFacade.count());
        }
        return items;
    }


     
     
    
}
