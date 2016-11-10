package com.wehelp.wehelp.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wehelp.wehelp.adapters.GsonUTCDateAdapter;
import com.wehelp.wehelp.classes.ServiceContainer;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EventoService {

    ServiceContainer serviceContainer;

    public EventoService(ServiceContainer serviceContainer) {
        this.serviceContainer = serviceContainer;
    }



//    public void login(String email, String password, final IServiceResponseCallback serviceResponse, final IExecuteCallback executeCallback)  {
//        /*
//        String s = "{\"id\":11,\"email\":\"teste@teste.com\",\"pessoa_id\":8,\"ong_id\":null,\"created_at\":\"2016-10-01 17:32:16\",\"updated_at\":\"2016-10-01 17:32:16\",\"pessoa\":{\"id\":8,\"nome\":\"teste\",\"foto\":null,\"telefone\":null,\"ranking\":0,\"moderador\":0,\"sexo\":\"F\",\"data_nascimento\":\"2016-01-01\",\"created_at\":\"2016-10-01 17:32:16\",\"updated_at\":\"2016-10-01 17:42:14\"},\"ong\":null}";
//        JSONObject obj = null;
//        try {
//            obj = new JSONObject(s);
//            JsonToUsuario(obj);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        */
//
//
//        Map<String, String>  params = new HashMap<String, String>();
//        params.put("email", email);
//        params.put("password", password);
//
//        String url = "http://www.wehelp.tigrimigri.com/api/login";
//
//        this.serviceContainer.PostRequest(url, params,
//                new IServiceResponseCallback() {
//                    @Override
//                    public void execute(JSONObject response) {
//                        try {
//                            serviceContainer.SaveAccessToken(response.getString("access_token"), response.getString("refresh_token"));
//                            getUsuario(new IServiceResponseCallback() {
//                                @Override
//                                public void execute(JSONObject response) {
//                                    serviceResponse.execute(response);
//                                }
//                            }, new IExecuteCallback() {
//                                @Override
//                                public void execute() {
//                                    executeCallback.execute();
//                                }
//                            });
//                        } catch (JSONException e) {
//                            Log.d("Error", e.getStackTrace().toString());
//                        }
//                    }
//                },
//                new IServiceErrorCallback() {
//                    @Override
//                    public void execute(VolleyError error) {
//                        executeCallback.execute();
//                    }
//                }
//        );
//    }

    public void getEventsList(final IServiceResponseCallback serviceResponse, final IExecuteCallback executeCallback) {
        String url = "http://www.wehelp.tigrimigri.com/api/eventos";
        this.serviceContainer.GetRequest(url,
                new IServiceResponseCallback() {
                    @Override
                    public void execute(JSONObject response) {
                        System.out.println(response.toString());
                        serviceResponse.execute(response);
                    }
                },
                new IServiceErrorCallback() {
                    @Override
                    public void execute(VolleyError error) {}
                }
        );
    }

//    public void getUsuario(final IServiceResponseCallback serviceResponse, final IExecuteCallback executeCallback) {
//        String url = "http://www.wehelp.tigrimigri.com/api/usuarios";
//        this.serviceContainer.GetRequest(url,
//                new IServiceResponseCallback() {
//                    @Override
//                    public void execute(JSONObject response) {
//                        try {
//                            Log.d("WeHelpWS", response.getString("email"));
//                            JsonToUsuario(response);
//                            serviceResponse.execute(response);
//                        } catch (JSONException e) {
//                            Log.d("Error", e.getStackTrace().toString());
//                        }
//                    }
//                },
//                new IServiceErrorCallback() {
//                    @Override
//                    public void execute(VolleyError error) {}
//                }
//        );
//    }

//    public void JsonToUsuario(JSONObject jsonObject) {
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(Date.class, new GsonUTCDateAdapter())
//                .registerTypeAdapter(Boolean.class, booleanAsIntAdapter)
//                .registerTypeAdapter(boolean.class, booleanAsIntAdapter)
//                .create();
//        Usuario usuario = gson.fromJson(jsonObject.toString(), Usuario.class);
//        Log.d("WeHelpWS.Usuario", usuario.getEmail());
//    }

//    private static final TypeAdapter<Boolean> booleanAsIntAdapter = new TypeAdapter<Boolean>() {
//        @Override public void write(JsonWriter out, Boolean value) throws IOException {
//            if (value == null) {
//                out.nullValue();
//            } else {
//                out.value(value);
//            }
//        }
//        @Override public Boolean read(JsonReader in) throws IOException {
//            JsonToken peek = in.peek();
//            switch (peek) {
//                case BOOLEAN:
//                    return in.nextBoolean();
//                case NULL:
//                    in.nextNull();
//                    return null;
//                case NUMBER:
//                    return in.nextInt() != 0;
//                case STRING:
//                    return Boolean.parseBoolean(in.nextString());
//                default:
//                    throw new IllegalStateException("Expected BOOLEAN or NUMBER but was " + peek);
//            }
//        }
//    };

}
