package com.d8sense.tgic.ark.client.http;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Client {
    private String host;
    private int version;
    private OkHttpClient client;
    private MediaType JSON = MediaType.parse("application/json charset=utf-8");

    public Client(String host, int version) {
        this.host = host;
        this.version = version;
        this.client = new OkHttpClient();
    }

    public LinkedTreeMap<String, Object> get(String url, Map<String, Object> params) throws IOException {
        HttpUrl.Builder httpBuider = HttpUrl.parse(this.host + url).newBuilder();

        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                httpBuider.addQueryParameter(entry.getKey(), entry.getValue().toString());
            }
        }

        Request request = new Request.Builder().url(httpBuider.build()).build();
        Response response = client.newCall(request).execute();
        return new Gson().fromJson(response.body().string(), new LinkedTreeMap<String, Object>().getClass());
    }

    public LinkedTreeMap<String, Object> get(String url) throws IOException {
        return get(url, new HashMap());
    }

    public LinkedTreeMap<String, Object> post(String url, Map payload) throws IOException {
        RequestBody body = RequestBody.create(JSON, new Gson().toJson(payload));
        Request request = new Request.Builder().url(this.host + url).post(body).build();
        Response response = client.newCall(request).execute();
        return new Gson().fromJson(response.body().string(), new LinkedTreeMap<String, Object>().getClass());
    }

    public LinkedTreeMap<String, Object> postWithHeaders(String url, String payload,Map headersParams) throws IOException {
        RequestBody body = RequestBody.create(JSON, payload);

        Headers headers=null;

        okhttp3.Headers.Builder headersbuilder=new okhttp3.Headers.Builder();
        if(headersParams != null) {
            Iterator<String> iterator = headersParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                headersbuilder.add(key, headersParams.get(key).toString());
            }
        }

        headers=headersbuilder.build();

        Request request = new Request.Builder().url(this.host + url).headers(headers).post(body).build();

        Response response = client.newCall(request).execute();
        return new Gson().fromJson(response.body().string(), new LinkedTreeMap<String, Object>().getClass());
    }

    public OkHttpClient getClient() {
        return client;
    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }

}
