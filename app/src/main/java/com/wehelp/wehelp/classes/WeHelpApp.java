package com.wehelp.wehelp.classes;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.wehelp.wehelp.di.AppModule;
import com.wehelp.wehelp.di.DaggerNetComponent;
import com.wehelp.wehelp.di.NetComponent;
import com.wehelp.wehelp.di.NetModule;


public class WeHelpApp extends MultiDexApplication {
    private NetComponent netComponent;
    private static WeHelpApp instance;
    private User user;

    @Override
    public void onCreate() {
        super.onCreate();

        // Dagger%COMPONENT_NAME%
        netComponent = DaggerNetComponent.builder()
                // list of modules that are part of this component need to be created here too
                .appModule(new AppModule(this)) // This also corresponds to the name of your module: %component_name%Module
                .netModule(new NetModule())
                .build();

    }

    public WeHelpApp getInstance() {
        return instance;
    }

    public NetComponent getNetComponent() {
        return netComponent;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
