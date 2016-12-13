package com.wehelp.wehelp;

import android.app.Application;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class MyEventDetailActivity extends AppCompatActivity {

    @Inject
    EventController eventController;
    @Inject
    Application application;

    public Event event;
    RelativeLayout loadingPanel;
    boolean userIsParticipating;
    int userId;


    public ArrayList<EventRequirement> checkedRequirementList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((WeHelpApp) getApplication()).getNetComponent().inject(this);
        userId = ((WeHelpApp)application).getUser().getId();
        setContentView(R.layout.activity_my_event_detail);

        setTitle("Meu evento");
        event = (Event)getIntent().getSerializableExtra("event");

        Gson gson = new Gson();
        String eventToString = gson.toJson(event);

        System.out.println("Detalhe do evento: "+eventToString);
        System.out.println("PARTICIPANTES: "+event.getParticipantes());

        for(int i = 0; i < event.getParticipantes().size(); i++) {
            if(event.getParticipantes().get(i).getId() == userId) {
                userIsParticipating = true;
            }
        }

        TextView eventName = (TextView) findViewById(R.id.help_event_name);
        TextView eventDescription = (TextView) findViewById(R.id.help_event_description);
        TextView eventDate = (TextView) findViewById(R.id.event_help_date);
        TextView eventAddress = (TextView) findViewById(R.id.event_help_address);
        TextView eventParticipants = (TextView) findViewById(R.id.event_help_participants);
        TextView txtParticipating = (TextView) findViewById(R.id.txt_participating);
        TextView txtEmailResponsable = (TextView)findViewById(R.id.txt_email_participants);
        LinearLayout emailsLayout = (LinearLayout)findViewById(R.id.layout_email_participants);
        Button deleteBtn = (Button)findViewById(R.id.btn_register_help);

        String address = event.getCidade()+" / "+event.getRua()+" - "+event.getNumero()+", "+event.getComplemento();
        String date = new SimpleDateFormat("dd/MM/yyyy / HH:mm").format(event.getDataInicio());

        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
        checkedRequirementList = new ArrayList<>();

        final ListView lvRequirementsCheckbox = (ListView)findViewById(R.id.listview_requirements_checkbox);
        final ArrayList<EventRequirement> requirementList = new ArrayList<>();

        RequirementCheckboxAdapter checkboxAdapter = new RequirementCheckboxAdapter(this,R.layout.row_checkbox_requirement,requirementList, "HelpEvent", event);

        userIsParticipating = false;

        assert eventName != null;
        assert eventDescription != null;
        assert eventAddress != null;
        assert eventDate != null;
        assert eventParticipants != null;

        assert lvRequirementsCheckbox != null;

        assert txtParticipating != null;
        assert loadingPanel != null;

        eventName.setText(event.getNome());
        eventDescription.setText(event.getDescricao());
        eventAddress.setText(address);
        eventDate.setText(date);
        txtEmailResponsable.setVisibility(View.GONE);
        emailsLayout.setVisibility(View.GONE);

        txtParticipating.setVisibility(View.GONE);
        loadingPanel.setVisibility(View.GONE);

        if(event.getParticipantes().size() > 0) {
            eventParticipants.setText(event.getParticipantes().size()+" pessoas irão participar deste evento.");
        }
        if(event.getParticipantes().size() == 1) {
            eventParticipants.setText(event.getParticipantes().size()+" pessoa irá participar deste evento.");
        }
        if(event.getParticipantes().size() == 0){
            eventParticipants.setText("Não há nenhuma pessoa participando no momento. Seja a primeira!");
        }

        requirementList.addAll(event.getRequisitos());
        lvRequirementsCheckbox.setAdapter(checkboxAdapter);
        checkboxAdapter.notifyDataSetChanged();

        int userRequirementId;
        for(int i = 0; i < requirementList.size(); i++) {
            for(int j = 0; j < requirementList.get(i).getUsuariosRequisito().size(); j++) {
                userRequirementId = requirementList.get(i).getUsuariosRequisito().get(j).getId();
                if(userId == userRequirementId) {
                    userIsParticipating = true;
                    event.setParticipating(true);
                    txtParticipating.setVisibility(View.VISIBLE);
                }
            }
        }

        if(event.getParticipantes().size() > 0) {
            txtEmailResponsable.setVisibility(View.VISIBLE);
            emailsLayout.setVisibility(View.VISIBLE);
            for( int z = 0; z < event.getParticipantes().size(); z++) {
                TextView tvEmail = new TextView(this);
                String creatorEmail = event.getParticipantes().get(z).getEmail();
                tvEmail.setText(Html.fromHtml("<a href=\"mailto:"+creatorEmail+"\">"+creatorEmail+"</a>"));
                tvEmail.setMovementMethod(LinkMovementMethod.getInstance());
                emailsLayout.addView(tvEmail);
            }
        }

        setListViewHeightBasedOnChildren(lvRequirementsCheckbox);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedRequirementList = new ArrayList<EventRequirement>();
                for(int i = 0; i < requirementList.size(); i++) {
                    if(requirementList.get(i).isSelected()) {
                        checkedRequirementList.add(requirementList.get(i));
                    }
                }
                new DeleteEventTask().execute();
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

    private class DeleteEventTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            // loader
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                eventController.deleteEvent(event);
                while (!eventController.errorService){}
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
                finish();
                Toast.makeText(getApplicationContext(), "Evento deletado com sucesso!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
