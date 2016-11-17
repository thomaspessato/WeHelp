package com.wehelp.wehelp.tabs.tabs_register;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.wehelp.wehelp.R;
import com.wehelp.wehelp.classes.Person;
import com.wehelp.wehelp.classes.User;
import com.wehelp.wehelp.classes.WeHelpApp;
import com.wehelp.wehelp.controllers.UserController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

public class FragmentPersonRegister extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    @Inject
    UserController userController;
    EditText txtName;
    EditText txtMail;
    EditText txtPhone;
    EditText txtDate;
    RadioGroup radGenre;
    Button btnSavePerson;

    public FragmentPersonRegister() {
        // Required empty public constructor
    }


    public static FragmentPersonRegister newInstance(String param1, String param2) {
        FragmentPersonRegister fragment = new FragmentPersonRegister();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((WeHelpApp)getActivity().getApplication()).getNetComponent().inject(this);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*
        ((WeHelpApp)getActivity().getApplication()).getNetComponent().inject(this);
        User user = new User();
        Person person = new Person();
        user.setEmail("silvadasilva@teste.com");
        user.setPassword("12345");
        SimpleDateFormat sdf1= new SimpleDateFormat("dd/MM/yyyy");
        try {
            person.setDataNascimento(sdf1.parse("02/02/1980"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        person.setFoto("");
        person.setModerador(true);
        person.setNome("lllllppppp");
        person.setRanking(3);
        person.setSexo("m");
        person.setTelefone("5145784578");
        user.setPessoa(person);
        //new CreatePersonTask().execute(user);
        */

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_fragment_person_register, container, false);
        // Elements
        txtName = (EditText)rootView.findViewById(R.id.input_name);
        txtMail = (EditText)rootView.findViewById(R.id.input_mail);
        txtPhone = (EditText)rootView.findViewById(R.id.input_phone);
        txtDate = (EditText)rootView.findViewById(R.id.whichdate);
        radGenre = (RadioGroup)rootView.findViewById(R.id.radio_genre);
        btnSavePerson = (Button)rootView.findViewById(R.id.btnSavePerson);
        btnSavePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validar campos
                User user = new User();
                Person person = new Person();
                SimpleDateFormat sdf1= new SimpleDateFormat("dd/MM/yyyy");

                user.setEmail(txtMail.getText().toString());
                user.setPassword("12345");
                try {
                    person.setDataNascimento(sdf1.parse(txtDate.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                person.setFoto("");
                person.setModerador(false);
                person.setNome(txtName.getText().toString());
                person.setRanking(0);
                person.setSexo(radGenre.indexOfChild(getView().findViewById(radGenre.getCheckedRadioButtonId())) == 1 ? "F" : "M" );
                person.setTelefone(txtPhone.getText().toString());
                user.setPessoa(person);
                new CreatePersonTask().execute(user);
            }
        });


        final EditText date = (EditText)rootView.findViewById(R.id.whichdate);

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.println("BEFORE TEXT CHANGED");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("TEXT BEING CHANGED");
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
                    date.setText(current);
                    date.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();
        };


        date.addTextChangedListener(tw);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class CreatePersonTask extends AsyncTask<User, Void, User> {

        @Override
        protected void onPreExecute() {
            // carregar loader
        }

        @Override
        protected User doInBackground(User... user) {
            try {
                userController.createPerson(user[0]);
                while (userController.userTemp == null && !userController.errorService){}
                if (userController.errorService) {
                    return null;
                }
                User userReturned = userController.userTemp;
                userController.userTemp = null;
                return userReturned;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(User user) {
            if (user == null) {
                Toast.makeText(getActivity().getApplicationContext(), userController.errorMessages.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity().getApplicationContext(), "Erro ao registrar pessoa", Toast.LENGTH_LONG).show();
                Log.d("WeHelpWS", userController.errorMessages.toString());
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Retorno: " + user.getPessoa().getNome(), Toast.LENGTH_LONG).show();
            }

            // remover loader
        }
    }
}
