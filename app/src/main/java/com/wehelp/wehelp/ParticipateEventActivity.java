package com.wehelp.wehelp;

import android.app.Application;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.wehelp.wehelp.classes.Event;
import com.wehelp.wehelp.classes.WeHelpApp;
import com.wehelp.wehelp.controllers.EventController;

import java.util.ArrayList;

import javax.inject.Inject;

public class ParticipateEventActivity extends AppCompatActivity {

    @Inject
    EventController eventController;
    int userId;

    @Inject
    Application application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participate_event);
        setTitle("Eventos que participo");

        ((WeHelpApp) getApplication()).getNetComponent().inject(this);

        System.out.println("USER ID: "+((WeHelpApp)this.application).getUser().getId());
        new ListParticipatingEventsTask().execute();
    }

    private class ListParticipatingEventsTask extends AsyncTask<Void, Void, ArrayList<Event>> {
        @Override
        protected void onPreExecute() {
            // carregar loader
        }

        @Override
        protected ArrayList<Event> doInBackground(Void... params) {

                eventController.getParticipatingEvents(((WeHelpApp)application).getUser().getId());
                while (eventController.getListEvents() == null && !eventController.errorService){}
                if (eventController.errorService) {
                    return null;
                }
                ArrayList<Event> listEvents = eventController.getListEvents();
                eventController.setListEvents(null);
                System.out.println("listEvents: "+listEvents);
                return listEvents;
        }

        protected void onPostExecute(ArrayList<Event> events) {
            if (events == null) {
                Toast.makeText(getApplicationContext(), eventController.errorMessages.toString(), Toast.LENGTH_LONG).show();
            } else {
//                listEvents = events;
                //Toast.makeText(getActivity().getApplicationContext(), "Retorno: OK", Toast.LENGTH_LONG).show();
                // Adiciona lista de eventos a Activity principal

            }
            // remover loader
        }
    }
}
