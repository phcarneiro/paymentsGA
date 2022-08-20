package br.ufg.inf.auditOP;

public enum RelevanciaMontantePagamento {
    Pouco_Relevante(20), Pequena_Relevancia(40), Relevante(60), Grande_Relevancia(80);

    public int valor;

    RelevanciaMontantePagamento(int i) {
        valor = i;
    }
}
