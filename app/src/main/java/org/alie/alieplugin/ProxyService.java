package org.alie.alieplugin;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import org.alie.pluginpaystander.PayInterfaceService;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Alie on 2019/8/25.
 * 类描述 这个是桩service
 * 版本
 */
public class ProxyService extends Service {
    private PayInterfaceService payInterfaceService;
    private String serviceName;

    private void init(Intent intent) {
        serviceName = intent.getStringExtra("serviceName");
        try {
            Class loadClass = PluginManager.getInstance().getDexClassLoader().loadClass(serviceName);
            Constructor localConstructor = loadClass.getConstructor(new Class[]{});
            Object object = localConstructor.newInstance(new Object[]{});
            payInterfaceService = (PayInterfaceService) object;
            payInterfaceService.attach(this);
            payInterfaceService.onCreate();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        init(intent);
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (payInterfaceService == null) {
            init(intent);
            return payInterfaceService.onStartCommand(intent, flags, startId);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (payInterfaceService != null) {
            return payInterfaceService.onUnbind(intent);
        }
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        if (payInterfaceService != null) {
            payInterfaceService.onRebind(intent);
        } else {
            super.onRebind(intent);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}
