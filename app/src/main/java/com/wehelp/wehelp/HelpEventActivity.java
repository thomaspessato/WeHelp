package com.wehelp.wehelp;

import android.app.Application;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wehelp.wehelp.adapters.RequirementCheckboxAdapter;
import com.wehelp.wehelp.classes.Event;
import com.wehelp.wehelp.classes.EventRequirement;
import com.wehelp.wehelp.classes.WeHelpApp;
import com.wehelp.wehelp.controllers.EventController;

import org.json.JSONException;

import java.util.ArrayList;

import javax.inject.Inject;

public class HelpEventActivity extends AppCompatActivity {

    @Inject
    EventController eventController;
    @Inject
    Application application;

    public Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((WeHelpApp) getApplication()).getNetComponent().inject(this);

        setContentView(R.layout.activity_help_event);

        setTitle("Quero ajudar");
        this.event = (Event)getIntent().getSerializableExtra("event");
        TextView eventName = (TextView) findViewById(R.id.help_event_name);
        TextView eventDescription = (TextView) findViewById(R.id.help_event_description);
        Button helpRegisterButton = (Button)findViewById(R.id.btn_register_help);
//        final ListView lvRequirementsCheckbox = (ListView)findViewById(R.id.listview_requirements_checkbox);
//        final ArrayList<EventRequirement> requirementList = new ArrayList<>();
//        final ArrayList<EventRequirement> checkedRequirementList = new ArrayList<>();
//        RequirementCheckboxAdapter checkboxAdapter = new RequirementCheckboxAdapter(this,R.layout.row_checkbox_requirement,requirementList);

        assert eventName != null;
        assert eventDescription != null;
//        assert lvRequirementsCheckbox != null;
        eventName.setText(event.getNome());
        eventDescription.setText(event.getDescricao());
//        lvRequirementsCheckbox.setAdapter(checkboxAdapter);

//        for(int i = 0; i< event.getRequisitos().size(); i++) {
//            EventRequirement requirement = event.getRequisitos().get(i);
//            requirementList.add(requirement);
//        }

//        checkboxAdapter.notifyDataSetChanged();

        assert helpRegisterButton != null;
        helpRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                checkedRequirementList.clear();
//                for(int i = 0; i< requirementList.size(); i++) {
//                    if(requirementList.get(i).isSelected()) {
//                        checkedRequirementList.add(requirementList.get(i));
//                    }
//                }
                new ParticipateEventsTask().execute();
            }
        });

    }

    private class ParticipateEventsTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            // carregar loader
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                eventController.addUser(event,((WeHelpApp)application).getUser());
                while (!eventController.addUserOk && !eventController.errorService){}
                if (eventController.errorService) {
                    return false;
                } else {

                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }

        }

        protected void onPostExecute(boolean retorno) {
            if (!retorno) {
                Toast.makeText(getApplicationContext(), eventController.errorMessages.toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Retorno: OK", Toast.LENGTH_LONG).show();

            }
            // remover loader
        }
    }
}
