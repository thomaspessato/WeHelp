package com.wehelp.wehelp;

import android.app.Application;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wehelp.wehelp.adapters.ParticipatingEventAdapter;
import com.wehelp.wehelp.adapters.TimelineEventAdapter;
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

    public ArrayAdapter<Event> eventArrayAdapter;
    public ArrayList<Event> eventList = new ArrayList<Event>();
    ListView listView;
    View footer; //lazy load
    private SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout loadingPanel;
    RelativeLayout noEventsPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participate_event);

        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
        noEventsPanel = (RelativeLayout) findViewById(R.id.no_events_panel);
        assert noEventsPanel != null;
        noEventsPanel.setVisibility(View.GONE);
        setTitle("Eventos que participo");

        ((WeHelpApp) getApplication()).getNetComponent().inject(this);

        listView = (ListView)findViewById(R.id.participating_listview);
        eventArrayAdapter = new ParticipatingEventAdapter(this,eventList);
        listView.setAdapter(eventArrayAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new ListParticipatingEventsTask().execute();
            }
        });


        loadingPanel.setVisibility(View.VISIBLE);
        new ListParticipatingEventsTask().execute();

    }

    private class ListParticipatingEventsTask extends AsyncTask<Void, Void, ArrayList<Event>> {
        @Override
        protected void onPreExecute() {
            eventArrayAdapter.clear();
            loadingPanel.setVisibility(View.VISIBLE);
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
                loadingPanel.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), eventController.errorMessages.toString(), Toast.LENGTH_LONG).show();
            } else {

                if(events.size() > 0) {
                    eventList.addAll(events);
                    eventArrayAdapter.notifyDataSetChanged();
                    noEventsPanel.setVisibility(View.GONE);
                } else {
                    noEventsPanel.setVisibility(View.VISIBLE);
                }
                swipeRefreshLayout.setRefreshing(false);
                loadingPanel.setVisibility(View.GONE);
            }
            // remover loader
        }
    }
}
