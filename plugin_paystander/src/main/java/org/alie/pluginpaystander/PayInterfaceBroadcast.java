package org.alie.pluginpaystander;

import android.content.Context;
import android.content.Intent;


/**
 * Created by Alie on 2019/8/18.
 * 类描述  制定标准的moudle中的接口，由于刚才提到，这里是进行广播的设置
 */
public interface PayInterfaceBroadcast {

    public void attach(Context context);

    public void onReceive(Context context, Intent intent);

}
