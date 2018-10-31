package com.d8sense.tgic.ark.client.api.one;

import com.google.gson.internal.LinkedTreeMap;
import com.d8sense.tgic.ark.client.http.Client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Transactions {
    Client client;

    public Transactions(Client client) {
        this.client = client;
    }

    public LinkedTreeMap<String, Object> all(Map query) throws IOException {
        return this.client.get("transactions", query);
    }

    public LinkedTreeMap<String, Object> all() throws IOException {
        return this.all(new HashMap());
    }

    public LinkedTreeMap<String, Object> show(String id) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        return this.client.get("transactions/get", map);
    }

    public LinkedTreeMap<String, Object> allUnconfirmed(Map query) throws IOException {
        return this.client.get("transactions/unconfirmed", query);
    }

    public LinkedTreeMap<String, Object> allUnconfirmed() throws IOException {
        return this.allUnconfirmed(new HashMap());
    }

    public LinkedTreeMap<String, Object> showUnconfirmed(String id) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        return this.client.get("transactions/unconfirmed/get", map);
    }

}
