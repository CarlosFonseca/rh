/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.faces;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.LazyDataModel;
import pg.entities.PayIrsNreDescontos;
import pg.session.PayIrsNreDescontosFacade;

/**
 *
 * @author bnf02107
 */
@ManagedBean(name = "payIrsNreDescontosController")
@SessionScoped
public class PayIrsNreDescontosController {
    
    Integer ano = 0;
    Integer mes = 0;
    List<Integer> anos = new ArrayList(); 


    private PayIrsNreDescontos current;
    private LazyDataModel items = null;
    @EJB
    private pg.session.PayIrsNreDescontosFacade ejbFacade;
    private int selectedItemIndex;
    private int tableWidth;

        /**
     * Creates a new instance of PayIrsNreDescontosController
     */
    public PayIrsNreDescontosController() {
        if (ano == 0 ){
           Calendar cal=Calendar.getInstance(); 
           ano = cal.get(Calendar.YEAR);
           mes = cal.get(Calendar.MONTH);
        }        
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
       

    public List<Integer> getAnos() {
        if (anos.isEmpty()){
            Calendar cal=Calendar.getInstance();
            for (int i = 0; cal.get(Calendar.YEAR)-i > 2007; i++) {
              anos.add(cal.get(Calendar.YEAR)-i);  
            }
        }
        return anos;
    }

    public void setAnos(List<Integer> anos) {
        this.anos = anos;
    }

        
        public PayIrsNreDescontos getSelected() {
        if (current == null) {
            current = new PayIrsNreDescontos();
            selectedItemIndex = -1;
        }
        return current;
    }

    private PayIrsNreDescontosFacade getFacade() {
        return ejbFacade;
    }
    
    
    
    
    public List<Object[]> getDmrValidacaoPorAnoMes(){
        return ejbFacade.dmrValidacaoPorAnoMes(ano, mes);        
    }
    
    
    public List<Object[]> getDmrErrosPorNreAnoMes(){
        return ejbFacade.dmrErrosPorNreAnoMes(ano, mes);
        
    } 
 

    
}
