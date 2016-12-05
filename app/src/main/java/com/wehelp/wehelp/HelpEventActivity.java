package com.wehelp.wehelp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wehelp.wehelp.classes.Event;

public class HelpEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_event);

        setTitle("Quero ajudar");
        Event event = (Event)getIntent().getSerializableExtra("event");
        LinearLayout requirementLayout = (LinearLayout)findViewById(R.id.layout_checkbox_requirements);
        TextView eventName = (TextView) findViewById(R.id.help_event_name);
        TextView eventDescription = (TextView) findViewById(R.id.help_event_description);

        eventName.setText(event.getNome());
        eventDescription.setText(event.getDescricao());

        for(int i = 0; i< event.getRequisitos().size(); i++) {
            CheckBox requirement = new CheckBox(this);
            requirement.setText(event.getRequisitos().get(i).getDescricao());
            requirementLayout.addView(requirement);
        }


        System.out.println("HELP EVENT: "+event.getNome());
    }
}
