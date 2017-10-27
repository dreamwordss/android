package com.lbl.codek3demo.nfc;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.widget.Toast;

import com.lbl.codek3demo.R;

/**
 * author：libilang
 * time: 17/7/21 18:49
 * 邮箱：libi_lang@163.com
 * 读取nfc数据 打开系统的短信界面
 */

public class BlNfcOpenMessageActivity extends BaseBLNfcActivity {

    String mNfctagName = "com.android.mms";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_app);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //界面打开的时候会去截取nfc标签的数据 获取tag对象
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        //往设备里面写入我们的标签信息 以便第二次打开的时候获取
        writeNFCTag(detectedTag);
    }

        public void writeNFCTag(Tag tag) {
            if (tag == null) {
                return;
            }
            NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{NdefRecord
                    .createApplicationRecord(mNfctagName)});
            //转换成字节获得大小
            int size = ndefMessage.toByteArray().length;
            try {
                //判断NFC标签的数据类型（通过Ndef.get方法）
                Ndef ndef = Ndef.get(tag);
                //判断是否为NDEF标签
                if (ndef != null) {
                    ndef.connect();
                    //判断是否支持可写
                    if (!ndef.isWritable()) {
                        return;
                    }
                    //判断标签的容量是否够用
                    if (ndef.getMaxSize() < size) {
                        return;
                    }
                    //3.写入数据
                    ndef.writeNdefMessage(ndefMessage);
                    Toast.makeText(this, "写入数据成功", Toast.LENGTH_SHORT).show();
                } else { //当我们买回来的NFC标签是没有格式化的，或者没有分区的执行此步
                    //Ndef格式类
                    NdefFormatable format = NdefFormatable.get(tag);
                    //判断是否获得了NdefFormatable对象，有一些标签是只读的或者不允许格式化的
                    if (format != null) {
                        //连接
                        format.connect();
                        //格式化并将信息写入标签
                        format.format(ndefMessage);
                        Toast.makeText(this, "写入数据成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "写入数据失败", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
            }
        }
}
