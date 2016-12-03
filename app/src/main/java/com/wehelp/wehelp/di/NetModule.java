package com.wehelp.wehelp.di;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.android.volley.Cache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wehelp.wehelp.adapters.GsonUTCDateAdapter;
import com.wehelp.wehelp.classes.Event;
import com.wehelp.wehelp.classes.ServiceContainer;
import com.wehelp.wehelp.controllers.EventController;
import com.wehelp.wehelp.controllers.UserController;
import com.wehelp.wehelp.services.EventService;
import com.wehelp.wehelp.services.UserService;

import java.io.IOException;
import java.util.Date;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NetModule {

    public NetModule() {}

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new GsonUTCDateAdapter())
                .registerTypeAdapter(Boolean.class, booleanAsIntAdapter)
                .registerTypeAdapter(boolean.class, booleanAsIntAdapter)
                .create();
        return gson;
    }

    @Provides
    @Singleton
    ServiceContainer provideServiceContainer(Context context, SharedPreferences sharedPreferences) {
        ServiceContainer serviceContainer = new ServiceContainer(context, sharedPreferences);
        return serviceContainer;
    }

    @Provides
    UserService provideUserService(ServiceContainer serviceContainer, Gson gson) {
        UserService userService = new UserService(serviceContainer, gson);
        return userService;
    }

    @Provides
    EventService provideEventService(ServiceContainer serviceContainer, Gson gson) {
        EventService eventService = new EventService(serviceContainer, gson);
        return eventService;
    }


    @Provides
    UserController provideUserController(UserService userService, Gson gson, Application application) {
        UserController userController = new UserController(userService, gson, application);
        return userController;
    }

    @Provides
    EventController provideEventController(EventService eventService, Gson gson, Application application) {
        EventController eventController = new EventController(eventService, gson, application);
        return eventController;
    }





    private static final TypeAdapter<Boolean> booleanAsIntAdapter = new TypeAdapter<Boolean>() {
        @Override public void write(JsonWriter out, Boolean value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value);
            }
        }
        @Override public Boolean read(JsonReader in) throws IOException {
            JsonToken peek = in.peek();
            switch (peek) {
                case BOOLEAN:
                    return in.nextBoolean();
                case NULL:
                    in.nextNull();
                    return null;
                case NUMBER:
                    return in.nextInt() != 0;
                case STRING:
                    return Boolean.parseBoolean(in.nextString());
                default:
                    throw new IllegalStateException("Expected BOOLEAN or NUMBER but was " + peek);
            }
        }
    };
}
