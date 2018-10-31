package com.d8sense.tgic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.longsh.optionframelibrary.OptionBottomDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WalletBackupActivity extends AppCompatActivity {
    private MyToolbar mMyToolbar;
    private GridView mGridView;
    private SimpleAdapter mAdapter;
    private List<Map<String, Object>> mDataList;
    private Wallet wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_backup);

        this.wallet = (Wallet)getIntent().getSerializableExtra("wallet");

        mMyToolbar = findViewById(R.id.my_toolbar);

        mMyToolbar.setRightTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(WalletBackupActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
            }
        });

        String[] from={"text"};

        int[] to={R.id.text};

        mGridView = (GridView) findViewById(R.id.gridview);
        initGridData(this.wallet.getMnemonic());

        mAdapter=new SimpleAdapter(this, mDataList, R.layout.word_grid_item, from, to);

        mGridView.setAdapter(mAdapter);


        Button button = (Button)findViewById(R.id.confirm_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletBackupActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initGridData(List<String> words) {

        mDataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i <words.size(); i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("text",words.get(i));
            mDataList.add(map);
        }
    }

}
