package com.wehelp.wehelp.tabs.tabs_register;

import android.content.Context;
import android.content.Intent;
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

import com.wehelp.wehelp.LoginActivity;
import com.wehelp.wehelp.R;
import com.wehelp.wehelp.TabbedActivity;
import com.wehelp.wehelp.classes.Person;
import com.wehelp.wehelp.classes.User;
import com.wehelp.wehelp.classes.WeHelpApp;
import com.wehelp.wehelp.controllers.UserController;
import com.wehelp.wehelp.services.IExecuteCallback;
import com.wehelp.wehelp.services.IServiceResponseCallback;

import org.json.JSONException;
import org.json.JSONObject;

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
    EditText txtPassword;
    EditText txtPasswordVerification;
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

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_fragment_person_register, container, false);
        // Elements
        txtName = (EditText)rootView.findViewById(R.id.input_name);
        txtMail = (EditText)rootView.findViewById(R.id.input_mail);
        txtPassword = (EditText)rootView.findViewById(R.id.register_password_person);
        txtPasswordVerification = (EditText)rootView.findViewById(R.id.register_password_person_verification);
        btnSavePerson = (Button)rootView.findViewById(R.id.btnSavePerson);
        btnSavePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validar campos
                String password = txtPassword.getText().toString();
                String passwordValidation = txtPasswordVerification.getText().toString();
                String name = txtName.getText().toString();
                String email = txtMail.getText().toString();

                if(!password.equals("") && !passwordValidation.equals("") && !name.equals("") && !email.equals("")) {
                    if(password.equals(passwordValidation)) {
                        System.out.println("PASSWORD: "+password);
                        System.out.println("PASSWORD VALIDATION: "+passwordValidation);
                        User user = new User();
                        Person person = new Person();

                        user.setEmail(txtMail.getText().toString());
                        user.setPassword(txtPassword.getText().toString());

                        person.setFoto("");
                        person.setModerador(false);
                        person.setNome(txtName.getText().toString());
                        person.setRanking(0);
                        user.setPessoa(person);

                        new CreatePersonTask().execute(user);
                    } else {
//                        Toast.makeText(getActivity().getApplicationContext(), "Senhas não conferem", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Por favor, preencha todos os campos.", Toast.LENGTH_LONG).show();
                }



            }
        });

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
//                Toast.makeText(getActivity().getApplicationContext(), "Erro ao registrar pessoa", Toast.LENGTH_LONG).show();
//                Log.d("WeHelpWS", userController.errorMessages.toString());
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Retorno: " + user.getPessoa().getNome(), Toast.LENGTH_LONG).show();


                String email = txtMail.getText().toString();
                String password = txtPassword.getText().toString();

                userController.login(email, password,
                        new IServiceResponseCallback() {
                            @Override
                            public void execute(JSONObject response) {
                                Intent intent = new Intent(getActivity(), TabbedActivity.class);
                                try {
                                    System.out.println("RESPONSE FULL: "+response);
                                    JSONObject pessoa = response.getJSONObject("pessoa");
                                    intent.putExtra("nome",pessoa.getString("nome"));
                                    intent.putExtra("email",response.getString("email"));
                                    intent.putExtra("pessoa_id",pessoa.getString("pessoa_id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                startActivity(intent);
                            }
                        },
                        new IExecuteCallback() {
                            @Override
                            public void execute() {
                                Toast.makeText(getActivity(), "Erro ao logar", Toast.LENGTH_LONG).show();
                            }
                        }
                );
            }

            // remover loader
        }
    }
}
