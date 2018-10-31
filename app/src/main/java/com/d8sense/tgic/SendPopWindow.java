package com.d8sense.tgic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * Created by Jason.z on 2018/9/13.
 * Copyright (c) 2016 BeikePay. All rights reserved.
 */
public class SendPopWindow extends PopupWindow {

    private Context mContext;

    private View view;

    private Button send_btn;

    public ImageView qrcode_image;
    public ImageView close_icon;

    public EditText addr_text;

    public int REQUEST_CODE = 100;

    public SendPopWindow(Activity mContext, View.OnClickListener itemsOnClick) {

        this.mContext = mContext;

        this.view = LayoutInflater.from(mContext).inflate(R.layout.send_pop_window, null);

        qrcode_image = (ImageView) view.findViewById(R.id.qrcode_image);
        addr_text = (EditText) view.findViewById(R.id.add);
        send_btn =  (Button) view.findViewById(R.id.send_btn);
        close_icon =  (ImageView) view.findViewById(R.id.close_icon);

        // 设置按钮监听
        send_btn.setOnClickListener(itemsOnClick);
        close_icon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(SendPopWindow.this.isShowing()){
                    SendPopWindow.this.dismiss();
                }
            }
        });

        ImageView scan_icon = (ImageView) view.findViewById(R.id.scan_icon);


        scan_icon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CaptureActivity.class);
                mContext.startActivityForResult(intent, REQUEST_CODE);
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
