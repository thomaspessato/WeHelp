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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tab_timeline, container, false);



        listView = (ListView)rootView.findViewById(R.id.timeline_listview);
        eventArrayAdapter = new TimelineEventAdapter(getContext(),eventList);
        listView.setAdapter(eventArrayAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                IMPLEMENT REFRESH ON TIMELINE
            }
        });



//        JSONArray jsonArray = null;
//        try {
//            jsonArray = new JSONArray(data);
//            for(int i = 0; i<jsonArray.length(); i++){
//
//
//                Event event = new Event();
//                JSONObject dataEvent = (JSONObject) jsonArray.get(i);
//                //set image
////                event.setTitle(dataEvent.getString("descricao"));
//                event.setCidade(dataEvent.getString("cidade"));
//                event.setNumero(dataEvent.getString("numero"));
////                event.setAddressCity("Porto Alegre");
////                String date = "12-12-2016"; //how to pass date variable to method? (setEndDate)
////                event.setCategory("Categoria "+i);
////                event.setUsuario("Creator "+ i);
//
//                eventList.add(event);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

                eventArrayAdapter.notifyDataSetChanged();
        return rootView;
    }
}
