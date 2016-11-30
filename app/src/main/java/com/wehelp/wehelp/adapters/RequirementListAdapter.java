package com.wehelp.wehelp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.wehelp.wehelp.R;

import java.util.List;

/**
 * Created by thomaspessato on 28/11/16.
 */

public class RequirementListAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> requirementList;


    public RequirementListAdapter(Context context, List<String> list) {
        super(context, R.layout.row_timeline, list);
        this.context = context;
        this.requirementList= list;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {

        final String requirement = requirementList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_requirement, null);
            System.out.println("timeline event: convertView was NULL");
        }

        final TextView requirementItem = (TextView)convertView.findViewById(R.id.requirement_text);
        Button btnDeleteRequirement = (Button)convertView.findViewById(R.id.btn_requirement_delete);

        btnDeleteRequirement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("CLICKED ON A REQUIREMENT ITEM! "+requirementItem.getText().toString());
                requirementList.remove(position);
                notifyDataSetChanged();
            }
        });

        requirementItem.setText(requirement);


        return convertView;
    }
}
