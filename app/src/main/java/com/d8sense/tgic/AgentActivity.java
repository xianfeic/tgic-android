package com.d8sense.tgic;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AgentActivity extends AppCompatActivity {

    private TabHost tabHost;
    private TabWidget tabWidget;
    private List<Delegate> delegates1 = new ArrayList<Delegate>();
    private List<Delegate> delegates2 = new ArrayList<Delegate>();
    MyBaseAdapter adapter=new MyBaseAdapter();
    private MyToolbar mMyToolbar;
    private Wallet currentWallet;
    private SweetAlertDialog pDialog;
    private AgentDetailPopWindow mAgentDetailPopWindow;

    public Handler uiHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    break;
                case 1:
                    Toast.makeText(AgentActivity.this,"发送成功",Toast.LENGTH_SHORT).show();
                    if(pDialog != null) {
                        pDialog.hide();
                    }
                    if(mAgentDetailPopWindow.isShowing()){
                        mAgentDetailPopWindow.dismiss();
                    }

                    adapter.notifyDataSetChanged();

                    break;
                case 2:
                    String error = msg.getData().getString("error");
                    Toast.makeText(AgentActivity.this,"发送失败:"+error,Toast.LENGTH_LONG).show();
                    if(pDialog != null) {
                        pDialog.hide();
                    }
                    break;
                case 3:
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
        setContentView(R.layout.activity_agent);

        Intent intent=getIntent();
        currentWallet = (Wallet) intent.getSerializableExtra("wallet");

        try {
            initView();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initView() throws IOException {

        LayoutInflater inflater = this.getLayoutInflater();

        View view1 = inflater.inflate(R.layout.agent_tab_tag, null);
        TextView txt1 = (TextView) view1.findViewById(R.id.tab_lable);
        txt1.setText("主动");

        View view2 = inflater.inflate(R.layout.agent_tab_tag, null);
        TextView txt2 = (TextView) view2.findViewById(R.id.tab_lable);
        txt2.setText("待命");

        tabHost =(TabHost)findViewById(R.id.tab_host);
        tabHost.setup();
        tabWidget = tabHost.getTabWidget();
        tabHost.addTab(tabHost.newTabSpec("one").setIndicator(view1).setContent(R.id.tab1));
        tabHost.addTab(tabHost.newTabSpec("two").setIndicator(view2).setContent(R.id.tab2));

        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                tabHost.setCurrentTabByTag(s);
                updateTab(tabHost);
                if(s.equals("1")) {
                    if(delegates1.size() == 0) {
                            AgentActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // refresh ui 的操作代码
                                    try {
                                        ArkService.getDelegates("0", new ArkService.GetDelegateCallback() {
                                            @Override
                                            public void success(List<Delegate> delegates) {
                                                delegates1 = delegates;
                                                AgentActivity.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        // refresh ui 的操作代码
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                });
                                            }
                                        });
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    }
                } else {
                    if(delegates2.size() == 0) {
                        try {
                            ArkService.getDelegates("51", new ArkService.GetDelegateCallback() {
                                @Override
                                public void success(List<Delegate> delegates) {
                                    delegates2 = delegates;
                                    AgentActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // refresh ui 的操作代码
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        mMyToolbar = findViewById(R.id.my_toolbar);

        mMyToolbar.setLeftTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ListView listView1=(ListView)findViewById(R.id.delegate_list_1);
        ListView listView2=(ListView)findViewById(R.id.delegate_list_2);


        listView1.setVerticalScrollBarEnabled(false);
        listView1.setFastScrollEnabled(false);

        listView2.setVerticalScrollBarEnabled(false);
        listView2.setFastScrollEnabled(false);


        try {
            ArkService.getDelegates("0",new ArkService.GetDelegateCallback() {
                @Override
                public void success(List<Delegate> delegates) {
                    delegates1 = delegates;
                    AgentActivity.this.runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             // refresh ui 的操作代码
                             listView1.setAdapter(adapter);
                             listView2.setAdapter(adapter);
                         }
                     });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        //创建自定义的MyAdapter对象

        //调用ListView的setAdapter()方法设置适配器
//        listView2.setAdapter(adapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
                Delegate delegate = delegates1.get(position);
                showAgentDetail(delegate);
            }
        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
                Delegate delegate = delegates2.get(position);
                showAgentDetail(delegate);
            }
        });

    }

    private void showAgentDetail(Delegate delegate) {
        mAgentDetailPopWindow = new AgentDetailPopWindow(delegate,currentWallet,new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        mAgentDetailPopWindow.showAtLocation(findViewById(R.id.agent_view), Gravity.CENTER, 0, 0);
    }

    private void updateTab(final TabHost tabHost)
    {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++)
        {
            View view = tabHost.getTabWidget().getChildAt(i);
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(R.id.tab_lable);
            tv.setTextSize(16);
            tv.setTypeface(Typeface.SERIF, 0); // 设置字体和风格
            if (tabHost.getCurrentTab() == i)
            {
                //选中
//                view.setBackground(getResources().getDrawable(R.drawable.tabhost_current));//选中后的背景
//                tv.setTextColor(this.getResources().getColorStateList(android.R.color.white));
            }
            else
            {
                //不选中
//                view.setBackground(getResources().getDrawable(R.drawable.tabhost_default));//非选择的背景
//                tv.setTextColor(this.getResources().getColorStateList(android.R.color.black));
            }
        }
    }

    public class MyBaseAdapter extends BaseAdapter {

        public MyBaseAdapter() {
            super();
        }

        /**
         * 剩下的问题就是依次实现BaseAdapter的这几个类方法就可以了
         */

        @Override
        public int getCount() {        //这个方法返回的是ListView的行数
            // TODO Auto-generated method stub
            int count = 0;

            if(tabHost.getCurrentTab() == 0) {
                count  = delegates1.size();
            } else {
                count =  delegates2.size();
            }

            return count;
        }
        @Override
        public Object getItem(int arg0) {      //这个方法没必要使用，可以用getItemId代替
            // TODO Auto-generated method stub
            return null;
        }
        @Override
        public long getItemId(int itemId) {     //点击某一行时会调用该方法，其形参由安卓系统提供
            // TODO Auto-generated method stub
            return itemId;
        }

        class ViewHolder{
            public TextView delegateIndex=null;
            public TextView delegateName=null;
            public TextView delegateRate=null;
            public TextView delegateStatus=null;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            // TODO Auto-generated method stub

            ViewHolder viewHolder=null;

            //注意convertView不是随意创建的，需要有LayoutInflater,根据list_item布局文件创建
            if (convertView == null) {
                viewHolder = new ViewHolder();

                convertView = getLayoutInflater().inflate(R.layout.delegate_list_item, null);
                viewHolder.delegateIndex = (TextView) convertView.findViewById(R.id.delegate_index);
                viewHolder.delegateName = (TextView) convertView.findViewById(R.id.delegate_name);
                viewHolder.delegateRate = (TextView) convertView.findViewById(R.id.delegate_rate);
                viewHolder.delegateStatus = (TextView) convertView.findViewById(R.id.delegate_status);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Delegate delegate;

            int index = position + 1;

            if(tabHost.getCurrentTab() == 0) {
                delegate =  delegates1.get(position);
            } else {
                index = 51 + position;
                delegate =  delegates2.get(position);
            }

            viewHolder.delegateIndex.setText(String.valueOf(index));
            viewHolder.delegateName.setText(delegate.username);
            viewHolder.delegateRate.setText(String.valueOf(delegate.productivity)+"%");

            if(delegate.publicKey!=null&&currentWallet.vote!=null&&currentWallet.vote.equals(delegate.publicKey)) {
                viewHolder.delegateStatus.setVisibility(View.VISIBLE);
            } else {
                viewHolder.delegateStatus.setVisibility(View.GONE);
            }

            return convertView;
        }
    }


    public class AgentDetailPopWindow extends PopupWindow {

        private View view;

        private Button vote_btn;
        public ImageView delegate_qrcode;
        public ImageView close_icon;
        public TextView delegate_address;
        public TextView delegate_username;
        public TextView delegate_status;
        public TextView delegate_approval;
        public TextView delegate_productivity;


        public AgentDetailPopWindow(Delegate delegate, Wallet wallet, View.OnClickListener itemsOnClick) {


            this.view = LayoutInflater.from(AgentActivity.this).inflate(R.layout.agent_detail_pop_window, null);

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

            if(delegate.publicKey!=null&&currentWallet.vote!=null&&currentWallet.vote.equals(delegate.publicKey)) {
               vote_btn.setText("取消投票(T1)");
            }

            // 设置按钮监听
            vote_btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {


                    pDialog = new SweetAlertDialog(AgentActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("发送中...");
                    pDialog.setCancelable(false);
                    pDialog.show();

                    Transaction transaction = new Transaction();
                    transaction.type = Types.VOTE;
                    transaction.amount = 0L;

                    if(delegate.publicKey!=null&&currentWallet.vote!=null&&currentWallet.vote.equals(delegate.publicKey)) {
                        String s = new String("-");
                        transaction.asset.votes.add(s.concat(delegate.publicKey));
                    } else {
                        String s = new String("+");
                        transaction.asset.votes.add(s.concat(delegate.publicKey));
                    }

                    transaction.fee = Fees.VOTE.getValue();

                    transaction.recipientId = wallet.getAddress();
                    transaction.timestamp = Slot.time();
                    String passphrase = Joiner.on(" ").join(wallet.getMnemonic());

                    transaction.sign(passphrase);

                    transaction.id = transaction.computeId();

                    ArkService.getInstance(AgentActivity.this).saveTranaction(transaction, new ArkService.SaveTransactionCallback(){
                        @Override
                        public void success(String id) {
                            uiHandler.sendEmptyMessage(1);
                            if(delegate.publicKey!=null&&currentWallet.vote!=null&&currentWallet.vote.equals(delegate.publicKey)) {
                                currentWallet.vote = null;
                            } else {
                                currentWallet.vote = delegate.publicKey;
                            }
                        }

                        @Override
                        public void failure(String error) {
                         Message msg = new Message();
                            msg.what = 2;
                            Bundle bundle = new Bundle();
                            bundle.putString("error",error);  //往Bundle中存放数据
                            msg.setData(bundle);//mes利用Bundle传递数据
                            uiHandler.sendMessage(msg);//用acti
                        }

                    });


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
            final Window dialogWindow = AgentActivity.this.getWindow();

            WindowManager m = AgentActivity.this.getWindowManager();
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

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode== KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;//不执行父类点击事件
        }
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }



}


