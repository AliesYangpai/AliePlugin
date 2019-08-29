package org.alie.alieplugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.alie.pluginpaystander.PayInterfaceBroadcast;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Alie on 2019/8/29.
 * 类描述
 * 版本
 */
public class ProxyBroadcastReceiver extends BroadcastReceiver {
    private String pluginReceiverClassName;
    private PayInterfaceBroadcast payInterfaceBroadcast;

    public ProxyBroadcastReceiver(String pluginReceiverClassName, Context context) {
        this.pluginReceiverClassName = pluginReceiverClassName;
        try {
            Class loadClass = PluginManager.getInstance().getDexClassLoader().loadClass(pluginReceiverClassName);
            Constructor loadConstructor = loadClass.getConstructor(new Class[]{});
            Object instance = loadConstructor.newInstance(new Object[]{});
            payInterfaceBroadcast = (PayInterfaceBroadcast) instance;
            payInterfaceBroadcast.attach(context);

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

    // 根据pluginReceiverClassName -->class-->Object-->PayInterfaceBroadcast
    @Override
    public void onReceive(Context context, Intent intent) {
        payInterfaceBroadcast.onReceive(context, intent);
    }
}
