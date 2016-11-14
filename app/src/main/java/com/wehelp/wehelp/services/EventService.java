package com.wehelp.wehelp.services;

import android.util.Log;

import com.google.gson.Gson;
import com.wehelp.wehelp.classes.Event;
import com.wehelp.wehelp.classes.ServiceContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class EventService {
    ServiceContainer serviceContainer;
    Gson gson;

    public EventService(ServiceContainer serviceContainer, Gson gson) {
        this.serviceContainer = serviceContainer;
        this.gson = gson;
    }

    public void getEvents(double lat, double lng, int perimetro, final IServiceArrayResponseCallback serviceArrayResponseCallback, final IServiceErrorCallback serviceErrorCallback) {
        String url = "eventos_por_perimetro?lat=" + lat + "&lng=" + lng + "&perimetro=" + perimetro;
        this.serviceContainer.GetArrayRequest(url, new IServiceArrayResponseCallback() {
            @Override
            public void execute(JSONArray response) {
                Log.d("WeHelpWs", response.toString());
                serviceArrayResponseCallback.execute(response);
            }
        }, serviceErrorCallback);
    }

    public void createEvent(Event event, final IServiceResponseCallback serviceResponseCallback, final IServiceErrorCallback serviceErrorCallback) throws JSONException {
        String url = "eventos";
        JSONObject json = new JSONObject();
        json.put("descricao", event.getDescricao());
        json.put("usuario_id", event.getUsuarioId());
        json.put("categoria_id", event.getCategoriaId());
        json.put("pais", event.getPais());
        json.put("uf", event.getUf());
        json.put("cidade", event.getCidade());
        json.put("rua", event.getRua());
        json.put("numero", event.getNumero());
        json.put("complemento", event.getComplemento());
        json.put("cep", event.getCep());
        json.put("bairro", event.getBairro());
        json.put("ranking", event.getRanking());
        json.put("status", event.getStatus());
        json.put("lat", event.getLat());
        json.put("lng", event.getLng());
        json.put("certificado", event.isCertificado() ? "1" : "0");
        json.put("data_inicio",  new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(event.getDataInicio()));
        json.put("data_fim", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(event.getDataFim()));

        JSONObject jsonReq = new JSONObject();
        jsonReq.put("descricao", "1 violão");
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonReq);

        json.put("requisitos", jsonArray);

        this.serviceContainer.PostRequest(url, json, serviceResponseCallback, serviceErrorCallback);
    }


}
