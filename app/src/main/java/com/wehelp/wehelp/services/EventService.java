package com.wehelp.wehelp.services;

import android.util.Log;

import com.google.gson.Gson;
import com.wehelp.wehelp.classes.Event;
import com.wehelp.wehelp.classes.EventRequirement;
import com.wehelp.wehelp.classes.ServiceContainer;
import com.wehelp.wehelp.classes.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

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


//    /api/eventos?participante_id=11

    public void getParticipatingEvents(int userId, final IServiceArrayResponseCallback serviceArrayResponseCallback, final IServiceErrorCallback serviceErrorCallback) {
        String url = "eventos?participante_id="+userId;
        this.serviceContainer.GetArrayRequest(url, new IServiceArrayResponseCallback() {
            @Override
            public void execute(JSONArray response) {
                Log.d("WeHelpWs", response.toString());
                serviceArrayResponseCallback.execute(response);
            }
        }, serviceErrorCallback);
    }

    public void getMyEvents(int userId, final IServiceArrayResponseCallback serviceArrayResponseCallback, final IServiceErrorCallback serviceErrorCallback) {
        String url = "eventos?usuario_id="+userId;
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
        json.put("nome", event.getNome());
        json.put("descricao", event.getDescricao());
        json.put("usuario_id", event.getUsuarioId());
        json.put("categoria_id", event.getCategoriaId());
        json.put("pais", event.getPais());
        json.put("uf", event.getUf());
        json.put("cidade", event.getCidade());
        json.put("rua", event.getRua());
        json.put("numero", event.getNumero());
        json.put("complemento", event.getComplemento());
        json.put("ranking", event.getRanking());
        json.put("status", event.getStatus());
        json.put("lat", event.getLat());
        json.put("lng", event.getLng());
        json.put("certificado", event.isCertificado() ? "1" : "0");
        json.put("data_inicio",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(event.getDataInicio()));

        if (event.getRequisitos().size() > 0) {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < event.getRequisitos().size(); i++) {
                JSONObject jsonReq = new JSONObject();
                jsonReq.put("descricao", event.getRequisitos().get(i).getDescricao());
                jsonReq.put("quant", String.valueOf(event.getRequisitos().get(i).getQuant()));
                jsonReq.put("un", event.getRequisitos().get(i).getUn());
                jsonArray.put(jsonReq);
            }
            json.put("requisitos", jsonArray);
        }

        this.serviceContainer.PostRequest(url, json, serviceResponseCallback, serviceErrorCallback);
    }

    public void addUser(Event event, User user, ArrayList<EventRequirement> listRequirements, final IServiceResponseCallback serviceResponseCallback, final IServiceErrorCallback serviceErrorCallback) throws JSONException {
        String url = "adicionar_participante";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("usuario_id", user.getId());
        jsonObject.put("evento_id", event.getId());

        if (listRequirements.size() > 0) {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < listRequirements.size(); i++) {
                JSONObject jsonReq = new JSONObject();
                jsonReq.put("usuario_id", user.getId());
                jsonReq.put("requisito_id", listRequirements.get(i).getId());
                jsonReq.put("quant", String.valueOf(listRequirements.get(i).getSelectedQuant()));
                jsonReq.put("un", listRequirements.get(i).getUn());
                jsonArray.put(jsonReq);
            }
            jsonObject.put("requisitos", jsonArray);
        }
        Log.d("TESTE", jsonObject.toString());
        this.serviceContainer.PostRequest(url, jsonObject, serviceResponseCallback, serviceErrorCallback);
    }

    public void removeUser(Event event, User user, final IServiceResponseCallback serviceResponseCallback, final IServiceErrorCallback serviceErrorCallback) throws JSONException {
        String url = "remover_participante";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("usuario_id", user.getId());
        jsonObject.put("evento_id", event.getId());
        this.serviceContainer.PostRequest(url, jsonObject, serviceResponseCallback, serviceErrorCallback);
    }

    public void addRequirement(Event event, EventRequirement requirement, final IServiceResponseCallback serviceResponseCallback, final IServiceErrorCallback serviceErrorCallback) throws JSONException {
        String url = "requisitos";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("descricao", requirement.getId());
        jsonObject.put("evento_id", event.getId());
        this.serviceContainer.PostRequest(url, jsonObject, serviceResponseCallback, serviceErrorCallback);
    }


    public void deleteEvent(Event event, final IServiceResponseCallback serviceResponseCallback, final IServiceErrorCallback serviceErrorCallback) throws JSONException {
        String url = "eventos/" + event.getId();
        this.serviceContainer.DeleteRequest(url, serviceResponseCallback, serviceErrorCallback);
    }
}
