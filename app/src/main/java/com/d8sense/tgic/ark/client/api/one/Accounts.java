package com.d8sense.tgic.ark.client.api.one;

import com.google.gson.internal.LinkedTreeMap;
import com.d8sense.tgic.ark.client.http.Client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Accounts {
    Client client;

    public Accounts(Client client) {
        this.client = client;
    }

    public LinkedTreeMap<String, Object> all(Map query) throws IOException {
        return this.client.get("accounts/getAllAccounts", query);
    }

    public LinkedTreeMap<String, Object> all() throws IOException {
        return this.all(new HashMap());
    }

    public LinkedTreeMap<String, Object> show(String address) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("address", address);
        return this.client.get("accounts", map);
    }

    public LinkedTreeMap<String, Object> count() throws IOException {
        return this.client.get("accounts/count");
    }

    public LinkedTreeMap<String, Object> delegates(String address) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("address", address);
        return this.client.get("accounts/delegates", map);
    }

    public LinkedTreeMap<String, Object> fee() throws IOException {
        return this.client.get("accounts/delegates/fee");
    }

    public LinkedTreeMap<String, Object> balance(String address) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("address", address);
        return this.client.get("accounts/getBalance", map);
    }

    public LinkedTreeMap<String, Object> publicKey(String address) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("address", address);
        return this.client.get("accounts/getPublicKey", map);
    }

    public LinkedTreeMap<String, Object> top(Map query) throws IOException {
        return this.client.get("accounts/top", query);
    }

    public LinkedTreeMap<String, Object> top() throws IOException {
        return top(new HashMap());
    }

}
