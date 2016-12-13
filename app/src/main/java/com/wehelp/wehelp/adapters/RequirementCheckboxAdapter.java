package com.wehelp.wehelp.adapters;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;
import com.google.gson.Gson;
import com.wehelp.wehelp.R;
import com.wehelp.wehelp.classes.Event;
import com.wehelp.wehelp.classes.EventRequirement;
import com.wehelp.wehelp.classes.UserRequirement;
import com.wehelp.wehelp.classes.WeHelpApp;
import com.wehelp.wehelp.controllers.EventController;

import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by thomaspessato on 05/12/16.
 */

public class RequirementCheckboxAdapter extends ArrayAdapter<EventRequirement> {

    private ArrayList<EventRequirement> requirementList;
    Context context;
    String className;
    Event event;

    @Inject
    EventController eventController;
    int userId;
    boolean userIsHelping;
    double quantidadeFaltante;

    @Inject
    Application application;

    public RequirementCheckboxAdapter(Context context, int textViewResourceId,
                                      ArrayList<EventRequirement> list) {
        super(context, textViewResourceId, list);
        this.context = context;
        this.requirementList = list;
    }


    public RequirementCheckboxAdapter(Context context, int textViewResourceId,
                                      ArrayList<EventRequirement> list, String className, Event event) {
        super(context, textViewResourceId, list);
        this.context = context;
        this.requirementList = list;
        this.className = className;
        this.event = event;
    }

    private class ViewHolder {
        TextView code;
        TextView helpQtd;
        CheckBox name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));
        quantidadeFaltante = 0;
        userIsHelping = false;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_checkbox_requirement, null);

            final View dialogLayout = inflater.inflate(R.layout.dialog_requirement_help,null);
            holder = new ViewHolder();
            holder.code = (TextView) convertView.findViewById(R.id.code);
            holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
            holder.helpQtd = (TextView)convertView.findViewById(R.id.requirement_txt_helping);
            EventRequirement requirement = null;
            convertView.setTag(holder);

            if(requirementList.get((position)) != null) {
                requirement = requirementList.get(position);
                holder.code.setText("");
                quantidadeFaltante = requirement.getQuant();
                ArrayList<UserRequirement> userRequirements = requirementList.get(position).getUsuariosRequisito();

                if(userRequirements.size() > 0) {
                    int userId = ((WeHelpApp)getContext().getApplicationContext()).getUser().getId();
                    double userHelpingQtd = 0;
                    double userRequirementQnt;

                    for(int i = 0; i<userRequirements.size(); i++) {
                        UserRequirement userRequirement = userRequirements.get(i);
                        int userIdRequisito = userRequirement.getId();

                        if(userIdRequisito == userId) {
                            userIsHelping = true;
                            userHelpingQtd = userRequirement.getQuant();
                        }
                        userRequirementQnt = userRequirements.get(i).getQuant();
                        quantidadeFaltante -= userRequirementQnt;
                    }

                    if(userIsHelping) {
                        holder.helpQtd.setText("Você irá ajudar com "+ userHelpingQtd+" "+ requirement.getUn());
                        requirement.setSelected(true);
                    }

                    if(quantidadeFaltante <= 0) {
                        holder.name.setText(requirement.getQuant()+" "+requirement.getUn()+" "+requirement.getDescricao()+" (quantidade alcançada!)");
                        holder.name.setTextColor(getContext().getResources().getColor(R.color.checkedRequirement));
                        holder.name.setChecked(true);
                    } else{
                        holder.name.setChecked(requirement.isSelected());
                        holder.name.setText(requirement.getQuant()+" "+requirement.getUn()+" "+requirement.getDescricao()+" (faltam "+quantidadeFaltante+")");
                    }
                } else {
                    holder.name.setText(requirement.getQuant()+" "+requirement.getUn()+" "+requirement.getDescricao());
                    holder.name.setChecked(requirement.isSelected());
                }

                requirement.setQuantidadeFaltante(quantidadeFaltante);
                holder.name.setTag(requirement);
            }



            final ViewHolder finalHolder = holder;

            final EventRequirement finalRequirement = requirement;


            assert finalRequirement != null;

            convertView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    if(dialogLayout.getParent() != null) {
                        ((ViewGroup)dialogLayout.getParent()).removeView(dialogLayout);
                    }

                    final TextView tvDialogRequirement = (TextView)dialogLayout.findViewById(R.id.dialog_requirement);
                    String unidade = finalRequirement.getUn();
                    String descricao = finalRequirement.getDescricao();
                    tvDialogRequirement.setText(finalRequirement.getQuant()+" "+finalRequirement.getUn()+" "+finalRequirement.getDescricao()+" (faltam "+finalRequirement.getQuantidadeFaltante()+")");

                    builder.setView(dialogLayout)
                            .setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EditText helpQtd = (EditText)dialogLayout.findViewById(R.id.requirement_help_qtd);
                                    if(helpQtd.getText().toString().equalsIgnoreCase("")) {
                                        helpQtd.setText("1");
                                    }
                                    if(finalRequirement.getUn() == null) {
                                        finalRequirement.setUn("unidade");
                                    }
                                    finalHolder.helpQtd.setText("Você irá ajudar com "+ helpQtd.getText()+" "+ finalRequirement.getUn());
                                    finalHolder.name.setChecked(true);
                                    finalRequirement.setSelected(true);
                                    finalRequirement.setSelectedQuant(Double.parseDouble(helpQtd.getText().toString()));
                                }
                            }).setNegativeButton("NÃO CONTRIBUIR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            EditText helpQtd = (EditText)dialogLayout.findViewById(R.id.requirement_help_qtd);
                            helpQtd.setText("");
                            finalHolder.helpQtd.setText("");
                            finalRequirement.setSelected(false);
                            finalHolder.name.setChecked(false);

                        }
                    });

                    builder.create();
                    builder.show();
                    
                }
            });

        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

}
