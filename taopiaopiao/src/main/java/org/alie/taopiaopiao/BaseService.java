package org.alie.taopiaopiao;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.util.Log;

import org.alie.pluginpaystander.PayInterfaceService;

/**
 * Created by Alie on 2019/8/25.
 * 类描述
 * 版本
 */
public class BaseService extends Service implements PayInterfaceService {
    public static final String TAG = "PluginBaseService";
    private Service that;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void attach(Service proxyService) {
        this.that = proxyService;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, TAG + " onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.d(TAG, TAG + " onStartCommand");
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Log.d(TAG, TAG + " onDestroy");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        Log.d(TAG, TAG + " onConfigurationChanged");
    }

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        Log.d(TAG, TAG + " onLowMemory");
    }

    @Override
    public void onTrimMemory(int level) {
        // TODO Auto-generated method stub
        Log.d(TAG, TAG + " onTrimMemory");

    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        Log.d(TAG, TAG + " onUnbind");
        return false;
    }

    @Override
    public void onRebind(Intent intent) {
        // TODO Auto-generated method stub
        Log.d(TAG, TAG + " onRebind");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // TODO Auto-generated method stub
        Log.d(TAG, TAG + " onTaskRemoved");
    }
}
