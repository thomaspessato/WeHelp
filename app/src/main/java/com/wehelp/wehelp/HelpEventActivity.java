package com.wehelp.wehelp;

import android.app.Application;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wehelp.wehelp.adapters.RequirementCheckboxAdapter;
import com.wehelp.wehelp.classes.Event;
import com.wehelp.wehelp.classes.EventRequirement;
import com.wehelp.wehelp.classes.WeHelpApp;
import com.wehelp.wehelp.controllers.EventController;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.inject.Inject;

public class HelpEventActivity extends AppCompatActivity {

    @Inject
    EventController eventController;
    @Inject
    Application application;

    public Event event;
    RelativeLayout loadingPanel;
    boolean userIsParticipating;


    public ArrayList<EventRequirement> checkedRequirementList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((WeHelpApp) getApplication()).getNetComponent().inject(this);

        setContentView(R.layout.activity_help_event);

        setTitle("Quero ajudar");
        event = (Event)getIntent().getSerializableExtra("event");

        Gson gson = new Gson();
        String eventToString = gson.toJson(event);

        System.out.println("Detalhe do evento: "+eventToString);

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
        checkedRequirementList = new ArrayList<>();
        RequirementCheckboxAdapter checkboxAdapter = new RequirementCheckboxAdapter(this,R.layout.row_checkbox_requirement,requirementList, "HelpEvent", event);

        String address = event.getCidade()+" / "+event.getRua()+" - "+event.getNumero()+", "+event.getComplemento();
        String date = new SimpleDateFormat("dd/mm/yyyy / hh:mm").format(event.getDataInicio());
        userIsParticipating = false;

        assert eventName != null;
        assert eventDescription != null;
        assert eventAddress != null;
        assert eventDate != null;
        assert eventParticipants != null;
        assert lvRequirementsCheckbox != null;
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

        requirementList.addAll(event.getRequisitos());
        lvRequirementsCheckbox.setAdapter(checkboxAdapter);
        checkboxAdapter.notifyDataSetChanged();

        int userId = ((WeHelpApp)application).getUser().getId();
        int userRequirementId;
        for(int i = 0; i < requirementList.size(); i++) {
            for(int j = 0; j < requirementList.get(i).getUsuariosRequisito().size(); j++) {
                userRequirementId = requirementList.get(i).getUsuariosRequisito().get(j).getId();
                if(userId == userRequirementId) {
                    userIsParticipating = true;
                    event.setParticipating(true);
                    helpRegisterButton.setText("Você já está participando!");
                    helpRegisterButton.setBackgroundColor(getResources().getColor(R.color.DividerColor));
                    helpRegisterButton.setTextColor(getResources().getColor(R.color.PriceTextColor));
                }
            }
        }

        setListViewHeightBasedOnChildren(lvRequirementsCheckbox);

        assert helpRegisterButton != null;
        helpRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!event.isParticipating()) {
                    checkedRequirementList = new ArrayList<EventRequirement>();
                    for(int i = 0; i < requirementList.size(); i++) {
                        if(requirementList.get(i).isSelected()) {
                            checkedRequirementList.add(requirementList.get(i));
                            System.out.println("CHECKED ITEM: "+requirementList.get(i).getDescricao().toString());
                        }
                    }
                    new ParticipateEventsTask().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Você já está participando do evento", Toast.LENGTH_LONG).show();
                }

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


    private class ParticipateEventsTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            loadingPanel.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {



            try {
                eventController.addUser(event,((WeHelpApp)application).getUser(), checkedRequirementList);
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


        protected void onPostExecute(Boolean retorno) {
            if (!retorno) {
                Toast.makeText(getApplicationContext(), eventController.errorMessages.toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Você está participando do evento!", Toast.LENGTH_LONG).show();
                finish();
            }
            loadingPanel.setVisibility(View.GONE);
        }
    }
}
