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
public class MovimentDirecController {

    
    Integer ano = 0;
    Integer mes = 0;
    List<Integer> anos = new ArrayList(); 
    QacEmpresas qacEmpresas = null;


    @EJB
    private pg.session.QacOrganizerDetailsFacade ejbFacade;

    /**
     * Creates a new instance of MovimentacoesController
     */
    public MovimentDirecController() {
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

    public QacEmpresas getQacEmpresas() {
        return qacEmpresas;
    }

    public void setQacEmpresas(QacEmpresas qacEmpresas) {  
            this.qacEmpresas = qacEmpresas;
    }

 
    
    
    public List<Object[]> getMovimentacoesPorDirecao(){
        
        if (qacEmpresas == null) {
            return ejbFacade.movimentacoesPorDirecao("Banif",ano, mes);

        } else{
          return ejbFacade.movimentacoesPorDirecao(qacEmpresas.getSiglaE(),ano, mes);

        }
        
    }
    
   public int getTotalEntradas() {  
         int total = 0;  
   
         for(Object[] movimentacoes : getMovimentacoesPorDirecao()) {  
             total += Integer.parseInt(movimentacoes[6].toString());  
         }  
   
         return total;  
     } 
  
   public int getTotalMovEntr() {  
         int total = 0;  
   
         for(Object[] movimentacoes : getMovimentacoesPorDirecao()) {  
             total += Integer.parseInt(movimentacoes[7].toString());  
         }  
   
         return total;  
     } 
    
   public int getTotalMovSai() {  
         int total = 0;  
   
         for(Object[] movimentacoes : getMovimentacoesPorDirecao()) {  
             total += Integer.parseInt(movimentacoes[8].toString());  
         }  
   
         return total;  
     } 

   public int getTotalSaida() {  
         int total = 0;  
   
         for(Object[] movimentacoes : getMovimentacoesPorDirecao()) {  
             total += Integer.parseInt(movimentacoes[9].toString());  
         }  
   
         return total;  
     } 

      public int getVariacao() {  
         int total = 0;  
   
         for(Object[] movimentacoes : getMovimentacoesPorDirecao()) {  
             total += Integer.parseInt(movimentacoes[10].toString());  
         }  
   
         return total;  
     } 

}
