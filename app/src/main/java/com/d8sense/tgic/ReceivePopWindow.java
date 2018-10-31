package com.d8sense.tgic;

import android.app.Activity;
import android.content.ClipboardManager;
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

import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * Created by Jason.z on 2018/9/13.
 * Copyright (c) 2016 BeikePay. All rights reserved.
 */
public class ReceivePopWindow extends PopupWindow {

    private Context mContext;

    private View view;

    private Button share_btn;

    public ImageView qrcode_image;
    public ImageView close_icon;
    public TextView addr_text;

    public ReceivePopWindow(Activity mContext, Wallet wallet,View.OnClickListener itemsOnClick) {

        this.mContext = mContext;

        this.view = LayoutInflater.from(mContext).inflate(R.layout.receive_pop_window, null);

        qrcode_image = (ImageView) view.findViewById(R.id.qrcode_image);

        Bitmap mBitmap = CodeUtils.createImage("ark:"+wallet.getAddress(), 300, 300, null);
        qrcode_image.setImageBitmap(mBitmap);

        share_btn =  (Button) view.findViewById(R.id.share_btn);
        close_icon =  (ImageView) view.findViewById(R.id.close_icon);
        addr_text = (TextView)view.findViewById(R.id.address_text);

        addr_text.setText(wallet.getAddress());

        addr_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(wallet.getAddress());
                Toast.makeText(mContext, "地址已复制到剪贴板", Toast.LENGTH_LONG).show();
            }
        });

        // 设置按钮监听
        share_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"暂不支持",Toast.LENGTH_SHORT).show();
                return;
//                if(ReceivePopWindow.this.isShowing()){
//                    ReceivePopWindow.this.dismiss();
//                }
            }
        });


        close_icon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(ReceivePopWindow.this.isShowing()){
                    ReceivePopWindow.this.dismiss();
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
