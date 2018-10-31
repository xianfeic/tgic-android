package com.d8sense.tgic;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.d8sense.tgic.ark.core.TransactionType;
import com.d8sense.tgic.ark.crypto.enums.Fees;
import com.d8sense.tgic.ark.crypto.enums.Types;
import com.d8sense.tgic.ark.crypto.identities.Address;
import com.d8sense.tgic.ark.crypto.transactions.Serializer;
import com.d8sense.tgic.ark.crypto.transactions.Transaction;
import com.d8sense.tgic.ark.crypto.utils.Slot;
import com.d8sense.tgic.services.ArkService;
import com.google.common.base.Joiner;
import com.jaeger.library.StatusBarUtil;
import com.longsh.optionframelibrary.OptionBottomDialog;
//import com.unistrong.yang.zb_permission.ZbPermission;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class WalletDetailActivity extends BaseActivity {

    private MyToolbar mMyToolbar;
    private static Context mContext;
    public ArrayList<HashMap<String,Object>> list=null;
    HashMap<String,Object> map=null;
    ListView listView=null;
    public static Wallet wallet;
    public List<Transaction> transactions = new ArrayList<Transaction>();
    public int REQUEST_CODE = 100;
    private static SendPopWindow mSendPopWindow;
    private static MyBaseAdapter myBaseAdapter;
    private static SweetAlertDialog pDialog;
    private static TextView balanceText;
    private TextView addressText;

    private static final int MY_PERMISSION_REQUEST_CODE = 10000;

    public static Handler uiHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    myBaseAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    Toast.makeText(mContext,"发送成功",Toast.LENGTH_SHORT).show();

                    if(pDialog != null) {
                        pDialog.hide();
                    }

                    if(mSendPopWindow != null){
                        mSendPopWindow.dismiss();
                    }

//                    String id = msg.getData().getString("id");
//
//                    if(id != null) {
//                        Intent intent = new Intent(WalletDetailActivity.this,TransDetailActivity.class);
//                        Bundle bundle=new Bundle();
//                        bundle.putString("id", id);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                    }

                    break;
                case 2:
                    String error = msg.getData().getString("error");
                    Toast.makeText(mContext,"发送失败:"+error,Toast.LENGTH_LONG).show();
                    if(pDialog != null) {
                        pDialog.hide();
                    }
                    break;
                case 3:
                    balanceText.setText("T "+ String.format("%.1f",wallet.balance/(double)100000000L));
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

        mContext = this;

        setContentView(R.layout.activity_wallet_detail);
        StatusBarUtil.setColor(this,R.attr.colorPrimary);

        Intent intent=getIntent();
        wallet = (Wallet) intent.getSerializableExtra("wallet");

        balanceText = (TextView)findViewById(R.id.balance_text);
        String balance = "T "+ String.format("%.1f",wallet.balance/(double)100000000L);
        balanceText.setText(balance);
        addressText = (TextView)findViewById(R.id.address_text);
        addressText.setText(wallet.getAddress());

        addressText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(wallet.getAddress());
                Toast.makeText(WalletDetailActivity.this, "地址已复制到剪贴板", Toast.LENGTH_LONG).show();
            }
        });

        try {
            initData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMyToolbar = findViewById(R.id.my_toolbar);

        mMyToolbar.setLeftTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mMyToolbar.setRightTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> stringList = new ArrayList<String>();
                stringList.add("注册/委托");
                stringList.add("代表");
                stringList.add("设置标签");
                stringList.add("移除钱包");

                final OptionBottomDialog optionBottomDialog = new OptionBottomDialog(WalletDetailActivity.this, stringList);
                optionBottomDialog.setItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if(position == 0 ) {
                            optionBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {

                                @Override
                                public void onDismiss() {
                                    EntrustedPopWindow entrustedPopWindow = new EntrustedPopWindow(wallet,new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    });

                                    entrustedPopWindow.showAtLocation(findViewById(R.id.wallet_detail_view), Gravity.CENTER, 0, 0);
                                }
                            });

                            optionBottomDialog.dismiss();

                        } else if(position == 1) {
                            optionBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {

                                @Override
                                public void onDismiss() {
                                    Intent intent = new Intent(WalletDetailActivity.this,AgentActivity.class);
                                    Bundle bundle=new Bundle();
                                    bundle.putSerializable("wallet", wallet);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });

                            optionBottomDialog.dismiss();

                        } else if(position == 2) {

                            optionBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {

                                @Override
                                public void onDismiss() {
                                    SetTagPopWindow setTagPopWindow =  new SetTagPopWindow(WalletDetailActivity.this,new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    });

                                    setTagPopWindow.showAtLocation(findViewById(R.id.wallet_detail_view), Gravity.CENTER, 0, 0);
                                }
                            });

                            optionBottomDialog.dismiss();

                        } else if(position == 3) {
                            optionBottomDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {

                                @Override
                                public void onDismiss() {
                                    removeWallet();
                                }
                            });

                            optionBottomDialog.dismiss();
                        } else if(position == 4) {
                            optionBottomDialog.dismiss();
                        }
                    }
                });
            }
        });

        Drawable drawable1 = getResources().getDrawable(R.mipmap.send);
        drawable1.setBounds(0,0,40,40);
        Button sendBtn = (Button)findViewById(R.id.send_btn);
        sendBtn.setCompoundDrawables(drawable1,null,null,null);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSendPopWindow = new SendPopWindow();

                FragmentManager fragmentManager = getFragmentManager();

                mSendPopWindow.show(fragmentManager,"mSendPopWindow");

//                mSendPopWindow.showAtLocation(findViewById(R.id.wallet_detail_view), Gravity.CENTER, 0, 0);
            }
        });

        Drawable drawable2 = getResources().getDrawable(R.mipmap.receive);
        drawable2.setBounds(0,0,40,40);
        final Button receiveBtn = (Button)findViewById(R.id.receive_btn);
        receiveBtn.setCompoundDrawables(drawable2,null,null,null);

        receiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReceivePopWindow receivePopWindow = new ReceivePopWindow(WalletDetailActivity.this,wallet,new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {

                    }
                });

                receivePopWindow.showAtLocation(findViewById(R.id.wallet_detail_view), Gravity.CENTER, 0, 0);
            }
        });

        listView=(ListView)findViewById(R.id.trans_list);
        TextView textTip = (TextView)findViewById(R.id.text_tip);

        listView.setVerticalScrollBarEnabled(false);
        listView.setFastScrollEnabled(false);
        //创建自定义的MyAdapter对象
        myBaseAdapter=new MyBaseAdapter();

        //调用ListView的setAdapter()方法设置适配器
        listView.setAdapter(myBaseAdapter);
        listView.setEmptyView(textTip);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
//                Intent intent = new Intent(WalletDetailActivity.this,TransDetailActivity.class);
//                startActivity(intent);
            }
        });


    }

    @Override
    public  void onResume(){
        super.onResume();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1; //0.0-1.0
        getWindow().setAttributes(lp);

        try {
            initData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeWallet() {
        new SweetAlertDialog(WalletDetailActivity.this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("确认删除钱包?")
            .setContentText("你的钱包将会将会被移除，请在删除前确认备好你的助记词，你需要使用他才能恢复钱包。")
            .setConfirmText("确认删除")
            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    if(ArkService.getInstance(WalletDetailActivity.this).removeWallet(wallet)) {
                        sDialog.setTitleText("删除成功!")
                                .setContentText("")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismiss();
                                        Intent intent = new Intent(WalletDetailActivity.this,MainActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    } else {
                        sDialog.setTitleText("删除失败!")
                                .setContentText("")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                })
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                }
            })
            .show();

    }

    private void initData() throws IOException {

        ArkService.getWallet(wallet, new ArkService.GetWalletCallback() {
            @Override
            public void success(Long balance,String username) {
               wallet.balance = balance;
//               wallet.username = username;
               ArkService.getInstance(WalletDetailActivity.this).addWallet(wallet);
                uiHandler.sendEmptyMessage(3);
            }

        });

        ArkService.getWalletVote(wallet, new ArkService.GetWalletVoteCallback() {
            @Override
            public void success(String vote) {
                wallet.vote = vote;
                ArkService.getInstance(WalletDetailActivity.this).addWallet(wallet);
            }

        });

        ArkService.getTransactions(wallet, new ArkService.GetTransactionsCallback() {
            @Override
            public void success(List<Transaction> trans) {
                transactions = trans;
                uiHandler.sendEmptyMessage(0);
            }
            @Override
            public void failure(String error) {
                Toast.makeText(WalletDetailActivity.this,error,Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if(result != null) {
                        String str[] = result.split("\\:");
                        if(str[1].toString() != null) {
                            mSendPopWindow.setAddrText(str[1].toString());
                        } else {
                            Toast.makeText(WalletDetailActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(WalletDetailActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(WalletDetailActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
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
            return transactions.size();
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
            public TextView transTitle=null;
            public TextView transAddr=null;
            public ImageView transType=null;
            public TextView transFee=null;

        }
        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            // TODO Auto-generated method stub

            ViewHolder viewHolder=null;

            //注意convertView不是随意创建的，需要有LayoutInflater,根据list_item布局文件创建
            if (convertView == null) {
                viewHolder = new ViewHolder();

                convertView = getLayoutInflater().inflate(R.layout.trans_list_item, null);
                viewHolder.transTitle = (TextView) convertView.findViewById(R.id.trans_title);
                viewHolder.transAddr = (TextView) convertView.findViewById(R.id.trans_address);
                viewHolder.transFee = (TextView) convertView.findViewById(R.id.trans_fee);
                viewHolder.transType = (ImageView) convertView.findViewById(R.id.trans_icon);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Transaction transaction = transactions.get(position);
            if(transaction.type.equals(Types.TRANSFER)) {
                if(transaction.recipientId.equals(wallet.getAddress())) {
                    viewHolder.transTitle.setText("接收到");
                    String address = Address.fromPublicKey(transaction.senderPublicKey,50);
                    viewHolder.transAddr.setText(address);
                    viewHolder.transFee.setText("+"+String.format("%.1f",(transaction.amount+transaction.fee)/(double)100000000L)+" TGI");

                } else {
                    viewHolder.transTitle.setText("发送到");
                    viewHolder.transAddr.setText(transaction.recipientId);
                    viewHolder.transType.setImageResource(R.drawable.ic_call_missed_outgoing);
                    viewHolder.transFee.setText("-"+String.format("%.1f",(transaction.amount+transaction.fee)/(double)100000000L)+" TGI");

                }
            } else if(transaction.type.equals(Types.DELEGATE_REGISTRATION)) {
                viewHolder.transTitle.setText("注册委托");
                viewHolder.transAddr.setText("");
                viewHolder.transFee.setText("-"+String.format("%.1f",(transaction.amount+transaction.fee)/(double)100000000L)+" TGI");
            } else if(transaction.type.equals(Types.VOTE)) {
                viewHolder.transTitle.setText("投票");
                viewHolder.transAddr.setText("");
                viewHolder.transFee.setText("-"+String.format("%.1f",(transaction.amount+transaction.fee)/(double)100000000L)+" TGI");
            }


            return convertView;
        }
    }


    public static class SendPopWindow extends DialogFragment {

        private Button send_btn;

        public ImageView qrcode_image;
        public ImageView close_icon;

        public EditText addr_text;
        public EditText num_text;
        private TextView send_all_text;
        private TextView smart_bridge_text;

        private View mLayout;



            // 设置外部可点击
//            this.setOutsideTouchable(true);

        /* 设置弹出窗口特征 */
            // 设置视图
//            this.setContentView(this.view);
//            this.setOutsideTouchable(true);
//            this.setBackgroundDrawable(new BitmapDrawable());

            // 设置弹出窗体的宽和高
          /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
//            final Window dialogWindow = mContext.getWindow();
//
//            WindowManager m = mContext.getWindowManager();
//            Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
//            WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//            p.alpha = 0.2f;
//
//            dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//            dialogWindow.setAttributes(p);

//            this.setOnDismissListener(new PopupWindow.OnDismissListener() {
//
//                @Override
//                public void onDismiss() {
//                    WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//                    p.alpha = 1f;
//
//                    dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//                    dialogWindow.setAttributes(p);
//                }
//            });

//            this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
//            this.setWidth((int) (d.getWidth() * 0.8));

            // 设置弹出窗体可点击
//            this.setFocusable(true);


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mLayout = LayoutInflater.from((WalletDetailActivity)getActivity()).inflate(R.layout.send_pop_window, container,false);
//            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//            getDialog().getWindow().setBackgroundDrawable(getResources().getColor(R.color.black_overlay));

            //点击阴影消失
//            mLayout.setOnTouchListener(new View.OnTouchListener() {
//                public boolean onTouch(View v, MotionEvent event) {
////                    int top = mLayout.findViewById(R.id.sl_comm_bot).getTop();
////                    int y = (int) event.getY();
////                    if (event.getAction() == MotionEvent.ACTION_UP) {
////                        if (y < top) {
////                            dismiss();
////                        }
////                    }
//                    dismiss();
//                    return true;
//                }
//            });

            addr_text = (EditText) mLayout.findViewById(R.id.address_text);
            num_text = (EditText) mLayout.findViewById(R.id.num_text);
            send_btn =  (Button) mLayout.findViewById(R.id.send_btn);
            smart_bridge_text =  (Button) mLayout.findViewById(R.id.send_btn);

            close_icon =  (ImageView) mLayout.findViewById(R.id.close_icon);
            send_all_text = (TextView)mLayout.findViewById(R.id.send_all_text);

            addr_text.setTextIsSelectable(true);
            addr_text.setFocusable(true);
            addr_text.setFocusableInTouchMode(true);

            // 设置按钮监听
            send_btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(addr_text.getText().toString() == null || addr_text.getText().toString().length() < 32) {
                        Toast.makeText((WalletDetailActivity)getActivity(),"地址不合法",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(!Address.validate(addr_text.getText().toString(),50)) {
                        Toast.makeText((WalletDetailActivity)getActivity(),"钱包地址不合法",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(num_text.getText().toString() == null || num_text.getText().toString().length() == 0) {
                        Toast.makeText((WalletDetailActivity)getActivity(),"数量不可为空",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    pDialog = new SweetAlertDialog((WalletDetailActivity)getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("发送中...");
                    pDialog.setCancelable(false);
                    pDialog.show();

                    Transaction transaction = new Transaction();
                    transaction.type = Types.TRANSFER;
                    transaction.amount = Long.parseLong(num_text.getText().toString())*100000000;
                    transaction.fee = Fees.TRANSFER.getValue();
                    transaction.recipientId = addr_text.getText().toString();
                    transaction.timestamp = Slot.time();
                    String passphrase = Joiner.on(" ").join(wallet.getMnemonic());

                    transaction.sign(passphrase);

                    transaction.id = transaction.computeId();

                    ArkService.getInstance((WalletDetailActivity)getActivity()).saveTranaction(transaction, new ArkService.SaveTransactionCallback(){
                        @Override
                        public void success(String id) {
                            Message msg = new Message();
                            msg.what = 1;
                            Bundle bundle = new Bundle();
                            bundle.putString("id",id);  //往Bundle中存放数据
                            msg.setData(bundle);//mes利用Bundle传递数据
                            uiHandler.sendMessage(msg);
                        }

                        @Override
                        public void failure(String error) {
                            Message msg = new Message();
                            msg.what = 2;
                            Bundle bundle = new Bundle();
                            bundle.putString("error",error);  //往Bundle中存放数据
                            msg.setData(bundle);//mes利用Bundle传递数据
                            uiHandler.sendMessage(msg);//用activity中的handler发送消息
                        }

                    });
                }
            });
            close_icon.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    dismiss();
//                    if(SendPopWindow.this.isShowing()){
//                        SendPopWindow.this.dismiss();
//                    }
                }
            });

            ImageView scan_icon = (ImageView) mLayout.findViewById(R.id.scan_icon);

            scan_icon.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    boolean isPermission = checkSelfPermissionAll((WalletDetailActivity)getActivity(),new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO});
                    if (!isPermission) {
                        ActivityCompat.requestPermissions((WalletDetailActivity)getActivity(), new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO}, MY_PERMISSION_REQUEST_CODE);
                    }



//                    ZbPermission.with(WalletDetailActivity.this)
//                            .addRequestCode(100)
//                            .permissions(Manifest.permission_group.CAMERA)
//                            .request(new ZbPermission.ZbPermissionCallback() {
//                                @Override
//                                public void permissionSuccess(int requestCode) {
//
//                                }
//
//                                @Override
//                                public void permissionFail(int requestCode) {
//                                    Toast.makeText(WalletDetailActivity.this, "请求权限失败" , Toast.LENGTH_SHORT).show();
//                                }
//                            });

                }
            });

            send_all_text.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // TODO 获取钱包数量
                    num_text.setText(String.format("%.1f",wallet.balance/(double)100000000L));
                }
            });


            return mLayout;
        }

        public void setAddrText(String text) {
            if(SendPopWindow.this.addr_text != null) {
                SendPopWindow.this.addr_text.setText(text);
            }
        }
    }

    private static boolean checkSelfPermissionAll(Activity activity,String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE: //拨打电话
                if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {//失败
                    Toast.makeText(this,"请允许权限后再试",Toast.LENGTH_SHORT).show();
                } else {//成功
                    Intent intent = new Intent(WalletDetailActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);

                }
                break;
        }
    }

    public class SetTagPopWindow extends PopupWindow {

        private Context mContext;

        private View view;

        private Button tag_btn;
        public EditText tag_text;
        public ImageView close_icon;

        public SetTagPopWindow(Activity mContext, View.OnClickListener itemsOnClick) {

            this.mContext = mContext;

            this.view = LayoutInflater.from(mContext).inflate(R.layout.set_tag_pop_window, null);

            tag_btn =  (Button) view.findViewById(R.id.tag_btn);
            close_icon =  (ImageView) view.findViewById(R.id.close_icon);
            tag_text = (EditText)view.findViewById(R.id.tag_text);

            // 设置按钮监听
            tag_btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    if(tag_text.getText().toString() == null) {
                        Toast.makeText(WalletDetailActivity.this,"请输入钱包名称",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    wallet.username = tag_text.getText().toString();

                    if(!ArkService.getInstance(WalletDetailActivity.this).addWallet(wallet)) {
                        Toast.makeText(WalletDetailActivity.this,"设置失败",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(SetTagPopWindow.this.isShowing()){
                        SetTagPopWindow.this.dismiss();
                    }
                }
            });



            close_icon.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(SetTagPopWindow.this.isShowing()){
                        SetTagPopWindow.this.dismiss();
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


    public class EntrustedPopWindow extends PopupWindow {

        private View view;

        private Button add_btn;
        public ImageView close_icon;
        public EditText delegate_name;

        public EntrustedPopWindow(Wallet wallet,View.OnClickListener itemsOnClick) {

            this.view = LayoutInflater.from(WalletDetailActivity.this).inflate(R.layout.entrust_pop_window, null);

            delegate_name = (EditText) view.findViewById(R.id.delegate_name);
            add_btn =  (Button) view.findViewById(R.id.add_btn);
            close_icon =  (ImageView) view.findViewById(R.id.close_icon);

            // 设置按钮监听
            add_btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(delegate_name.getText() == null || delegate_name.getText().length() == 0) {
                        Toast.makeText(WalletDetailActivity.this,"请输入代表名称",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(wallet.balance < 2500000000L) {
                        Toast.makeText(WalletDetailActivity.this,"钱包余额不足",Toast.LENGTH_SHORT).show();
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

                    ArkService.getInstance(WalletDetailActivity.this).saveTranaction(transaction, new ArkService.SaveTransactionCallback(){
                        @Override
                        public void success(String id) {
                            wallet.username = delegate_name.getText().toString();
                            ArkService.getInstance(WalletDetailActivity.this).addWallet(wallet);
                            Message msg = new Message();
                            msg.what = 1;
                            Bundle bundle = new Bundle();
                            bundle.putString("id",id);  //往Bundle中存放数据
                            msg.setData(bundle);//mes利用Bundle传递数据
                            uiHandler.sendMessage(msg);
                        }

                        @Override
                        public void failure(String error) {
                         Message msg = new Message();
                            msg.what = 2;
                            Bundle bundle = new Bundle();
                            bundle.putString("error",error);  //往Bundle中存放数据
                            msg.setData(bundle);//mes利用Bundle传递数据
                            uiHandler.sendMessage(msg);
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
            final Window dialogWindow = WalletDetailActivity.this.getWindow();

            WindowManager m =  WalletDetailActivity.this.getWindowManager();
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
