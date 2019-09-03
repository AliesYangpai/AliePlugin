package org.alie.alieplugin;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import dalvik.system.DexClassLoader;

/**
 * Created by Alie on 2019/8/18.
 * 类描述 这个类的作用是，获取插件apk中的activity
 * 版本
 */
public class PluginManager {

    /**
     * 通过pakegeInfo来得到apk中所有的activity信息
     */
    private PackageInfo packageInfo;

    private Resources resources;
    private Context context;
    private DexClassLoader dexClassLoader;

    private volatile static PluginManager mInstance;

    public static PluginManager getInstance() {
        if (mInstance == null) {
            synchronized (PluginManager.class) {
                if (mInstance == null) {
                    mInstance = new PluginManager();
                }
            }
        }
        return mInstance;
    }


    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    /**
     * 加载一下目录，这个目录就是刚才 从sd导出到的一个安全路径
     *
     * @param context
     */
    public void loadPath(Context context) {
        File filesDir = context.getDir("testplugin", Context.MODE_PRIVATE);
        String name = "taopiaopiao-debug.apk";
        String filePath = new File(filesDir, name).getAbsolutePath();


        PackageManager packageManager = context.getPackageManager();
        packageInfo = packageManager.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);


        // 接下来我们需要拿到这个activity的名称，等信息，才能够使用它

        // 拿到插件activity之后，我们需要得到进行 宿主壳activity

        // 与网易云换肤类似 先来进行classLoaderd的实例化
        File dexOutFile = context.getDir("dex", Context.MODE_PRIVATE); // 配合DexClassLoader的构造方法
        /**
         * DexClassLoader(String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent)
         *   dexPath：可以是一个apk结尾的路径
         *   optimizedDirectory： 这是个缓存目录，因为虚拟机要加载apk的时候会讲apk缓存到缓存目录中
         *   librarySearchPath：依赖库的路径 可选，如果不指定，则依赖于dexPath
         *   parent：classLoader对象
         */
        dexClassLoader = new DexClassLoader(
                filePath,
                dexOutFile.getAbsolutePath(),
                null,
                context.getClassLoader());

        // 再来实例化resource对象,由于要得到AssetManager对象作为第一个参数，因此此处必须要进行反射

        AssetManager assetManager = null;
        try {
            assetManager = AssetManager.class.newInstance();
            Method addAssetPath = AssetManager.class.getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, filePath);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        resources = new Resources(assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
        parseReceivers(context, filePath);
    }

    private void parseReceivers(Context context, String apkPath) {
        // PackageParser pp = new PackageParser()；
        // pkg = pp.parsePackage(scanFile, parseFlags);
        // 这样搞肯定是不行的，所以 我们需要进行反射

        try {
            Class packageParserClass = Class.forName("android.content.pm.PackageParser");
            Method parsePackageMethod = packageParserClass.getDeclaredMethod("parsePackage", File.class, int.class);
            Object packageParser = packageParserClass.newInstance();
            // 这个packageObj 就是那个对象PackageParse.Packge对象
            Object packageObj = parsePackageMethod.invoke(packageParser, new File(apkPath), PackageManager.GET_ACTIVITIES);
            Field receiverField = packageObj.getClass().getDeclaredField("receivers");

            //拿到receivers  广播集合    app存在多个广播   集合  List<Activity>  name  ————》 ActivityInfo   className
            List receivers = (List) receiverField.get(packageObj);

            Class<?> componentClass = Class.forName("android.content.pm.PackageParser$Component");
            Field intentsField = componentClass.getDeclaredField("intents");

            // 调用generateActivityInfo 方法, 把PackageParser.Activity 转换成
            Class<?> packageParser$ActivityClass = Class.forName("android.content.pm.PackageParser$Activity");
//            generateActivityInfo方法
            Class<?> packageUserStateClass = Class.forName("android.content.pm.PackageUserState");
            Object defaltUserState= packageUserStateClass.newInstance();
            Method generateReceiverInfo = packageParserClass.getDeclaredMethod("generateActivityInfo",
                    packageParser$ActivityClass, int.class, packageUserStateClass, int.class);
            Class<?> userHandler = Class.forName("android.os.UserHandle");
            Method getCallingUserIdMethod = userHandler.getDeclaredMethod("getCallingUserId");
            int userId = (int) getCallingUserIdMethod.invoke(null);


            for (Object activity : receivers) {
                ActivityInfo info= (ActivityInfo) generateReceiverInfo.invoke(packageParser,  activity,0, defaltUserState, userId);
                BroadcastReceiver broadcastReceiver= (BroadcastReceiver) dexClassLoader.loadClass(info.name).newInstance();
                List<? extends IntentFilter> intents= (List<? extends IntentFilter>) intentsField.get(activity);
                for (IntentFilter intentFilter : intents) {
                    context.registerReceiver(broadcastReceiver, intentFilter);
                }
            }
            //generateActivityInfo
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public Resources getResources() {
        return resources;
    }

    public DexClassLoader getDexClassLoader() {
        return dexClassLoader;
    }
}
