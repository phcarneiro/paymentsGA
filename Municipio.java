package br.ufg.inf.auditOP;

public class Municipio {

    private Integer id;
    private StringBuilder nome;

    private Porte porteEnumeration;

    public Porte getPorteEnumeration() {
        return porteEnumeration;
    }

    public void setPorteEnumeration(Porte porteEnumeration) {
        this.porteEnumeration = porteEnumeration;
    }

    public StringBuilder getNome() {
        return nome;
    }

    public void setNome(StringBuilder nome) {
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Municipio(StringBuilder nome, Porte porteEnumeration, Integer id) {
        this.nome = nome;
        this.porteEnumeration = porteEnumeration;
        this.id = id;
    }
}
