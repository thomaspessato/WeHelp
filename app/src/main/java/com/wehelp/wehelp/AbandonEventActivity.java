package com.wehelp.wehelp;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wehelp.wehelp.adapters.RequirementCheckboxAdapter;
import com.wehelp.wehelp.classes.Event;
import com.wehelp.wehelp.classes.EventRequirement;
import com.wehelp.wehelp.classes.WeHelpApp;
import com.wehelp.wehelp.controllers.EventController;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.inject.Inject;

public class AbandonEventActivity extends AppCompatActivity {

    @Inject
    EventController eventController;
    @Inject
    Application application;
    RelativeLayout loadingPanel;
    public Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((WeHelpApp) getApplication()).getNetComponent().inject(this);

        setContentView(R.layout.activity_abandon_event);

        setTitle("Desistir do evento");
        this.event = (Event)getIntent().getSerializableExtra("event");
        TextView eventName = (TextView) findViewById(R.id.help_event_name);
        TextView eventDescription = (TextView) findViewById(R.id.help_event_description);
        TextView eventDate = (TextView) findViewById(R.id.event_help_date);
        TextView eventAddress = (TextView) findViewById(R.id.event_help_address);
        TextView eventParticipants = (TextView) findViewById(R.id.event_help_participants);
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
        assert loadingPanel != null;
        loadingPanel.setVisibility(View.GONE);

        Button helpRegisterButton = (Button)findViewById(R.id.btn_register_help);
        final ListView lvRequirementsCheckbox = (ListView)findViewById(R.id.listview_requirements_checkbox);
        final ArrayList<EventRequirement> requirementList = new ArrayList<>();
        final ArrayList<EventRequirement> checkedRequirementList = new ArrayList<>();
        event.setParticipating(true);
        RequirementCheckboxAdapter checkboxAdapter = new RequirementCheckboxAdapter(this,R.layout.row_checkbox_requirement,requirementList, "AbandonEvent", event);


        String address = event.getCidade()+" / "+event.getRua()+" - "+event.getNumero()+", "+event.getComplemento();
        String date = new SimpleDateFormat("dd/mm/yyyy / hh:mm").format(event.getDataInicio());

        assert eventName != null;
        assert eventDescription != null;
        assert eventAddress != null;
        assert eventDate != null;
        assert eventParticipants != null;
//        assert lvRequirementsCheckbox != null;
        eventName.setText(event.getNome());
        eventDescription.setText(event.getDescricao());
        eventAddress.setText(address);
        eventDate.setText(date);

        if(event.getNumeroParticipantes() > 0) {
            eventParticipants.setText(event.getNumeroParticipantes()+" pessoas irão participar deste evento.");
        }
        if(event.getNumeroParticipantes() == 1) {
            eventParticipants.setText(event.getNumeroParticipantes()+" pessoa irá participar deste evento.");
        }
        if(event.getNumeroParticipantes() == 0){
            eventParticipants.setText("Não há nenhuma pessoa participando no momento. Seja a primeira!");
        }

        lvRequirementsCheckbox.setAdapter(checkboxAdapter);
        requirementList.addAll(event.getRequisitos());

        checkboxAdapter.notifyDataSetChanged();

        setListViewHeightBasedOnChildren(lvRequirementsCheckbox);

        assert helpRegisterButton != null;
        helpRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPanel.setVisibility(View.VISIBLE);
                checkedRequirementList.clear();
                for(int i = 0; i< requirementList.size(); i++) {
                    if(requirementList.get(i).isSelected()) {
                        checkedRequirementList.add(requirementList.get(i));
                    }
                }
                new AbandonEventTask().execute();
            }
        });

    }


    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {

            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();

    }


    private class AbandonEventTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            loadingPanel.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                eventController.removeUser(event,((WeHelpApp)application).getUser());
                while (!eventController.removeUserOk && !eventController.errorService){}
                if (eventController.errorService) {
                    return false;
                } else {
                    finish();
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }

        }

        protected void onPostExecute(Boolean retorno) {
            if (!retorno) {
                Toast.makeText(getApplicationContext(), eventController.errorMessages.toString(), Toast.LENGTH_LONG).show();
                loadingPanel.setVisibility(View.GONE);
            } else {
                Toast.makeText(getApplicationContext(), "Você desistiu de participar do evento", Toast.LENGTH_LONG).show();
                loadingPanel.setVisibility(View.GONE);
            }
        }
    }
}
