/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.faces;

import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.persistence.Tuple;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import pg.entities.UserInfoContratos;

/**
 *
 * @author bnf02107
 */
@ManagedBean
@RequestScoped
public class DashBoardController {
    
    private DashboardModel model;
    @EJB
    private pg.session.UserInfoContratosFacade userInfoContratosFacade;
    private LazyDataModel items = null;
    @EJB
    private pg.session.InfoEmpresasFacade infoEmpresasFacade;
 
    

    /** Creates a new instance of DashBoardController */
    public DashBoardController() {
        
        model = new DefaultDashboardModel();
        
        DashboardColumn column1 = new DefaultDashboardColumn();
        DashboardColumn column2 = new DefaultDashboardColumn();
        DashboardColumn column3 = new DefaultDashboardColumn();
        
        column1.addWidget("cd1");
        column1.addWidget("cd2");
        column1.addWidget("cd3");
        column1.addWidget("cd4");
        column2.addWidget("cd5");
        column2.addWidget("cd6");
        column2.addWidget("cd7");
        column2.addWidget("cd8");
        column2.addWidget("cd9");
            
        model.addColumn(column1);
        model.addColumn(column2);
        model.addColumn(column3);
    }

    public DashboardModel getModel() {
        return model;
    }

    
    //---------------------------------------------------
    //---------------- Dados Actuais
    //---------------------------------------------------
    public List<UserInfoContratos> getDemitidosVinculo(){        
       return userInfoContratosFacade.DemitidosVinculo();        
    }
    
    public List<Tuple> getEmpregSemVinculo(){        
       return userInfoContratosFacade.EmpregSemVinculo();    
    }
    
    
    public List<UserInfoContratos> getContratadosSemVinculo(){        
       return userInfoContratosFacade.ContratadosSemVinculo();    
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
            items.setRowCount(infoEmpresasFacade.count());
        }
        return items;
    }
    
    
     
     
    
}
