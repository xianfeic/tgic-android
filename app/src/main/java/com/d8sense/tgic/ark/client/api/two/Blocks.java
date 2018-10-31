package com.d8sense.tgic.ark.client.api.two;

import com.google.gson.internal.LinkedTreeMap;
import com.d8sense.tgic.ark.client.http.Client;

import java.io.IOException;
import java.util.Map;

public class Blocks {
    Client client;

    public Blocks(Client client) {
        this.client = client;
    }

    public LinkedTreeMap<String, Object> all() throws IOException {
        return this.client.get("blocks");
    }

    public LinkedTreeMap<String, Object> show(String id) throws IOException {
        return this.client.get("blocks/" + id);
    }

    public LinkedTreeMap<String, Object> transactions(String id) throws IOException {
        return this.client.get("blocks/" + id + "/transactions");
    }

    public LinkedTreeMap<String, Object> search(Map<String, Object> parameters) throws IOException {
        return this.client.post("blocks/search", parameters);
    }

}
