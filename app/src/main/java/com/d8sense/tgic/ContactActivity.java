package com.d8sense.tgic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.d8sense.tgic.ark.crypto.identities.Address;
import com.d8sense.tgic.services.ArkService;
import com.longsh.optionframelibrary.OptionBottomDialog;
import com.uuzuche.lib_zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ContactActivity extends AppCompatActivity {

    //数据链表和Map容器
    public ArrayList<HashMap<String,Object>> list=null;
    HashMap<String,Object> map=null;

    ListView listView=null;
    private MyToolbar mMyToolbar;

    public List<Contact> contacts = new ArrayList<Contact>();

    Mybaseadapter myBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        listView=(ListView)findViewById(R.id.contact_list);
        TextView textTip = (TextView)findViewById(R.id.text_tip);
        listView.setEmptyView(textTip);

        list=new ArrayList<HashMap<String,Object>>();
        contacts = ArkService.getInstance(this).getContacts();


//        for(int i=0; i<4; i++){
//            map=new HashMap<String,Object>();    //map调用put方法添加键值对
//            map.put("name","联系人1");
//            map.put("address", "M123123f323212f123123");
//            list.add(map);
//        }
        //创建自定义的MyAdapter对象
        myBaseAdapter=new Mybaseadapter();

        //调用ListView的setAdapter()方法设置适配器
        listView.setAdapter(myBaseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // TODO Auto-generated method stub

                Contact contact = contacts.get(position)
;
                List<String> stringList = new ArrayList<String>();
                stringList.add("编辑");
                stringList.add("删除");

                final OptionBottomDialog optionBottomDialog = new OptionBottomDialog(ContactActivity.this, stringList);
                optionBottomDialog.setItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if(position == 0 ) {

                            AddContactPopWindow addContactPopWindow = new AddContactPopWindow(ContactActivity.this,contact,new View.OnClickListener(){
                                @Override
                                public void onClick(View v) {

                                }
                            });

                            addContactPopWindow.showAtLocation(findViewById(R.id.contact_view), Gravity.CENTER, 0, 0);
                        } else if(position == 1) {

                            ArkService.removeContact(contact);

                            contacts = ArkService.getInstance(ContactActivity.this).getContacts();
                            myBaseAdapter.notifyDataSetChanged();

                        } else if(position == 2) {

                        }

                        optionBottomDialog.dismiss();
                    }
                });

            }
        });

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
                AddContactPopWindow addContactPopWindow = new AddContactPopWindow(ContactActivity.this,null,new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {

                    }
                });

                addContactPopWindow.showAtLocation(findViewById(R.id.contact_view), Gravity.CENTER, 0, 0);

            }
        });



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
        return contacts!=null?contacts.size():0;
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
        public TextView contactName=null;
        public TextView contactAddr=null;
        public ImageView contactAction=null;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub

        ViewHolder viewHolder=null;

        //注意convertView不是随意创建的，需要有LayoutInflater,根据list_item布局文件创建
        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = getLayoutInflater().inflate(R.layout.contact_list_item, null);
            viewHolder.contactName = (TextView) convertView.findViewById(R.id.contact_name);
            viewHolder.contactAddr = (TextView) convertView.findViewById(R.id.contact_address);
            viewHolder.contactAction = (ImageView) convertView.findViewById(R.id.contact_action);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(contacts.size()>0) {
            Contact contact = contacts.get(position);

            if(contact != null) {
                viewHolder.contactName.setText(contact.username);
                viewHolder.contactAddr.setText(contact.address);
            }


        }

        return convertView;     //把这个每一行的View对象返回
    }
}


    public class AddContactPopWindow extends PopupWindow {

        private Context mContext;

        private View view;

        private Button add_btn;

        public ImageView close_icon;

        public EditText contact_addr;
        public EditText contact_name;

        public int REQUEST_CODE = 100;
        private Contact mContact;

        public AddContactPopWindow(Activity mContext,Contact contact, View.OnClickListener itemsOnClick) {

            this.mContext = mContext;
            this.mContact = contact;

            this.view = LayoutInflater.from(mContext).inflate(R.layout.add_contact_pop_window, null);

            contact_addr = (EditText) view.findViewById(R.id.contact_address);
            contact_name = (EditText) view.findViewById(R.id.contact_name);

            add_btn =  (Button) view.findViewById(R.id.add_btn);
            close_icon =  (ImageView) view.findViewById(R.id.close_icon);

            if(contact != null) {
                contact_addr.setText(contact.address);
                contact_name.setText(contact.username);
            }

            if(mContact == null) {
                mContact = new Contact();
            }

            // 设置按钮监听
            add_btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    if(contact_addr.getText().toString().length() < 32){
                        Toast.makeText(ContactActivity.this,"地址长度不合法",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(contact_name.getText().toString().length() == 0){
                        Toast.makeText(ContactActivity.this,"请输入联系人名称",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(!Address.validate(contact_addr.getText().toString(),50)) {
                        Toast.makeText(ContactActivity.this,"无效的地址",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    mContact.address = contact_addr.getText().toString();
                    mContact.username = contact_name.getText().toString();
                    mContact.id = getUUID();

                    ArkService.getInstance(ContactActivity.this).saveContact(mContact);

                    contacts = ArkService.getInstance(ContactActivity.this).getContacts();
                    myBaseAdapter.notifyDataSetChanged();

                    if(AddContactPopWindow.this.isShowing()){
                        AddContactPopWindow.this.dismiss();
                    }
                }
            });
            close_icon.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(AddContactPopWindow.this.isShowing()){
                        AddContactPopWindow.this.dismiss();
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

    @Override
    protected void onResume() {
        super.onResume();

        contacts = ArkService.getInstance(this).getContacts();
        myBaseAdapter.notifyDataSetChanged();
    }


    public static String getUUID(){
        UUID uuid= UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr=str.replace("-", "");
        return uuidStr;
    }






}


