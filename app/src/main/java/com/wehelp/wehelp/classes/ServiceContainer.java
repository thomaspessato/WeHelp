package com.wehelp.wehelp.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wehelp.wehelp.services.IServiceErrorCallback;
import com.wehelp.wehelp.services.IServiceResponseCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ServiceContainer {

    private RequestQueue requestQueue;
    private Context context;

    public static final String TAG = "WeHelpTag";
    private static ServiceContainer instance;

    private ServiceContainer(Context context) {
        this.context = context;
    }

    public static ServiceContainer getInstance(Context context)
    {
        if (ServiceContainer.instance == null) {
            ServiceContainer.instance = new ServiceContainer(context);
        }
        return  ServiceContainer.instance;
    }

    public Context getContext() {
        return this.context;
    }

    public RequestQueue getRequestQueue() {
        if (this.requestQueue == null) {
            this.requestQueue = Volley.newRequestQueue(this.context);
        }
        return this.requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        VolleyLog.d("Request added to queue: %s", request.getUrl());
        this.getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        VolleyLog.d("Request added to queue: %s", request.getUrl());
        this.getRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object tag) {
        if (this.requestQueue != null) {
            this.requestQueue.cancelAll(tag);
        }
    }

    public void PostRequest(String url, Map<String, String> params, final IServiceResponseCallback responseCallback, final IServiceErrorCallback errorCallback) {
        JsonObjectRequest postRequest = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("WeHelpWS", response.toString());
                        responseCallback.execute(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("WeHelpWs.Error", error.toString());
                        errorCallback.execute(error);
                    }
                }
        ) {

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }

        };
        this.addToRequestQueue(postRequest);
    }

    public void GetRequest(String url, final IServiceResponseCallback responseCallback, final IServiceErrorCallback errorCallback) {
        JsonObjectRequest getRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("WeHelpWS", response.toString());
                        responseCallback.execute(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("WeHelpWs.Error", error.toString());
                        errorCallback.execute(error);
                    }
                }
        ) {

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + GetAccessToken());
                return params;
            }

        };
        this.addToRequestQueue(getRequest);
    }

    public String GetAccessToken()
    {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("com.wehelp.wehelp", Context.MODE_PRIVATE);
        return sharedPreferences.getString("WEHELP_ACCESS_TOKEN", "");
    }

    public void SaveAccessToken(String accessToken, String refreshToken) {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("com.wehelp.wehelp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("WEHELP_ACCESS_TOKEN", accessToken);
        editor.putString("WEHELP_REFRESH_TOKEN", refreshToken);
        editor.commit();
    }
}
