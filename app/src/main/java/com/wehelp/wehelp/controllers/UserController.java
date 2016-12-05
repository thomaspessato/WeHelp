package com.wehelp.wehelp.controllers;

import android.app.Application;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.wehelp.wehelp.classes.User;
import com.wehelp.wehelp.classes.Util;
import com.wehelp.wehelp.classes.WeHelpApp;
import com.wehelp.wehelp.services.IExecuteCallback;
import com.wehelp.wehelp.services.IServiceErrorCallback;
import com.wehelp.wehelp.services.IServiceResponseCallback;
import com.wehelp.wehelp.services.UserService;

import org.json.JSONObject;

import javax.inject.Inject;

public class UserController {

    public Gson gson;
    public UserService userService;
    public WeHelpApp weHelpApp;

    public User userTemp = null;
    public boolean errorService = false;
    public JSONObject errorMessages = null;

    public UserController(UserService userService, Gson gson, Application application) {
        this.userService = userService;
        this.gson = gson;
        this.weHelpApp = (WeHelpApp)application;
    }

    public void login(String email, String password, final IServiceResponseCallback serviceResponseCallback, final IExecuteCallback executeCallback) {
        this.userService.login(email, password,
                new IServiceResponseCallback() {
                    @Override
                    public void execute(JSONObject response) {
                        getUser(serviceResponseCallback, executeCallback);
                    }
                },
                new IExecuteCallback() {
                    @Override
                    public void execute() {
                        executeCallback.execute();
                    }
                });
    }


    public void getUser(final IServiceResponseCallback serviceResponseCallback, final IExecuteCallback executeErrorCallback) {
        userTemp = null;
        errorMessages = null;
        errorService = false;
        this.userService.getUser(new IServiceResponseCallback() {
            @Override
            public void execute(JSONObject response) {
                weHelpApp.setUser(JsonToUser(response));
                userTemp = weHelpApp.getUser();
                serviceResponseCallback.execute(response);
            }
        }, new IExecuteCallback() {
            @Override
            public void execute() {
                Log.d("WeHelpWS", "Erro ao buscar dados do usuário");

                errorService = true;
                executeErrorCallback.execute();
            }
        });
    }

    public void createPerson(User user) throws Exception {

        this.userTemp = null;
        this.errorService = false;
        this.errorMessages = null;

        if (user.getPessoa() == null) {
            throw new Exception("Objeto pessoa é NULL");
        }

        this.userService.createPerson(user, new IServiceResponseCallback() {
            @Override
            public void execute(JSONObject response) {
                Log.d("WeHelpWs", response.toString());
                userTemp = JsonToUser(response);
            }
        }, new IServiceErrorCallback() {
            @Override
            public void execute(VolleyError error) {
                Log.d("WeHelpWS", "Error: " + error.getMessage());
                errorService = true;
                errorMessages = Util.ServiceErrorToJson(error);
            }
        });
    }

    public void createOng(User user) throws Exception {

        this.userTemp = null;
        this.errorService = false;
        this.errorMessages = null;

        if (user.getOng() == null) {
            throw new Exception("Objeto ONG é NULL");
        }

        this.userService.createOng(user, new IServiceResponseCallback() {
            @Override
            public void execute(JSONObject response) {
                Log.d("WeHelpWs", response.toString());
                userTemp = JsonToUser(response);
            }
        }, new IServiceErrorCallback() {
            @Override
            public void execute(VolleyError error) {
                Log.d("WeHelpWS", "Error: " + error.getMessage());
                errorService = true;
                errorMessages = Util.ServiceErrorToJson(error);
            }
        });
    }

    public User JsonToUser(JSONObject jsonObject) {
        User user = gson.fromJson(jsonObject.toString(), User.class);
        Log.d("WeHelpWS.User", user.getEmail());
        return user;
    }


}
