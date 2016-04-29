/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.faces;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.Tuple;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import pg.session.QacOrganizerDetailsFacade;

/**
 *
 * @author bnf02107
 */
@ManagedBean
@SessionScoped
public class HistQPController {
    
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
    
    private CartesianChartModel aoServicoEmpresaModel;  
    private CartesianChartModel aoServicoBanifModel;  
    private CartesianChartModel porGeneroEmpresaModel;  
    private CartesianChartModel porGeneroBanifModel;  
    private CartesianChartModel porMediaEtariaEmpresaModel;  
    private CartesianChartModel porMediaEtariaBanifModel;  
    
    @EJB
    private pg.session.QacOrganizerDetailsFacade ejbFacade;

    /** Creates a new instance of HistQPController */
    public HistQPController() {
    }
    
    
     private QacOrganizerDetailsFacade getFacade() {
        return ejbFacade;
 
    }
    
    public LazyDataModel getAoServico() {
        if (aoServico == null) {
            aoServico = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad = getFacade().aoServicoPorEmpresa(new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            aoServico.setPageSize(10);
            aoServico.setRowCount(getFacade().countEmpresaAnoMes());
        }
        return aoServico;
    }


      
      
   
    
    
    public LazyDataModel getPorGenero() {
        if (porGenero == null) {
            porGenero = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad = getFacade().porGeneroPorEmpresa(new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            porGenero.setPageSize(10);
            porGenero.setRowCount(getFacade().countEmpresaAnoMes());
        }
        return porGenero;
    }
        
    public LazyDataModel getPorMediaEtaria() {
        if (porMediaEtaria == null) {
            porMediaEtaria = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad = getFacade().porMediaEtariaPorEmpresa(new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            porMediaEtaria.setPageSize(10);
            porMediaEtaria.setRowCount(getFacade().countEmpresaAnoMes());
        }
        return porMediaEtaria;    
    }  
    
    public LazyDataModel getPorNivel() {
        if (porNivel == null) {
            porNivel = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad = getFacade().porNivelPorEmpresa(new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            porNivel.setPageSize(10);
            porNivel.setRowCount(getFacade().countEmpresaAnoMes());
        }
        return porNivel;    
    }
    
    
    public LazyDataModel getPorAntiguidade() {
        if (porAntiguidade == null) {
            porAntiguidade = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad = getFacade().porAntiguidadePorEmpresa(new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            porAntiguidade.setPageSize(10);
            porAntiguidade.setRowCount(getFacade().countEmpresaAnoMes());
        }
        return porAntiguidade;    
    } 
    
        public LazyDataModel getPorAntiguidadeBanca() {
        if (porAntiguidadeBanca == null) {
            porAntiguidadeBanca = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad = getFacade().porAntiguidadeBancaPorEmpresa(new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            porAntiguidadeBanca.setPageSize(10);
            porAntiguidadeBanca.setRowCount(getFacade().countEmpresaAnoMes());
        }
        return porAntiguidadeBanca;    
    }     

    public LazyDataModel getPorEstruturaEtaria() {
        if (porEstruturaEtaria == null) {
            porEstruturaEtaria = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad = getFacade().porEstruturaEtariaPorEmpresa(new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            porEstruturaEtaria.setPageSize(10);
            porEstruturaEtaria.setRowCount(getFacade().countEmpresaAnoMes());
        }
        return porEstruturaEtaria;    
    }  
        
    public LazyDataModel getPorHabilitacao() {
        if (porHabilitacao == null) {
            porHabilitacao = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad = getFacade().porHabilitacaoPorEmpresa(new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            porHabilitacao.setPageSize(10);
            porHabilitacao.setRowCount(getFacade().countEmpresaAnoMes());
        }
        return porHabilitacao;    
    }      

    public LazyDataModel getPorSitGeo() {
        if (porSitGeo == null) {
            porSitGeo = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad = getFacade().porSitGeoPorEmpresa (new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            porSitGeo.setPageSize(10);
            porSitGeo.setRowCount(getFacade().countEmpresaAnoMes());
        }
        return porSitGeo;    
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
            aoServicoPorDirecao.setRowCount(getFacade().countDirecaoAnoMes());
        }
        return aoServicoPorDirecao;
    }

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
            porGeneroPorDirecao.setRowCount(getFacade().countDirecaoAnoMes());
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
            porMediaEtariaPorDirecao.setRowCount(getFacade().countDirecaoAnoMes());
        }
        return porMediaEtariaPorDirecao;    
    }  
    
    public LazyDataModel getPorNivelPorDirecao() {
        if (porNivelPorDirecao == null) {
            porNivelPorDirecao = new LazyDataModel() {

                @Override
                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
                    List lazyCad = getFacade().porNivelPorDirecao(new int[]{fist, fist + pageSize}, sortField, sortOder, filters);
                    return lazyCad;
                }
            };

            porNivelPorDirecao.setPageSize(10);
            porNivelPorDirecao.setRowCount(getFacade().countDirecaoAnoMes());
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
            porAntiguidadePorDirecao.setRowCount(getFacade().countDirecaoAnoMes());
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
            porAntiguidadeBancaPorDirecao.setRowCount(getFacade().countDirecaoAnoMes());
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
            porEstruturaEtariaPorDirecao.setRowCount(getFacade().countDirecaoAnoMes());
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
            porHabilitacaoPorDirecao.setRowCount(getFacade().countDirecaoAnoMes());
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
            porSitGeoPorDirecao.setRowCount(getFacade().countDirecaoAnoMes());
        }
        return porSitGeoPorDirecao;    
    }       
    
//------------------------------------------------------------------------------   
//-------------------------------- Por Gráficos  --------------------------------   
//------------------------------------------------------------------------------   

    
        
    public CartesianChartModel getAoServicoEmpresaModel() { 
        if (aoServicoEmpresaModel == null) {
            aoServicoEmpresaModel = new CartesianChartModel();          
            Iterator<Tuple> iterator = getFacade().aoServicoPeriodos(false,"Banif","BCA").iterator();
             CreateModel(aoServicoEmpresaModel, iterator);  
        }
        return aoServicoEmpresaModel;
     }  
  
      public CartesianChartModel getAoServicoBanifModel() { 
        if (aoServicoBanifModel == null) {
            aoServicoBanifModel = new CartesianChartModel();          
            Iterator<Tuple> iterator = getFacade().aoServicoPeriodos(true,"Banif").iterator();            
            CreateModel(aoServicoBanifModel, iterator);            
        }
        return aoServicoBanifModel;
     }  

    public CartesianChartModel getPorGeneroEmpresaModel() { 
        if (porGeneroEmpresaModel == null) {
            porGeneroEmpresaModel = new CartesianChartModel();          
            Iterator<Tuple> iterator = getFacade().porGeneroPeriodos(false,"Banif","BCA").iterator();
             CreateModel(porGeneroEmpresaModel, iterator);  
        }
        return porGeneroEmpresaModel;
     }  
  
      public CartesianChartModel getPorGeneroBanifModel() { 
        if (porGeneroBanifModel == null) {
            porGeneroBanifModel = new CartesianChartModel();          
            Iterator<Tuple> iterator = getFacade().porGeneroPeriodos(true,"Banif").iterator();            
            CreateModel(porGeneroBanifModel, iterator);            
        }
        return porGeneroBanifModel;
     }  

    public CartesianChartModel getPorMediaEtariaEmpresaModel() { 
        if (porMediaEtariaEmpresaModel == null) {
            porMediaEtariaEmpresaModel = new CartesianChartModel();          
            Iterator<Tuple> iterator = getFacade().porMediaEtariaPeriodos(false,"BCA").iterator();
             CreateModel(porMediaEtariaEmpresaModel, iterator);  
        }
        return porMediaEtariaEmpresaModel;
     }  
      
    
      public void CreateModel(CartesianChartModel cartesianChartModel, Iterator<Tuple> iterator){
         
            ChartSeries Empresa = null; 
            while(iterator.hasNext()) {
                Tuple t =iterator.next();

                if(Empresa==null) {
                     Empresa = new ChartSeries();
                    Empresa.setLabel(t.get("empresa").toString());
                }
                else if (!t.get("empresa").toString().equals(Empresa.getLabel()))  {
                    cartesianChartModel.addSeries(Empresa);
                    Empresa = new ChartSeries();
                    Empresa.setLabel(t.get("empresa").toString());                
                }
                Empresa.set(t.get("legenda").toString(), (Number) t.get("valor") );
            }
            
            if (!iterator.hasNext()) {
                cartesianChartModel.addSeries(Empresa);
            }
     }
 
        
}
