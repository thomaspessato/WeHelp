package com.wehelp.wehelp.tabs;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wehelp.wehelp.R;
import com.wehelp.wehelp.adapters.TimelineEventAdapter;
import com.wehelp.wehelp.classes.Event;

import java.sql.Date;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

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

    public static FragmentTimeline newInstance() {
        FragmentTimeline fragment = new FragmentTimeline ();
        Bundle args = new Bundle();
//        args.putInt("1", sectionNumber);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tab_timeline, container, false);


        listView = (ListView)rootView.findViewById(R.id.timeline_listview);

        eventArrayAdapter = new TimelineEventAdapter(getContext(),eventList);
        listView.setAdapter(eventArrayAdapter);
//        footer = View.inflate(getActivity(),R.layout.progress_bar,null); // to make lazy load

        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                IMPLEMENT REFRESH ON TIMELINE
            }
        });

        for(int i = 0; i<10; i++){
//            JSONObject jb = (JSONObject) array.get(i);

            Event event = new Event();

            //set image
//            event.setTitle("Novo evento numero "+i);
//            event.setAddressCity("Porto Alegre");
            String date = "12-12-2016"; //how to pass date variable to method? (setEndDate)
//            event.setCategory("Categoria "+i);
//            event.setCreator("Creator "+ i);

            eventList.add(event);

        }

        eventArrayAdapter.notifyDataSetChanged();

        return rootView;
    }
}
