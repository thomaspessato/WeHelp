package com.wehelp.wehelp.services;

import android.util.Log;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserService {

    ServiceContainer serviceContainer;

    public UserService(ServiceContainer serviceContainer) {
        this.serviceContainer = serviceContainer;
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
                    public void execute(VolleyError error) {}
                }
        );
    }


}
