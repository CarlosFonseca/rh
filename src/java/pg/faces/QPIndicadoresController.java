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
import javax.persistence.Tuple;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import pg.session.InfoEmpresasFacade;

/**
 *
 * @author bnf02107
 */
@ManagedBean
@SessionScoped
public class QPIndicadoresController {
    
    private LazyDataModel items = null;
    private LazyDataModel aoServico = null;
    private LazyDataModel porGenero = null;
    private LazyDataModel porMediaEtaria = null;
    private LazyDataModel porNivel = null;
    private LazyDataModel porAntiguidade = null;
    private LazyDataModel porAntiguidadeBanca = null;
    private LazyDataModel porEstruturaEtaria = null;
    private LazyDataModel porHabilitacao = null;
    private LazyDataModel porSitGeo = null;
    private LazyDataModel aoServicoPorDirecao = null;
    private LazyDataModel porGeneroPorDirecao = null;
    private LazyDataModel porMediaEtariaPorDirecao = null;
    private LazyDataModel porNivelPorDirecao = null;
    private LazyDataModel porAntiguidadePorDirecao = null;
    private LazyDataModel porAntiguidadeBancaPorDirecao = null;
    private LazyDataModel porEstruturaEtariaPorDirecao = null;
    private LazyDataModel porHabilitacaoPorDirecao = null;
    private LazyDataModel porSitGeoPorDirecao = null;

    
    @EJB
    private pg.session.InfoEmpresasFacade ejbFacade; 

    /** Creates a new instance of QPIndicadoresController */
    public QPIndicadoresController() {
    }
    
    private InfoEmpresasFacade getFacade() {
        return ejbFacade;
 
    }
    
    public List<Tuple> getTotalAoServico(){
        return ejbFacade.totalAoServicoPorEmpresa();
    }

   public List<Tuple> getTotalporSexo(){
        return ejbFacade.totalporSexoPorEmpresa();
    }

    public List<Tuple> getMediaEtaria(){
        return ejbFacade.mediaEtariaPorEmpresa();
    }
   
    public List<Tuple> getTotalporNivel(){
        return ejbFacade.totalporNivelPorEmpresa();
    }   

    public List<Tuple> getTotalporAntiguidade(){
        return ejbFacade.totalPorAntiguidadePorEmpresa();
    } 
    
    public List<Tuple> getTotalporAntiguidadeBanca(){
        return ejbFacade.totalPorAntiguidadeBancaPorEmpresa();
    } 

    public List<Tuple> getTotalporEstruturaEtaria(){
        return ejbFacade.totalPorEstruturaEtariaPorEmpresa();
    } 

     public List<Tuple> getTotalporhabilitacao(){
        return ejbFacade.totalPorHabilitacaoPorEmpresa();
    } 
     
     public List<Tuple> getTotalporSitGeo(){
        return ejbFacade.totalporSitGeoPorEmpresa();
    }
     

     
     public List<Tuple> getTableauDeBordPorEmpresa(){
        return ejbFacade.TableauDeBordPorEmpresa();
    } 
     
     
     
   //------------------------------------------------------------------------------   
//-------------------------------- Por Direcao  --------------------------------   
//------------------------------------------------------------------------------   
        
     public LazyDataModel getAoServicoPorDirecao() {
        if (aoServicoPorDirecao == null) {
            aoServicoPorDirecao = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad = getFacade().aoServicoPorDirecao(new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            aoServicoPorDirecao.setPageSize(10);
            aoServicoPorDirecao.setRowCount(getFacade().countDirecao());
        }
        return aoServicoPorDirecao;
    }    
     
//    public LazyDataModel getAoServicoPorDirecao() {
//        
//        if (aoServicoPorDirecao == null) {
//            aoServicoPorDirecao = new LazyDataModel() {
//
//                @Override
//                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
//                    List lazyCad = getFacade().aoServicoPorDirecao(new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
//                    return lazyCad;
//                }
//            };
//
//            aoServicoPorDirecao.setPageSize(10);
//            aoServicoPorDirecao.setRowCount(getFacade().countDirecao());
//        }
//        return aoServicoPorDirecao;
//    }

    public LazyDataModel getPorGeneroPorDirecao() {
        if (porGeneroPorDirecao == null) {
            porGeneroPorDirecao = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad = getFacade().porGeneroPorDirecao(new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            porGeneroPorDirecao.setPageSize(10);
            porGeneroPorDirecao.setRowCount(getFacade().countDirecao());
        }
        return porGeneroPorDirecao;
    }
        
    public LazyDataModel getPorMediaEtariaPorDirecao() {
        if (porMediaEtariaPorDirecao == null) {
            porMediaEtariaPorDirecao = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad = getFacade().porMediaEtariaPorDirecao(new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            porMediaEtariaPorDirecao.setPageSize(10);
            porMediaEtariaPorDirecao.setRowCount(getFacade().countDirecao());
        }
        return porMediaEtariaPorDirecao;    
    }  
    
    public LazyDataModel getPorNivelPorDirecao() {
        if (porNivelPorDirecao  == null) {
            porNivelPorDirecao = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad = getFacade().porNivelPorDirecao(new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            porNivelPorDirecao.setPageSize(10);
            porNivelPorDirecao.setRowCount(getFacade().countDirecao());
        }
        return porNivelPorDirecao;    
    }
    
    
    public LazyDataModel getPorAntiguidadePorDirecao() {
        if (porAntiguidadePorDirecao == null) {
            porAntiguidadePorDirecao = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad = getFacade().porAntiguidadePorDirecao(new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            porAntiguidadePorDirecao.setPageSize(10);
            porAntiguidadePorDirecao.setRowCount(getFacade().countDirecao());
        }
        return porAntiguidadePorDirecao;    
    } 
    
        public LazyDataModel getPorAntiguidadeBancaPorDirecao() {
        if (porAntiguidadeBancaPorDirecao == null) {
            porAntiguidadeBancaPorDirecao = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad = getFacade().porAntiguidadeBancaPorDirecao(new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            porAntiguidadeBancaPorDirecao.setPageSize(10);
            porAntiguidadeBancaPorDirecao.setRowCount(getFacade().countDirecao());
        }
        return porAntiguidadeBancaPorDirecao;    
    }     

        public LazyDataModel getPorEstruturaEtariaPorDirecao() {
        if (porEstruturaEtariaPorDirecao == null) {
            porEstruturaEtariaPorDirecao = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad = getFacade().porEstruturaEtariaPorDirecao(new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            porEstruturaEtariaPorDirecao.setPageSize(10);
            porEstruturaEtariaPorDirecao.setRowCount(getFacade().countDirecao());
        }
        return porEstruturaEtariaPorDirecao;    
    }     
        public LazyDataModel getPorHabilitacaoPorDirecao() {
        if (porHabilitacaoPorDirecao == null) {
            porHabilitacaoPorDirecao = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad = getFacade().porHabilitacaoPorDirecao(new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            porHabilitacaoPorDirecao.setPageSize(10);
            porHabilitacaoPorDirecao.setRowCount(getFacade().countDirecao());
        }
        return porHabilitacaoPorDirecao;    
    }        
  
        public LazyDataModel getPorSitGeoPorDirecao() {
        if (porSitGeoPorDirecao == null) {
            porSitGeoPorDirecao = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad = getFacade().porSitGeoPorDirecao(new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            porSitGeoPorDirecao.setPageSize(10);
            porSitGeoPorDirecao.setRowCount(getFacade().countDirecao());
        }
        return porSitGeoPorDirecao;    
    }        
     
    
    
}
