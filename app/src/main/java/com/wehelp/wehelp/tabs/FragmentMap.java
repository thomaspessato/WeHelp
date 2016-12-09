package com.wehelp.wehelp.tabs;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import android.widget.TextView;
import android.widget.Toast;

import com.wehelp.wehelp.EventDetailActivity;
import com.wehelp.wehelp.HelpEventActivity;
import com.wehelp.wehelp.R;
import com.wehelp.wehelp.TabbedActivity;
import com.wehelp.wehelp.classes.Category;
import com.wehelp.wehelp.classes.Event;
import com.wehelp.wehelp.classes.EventRequirement;
import com.wehelp.wehelp.classes.User;
import com.wehelp.wehelp.classes.WeHelpApp;
import com.wehelp.wehelp.controllers.EventController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
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
    private GoogleMap googleMap;
    public GoogleApiClient mGoogleApiClient;
    public ArrayList<Event> listEvents;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;

    @Nullable

    MapView mMapView;

    public static FragmentTimeline newInstance() {
        FragmentTimeline fragment = new FragmentTimeline();
        Bundle args = new Bundle();
//        args.putInt("1", sectionNumber);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((WeHelpApp) getActivity().getApplication()).getNetComponent().inject(this);

        //new ListEventsTask().execute();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tab_map, container, false);
        final RelativeLayout loadingPanel = (RelativeLayout) rootView.findViewById(R.id.loadingPanel);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        assert loadingPanel != null;
        assert mMapView != null;

        loadingPanel.setVisibility(View.VISIBLE);
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

                            System.out.println("USER LOCATION:" + mLastLocation);
                            return;
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        System.out.println("CONNECTION SUSPENDED");
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        System.out.println("CONNECTION FAILED");
                    }
                })
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        new ListEventsTask().execute();
    }

    private class ListEventsTask extends AsyncTask<Void, Void, ArrayList<Event>> {


        Location location;
        Criteria criteria;
        LocationManager locationManager;

        @Override
        protected void onPreExecute() {
            // carregar loader
        }

        @Override
        protected ArrayList<Event> doInBackground(Void... params) {
            try {
                locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                criteria = new Criteria();
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    location = new Location("");
                    location.setLatitude(-30.034647);
                    location.setLongitude(-51.217658);
                } else {
                    location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                }

                if (location == null) {
                    location = new Location("");
                    location.setLatitude(-30.034647);
                    location.setLongitude(-51.217658);
                }

                double userLatitude = location.getLatitude();
                double userLongitude = location.getLongitude();
                eventController.getEvents(userLatitude, userLongitude, 50);
                while (eventController.getListEvents() == null && !eventController.errorService){}
                if (eventController.errorService) {
                    return null;
                }
                ArrayList<Event> listEvents = eventController.getListEvents();
                eventController.setListEvents(null);
                return listEvents;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(ArrayList<Event> events) {
            if (events == null) {
                Toast.makeText(getActivity().getApplicationContext(), eventController.errorMessages.toString(), Toast.LENGTH_LONG).show();
            } else {
                listEvents = events;
                //Toast.makeText(getActivity().getApplicationContext(), "Retorno: OK", Toast.LENGTH_LONG).show();
                // Adiciona lista de eventos a Activity principal

            }
            TabbedActivity tab = (TabbedActivity)getActivity();
            tab.listEvents = listEvents;

            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {
                    googleMap = mMap;
                    LatLng marker = new LatLng(-30.012054, -51.178840);
                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

                    mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    try {
                        List<Address> addresses = new ArrayList<Address>();
                        if (listEvents == null && listEvents.size() == 0) {
                            addresses.add(geocoder.getFromLocationName("Rua Marechal José Inácio, Porto Alegre", 1).get(0));
                            addresses.add(geocoder.getFromLocationName("Rua Padre Hildebrando, Porto Alegre", 1).get(0));
                            addresses.add(geocoder.getFromLocationName("Avenida Assis Brasil, Porto Alegre", 1).get(0));
                            addresses.add(geocoder.getFromLocationName("Avenida Sertório, Porto Alegre", 1).get(0));
                            addresses.add(geocoder.getFromLocationName("Avenida Plínio Brasil Milano, Porto Alegre", 1).get(0));
                            addresses.add(geocoder.getFromLocationName("Rua Novo Hamburgo, Porto Alegre", 1).get(0));

                            for (int i = 0; i < addresses.size(); i++) {
                                double longitude = addresses.get(i).getLongitude();
                                double latitude = addresses.get(i).getLatitude();
                                LatLng test = new LatLng(latitude, longitude);

                                googleMap.addMarker(new MarkerOptions().position(test).title("BLABLBALBA | Educação").snippet("TESTANDO"));
                            }
                        } else {
                            for (int i = 0; i < listEvents.size(); i++) {
                                double longitude = listEvents.get(i).getLng();
                                double latitude = listEvents.get(i).getLat();
                                LatLng test = new LatLng(latitude, longitude);


                                Marker newMarker = googleMap.addMarker(new MarkerOptions().position(test).title(listEvents.get(i).getNome()).snippet(listEvents.get(i).getCategoria().getDescricao()));
                                newMarker.setTag(listEvents.get(i));
                            }
                        }


                        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                    System.out.println("MARKER TAG "+marker.getTag());
                                Intent intentHelp = new Intent(getContext(), HelpEventActivity.class);

                                Bundle mBundle = new Bundle();
                                mBundle.putSerializable("event", (Event)marker.getTag());
                                intentHelp.putExtras(mBundle);

                                startActivity(intentHelp);
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//

                    // ISSO FUNCIONA
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


            // remover loader
        }


    }
    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter, GoogleMap.OnMarkerClickListener {

        private final View myContentsView;

        MyInfoWindowAdapter(){
            myContentsView = getActivity().getLayoutInflater().inflate(R.layout.custom_info_window, null);
        }


        @Override
        public View getInfoContents(Marker marker) {
            TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
            tvTitle.setText(marker.getTitle());
            TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
            tvSnippet.setText(marker.getSnippet());
            return myContentsView;
        }



        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean onMarkerClick(Marker marker) {

            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        System.out.println("CLICKED ON ITEM! "+item.getTitle());

        return super.onOptionsItemSelected(item);
    }
}