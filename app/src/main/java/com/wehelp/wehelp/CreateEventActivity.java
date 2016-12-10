package com.wehelp.wehelp;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;

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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.wehelp.wehelp.adapters.CategorySpinnerAdapter;
import com.wehelp.wehelp.adapters.RequirementListAdapter;
import com.wehelp.wehelp.classes.Category;
import com.wehelp.wehelp.classes.Event;
import com.wehelp.wehelp.classes.EventRequirement;
import com.wehelp.wehelp.classes.WeHelpApp;
import com.wehelp.wehelp.controllers.CategoryController;
import com.wehelp.wehelp.controllers.EventController;

import java.io.IOException;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class CreateEventActivity extends AppCompatActivity {


    @Inject
    public EventController eventController;

    @Inject
    public CategoryController categoryController;
    public ArrayList<Category> listCategories;
    RelativeLayout loadingPanel;

    Spinner categorySpinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        ((WeHelpApp)getApplication()).getNetComponent().inject(this);

        setTitle("Criar evento");

        new ListCategoriesTask().execute();

        final EditText eventTitle = (EditText)findViewById(R.id.register_event_title);
        final EditText eventAddressStreet = (EditText)findViewById(R.id.register_event_street);
        final EditText eventAddressNumber = (EditText)findViewById(R.id.register_event_number);
        final EditText eventAddressComp = (EditText)findViewById(R.id.register_event_comp);
        final EditText eventDate = (EditText)findViewById(R.id.register_event_date);
        final EditText eventRequirement = (EditText)findViewById(R.id.register_event_requirement);
        final EditText eventHour = (EditText)findViewById(R.id.register_event_hour);
        final EditText eventDesc = (EditText)findViewById(R.id.register_event_desc);
        final EditText eventRequirementQtd = (EditText)findViewById(R.id.register_event_requirement_qtd);
        final EditText eventRequirementUnit = (EditText)findViewById(R.id.register_event_requirement_uni);
        final ListView lvRequirements = (ListView)findViewById(R.id.listview_requirements);
        final ArrayList<EventRequirement> listItems=new ArrayList<EventRequirement>();
        final ArrayAdapter requirementsArrayAdapter;
        final Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        Button btnNewRequirement = (Button)findViewById(R.id.btn_new_requirement);
        Button btnRegisterEvent = (Button)findViewById(R.id.btn_register_event);
        categorySpinner = (Spinner)findViewById(R.id.event_register_spinner_category);
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.GONE);

        assert loadingPanel != null;
        assert btnNewRequirement != null;
        assert eventRequirement != null;

        requirementsArrayAdapter = new RequirementListAdapter(this,listItems);
        lvRequirements.setAdapter(requirementsArrayAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Category item = (Category)adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);

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
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 4){
                        clean = clean + hhmm.substring(clean.length());
                    }else{
                        int hour  = Integer.parseInt(clean.substring(0,2));
                        int min  = Integer.parseInt(clean.substring(2,4));

                        if(hour > 23) {
                            hour = 23;
                        }

                        if(min > 59) {
                            min = 59;
                        }

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

                    EventRequirement requirement = new EventRequirement();
                    double etQuantidade = Double.parseDouble(eventRequirementQtd.getText().toString());
                    if(etQuantidade > 0) {
                        requirement.setQuant(etQuantidade);
                    } else {
                        requirement.setQuant(1);
                    }
                    requirement.setDescricao(eventRequirement.getText().toString());
                    requirement.setUn(eventRequirementUnit.getText().toString());
                    if(!requirement.getDescricao().equalsIgnoreCase("")) {
                        listItems.add(requirement);
                        requirementsArrayAdapter.notifyDataSetChanged();
                        eventRequirement.setText("");
                        setListViewHeightBasedOnChildren(lvRequirements);
                    } else {
                        Toast.makeText(getApplicationContext(), "Por favor, preencha algo no requisito", Toast.LENGTH_SHORT).show();
                    }
                }
        });

        btnRegisterEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadingPanel.setVisibility(View.VISIBLE);
                String title = eventTitle.getText().toString();
                Category category = (Category)categorySpinner.getSelectedItem();
                String addressStreet = eventAddressStreet.getText().toString();
                String addressNumber = eventAddressNumber.getText().toString();
                String addressComp = eventAddressComp.getText().toString();
                String date = eventDate.getText().toString();
                String hour = eventHour.getText().toString();
                String desc = eventDesc.getText().toString();
                ArrayList<EventRequirement> requirementsArr = new ArrayList();

                for (int i=0;i<requirementsArrayAdapter.getCount();i++){

                    Log.d("TESTE", requirementsArrayAdapter.getItem(i).toString());

                    requirementsArr.add((EventRequirement)requirementsArrayAdapter.getItem(i));
                }

                System.out.println("requirements: "+requirementsArr);

                if(title.equalsIgnoreCase("") ||
                        category == null ||
                        addressStreet.equalsIgnoreCase("") ||
                        addressNumber.equalsIgnoreCase("") ||
                        desc.equalsIgnoreCase("") ||
                        date.equalsIgnoreCase("") ||
                        hour.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Desculpe, todos os campos são necessários!", Toast.LENGTH_SHORT).show();
                    loadingPanel.setVisibility(View.GONE);
                    return;
                }

                double latitude = 0;
                double longitude = 0;
                Address address;
                int tentativas = 3;
                while (tentativas > 0) {
                    try {
                        List<Address> lst = geocoder.getFromLocationName(addressStreet + " " + addressNumber + " Porto Alegre RS Brasil", 1);
                        if(lst.size() > 0) {
                            address = lst.get(0);
                            latitude = address.getLatitude();
                            longitude = address.getLongitude();
                            System.out.println("lat: " + latitude);
                            System.out.println("lng: " + longitude);
                        } else {
                            Toast.makeText(getApplicationContext(), "Tentando encontrar endereço", Toast.LENGTH_SHORT).show();
                            tentativas--;
                            continue;
                        }
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                        tentativas--;
                        if (tentativas == 0) {
                            Toast.makeText(getApplicationContext(), "Endereço inválido", Toast.LENGTH_SHORT).show();
                            loadingPanel.setVisibility(View.GONE);
                            return;
                        }
                    }
                }

//                IMPLEMENTAR CADASTRO DE EVENTO WS
                Event event = new Event();
                event.setNome(title);
                event.setRua(addressStreet);
                event.setComplemento(addressComp);
                event.setCategoriaId(category.getId()); //pegar categoria selecionada
                event.setCertificado(true);
                event.setCidade("Porto Alegre");
                event.setUf("RS");
                SimpleDateFormat sdf1= new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                try {
                    //event.setDataFim(sdf1.parse(date));
                    event.setDataInicio(sdf1.parse(date + " " + hour + ":00"));
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Data/Hora são inválidos!", Toast.LENGTH_SHORT).show();
                    eventDate.requestFocus();
                    return;
                }
                event.setDescricao(desc);
                event.setLat(latitude);
                event.setLng(longitude);
                event.setNumero(addressNumber);
                event.setPais("Brasil");
                event.setRanking(1);
                event.setStatus("A");
                event.setUsuarioId(((WeHelpApp)getApplication()).getUser().getId());
                event.setRequisitos(requirementsArr);
                new CreateEventTask().execute(event);
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

    private class CreateEventTask extends AsyncTask<Event, Void, Event> {

        @Override
        protected void onPreExecute() {
            // carregar loader
        }

        @Override
        protected Event doInBackground(Event... event) {
            try {
                System.out.println("CREATING EVENT");
                eventController.createEvent(event[0]);
                while (eventController.eventTemp == null && !eventController.errorService){}
                if (eventController.errorService) {
                    return null;
                }

                Event eventReturned = eventController.eventTemp;
                System.out.println("EVENT RETURNED");
                eventController.eventTemp = null;
                return eventReturned;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(Event event) {
            if (event == null) {
                System.out.println("Erro ao cadastrar evento: "+eventController.errorMessages);
                loadingPanel.setVisibility(View.GONE);
            } else {
                System.out.println("event: NOVO EVENTO: "+event);
                System.out.println("event: NOME DO EVENTO: "+event.getNome());
                System.out.println("event: RUA DO EVENTO: "+event.getRua());
                Toast.makeText(getApplicationContext(), "Evento  " + event.getNome() + " cadastrado", Toast.LENGTH_LONG).show();
                loadingPanel.setVisibility(View.GONE);
                Intent intent = new Intent(CreateEventActivity.this, TabbedActivity.class);
                startActivity(intent);
            }
            // remover loader
        }
    }

    private class ListCategoriesTask extends AsyncTask<Void, Void, ArrayList<Category>> {


        @Override
        protected void onPreExecute() {
            // carregar loader
        }

        @Override
        protected ArrayList<Category> doInBackground(Void... params) {
            try {
                categoryController.getCatgeories();
                while (categoryController.getListCategories() == null && !categoryController.errorService) {
                }
                if (categoryController.errorService) {
                    return null;
                }
                ArrayList<Category> listCategories = categoryController.getListCategories();
                categoryController.setListCategories(null);
                return listCategories;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(ArrayList<Category> categories) {
            if (categories == null) {
                Toast.makeText(getApplicationContext(), categoryController.errorMessages.toString(), Toast.LENGTH_LONG).show();
            } else {
                listCategories = categories;
                ArrayList<Category> arrayListCategories = new ArrayList<>();
                for(int i = 0; i < listCategories.size(); i++) {
                    arrayListCategories.add(listCategories.get(i));
                }
                CategorySpinnerAdapter spinnerAdapter = new CategorySpinnerAdapter(CreateEventActivity.this, R.layout.row_spinner_category,arrayListCategories);
                spinnerAdapter.notifyDataSetChanged();
                categorySpinner.setAdapter(spinnerAdapter);
            }
        }
    }


}
