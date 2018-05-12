package com.vishnu.emersave;

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
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
/*
notes on haversine

notes on handler
 */
/*
Seems that if anyone ever enters in info wrong, it does not work
 */
    private static final int REQUEST_LOCATION = 1;
   static Button button;
    static Button postToServerButton;
   static LocationManager locationManager;
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
    final DatabaseReference  root = database.getReference("messages");
    long timey = 7000;
    ArrayList<String> ids = new ArrayList<String>();
    int mNotificationId = 1;
    int mNotificationSafe = 1;
    String previous = "";
    long[] vibr = {200,200,200};
    String message;
    String m_androidId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* Intent intent = getIntent();
        if(SearchIntents.ACTION_SEARCH.equals(intent.getAction()))
        {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
        ChildEventListener listen = root.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
             /*
                    For WAAAAAAYYYYY Later:
                    Not Severity, have it as a pre-set option in settings, check it here and display as well

              */
        }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                boolean cont = dataSnapshot.hasChild("latitude") && dataSnapshot.hasChild("longitude") && dataSnapshot.hasChild("expires") &&
                        dataSnapshot.hasChild("Name") && dataSnapshot.hasChild("message");

                if(!cont)
                {
                   // String all = dataSnapshot.toString();
                    //Toast.makeText(MainActivity.this,all,Toast.LENGTH_LONG).show();
                    return;
                }

                //   Toast.makeText(MainActivity.this,dataSnapshot.toString(),Toast.LENGTH_SHORT).show();
                // Toast.makeText(MainActivity.this,dataSnapshot.getKey(),Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this,dataSnapshot.getKey(),Toast.LENGTH_SHORT).show();
                if(!getId().equals(dataSnapshot.getKey())) {
                    // testyes = String.valueOf(HaversineInKM(latitude, longitude, dataSnapshot.child("latitude").getValue(double.class), dataSnapshot.child("longitude").getValue(double.class)));
                    //Toast.makeText(MainActivity.this,testyes,Toast.LENGTH_SHORT).show();
                    if (HaversineInKM(latitude, longitude, dataSnapshot.child("latitude").getValue(double.class), dataSnapshot.child("longitude").getValue(double.class)) < 10) {
                        //  Toast.makeText(MainActivity.this,"Within 10KM",Toast.LENGTH_SHORT).show();
                        if(date.getTime()<dataSnapshot.child("expires").getValue(double.class)) {

                            String Name = dataSnapshot.child("Name").getValue(String.class);
                            String latty = String.valueOf(dataSnapshot.child("latitude").getValue(double.class));
                            String longy =  String.valueOf(dataSnapshot.child("longitude").getValue(double.class));
                            id = dataSnapshot.getKey();
                            if(!ids.contains(id)) {
                                notif(Name, latty, longy);
                                ids.add(id);
                                Toast.makeText(MainActivity.this,"A Friend Needs Help. Check Notifications",Toast.LENGTH_LONG).show();
                            }

                        }
                    }


                }
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String namer = dataSnapshot.child("Name").getValue(String.class);
                if(!getId().equals(dataSnapshot.getKey())) {
                    safey(namer);
                }
                ids.remove(dataSnapshot.getKey());

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,databaseError.toString(),Toast.LENGTH_LONG).show();
            }

        });

      /*  handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {*/
      //Toast.makeText(MainActivity.this,"I am here",Toast.LENGTH_SHORT).show();

        //onDestroy(mess,listen);

      /*  handler.postDelayed(this, (long) timey);
            }
        };
        handler.postDelayed(runnable, (long) timey);
*/

    /* DatabaseReference oops = database.getReference(getId()).getParent();
       Query tasks = oops.orderByChild("Message").equalTo("Success");
       tasks.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               for(DataSnapshot snapshot: dataSnapshot.getChildren())
               {
                   snapshot.getRef().child("Message").setValue("Hello World");
               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });
       The above is special code, do not remove
       */
    Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
    if(getIntent().getBooleanExtra("crash",false))
    {

    }
    }
    String lon;
    String lat;
    String latVishnu;
    String lonVishnu;
    String latFriend;
    String lonFriend;
    public void notif(String Name, String latty, String longy) {


        Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
        notificationIntent.setData(Uri.parse("http://maps.google.com/?q="+latty+","+longy));
        PendingIntent pi = PendingIntent.getActivity(this, 0, notificationIntent, 0);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark).setContentTitle(Name + " needs help!").setContentText("Yeets").setContentIntent(pi).setAutoCancel(true);


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
                    MainActivity.class);
            startActivity(a);
        }
    }
    public void safey(String Name)
    {
        NotificationCompat.Builder mBuildery = new NotificationCompat.Builder(this);
        mBuildery.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark).setContentTitle(Name + " is Safe").setContentText("Your friend "+Name+" is out of danger. You should still check on them to verify safety").setAutoCancel(true);


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

    protected void onDestroy(DatabaseReference mess, ChildEventListener listen) {
       // super.onDestroy();
        mess.removeEventListener(listen);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
           return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
         int id = item.getItemId();

        if (id == R.id.nav_camera) {
            promptSpeechInput();

        } else if (id == R.id.nav_gallery) {
            Toast.makeText(this, "This is gallary", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_slideshow) {

            getInfo();
        } else if (id == R.id.nav_manage) {
            Intent i = new Intent(
                    MainActivity.this,
          SignIn.class);
            startActivity(i);
        } else if (id == R.id.nav_share) {
            DatabaseReference lats = database.getReference(getId()).child("Latitude");

        lats.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lat = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
            DatabaseReference lons = database.getReference(getId()).child("Longitude");

            lons.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    lon = dataSnapshot.getValue(String.class);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else if (id == R.id.nav_send) {


          /*  DatabaseReference lats = database.getReferenceFromUrl("https://emersave-2d464.firebaseio.com/Vishnu/Latitude");
            lats.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    latVishnu = dataSnapshot.getValue(double.class);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            DatabaseReference lons = database.getReferenceFromUrl("https://emersave-2d464.firebaseio.com/Vishnu/Longitude");
            lons.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    lonVishnu = dataSnapshot.getValue(double.class);

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
            });*/

        double distance = (double)HaversineInKM(Double.valueOf(latVishnu),Double.valueOf(lonVishnu),Double.valueOf(latFriend),Double.valueOf(lonFriend));
          String dist = Double.toString(distance);
          Toast.makeText(this, dist, Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void promptSpeechInput()
    {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello Friend");

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
                if(result.get(0).equals("I need help"))
                {

                    DatabaseReference distress = database.getReference("messages").child(getId());

                    distress.child("message").setValue("I need help");
                    distress.child("latitude").setValue(latitude);
                    distress.child("longitude").setValue(longitude);

                    Date date = new Date();
                    distress.child("expires").setValue(date.getTime()+7200000);
                }
                else
                {
                    Toast.makeText(this,result.get(0),Toast.LENGTH_SHORT).show();
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
        Longitude.setValue(this.longitude);
        Latitude.setValue(this.latitude);
       DatabaseReference Message = database.getReference(getId()).child("Message");
       Message.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               message = dataSnapshot.getValue(String.class);
                if(!message.equals(previous)) {
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(MainActivity.this)
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

    public void onClick(View v) {

        if (v.getId() == R.id.button_location) {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();

            } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                getLocation();
                Vibrator z = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                z.vibrate(400);
                Toast.makeText(this, "Your Current Location:" + "\n" + "Latitude = " + this.latitude
                        + "\n" + "Longitude = " + this.longitude, Toast.LENGTH_LONG).show();

            }
            else
            {

            }


        } else if (v.getId() == R.id.button_server) {
            Intent i = new Intent(
                    MainActivity.this,
                    JustTest.class);
            startActivity(i);

        }
        else if(v.getId()==R.id.mBtn1)
        {
            Intent i = new Intent(
                    MainActivity.this,
                    Home.class);
            startActivity(i);
        }
        else if(v.getId()==R.id.button_help)
        {

            DatabaseReference  distress = database.getReference("messages").child(getId());

         distress.child("message").setValue("I need help");
            distress.child("latitude").setValue(latitude);
            distress.child("longitude").setValue(longitude);
            distress.child("Name").setValue("Vishny");
           Date date = new Date();
            distress.child("expires").setValue(date.getTime()+7200000);
            //The code in the line above gets time in MILLISECONDS, so to give a 2 hour deadline for help request, convert 2 hours to milliseconds and add as I did
        }
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {

                 latitude = location.getLatitude();
                 longitude = location.getLongitude();
                this.Latitudes = String.valueOf(latitude);
                this.Longitudes = String.valueOf(longitude);

            } else {
               // Toast.makeText(this, "Unble to Determine your Location default", Toast.LENGTH_SHORT).show();
                latitude = 42.2848138;
                longitude = -71.7041917;
                this.Latitudes = String.valueOf(latitude);
                this.Longitudes = String.valueOf(longitude);
            }
        }
    }
    protected void buildAlertMessageNoGps() {

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


