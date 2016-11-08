package com.wehelp.wehelp.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return  this.application;
    }

    @Provides
    @Singleton
    Context providesContext() {
        return  this.application.getApplicationContext();
    }
}
