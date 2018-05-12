package com.vishnu.emersave;

import android.app.Activity;
import android.app.AlarmManager;
import android.location.LocationListener;
import android.os.Build;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.google.android.gms.actions.SearchIntents;

import static com.vishnu.emersave.PostingPage.namef;

public class Home extends AppCompatActivity {
    TextToSpeech t1;
    private static final int REQUEST_LOCATION = 1;
    static Button button;
    static Button postToServerButton;
    //static LocationManager locationManager;
    static String Latitudes, Longitudes;
    //String Name;
    double latitude;
    double longitude;
    String id;
    String testyes;
    final Handler handler = new Handler();
    private TextView seeSpeak;
    Date date = new Date();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    static int moose = 2;
    long timey = 7000;
    ArrayList<String> ids = new ArrayList<String>();
    int mNotificationId = 1;
    int mNotificationSafe = 1;
    int mtNotificationSafe = 1;
    String previous = "";
    long[] vibr = {200, 200, 200};
    String message;
    String m_androidId;
    String lon;
    String lat;
    String latVishnu;
    String lonVishnu;
    String latFriend;
    String lonFriend;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    LocationListener locationListener;
    String speak = "Help has been called!";
    String safeSpeak = "Make sure you let others know you're safe";
    LocationManager locationManager;
    Location Location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(Home.this, SignIn.class));
                }
            }
        };
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });

        //   Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        if (getIntent().getBooleanExtra("crash", false)) {
            // Toast.makeText(Home.class,ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
        promptSpeechInput();

      /* locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Location = location;
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        else {

        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


*/
      if(Build.VERSION.SDK_INT>=23 && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Toast.makeText(this,"Requesting",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(Home.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},100);
            }
        }

        Toast.makeText(this,"Starting Service",Toast.LENGTH_SHORT).show();
        startService(new Intent(Home.this, MyService.class));
        }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void setAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
      // alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+1000,3000,pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(), 60000, pendingIntent);
       /* AlarmManager mgr=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent i=new Intent(this, MyAlarm.class);
        PendingIntent pi=PendingIntent.getBroadcast(this, 0, i, 0);

        mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 6000,pi);*/
    }

    public void click(View v)
    {
        if(v.getId()==R.id.quick_location)
        {
                Vibrator z = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                z.vibrate(400);
                Toast.makeText(this, "Your Current Location:" + "\n" + "Latitude = " + MyService.latitude
                        + "\n" + "Longitude = " + MyService.longitude, Toast.LENGTH_LONG).show();

        }
        if(v.getId()==R.id.help_request)
        {
            DatabaseReference  distress = database.getReference("messages").child(Register.group).child(getId());

            distress.child("message").setValue("I need help");
            distress.child("latitude").setValue(MyService.latitude);
            distress.child("longitude").setValue(MyService.longitude);
           distress.child("Name").setValue(SignIn.account.getDisplayName());
            Date date = new Date();
            distress.child("expires").setValue(date.getTime()+7200000);
            Toast.makeText(this,"Help Request has been Sent",Toast.LENGTH_LONG).show();
            t1.speak(speak, TextToSpeech.QUEUE_FLUSH, null);
        }
        if(v.getId()==R.id.user_info)
        {
            Intent i = new Intent(
                    Home.this,
                    PostingPage.class);
            startActivity(i);
        }
        if(v.getId()==R.id.cancel)
        {
            DatabaseReference safe = database.getReference("messages").child(Register.group).child(getId());
            safe.removeValue();
            Toast.makeText(this,"Request Cancelled",Toast.LENGTH_SHORT).show();
            t1.speak(safeSpeak, TextToSpeech.QUEUE_FLUSH, null);

        }
        /*if(v.getId()==R.id.settings)
        {
            Toast.makeText(this,"Settings not available yet",Toast.LENGTH_SHORT).show();
        }*/
        if(v.getId()==R.id.floatingActionButton)
        {
            promptSpeechInput();
        }
        if(v.getId()==R.id.settings)
        {
            mAuth.signOut();
        }
    }

    public void notif(String Name, String latty, String longy) {


        Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
        notificationIntent.setData(Uri.parse("http://maps.google.com/?q="+latty+","+longy));
        PendingIntent pi = PendingIntent.getActivity(this, 0, notificationIntent, 0);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark).setContentTitle(Name + " needs help!").setContentText("Yeets").setContentIntent(pi);


        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
        mNotificationId++;
        //Can change value below to decide number of notifications on whim
        if(mNotificationId>20)
        {
            mNotificationId = 1;
        }

    }

    public void doSearch(String query)
    {
        if(query.contains("help"))
        {

            Toast.makeText(this,"This worked",Toast.LENGTH_LONG).show();
            Intent a = new Intent(
                    this,
                    Home.class);
            startActivity(a);
        }
    }

    public void safey(String Name)
    {
        NotificationCompat.Builder mBuildery = new NotificationCompat.Builder(this);
        mBuildery.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark).setContentTitle(Name + " is Safe").setContentText("Your friend "+Name+" is out of danger. You should still check on them to verify safety");


        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgry =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgry.notify(mNotificationSafe, mBuildery.build());
        mNotificationSafe++;
        //Can change value below to decide number of notifications on whim
        if(mNotificationSafe>20)
        {
            mNotificationSafe = 1;
        }
    }
    AlarmManager alarmMgr;
    PendingIntent pendingIntent;

    public void startAlarmManager()
    {
        Intent dialogIntent = new Intent(getBaseContext(), MyAlarm.class);

        alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(this, 0, dialogIntent,PendingIntent.FLAG_CANCEL_CURRENT);

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(), 10000, pendingIntent);

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
    public void notiftest(Context context)
    {
        NotificationCompat.Builder mtBuildery = new NotificationCompat.Builder(context);
        mtBuildery.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark).setContentTitle(" is Safe").setContentText("Your friend is out of danger. You should still check on them to verify safety");


        // Gets an instance of the NotificationManager service
        NotificationManager mtNotifyMgry =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mtNotifyMgry.notify(mtNotificationSafe, mtBuildery.build());
        mtNotificationSafe++;
        //Can change value below to decide number of notifications on whim
        if(mtNotificationSafe>20)
        {
            mtNotificationSafe = 1;
        }
    }
    public void promptSpeechInput()
    {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi " +SignIn.account.getDisplayName());

        try {
            startActivityForResult(i, 100);
        }
        catch (ActivityNotFoundException a){
            Toast.makeText(this, "Sorry does not support your language", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int request_code, int result_code, Intent i)
    {
        super.onActivityResult(request_code,result_code,i);
        switch (request_code)
        {
            case 100: if(result_code== RESULT_OK && i!=null)
            {
                ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if(result.get(0).toLowerCase().contains("help"))
                {

                    DatabaseReference  distress = database.getReference("messages").child(Register.group).child(getId());

                    distress.child("message").setValue("I need help");
                    distress.child("latitude").setValue(MyService.latitude);
                    distress.child("longitude").setValue(MyService.longitude);
                    distress.child("Name").setValue(SignIn.account.getDisplayName());
                    Date date = new Date();
                    distress.child("expires").setValue(date.getTime()+7200000);
                    Toast.makeText(this,"Help Request has been Sent",Toast.LENGTH_LONG).show();
                    t1.speak(speak, TextToSpeech.QUEUE_FLUSH, null);

                }
                else if(result.get(0).toLowerCase().contains("safe"))
                {
                    DatabaseReference safe = database.getReference("messages").child(Register.group).child(getId());
                    safe.removeValue();
                    Toast.makeText(this,"Request Cancelled",Toast.LENGTH_SHORT).show();
                    t1.speak(safeSpeak, TextToSpeech.QUEUE_FLUSH, null);

                }
                else
                {
                    Toast.makeText(this,"Please say HELP if you need help, and SAFE if you wish to cancel HELP request",Toast.LENGTH_LONG).show();
                }
            }
                break;
        }
    }

    public void getInfo()
    {
        DatabaseReference lats = database.getReferenceFromUrl("https://emersave-2d464.firebaseio.com/Vishnu/Latitude");
        lats.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                latVishnu = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference lons = database.getReferenceFromUrl("https://emersave-2d464.firebaseio.com/Vishnu/Longitude");
        lons.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lonVishnu = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference lonsF = database.getReferenceFromUrl("https://emersave-2d464.firebaseio.com/Friend/Latitude");
        lonsF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                latFriend = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference latsF = database.getReferenceFromUrl("https://emersave-2d464.firebaseio.com/Friend/Longitude");
        latsF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lonFriend = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Toast.makeText(this,String.valueOf(latVishnu+lonFriend),Toast.LENGTH_SHORT).show();
    }

    public void display()
    {
        DatabaseReference Longitude = database.getReference(getId()).child("Longitude");
        DatabaseReference Latitude = database.getReference(getId()).child("Latitude");
        Longitude.removeValue();
        Latitude.removeValue();
        Longitude.setValue(MyService.longitude);
        Latitude.setValue(MyService.latitude);
        DatabaseReference Message = database.getReference(getId()).child("Message");
        Message.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                message = dataSnapshot.getValue(String.class);
                if(!message.equals(previous)) {
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(Home.this)
                                    .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                                    .setContentTitle("My notification")
                                    .setContentText(message);

                    int mNotificationId = 001;
// Gets an instance of the NotificationManager service
                    NotificationManager mNotifyMgr =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
                    mNotifyMgr.notify(mNotificationId, mBuilder.build());
                }
                previous = message;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public Location getLocation() {
        if (ActivityCompat.checkSelfPermission(Home.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (Home.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null)
        {
            return location;
        }

        Toast.makeText(this, "Unble to Determine your Location default", Toast.LENGTH_SHORT).show();
        return null;
    }

     public void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void showLocation(Location location) {
        String latitude = "Latitude: ";
        String longitude = "Longitude: ";

        if (location != null) {
            latitude += location.getLatitude();
            longitude += location.getLongitude();
            Toast.makeText(this, latitude + ", " + longitude, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Unble to Determine your Location", Toast.LENGTH_SHORT).show();
        }
    }

    static final double _eQuatorialEarthRadius = 6378.1370D;

    static final double _d2r = (Math.PI / 180D);

    public static int HaversineInM(double lat1, double long1, double lat2, double long2) {
        return (int) (1000D * HaversineInKM(lat1, long1, lat2, long2));
    }

    public static double HaversineInKM(double lat1, double long1, double lat2, double long2) {
        double dlong = (long2 - long1) * _d2r;
        double dlat = (lat2 - lat1) * _d2r;
        double a = Math.pow(Math.sin(dlat / 2D), 2D) + Math.cos(lat1 * _d2r) * Math.cos(lat2 * _d2r)
                * Math.pow(Math.sin(dlong / 2D), 2D);
        double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));
        double d = _eQuatorialEarthRadius * c;

        return d;
    }

    public static double HaversineInMi(double lat1, double long1, double lat2, double long2) {
        double dlong = (long2 - long1) * _d2r;
        double dlat = (lat2 - lat1) * _d2r;
        double a = Math.pow(Math.sin(dlat / 2D), 2D) + Math.cos(lat1 * _d2r) * Math.cos(lat2 * _d2r)
                * Math.pow(Math.sin(dlong / 2D), 2D);
        double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));
        double d = _eQuatorialEarthRadius * c;
        d = d*0.62137119;
        return d;
    }

    public static double HaversineInFt(double lat1, double long1, double lat2, double long2) {
        double dlong = (long2 - long1) * _d2r;
        double dlat = (lat2 - lat1) * _d2r;
        double a = Math.pow(Math.sin(dlat / 2D), 2D) + Math.cos(lat1 * _d2r) * Math.cos(lat2 * _d2r)
                * Math.pow(Math.sin(dlong / 2D), 2D);
        double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));
        double d = _eQuatorialEarthRadius * c;
        d = d*0.62137119*5280;
        return d;
    }

}
