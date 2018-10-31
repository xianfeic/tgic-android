package com.d8sense.tgic;

import java.util.List;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.BarUtils;
import com.d8sense.tgic.services.ArkService;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class BaseActivity extends AppCompatActivity {
    protected boolean isActive = true;
    private int LOGIN_REQUEST_CODE = 300;
    private MyReceiver receiver;
    public static final String SYSTEM_EXIT = "com.example.exitsystem.system_exit";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BarUtils.setStatusBarAlpha(this,0);

        IntentFilter filter = new IntentFilter();
        filter.addAction(SYSTEM_EXIT);
        receiver = new MyReceiver();
        this.registerReceiver(receiver, filter);

        if(ArkService.getInstance(this).isNew()) {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        } else {
            if(!ArkService.getInstance(BaseActivity.this).isLogin()) {
                Intent intent = new Intent(this,PinActivity.class);
                intent.putExtra("isValid", true);
                startActivityForResult(intent,LOGIN_REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

        if (!isAppOnForeground()) {
            //app 进入后台
            isActive = false;
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (!isActive) {
//        app 从后台唤醒，进入前台
            isActive = true;
            Intent intent = new Intent(this,PinActivity.class);
            intent.putExtra("isValid", true);
            startActivity(intent);
        }
    }

    protected void onDestroy() {
        //记得取消广播注册
        this.unregisterReceiver(receiver);
        super.onDestroy();
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == LOGIN_REQUEST_CODE) {

            String pinCode = data.getStringExtra("pinCode");

            if(pinCode != null) {
                ArkService.getInstance(BaseActivity.this).setLogin(true);
            }
        }
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }
}
