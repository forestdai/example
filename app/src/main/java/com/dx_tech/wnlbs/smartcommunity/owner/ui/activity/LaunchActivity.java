package com.dx_tech.wnlbs.smartcommunity.owner.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.dx_tech.wnlbs.smartcommunity.owner.R;

/**
 * Created by 戴圣伟 on 0018 2017/10/18.
 * 启动页面
 * 数据加载，升级检测等业务逻辑在此实现
 */

public class LaunchActivity extends BaseActivity {

    private final int START_HOME_MSG = 0;
    private final int START_GUIDE_MSG = 1;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case START_HOME_MSG:
                    startHomeActivity();
                    break;
                case START_GUIDE_MSG:
                    startGuideActivity();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        mHandler.sendEmptyMessageDelayed(START_GUIDE_MSG, 3000);
    }

    private void startHomeActivity(){
        Intent it = new Intent(LaunchActivity.this, HomePageActivity.class);
        startActivity(it);
        finish();
    }

    private void startGuideActivity(){
        Intent it = new Intent(LaunchActivity.this, GuideActivity.class);
        startActivity(it);
        finish();
    }
}
