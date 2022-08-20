package br.ufg.inf.auditOP;

import java.math.BigDecimal;
import java.util.HashMap;

public class FatoPagamento {
    private Integer pagamentoID;
    private Integer dataPagamento;
    private Integer municipioID;
    private String credorID;
    private String nomeCredor;
    private BigDecimal valor;
    private String especificacao;
    private Integer numeroEmpenho;
    private String tipoPagamento;

    private Municipio municipio;

    private Integer sumFatosRelevantes = 0;

    private CustoAnalise custo;

    private RelevanciaMontantePagamento beneficio;

    public FatoPagamento(Integer pagamentoID, Integer dataPagamento,
                         Integer municipioID, String credorID, String nomeCredor, BigDecimal valor,
                         String especificacao, Integer numeroEmpenho, String tipoPagamento) {
        this.pagamentoID = pagamentoID;
        this.dataPagamento = dataPagamento;
        this.municipioID = municipioID;
        this.credorID = credorID;
        this.nomeCredor = nomeCredor;
        this.valor = valor;
        this.especificacao = especificacao;
        this.numeroEmpenho = numeroEmpenho;
        this.tipoPagamento = tipoPagamento;
    }

    public CustoAnalise getCusto() {
        return custo;
    }

    public void setCusto(CustoAnalise custo) {
        this.custo = custo;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public Integer getSumFatosRelevantes() {
        return sumFatosRelevantes;
    }

    public void setSumFatosRelevantes(Integer sumFatosRelevantes) {
        this.sumFatosRelevantes = sumFatosRelevantes;
    }

    public RelevanciaMontantePagamento getBeneficio() {
        return beneficio;
    }

    public void setBeneficio(RelevanciaMontantePagamento beneficio) {
        this.beneficio = beneficio;
    }

    public void setPagamentoID(Integer pagamentoID) {
        this.pagamentoID = pagamentoID;
    }

    public void setMunicipioID(Integer municipioID) {
        this.municipioID = municipioID;
    }

    public void setNomeCredor(String nomeCredor) {
        this.nomeCredor = nomeCredor;
    }

    public void setEspecificacao(String especificacao) {
        this.especificacao = especificacao;
    }

    public void setNumeroEmpenho(Integer numeroEmpenho) {
        this.numeroEmpenho = numeroEmpenho;
    }

    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public void setRelevanciaMontantePagamento(RelevanciaMontantePagamento relevanciaMontantePagamento) {
        this.relevanciaMontantePagamento = relevanciaMontantePagamento;
    }

    private RelevanciaMontantePagamento relevanciaMontantePagamento;

    public Integer getPagamentoID() {
        return pagamentoID;
    }

    public Integer getMunicipioID() {
        return municipioID;
    }

    public String getNomeCredor() {
        return nomeCredor;
    }

    public String getEspecificacao() {
        return especificacao;
    }

    public Integer getNumeroEmpenho() {
        return numeroEmpenho;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public RelevanciaMontantePagamento getRelevanciaMontantePagamento() {
        return relevanciaMontantePagamento;
    }

    public Integer getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Integer dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getCredorID() {
        return credorID;
    }

    public void setCredorID(String credorID) {
        this.credorID = credorID;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void calcularRelevancia(HashMap<Integer, Municipio> municipios) {
        calcularRelevanciaValor();
        calcularRelevanciaDoMunicipio(municipios);
        setMunicipio(municipios.get(this.getMunicipioID()));
        calcularCustoAnalisePagamento();
    }

    private void calcularCustoAnalisePagamento() {
        if (getSumFatosRelevantes() > 160) {
            setCusto(CustoAnalise.Grande_Relevancia);
        }
        if (getSumFatosRelevantes() <= 160 && getSumFatosRelevantes() > 120) {
            setCusto(CustoAnalise.Relevante);
        }
        if (getSumFatosRelevantes() <= 120 && getSumFatosRelevantes() > 60) {
            setCusto(CustoAnalise.Pequena_Relevancia);
        }
        if (getSumFatosRelevantes() <= 60) {
            setCusto(CustoAnalise.Irrelevante);
        }
    }

    private void calcularRelevanciaDoMunicipio(HashMap<Integer, Municipio> municipios) {
        Municipio municipio = municipios.get(this.getMunicipioID());
        Integer fatosRelevantes = getSumFatosRelevantes();
        setSumFatosRelevantes(fatosRelevantes += municipio.getPorteEnumeration().valorPorteMunicipal);
    }

    private void calcularRelevanciaEspecificacaoPagamento() {

    }

    private void calcularRelevanciaValor() {
        if (this.getValor().compareTo(new BigDecimal("5000000.00")) == 1) {
            Integer fatosRelevantes = getSumFatosRelevantes();
            setSumFatosRelevantes(fatosRelevantes += 100);
        } else if (this.getValor().compareTo(new BigDecimal("1000000.00")) == 1
                && this.getValor().compareTo(new BigDecimal("5000000.00")) == -1) {
            Integer fatosRelevantes = getSumFatosRelevantes();
            setSumFatosRelevantes(fatosRelevantes += 80);
        } else if (this.getValor().compareTo(new BigDecimal("200000.00")) == 1 &&
                this.getValor().compareTo(new BigDecimal("1000000.00")) == -1) {
            Integer fatosRelevantes = getSumFatosRelevantes();
            setSumFatosRelevantes(fatosRelevantes += 60);
        } else if (this.getValor().compareTo(new BigDecimal("50000.00")) == 1 &&
                this.getValor().compareTo(new BigDecimal("200000.00")) == -1) {
            Integer fatosRelevantes = getSumFatosRelevantes();
            setSumFatosRelevantes(fatosRelevantes += 40);
        } else if (this.getValor().compareTo(new BigDecimal("50000.00")) == -1) {
            Integer fatosRelevantes = getSumFatosRelevantes();
            setSumFatosRelevantes(fatosRelevantes += 20);
        }
    }
}