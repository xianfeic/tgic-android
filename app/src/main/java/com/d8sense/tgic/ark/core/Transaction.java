package com.d8sense.tgic.ark.core;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;

import org.bitcoinj.core.Base58;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.google.common.io.BaseEncoding.base16;

/**
 * Created by Jason.z on 2018/9/24.
 * Copyright (c) 2016 BeikePay. All rights reserved.
 */



public class Transaction extends Object{

    int timestamp;
    public String recipientId;
    public Long amount;
    public Long fee;
    TransactionType type;
    String vendorField;
    String signature;
    String signSignature;
    String senderPublicKey;
    String requesterPublicKey;
    Map<String, Object> asset = new HashMap<String, Object>();
    String id;

    public Transaction(TransactionType type,Long amount,Long fee) {

    }

    Transaction(TransactionType type,Long amount,Long fee,String recipientId,String vendorField ) {

    }

    /**
     * Serializes this transaction object into a byte array
     *
     * @param skipSignature
     * @param skipSecondSignature
     * @return an array of bytes representing this object
     */
    public byte[] toBytes(boolean skipSignature, boolean skipSecondSignature){
        ByteBuffer buffer = ByteBuffer.allocate(1000);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.put(type.getByteValue());
        buffer.putInt(timestamp);
        buffer.put(BaseEncoding.base16().lowerCase().decode(senderPublicKey));

        if(requesterPublicKey != null){
            buffer.put(BaseEncoding.base16().lowerCase().decode(requesterPublicKey));
        }

        if(recipientId != null){
            buffer.put(Base58.decodeChecked(recipientId));
        }
        else {
            buffer.put(new byte[21]);
        }

        if(vendorField != null){
            byte[] vbytes = vendorField.getBytes();
            if(vbytes.length<65){
                buffer.put(vbytes);
                buffer.put(new byte[64-vbytes.length]);
            }
        }
        else {
            buffer.put(new byte[64]);
        }

        buffer.putLong(amount);
        buffer.putLong(fee);

        if(type == TransactionType.SECONDSIGNITURE || type == TransactionType.SECONDSIGNATURE){
//            buffer.put(BaseEncoding.base16().lowerCase().decode(asset.get("signature").get("publicKey")));
        }
        else if(type == TransactionType.DELEGATE){
            String username = asset.get("username").toString();
            buffer.put(username.getBytes());
        }
        else if(type == TransactionType.VOTE){
            buffer.put(asset.get("votes").toString().getBytes());
        }
        // TODO: multisignature
        // else if(type==4){
        //   buffer.put BaseEncoding.base16().lowerCase().decode(asset.signature)
        // }

        if(!skipSignature && signature != null){
            buffer.put(BaseEncoding.base16().lowerCase().decode(signature));
        }
        if(!skipSecondSignature && signature != null){
            buffer.put(BaseEncoding.base16().lowerCase().decode(signSignature));
        }

        byte[] outBuffer = new byte[buffer.position()];
        buffer.rewind();
        buffer.get(outBuffer);
        return outBuffer;
    }

//    public Map toObject(){
//        return this.properties.subMap(["id", "timestamp", "recipientId", "amount", "fee", "type", "vendorField", "signature", "signSignature", "senderPublicKey", "requesterPublicKey", "asset"]);
//    }

    String sign(String passphrase){
        senderPublicKey = base16().lowerCase().encode(Crypto.getKeys(passphrase).getPubKey());
        signature = base16().lowerCase().encode(Crypto.sign(this, passphrase).encodeToDER());
        return signature;
    }

    String secondSign(String passphrase){
        signSignature = base16().lowerCase().encode(Crypto.secondSign(this, passphrase).encodeToDER());
        return signSignature;
    }

    String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    static Transaction fromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Transaction.class);
    }

    static Transaction createTransaction(String recipientId, long satoshiAmount, String vendorField, String passphrase, String secondPassphrase){
        Transaction tx = new Transaction(TransactionType.NORMAL, satoshiAmount, (long) 10000000,recipientId, vendorField);
        tx.timestamp = Slot.getTime(null);
        tx.sign(passphrase);
        if(secondPassphrase != null)
            tx.secondSign(secondPassphrase);
        tx.id = Crypto.getId(tx);
        return tx;
    }

    static Transaction createVote(List votes, String passphrase, String secondPassphrase){
        Transaction tx = new Transaction(TransactionType.VOTE, 0L, 100000000L);
        tx.asset.put("votes", votes);
        tx.recipientId = Crypto.getAddress(Crypto.getKeys(passphrase));
        tx.timestamp = Slot.getTime(null);
        tx.sign(passphrase);
        if(secondPassphrase != null)
            tx.secondSign(secondPassphrase);
        tx.id = Crypto.getId(tx);
        return tx;
    }

    static Transaction createDelegate(String username, String passphrase, String secondPassphrase){
        Transaction tx = new Transaction(TransactionType.DELEGATE, 0L, 2500000000L);
        tx.asset.put("username",username);
        tx.timestamp = Slot.getTime(null);
        tx.sign(passphrase);
        if(secondPassphrase != null)
            tx.secondSign(secondPassphrase);
        tx.id = Crypto.getId(tx);
        return tx;
    }

    static Transaction createSecondSignature(String secondPassphrase, String passphrase){
        Transaction tx = new Transaction(TransactionType.SECONDSIGNATURE, 0L, 500000000L);
//        tx.asset.put("signature",publicKey:BaseEncoding.base16().lowerCase().encode(Crypto.getKeys(secondPassphrase).getPubKey());
        tx.timestamp = Slot.getTime(null);
        tx.sign(passphrase);
        tx.id = Crypto.getId(tx);
        return tx;
    }

    //TODO: create multisignature

    //Custom getter to map type to byte value
    byte getType()
    {
        return type.getByteValue();
    }
}
