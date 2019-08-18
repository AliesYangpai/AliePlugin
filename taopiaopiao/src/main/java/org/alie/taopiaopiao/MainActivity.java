package org.alie.taopiaopiao;

import android.os.Bundle;

/**
 * 这里是不能是用插件本身的上下文的，必须使用宿主module的上下文才可以
 */
public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onStart() {
        super.onStart();
    }


}
