package com.wehelp.wehelp.services;

import android.util.Log;

import com.wehelp.wehelp.classes.ServiceContainer;

import org.json.JSONArray;
import org.json.JSONObject;

public class EventService {
    ServiceContainer serviceContainer;

    public EventService(ServiceContainer serviceContainer) {
        this.serviceContainer = serviceContainer;
    }

    /**
     *
     * @param lat
     * @param lng
     * @param perimetro (KM)
     * @param serviceArrayResponseCallback
     * @param serviceErrorCallback
     */
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


}
