package com.wehelp.wehelp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.wehelp.wehelp.R;
import com.wehelp.wehelp.classes.Event;

import java.util.List;

/**
 * Created by temp on 02/11/16.
 */
public class EventParticipateAdapter extends ArrayAdapter<Event> {

    private Context context;
    private List<Event> eventList;


    public EventParticipateAdapter(Context context, List<Event> list) {
        super(context, R.layout.row_event_participate, list);
        this.context = context;
        this.eventList= list;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final Event event = eventList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_event_participate, null);
            System.out.println("participating event: convertView was NULL");
        }

        TextView eventDate = (TextView)convertView.findViewById(R.id.event_participate_time);
        TextView eventAddress = (TextView)convertView.findViewById(R.id.event_participate_address);
        TextView eventTitle = (TextView)convertView.findViewById(R.id.event_participate_title);

        String address = event.getAddressStreet() + event.getAddressNumber();
//        String date = event.getEndDate().toString();
        String title = event.getTitle();

        eventTitle.setText(title);
//        eventDate.setText(date);
        eventAddress.setText(address);

        return convertView;
    }

}
