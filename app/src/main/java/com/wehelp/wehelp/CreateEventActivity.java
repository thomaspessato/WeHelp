package com.wehelp.wehelp;

import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wehelp.wehelp.adapters.RequirementListAdapter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CreateEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        setTitle("Criar evento");

        final EditText eventTitle = (EditText)findViewById(R.id.register_event_title);
        final EditText eventCategory  = (EditText)findViewById(R.id.register_event_category);
        final EditText eventAddressStreet = (EditText)findViewById(R.id.register_event_street);
        final EditText eventAddressNumber = (EditText)findViewById(R.id.register_event_number);
        final EditText eventAddressComp = (EditText)findViewById(R.id.register_event_comp);
        final EditText eventDate = (EditText)findViewById(R.id.register_event_date);
        final EditText eventRequirement = (EditText)findViewById(R.id.register_event_requirement);
        final EditText eventHour = (EditText)findViewById(R.id.register_event_hour);
        Button btnNewRequirement = (Button)findViewById(R.id.btn_new_requirement);
        Button btnRegisterEvent = (Button)findViewById(R.id.btn_register_event);
        final ListView lvRequirements = (ListView)findViewById(R.id.listview_requirements);
        final ArrayList<String> listItems=new ArrayList<String>();

        final ArrayAdapter requirementsArrayAdapter;
        final Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        assert btnNewRequirement != null;
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
                    setListViewHeightBasedOnChildren(lvRequirements);
                } else {
                    Toast.makeText(getApplicationContext(), "Por favor, preencha algo no requisito", Toast.LENGTH_SHORT).show();
                }

            }
        });


        btnRegisterEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = eventTitle.getText().toString();
                String category = eventCategory.getText().toString();
                String addressStreet = eventAddressStreet.getText().toString();
                String addressNumber = eventAddressNumber.getText().toString();
                String addressComp = eventAddressComp.getText().toString();
                String date = eventDate.getText().toString();
                String hour = eventHour.getText().toString();
                ArrayList requirementsArr = new ArrayList();

                for (int i=0;i<requirementsArrayAdapter.getCount();i++){
                    requirementsArr.add(i,requirementsArrayAdapter.getItem(i));
                }

                System.out.println("requirements: "+requirementsArr);

                if(title.equalsIgnoreCase("") ||
                        category.equalsIgnoreCase("") ||
                        addressStreet.equalsIgnoreCase("") ||
                        addressNumber.equalsIgnoreCase("") ||
                        addressComp.equalsIgnoreCase("") ||
                        date.equalsIgnoreCase("") ||
                        hour.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Desculpe, todos os campos são necessários!", Toast.LENGTH_SHORT).show();
                    return;
                }



                try {
                    double latitude = geocoder.getFromLocationName(addressStreet+addressNumber+addressComp+", Porto Alegre",1).get(0).getLatitude();
                    double longitude = geocoder.getFromLocationName(addressStreet+addressNumber+addressComp+", Porto Alegre",1).get(0).getLongitude();
                    System.out.println("lat: "+latitude);
                    System.out.println("lng: "+longitude);
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Endereço inválido", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    return;
                }

//                IMPLEMENTAR CADASTRO DE EVENTO WS
            }
        });


    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        Log.e("Listview Size ", "" + listView.getCount());
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

    };

}
