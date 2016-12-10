package com.wehelp.wehelp.adapters;

import android.app.AlertDialog;
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

import com.wehelp.wehelp.R;
import com.wehelp.wehelp.classes.EventRequirement;

import java.util.ArrayList;

/**
 * Created by thomaspessato on 05/12/16.
 */

public class RequirementCheckboxAdapter extends ArrayAdapter<EventRequirement> {

    private ArrayList<EventRequirement> requirementList;
    Context context;

    public RequirementCheckboxAdapter(Context context, int textViewResourceId,
                                      ArrayList<EventRequirement> list) {
        super(context, textViewResourceId, list);
        this.context = context;
        this.requirementList = list;
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

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_checkbox_requirement, null);

            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                holder.name.setText(requirement.getDescricao());
                holder.name.setChecked(requirement.isSelected());
                holder.name.setTag(requirement);
            }

            final ViewHolder finalHolder = holder;
            final ViewHolder finalHolder1 = holder;
            final EventRequirement finalRequirement = requirement;
            assert finalRequirement != null;
            builder.setView(dialogLayout)
                    .setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText helpQtd = (EditText)dialogLayout.findViewById(R.id.requirement_help_qtd);
                            if(helpQtd.getText().toString().equalsIgnoreCase("")) {
                                helpQtd.setText("1");
                            }
//                            if(finalRequirement.getUnidade() == null) {
                                finalRequirement.setUnidade("unidade");
//                            }
                            finalHolder1.helpQtd.setText("Você irá ajudar com "+ helpQtd.getText()+" "+ finalRequirement.getUnidade());
                            finalHolder.name.setChecked(true);
                        }
                    }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finalHolder.name.setChecked(false);
                }
            });

            convertView.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    if(dialogLayout.getParent() != null) {
                        ((ViewGroup)dialogLayout.getParent()).removeView(dialogLayout);
                    }
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
