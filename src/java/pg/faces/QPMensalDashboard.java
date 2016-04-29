/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.faces;

import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Tuple;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import pg.entities.UserInfoContratos;

/**
 *
 * @author bnf02107
 */
@ManagedBean
@RequestScoped
public class QPMensalDashboard {    
    
    
    private Integer ano ; 
    private Integer mes;   
    private DashboardModel model;
    private boolean esconder = true;
    @EJB
    private pg.session.UserInfoContratosFacade userInfoContratosFacade; 
    @EJB
    private pg.session.InfoEmpresasFacade infoEmpresasFacade; 
    
    
    
    /** Creates a new instance of QPMensalDashboard */
    public QPMensalDashboard() {

     if (ano == null){
         Calendar now = Calendar.getInstance();
           now.add(Calendar.MONTH, -1);       
           ano = now.get(Calendar.YEAR);
           mes = now.get(Calendar.MONTH)+1;  
        } 
       
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
    
    public Integer getAno() {
        
        
        return ano;
    }

    public void setAno(Integer ano) {
        
        this.ano = ano;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public boolean isEsconder() {
        return esconder;
    }

    public void setEsconder(boolean esconder) {
        this.esconder = esconder;
    }
 
    
    
    public List<UserInfoContratos> getDemitidos(){
            return userInfoContratosFacade.Demitidos(ano, mes);
    } 
    
    
     public List<UserInfoContratos> getAdmitidos(){          
        return userInfoContratosFacade.Admitidos(ano, mes);

    } 

    public List<UserInfoContratos> getDemitidosVinculo(){        
       return userInfoContratosFacade.DemitidosVinculo();        
    }
    
    public List<Tuple> getEmpregSemVinculo(){        
       return userInfoContratosFacade.EmpregSemVinculo();    
    }
        
    public List<UserInfoContratos> getContratadosSemVinculo(){        
       return userInfoContratosFacade.ContratadosSemVinculo();    
    }   
    
    public List<Tuple> getTotalAoServicoMensal(){
               return infoEmpresasFacade.TotalAoServicoMensal(ano, mes);

    } 
    

        
}
