/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pg.faces;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pg.entities.InfoEmpresas;
import pg.entities.QacAreasNegocio;
import pg.entities.QacEmpresas;
import pg.entities.QacEstabelecimentos;
import pg.entities.QacEstruturas;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import pg.faces.util.JsfUtil;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import pg.session.InfoEmpresasFacade;
import pg.faces.util.QPtoPDF;

/**
 *
 * @author Carlos
 */
@ManagedBean
@ViewScoped
public class QPController {
    
    
    private InfoEmpresas current;
    private LazyDataModel items = null;

    
    QacEmpresas qacEmpresas;
    String empresa = "Banif" ;
    int tabIndex = 0 ;
    boolean disableSelectores =true ;
    QacAreasNegocio qacAreasNegocio;
    String direccao = "";
    QacEstruturas qacEstruturas;
    int estrutura;
    QacEstabelecimentos qacEstabelecimentos;
    String estabelecimento = "";
    QacAreasNegocio qacAreasNegocioEstab;
    String DirecaoEstab = "";

    List<QacEstruturas> listQacEstrutura;
    List<InfoEmpresas>  listInfoEmpresas;
    
//    @EJB private pg.session.QacEstruturasFacade ejbFacade;
    @EJB private pg.session.InfoEmpresasFacade ejbEmpresaFacade;
//    @EJB private pg.session.QacEmpresasFacade ejbQacEmpresasFacade;


    /** Creates a new instance of QPController */
    public QPController() {     
    }

    public QacEmpresas getQacEmpresas() {
         if(qacEmpresas == null) {
//            this.setQacEmpresas(ejbQacEmpresasFacade.find("Banif"));            
            disableSelectores = false ;
         }
         
        return qacEmpresas;
    }

    public void setQacEmpresas(QacEmpresas qacEmpresas) {  
            this.qacEmpresas = qacEmpresas;
            disableSelectores = false ;

        try {
            this.empresa = this.qacEmpresas.getSiglaE();
        } catch (Exception e) {
            this.empresa ="";
            disableSelectores = true;
        }  
    }

    public String getEmpresa() {   
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public boolean isDisableSelectores() {
        return disableSelectores;
    }

    

//-----------------------por Direcção-------------------------------------------
    public QacAreasNegocio getQacAreasNegocio() {
        return qacAreasNegocio;
    }

    public void setQacAreasNegocio(QacAreasNegocio qacAreasNegocio) {
        this.qacAreasNegocio = qacAreasNegocio;
        this.tabIndex = 0;
        try {
            this.direccao = qacAreasNegocio.getCdSbu();
//            this.direccao = "50";
        } catch (Exception e) {
           this.direccao = "";
        }
    }


//-----------------------por Estrutura------------------------------------------
    public QacEstruturas getQacEstruturas() {
        return qacEstruturas;
    }

    public void setQacEstruturas(QacEstruturas qacEstruturas) {
        this.qacEstruturas = qacEstruturas;
        this.tabIndex = 1;
        try {
            this.estrutura = qacEstruturas.getQacEstruturasPK().getCdEstrutura();
        } catch (Exception e) {
        }
    }


//----------------------por direcção Estabelecimento----------------------------

    public QacAreasNegocio getQacAreasNegocioEstab() {
        return qacAreasNegocioEstab;
    }

    public void setQacAreasNegocioEstab(QacAreasNegocio qacAreasNegocioEstab) {
        this.qacAreasNegocioEstab = qacAreasNegocioEstab;
        this.tabIndex = 2;
        try {
            this.DirecaoEstab = qacAreasNegocioEstab.getCdSbu();
        } catch (Exception e) {
        }
    }

    public QacEstabelecimentos getQacEstabelecimentos() {
        return qacEstabelecimentos;
    }

    public void setQacEstabelecimentos(QacEstabelecimentos qacEstabelecimentos) {
        this.qacEstabelecimentos = qacEstabelecimentos;
        this.tabIndex = 2;
        try {
            this.estabelecimento = qacEstabelecimentos.getQacEstabelecimentosPK().getCdEstab();
        } catch (Exception e) {
        }
    }
    
//    public List<InfoEmpresas> getQuadro(){
//        listInfoEmpresas =  ejbEmpresaFacade. findBy(null,tabIndex, direccao, empresa, estrutura, DirecaoEstab, estabelecimento);
//        return listInfoEmpresas;
//    }


      private InfoEmpresasFacade getEmpresaFacade() {
        return ejbEmpresaFacade;
    }  
     
//   public LazyDataModel getItems() {
//        if (items == null) {
//            items = new LazyDataModel() {
//
//                @Override
//                public List load(int fist, int pageSize, String sortField, SortOrder sortOder, Map filters) {
//                    // tabIndex é o tipo de pesquisa
//                    // tabIndex = 0 ===> direccao
//                    // tabIndex = 1 ===> empresa estrutura
//                    // tabIndex = 2 ===> DirecaoEstab estabelecimento
//                    List lazyCad = getEmpresaFacade().findBy(new int[]{fist, fist + pageSize},tabIndex, direccao, empresa, estrutura, DirecaoEstab, estabelecimento);
//                    return lazyCad;
//                }
//            };
//
//            items.setPageSize(10);
//            items.setRowCount(getEmpresaFacade().count());
//        }
//        return items;
//    }

//----------------------------------------------- Exporta PDF-------------------------------------------------   
//   public String direccaoExpPDF() {
//       
//        FacesContext context = FacesContext.getCurrentInstance();
//
//         if (empresa.equals("")){
//             JsfUtil.addErrorMessage("Selecione uma Empresa");
//         }
//         if (direccao.equals(""))
//             JsfUtil.addErrorMessage("Selecione uma Direcção");
//         
//         if(qacAreasNegocio == null || qacEmpresas == null)
//             JsfUtil.addErrorMessage("Não existem dados para o critério seleccionado ");      
//
//         if(!context.getMessageList().isEmpty()){
//             return null;
//         }else{
//               QPtoPDF qp = new QPtoPDF();
//               try {
//                    JsfUtil.addErrorMessage(qp.exportaQP(FacesContext.getCurrentInstance() , "qp", qacEmpresas.getDsSocial(), qacAreasNegocio.getDsComp(), getQuadro() ));
//               } catch (IOException ex) {
//                    Logger.getLogger(QPController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//         }
//             return null;
//   }

//      public String estruturaExpPDF() {
//       
//        FacesContext context = FacesContext.getCurrentInstance();
//
//         if (empresa.equals("")){
//             JsfUtil.addErrorMessage("Selecione uma Empresa");
//         }
//         if (estrutura==0 )
//             JsfUtil.addErrorMessage("Selecione uma estrutura ");
//         
//         if(qacEstruturas == null || qacEmpresas == null)
//             JsfUtil.addErrorMessage("Não existem dados para o critério seleccionado ");
//
//         if(!context.getMessageList().isEmpty()){
//             return null;
//         }else{
//               QPtoPDF qp = new QPtoPDF();
//               try {
//                    if (qacEstruturas.getCdSbu()!=null)
//                        qp.exportaQP(FacesContext.getCurrentInstance() , "qp", qacEmpresas.getDsSocial(), qacEstruturas.getCdSbu().getDsComp(), getQuadro() );
//                    else 
//                        qp.exportaQP(FacesContext.getCurrentInstance() , "qp",  qacEmpresas.getDsSocial(),"", getQuadro() );
//
//               } catch (Exception ex) {
//                    Logger.getLogger(QPController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//         }
//             return null;
//   }

//   public String direccaoEstabExpPDF() {
//       
//        FacesContext context = FacesContext.getCurrentInstance();
//
//         if (empresa.equals("")){
//             JsfUtil.addErrorMessage("Selecione uma Empresa");
//         }
//         if (DirecaoEstab.equals("") || estabelecimento.equals(""))
//             JsfUtil.addErrorMessage("Selecione uma Direcção / Estabecimento");
//
//         if(qacAreasNegocioEstab == null || qacEmpresas == null)
//             JsfUtil.addErrorMessage("Não existem dados para o critério seleccionado ");
//         
//         
//         if(!context.getMessageList().isEmpty()){
//             return null;
//         }else{
//               QPtoPDF qp = new QPtoPDF();
//               try {
//                    JsfUtil.addErrorMessage(qp.exportaQP(FacesContext.getCurrentInstance() , "qp", qacEmpresas.getDsSocial(), qacAreasNegocioEstab.getDsComp(), getQuadro() ));
//               } catch (IOException ex) {
//                    Logger.getLogger(QPController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//         }
//             return null;
//   }
      
   
   
 }
