package com.wehelp.wehelp.classes;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class Util {

    public static JSONObject ServiceErrorToJson(VolleyError error) {
        JSONObject json = new JSONObject();
        try {
            if (error.networkResponse != null) {
                NetworkResponse response = error.networkResponse;
                json.put("StatusCode", response.statusCode);
                json.put("Errors", new String(response.data));
            } else {
                json.put("Errors", error.getClass().toString());
            }

        } catch (JSONException e) {
            Log.d("WeHelpWs", "Erro ao converter VolleyError para JsonObject: " + e.getStackTrace().toString());
        }
        return json;
    }
}
