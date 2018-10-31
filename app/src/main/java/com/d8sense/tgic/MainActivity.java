package com.d8sense.tgic;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import com.d8sense.tgic.services.ArkService;
import com.jaeger.library.StatusBarUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.longsh.optionframelibrary.OptionBottomDialog;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends BaseActivity {

    private MyToolbar mMyToolbar;
    private SlidingMenu mMenu;
    public List<Wallet> wallets = new ArrayList<Wallet>();
    HashMap<String,Object> map=null;
    ListView listView=null;
    Mybaseadapter mybaseadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ZXingLibrary.initDisplayOpinion(this);


        StatusBarUtil.setColor(this,R.attr.colorPrimary);

        listView = (ListView)findViewById(R.id.wallet_list);
        wallets = ArkService.getInstance(this).getWallets();

        setTotalBalance();

        TextView text_tip = (TextView) findViewById(R.id.text_tip);

        listView.setVerticalScrollBarEnabled(false);
        listView.setFastScrollEnabled(false);
        //创建自定义的MyAdapter对象
        mybaseadapter=new Mybaseadapter();

        //调用ListView的setAdapter()方法设置适配器
        listView.setAdapter(mybaseadapter);
        listView.setEmptyView(text_tip);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
                Intent intent = new Intent(MainActivity.this,WalletDetailActivity.class);
                Wallet wallet = wallets.get(position);
                Bundle bundle=new Bundle();
                bundle.putSerializable("wallet", wallet);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mMenu = new SlidingMenu(this);
        mMenu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        mMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//        mMenu.setShadowWidthRes(R.dimen.shadow_width);
//        mMenu.setShadowDrawable(R.color.colorAccent);

        // 设置滑动菜单视图的宽度
        mMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        mMenu.setFadeDegree(0.35f);
        mMenu.toggle();
        /**
         * SLIDING_WINDOW will include the Title/ActionBar in the content
         * section of the SlidingMenu, while SLIDING_CONTENT does not.
         */
        mMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        mMenu.setMenu(R.layout.sliding_menu);

        mMyToolbar = findViewById(R.id.my_toolbar);

        mMyToolbar.setLeftTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenu.toggle();
            }
        });

        mMyToolbar.setRightTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> stringList = new ArrayList<String>();
                stringList.add("生成");
                stringList.add("导入");

                final OptionBottomDialog optionBottomDialog = new OptionBottomDialog(MainActivity.this, stringList);
                optionBottomDialog.setItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent;

                        if(position == 0 ) {
                            intent = new Intent(MainActivity.this,WalletAddActivity.class);
                            startActivity(intent);
                        } else if(position == 1) {
                            intent = new Intent(MainActivity.this,WalletImportActivity.class);
                            startActivity(intent);
                        } else if(position == 2) {
                            optionBottomDialog.dismiss();
                        }
                    }
                });
            }
        });

        TableRow row1 = (TableRow)findViewById(R.id.menu_row1);
        row1.setClickable(true);

        row1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mMenu.toggle();
            }
        });


        TableRow row2 = (TableRow)findViewById(R.id.menu_row2);
        row2.setClickable(true);

        row2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.setBackgroundColor(Color.GRAY);
                System.out.println("Row clicked: " + v.getId());

                //get the data you need
                Intent intent = new Intent(MainActivity.this,WaitActivity.class);
                startActivity(intent);
            }
        });

        TableRow row3 = (TableRow)findViewById(R.id.menu_row3);
        row3.setClickable(true);

        row3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.setBackgroundColor(Color.GRAY);
                System.out.println("Row clicked: " + v.getId());

                //get the data you need
                Intent intent = new Intent(MainActivity.this,WaitActivity.class);
                startActivity(intent);
            }
        });


        TableRow row4 = (TableRow)findViewById(R.id.menu_row4);
        row4.setClickable(true);

        row4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.setBackgroundColor(Color.GRAY);
                System.out.println("Row clicked: " + v.getId());

                //get the data you need
                Intent intent = new Intent(MainActivity.this,WaitActivity.class);
                startActivity(intent);
            }
        });

        TableRow row5 = (TableRow)findViewById(R.id.menu_row5);
        row5.setClickable(true);

        row5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.setBackgroundColor(Color.GRAY);
                System.out.println("Row clicked: " + v.getId());

                //get the data you need
                Intent intent = new Intent(MainActivity.this,WaitActivity.class);
                startActivity(intent);
            }
        });

        TableRow settingRow = (TableRow)findViewById(R.id.setting_row);
        settingRow.setClickable(true);

        settingRow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.setBackgroundColor(Color.GRAY);
                System.out.println("Row clicked: " + v.getId());

                //get the data you need
                Intent intent = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setTotalBalance()
    {
        Long total = 0L;

        if(wallets != null) {
            if(wallets.size()>0) {
                for(int i=0;i<wallets.size();i++) {
                    Wallet wallet = wallets.get(i);
                    total += wallet.balance;
                }
            }
        }

        String balanceText = "T "+String.format("%.1f",total/(double)100000000L);

        TextView totalText = (TextView) findViewById(R.id.total_balance);
        totalText.setText(balanceText);

    }


    public class Mybaseadapter extends BaseAdapter {

        public Mybaseadapter() {
            super();
        }

        /**
         * 剩下的问题就是依次实现BaseAdapter的这几个类方法就可以了
         */

        @Override
        public int getCount() {        //这个方法返回的是ListView的行数
            // TODO Auto-generated method stub
            return wallets!=null?wallets.size():0;
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
            public TextView walletName=null;
            public TextView walletAddrress=null;
            public TextView walletBalance=null;

        }
        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            // TODO Auto-generated method stub

            ViewHolder viewHolder=null;

            //注意convertView不是随意创建的，需要有LayoutInflater,根据list_item布局文件创建
            if (convertView == null) {
                viewHolder = new ViewHolder();

                convertView = getLayoutInflater().inflate(R.layout.wallet_list_item, null);
                viewHolder.walletName = (TextView) convertView.findViewById(R.id.wallet_name);
                viewHolder.walletAddrress = (TextView) convertView.findViewById(R.id.wallet_address);
                viewHolder.walletBalance = (TextView) convertView.findViewById(R.id.wallet_balance);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Wallet wallet = wallets.get(position);

            String username;

            if(wallet.username != null) {
                username = wallet.username;
            } else {
                username = "钱包"+(position+1);
            }

            viewHolder.walletName.setText(username);
            viewHolder.walletAddrress.setText(wallet.getAddress());
            String balanceText = "T "+String.format("%.1f",wallet.balance/(double)100000000L);
            viewHolder.walletBalance.setText(balanceText);

            return convertView;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        wallets = ArkService.getInstance(this).getWallets();
        mybaseadapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
