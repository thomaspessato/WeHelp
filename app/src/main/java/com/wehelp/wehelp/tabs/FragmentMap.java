package com.wehelp.wehelp.tabs;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wehelp.wehelp.Manifest;
import com.wehelp.wehelp.R;

/**
 * Created by temp on 9/15/16.
 */
public class FragmentMap extends Fragment {

    @Nullable

    MapView mMapView;
    private GoogleMap googleMap;

    public static FragmentTimeline newInstance() {
        FragmentTimeline fragment = new FragmentTimeline ();
        Bundle args = new Bundle();
//        args.putInt("1", sectionNumber);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tab_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button


                // For dropping a marker at a point on the Map
                LatLng melbourne = new LatLng(-37.81319, 144.96298);
                LatLng marker = new LatLng(-37.81214, 144.96193);
                googleMap.addMarker(new MarkerOptions().position(marker).title("Creche Moranguinho | Educação").snippet("Necessitamos de 20 caixas de lápis, 30 pacotes de folhas"));
//                googleMap.addMarker(new MarkerOptions()
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.common_google_signin_btn_icon_dark_normal))
//                        .snippet("Evento de ajuda | EDUCAÇÃO | MATERIAL ESCOLAR")
//                        .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
//                        .position(marker));


                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(melbourne).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                } else {
                    // Show rationale and request permission.
                }

                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);


            }
        });

        return rootView;
    }
}