package com.lbl.codek3demo;

import android.app.Application;
import android.content.Context;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

/**
 * author：libilang
 * time: 17/7/31 11:40
 * 邮箱：libi_lang@163.com
 */

public class application extends Application {

    Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);

        //初始化
        EMClient.getInstance().init(mContext, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);

//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(10000L, TimeUnit.MILLISECONDS) //链接超时
//                .readTimeout(10000L, TimeUnit.MILLISECONDS) //读取超时
//                .build(); //其他配置
//        OkHttpUtils.initClient(okHttpClient);
    }

}


