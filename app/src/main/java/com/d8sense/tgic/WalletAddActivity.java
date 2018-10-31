package com.d8sense.tgic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;

import com.d8sense.tgic.ark.crypto.identities.Address;
import com.d8sense.tgic.ark.crypto.encoding.Base58;
import com.d8sense.tgic.services.ArkService;
import com.google.common.base.Joiner;
import com.jaeger.library.StatusBarUtil;
import com.quincysx.crypto.bip39.MnemonicGenerator;
import com.quincysx.crypto.bip39.RandomSeed;
import com.quincysx.crypto.bip39.SeedCalculator;
import com.quincysx.crypto.bip39.WordCount;
import com.quincysx.crypto.bip39.wordlists.*;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


public class WalletAddActivity extends AppCompatActivity {
     private MyToolbar mMyToolbar;
     private ProgressBar mProgressbar;

    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    private int[] bytes= new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private int segment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_add);

        StatusBarUtil.setColor(this,R.attr.colorPrimary);
//        StatusBarUtil.setTransparent(this);

        mMyToolbar = findViewById(R.id.my_toolbar);

         mMyToolbar.setLeftTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mProgressbar = (ProgressBar)findViewById(R.id.progress_bar);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //继承了Activity的onTouchEvent方法，直接监听点击事件
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            x2 = event.getX();
            y2 = event.getY();
            if(y1 - y2 > 50) {
                updateProgressBar();
            } else if(y2 - y1 > 50) {
                updateProgressBar();
            } else if(x1 - x2 > 50) {
                updateProgressBar();
            } else if(x2 - x1 > 50) {
                updateProgressBar();
            }
        }
        return super.onTouchEvent(event);
    }

    private void updateProgressBar() {
        if(mProgressbar.getProgress()<75) {
            mProgressbar.incrementProgressBy(25);
            mProgressbar.incrementSecondaryProgressBy(20);
            segment = segment + 1;
//            randomSeed();
        } else {

            Wallet wallet = new Wallet();
            byte[] random = RandomSeed.random(WordCount.TWELVE);

            String entropy =  bytesToHex(random);

            wallet.setEntropy(entropy);

            List<String> mnemonic = new MnemonicGenerator(English.INSTANCE).createMnemonic(random);
            wallet.setMnemonic(mnemonic);
            byte[] seed = new SeedCalculator().calculateSeed(mnemonic, "");
            String seedStr = bytesToHex(seed);
            wallet.setSeed(seedStr);

            String mnemonicStr = Joiner.on(" ").join(mnemonic);

            byte[] sha256 = Sha256Hash.hash(mnemonicStr.getBytes());
            ECKey key = ECKey.fromPrivate(sha256,true);
            String publicKeyStr = key.getPublicKeyAsHex();
            wallet.setPublicKey(publicKeyStr);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                outputStream.write(187);
                outputStream.write(sha256);
                outputStream.write(0x01);

               String wif =  Base58.encodeChecked(outputStream.toByteArray());
                wallet.setWif(wif);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String address = Address.fromPublicKey(publicKeyStr,50);
            wallet.setAddress(address);

            wallet.setPublicKey(publicKeyStr);
            ArkService.getInstance(this).addWallet(wallet);

            Intent intent = new Intent(this,WalletBackupActivity.class);
            intent.putExtra("wallet",wallet);
            startActivity(intent);
            segment = 0;
            finish();
        }
    }


    private String getKey() {
        String s = "";

        for(int i=0;i<bytes.length;i++) {
//            String c = Integer.toHexString(bytes[i]);
            String b = String.format("%02x",bytes[i]);
//            System.out.println(b);
            s += b;
        }

        return s;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


}
