package com.wehelp.wehelp;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wehelp.wehelp.adapters.EventParticipateAdapter;
import com.wehelp.wehelp.adapters.TimelineEventAdapter;
import com.wehelp.wehelp.classes.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ParticipateEventActivity extends AppCompatActivity {

    public ArrayAdapter<Event> eventArrayAdapter;
    public ArrayList<Event> eventList = new ArrayList<Event>();
    ListView listView;
    View footer; //lazy load
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participate_event);
        setTitle("Eventos que participo");

        listView = (ListView)findViewById(R.id.participate_listview);
        eventArrayAdapter = new EventParticipateAdapter(this,eventList);
        listView.setAdapter(eventArrayAdapter);
//        footer = View.inflate(getActivity(),R.layout.progress_bar,null); // to make lazy load

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.event_participate_swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                IMPLEMENT REFRESH ON TIMELINE
            }
        });

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

        for(int i = 0; i < 10; i++) {
            Event event = new Event();
            event.setAddressCity("Cidade");
            event.setAddressStreet("Rua Padre Hildebrando");
            event.setAddressNumber("585");
            event.setTitle("Evento que participo "+i);

            eventList.add(event);
        }

        eventArrayAdapter.notifyDataSetChanged();
    }
}
