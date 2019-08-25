package org.alie.taopiaopiao;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class PluginService extends BaseService {
    private int i = 0;

    public PluginService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        doTest();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void doTest() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    Log.i(TAG, "run: " + (i++));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
