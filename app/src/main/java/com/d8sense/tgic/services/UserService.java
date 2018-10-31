package com.d8sense.tgic.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.d8sense.tgic.Wallet;

public class UserService extends Service {
    Context mContext;

    public UserService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static boolean isLogin() {
        return false;
    }

    public static boolean isNew() {
//        SharedPreferences sp = context.getSharedPreferences("save.himi", Context.MODE_PRIVATE);
//        boolean b = sp.getBoolean("isLogin", false);
        return false;
    }

    public static void addWallet(Wallet wallet,String mnenonic) {

    }
}
