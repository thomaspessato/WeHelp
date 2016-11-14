package com.wehelp.wehelp.tabs.tabs_register;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wehelp.wehelp.R;
import com.wehelp.wehelp.classes.Ong;
import com.wehelp.wehelp.classes.Person;
import com.wehelp.wehelp.classes.User;
import com.wehelp.wehelp.classes.WeHelpApp;
import com.wehelp.wehelp.controllers.UserController;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentOngRegister.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentOngRegister#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class FragmentOngRegister extends Fragment {
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentOngRegister.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentOngRegister newInstance(String param1, String param2) {
        FragmentOngRegister fragment = new FragmentOngRegister();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public FragmentOngRegister() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((WeHelpApp)getActivity().getApplication()).getNetComponent().inject(this);
        User user = new User();
        user.setEmail("onglalari5@onglalari.com");
        user.setPassword("12345");

        Ong ong = new Ong();
        ong.setFoto("");
        ong.setNome("lllllppppp");
        ong.setTelefone("5132487845");
        ong.setAtivo(true);
        ong.setBairro("Azenha");
        ong.setCep("81458254");
        ong.setCidade("Porto Alegre");
        ong.setCnpj("35043834000168");
        ong.setComplemento("Atrás da caixa d'água");
        ong.setLat(-30.034647);
        ong.setLng(-51.217658);
        ong.setNacionalidade("Brasileira");
        ong.setResponsavelNome("Carlos");
        ong.setResponsavelCpf("17754886243");
        ong.setRua("Av. Azenha");
        ong.setUf("RS");
        ong.setRanking(3);
        ong.setNumero(100);
        user.setOng(ong);
        new CreateOngTask().execute(user);


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_ong_register, container, false);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class CreateOngTask extends AsyncTask<User, Void, User> {

        @Override
        protected User doInBackground(User... user) {
            try {
                userController.createOng(user[0]);
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
                Toast.makeText(getActivity().getApplicationContext(), "Erro ao registrar ONG", Toast.LENGTH_LONG).show();
                Log.d("WeHelpWS", userController.errorMessages.toString());
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Retorno: " + user.getOng().getNome(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
