package com.wehelp.wehelp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.wehelp.wehelp.EventDetailActivity;
import com.wehelp.wehelp.HelpEventActivity;
import com.wehelp.wehelp.R;
import com.wehelp.wehelp.classes.Category;
import com.wehelp.wehelp.classes.Event;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by temp on 9/15/16.
 */
public class ParticipatingEventAdapter extends ArrayAdapter<Event>{

    private Context context;
    private List<Event> eventList;


    public ParticipatingEventAdapter(Context context, List<Event> list) {
        super(context, R.layout.row_participating_event, list);
        this.context = context;
        this.eventList= list;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final Event timelineEvent = eventList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_participating_event, null);
            System.out.println("timeline event: convertView was NULL");
        }

        TextView linkInfoEvent = (TextView) convertView.findViewById(R.id.btn_moreinfo);
        TextView eventAddress = (TextView)convertView.findViewById(R.id.event_timeline_date);
        TextView eventHour = (TextView)convertView.findViewById(R.id.event_timeline_hour);
        TextView eventCreator = (TextView)convertView.findViewById(R.id.event_timeline_creator);
        TextView eventCategory = (TextView)convertView.findViewById(R.id.event_timeline_category);
        TextView eventTitle = (TextView)convertView.findViewById(R.id.event_timeline_title);
        TextView eventDescription = (TextView)convertView.findViewById(R.id.event_timeline_description);
        Button btnAbandon = (Button)convertView.findViewById(R.id.btn_abandon);
//        LinearLayout requirementsLayout = (LinearLayout)convertView.findViewById(R.id.event_requirements_layout);

        String address = "Endereço: "+timelineEvent.getCidade()+" / "+timelineEvent.getRua()+" - "+timelineEvent.getNumero()+", "+timelineEvent.getComplemento();
        String hour = "Data: "+new SimpleDateFormat("dd/mm/yyyy / hh:mm").format(timelineEvent.getDataInicio());

        Category category = timelineEvent.getCategoria();

        String categoria = category.getDescricao();
        String title = timelineEvent.getNome();
        String descricao = timelineEvent.getDescricao();
//        ArrayList requisitos = timelineEvent.getRequisitos();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1);

//        requirementsLayout.removeAllViews();
//
//        for(int i = 0; i< requisitos.size() ; i++) {
//            Object requisito = requisitos.get(i);
//            String requisitoString = ((EventRequirement) requisito).getDescricao();
//            TextView requirementTxt = new TextView(getContext());
//            requirementTxt.setText(requisitoString);
//            requirementsLayout.addView(requirementTxt);
//
//        }

        linkInfoEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EventDetailActivity.class);
                context.startActivity(intent);
            }
        });

        btnAbandon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intentHelp = new Intent(getContext(), HelpEventActivity.class);

//                Bundle mBundle = new Bundle();
//                mBundle.putSerializable("event", timelineEvent);
//                intentHelp.putExtras(mBundle);


//                context.startActivity(intentHelp);
            }
        });

        eventTitle.setText(title);
        eventCategory.setText(categoria);
        eventAddress.setText(address);
        eventHour.setText(hour);
        eventCreator.setText("CRIADOR");
        eventDescription.setText(timelineEvent.getDescricao());

        return convertView;
    }
}
