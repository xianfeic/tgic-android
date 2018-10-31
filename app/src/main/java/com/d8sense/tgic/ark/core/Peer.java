package com.d8sense.tgic.ark.core;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Jason.z on 2018/9/24.
 * Copyright (c) 2016 BeikePay. All rights reserved.
 */
public class Peer {

    String ip;
    int port;
    String protocol = "http://";
    String status = "NEW";

    private OkHttpClient http;
    private Map networkHeaders;

    Peer(String ip, int port, String protocol, Map networkHeaders){
        this.ip = ip;
        this.port = port;
        this.protocol = protocol;
        this.networkHeaders = networkHeaders;

        if(http == null)
            http = new OkHttpClient();
    }

    public static Peer create(String string, Map networkHeaders){
        String[] data = string.split(":");
        int port = Integer.parseInt(data[1]);
        String protocol = "http://";
        if(port%1000 == 443) protocol = "https://";
        return new Peer(data[0], port, protocol, networkHeaders);
    }

    // return Future that will deliver the JSON as a Map
    // the "query" argument allows to pass URL parameters as param1: 'value1', param2: 'value2'... string
    public void request(String method, String path,Map<String,String> bodyParams,final MyNetCall myNetCall){

        RequestBody body=setRequestBody(bodyParams);

        Request request = null;
        Response response = null;

        String url = protocol+ip+port;

        Request.Builder requestBuilder = new Request.Builder();


        switch(method){
            case "POST":
                request = requestBuilder.post(body).build();
                break;
            case "PUT":
                request = requestBuilder.url(url).put(body).build();
                break;
            case "GET":
                request = new Request.Builder().url(url).get().build();
        }

        Call call = http.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myNetCall.failed(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                myNetCall.success(call,response);

            }
        });

    }


    public interface MyNetCall{
        void success(Call call, Response response) throws IOException;
        void failed(Call call, IOException e);
    }


    private RequestBody setRequestBody(Map<String, String> BodyParams){
        RequestBody body=null;
        okhttp3.FormBody.Builder formEncodingBuilder=new okhttp3.FormBody.Builder();
        if(BodyParams != null){
            Iterator<String> iterator = BodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                formEncodingBuilder.add(key, BodyParams.get(key));
//                Log.d("post http", "post_Params==="+key+"===="+BodyParams.get(key));
            }
        }
        body=formEncodingBuilder.build();
        return body;

    }

//    public Map getStatus(){
//        return request("GET", "/peer/status").get();
//    }

    /*
     * TODO: Actually use this to get an updated list of peers instead of the
     * constantly breaking hardcoded one in the Network class
     */
//    public Map getPeers(){
//
//         request("GET", "/peer/list", null, new MyNetCall() {
//            @Override
//            public void success(Call call, Response response) throws IOException {
//                String result = response.body().string();
////                final ResponseBean responseBean = JSON.parseObject(result, ResponseBean.class);
//
//            }
//
//            @Override
//            public void failed(Call call, IOException e) {
//            }
//        });
//
//
//    }

//    public Map getDelegates(){
//        request("GET", "/api/delegates").get()
//    }

//    public Map postTransaction(Transaction transaction){
//
//        Future future = http.request(POST, JSON) {
//            uri.path = "/peer/transactions"
//            headers << networkHeaders
//            body = [transactions:[transaction.toObject()]]
//
//            response.success = { resp, json ->
//                    json
//            }
//        }
//        future.get()
//    }

//    public Map getTransactions(Account account, int amount)
//    {
//        if(!http) http = new AsyncHTTPBuilder(uri: "${protocol}${ip}:${port}")
//
//        Future c = http.get(path: "/api/transactions",
//            headers: networkHeaders,
//            contentType: JSON,
//            query: [recipientId:account.getAddress(),
//            senderId:account.getAddress(),
//            limit:amount])
//
//        future.get()
//    }

//    public Map leftShift(Transaction transaction){
//        postTransaction(transaction)
//    }
}
