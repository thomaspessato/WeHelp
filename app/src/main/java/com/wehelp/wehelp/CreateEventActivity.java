package com.wehelp.wehelp;

import android.content.DialogInterface;
import android.location.Geocoder;

import android.support.v7.app.AlertDialog;

import android.os.AsyncTask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wehelp.wehelp.adapters.RequirementListAdapter;
import com.wehelp.wehelp.classes.Event;
import com.wehelp.wehelp.classes.EventRequirement;
import com.wehelp.wehelp.classes.User;
import com.wehelp.wehelp.classes.WeHelpApp;
import com.wehelp.wehelp.controllers.EventController;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class CreateEventActivity extends AppCompatActivity {


    @Inject
    public EventController eventController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        ((WeHelpApp)getApplication()).getNetComponent().inject(this);

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
        final EditText eventDesc = (EditText)findViewById(R.id.register_event_desc);
        final ListView lvRequirements = (ListView)findViewById(R.id.listview_requirements);

        final ArrayList<String> listItems=new ArrayList<String>();

        final ArrayAdapter requirementsArrayAdapter;
        final Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        assert btnNewRequirement != null;
        assert eventRequirement != null;


        requirementsArrayAdapter = new RequirementListAdapter(this,listItems);
        final int[] requirementsCounter = {0};
        lvRequirements.setAdapter(requirementsArrayAdapter);


        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    eventDate.setText(current);
                    eventDate.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();
        };
        TextWatcher twHour = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 4; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 4){
                        clean = clean + hhmm.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int hour  = Integer.parseInt(clean.substring(0,2));
                        int min  = Integer.parseInt(clean.substring(2,4));
//                        int year = Integer.parseInt(clean.substring(4,8));

                        if(hour > 23) {
                            hour = 23;
                        }

                        if(min > 59) {
                            min = 59;
                        }

                        System.out.println("CLEANING AFTER 23");
//                        cal.set(Calendar.MONTH, mon-1);
//                        year = (year<1900)?1900:(year>2100)?2100:year;
//                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        clean = String.format("%02d%02d",hour, min);
                    }

                    clean = String.format("%s:%s", clean.substring(0, 2),
                            clean.substring(2, 4));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    eventHour.setText(current);
                    eventHour.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

            private String current = "";
            private String hhmm = "hhmm";
            private Calendar cal = Calendar.getInstance();
        };



        eventHour.addTextChangedListener(twHour);
        eventDate.addTextChangedListener(tw);


        btnNewRequirement.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                    String requirement = eventRequirement.getText().toString();
                    if(!requirement.equalsIgnoreCase("")) {
                        listItems.add(requirement);
                        requirementsArrayAdapter.notifyDataSetChanged();
                        eventRequirement.setText("");
                        setListViewHeightBasedOnChildren(lvRequirements);
                    } else {
                        Toast.makeText(getApplicationContext(), "Por favor, preencha algo no requisito", Toast.LENGTH_SHORT).show();
                    }
                }
        });


//
//        lvRequirements.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
//
//                System.out.println("clicked on item");
//                AlertDialog.Builder adb=new AlertDialog.Builder(CreateEventActivity.this);
//                adb.setTitle("Delete?");
//                adb.setMessage("Are you sure you want to delete " + position);
//                final int positionToRemove = position;
//                adb.setNegativeButton("Cancel", null);
//                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        listItems.remove(positionToRemove);
//                        requirementsArrayAdapter.notifyDataSetChanged();
//                    }});
//                adb.show();
//            }
//        });



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
                String desc = eventDesc.getText().toString();
                ArrayList<EventRequirement> requirementsArr = new ArrayList();

                for (int i=0;i<requirementsArrayAdapter.getCount();i++){
                    EventRequirement req = new EventRequirement();
                    req.setDescricao(requirementsArrayAdapter.getItem(i).toString());
                    requirementsArr.add(req);
                }

                System.out.println("requirements: "+requirementsArr);

                if(title.equalsIgnoreCase("") ||
                        category.equalsIgnoreCase("") ||
                        addressStreet.equalsIgnoreCase("") ||
                        addressNumber.equalsIgnoreCase("") ||
                        addressComp.equalsIgnoreCase("") ||
                        desc.equalsIgnoreCase("") ||
                        date.equalsIgnoreCase("") ||
                        hour.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Desculpe, todos os campos são necessários!", Toast.LENGTH_SHORT).show();
                    return;
                }

                double latitude;
                double longitude;
                try {
                    latitude = geocoder.getFromLocationName(addressStreet+addressNumber+addressComp+", Porto Alegre",1).get(0).getLatitude();
                    longitude = geocoder.getFromLocationName(addressStreet+addressNumber+addressComp+", Porto Alegre",1).get(0).getLongitude();
                    System.out.println("lat: "+latitude);
                    System.out.println("lng: "+longitude);
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Endereço inválido", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    return;
                }

//                IMPLEMENTAR CADASTRO DE EVENTO WS
                Event event = new Event();
                event.setNome(title);
                event.setRua(addressStreet);
                event.setComplemento(addressComp);
                event.setCategoriaId(1);
                event.setCertificado(true);
                event.setCidade("Porto Alegre");
                SimpleDateFormat sdf1= new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                try {
                    //event.setDataFim(sdf1.parse(date));
                    event.setDataInicio(sdf1.parse(date + " " + hour + ":00"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                event.setDescricao(desc);
                event.setLat(latitude);
                event.setLng(longitude);
                event.setNumero(addressNumber);
                event.setPais("Brasil");
                event.setRanking(1);
                event.setStatus("A");
                event.setUsuarioId(((WeHelpApp)getApplication()).getUser().getId());
                ArrayList<EventRequirement> listReq = new ArrayList<>();
                event.setRequisitos(requirementsArr);
                new CreateEventTask().execute(event);
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

    }

    private class CreateEventTask extends AsyncTask<Event, Void, Event> {

        @Override
        protected void onPreExecute() {
            // carregar loader
        }

        @Override
        protected Event doInBackground(Event... event) {
            try {
                eventController.createEvent(event[0]);
                while (eventController.eventTemp == null && !eventController.errorService){}
                if (eventController.errorService) {
                    return null;
                }
                Event eventReturned = eventController.eventTemp;
                eventController.eventTemp = null;
                return eventReturned;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(Event event) {
            if (event == null) {
                Toast.makeText(getApplicationContext(), eventController.errorMessages.toString(), Toast.LENGTH_LONG).show();
//                Toast.makeText(getActivity().getApplicationContext(), "Erro ao registrar pessoa", Toast.LENGTH_LONG).show();
//                Log.d("WeHelpWS", userController.errorMessages.toString());
            } else {
                Toast.makeText(getApplicationContext(), "Retorno: " + event.getNome(), Toast.LENGTH_LONG).show();
            }

            // remover loader
        }
    }



}
