package com.wehelp.wehelp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.wehelp.wehelp.R;
import com.wehelp.wehelp.classes.Category;
import com.wehelp.wehelp.classes.Event;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by thomaspessato on 06/12/16.
 */

public class MyEventAdapter extends ArrayAdapter<Event> {

    private Context context;
    private List<Event> eventList;


    public MyEventAdapter(Context context, List<Event> list) {
        super(context, R.layout.row_event_myevent, list);
        this.context = context;
        this.eventList= list;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final Event timelineEvent = eventList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_event_myevent, null);
            System.out.println("timeline event: convertView was NULL");
        }

        TextView eventAddress = (TextView)convertView.findViewById(R.id.event_my_date);
        TextView eventHour = (TextView)convertView.findViewById(R.id.event_my_hour);
//        TextView eventCreator = (TextView)convertView.findViewById(R.id.event_my_creator);
        TextView eventCategory = (TextView)convertView.findViewById(R.id.event_my_category);
        TextView eventTitle = (TextView)convertView.findViewById(R.id.event_my_title);
        TextView eventDescription = (TextView)convertView.findViewById(R.id.event_my_description);
        TextView eventParticipants = (TextView)convertView.findViewById(R.id.event_my_participating);
        Button btnAbandon = (Button)convertView.findViewById(R.id.btn_delete);

        String address = "Endereço: "+timelineEvent.getCidade()+" / "+timelineEvent.getRua()+" - "+timelineEvent.getNumero()+", "+timelineEvent.getComplemento();
        String hour = "Data: "+new SimpleDateFormat("dd/mm/yyyy / hh:mm").format(timelineEvent.getDataInicio());

        Category category = timelineEvent.getCategoria();

        String categoria = category.getDescricao();
        String title = timelineEvent.getNome();
        String descricao = timelineEvent.getDescricao();
//        ArrayList requisitos = timelineEvent.getRequisitos();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1);

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

        System.out.println("PARTICIPATING EVENT:" +timelineEvent);

        eventTitle.setText(title);
        eventCategory.setText(categoria);
        eventAddress.setText(address);
        eventHour.setText(hour);
//        eventCreator.setText("CRIADOR");
        eventDescription.setText(timelineEvent.getDescricao());

        if(timelineEvent.getNumeroParticipantes() > 0) {
            eventParticipants.setText("Você e mais +"+(timelineEvent.getNumeroParticipantes()-1)+" pessoas irão participar deste evento.");
        }
        if(timelineEvent.getNumeroParticipantes() == 0) {
            eventParticipants.setText("Apenas você está participando deste projeto. Divulgue!");
        }

        return convertView;
    }
}
