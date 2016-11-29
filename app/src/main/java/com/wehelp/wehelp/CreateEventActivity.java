package com.wehelp.wehelp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wehelp.wehelp.adapters.RequirementListAdapter;

import java.util.ArrayList;
import java.util.List;

public class CreateEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        EditText eventTitle = (EditText)findViewById(R.id.register_event_title);
        EditText eventCategory  = (EditText)findViewById(R.id.register_event_category);
        EditText eventAddressStreet = (EditText)findViewById(R.id.register_event_street);
        EditText eventAddressNumber = (EditText)findViewById(R.id.register_event_number);
        EditText eventAddressComp = (EditText)findViewById(R.id.register_event_comp);
        EditText eventDate = (EditText)findViewById(R.id.register_event_date);
        final EditText eventRequirement = (EditText)findViewById(R.id.register_event_requirement);
        final LinearLayout requirementsLayout = (LinearLayout)findViewById(R.id.linear_requirements);
        Button btnNewRequirement = (Button)findViewById(R.id.btn_new_requirement);
        Button btnRegisterEvent = (Button)findViewById(R.id.btn_register_event);
        final ListView lvRequirements = (ListView)findViewById(R.id.listview_requirements);
        final ArrayList<String> listItems=new ArrayList<String>();

        final ArrayAdapter requirementsArrayAdapter;

        assert btnNewRequirement != null;
        assert requirementsLayout != null;
        assert eventRequirement != null;


        requirementsArrayAdapter = new RequirementListAdapter(this,listItems);
        final int[] requirementsCounter = {0};
        lvRequirements.setAdapter(requirementsArrayAdapter);

        btnNewRequirement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String requirement = eventRequirement.getText().toString();
                if(!requirement.equalsIgnoreCase("")) {
                    listItems.add(requirementsCounter[0],requirement);
                    requirementsArrayAdapter.notifyDataSetChanged();
                    eventRequirement.setText("");
                    requirementsCounter[0]++;
                } else {
                    System.out.println("Need fill the requirement field.");
                }
            }
        });

        btnRegisterEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                IMPLEMENTAR CADASTRO DE EVENTO
            }
        });

    }
}
