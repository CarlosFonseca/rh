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
import pg.entities.QacEmpresas;

/**
 *
 * @author bnf02107
 */
@ManagedBean
@SessionScoped
public class MovimentacoesController {

    
    Integer ano = 0;
    List<Integer> anos = new ArrayList(); 
    QacEmpresas qacEmpresas;
    @EJB
    private pg.session.QacOrganizerDetailsFacade ejbFacade;

    /**
     * Creates a new instance of MovimentacoesController
     */
    public MovimentacoesController() {
        if (ano == 0 ){
           Calendar cal=Calendar.getInstance(); 
           ano = cal.get(Calendar.YEAR);
        }
            
    }

    public Integer getAno() {
        
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
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

    public QacEmpresas getQacEmpresas() {
        return qacEmpresas;
    }

    public void setQacEmpresas(QacEmpresas qacEmpresas) {
        this.qacEmpresas = qacEmpresas;
    }
    
    
    
    public List<Object[]> getMovimentacoesPorEmpresas(){
        
        if (qacEmpresas==null) {
           return ejbFacade.movimentacoesPorEmpresas("Banif", ano);
        } else{
          return ejbFacade.movimentacoesPorEmpresas(qacEmpresas.getSiglaE(), ano);

        }
        
    }
    
   public int getTotalEntradas() {  
         int total = 0;  
   
         for(Object[] movimentacoes : getMovimentacoesPorEmpresas()) {  
             total += Integer.parseInt(movimentacoes[4].toString());  
         }  
   
         return total;  
     } 
  
   public int getTotalMovEntr() {  
         int total = 0;  
   
         for(Object[] movimentacoes : getMovimentacoesPorEmpresas()) {  
             total += Integer.parseInt(movimentacoes[5].toString());  
         }  
   
         return total;  
     } 
    
   public int getTotalMovSai() {  
         int total = 0;  
   
         for(Object[] movimentacoes : getMovimentacoesPorEmpresas()) {  
             total += Integer.parseInt(movimentacoes[6].toString());  
         }  
   
         return total;  
     } 

   public int getTotalSaida() {  
         int total = 0;  
   
         for(Object[] movimentacoes : getMovimentacoesPorEmpresas()) {  
             total += Integer.parseInt(movimentacoes[7].toString());  
         }  
   
         return total;  
     } 

      public int getVariacao() {  
         int total = 0;  
   
         for(Object[] movimentacoes : getMovimentacoesPorEmpresas()) {  
             total += Integer.parseInt(movimentacoes[8].toString());  
         }  
   
         return total;  
     } 

}
