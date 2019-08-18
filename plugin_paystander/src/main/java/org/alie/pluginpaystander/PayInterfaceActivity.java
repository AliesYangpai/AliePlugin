package org.alie.pluginpaystander;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 * Created by Alie on 2019/8/18.
 * 类描述  制定标准的moudle中的接口，由于刚才提到，插件化开发中难点是activity生命周期的控制，
 * 因此，需要创建一个“标准module”，并且在主mudlue中通过这个类来将上下文传入，在插件module中进行控制
 * 版本
 */
public interface PayInterfaceActivity {
    public void attach(Activity proxyActivity);

    public void onCreate(Bundle savedInstanceState);

    public void onStart();

    public void onResume();

    public void onPause();

    public void onStop();

    public void onDestroy();

    public void onSaveInstanceState(Bundle outState);

    public boolean onTouchEvent(MotionEvent event);

    public void onBackPressed();
}
