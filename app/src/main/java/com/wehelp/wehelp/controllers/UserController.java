package com.wehelp.wehelp.controllers;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wehelp.wehelp.adapters.GsonUTCDateAdapter;
import com.wehelp.wehelp.classes.ServiceContainer;
import com.wehelp.wehelp.classes.User;
import com.wehelp.wehelp.classes.WeHelpApp;
import com.wehelp.wehelp.services.IExecuteCallback;
import com.wehelp.wehelp.services.IServiceResponseCallback;
import com.wehelp.wehelp.services.UserService;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;

public class UserController {

    @Inject
    public Gson gson;

    @Inject
    public UserService userService;

    public WeHelpApp weHelpApp;


    public UserController(UserService userService, Application application) {
        this.userService = userService;
        this.weHelpApp = (WeHelpApp) application;
        this.weHelpApp.getNetComponent().inject(this);
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
        this.userService.getUser(new IServiceResponseCallback() {
            @Override
            public void execute(JSONObject response) {
                weHelpApp.setUser(JsonToUser(response));
                serviceResponseCallback.execute(response);
            }
        }, new IExecuteCallback() {
            @Override
            public void execute() {
                Log.d("WeHelpWS", "Erro ao buscar dados do usuário");
                executeErrorCallback.execute();
            }
        });
    }


    public User JsonToUser(JSONObject jsonObject) {
        User user = gson.fromJson(jsonObject.toString(), User.class);
        Log.d("WeHelpWS.User", user.getEmail());
        return user;
    }


}
