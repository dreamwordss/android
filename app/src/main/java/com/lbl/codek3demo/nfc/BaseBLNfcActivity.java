package com.lbl.codek3demo.nfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;

/**
 * author：libilang
 * time: 17/7/20 10:16
 * 邮箱：libi_lang@163.com
 * nfc 基础类
 * 当NFC标签靠近是作为接收
 * 1.子类需要在onCreate方法中做Activity初始化。
 * 2.子类需要在onNewIntent方法中进行NFC标签相关操作。
 *   当launchMode设置为singleTop时，第一次运行调用onCreate方法，
 *   第二次运行将不会创建新的Activity实例，将调用onNewIntent方法
 *   所以我们获取intent传递过来的Tag数据操作放在onNewIntent方法中执行
 *   如果在栈中已经有该Activity的实例，就重用该实例(会调用实例的onNewIntent())
 *   只要NFC标签靠近就执行
 */

public class BaseBLNfcActivity extends AppCompatActivity {
    private NfcAdapter mBLNfcAdapter;
    private PendingIntent mPendingIntent;


    @Override
    protected void onStart() {
        super.onStart();
        mBLNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //截获NFC消息，通过PendingIntent调用窗口
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        //设置处理优于所有其他NFC的处理
        if (mBLNfcAdapter != null)
            mBLNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
    }

    @Override
    public void onPause() {
        super.onPause();
        //恢复默认状态
        if (mBLNfcAdapter != null)
            mBLNfcAdapter.disableForegroundDispatch(this);
    }

}
