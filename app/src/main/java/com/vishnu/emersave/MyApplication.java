package com.vishnu.emersave;

import android.app.Application;
import android.content.Context;

/**
 * Created by Vishn on 12/28/2017.
 */

public class MyApplication extends Application {
    public static MyApplication instance;
    @Override
    public void onCreate()
    {
        super.onCreate();
        instance=this;
    }
    @Override
    public Context getApplicationContext()
    {
       return super.getApplicationContext();
    }
    public static MyApplication getInstance()
    {
        return instance;
    }
}
