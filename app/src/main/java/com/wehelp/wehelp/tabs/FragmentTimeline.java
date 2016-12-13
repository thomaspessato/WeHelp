package com.wehelp.wehelp.tabs;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wehelp.wehelp.CreateEventActivity;
import com.wehelp.wehelp.R;
import com.wehelp.wehelp.TabbedActivity;
import com.wehelp.wehelp.adapters.TimelineEventAdapter;
import com.wehelp.wehelp.classes.Event;
import com.wehelp.wehelp.classes.ServiceContainer;
import com.wehelp.wehelp.classes.WeHelpApp;
import com.wehelp.wehelp.controllers.EventController;
import com.wehelp.wehelp.services.IExecuteCallback;
import com.wehelp.wehelp.services.IServiceResponseCallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by temp on 9/15/16.
 */
public class FragmentTimeline extends Fragment {

    @Inject
    public EventController eventController;

    @Nullable

    public ArrayAdapter<Event> eventArrayAdapter;
    public ArrayList<Event> eventList = new ArrayList<Event>();

    ListView listView;
    View footer; //lazy load
    private SwipeRefreshLayout swipeRefreshLayout;
    ViewGroup rootView;
    RelativeLayout noEventsPanel;

    public static FragmentTimeline newInstance() {
        FragmentTimeline fragment = new FragmentTimeline ();
        Bundle args = new Bundle();
//        args.putInt("1", sectionNumber);
//        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            TabbedActivity tab = (TabbedActivity)getActivity();
            eventList = tab.listEvents != null ? tab.listEvents : new ArrayList<Event>();
            System.out.println("FRAGMENT TIMELINE / SIZE OF EVENTS: "+eventList.size());
            if(eventList.size() == 0) {
                noEventsPanel.setVisibility(View.VISIBLE);
            } else {
                noEventsPanel.setVisibility(View.GONE);
            }

            listView = (ListView)rootView.findViewById(R.id.timeline_listview);
            eventArrayAdapter = new TimelineEventAdapter(getContext(),eventList);
            listView.setAdapter(eventArrayAdapter);
            eventArrayAdapter.notifyDataSetChanged();
        } else {
            // Do your Work
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((WeHelpApp) getActivity().getApplication()).getNetComponent().inject(this);
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tab_timeline, container, false);
        TabbedActivity tab = (TabbedActivity)getActivity();
        eventList = tab.listEvents != null ? tab.listEvents : new ArrayList<Event>();

        noEventsPanel = (RelativeLayout)rootView.findViewById(R.id.no_events_panel);
        Button btnCreateEvent = (Button)rootView.findViewById(R.id.btn_create_event_none);
        assert noEventsPanel != null;
        noEventsPanel.setVisibility(View.GONE);

        listView = (ListView)rootView.findViewById(R.id.timeline_listview);
        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh_layout);
        eventArrayAdapter = new TimelineEventAdapter(getContext(),eventList);
        listView.setAdapter(eventArrayAdapter);

        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCreateEvent= new Intent(getContext(), CreateEventActivity.class);
                startActivity(intentCreateEvent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                eventArrayAdapter.clear();
                System.out.println("No events!");
                new ListEventsTask().execute();
            }
        });

                eventArrayAdapter.notifyDataSetChanged();
        return rootView;
    }

    private class ListEventsTask extends AsyncTask<Void, Void, ArrayList<Event>> {

        Location location;
        Criteria criteria;
        LocationManager locationManager;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected ArrayList<Event> doInBackground(Void... params) {
            try {
                locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                criteria = new Criteria();
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    location = new Location("");
                    location.setLatitude(-30.034647);
                    location.setLongitude(-51.217658);
                } else {
                    location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                }

                if (location == null) {
                    location = new Location("");
                    location.setLatitude(-30.034647);
                    location.setLongitude(-51.217658);
                }

                double userLatitude = location.getLatitude();
                double userLongitude = location.getLongitude();
                eventController.getEvents(userLatitude, userLongitude, 50);
                while (eventController.getListEvents() == null && !eventController.errorService){}
                if (eventController.errorService) {
                    return null;
                }
                ArrayList<Event> listEvents = eventController.getListEvents();
                eventController.setListEvents(null);
                return listEvents;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(ArrayList<Event> events) {
            if (events == null) {
                Toast.makeText(getActivity().getApplicationContext(), "Não foi possível atualizar os eventos. Tente novamente.", Toast.LENGTH_LONG).show();
            } else {
                eventList = events;
                TabbedActivity tab = (TabbedActivity)getActivity();
                tab.listEvents = eventList;
                eventArrayAdapter = new TimelineEventAdapter(getContext(),eventList);
                listView.setAdapter(eventArrayAdapter);
                eventArrayAdapter.notifyDataSetChanged();
            }
            swipeRefreshLayout.setRefreshing(false);
        }


    }
}
