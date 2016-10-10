package com.wehelp.wehelp.tabs;

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
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.text.Text;
import android.Manifest;
import com.wehelp.wehelp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by temp on 9/15/16.
 */
public class FragmentMap extends Fragment {

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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tab_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        final TextView mLatitudeText = (TextView)rootView.findViewById((R.id.mLocationLat));
        final TextView mLongitudeText= (TextView)rootView.findViewById((R.id.mLocationLong));
        assert mMapView != null;
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        System.out.println("GOOGLE MAPS API CLIENT CONNECTED");
                        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                            mLatitudeText.setText("INSIDE REQUEST FOR LOCATION");
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
                List<Address> addresses = new ArrayList<Address>();

                try {
                    addresses.add(geocoder.getFromLocationName("Rua Marechal José Inácio, Porto Alegre", 1).get(0));
                    addresses.add(geocoder.getFromLocationName("Rua Padre Hildebrando, Porto Alegre", 1).get(0));
                    addresses.add(geocoder.getFromLocationName("Avenida Assis Brasil, Porto Alegre", 1).get(0));
                    addresses.add(geocoder.getFromLocationName("Avenida Sertório, Porto Alegre", 1).get(0));
                    addresses.add(geocoder.getFromLocationName("Avenida Plínio Brasil Milano, Porto Alegre", 1).get(0));
                    addresses.add(geocoder.getFromLocationName("Rua Novo Hamburgo, Porto Alegre", 1).get(0));

                    for (int i = 0; i< addresses.size(); i++) {
                        double longitude = addresses.get(i).getLongitude();
                        double latitude = addresses.get(i).getLatitude();
                        LatLng test = new LatLng(latitude,longitude);
                        googleMap.addMarker(new MarkerOptions().position(test).title("BLABLBALBA | Educação").snippet("TESTANDO"));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                googleMap.addMarker(new MarkerOptions().position(marker).title("Creche Moranguinho | Educação").snippet("Necessitamos de 20 caixas de lápis, 30 pacotes de folhas"));
//                googleMap.addMarker(new MarkesrOptions()
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.common_google_signin_btn_icon_dark_normal))
//                        .snippet("Evento de ajuda | EDUCAÇÃO | MATERIAL ESCOLAR")
//                        .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
//                        .position(marker));


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