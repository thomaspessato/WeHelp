package com.wehelp.wehelp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.wehelp.wehelp.EventDetailActivity;
import com.wehelp.wehelp.HelpEventActivity;
import com.wehelp.wehelp.R;
import com.wehelp.wehelp.classes.Event;

import java.util.List;

/**
 * Created by temp on 9/15/16.
 */
public class TimelineEventAdapter extends ArrayAdapter<Event>{

    private Context context;
    private List<Event> eventList;


    public TimelineEventAdapter(Context context, List<Event> list) {
        super(context, R.layout.row_timeline, list);
        this.context = context;
        this.eventList= list;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final Event timelineEvent = eventList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_timeline, null);
            System.out.println("timeline event: convertView was NULL");
        }

        TextView linkInfoEvent = (TextView) convertView.findViewById(R.id.btn_moreinfo);
        TextView eventDate = (TextView)convertView.findViewById(R.id.event_timeline_date);
        TextView eventCreator = (TextView)convertView.findViewById(R.id.event_timeline_creator);
        TextView eventCategory = (TextView)convertView.findViewById(R.id.event_timeline_category);
        TextView eventTitle = (TextView)convertView.findViewById(R.id.event_timeline_title);
        Button btnHelp = (Button)convertView.findViewById(R.id.btn_help);

//        String address = timelineEvent.getAddressStreet()+timelineEvent.getAddressNumber()+" - "+timelineEvent.getEndDate().toString();
//        String category = timelineEvent.getCategory();
//        String title = timelineEvent.getTitle();
//        String creator = timelineEvent.getCreator();

        linkInfoEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EventDetailActivity.class);
                context.startActivity(intent);
            }
        });

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentHelp = new Intent(getContext(), HelpEventActivity.class);
                context.startActivity(intentHelp);
            }
        });

//        eventTitle.setText(title);
//        eventCategory.setText(category);
//        eventCreator.setText(creator);
//        eventDate.setText(address);
//        eventCreator.setText(timelineEvent);

        return convertView;
    }
}
