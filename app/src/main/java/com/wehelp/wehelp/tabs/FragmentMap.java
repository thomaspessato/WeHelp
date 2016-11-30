package com.wehelp.wehelp.tabs;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.Manifest;
import android.widget.RelativeLayout;

import com.wehelp.wehelp.EventDetailActivity;
import com.wehelp.wehelp.R;
import com.wehelp.wehelp.classes.Category;
import com.wehelp.wehelp.classes.Event;
import com.wehelp.wehelp.classes.EventRequirement;
import com.wehelp.wehelp.classes.User;
import com.wehelp.wehelp.classes.WeHelpApp;
import com.wehelp.wehelp.controllers.EventController;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;


public class FragmentMap extends Fragment {

    @Inject
    public EventController eventController;

    public ArrayList<Event> listEvents;

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    @Nullable

    MapView mMapView;
    private GoogleMap googleMap;
    public GoogleApiClient mGoogleApiClient;


    public static FragmentTimeline newInstance() {
        FragmentTimeline fragment = new FragmentTimeline ();
        Bundle args = new Bundle();
//        args.putInt("1", sectionNumber);
//        fragment.setArguments(args);
        return fragment;
    }

    protected void search(List<Address> addresses) {

        //TO DO (FILTER BY CITY)

//        Address address = (Address) addresses.get(0);
//        home_long = address.getLongitude();
//        home_lat = address.getLatitude();
//        latLng = new LatLng(address.getLatitude(), address.getLongitude());
//
//        addressText = String.format(
//                "%s, %s",
//                address.getMaxAddressLineIndex() > 0 ? address
//                        .getAddressLine(0) : "", address.getCountryName());
//
//        markerOptions = new MarkerOptions();
//
//        markerOptions.position(latLng);
//        markerOptions.title(addressText);
//
//        map1.clear();
//        map1.addMarker(markerOptions);
//        map1.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        map1.animateCamera(CameraUpdateFactory.zoomTo(15));
//        locationTv.setText("Latitude:" + address.getLatitude() + ", Longitude:"
//                + address.getLongitude());
//

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((WeHelpApp)getActivity().getApplication()).getNetComponent().inject(this);



        // Exemplo cadastro de eventos
        /*
        Event event = new Event();
        event.setNome("Teste de nome");
        event.setUf("RS");
        event.setRua("Longe");
        event.setComplemento("");
        event.setBairro("Centro");
        event.setCategoriaId(1);
        event.setCertificado(true);
        event.setCidade("Porto Alegre");
        event.setCep("91774845");
        SimpleDateFormat sdf1= new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        try {
            event.setDataFim(sdf1.parse("02/02/1980 14:30:00"));
            event.setDataInicio(sdf1.parse("02/02/1980 15:30:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        event.setDescricao("teste de cadastro");
        event.setLat(-30.034647);
        event.setLng(-51.217658);
        event.setNumero("100");
        event.setPais("Brasil");
        event.setRanking(1);
        event.setStatus("A");
        event.setUsuarioId(11);
        ArrayList<EventRequirement> listReq = new ArrayList<>();
        EventRequirement req = new EventRequirement();
        req.setDescricao("Violão");
        listReq.add(req);
        event.setRequisitos(listReq);
        try {
            eventController.createEvent(event);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        */

        // Exemplo para usuário participar/abandonar evento

        User user = new User();
        user.setId(11);
        Event event = new Event();
        event.setId(11);
        try {
            this.eventController.removeUser(event,user);
            //this.eventController.addUser(event, user);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        this.eventController.getEvents(-30.034647, -51.217658, 50);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tab_map, container, false);

        final RelativeLayout loadingPanel = (RelativeLayout)rootView.findViewById(R.id.loadingPanel);
        assert loadingPanel != null;
        loadingPanel.setVisibility(View.GONE);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        assert mMapView != null;
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
            loadingPanel.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }


        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        loadingPanel.setVisibility(View.GONE);
                        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                    mGoogleApiClient);
                            return;
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(LocationServices.API)
                .build();


        mGoogleApiClient.connect();


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                // For showing a move to my location button
                // For dropping a marker at a point on the Map
                LatLng marker = new LatLng(-30.012054, -51.178840);
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
//                try {
////                List<Address> addresses = new ArrayList<Address>();
//                    /*
//                    while (eventController.getListEvents() == null){
//                        Log.d("WeHelpWS", "Ainda carregando eventos ...");
//                    }
//                    */
//                    List<Address> addresses = new ArrayList<Address>();
//                    if (eventController.getListEvents().size() == 0) {
//                        addresses.add(geocoder.getFromLocationName("Rua Marechal José Inácio, Porto Alegre", 1).get(0));
//                        addresses.add(geocoder.getFromLocationName("Rua Padre Hildebrando, Porto Alegre", 1).get(0));
//                        addresses.add(geocoder.getFromLocationName("Avenida Assis Brasil, Porto Alegre", 1).get(0));
//                        addresses.add(geocoder.getFromLocationName("Avenida Sertório, Porto Alegre", 1).get(0));
//                        addresses.add(geocoder.getFromLocationName("Avenida Plínio Brasil Milano, Porto Alegre", 1).get(0));
//                        addresses.add(geocoder.getFromLocationName("Rua Novo Hamburgo, Porto Alegre", 1).get(0));
//                    } else {
//                        ArrayList<Event> list = eventController.getListEvents();
//                        for (int i = 0; i < list.size(); i++)
//                        {
//                            addresses.add(geocoder.getFromLocation(list.get(i).getLat(), list.get(i).getLng(), 1).get(0));
//                        }
//                    }
//
//
//
//                    for (int i = 0; i< addresses.size(); i++) {
//                        double longitude = addresses.get(i).getLongitude();
//                        double latitude = addresses.get(i).getLatitude();
//                        LatLng test = new LatLng(latitude,longitude);
//
//                        googleMap.addMarker(new MarkerOptions().position(test).title("BLABLBALBA | Educação").snippet("TESTANDO"));
//                    }
//
//                    googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//                        @Override
//                        public void onInfoWindowClick(Marker marker) {
//                            Intent intentDetail = new Intent(getActivity(), EventDetailActivity.class);
//                            startActivity(intentDetail);
//                        }
//                    });
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                googleMap.addMarker(new MarkerOptions().position(marker).title("Creche Moranguinho | Educação").snippet("Necessitamos de 20 caixas de lápis, 30 pacotes de folhas"));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(marker).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                } else {
                    // Show rationale and request permission.
                }

                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


            }
        });

        return rootView;
    }
}