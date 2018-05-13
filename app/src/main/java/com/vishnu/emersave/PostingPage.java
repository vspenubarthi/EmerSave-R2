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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.vishnu.emersave.MainActivity.Latitudes;
import static com.vishnu.emersave.MainActivity.Longitudes;


public class PostingPage extends AppCompatActivity{
String m_androidId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting_page);

        TextView name_text = findViewById(R.id.name_text);
        TextView email_text = findViewById(R.id.email_text);
        TextView phone_text = findViewById(R.id.phone_id_text);
        TextView group_text = findViewById(R.id.group_id_text);

        name_text.setText(SignIn.account.getDisplayName());
        email_text.setText(SignIn.account.getEmail());
        phone_text.setText(getId());
        group_text.setText(Register.group);

    }
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

}
