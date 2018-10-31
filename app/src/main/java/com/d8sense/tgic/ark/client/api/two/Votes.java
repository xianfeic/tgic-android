package com.d8sense.tgic.ark.client.api.two;

import com.google.gson.internal.LinkedTreeMap;
import com.d8sense.tgic.ark.client.http.Client;

import java.io.IOException;

public class Votes {
    Client client;

    public Votes(Client client) {
        this.client = client;
    }

    public LinkedTreeMap<String, Object> all() throws IOException {
        return this.client.get("votes");
    }

    public LinkedTreeMap<String, Object> show(String ip) throws IOException {
        return this.client.get("votes/" + ip);
    }

}
