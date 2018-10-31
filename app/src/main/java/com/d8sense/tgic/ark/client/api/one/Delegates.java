package com.d8sense.tgic.ark.client.api.one;

import com.google.gson.internal.LinkedTreeMap;
import com.d8sense.tgic.ark.client.http.Client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Delegates {
    Client client;

    public Delegates(Client client) {
        this.client = client;
    }

    public LinkedTreeMap<String, Object> all(Map query) throws IOException {
        return this.client.get("delegates", query);
    }

    public LinkedTreeMap<String, Object> all() throws IOException {
        return this.all(new HashMap());
    }

    public LinkedTreeMap<String, Object> show(Map id) throws IOException {
        return this.client.get("delegates/get", id);
    }

    public LinkedTreeMap<String, Object> count() throws IOException {
        return this.client.get("delegates/count");
    }

    public LinkedTreeMap<String, Object> fee() throws IOException {
        return this.client.get("delegates/fee");
    }

    public LinkedTreeMap<String, Object> forgedByAccount(String generatorPublicKey) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("generatorPublicKey", generatorPublicKey);
        return this.client.get("delegates/forging/getForgedByAccount", map);
    }

    public LinkedTreeMap<String, Object> search(String query) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("q", query);
        return this.client.get("delegates/search", map);
    }

    public LinkedTreeMap<String, Object> voters(String publicKey) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("publicKey", publicKey);
        return this.client.get("delegates/voters", map);
    }

    public LinkedTreeMap<String, Object> nextForgers() throws IOException {
        return this.client.get("delegates/getNextForgers");
    }

}
