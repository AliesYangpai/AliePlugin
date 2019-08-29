package org.alie.taopiaopiao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.alie.pluginpaystander.PayInterfaceBroadcast;

/**
 * Created by Alie on 2019/8/29.
 * 类描述
 * 版本
 */
public class PluginBroadcastReceiver extends BroadcastReceiver implements PayInterfaceBroadcast {


    @Override
    public void attach(Context context) {
        Toast.makeText(context,"----广播绑定content成功----",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"----收到来自主module的广播----",Toast.LENGTH_SHORT).show();
    }
}
