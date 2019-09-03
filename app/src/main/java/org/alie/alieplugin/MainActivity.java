package org.alie.alieplugin;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button btn1, btn2,btn3;
    static final String ACTION = "org.alie.taopiaopiao.PLUGIN_ACTION";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        initShakeHandsBroadcastReceiver();
    }

    private void initView() {
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
    }

    private void initListener() {
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
    }

    private void initShakeHandsBroadcastReceiver(){
        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, " 我是宿主，收到你的消息,握手完成!", Toast.LENGTH_SHORT).show();
            }
        };
        registerReceiver(mReceiver, new IntentFilter(ACTION));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                loadPlugin();
                break;
            case R.id.btn2:
                Intent intent = new Intent(this, ProxyActivity.class);

                /**
                 * 通过packageInfo来获取activity信息；0代表launcher activity的全类名
                 */
                String className = "";
                ActivityInfo[] activities = PluginManager.getInstance().getPackageInfo().activities;
                className = activities[0].name;
                intent.putExtra("className", className);
                startActivity(intent);
                break;
            case R.id.btn3:
                sendBroadCastToPlugin();
                break;
        }
    }


    /**
     * 将sd卡中的插件apk copy到私有目录
     * 注意，本身这插件apk是要在 管理台配置好的，但是此处我们就直接加载sd卡中的插件即可
     * 也就是说，至此 我们已经得到了 这个apk了，接下来，我们怎样从apk中得到plugin的activity信息呢
     */
    private void loadPlugin() {
        File filesDir = this.getDir("testplugin", Context.MODE_PRIVATE);
        String name = "taopiaopiao-debug.apk";
        String filePath = new File(filesDir, name).getAbsolutePath();
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        InputStream is = null;
        FileOutputStream os = null;
        try {
            Log.i(TAG, "加载插件 " + new File(Environment.getExternalStorageDirectory(), name).getAbsolutePath());
            is = new FileInputStream(new File(Environment.getExternalStorageDirectory(), name));
            os = new FileOutputStream(filePath);
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            File f = new File(filePath);
            if (f.exists()) {
                Toast.makeText(this, "dex overwrite", Toast.LENGTH_SHORT).show();
            }
            PluginManager.getInstance().loadPath(this);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                os.close();
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public void sendBroadCastToPlugin() {
        Toast.makeText(getApplicationContext(), "我是宿主  插件插件!收到请回答!!  1", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setAction("org.alie.alieplugin.MainActivity");
        sendBroadcast(intent);
    }
}
