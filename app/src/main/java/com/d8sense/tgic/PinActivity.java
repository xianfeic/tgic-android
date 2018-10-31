package com.d8sense.tgic;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.BarUtils;
import com.d8sense.tgic.services.ArkService;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.xnumberkeyboard.android.XNumberKeyboardView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PinActivity extends AppCompatActivity implements XNumberKeyboardView.IOnKeyboardListener,OnOtpCompletionListener {

    XNumberKeyboardView keyboardView;

    OtpView otpView;

    boolean isConfirm = false;
    boolean isValid = false;
    boolean isReset = false;

    String oldCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pin);

        BarUtils.setStatusBarAlpha(this,0);

        otpView = findViewById(R.id.otp_view);
        otpView.setOtpCompletionListener(this);

        keyboardView = (XNumberKeyboardView) findViewById(R.id.view_keyboard);
        keyboardView.setIOnKeyboardListener(this);
        keyboardView.shuffleKeyboard();

        if(getIntent().hasExtra("isConfirm")) {
            this.isConfirm = getIntent().getBooleanExtra("isConfirm",false);
        }

        if(getIntent().hasExtra("isValid")) {
            this.isValid = getIntent().getBooleanExtra("isValid",false);
        }

        if(getIntent().hasExtra("isReset")) {
            this.isReset = getIntent().getBooleanExtra("isReset",false);
        }

    }

    @Override
    public void onInsertKeyEvent(String text) {
        if(otpView.length()<6) {
            otpView.append(text);
        }
    }

    @Override
    public void onDeleteKeyEvent() {
        int start = otpView.length() - 1;
        if (start >= 0) {
            otpView.getText().delete(start, start + 1);
        }
    }

    @Override
    public void onOtpCompleted(String otp) {

        String pinCode = otpView.getText().toString();

        if(this.isValid) {
            String localPinCode = ArkService.getInstance(PinActivity.this).getPinCode();

            if(!pinCode.equals(localPinCode)) {
                Toast.makeText(this,"PIN码输入错误", Toast.LENGTH_SHORT).show();
                return;
            } else {
                this.isValid = false;

                if(this.isReset) {
                    otpView.setText("");
                    TextView tipText = (TextView)findViewById(R.id.pin_tip);
                    tipText.setText("请输入新的PIN码");
                    return;
                }
            }
        }

        // 验证PIN code
        if(this.isConfirm) {
            // 第一次输入
            if(oldCode == null) {
                oldCode = pinCode;
                otpView.setText("");
                TextView tipText = (TextView)findViewById(R.id.pin_tip);
                tipText.setText("请再次输入PIN码");
                return;
            } else {

                if (!pinCode.equals(oldCode)) {
                    Toast.makeText(this,"两次PINCODE不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }


        Intent intent = new Intent();

        intent.putExtra("pinCode", pinCode);

        setResult(2, intent);

        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode== KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;//不执行父类点击事件
        }
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }


}
