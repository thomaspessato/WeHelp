package com.wehelp.wehelp.services;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wehelp.wehelp.adapters.GsonUTCDateAdapter;
import com.wehelp.wehelp.classes.ServiceContainer;
import com.wehelp.wehelp.classes.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserService {

    ServiceContainer serviceContainer;
    Gson gson;

    public UserService(ServiceContainer serviceContainer, Gson gson) {
        this.serviceContainer = serviceContainer;
        this.gson = gson;
    }

    public void login(String email, String password, final IServiceResponseCallback serviceResponse, final IExecuteCallback executeCallback)  {

        Map<String, String>  params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        String url = "login";
        this.serviceContainer.PostRequest(url, params,
                new IServiceResponseCallback() {
                    @Override
                    public void execute(JSONObject response) {
                        try {
                            serviceContainer.SaveAccessToken(response.getString("access_token"), response.getString("refresh_token"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        serviceResponse.execute(response);
                    }
                },
                new IServiceErrorCallback() {
                    @Override
                    public void execute(VolleyError error) {
                        executeCallback.execute();
                    }
                }
        );
    }


    public void getUser(final IServiceResponseCallback serviceResponse, final IExecuteCallback executeCallback) {
        String url = "usuarios";
        this.serviceContainer.GetRequest(url,
                new IServiceResponseCallback() {
                    @Override
                    public void execute(JSONObject response) {
                        try {
                            Log.d("WeHelpWS", response.getString("email"));
                            serviceResponse.execute(response);
                        } catch (JSONException e) {
                            Log.d("Error", e.getStackTrace().toString());
                        }
                    }
                },
                new IServiceErrorCallback() {
                    @Override
                    public void execute(VolleyError error) {
                        executeCallback.execute();
                    }
                }
        );
    }

    public void createPerson(User user, final IServiceResponseCallback serviceResponse, final IServiceErrorCallback serviceErrorCallback)  {

        Map<String, String>  params = new HashMap<>();
        params.put("nome", user.getPessoa().getNome());
        params.put("foto", user.getPessoa().getFoto());
        params.put("ranking", String.valueOf(user.getPessoa().getRanking()));
        params.put("moderador", user.getPessoa().isModerador() ? "1" : "0");
        params.put("email", user.getEmail());
        params.put("password", user.getPassword());

        String url = "pessoas";
        this.serviceContainer.PostRequest(url, params,
                new IServiceResponseCallback() {
                    @Override
                    public void execute(JSONObject response) {
                        serviceResponse.execute(response);
                    }
                },
                new IServiceErrorCallback() {
                    @Override
                    public void execute(VolleyError error) {
                        serviceErrorCallback.execute(error);
                    }
                }
        );
    }

    public void createOng(User user, final IServiceResponseCallback serviceResponse, final IServiceErrorCallback serviceErrorCallback) throws JSONException {


        Map<String, String>  params = new HashMap<>();
        params.put("nome", user.getOng().getNome());
        params.put("cnpj", user.getOng().getCnpj());
        params.put("foto", user.getOng().getFoto());
        params.put("telefone", user.getOng().getTelefone());
        params.put("uf", user.getOng().getUf());
        params.put("cidade", user.getOng().getCidade());
        params.put("rua", user.getOng().getRua());
        params.put("numero", toString().valueOf(user.getOng().getNumero()));
        params.put("complemento", user.getOng().getComplemento());
        params.put("cep", user.getOng().getCep());
        params.put("ranking", String.valueOf(user.getOng().getRanking()));
        params.put("ativo", user.getOng().isAtivo() ? "1" : "0");
        params.put("lat", String.valueOf(user.getOng().getLat()));
        params.put("lng", String.valueOf(user.getOng().getLng()));
        params.put("email", user.getEmail());
        params.put("password", user.getPassword());

        String url = "ongs";

        JSONObject json = new JSONObject(params);

        this.serviceContainer.PostRequest(url, json,
                new IServiceResponseCallback() {
                    @Override
                    public void execute(JSONObject response) {
                        serviceResponse.execute(response);
                    }
                },
                new IServiceErrorCallback() {
                    @Override
                    public void execute(VolleyError error) {
                        serviceErrorCallback.execute(error);
                    }
                }
        );
    }



}
