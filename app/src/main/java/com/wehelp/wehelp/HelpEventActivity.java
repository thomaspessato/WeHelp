package com.wehelp.wehelp;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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

import com.google.android.gms.vision.text.Text;
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
    int userId;

    String creatorEmail;


    public ArrayList<EventRequirement> checkedRequirementList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((WeHelpApp) getApplication()).getNetComponent().inject(this);
        userId = ((WeHelpApp)application).getUser().getId();
        setContentView(R.layout.activity_help_event);

        setTitle("Quero ajudar");
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
        TextView txtEmailResponsable = (TextView)findViewById(R.id.txt_email_responsable);
        Button helpRegisterButton = (Button)findViewById(R.id.btn_register_help);
        Button abandonButton = (Button)findViewById(R.id.btn_register_abandon);

        creatorEmail = event.getUsuario().getEmail();
        txtEmailResponsable.setText(Html.fromHtml("<a href=\"mailto:"+creatorEmail+"\">"+creatorEmail+"</a>"));
        txtEmailResponsable.setMovementMethod(LinkMovementMethod.getInstance());

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
        assert helpRegisterButton != null;
        assert lvRequirementsCheckbox != null;
        assert abandonButton != null;
        assert txtParticipating != null;
        assert loadingPanel != null;

        eventName.setText(event.getNome());
        eventDescription.setText(event.getDescricao());
        eventAddress.setText(address);
        eventDate.setText(date);

        abandonButton.setVisibility(View.GONE);
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
                    helpRegisterButton.setText("Atualizar participação");
                    abandonButton.setVisibility(View.VISIBLE);
                    txtParticipating.setVisibility(View.VISIBLE);
                }
            }
        }

        for( int z = 0; z < event.getParticipantes().size(); z++) {
            if(event.getParticipantes().get(z).getId() == userId) {
                event.setParticipating(true);
                helpRegisterButton.setText("Atualizar participação");
                abandonButton.setVisibility(View.VISIBLE);
                txtParticipating.setVisibility(View.VISIBLE);
            }
        }

        setListViewHeightBasedOnChildren(lvRequirementsCheckbox);

        assert helpRegisterButton != null;
        helpRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedRequirementList = new ArrayList<EventRequirement>();
                for(int i = 0; i < requirementList.size(); i++) {
                    if(requirementList.get(i).isSelected()) {
                        checkedRequirementList.add(requirementList.get(i));
                    }
                }
                new ParticipateEventsTask().execute();
            }
        });

        abandonButton.setOnClickListener(new View.OnClickListener() {
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


    private class ParticipateEventsTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            loadingPanel.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                Gson gson = new Gson();
                String json = gson.toJson(checkedRequirementList);
                System.out.println("checked requirement list:");
                for(int i = 0; i< checkedRequirementList.size(); i++)
                    System.out.println("checked"+ checkedRequirementList.get(i));
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

                Intent i = new Intent(Intent.ACTION_SEND);
                String checkedItemsString = "";
                for(int j = 0; j < checkedRequirementList.size(); j++) {
                    String quantidade = Double.toString(checkedRequirementList.get(j).getQuant());
                    String unidade = checkedRequirementList.get(j).getUn();
                    checkedItemsString += quantidade+" "+unidade+" "+checkedRequirementList.get(j).getDescricao() + "\n";
                }

                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{creatorEmail});
                i.putExtra(Intent.EXTRA_SUBJECT, "[WeHelp] Participação no seu evento! "+event.getNome());
                i.putExtra(Intent.EXTRA_TEXT   , "Olá! Irei ajudar com algumas coisas no seu evento!\n\n\n" +checkedItemsString+"\n\n" +
                        "Espero que possamos fazer acontecer!");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(HelpEventActivity.this, "Você não tem nenhum client de e-mail instalado.", Toast.LENGTH_SHORT).show();
                }


                finish();
            }
            loadingPanel.setVisibility(View.GONE);
        }
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

                Intent i = new Intent(Intent.ACTION_SEND);
                String checkedItemsString = "";
                for(int j = 0; j < checkedRequirementList.size(); j++) {
                    String quantidade = Double.toString(checkedRequirementList.get(j).getQuant());
                    String unidade = checkedRequirementList.get(j).getUn();
                    checkedItemsString += quantidade+" "+unidade+" "+checkedRequirementList.get(j).getDescricao() + "\n";
                }

                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{creatorEmail});
                i.putExtra(Intent.EXTRA_SUBJECT, "[WeHelp] Participação no seu evento! "+event.getNome());
                i.putExtra(Intent.EXTRA_TEXT   , "Olá! Infelizmente não poderei mais participar do evento!\n\n\nIria levar:\n\n"+checkedItemsString+"\n\n" +
                        "Qualquer item que eu tiver marcado, não poderei levar! \n\n\n Boa sorte com o evento!");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(HelpEventActivity.this, "Você não tem nenhum client de e-mail instalado.", Toast.LENGTH_SHORT).show();
                }

                loadingPanel.setVisibility(View.GONE);
            }
        }
    }
}
