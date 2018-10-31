package com.d8sense.tgic;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.d8sense.tgic.services.ArkService;

import java.text.DecimalFormat;

public class SettingActivity extends BaseActivity {

    private MyToolbar mMyToolbar;

    private int PIN_REQUEST_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mMyToolbar = findViewById(R.id.my_toolbar);
        mMyToolbar.setLeftTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        final TextView cacheText = (TextView)findViewById(R.id.setting_cache_text);

        final CacheDiskUtils cache = CacheDiskUtils.getInstance();

        if(cache != null) {
            int cacheCount = cache.getCacheCount();
            if(cacheCount > 0) {
                DecimalFormat formater = new DecimalFormat("####.0");
                float mbsize = cacheCount / 1024f / 1024f;
                cacheText.setText(formater.format(mbsize) + "MB");
            }
        }

        TableRow contactRow = (TableRow)findViewById(R.id.setting_contact_row);
        contactRow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,ContactActivity.class);
                startActivity(intent);
            }
        });

        TableRow resetRow = (TableRow)findViewById(R.id.setting_reset_row);
        resetRow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,PinActivity.class);
                intent.putExtra("isConfirm", true);
                intent.putExtra("isValid", true);
                intent.putExtra("isReset", true);
                startActivityForResult(intent,PIN_REQUEST_CODE);
            }
        });

        TableRow cacheRow = (TableRow)findViewById(R.id.setting_cache_row);
        cacheRow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cache.clear();
                Toast.makeText(SettingActivity.this,"清理成功",Toast.LENGTH_SHORT).show();
                cacheText.setText("0MB");
            }
        });


        TableRow privateRow = (TableRow)findViewById(R.id.setting_private_row);
        privateRow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.tgichain.com/Privacy.txt");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        Button logoutBtn = (Button)findViewById(R.id.logout_button);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(SettingActivity.this,PinActivity.class);
//                intent.putExtra("isValid", true);
//                startActivityForResult(intent,PIN_REQUEST_CODE);
                finishAffinity();
                System.exit(0);
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
                Toast.makeText(SettingActivity.this,"重置成功",Toast.LENGTH_SHORT).show();
            }

        }
    }
}
