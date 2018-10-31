package com.d8sense.tgic.ark.client.api.one;

import com.google.gson.internal.LinkedTreeMap;
import com.d8sense.tgic.ark.client.http.Client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Peers {
    Client client;

    public Peers(Client client) {
        this.client = client;
    }

    public LinkedTreeMap<String, Object> all(Map query) throws IOException {
        return this.client.get("peers", query);
    }

    public LinkedTreeMap<String, Object> all() throws IOException {
        return this.all(new HashMap());
    }

    public LinkedTreeMap<String, Object> show(String ip, int port) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("ip", ip);
        map.put("port", port);
        return this.client.get("peers/get", map);
    }

    public LinkedTreeMap<String, Object> version() throws IOException {
        return this.client.get("peers/version");
    }

}
