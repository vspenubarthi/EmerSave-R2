package com.vishnu.emersave;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsPage extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        mAuth = FirebaseAuth.getInstance();
        final EditText setRadius = findViewById(R.id.radius_size);
        setRadius.setText(String.valueOf(MyService.radius));

        final EditText setHelp = findViewById(R.id.help_key_word);
        setHelp.setText(Home.help);
        final EditText setSafe = findViewById(R.id.safe_key_word);
        setSafe.setText(Home.safe);

        final EditText message = findViewById(R.id.set_help_message);
        message.setText(Home.helpMessage);

        Button upDateSettings = findViewById(R.id.settings_update);
        upDateSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!setRadius.getText().toString().equals(""))
                {
                    MyService.radius = Double.valueOf(setRadius.getText().toString());
                }

                if(!message.getText().toString().equals(""))
                {
                    Home.helpMessage = message.getText().toString();
                }
                Toast.makeText(SettingsPage.this,"Settings Updated",Toast.LENGTH_SHORT).show();

                Intent i = new Intent(
                        SettingsPage.this,
                        Home.class);
                startActivity(i);

            }

        });




        Button logOut = findViewById(R.id.logOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();

            }
        });


    }
}
