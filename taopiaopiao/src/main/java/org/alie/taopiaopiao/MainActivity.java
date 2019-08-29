package org.alie.taopiaopiao;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 这里是不能是用插件本身的上下文的，必须使用宿主module的上下文才可以
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv;
    private Button btn1, btn2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initView() {
        iv = (ImageView) findViewById(R.id.iv);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
    }

    private void initListener() {
        iv.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv:
                Toast.makeText(that, "插件apk界面点击", Toast.LENGTH_SHORT).show();
                doStartActivity();
                doStartService();

                break;
            case R.id.btn1:
                doRegisteBroadcastReceiver();
                break;
            case R.id.btn2:
                doStartBroadcastReceiver();
                break;
        }
    }

    private void doStartActivity() {
        startActivity(new Intent(that, SecondActivity.class));
    }

    private void doStartService() {
        startService(new Intent(that, PluginService.class));
    }

    private void doRegisteBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("org.alie.pluginBroadcast");
        registerReceiver(new PluginBroadcastReceiver(), intentFilter);
    }

    private void doStartBroadcastReceiver() {
        Intent intent = new Intent();
        intent.setAction("org.alie.pluginBroadcast");
        sendBroadcast(intent);
    }
}
