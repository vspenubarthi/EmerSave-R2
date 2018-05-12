package com.vishnu.emersave;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Vishn on 12/28/2017.
 */

public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {
   private Activity activity;

   public MyExceptionHandler(Activity a)
   {
       activity=a;
   }

    @Override
    public void uncaughtException(Thread thread, Throwable ex)
    {
      //  Toast.makeText(,ex.getMessage(),Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(activity,Home.class);
        intent.putExtra("crash",true);intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(MyApplication.getInstance().getBaseContext(),
                0,intent,PendingIntent.FLAG_ONE_SHOT);

        AlarmManager mgr = (AlarmManager) MyApplication.getInstance().getBaseContext()
                .getSystemService(Context.ALARM_SERVICE);
                    mgr.set(AlarmManager.RTC,System.currentTimeMillis() + 1000, pendingIntent);

                    activity.finish();

        System.exit(2);

    }
}
