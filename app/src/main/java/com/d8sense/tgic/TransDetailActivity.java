package com.d8sense.tgic;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.d8sense.tgic.ark.crypto.transactions.Transaction;
import com.d8sense.tgic.services.ArkService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TransDetailActivity extends AppCompatActivity {

    private MyToolbar mMyToolbar;
    private Transaction transaction;
    private TextView trans_type_text;
    private TextView trans_balance_text;
    private TextView trans_sender_text;
    private TextView trans_receipt_text;
    private TextView trans_date_text;
    private TextView trans_smartbridge_text;
    private TextView trans_id_text;
    private Button open_btn;

    public Handler uiHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    initView();
                    break;

                default:
                    break;

            }
            return false;
        }
    });



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_detail);

        mMyToolbar = findViewById(R.id.my_toolbar);

        mMyToolbar.setLeftTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        trans_type_text = (TextView)findViewById(R.id.trans_type_text);
        trans_balance_text = (TextView)findViewById(R.id.trans_balance_text);
        trans_sender_text = (TextView)findViewById(R.id.trans_sender_text);
        trans_receipt_text = (TextView)findViewById(R.id.trans_receipt_text);
        trans_date_text = (TextView)findViewById(R.id.trans_date_text);
        trans_smartbridge_text = (TextView)findViewById(R.id.trans_smartbridge_text);
        trans_id_text = (TextView)findViewById(R.id.trans_id_text);
        open_btn = (Button)findViewById(R.id.open_btn);

        Intent intent=getIntent();
        String id =  intent.getStringExtra("id");

        if(id != null) {
            try {
                ArkService.getTransaction(id, new ArkService.GetTransactionCallback() {
                    @Override
                    public void success(Transaction trans) {
                        transaction = trans;
                        uiHandler.sendEmptyMessage(1);
                    }
                    @Override
                    public void failure(String error) {
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        open_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                String url = "http://49.4.10.4:4200/transaction/"+id;
                intent.setData(Uri.parse(url));//Url 就是你要打开的网址
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent);
            }
        });

    }

    private void initView() {
        if(transaction != null) {
            trans_sender_text.setText(transaction.senderId);
            trans_receipt_text.setText(transaction.recipientId);
            trans_id_text.setText(transaction.id);

            trans_balance_text.setText(String.format("%.1f",(transaction.amount+transaction.fee)/(double)100000000)+" TGI");

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(transaction.timestamp*1000);
            trans_date_text.setText(date.toString());
        }
    }
}
