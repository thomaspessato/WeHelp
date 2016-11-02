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
import com.wehelp.wehelp.services.EventoService;
import com.wehelp.wehelp.services.IExecuteCallback;
import com.wehelp.wehelp.services.IServiceResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        EventoService eventoService = new EventoService(ServiceContainer.getInstance(getContext()));

        String data = "[\n" +
                "  {\n" +
                "    \"id\": 7,\n" +
                "    \"categoria_id\": 1,\n" +
                "    \"usuario_id\": 11,\n" +
                "    \"pais\": \"Brasil\",\n" +
                "    \"uf\": \"RS\",\n" +
                "    \"cidade\": \"Porto Alegre\",\n" +
                "    \"rua\": \"Marechal Jóse Inácio da Silva\",\n" +
                "    \"numero\": 355,\n" +
                "    \"complemento\": \"\",\n" +
                "    \"cep\": \"91478452\",\n" +
                "    \"bairro\": \"Passo d'Areia\",\n" +
                "    \"lat\": -30.034647,\n" +
                "    \"lng\": -51.217658,\n" +
                "    \"descricao\": \"Primeiro evento\",\n" +
                "    \"data_inicio\": \"2016-10-16 12:00:00\",\n" +
                "    \"data_fim\": \"2016-10-16 14:00:00\",\n" +
                "    \"ranking\": 0,\n" +
                "    \"status\": \"A\",\n" +
                "    \"certificado\": 1,\n" +
                "    \"created_at\": \"2016-10-16 22:54:52\",\n" +
                "    \"updated_at\": \"2016-10-16 22:54:52\",\n" +
                "    \"usuario\": {\n" +
                "      \"id\": 11,\n" +
                "      \"email\": \"teste@teste.com\",\n" +
                "      \"pessoa_id\": 8,\n" +
                "      \"ong_id\": null,\n" +
                "      \"created_at\": \"2016-10-01 17:32:16\",\n" +
                "      \"updated_at\": \"2016-10-01 17:32:16\"\n" +
                "    },\n" +
                "    \"categoria\": {\n" +
                "      \"id\": 1,\n" +
                "      \"descricao\": \"comida\",\n" +
                "      \"created_at\": \"2016-10-09 17:40:39\",\n" +
                "      \"updated_at\": \"2016-10-09 17:40:39\"\n" +
                "    },\n" +
                "    \"requisitos\": [\n" +
                "      {\n" +
                "        \"id\": 7,\n" +
                "        \"evento_id\": 7,\n" +
                "        \"descricao\": \"teste requisito 2\",\n" +
                "        \"created_at\": \"2016-10-17 22:00:31\",\n" +
                "        \"updated_at\": \"2016-10-17 22:01:44\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 8,\n" +
                "    \"categoria_id\": 2,\n" +
                "    \"usuario_id\": 11,\n" +
                "    \"pais\": \"Brasil\",\n" +
                "    \"uf\": \"RS\",\n" +
                "    \"cidade\": \"Porto Alegre\",\n" +
                "    \"rua\": \"Marechal Jóse Inácio da Silva\",\n" +
                "    \"numero\": 355,\n" +
                "    \"complemento\": \"\",\n" +
                "    \"cep\": \"91478452\",\n" +
                "    \"bairro\": \"Passo d'Areia\",\n" +
                "    \"lat\": -30.034647,\n" +
                "    \"lng\": -51.217658,\n" +
                "    \"descricao\": \"Segundo evento\",\n" +
                "    \"data_inicio\": \"2016-12-16 13:00:00\",\n" +
                "    \"data_fim\": \"2016-10-12 18:00:00\",\n" +
                "    \"ranking\": 0,\n" +
                "    \"status\": \"A\",\n" +
                "    \"certificado\": 1,\n" +
                "    \"created_at\": \"2016-10-16 22:57:23\",\n" +
                "    \"updated_at\": \"2016-10-16 22:57:23\",\n" +
                "    \"usuario\": {\n" +
                "      \"id\": 11,\n" +
                "      \"email\": \"teste@teste.com\",\n" +
                "      \"pessoa_id\": 8,\n" +
                "      \"ong_id\": null,\n" +
                "      \"created_at\": \"2016-10-01 17:32:16\",\n" +
                "      \"updated_at\": \"2016-10-01 17:32:16\"\n" +
                "    },\n" +
                "    \"categoria\": {\n" +
                "      \"id\": 2,\n" +
                "      \"descricao\": \"músicas\",\n" +
                "      \"created_at\": \"2016-10-16 22:44:36\",\n" +
                "      \"updated_at\": \"2016-10-16 22:46:24\"\n" +
                "    },\n" +
                "    \"requisitos\": [\n" +
                "      {\n" +
                "        \"id\": 5,\n" +
                "        \"evento_id\": 8,\n" +
                "        \"descricao\": \"2 Violões\",\n" +
                "        \"created_at\": \"2016-10-16 22:57:23\",\n" +
                "        \"updated_at\": \"2016-10-16 22:57:23\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 6,\n" +
                "        \"evento_id\": 8,\n" +
                "        \"descricao\": \"1 Professor\",\n" +
                "        \"created_at\": \"2016-10-16 22:57:23\",\n" +
                "        \"updated_at\": \"2016-10-16 22:57:23\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]";



        eventoService.getEventsList(new IServiceResponseCallback() {
            @Override
            public void execute(JSONObject response) {

            }

        }, new IExecuteCallback() {
            @Override
            public void execute() {

            }
        });


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

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(data);
            for(int i = 0; i<jsonArray.length(); i++){
                Event event = new Event();
                JSONObject dataEvent = (JSONObject) jsonArray.get(i);
                //set image
                event.setTitle(dataEvent.getString("descricao"));
                event.setAddressCity(dataEvent.getString("cidade"));
                event.setAddressNumber(dataEvent.getString("numero"));
                event.setAddressCity("Porto Alegre");
//                String date = "12-12-2016"; //how to pass date variable to method? (setEndDate)
                event.setCategory("Categoria "+i);
                event.setCreator("Creator "+ i);

                eventList.add(event);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        eventArrayAdapter.notifyDataSetChanged();

        return rootView;
    }
}
