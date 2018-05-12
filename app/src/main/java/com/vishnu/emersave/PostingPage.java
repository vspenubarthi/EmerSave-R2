package com.vishnu.emersave;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.vishnu.emersave.MainActivity.Latitudes;
import static com.vishnu.emersave.MainActivity.Longitudes;


public class PostingPage extends AppCompatActivity implements View.OnClickListener{
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    static String namef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting_page);
    }
    String m_androidId;
    public String getId()
    {
        try {

            m_androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return m_androidId;
    }
    public void onClick(View v) {

        if (v.getId() == R.id.btnPost) {

           /* EditText name = (EditText) findViewById(R.id.Name);
            String namef = name.getText().toString();
            DatabaseReference names = database.getReference("Vishnu").child("Name");
            names.setValue(namef);

            EditText country = (EditText) findViewById(R.id.Country);
            String countryf = country.getText().toString();
            DatabaseReference countries = database.getReference("Vishnu").child("Country");
            countries.setValue(countryf);

            EditText age = (EditText) findViewById(R.id.Age);
            String agef = age.getText().toString();
            DatabaseReference ages = database.getReference("Vishnu").child("Age");
            ages.setValue(agef);

            DatabaseReference status = database.getReference("Vishnu").child("Status");
            status.setValue(0);

            DatabaseReference Longitude = database.getReference("Vishnu").child("Longitude");
            Longitude.setValue(longitude);
            DatabaseReference Latitude = database.getReference("Vishnu").child("Latitude");
            Latitude.setValue(latitude);*/
            namef="Vishnu Default";
            EditText name = (EditText) findViewById(R.id.Name);
             namef = name.getText().toString();
            //DatabaseReference names = database.getReference("messages").child(getId()).child("Name");
            //names.setValue(namef);
            //getName(namef);
            EditText country = (EditText) findViewById(R.id.Country);
            String countryf = country.getText().toString();
            DatabaseReference countries = database.getReference(getId()).child("Country");
            countries.setValue(countryf);

            EditText age = (EditText) findViewById(R.id.Age);
            String agef = age.getText().toString();
            DatabaseReference ages = database.getReference(getId()).child("Age");
            ages.setValue(agef);

            DatabaseReference status = database.getReference(getId()).child("Status");
            status.setValue(0);

            DatabaseReference Longitude = database.getReference(getId()).child("Longitude");
            Longitude.setValue(Longitudes);
            DatabaseReference Latitude = database.getReference(getId()).child("Latitude");
            Latitude.setValue(Latitudes);

        }
    }
    public String getName(String namef)
    {

        return namef;
    }


}
