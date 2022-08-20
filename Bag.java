package br.ufg.inf.auditOP;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Bag {

    private HashMap<Integer, Municipio> municipios;

    private HashMap<Integer, FatoPagamento> pagamentos;
    
    private ArrayList<FatoPagamento> listaPagamentos = new ArrayList<>();

    public static final Integer LIMITE_CUSTO = 120;

    public ArrayList<FatoPagamento> getListaPagamentos() {
        return listaPagamentos;
    }

    public HashMap<Integer, Municipio> getMunicipios() {
        return municipios;
    }

    public void setMunicipios(HashMap<Integer, Municipio> municipios) {
        this.municipios = municipios;
    }

    public HashMap<Integer, FatoPagamento> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(HashMap<Integer, FatoPagamento> pagamentos) {
        this.pagamentos = pagamentos;
    }

    public Bag() throws IOException {
        extractJSONData();
        extractJSONDataOP();
        convertMapToArrayList();
        System.out.println("OI");
    }

    private void convertMapToArrayList() {
        Iterator<FatoPagamento> iterator = getPagamentos().values().iterator();
        for (Iterator<FatoPagamento> it = iterator; it.hasNext(); ) {
            FatoPagamento fato = it.next();
            getListaPagamentos().add(fato);
        }
    }


    private void extractJSONDataOP() throws IOException {
        File initialFile = new File("src/main/resources/data/audit/FatoPagamento_202208132051.json");
        HashMap<Integer, FatoPagamento> lista = new HashMap<>();
        if (initialFile.exists()) {
            InputStream is = new FileInputStream("src/main/resources/data/audit/FatoPagamento_202208132051.json");
            String jsonTxt = IOUtils.toString(is, StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(jsonTxt);
            JSONArray pagamentos = (JSONArray) json.get("Pagamentos");

            for (Object pagamento : pagamentos
            ) {
                Integer opID = (Integer) ((JSONObject) pagamento).get("pagamento_id");
                Integer dataPagamento = (Integer) ((JSONObject) pagamento).get("data_pagamento_id");
                Integer municipioID = (Integer) ((JSONObject) pagamento).get("municipio_id");
                String credor = (String) ((JSONObject) pagamento).get("credor_id");
                String nomeCredor = (String) ((JSONObject) pagamento).get("nome_credor");
                BigDecimal valor = (BigDecimal) ((JSONObject) pagamento).get("valor_pagamento");
                String especificacao = (String) ((JSONObject) pagamento).get("especificacao");
                Integer numeroEmpenho = (Integer) ((JSONObject) pagamento).get("numero_empenho");
                String tipoPagamento = (String) ((JSONObject) pagamento).get("tipo_pagamento");

                FatoPagamento op = new FatoPagamento(opID, dataPagamento, municipioID, credor,
                        nomeCredor, valor, especificacao, numeroEmpenho, tipoPagamento);
                op.calcularRelevancia(getMunicipios());
                lista.put(op.getPagamentoID(), op);
            }
        }
        setPagamentos(lista);
    }

    public void extractJSONData() throws IOException {
        File initialFile = new File("src/main/resources/data/audit/DimMunicipio_202208151148.json");
        HashMap<Integer, Municipio> lista = new HashMap<>();
        if (initialFile.exists()) {
            InputStream is = new FileInputStream("src/main/resources/data/audit/DimMunicipio_202208151148.json");
            String jsonTxt = IOUtils.toString(is, StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(jsonTxt);
            JSONArray municipios = (JSONArray) json.get("Municipios");
            for (Object municipio : municipios
            ) {
                String porte = ((JSONObject) municipio).get("porte").equals("Muito Pequeno")
                        ? "Muito_Pequeno"
                        : (String) ((JSONObject) municipio).get("porte");

                Municipio municipioObject = new Municipio(new StringBuilder((String)
                        ((JSONObject) municipio).get("municipio")),
                        Porte.valueOf(porte), (Integer) ((JSONObject) municipio).get("id"));
                lista.put(municipioObject.getId(), municipioObject);
            }
        }
        setMunicipios(lista);
    }
}