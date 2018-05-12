package com.vishnu.emersave;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class MyService extends Service {
    private static final int REQUEST_LOCATION = 1;
    static Button button;
    static Button postToServerButton;
    // static LocationManager locationManager;
    static String Latitudes, Longitudes;
    //String Name;
    static double latitude;
    static double longitude;
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
    private LocationListener locationListener;
    private LocationManager locationManager;
    int nums = 1;
    Home home;

    public MyService() {

    }

    @Override
    public void onCreate() {

      LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        final LocationListener locationListener = new LocationListener()
        {
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
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
            //    ActivityCompat#requestPermissions]
           // Intent change = new Intent(get)
        }

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        DatabaseReference root = database.getReference("messages").child(Register.group);
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
                    //Toast.makeText(MainActivity.this,testyes,Toast.LENGTH_SHORT).show();                                                                                  //1 mile in km = 1.609344
                    if (HaversineInKM(latitude, longitude, dataSnapshot.child("latitude").getValue(double.class), dataSnapshot.child("longitude").getValue(double.class)) < 1.609344) {
                        //  Toast.makeText(MainActivity.this,"Within 10KM",Toast.LENGTH_SHORT).show();
                        if(date.getTime()<dataSnapshot.child("expires").getValue(double.class)) {

                            String Name = dataSnapshot.child("Name").getValue(String.class);
                            String latty = String.valueOf(dataSnapshot.child("latitude").getValue(double.class));
                            String longy =  String.valueOf(dataSnapshot.child("longitude").getValue(double.class));
                            id = dataSnapshot.getKey();
                            if(!ids.contains(id)) {
                                notif(Name, latty, longy);
                                ids.add(id);
                                Toast.makeText(getApplicationContext(),"A Friend Needs Help. Check Notifications",Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(),databaseError.toString(),Toast.LENGTH_LONG).show();
            }

        });


    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (locationManager!= null)
        {
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
       // createnotification(getApplicationContext());
       // Toast.makeText(getApplicationContext(),"This works",Toast.LENGTH_SHORT).show();

        onTaskRemoved(intent);

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        Intent restartService = new Intent(getApplicationContext(),this.getClass());
        restartService.setPackage(getPackageName());
        startService(restartService);
        super.onTaskRemoved(rootIntent);
    }

    public void getGPS()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        else
        {
            home.buildAlertMessageNoGps();

            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                getGPS();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void createnotification(Context context) {
        PendingIntent notificIntent = PendingIntent.getActivity(context,0,new Intent(context,Home.class),0);
        NotificationCompat.Builder mbuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle(" is Safe")
                .setContentText("Your friend is out of danger. You should still check on them to verify safety").setContentIntent(notificIntent).setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(nums,mbuilder.build());
        nums++;
        if(nums>10)
        {
            nums=1;
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

    static final double _eQuatorialEarthRadius = 6378.1370D;
    static final double _d2r = (Math.PI / 180D);

    public static double HaversineInKM(double lat1, double long1, double lat2, double long2) {
        double dlong = (long2 - long1) * _d2r;
        double dlat = (lat2 - lat1) * _d2r;
        double a = Math.pow(Math.sin(dlat / 2D), 2D) + Math.cos(lat1 * _d2r) * Math.cos(lat2 * _d2r)
                * Math.pow(Math.sin(dlong / 2D), 2D);
        double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));
        double d = _eQuatorialEarthRadius * c;

        return d;
    }

    public static double HaversineInM(double lat1, double long1, double lat2, double long2) {
        double dlong = (long2 - long1) * _d2r;
        double dlat = (lat2 - lat1) * _d2r;
        double a = Math.pow(Math.sin(dlat / 2D), 2D) + Math.cos(lat1 * _d2r) * Math.cos(lat2 * _d2r)
                * Math.pow(Math.sin(dlong / 2D), 2D);
        double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));
        double d = _eQuatorialEarthRadius * c/1000;

        return d;
    }

    public void notif(String Name, String latty, String longy) {


        Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
        notificationIntent.setData(Uri.parse("http://maps.google.com/?q="+latty+","+longy));
        PendingIntent pi = PendingIntent.getActivity(this, 0, notificationIntent, 0);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark).setContentTitle(Name + " needs help!").setContentText("Click here to get directions to their location").setContentIntent(pi).setAutoCancel(true);


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

    public void safey(String Name)
    {
        if(mNotificationSafe<mNotificationId || mNotificationSafe==20) {


            NotificationCompat.Builder mBuildery = new NotificationCompat.Builder(this);
            mBuildery.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark).setContentTitle(Name + " is Safe").setContentText("Your friend " + Name + " is out of danger. You should still check on them to verify safety").setAutoCancel(true);


            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgry =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
            mNotifyMgry.notify(mNotificationSafe, mBuildery.build());
            mNotificationSafe++;
            //Can change value below to decide number of notifications on whim
            if (mNotificationSafe > 20) {
                mNotificationSafe = 1;
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

}
