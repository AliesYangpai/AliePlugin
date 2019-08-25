package org.alie.pluginpaystander;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;

/**
 * Created by Alie on 2019/8/25.
 * 类描述 这个是 插件化标准的service
 * 版本
 */
public interface PayInterfaceService  {
    public void onCreate();

    public void onStart(Intent intent, int startId);

    public int onStartCommand(Intent intent, int flags, int startId);

    public void onDestroy();

    public void onConfigurationChanged(Configuration newConfig);

    public void onLowMemory();

    public void onTrimMemory(int level);

    public IBinder onBind(Intent intent);

    public boolean onUnbind(Intent intent);

    public void onRebind(Intent intent);

    public void onTaskRemoved(Intent rootIntent);

    public void attach(Service proxyService);
}
