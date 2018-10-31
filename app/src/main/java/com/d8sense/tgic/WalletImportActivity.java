package com.d8sense.tgic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.blankj.utilcode.util.ScreenUtils;
import com.d8sense.tgic.ark.crypto.identities.Address;
import com.d8sense.tgic.services.ArkService;
import com.google.common.base.Joiner;
import com.quincysx.crypto.bip39.MnemonicValidator;
import com.quincysx.crypto.bip39.validation.InvalidChecksumException;
import com.quincysx.crypto.bip39.validation.InvalidWordCountException;
import com.quincysx.crypto.bip39.validation.UnexpectedWhiteSpaceException;
import com.quincysx.crypto.bip39.validation.WordNotFoundException;
import com.quincysx.crypto.bip39.wordlists.English;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class WalletImportActivity extends AppCompatActivity {

    private MyToolbar mMyToolbar;
    private GridView mGridView;
    private SimpleAdapter mAdapter;
    private List<Map<String, Object>> mDataList;
    private Button mImportBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_import);

        mMyToolbar = findViewById(R.id.my_toolbar);

        mMyToolbar.setLeftTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        initGridData();

        String[] from={"value"};

        int[] to={R.id.word_text};

        mGridView = (GridView) findViewById(R.id.gridview);
        mGridView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        mAdapter=new MyGridViewAdapter(this, mDataList, R.layout.input_grid_item);

        mGridView.setAdapter(mAdapter);



        mImportBtn = (Button)findViewById(R.id.confirm_btn);
//        mImportBtn.setOnClickListener(importClick);
    }

    public void importClick(View v) {

        SweetAlertDialog pDialog = new SweetAlertDialog(WalletImportActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("导入中，请稍候...");
        pDialog.setCancelable(false);
        pDialog.show();

        String mnemonicStr=null;

        List<String> mnemonic = new ArrayList<String>();

        for (int i = 0; i <mDataList.size(); i++) {
            Map<String, Object> map=mDataList.get(i);
            String value = map.get("value").toString();

            mnemonic.add(value);

            if(value == null || value.length() == 0) {
                pDialog.hide();
                Toast.makeText(WalletImportActivity.this,"请输入完整助记词",Toast.LENGTH_SHORT).show();
                return;
            }

            if(i == 0) {
                mnemonicStr = value;
            } else {
                mnemonicStr += " "+value;
            }

        }

        // TODO 验证

        try {
            MnemonicValidator
                    .ofWordList(English.INSTANCE)
                    .validate(mnemonicStr);
        } catch (UnexpectedWhiteSpaceException e) {
            pDialog.hide();
            Toast.makeText(WalletImportActivity.this,"助记词验证失败1",Toast.LENGTH_SHORT).show();
            return;
        } catch (InvalidWordCountException e) {
            pDialog.hide();
            Toast.makeText(WalletImportActivity.this,"助记词验证失败2",Toast.LENGTH_SHORT).show();
            return;
        } catch (InvalidChecksumException e) {
            pDialog.hide();
            Toast.makeText(WalletImportActivity.this,"助记词验证失败3",Toast.LENGTH_SHORT).show();
            return;
        } catch (WordNotFoundException e) {
            pDialog.hide();
            Toast.makeText(WalletImportActivity.this,"助记词验证失败4",Toast.LENGTH_SHORT).show();
            return;
            //e.getSuggestion1()
            //e.getSuggestion2()
        }

        byte[] sha256 = Sha256Hash.hash(mnemonicStr.getBytes());
        ECKey key = ECKey.fromPrivate(sha256,true);
        String publicKeyStr = key.getPublicKeyAsHex();

        String address = Address.fromPublicKey(publicKeyStr,50);
        Wallet wallet = new Wallet();
        wallet.setAddress(address);
        wallet.setPublicKey(publicKeyStr);
        wallet.setMnemonic(mnemonic);
        ArkService.getInstance(WalletImportActivity.this).addWallet(wallet);
        // TODO

        Intent intent = new Intent(WalletImportActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void initGridData() {

        String word[]={"","","","","","","","","","","",""};
        mDataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i <word.length; i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("value",word[i]);
            mDataList.add(map);
        }
    }

    private int calculateItemWidth() {

        int width = px2dip(ScreenUtils.getScreenWidth());

        return (width-40-16*2)/3;
    }

    public static int px2dip( float pxValue) {
        float density = ScreenUtils.getScreenDensity();
        return (int) (pxValue / density + 0.5f);
    }

    public class MyGridViewAdapter extends SimpleAdapter {


        public MyGridViewAdapter(Context context,
                                   List<? extends Map<String, ?>> data, int resource) {
            super(context, data, resource, null, null);
            // TODO Auto-generated constructor stub
            //get reference to the row
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 12;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mDataList.get(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if(convertView == null)
            {
                //get reference to the row
                convertView = getLayoutInflater().inflate(R.layout.input_grid_item, null);

//                view = super.getView(position, convertView, parent);

                holder = new ViewHolder();

                holder.wordText = (EditText) convertView.findViewById(R.id.word_text);
                holder.wordText.setText("");

                holder.wordText.setOnFocusChangeListener(new android.view.View.
                        OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            // 此处为得到焦点时的处理内容
                        } else {
                            // 此处为失去焦点时的处理内容

                        }
                    }
                });

                holder.wordText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        // TODO Auto-generated method stub
                        Map<String, Object> map = mDataList.get(position);
                        map.put("value",s.toString());
                    }
                });

                holder.wordText.setNextFocusDownId(R.id.word_text);

//                ViewGroup.LayoutParams params = holder.wordText.getLayoutParams();
//                params.width = calculateItemWidth();

//                holder.prodRate.setFocusable(isRateEditable);
//                holder.prodRate.setEnabled(isRateEditable);
//
//                holder.prodDisc.setFocusable(isDiscountEditable);
//                holder.prodDisc.setEnabled(isDiscountEditable);
//
//            /* For set Focus on Next */
//                if(isRateEditable)
//                    holder.prodQty.setNextFocusDownId(R.id.productRateValue);
//                else if(isDiscountEditable)
//                    holder.prodQty.setNextFocusDownId(R.id.productDiscountValue);
//
//                if(isDiscountEditable)
//                    holder.prodRate.setNextFocusDownId(R.id.productDiscountValue);

//                holder.prodDisc.setNextFocusDownId(R.id.productQuantityValue

                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }

            return convertView;
        }

        public class ViewHolder{
            EditText wordText;
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
