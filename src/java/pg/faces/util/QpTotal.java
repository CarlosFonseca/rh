/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.faces.util;

import java.util.Iterator;
import java.util.List;
import pg.entities.InfoEmpresas;


/**
 *
 * @author bnf02107
 */
public class QpTotal {    
    
    private long totalQuadro = 0 ; 
    private long totalQuadroAdock = 0;
    private long totalTotal = 0;
    private long totalSemVaga = 0;
    private String quadro = "";    
    private int cdEstrutura=0;
    String cdFuncao = "";

    
    private String designacao = "";    
    private String qpMaisAdoc = "";
    private long situacao = 0;
    private long vagas =0;
    private long eSVagas = 0;
    
    public QpTotal(String designacao , List<InfoEmpresas> listInfoEmpresas){
        
        this.designacao = designacao;
        
        Iterator<InfoEmpresas> it = listInfoEmpresas.iterator();

        while(it.hasNext()){
            InfoEmpresas ie = it.next(); 

              if (ie.getCadastros().getInfoProfs().getCdFuncao()!=null) 
                  cdFuncao = ie.getCadastros().getInfoProfs().getCdFuncao().getCdFuncao();
              else cdFuncao="";
            
             if(ie.getInfoEmpresasPK().getCdEstrutura() == cdEstrutura){
              if (ie.getCadastros().getCdSituacao().getCdSituacao()==21){
                  totalSemVaga +=1;
              }            
              
              if ((ie.getCadastros().getCdSituacao().getCdSituacao() !=21)
              && (!cdFuncao.equals("SL"))){
                    totalTotal += 1;
              }
              
            } else if(ie.getInfoEmpresasPK().getCdEstrutura() != cdEstrutura){              
               if (ie.getCadastros().getCdSituacao().getCdSituacao()==21){
                totalSemVaga += 1;
              }

              quadro = ie.getQacEstruturas().getPontuacao();
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
              
             
             if ((ie.getCadastros().getCdSituacao().getCdSituacao() !=21)
             && (!cdFuncao.equals("SL"))){
                    totalTotal +=1;
             }
                         
          }               
            
        cdEstrutura = ie.getQacEstruturas().getQacEstruturasPK().getCdEstrutura();     
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
