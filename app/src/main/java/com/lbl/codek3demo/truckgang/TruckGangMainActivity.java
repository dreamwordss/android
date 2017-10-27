package com.lbl.codek3demo.truckgang;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.lbl.codek3demo.R;

/**
 * author：libilang
 * time: 17/9/25 18:32
 * 邮箱：libi_lang@163.com
 */

public class TruckGangMainActivity extends Activity {
    View rootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_taobao_layout,
                null);
        setContentView(rootView);
    }


}
