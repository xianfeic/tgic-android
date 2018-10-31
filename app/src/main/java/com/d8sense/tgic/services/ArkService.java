package com.d8sense.tgic.services;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.d8sense.tgic.Contact;
import com.d8sense.tgic.Delegate;
import com.d8sense.tgic.TransDetailActivity;
import com.d8sense.tgic.User;
import com.d8sense.tgic.Wallet;
import com.d8sense.tgic.ark.client.Connection;
import com.d8sense.tgic.ark.client.http.Client;
import com.d8sense.tgic.ark.core.Network;
import com.d8sense.tgic.ark.crypto.enums.Types;
import com.d8sense.tgic.ark.crypto.transactions.Transaction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Jason.z on 2018/9/15.
 * Copyright (c) 2016 Jason.z. All rights reserved.
 */
public class ArkService {
    private static Network network;
    private Context mContext;
    private static SharedPreferences sp ;
    private static String PREFERENCE_NAME = "TGIC";
    private static String WALLETS_TAG = "wallets";
    private static String CONTACTS_TAG = "contacts";
    private static String USER_TAG = "user";
    private static String GUIDE_TAG = "guide";

    private static ArkService arkService = null;
    private static boolean isLogin = false;


    private List<Wallet> wallets;

    private ArkService(Context context)
    {
        super();
        this.mContext = context;
        if( sp == null) {
            sp = context.getApplicationContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
    }

    public static boolean isLogin(){
        return isLogin;
    }

    public static void setLogin(boolean status){
        isLogin = status;
    }

    public static boolean isNew(){
        String strJson = sp.getString(USER_TAG, null);

        if(strJson != null) {
            return false;
        }

        return true;
    }

    public static boolean isGuide(){
        boolean isGuide = sp.getBoolean(GUIDE_TAG, false);

        return isGuide;
    }

    public static void setGuide(boolean isGuide){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(GUIDE_TAG, isGuide);
        editor.commit();
    }

    public static String getPinCode() {

        String strJson = sp.getString(USER_TAG, null);

        Gson gson = new Gson();

        if(strJson != null) {
            User user = gson.fromJson(strJson, User.class);
            return user.getPinCode();
        }

        return null;
    }

    public static void savePinCode(String pinCode) {

        String strJson = sp.getString(USER_TAG, null);

        User user = new User();
        Gson gson = new Gson();

        if(strJson != null) {
            user = gson.fromJson(strJson, User.class);
        }

        user.setPincode(pinCode);
        String userJson = gson.toJson(user);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_TAG, userJson);
        editor.commit();

    }


    public static synchronized ArkService getInstance(Context context) {
        if (arkService == null) {
            arkService = new ArkService(context);
        }

        return arkService;
    }

    public void initArkNetwork() {

//        if(network == null) {
//            Peer peer = new Peer();
//            peer.setIp("49.4.10.4");
//            peer.setPort(4100);
//            peer.setProtocol("http://");
//            peer.setStatus("OK");
//
//            List<Peer> peers = new ArrayList<Peer>();
//            peers.add(peer);
//
//            network = new Network();
//            network.setPeers(peers);
//        }
    }

    public static boolean addContact(Contact contact) {

        return saveContact(contact);
    }

    public static boolean removeContact(Contact contact) {
        if(getContacts() != null) {
            List<Contact> contacts = getContacts();
            int index = contacts.indexOf(contact);
            if( index >= 0) {
                contacts.remove(index);
                SharedPreferences.Editor editor = sp.edit();
                Gson gson = new Gson();
                String strJson = gson.toJson(contacts);
                editor.putString(CONTACTS_TAG, strJson);
                editor.commit();
                return true;
            }
        }

        return false;
    }


    public static List<Contact> getContacts() {
        List<Contact> contacts=new ArrayList<Contact>();

        String strJson = sp.getString(CONTACTS_TAG, "");
        if (null == strJson) {
            return contacts;
        }
        Gson gson = new Gson();
        contacts = gson.fromJson(strJson, new TypeToken<List<Contact>>() {
        }.getType());

        return contacts;

    }


    public static boolean saveContact(Contact contact) {
        List<Contact> contacts;

        if(getContacts() != null) {
            contacts = getContacts();
            int index = contacts.indexOf(contact);
            if( index >= 0) {
                contacts.set(index,contact);
            } else {
                contacts.add(contact);
            }
        } else {
            contacts = new ArrayList<Contact>();
            contacts.add(contact);
        }

        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();

        String strJson = gson.toJson(contacts);
        editor.putString(CONTACTS_TAG, strJson);
        editor.commit();

        return true;

    }

    public static boolean addWallet(Wallet wallet) {
        return saveWallet(wallet);
    }

    public static boolean removeWallet(Wallet wallet) {
        if(getWallets() != null) {
            List<Wallet> wallets = getWallets();
            int index = wallets.indexOf(wallet);
            if( index >= 0) {
                wallets.remove(index);
                SharedPreferences.Editor editor = sp.edit();
                Gson gson = new Gson();
                String strJson = gson.toJson(wallets);
                editor.putString(WALLETS_TAG, strJson);
                editor.commit();
                return true;
            }
        }

        return false;
    }

    public static boolean saveWallet(Wallet wallet) {

        List<Wallet> wallets;

        if(getWallets() != null) {
            wallets = getWallets();
            int index = wallets.indexOf(wallet);
            if( index >= 0) {
                wallets.set(index,wallet);
            } else {
                wallets.add(wallet);
            }
        } else {
            wallets = new ArrayList<Wallet>();
            wallets.add(wallet);
        }

        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();

        String strJson = gson.toJson(wallets);
        editor.putString(WALLETS_TAG, strJson);
        editor.commit();

        return true;

    }


    public static List<Transaction> getTransactions(Wallet wallet, final GetTransactionsCallback getTransactionCallback) throws IOException {

        Client client = new Client("http://49.4.10.4:4100/",50);

        Map<String,Object> params = new HashMap<>();

        params.put("recipientId",wallet.getAddress());
        params.put("senderId",wallet.getAddress());
        params.put("orderBy","timestamp:desc");

        List<Transaction> transactionList = new ArrayList<>();

        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    LinkedTreeMap<String, Object> actual = client.get("api/transactions", params);
                    Boolean success = (boolean)actual.get("success");

                    if (success) {
                        JsonObject jsonObject = new Gson().toJsonTree(actual).getAsJsonObject();
                        JsonArray jsonArray = jsonObject.get("transactions").getAsJsonArray();

                        Type listType = new TypeToken<ArrayList<Transaction>>() {
                        }.getType();

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter( Types.class,new EnumSerializer());
                        Gson gson =gsonBuilder.create();

                        List<Transaction> transactionList = gson.fromJson(jsonArray, listType);

//                        for (int i = 0; i < jsonArray.size(); i++) {
//                            JsonObject t = jsonArray.get(i).getAsJsonObject();
//                            Transaction transaction = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
//                                    .create()
//                                    .fromJson(t, Transaction.class);
//                            transactionList.add(transaction);
//                        }
                        getTransactionCallback.success(transactionList);
                    } else {
                        String error = actual.get("error").toString();
                        getTransactionCallback.failure(error);
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();



        return transactionList;

    }

    public interface GetTransactionsCallback{
        void success(List<Transaction> transactions);
        void failure(String error);
    }

    public static void getTransaction(String id, final GetTransactionCallback getTransactionCallback) throws IOException {

        Client client = new Client("http://49.4.10.4:4100/",50);

        Map<String,Object> params = new HashMap<>();

        params.put("id",id);

        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    LinkedTreeMap<String, Object> actual = client.get("api/transactions/get", params);
                    Boolean success = (boolean)actual.get("success");

                    if (success) {
                        JsonObject jsonObject = new Gson().toJsonTree(actual).getAsJsonObject();
                        JsonObject transObj = jsonObject.get("transaction").getAsJsonObject();

                        Type listType = new TypeToken<Transaction>() {
                        }.getType();

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter( Types.class,new EnumSerializer());
                        Gson gson =gsonBuilder.create();

                        Transaction transaction = gson.fromJson(transObj, listType);

                        getTransactionCallback.success(transaction);
                    } else {
                        String error = actual.get("error").toString();
                        getTransactionCallback.failure(error);
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public interface GetTransactionCallback{
        void success(Transaction transaction);
        void failure(String error);
    }

    public static void getDelegates(String offset,final GetDelegateCallback getDelegatesCallback) throws IOException {
        Client client = new Client("http://49.4.10.4:4100/",50);

        Map<String,Object> params = new HashMap<>();

        params.put("limit","51");
        params.put("offset",offset);

        List<Delegate> delegateList = new ArrayList<>();

        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    LinkedTreeMap<String, Object> actual = client.get("api/delegates", params);
                    Boolean success = (boolean)actual.get("success");

                    if (success) {
                        JsonObject jsonObject = new Gson().toJsonTree(actual).getAsJsonObject();
                        JsonArray jsonArray = jsonObject.get("delegates").getAsJsonArray();

                        Type listType = new TypeToken<ArrayList<Delegate>>() {
                        }.getType();

                        List<Delegate> delegateList = new Gson().fromJson(jsonArray, listType);
                        getDelegatesCallback.success(delegateList);
                    } else {
                        String error = actual.get("error").toString();
//                        getDelegateCallback.failure(error);
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

//        client.get("api/delegates",null,params, new Client.MyNetCall(){
//            public void success(Call call, Response response) throws IOException {
//                String responseStr = response.body().string();
//
//                JsonParser jsonParser = new JsonParser();
//                JsonObject jsonObject = (JsonObject) jsonParser.parse(responseStr);
//
//                Boolean success = jsonObject.get("success").getAsBoolean();
//
//                if(success) {
//                    JsonArray jsonArray = jsonObject.get("delegates").getAsJsonArray();
//                    for(int i = 0;i<jsonArray.size();i++){
//                        JsonObject t = jsonArray.get(i).getAsJsonObject();
//                        Delegate delegate = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
//                                .create()
//                                .fromJson(t,Delegate.class);
//                        delegateList.add(delegate);
//                    }
//                }
//
//                getDelegatesCallback.success(delegateList);
//
//            };
//            public void failed(Call call, IOException e)  {
//
//                //TODO 不处理
////                getDelegatesCallback.success(delegateList);
//
//            };
//
//        });

    }

    public interface GetDelegateCallback {
        void success(List<Delegate> delegates);
    }

    public static void getWallet(Wallet wallet,final GetWalletCallback getWalletCallback) throws IOException {
        Client client = new Client("http://49.4.10.4:4100/",50);

        Map<String,Object> params = new HashMap<>();

        params.put("address",wallet.getAddress().toString());

        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    LinkedTreeMap<String, Object> actual = client.get("api/accounts", params);
                    Boolean success = (boolean) actual.get("success");

                    if (success) {
                        JsonObject jsonObject = new Gson().toJsonTree(actual).getAsJsonObject();
                        JsonObject jsonObject2 = jsonObject.get("account").getAsJsonObject();

                        Long balance = jsonObject2.get("balance").getAsLong();
//                        String username = jsonObject2.get("username").getAsString();

                        getWalletCallback.success(balance,null);
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void getWalletVote(Wallet wallet,final GetWalletVoteCallback getWalletVoteCallback) throws IOException {
        Client client = new Client("http://49.4.10.4:4100/",50);

        Map<String,Object> params = new HashMap<>();

        params.put("address",wallet.getAddress().toString());

        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    LinkedTreeMap<String, Object> actual = client.get("api/accounts/delegates", params);
                    Boolean success = (boolean) actual.get("success");

                    if (success) {
                        JsonObject jsonObject = new Gson().toJsonTree(actual).getAsJsonObject();
                        JsonArray jsonArray = jsonObject.get("delegates").getAsJsonArray();

                        String vote = null;

                        Type listType = new TypeToken<ArrayList<Delegate>>() {
                        }.getType();

                        List<Delegate> delegateList = new Gson().fromJson(jsonArray, listType);

                        if(delegateList.size()>0){
                            Delegate delegate = delegateList.get(0);
                            vote = delegate.publicKey;
                        }

                        getWalletVoteCallback.success(vote);
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface GetWalletCallback {
        void success(Long  balance,String username);
    }

    public interface GetWalletVoteCallback {
        void success(String vote);
    }

    public interface SaveTransactionCallback{
        void success(String id);
        void failure(String error);
    }

    public static List<Wallet> getWallets() {
        List<Wallet> wallets=new ArrayList<Wallet>();

        String strJson = sp.getString(WALLETS_TAG, "");
        if (null == strJson) {
            return wallets;
        }
        Gson gson = new Gson();
        wallets = gson.fromJson(strJson, new TypeToken<List<Wallet>>() {
        }.getType());

        return wallets;

    }

    public void createWallet() {

    }

    public void generateEntropy() {

    }

    public void createTranaction(){

    }

    public void saveTranaction(Transaction transaction,final SaveTransactionCallback saveTransactionCallback) {

        Client client = new Client("http://49.4.10.4:4100/",1);

        List<Transaction> transactionList = new ArrayList<Transaction>();

        transactionList.add(transaction);

        Map<String,List<Transaction>> params = new HashMap<>();

        Gson gson = new Gson();

        params.put("transactions",transactionList);

        System.out.print(gson.toJson(transactionList));

        Map<String,String> headersParams = new HashMap<>();
        headersParams.put("Content-Type","application/json");
        headersParams.put("os","ark-mobile");
        headersParams.put("version","1.0.0");
        headersParams.put("port","1");
        headersParams.put("nethash","fb06b3f610b84d536fd23f13c6389f7b3379f2038613b307700bb55a230d3116");

        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter( Types.class,new EnumSerializer());
                    Gson gson =gsonBuilder.create();
                    String payload = gson.toJson(params);

                    LinkedTreeMap<String, Object> actual = client.postWithHeaders("peer/transactions",payload,headersParams);
                    boolean success = (boolean)actual.get("success");

                    if(success) {
                        JsonObject jsonObject = new Gson().toJsonTree(actual).getAsJsonObject();
                        JsonArray  jsonArray= jsonObject.get("transactionIds").getAsJsonArray();

                        if(jsonArray.size()>0) {
                            String id = jsonArray.get(0).getAsString();
                            saveTransactionCallback.success(id);
                        } else {
                            saveTransactionCallback.success("");
                        }
                    } else {
                        String message = actual.get("message").toString();
                        String error = actual.get("error").toString();

                        saveTransactionCallback.failure(message+"("+error+")");

                    }
                }
                 catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();



//        new Thread(){
//            @Override
//            public void run()
//            {
//                try {
//                    client.post("peer/transactions",headersParams,params, new Client.MyNetCall(){
//                        public void success(Call call, Response response) throws IOException {
//                            String responseStr = response.body().string();
//
//                            JsonParser jsonParser = new JsonParser();
//                            JsonObject jsonObject = (JsonObject) jsonParser.parse(responseStr);
//
//                            Boolean success = jsonObject.get("success").getAsBoolean();
//
//                            if(success) {
//                                saveTransactionCallback.success();
//                            } else {
//                                saveTransactionCallback.failure();
//                            }
//
//
//                        };
//                        public void failed(Call call, IOException e)  {
//
//                            //TODO 不处理
//                            saveTransactionCallback.failure();
//
//                        };
//
//                    });
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();



    }


    public static class EnumSerializer implements JsonSerializer<Types>,JsonDeserializer<Types> {
        // 对象转为Json时调用,实现JsonSerializer<PackageState>接口
        @Override
        public JsonElement serialize(Types types, Type arg1,
                                     JsonSerializationContext arg2) {

            return new JsonPrimitive(types.ordinal());
        }

        // json转为对象时调用,实现JsonDeserializer<PackageState>接口
        @Override
        public Types deserialize(JsonElement json, Type type,
                                        JsonDeserializationContext context) throws JsonParseException {

            if (json.getAsInt() < Types.values().length)
                return Types.values()[json.getAsInt()];

            return null;
        }
    }

}
