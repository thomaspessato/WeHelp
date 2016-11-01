package com.wehelp.wehelp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wehelp.wehelp.EventDetailActivity;
import com.wehelp.wehelp.R;
import com.wehelp.wehelp.classes.Comment;
import com.wehelp.wehelp.classes.Event;

import java.util.List;

/**
 * Created by temp on 9/19/16.
 */
public class EventCommentAdapter extends ArrayAdapter<Comment> {

    private Context context;


    public EventCommentAdapter(Context context, List<Comment> list) {
        super(context, R.layout.row_comment, list);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_comment, null);
            System.out.println("timeline event: converTview was NULL");
        }

        System.out.println("IM INSIDE GET VIEW, EVENT COMMENTS!");
        return convertView;
    }
}
