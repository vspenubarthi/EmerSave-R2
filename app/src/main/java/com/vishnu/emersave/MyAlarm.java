package com.vishnu.emersave;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;
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

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.vishnu.emersave.Home.HaversineInKM;
import static com.vishnu.emersave.Home.moose;

/**
 * Created by Vishn on 1/13/2018.
 */

public class MyAlarm extends BroadcastReceiver {
int nums = 1;
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

    long timey = 7000;
    ArrayList<String> ids = new ArrayList<String>();

    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {

       Toast.makeText(context,String.valueOf(moose),Toast.LENGTH_SHORT).show();
        moose++;
    }

    private void createnotification(Context context, String s, String s1) {
        PendingIntent notificIntent = PendingIntent.getActivity(context,0,new Intent(context,Home.class),0);
        NotificationCompat.Builder mbuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle(" is Safe")
                .setContentText("Your friend is out of danger. You should still check on them to verify safety");

                mbuilder.setContentIntent(notificIntent);

                mbuilder.setAutoCancel(true);

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(nums,mbuilder.build());
                nums++;
                if(nums>10)
                {
                    nums=1;
                }

    }


}
