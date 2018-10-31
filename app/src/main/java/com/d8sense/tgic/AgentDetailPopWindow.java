package com.d8sense.tgic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.d8sense.tgic.ark.crypto.enums.Fees;
import com.d8sense.tgic.ark.crypto.enums.Types;
import com.d8sense.tgic.ark.crypto.transactions.Serializer;
import com.d8sense.tgic.ark.crypto.transactions.Transaction;
import com.d8sense.tgic.ark.crypto.utils.Slot;
import com.d8sense.tgic.services.ArkService;
import com.google.common.base.Joiner;
import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * Created by Jason.z on 2018/10/3.
 * Copyright (c) 2016 BeikePay. All rights reserved.
 */
public class AgentDetailPopWindow extends PopupWindow {

    private Context mContext;

    private View view;

    private Button vote_btn;
    public ImageView delegate_qrcode;
    public ImageView close_icon;
    public TextView delegate_address;
    public TextView delegate_username;
    public TextView delegate_status;
    public TextView delegate_approval;
    public TextView delegate_productivity;




    public AgentDetailPopWindow(Activity mContext, Delegate delegate,Wallet wallet, View.OnClickListener itemsOnClick) {

        this.mContext = mContext;

        this.view = LayoutInflater.from(mContext).inflate(R.layout.agent_detail_pop_window, null);

        delegate_qrcode = (ImageView) view.findViewById(R.id.delegate_qrcode);

        Bitmap mBitmap = CodeUtils.createImage("ark:"+delegate.address, 300, 300, null);
        delegate_qrcode.setImageBitmap(mBitmap);

        delegate_address = (TextView)view.findViewById(R.id.delegate_address);
        delegate_address.setText(delegate.address);

        delegate_username = (TextView)view.findViewById(R.id.delegate_username);
        delegate_username.setText(delegate.username);

        delegate_status = (TextView)view.findViewById(R.id.delegate_status);
        String status = delegate.rate <= 51 ? "主动":"被动";
        delegate_status.setText(String.valueOf(delegate.rate)+"/"+status);

        delegate_approval = (TextView)view.findViewById(R.id.delegate_approval);
        delegate_approval.setText(String.valueOf(delegate.approval)+"%");

        delegate_productivity = (TextView)view.findViewById(R.id.delegate_productivity);
        delegate_productivity.setText(String.valueOf(delegate.productivity)+"%");


        vote_btn =  (Button) view.findViewById(R.id.vote_btn);
        close_icon =  (ImageView) view.findViewById(R.id.close_icon);

        // 设置按钮监听
        vote_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Transaction transaction = new Transaction();
                transaction.type = Types.VOTE;
                transaction.amount = 0L;
                transaction.fee = Fees.VOTE.getValue();
                transaction.recipientId = delegate.address;
                transaction.timestamp = Slot.time();
                String passphrase = Joiner.on(" ").join(wallet.getMnemonic());

                transaction.sign(passphrase);

                transaction.id = transaction.computeId();

                byte[] bytes = new Serializer().serialize(transaction);

                ArkService.getInstance(mContext).saveTranaction(transaction, new ArkService.SaveTransactionCallback(){
                    @Override
                    public void success(String id) {
//                        uiHandler.sendEmptyMessage(1);
                    }

                    @Override
                    public void failure(String error) {
//                        uiHandler.sendEmptyMessage(1);
                    }

                });

//                if(ReceivePopWindow.this.isShowing()){
//                    ReceivePopWindow.this.dismiss();
//                }
            }
        });


        close_icon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(AgentDetailPopWindow.this.isShowing()){
                    AgentDetailPopWindow.this.dismiss();
                }
            }
        });

        // 设置外部可点击
        this.setOutsideTouchable(true);

        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());

        // 设置弹出窗体的宽和高
          /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        final Window dialogWindow = mContext.getWindow();

        WindowManager m = mContext.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.alpha = 0.2f;

        dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogWindow.setAttributes(p);

        this.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
                p.alpha = 1f;

                dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                dialogWindow.setAttributes(p);
            }
        });

        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth((int) (d.getWidth() * 0.8));

        // 设置弹出窗体可点击
        this.setFocusable(true);

    }
}
