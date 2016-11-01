package com.wehelp.wehelp.services;

import org.json.JSONObject;

/**
 * Created by Rael on 29/10/2016.
 */
public interface IServiceResponseCallback {
    void execute(JSONObject response);
}
