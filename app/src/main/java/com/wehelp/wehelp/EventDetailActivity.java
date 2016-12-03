package com.wehelp.wehelp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.wehelp.wehelp.adapters.EventCommentAdapter;
import com.wehelp.wehelp.adapters.TimelineEventAdapter;
import com.wehelp.wehelp.classes.Comment;
import com.wehelp.wehelp.classes.Event;

import java.util.ArrayList;

public class EventDetailActivity extends AppCompatActivity {


    public ArrayAdapter<Comment> commentArrayAdapter;
    public ArrayList<Comment> commentList = new ArrayList<Comment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        setTitle("Detalhe do evento");

        commentArrayAdapter = new EventCommentAdapter(this,commentList);

        getSupportActionBar().setHomeButtonEnabled(true);

        ListView commentListView = (ListView)findViewById(R.id.comment_list);
        commentListView.setAdapter(commentArrayAdapter);

        commentListView.setFocusable(false);

        for(int i = 0; i<10; i++) {
            Comment comment = new Comment();
            commentList.add(comment);
        }

        commentArrayAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(commentListView);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
