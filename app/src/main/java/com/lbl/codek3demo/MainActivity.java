package com.lbl.codek3demo;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lbl.codek3demo.adapter.MvpAdapter;
import com.lbl.codek3demo.camera.CameraActivity;
import com.lbl.codek3demo.data.MvpBean;
import com.lbl.codek3demo.scalldemo.StickListView;
import com.lbl.codek3demo.taobao.DemoActivity;
import com.lbl.codek3demo.taobao.PullDemoActivity;
import com.lbl.codek3demo.taobao.RecycleDemoActivity;
import com.lbl.codek3demo.taobao.TaobaoSearchDemoActivity;
import com.lbl.codek3demo.truckgang.TruckGangMainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * author：libilang
 * time: 17/7/18 17:24
 * 邮箱：libi_lang@163.com
 */
public class MainActivity extends AppCompatActivity {
    LinearLayout mTitle;
    RelativeLayout listviewMvplayout;
    ListView mvplistview;
    List<MvpBean> list = new ArrayList<>();
    MvpAdapter mvpAdapter;
    private static final int REQUEST_STREAM = 1;
    private static String[] PERMISSIONS_STREAM = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    boolean authorized = false;
    View headerView;
    TextView scallDemo, tianmaoDemo, recycleDemo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        initView();
        initDada();
        initListener();
        mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        Log.e("bilang", "mTouchSlop--" + mTouchSlop);
        //滑动监听
        showHideTitleBar(true);
        verifyPermissions();
    }

    float mFirstY, mCurrentY, mTouchSlop = 10;

    private void initListener() {
        findViewById(R.id.truck_gang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TruckGangMainActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.camrea).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.pulldemo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PullDemoActivity.class);
                startActivity(intent);
            }
        });
        scallDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, StickListView.class);
                startActivity(intent);
            }
        });
        tianmaoDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TaobaoSearchDemoActivity.class);
                startActivity(intent);
            }
        });
        recycleDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecycleDemoActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.demo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DemoActivity.class);
                startActivity(intent);
            }
        });
        mvplistview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mFirstY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCurrentY = event.getY();
                        if (mCurrentY - mFirstY > mTouchSlop / 2) {
                            // 下滑 显示titleBar
                            showHideTitleBar(true);
                        } else if (mFirstY - mCurrentY > mTouchSlop) {
                            // 上滑 隐藏titleBar
                            showHideTitleBar(false);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        });
    }

    private Animator mAnimatorTitle;
    private Animator mAnimatorTitlePage;
    private Animator mAnimatorContent;

    private void showHideTitleBar(boolean tag) {
        if (mAnimatorTitle != null && mAnimatorTitle.isRunning()) {
            mAnimatorTitle.cancel();
        }
        if (mAnimatorTitlePage != null && mAnimatorTitlePage.isRunning()) {
            mAnimatorTitlePage.cancel();
        }
        if (mAnimatorContent != null && mAnimatorContent.isRunning()) {
            mAnimatorContent.cancel();
        }
        if (tag) {
            mAnimatorTitle = ObjectAnimator.ofFloat(mTitle, "translationY", mTitle.getTranslationY(), 0);
//            mAnimatorTitlePage = ObjectAnimator.ofFloat(mTitlePage, "translationY", mTitlePage.getTranslationY(), 0);
            mAnimatorContent = ObjectAnimator.ofFloat(listviewMvplayout, "translationY", listviewMvplayout.getTranslationY(), getResources().getDimension(R.dimen.title_height));
            Log.e("bilang", "显示");
        } else {
            mAnimatorTitle = ObjectAnimator.ofFloat(mTitle, "translationY", mTitle.getTranslationY(), -mTitle.getHeight());
//            mAnimatorTitlePage = ObjectAnimator.ofFloat(mTitlePage, "translationY", mTitlePage.getTranslationY(), -mTitlePage.getHeight());
            mAnimatorContent = ObjectAnimator.ofFloat(listviewMvplayout, "translationY", listviewMvplayout.getTranslationY(), 0);
            Log.e("bilang", "隐藏");
        }
        mAnimatorTitle.start();
//        mAnimatorTitlePage.start();
        mAnimatorContent.start();

    }

    private void initView() {
        scallDemo = (TextView) findViewById(R.id.asdasdasda);
        tianmaoDemo = (TextView) findViewById(R.id.tianmaossss);
        recycleDemo = (TextView) findViewById(R.id.recycle);
        mvplistview = (ListView) findViewById(R.id.listview_mvp);
        mTitle = (LinearLayout) findViewById(R.id.title);
        listviewMvplayout = (RelativeLayout) findViewById(R.id.listview_mvplayout);
//        headerView = LayoutInflater.from(MainActivity.this)
//                .inflate(R.layout.actionbar_search_activity, null);
//        mvplistview.addHeaderView(headerView);

    }

    MvpBean mvpBean;

    private void initDada() {
        mvpBean = new MvpBean();
        mvpBean.type = "image";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "text";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "video";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "imagetext";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "special";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "image";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "video";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "text";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "video";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "imagetext";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "video";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "imagetext";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "special";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "image";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "video";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "text";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "video";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "imagetext";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "video";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "imagetext";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "special";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "image";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "video";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "text";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "video";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "imagetext";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "video";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "imagetext";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "special";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "image";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "video";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "text";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "video";
        list.add(mvpBean);
        mvpBean = new MvpBean();
        mvpBean.type = "imagetext";
        list.add(mvpBean);
        mvpAdapter = new MvpAdapter(list, mvplistview, this);
        mvplistview.setAdapter(mvpAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void verifyPermissions() {
        int CAMERA_permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
        int RECORD_AUDIO_permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO);
        int WRITE_EXTERNAL_STORAGE_permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (CAMERA_permission != PackageManager.PERMISSION_GRANTED ||
                RECORD_AUDIO_permission != PackageManager.PERMISSION_GRANTED ||
                WRITE_EXTERNAL_STORAGE_permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    PERMISSIONS_STREAM,
                    REQUEST_STREAM
            );
            authorized = false;
        } else {
            authorized = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STREAM) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                authorized = true;
            }
        }
    }
}
