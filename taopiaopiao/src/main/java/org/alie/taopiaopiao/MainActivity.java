package org.alie.taopiaopiao;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * 这里是不能是用插件本身的上下文的，必须使用宿主module的上下文才可以
 */
public class MainActivity extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(that,"插件apk界面点击",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }


}
