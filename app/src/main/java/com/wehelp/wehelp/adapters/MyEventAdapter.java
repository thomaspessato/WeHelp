package com.wehelp.wehelp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;
import com.wehelp.wehelp.AbandonEventActivity;
import com.wehelp.wehelp.HelpEventActivity;
import com.wehelp.wehelp.MyEventDetailActivity;
import com.wehelp.wehelp.R;
import com.wehelp.wehelp.classes.Category;
import com.wehelp.wehelp.classes.Event;
import com.wehelp.wehelp.classes.EventRequirement;
import com.wehelp.wehelp.classes.UserRequirement;
import com.wehelp.wehelp.classes.WeHelpApp;
import com.wehelp.wehelp.controllers.EventController;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by thomaspessato on 06/12/16.
 */

public class MyEventAdapter extends ArrayAdapter<Event> {

    private Context context;
    private List<Event> eventList;

    @Inject
    EventController eventController;

    Event timelineEvent;


    public MyEventAdapter(Context context, List<Event> list) {
        super(context, R.layout.row_event_myevent, list);
        this.context = context;
        this.eventList= list;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        timelineEvent = eventList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_event_myevent, null);
            System.out.println("timeline event: convertView was NULL");
        }

        TextView eventAddress = (TextView)convertView.findViewById(R.id.event_timeline_date);
        TextView eventHour = (TextView)convertView.findViewById(R.id.event_timeline_hour);
        TextView eventCategory = (TextView)convertView.findViewById(R.id.event_timeline_category);
        TextView eventTitle = (TextView)convertView.findViewById(R.id.event_timeline_title);
        TextView eventDescription = (TextView)convertView.findViewById(R.id.event_timeline_description);
        TextView eventParticipants = (TextView)convertView.findViewById(R.id.event_timeline_participating);
        Button btnDelete = (Button)convertView.findViewById(R.id.btn_abandon);
        LinearLayout requirementsLayout = (LinearLayout)convertView.findViewById(R.id.event_requirement_layout);
        LinearLayout userRequirementsLayout = (LinearLayout)convertView.findViewById(R.id.event_user_requirements_layout);
        TextView tvHelpWith = (TextView)convertView.findViewById(R.id.tv_helpwith);
        LinearLayout emailParticipantsLayout = (LinearLayout)convertView.findViewById(R.id.email_participants);
        tvHelpWith.setVisibility(View.GONE);
        String address = "Endereço: "+timelineEvent.getCidade()+" / "+timelineEvent.getRua()+" - "+timelineEvent.getNumero()+", "+timelineEvent.getComplemento();
        String hour = "Data: "+new SimpleDateFormat("dd/MM/yyyy / HH:mm").format(timelineEvent.getDataInicio());

        Category category = timelineEvent.getCategoria();

        String categoria = category.getDescricao();
        String title = timelineEvent.getNome();
        String descricao = timelineEvent.getDescricao();
        ArrayList requisitos = timelineEvent.getRequisitos();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1);

        requirementsLayout.removeAllViews();
        userRequirementsLayout.removeAllViews();
        emailParticipantsLayout.removeAllViews();




        TextView tvNeed = (TextView)convertView.findViewById(R.id.txt_need);
        tvNeed.setVisibility(View.GONE);
        if(requisitos.size() > 0) {
            tvNeed.setVisibility(View.VISIBLE);
            for(int i = 0; i< requisitos.size() ; i++) {
                Object objRequisito = requisitos.get(i);
                EventRequirement requisito = (EventRequirement) objRequisito;

                double quantidadeRequisito = requisito.getQuant();

                for(int j = 0; j < requisito.getUsuariosRequisito().size(); j++) {

                    UserRequirement userRequirement = requisito.getUsuariosRequisito().get(j);
                    int userId = ((WeHelpApp)getContext().getApplicationContext()).getUser().getId();
                    int userIdRequisito = userRequirement.getId();
                    if(userId == userIdRequisito){
                        tvHelpWith.setVisibility(View.VISIBLE);
                        userRequirementsLayout.setVisibility(View.VISIBLE);
                        String requisitoUserString;
                        if(userRequirement.getUn().equalsIgnoreCase("")) {
                            requisitoUserString = userRequirement.getQuant()+" "+requisito.getDescricao();
                        } else {
                            requisitoUserString = userRequirement.getQuant()+" "+userRequirement.getUn()+" de "+requisito.getDescricao();
                        }

                        TextView requirementUserTxt = new TextView(getContext());
                        requirementUserTxt.setTextColor(context.getResources().getColor(R.color.colorAccent));
                        requirementUserTxt.setTextSize(14);
                        requirementUserTxt.setPadding(5, 0, 0 ,0);
                        requirementUserTxt.setText(requisitoUserString);
                        userRequirementsLayout.addView(requirementUserTxt);
                    }
                    System.out.println("USER ID: "+((WeHelpApp)getContext().getApplicationContext()).getUser().getId());

                    quantidadeRequisito -= userRequirement.getQuant();
                }

                String requisitoString;

                if(requisito.getUn().equalsIgnoreCase("")) {
                    requisitoString = requisito.getQuant()+" "+requisito.getDescricao();
                } else {
                    requisitoString = requisito.getQuant()+" "+requisito.getUn()+" de "+requisito.getDescricao();
                }

                TextView requirementTxt = new TextView(getContext());

                if(quantidadeRequisito <= 0) {
                    requirementTxt.setText(requisitoString +" - Quantidade atingida!");
                    requirementTxt.setTextColor(context.getResources().getColor(R.color.checkedRequirement));
                } else {
                    requirementTxt.setText(requisitoString +" (ainda faltam "+quantidadeRequisito+")");
                    requirementTxt.setTextColor(context.getResources().getColor(R.color.colorAccent));
                }

                requirementTxt.setTextSize(14);
                requirementTxt.setPadding(0, 0, 0 ,0);
                requirementsLayout.addView(requirementTxt);
            }


        } else {
            tvNeed.setVisibility(View.GONE);
            tvHelpWith.setVisibility(View.GONE);
            userRequirementsLayout.setVisibility(View.GONE);
            requirementsLayout.setVisibility(View.GONE);
        }

        for( int z = 0; z < timelineEvent.getParticipantes().size(); z++) {
            TextView tvEmail = new TextView(getContext());
            String email = timelineEvent.getParticipantes().get(z).getEmail();
            tvEmail.setText(Html.fromHtml("<a href=\"mailto:"+email+"\">"+email+"</a>"));
            tvEmail.setMovementMethod(LinkMovementMethod.getInstance());
            emailParticipantsLayout.addView(tvEmail);
        }


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentHelp = new Intent(getContext(), MyEventDetailActivity.class);

                Bundle mBundle = new Bundle();
                mBundle.putSerializable("event", timelineEvent);
                intentHelp.putExtras(mBundle);

                context.startActivity(intentHelp);
            }
        });

        eventTitle.setText(title);
        eventCategory.setText(categoria);
        eventAddress.setText(address);
        eventHour.setText(hour);
        eventDescription.setText(timelineEvent.getDescricao());
        if(timelineEvent.getParticipantes().size() > 0) {
            eventParticipants.setText(timelineEvent.getParticipantes().size()+" pessoas irão participar deste evento.");
        }
        if(timelineEvent.getParticipantes().size() == 1) {
            eventParticipants.setText(timelineEvent.getParticipantes().size()+" pessoa irá participar deste evento.");
        }
        if(timelineEvent.getParticipantes().size() == 0){
            eventParticipants.setText("Ninguém está participando do seu evento ainda. Divulgue!");
        }
        return convertView;
    }

}
