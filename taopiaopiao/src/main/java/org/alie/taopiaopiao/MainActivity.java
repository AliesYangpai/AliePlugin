package org.alie.taopiaopiao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 这里是不能是用插件本身的上下文的，必须使用宿主module的上下文才可以
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initView(){
        iv = (ImageView) findViewById(R.id.iv);
    }
    private void initListener(){
        iv.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv:
                Toast.makeText(that,"插件apk界面点击",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(that, SecondActivity.class));
                break;
        }
    }
}
