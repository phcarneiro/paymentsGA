package br.ufg.inf.auditOP;

public enum CustoAnalise {
    Irrelevante(1), Pequena_Relevancia(3), Relevante(6), Grande_Relevancia(12);

    int horas;
    CustoAnalise(int i) {
        horas = i;
    }
}
