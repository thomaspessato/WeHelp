package com.wehelp.wehelp.controllers;

import android.app.Application;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.wehelp.wehelp.classes.Category;
import com.wehelp.wehelp.classes.Event;
import com.wehelp.wehelp.classes.Util;
import com.wehelp.wehelp.classes.WeHelpApp;
import com.wehelp.wehelp.services.CategoryService;
import com.wehelp.wehelp.services.EventService;
import com.wehelp.wehelp.services.IServiceArrayResponseCallback;
import com.wehelp.wehelp.services.IServiceErrorCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rael on 04/12/2016.
 */

public class CategoryController {

    CategoryService categoryService;
    public Gson gson;
    private ArrayList<Category> listCategories;
    public WeHelpApp weHelpApp;

    public boolean errorService = false;
    public JSONObject errorMessages = null;

    public ArrayList<Category> getListCategories() {
        return this.listCategories;
    }

    public void setListCategories(ArrayList<Category> listCategories) {
        this.listCategories = listCategories;
    }

    public CategoryController(CategoryService categoryService, Gson gson, Application application) {
        this.categoryService = categoryService;
        this.gson = gson;
        this.weHelpApp = (WeHelpApp)application;
    }

    public void getCatgeories() {
        this.errorService = false;
        this.setListCategories(null);
        this.errorMessages = null;

        this.categoryService.getCategories(new IServiceArrayResponseCallback() {
            @Override
            public void execute(JSONArray response) {
                setListCategories(JsonArrayToCategoryList(response));
            }
        }, new IServiceErrorCallback() {
            @Override
            public void execute(VolleyError error) {
                setListCategories(new ArrayList<Category>());
                errorService = true;
                errorMessages = Util.ServiceErrorToJson(error);
            }
        });
    }

    public ArrayList<Category> JsonArrayToCategoryList(JSONArray jsonArray) {
        ArrayList<Category> list = new ArrayList<Category>();
        for (int i = 0; i < jsonArray.length(); i++)
        {
            try {
                Category cat = gson.fromJson(jsonArray.get(i).toString(), Category.class);
                list.add(cat);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
