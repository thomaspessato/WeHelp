package com.wehelp.wehelp.tabs;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

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

    @Inject
    public EventController eventController;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            TabbedActivity tab = (TabbedActivity)getActivity();
            eventList = tab.listEvents != null ? tab.listEvents : new ArrayList<Event>();

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
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tab_timeline, container, false);
        eventController = new EventController();
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
//                System.out.println("No events!");
//                eventController.getEvents(-30.0381669,-51.214949,50);
//                while (eventController.getListEvents() == null && !eventController.errorService){}
//                if (eventController.errorService) {
//                    System.out.println("No events!");
//                }
//                ArrayList<Event> listEvents = eventController.getListEvents();
//                eventController.setListEvents(null);
//                System.out.println("EVENTS! "+listEvents);
//                swipeRefreshLayout.setRefreshing(false);
//
//                eventArrayAdapter.notifyDataSetChanged();

            }
        });

                eventArrayAdapter.notifyDataSetChanged();
        return rootView;
    }
}
