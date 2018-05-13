package com.vishnu.emersave;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class Register extends AppCompatActivity {
EditText groupCode;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
static String group;
    String AES = "AES";
    String EncDecpassword = "TestPassword";

    Random randy;
   private DatabaseReference groups = database.getReference("messages");
    private DatabaseReference individualGroups = database.getReference("groupIds");
    private String m_androidId;
    private int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        groupCode = (EditText)findViewById(R.id.group_code);


    }

char appender;
    int val = 0;
    String alphaNumero = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public void clicky(View v) {
        if(v.getId()==R.id.button_group)
        {

            String newId = getNewString();
            individualGroups.child(getId()).child("groupCode").setValue(newId);
            group = newId;
           Toast.makeText(this,"Your new group code is " + newId,Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Register.this, SignIn.class));
        }
        if(v.getId()==R.id.button_group_code)
        {
            if(groupCode.getText().toString().equals("Group Code"))
            {
                Toast.makeText(Register.this,"Please Enter a Group Code or Register a New Group",Toast.LENGTH_LONG).show();
            }
            else
            {
                final String val = groupCode.getText().toString();
                groups.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.hasChild(val))
                        {
                            Toast.makeText(Register.this,"Please Enter in a Valid Group",Toast.LENGTH_LONG).show();

                        } else if (dataSnapshot.hasChild(val))
                        {
                            individualGroups.child(getId()).child("groupCode").setValue(val);
                            group = val;
                            startActivity(new Intent(Register.this, SignIn.class));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

        }
    }
    public String getNewString() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder stringBuilder = new StringBuilder();
        Random rnd = new Random();
        while (stringBuilder.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * chars.length());
            stringBuilder.append(chars.charAt(index));
        }
        String groupings = stringBuilder.toString();
        return groupings;

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
