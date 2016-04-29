/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.faces.util;

import java.util.Iterator;
import java.util.List;
import pg.entities.QacOrganizerDetails;


/**
 *
 * @author bnf02107
 */
public class HistQpTotal {    
    
    private long totalQuadro = 0 ; 
    private long totalQuadroAdock = 0;
    private long totalTotal = 0;
    private long totalSemVaga = 0;
    private String quadro = "";    
    private String  cdEstrutura= "";
    String cdFuncao = "";

    
    private String designacao = "";    
    private String qpMaisAdoc = "";
    private long situacao = 0;
    private long vagas =0;
    private long eSVagas = 0;
    
    public HistQpTotal(String designacao , List<QacOrganizerDetails> listQacOrganizerDetails){
        
        this.designacao = designacao;
        
        Iterator<QacOrganizerDetails> it = listQacOrganizerDetails.iterator();

        while(it.hasNext()){
            QacOrganizerDetails qod = it.next(); 

                cdFuncao = qod.getVlr5(); // funcao
            
             if(qod.getVlr30().equals(cdEstrutura)){ //estrutura
              if (qod.getVlr5().equals("21")){
                  totalSemVaga +=1;
              }            
              
              if ((!qod.getVlr5().equals("21"))
              && (!cdFuncao.equals("SL"))){
                    totalTotal += 1;
              }
              
            } else if(!qod.getVlr30().equals(cdEstrutura)){              
               if (qod.getVlr5().equals("21")){
                totalSemVaga += 1;
              }
              quadro = qod.getVlr34(); // dotação do Quadro de Pessoal 
              try {
                  totalQuadro +=  Integer.parseInt(quadro.substring(0, 4));                    
              } catch (Exception e) {
                  System.out.println(e.getMessage());
              }
              
              try {
//                  String a = quadro.substring(0,1);
//                  System.out.println(a);
                  totalQuadroAdock +=  Integer.parseInt(quadro.substring(6, 7)); 

              } catch (Exception e) {
                  System.out.println(e.getMessage());
              }
              
             
             if ((qod.getCadastros().getCdSituacao().getCdSituacao() !=21)
             && (!cdFuncao.equals("SL"))){
                    totalTotal +=1;
             }
                         
          }               
            
        cdEstrutura = qod.getVlr30();     
        }
        if (totalQuadroAdock>0) qpMaisAdoc = totalQuadro + " + " + totalQuadroAdock;
        else qpMaisAdoc = totalQuadro+"";
        situacao = totalTotal;
        vagas = totalQuadro + totalQuadroAdock - totalTotal;
        eSVagas = totalSemVaga;
    }

    public String getQpMaisAdoc() {
        return qpMaisAdoc;
    }

    public long getESVagas() {
        return eSVagas;
    }

    public long getVagas() {
        return vagas;
    }

    public long getSituacao() {
        return situacao;
    }

    public String getDesignacao() {
        return designacao;
    }
    
    

    
}
