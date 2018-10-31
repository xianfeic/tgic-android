package com.d8sense.tgic.ark.client.api.one;

import com.google.gson.internal.LinkedTreeMap;
import com.d8sense.tgic.ark.client.http.Client;

import java.io.IOException;

public class Loader {
    Client client;

    public Loader(Client client) {
        this.client = client;
    }

    public LinkedTreeMap<String, Object> status() throws IOException {
        return this.client.get("loader/status");
    }

    public LinkedTreeMap<String, Object> sync() throws IOException {
        return this.client.get("loader/status/sync");
    }

    public LinkedTreeMap<String, Object> autoconfigure() throws IOException {
        return this.client.get("loader/autoconfigure");
    }

}
