package br.ufg.inf.auditOP;

public enum Porte {
    Muito_Pequeno(20), Pequeno(40), Médio(60), Grande(80);

    public int valorPorteMunicipal;

    Porte(int i) {
        valorPorteMunicipal = i;
    }
}
