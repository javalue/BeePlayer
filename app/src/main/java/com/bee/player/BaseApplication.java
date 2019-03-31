package com.bee.player;

import android.app.Application;

import com.umeng.commonsdk.UMConfigure;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调
         * 用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，
         * UMConfigure.init调用中appkey和channel参数请置为null）。
         */
        UMConfigure.init(this, "5ca04a9a61f5644637001aa9", "xiaomi", UMConfigure.DEVICE_TYPE_PHONE, "");
    }
}
