package com.d8sense.tgic.ark.client.api.one;

import com.google.gson.internal.LinkedTreeMap;
import com.d8sense.tgic.ark.client.http.Client;

import java.io.IOException;

public class Signatures {
    Client client;

    public Signatures(Client client) {
        this.client = client;
    }

    public LinkedTreeMap<String, Object> fee() throws IOException {
        return this.client.get("signatures/fee");
    }

}
