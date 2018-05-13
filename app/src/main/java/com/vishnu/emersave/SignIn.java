package com.vishnu.emersave;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {
    private SignInButton mGoogleBtn;
    public static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
   private FirebaseDatabase database = FirebaseDatabase.getInstance();
   private String m_androidId;

    private DatabaseReference individualGroups = database.getReference("groupIds");
    String group = "";
    String EncDecpassword = "TestPassword";
    String AES = "AES";

    ImageView icon;
TextView textView;
String accountName = "Default Name";
String urI = " ";
Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();
        mGoogleBtn = (SignInButton) findViewById(R.id.googleBtn);

        //Toast.makeText(SignIn.this,android.os.Build.MODEL,Toast.LENGTH_SHORT).show();
       // boolean isEmulator = isInEmulator();
      //  loadGroupId();

       // SystemClock.sleep(5000);
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null)
                {
                    if(Register.group!=null) {

                        startActivity(new Intent(SignIn.this, Home.class));
                    }
                    else
                    {
                        Toast.makeText(SignIn.this,"It seems you do not have a group, please register below",Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(SignIn.this,"You got an Error",Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


    }
    public boolean isInEmulator()
    {
        return android.os.Build.MODEL.contains("x86");
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
    private void signIn()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }
static GoogleSignInAccount account;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess())
            {
                 account = result.getSignInAccount();
                Toast.makeText(getApplicationContext(),"Successful Sign In",Toast.LENGTH_SHORT).show();
                firebaseAuthWithGoogle(account);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(SignIn.this,"Authentication failed",Toast.LENGTH_LONG).show();
                        }

                    }

                });
    }

    public void onClicky(View view) {
        startActivity(new Intent(SignIn.this, Register.class));
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
    public void loadGroupId()
    {
        individualGroups.child(getId()).child("groupCode").addListenerForSingleValueEvent(new ValueEventListener( ) {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Register.group = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
