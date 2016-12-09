package com.wehelp.wehelp.controllers;

import android.app.Application;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.wehelp.wehelp.classes.Event;
import com.wehelp.wehelp.classes.EventRequirement;
import com.wehelp.wehelp.classes.User;
import com.wehelp.wehelp.classes.Util;
import com.wehelp.wehelp.classes.WeHelpApp;
import com.wehelp.wehelp.services.EventService;
import com.wehelp.wehelp.services.IServiceArrayResponseCallback;
import com.wehelp.wehelp.services.IServiceErrorCallback;
import com.wehelp.wehelp.services.IServiceResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class EventController {

    EventService eventService;
    public Gson gson;
    private ArrayList<Event> listEvents;
    public WeHelpApp weHelpApp;

    public Event eventTemp = null;
    public boolean errorService = false;
    public JSONObject errorMessages = null;
    public boolean addUserOk = false;
    public boolean removeUserOk = false;

    public EventController() {

    }

    public ArrayList<Event> getListEvents() {
        return this.listEvents;
    }

    public void setListEvents(ArrayList<Event> listEvents) {
        this.listEvents = listEvents;
    }

    public EventController(EventService eventService, Gson gson, Application application) {
        this.eventService = eventService;
        this.gson = gson;
        this.weHelpApp = (WeHelpApp)application;
    }

    public void getEvents(double lat, double lng, int perimetro) {
        this.errorService = false;
        this.setListEvents(null);
        this.errorMessages = null;

        this.eventService.getEvents(lat, lng, perimetro, new IServiceArrayResponseCallback() {
            @Override
            public void execute(JSONArray response) {
                System.out.println("get events response: "+response);
                setListEvents(JsonArrayToEventList(response));
            }
        }, new IServiceErrorCallback() {
            @Override
            public void execute(VolleyError error) {
                setListEvents(new ArrayList<Event>());
                errorService = true;
                errorMessages = Util.ServiceErrorToJson(error);
            }
        });
    }

    public void getParticipatingEvents(int userId) {
        this.errorService = false;
        this.setListEvents(null);
        this.errorMessages = null;

        this.eventService.getParticipatingEvents(userId, new IServiceArrayResponseCallback() {
            @Override
            public void execute(JSONArray response) {
                setListEvents(JsonArrayToEventList(response));
            }
        }, new IServiceErrorCallback() {
            @Override
            public void execute(VolleyError error) {
                setListEvents(new ArrayList<Event>());
                errorService = true;
                errorMessages = Util.ServiceErrorToJson(error);
            }
        });
    }

    public void getMyEvents(int userId) {
        this.errorService = false;
        this.setListEvents(null);
        this.errorMessages = null;

        this.eventService.getMyEvents(userId, new IServiceArrayResponseCallback() {
            @Override
            public void execute(JSONArray response) {
                setListEvents(JsonArrayToEventList(response));
            }
        }, new IServiceErrorCallback() {
            @Override
            public void execute(VolleyError error) {
                setListEvents(new ArrayList<Event>());
                errorService = true;
                errorMessages = Util.ServiceErrorToJson(error);
            }
        });
    }

    public ArrayList<Event> JsonArrayToEventList(JSONArray jsonArray) {
        ArrayList<Event> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++)
        {
            try {
                Event event = gson.fromJson(jsonArray.get(i).toString(), Event.class);
                list.add(event);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public Event JsonToEvent(JSONObject jsonObject) {
        return gson.fromJson(jsonObject.toString(), Event.class);
    }

    public void createEvent(Event event) throws JSONException {
        this.eventTemp = null;
        this.errorService = false;
        this.errorMessages = null;

        this.eventService.createEvent(event, new IServiceResponseCallback() {
            @Override
            public void execute(JSONObject response) {
                Log.d("WeHelpWs", response.toString());
                eventTemp = JsonToEvent(response);
            }
        }, new IServiceErrorCallback() {
            @Override
            public void execute(VolleyError error) {
                Log.d("WeHelpWS", "Error: " + error.getMessage());
                errorService = true;
                errorMessages = Util.ServiceErrorToJson(error);
            }
        });
    }

    public void addUser(final Event event, final User user) throws JSONException {
        this.errorService = false;
        this.errorMessages = null;
        this.addUserOk = false;

        this.eventService.addUser(event, user, new IServiceResponseCallback() {
            @Override
            public void execute(JSONObject response) {
                Log.d("WeHelpWs", "Usuário " + user.getId() + " participando do evento " + event.getId());
                addUserOk = true;
            }
        }, new IServiceErrorCallback() {
            @Override
            public void execute(VolleyError error) {
                Log.d("WeHelpWS", "Error: " + error.getMessage());
                errorService = true;
                errorMessages = Util.ServiceErrorToJson(error);
            }
        });
    }

    public void removeUser(final Event event, final User user) throws JSONException {
        this.errorService = false;
        this.errorMessages = null;
        this.eventService.removeUser(event, user, new IServiceResponseCallback() {
            @Override
            public void execute(JSONObject response) {
                Log.d("WeHelpWs", "Usuário " + user.getId() + " removido do evento " + event.getId());
                removeUserOk = true;
            }
        }, new IServiceErrorCallback() {
            @Override
            public void execute(VolleyError error) {
                Log.d("WeHelpWS", "Error: " + error.getMessage());
                errorService = true;
                errorMessages = Util.ServiceErrorToJson(error);
            }
        });
    }

    public void addRequirement(final Event event, final EventRequirement requirement) throws JSONException {
        this.errorService = false;
        this.errorMessages = null;
        this.eventService.addRequirement(event, requirement, new IServiceResponseCallback() {
            @Override
            public void execute(JSONObject response) {
                Log.d("WeHelpWs", "Requisito " + requirement.getDescricao() + " adicionado ao evento " + event.getId());
                event.getRequisitos().add(gson.fromJson(response.toString(), EventRequirement.class));
            }
        }, new IServiceErrorCallback() {
            @Override
            public void execute(VolleyError error) {
                Log.d("WeHelpWS", "Error: " + error.getMessage());
                errorService = true;
                errorMessages = Util.ServiceErrorToJson(error);
            }
        });
    }
}
