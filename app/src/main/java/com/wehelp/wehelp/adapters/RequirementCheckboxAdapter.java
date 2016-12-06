package com.wehelp.wehelp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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

    public RequirementCheckboxAdapter(Context context, int textViewResourceId,
                                      ArrayList<EventRequirement> list) {
        super(context, textViewResourceId, list);
        this.requirementList = list;
    }

    private class ViewHolder {
        TextView code;
        CheckBox name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_checkbox_requirement, null);

            holder = new ViewHolder();
            holder.code = (TextView) convertView.findViewById(R.id.code);
            holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
            convertView.setTag(holder);

            holder.name.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    EventRequirement requirement = (EventRequirement) cb.getTag();
                    Toast.makeText(getContext(),
                            "Clicked on Checkbox: " + cb.getText() +
                                    " is " + cb.isChecked(),
                            Toast.LENGTH_LONG).show();
                    requirement.setSelected(cb.isChecked());
                }
            });
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(requirementList.get((position)) != null) {
            EventRequirement requirement = requirementList.get(position);
            holder.code.setText("");
            holder.name.setText(requirement.getDescricao());
            holder.name.setChecked(requirement.isSelected());
            holder.name.setTag(requirement);

        }

        return convertView;

    }

}
