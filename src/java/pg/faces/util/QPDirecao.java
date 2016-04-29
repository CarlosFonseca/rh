/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pg.faces.util;

import java.util.Date;

/**
 *
 * @author bnf02107
 */
public class QPDirecao {
    
    public Integer nre;
    public String nomeRedz;
    public Date dtNasc;
    public Date dtAdmiss;
    public String habDsRedz;
    public String cdGrupo;
    public Integer cdNivel;
    public Date dtNivel;
    public String funcaoDsComp;
    public String funcIntDsComp;
    public String situacaoDsRedz;
    public String cdFuncao;
    public Integer cdSituacao;
    public int cdEstrutura;
    public String siglaE;
    public String estruturaDsComp;
    public String pontuacao;

    public QPDirecao() {
    }

    public QPDirecao(Integer nre, String nomeRedz, Date dtNasc, Date dtAdmiss, String habDsRedz, String cdGrupo, Integer cdNivel, Date dtNivel, String funcaoDsComp, String funcIntDsComp, String situacaoDsRedz, String cdFuncao, Integer cdSituacao, int cdEstrutura, String siglaE, String estruturaDsComp, String pontuacao) {
        this.nre = nre;
        this.nomeRedz = nomeRedz;
        this.dtNasc = dtNasc;
        this.dtAdmiss = dtAdmiss;
        this.habDsRedz = habDsRedz;
        this.cdGrupo = cdGrupo;
        this.cdNivel = cdNivel;
        this.dtNivel = dtNivel;
        this.funcaoDsComp = funcaoDsComp;
        this.funcIntDsComp = funcIntDsComp;
        this.situacaoDsRedz = situacaoDsRedz;
        this.cdFuncao = cdFuncao;
        this.cdSituacao = cdSituacao;
        this.cdEstrutura = cdEstrutura;
        this.siglaE = siglaE;
        this.estruturaDsComp = estruturaDsComp;
        this.pontuacao = pontuacao;
    }

    public Integer getNre() {
        return nre;
    }

    public String getNomeRedz() {
        return nomeRedz;
    }

    public Date getDtNasc() {
        return dtNasc;
    }

    public Date getDtAdmiss() {
        return dtAdmiss;
    }

    public String getHabDsRedz() {
        return habDsRedz;
    }

    public String getCdGrupo() {
        return cdGrupo;
    }

    public Integer getCdNivel() {
        return cdNivel;
    }

    public Date getDtNivel() {
        return dtNivel;
    }

    public String getFuncaoDsComp() {
        return funcaoDsComp;
    }

    public String getFuncIntDsComp() {
        return funcIntDsComp;
    }

    public String getSituacaoDsRedz() {
        return situacaoDsRedz;
    }

    public String getCdFuncao() {
        return cdFuncao;
    }

    public Integer getCdSituacao() {
        return cdSituacao;
    }

    public int getCdEstrutura() {
        return cdEstrutura;
    }

    public String getSiglaE() {
        return siglaE;
    }

    public String getEstruturaDsComp() {
        return estruturaDsComp;
    }

    public String getPontuacao() {
        return pontuacao;
    }


    

    

    
    
}
