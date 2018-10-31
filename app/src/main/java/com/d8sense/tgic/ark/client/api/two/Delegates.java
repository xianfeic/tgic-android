package com.d8sense.tgic.ark.client.api.two;

import com.google.gson.internal.LinkedTreeMap;
import com.d8sense.tgic.ark.client.http.Client;

import java.io.IOException;

public class Delegates {
    Client client;

    public Delegates(Client client) {
        this.client = client;
    }

    public LinkedTreeMap<String, Object> all() throws IOException {
        return this.client.get("delegates");
    }

    public LinkedTreeMap<String, Object> show(String id) throws IOException {
        return this.client.get("delegates/" + id);
    }

    public LinkedTreeMap<String, Object> blocks(String id) throws IOException {
        return this.client.get("delegates/" + id + "/blocks");
    }

    public LinkedTreeMap<String, Object> voters(String id) throws IOException {
        return this.client.get("delegates/" + id + "/voters");
    }

}
