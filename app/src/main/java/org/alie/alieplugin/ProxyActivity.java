package org.alie.alieplugin;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.alie.pluginpaystander.PayInterfaceActivity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 这个activity的作用是作为一个壳来加载另一个activity的，因此，就需要得到另一个activity的类还有资源
 */
public class ProxyActivity extends Activity {

    //需要得到 插件apk的全类名
    private String className; // org.alie.taopiaopiao.MainActivity
    private PayInterfaceActivity payInterfaceActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        className = getIntent().getStringExtra("className");
        // 我们需要拿到 这个类的类型
        /**
         * 不能够使用Class.forName来拿到类类型，因为，这个插件apk还没有被安装
         * 到手机中，因此Class.forName 是根本找不到的，所以不能这么搞
         */
//        try {
//            Class.forName(className);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        try {
            // 下面三行代码是为了将 加载的类进行实例化
            Class activityClass = getClassLoader().loadClass(className);
            Constructor constructor = activityClass.getConstructor(new Class[]{});
            Object instance = constructor.newInstance(new Object[]{}); // 得到插件activity对象

            // 接下来我们就可以想办法调用插件activity中的方法了，那么这么做呢？
            // 方法1 反射：可以但是耗性能【这会儿可以反射了，因为 classLoader已经加载apk了】 不选择
            // 方法2 使用之前定义的标准

            // 这里可以直接进行强转的，因为 插件activity的基类中实现了PayInterfaceActivity
            payInterfaceActivity = (PayInterfaceActivity) instance;
            // 如果要进行传参，实例化一个bundle
            Bundle bundle = new Bundle();
            // 最终要的一点 是注入上下文
            /**
             * 这里的this 就是主mudule 的上下文，此方法就完成了上下文的注入
             */
            payInterfaceActivity.attach(this);
            payInterfaceActivity.onCreate(bundle);

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
    /**
     * 不能够使用Class.forName来拿到类类型，因为，这个插件apk还没有被安装
     * 到手机中，因此Class.forName 是根本找不到的，所以不能这么搞，那该怎么做呢？
     * 由于这个activity需要进行加载插件activity的类与资源，因此我们需要重写以下两个方法
     * 要想成功加载插件activity必须要重写 getClassLoader 与getResources
     */
    /**
     * 这个classLoader的作用者应该是 可以加载插件apk的classLoader,
     * 所以 这里肯定不能用super了，因此super代表的是主mudule的classLoader
     *
     * @return
     */
    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance().getDexClassLoader();
    }

    @Override
    public Resources getResources() {
        return PluginManager.getInstance().getResources();
    }

    @Override
    protected void onStart() {
        super.onStart();
        payInterfaceActivity.onStart();
    }

    @Override
    public void startActivity(Intent intent) {
        String className1=intent.getStringExtra("className");
        Intent intent1 = new Intent(this, ProxyActivity.class);
        intent1.putExtra("className", className1);
        super.startActivity(intent1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        payInterfaceActivity.onDestroy();
    }
}
