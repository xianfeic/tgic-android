package com.d8sense.tgic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.BarUtils;
import com.d8sense.tgic.services.ArkService;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoginActivity extends Activity {

    private Button loginBtn;

    private int PIN_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        BarUtils.setStatusBarAlpha(this,0);

        loginBtn = (Button)findViewById(R.id.login_button);

        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,PinActivity.class);
                intent.putExtra("isConfirm", true);
                startActivityForResult(intent,PIN_REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PIN_REQUEST_CODE) {

            String pinCode = data.getStringExtra("pinCode");

            if(pinCode != null) {
                ArkService.getInstance(this).savePinCode(pinCode);
                ArkService.getInstance(this).setLogin(true);

                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }


}
