package com.wehelp.wehelp.services;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

/**
 * Created by Rael on 29/10/2016.
 */
public interface IServiceErrorCallback {
    void execute(VolleyError error);
}
