package com.vishnu.emersave;

import android.location.Address;
import android.location.Geocoder;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    String lon;
    String lat;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /*List<Address> addressList = null;
        Geocoder geocoder = new Geocoder(this);
        DatabaseReference lons = database.getReferenceFromUrl("https://emersave-2d464.firebaseio.com/Vishnu/Latitude");
        lons.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lon = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference lats = database.getReferenceFromUrl("https://emersave-2d464.firebaseio.com/Vishnu/Longitude");
        lats.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lat = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        try {
            addressList = geocoder.getFromLocation(Double.valueOf(lat),Double.valueOf(lon),1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i = 0; i<addressList.size(); i++)
        {
            Address myAdress = addressList.get(i);
            LatLng latLng = new LatLng(myAdress.getLatitude(),myAdress.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Vishnu's Location)"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }

        //LatLng VishnuLoc;

        /*VishnuLoc = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(VishnuLoc).title("Vishnu's Location)"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(VishnuLoc));*/
    }
}
