package com.d8sense.tgic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.BarUtils;
import com.d8sense.tgic.services.ArkService;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity {

    int[] imgs = { R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        BarUtils.setStatusBarAlpha(this,0);

        setContentView(R.layout.activity_guide);

        GuideView guideView = (GuideView) findViewById(R.id.guide_view);

        List<ImageView> mIvs = new ArrayList<ImageView>();
        for(int i = 0; i < imgs.length; i++){
            ImageView iv = new ImageView(getApplicationContext());
            //加载图片资源
            //iv.setImageResource(mImgs[i]);
//            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            scaleImage(this,iv,imgs[i]);
            mIvs.add(iv);
        }
        // 设置数据
        guideView.setImgs(mIvs);

        // 给开始体验按钮设置监听
        guideView.setOnStartExpClickListener(new GuideView.OnStartExpClickListener() {

            @Override
            public void clickStartExp(Button button) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ArkService.getInstance(GuideActivity.this).setGuide(true);

                        Intent intent = new Intent(GuideActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private  void scaleImage(final Activity activity, final View view, int drawableResId) {

        // 获取屏幕的高宽
        Point outSize = new Point();
        activity.getWindow().getWindowManager().getDefaultDisplay().getSize(outSize);

        // 解析将要被处理的图片
        Bitmap resourceBitmap = BitmapFactory.decodeResource(activity.getResources(), drawableResId);

        if (resourceBitmap == null) {
            return;
        }

        // 开始对图片进行拉伸或者缩放

        // 使用图片的缩放比例计算将要放大的图片的高度
        int bitmapScaledHeight = Math.round(resourceBitmap.getHeight() * outSize.x * 1.0f / resourceBitmap.getWidth());

        // 以屏幕的宽度为基准，如果图片的宽度比屏幕宽，则等比缩小，如果窄，则放大
        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(resourceBitmap, outSize.x, bitmapScaledHeight, false);

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                // 当UI绘制完毕，我们对图片进行处理
                int viewHeight = view.getMeasuredHeight();

                // 计算将要裁剪的图片的顶部以及底部的便宜量
                int offset = (scaledBitmap.getHeight() - viewHeight) / 2;

                // 对图片以中心进行裁剪，裁剪出的图片就是非常适合做引导页的图片了
                Bitmap finallyBitmap = Bitmap.createBitmap(scaledBitmap, 0, offset, scaledBitmap.getWidth(),
                        scaledBitmap.getHeight() - offset * 2);

                view.setBackground(new BitmapDrawable(activity.getResources(), finallyBitmap));
            }
        });
    }

}
