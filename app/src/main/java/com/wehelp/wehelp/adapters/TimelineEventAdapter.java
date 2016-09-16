package com.wehelp.wehelp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.wehelp.wehelp.R;
import com.wehelp.wehelp.classes.Event;

import java.util.List;

/**
 * Created by temp on 9/15/16.
 */
public class TimelineEventAdapter extends ArrayAdapter<Event>{
    public TimelineEventAdapter(Context context, List<Event> list) {
        super(context, R.layout.row_timeline, list);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_timeline, null);
            System.out.println("timeline event: converTview was NULL");
        }


        System.out.println("IM INSIDE GET VIEW, TIMELINE EVENTS!");
        return convertView;
    }
}
