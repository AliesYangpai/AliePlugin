package org.alie.taopiaopiao;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import org.alie.pluginpaystander.PayInterfaceActivity;

/**
 * 涉及到上下文的方法都需要重写
 */
public class BaseActivity extends Activity implements PayInterfaceActivity {

    protected Activity that;

    @Override
    public void attach(Activity proxyActivity) {
        this.that = proxyActivity;
    }


    @Override
    public void setContentView(View view) {
        if (that != null) {
            that.setContentView(view);
        } else {
            super.setContentView(view);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        that.setContentView(layoutResID);
    }


    @Override
    public View findViewById(int id) {
        return that.findViewById(id);
    }

    @Override
    public Intent getIntent() {
        if (that != null) {
            return that.getIntent();
        }
        return super.getIntent();
    }

    @Override
    public ClassLoader getClassLoader() {
        return that.getClassLoader();
    }


    /**
     * 重写startActivity，使用宿主的context
     *
     * @param intent
     */
    @Override
    public void startActivity(Intent intent) {
//        ProxyActivity --->className
        Intent m = new Intent();
        m.putExtra("className", intent.getComponent().getClassName());
        that.startActivity(m);
    }

    /**
     * 重写startService，使用宿主的Context
     *
     * @param service
     * @return
     */
    @Override
    public ComponentName startService(Intent service) {
        Intent m = new Intent();
        m.putExtra("serviceName", service.getComponent().getClassName());
        return that.startService(m);
    }

    /**
     * 重写registerReceiver，使用宿主的context
     *
     * @param receiver
     * @param filter
     * @return
     */
    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return that.registerReceiver(receiver, filter);
    }

    /**
     * 重写sendBroadcast，使用宿主的context
     *
     * @param intent
     * @return
     */
    @Override
    public void sendBroadcast(Intent intent) {
        that.sendBroadcast(intent);
    }

    @NonNull
    @Override
    public LayoutInflater getLayoutInflater() {
        return that.getLayoutInflater();
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        return that.getApplicationInfo();
    }


    @Override
    public Window getWindow() {
        return that.getWindow();
    }


    @Override
    public WindowManager getWindowManager() {
        return that.getWindowManager();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }
}
