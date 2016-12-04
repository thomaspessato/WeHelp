package com.wehelp.wehelp.services;

import android.util.Log;

import com.google.gson.Gson;
import com.wehelp.wehelp.classes.ServiceContainer;

import org.json.JSONArray;

/**
 * Created by Rael on 04/12/2016.
 */

public class CategoryService {
    ServiceContainer serviceContainer;
    Gson gson;
    public CategoryService(ServiceContainer serviceContainer, Gson gson)
    {
        this.gson = gson;
        this.serviceContainer = serviceContainer;
    }

    public void getCategories(final IServiceArrayResponseCallback serviceArrayResponseCallback, final IServiceErrorCallback serviceErrorCallback) {
        String url = "categorias";
        this.serviceContainer.GetArrayRequest(url, new IServiceArrayResponseCallback() {
            @Override
            public void execute(JSONArray response) {
                Log.d("WeHelpWs", response.toString());
                serviceArrayResponseCallback.execute(response);
            }
        }, serviceErrorCallback);
    }


}
