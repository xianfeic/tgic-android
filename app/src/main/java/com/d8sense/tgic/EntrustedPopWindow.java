package com.d8sense.tgic;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Toast;

import com.d8sense.tgic.ark.crypto.enums.Fees;
import com.d8sense.tgic.ark.crypto.enums.Types;
import com.d8sense.tgic.ark.crypto.transactions.Serializer;
import com.d8sense.tgic.ark.crypto.transactions.Transaction;
import com.d8sense.tgic.ark.crypto.utils.Slot;
import com.d8sense.tgic.services.ArkService;
import com.google.common.base.Joiner;

/**
 * Created by Jason.z on 2018/9/13.
 * Copyright (c) 2016 BeikePay. All rights reserved.
 */
public class EntrustedPopWindow extends PopupWindow {

    private Context mContext;

    private View view;

    private Button add_btn;
    public ImageView close_icon;
    public EditText delegate_name;

    public EntrustedPopWindow(Activity mContext, Wallet wallet,View.OnClickListener itemsOnClick) {

        this.mContext = mContext;

        this.view = LayoutInflater.from(mContext).inflate(R.layout.entrust_pop_window, null);

        delegate_name = (EditText) view.findViewById(R.id.delegate_name);
        add_btn =  (Button) view.findViewById(R.id.add_btn);
        close_icon =  (ImageView) view.findViewById(R.id.close_icon);

        // 设置按钮监听
        add_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(delegate_name.getText() == null || delegate_name.getText().length() == 0) {
                    Toast.makeText(mContext,"请输入代表名称",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(wallet.balance < 2500000000L) {
                    Toast.makeText(mContext,"钱包余额不足",Toast.LENGTH_SHORT).show();
                    return;
                }

                Transaction transaction = new Transaction();
                transaction.type = Types.DELEGATE_REGISTRATION;
                transaction.asset.delegate.username = delegate_name.getText().toString();
                transaction.asset.delegate.publicKey = wallet.getPublicKey();
                transaction.fee = Fees.DELEGATE_REGISTRATION.getValue();
                transaction.senderPublicKey = wallet.getPublicKey();
                transaction.timestamp = Slot.time();
                String passphrase = Joiner.on(" ").join(wallet.getMnemonic());

                transaction.sign(passphrase);

                transaction.id = transaction.computeId();

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

                if(EntrustedPopWindow.this.isShowing()){
                    EntrustedPopWindow.this.dismiss();
                }
            }
        });
        close_icon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(EntrustedPopWindow.this.isShowing()){
                    EntrustedPopWindow.this.dismiss();
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
