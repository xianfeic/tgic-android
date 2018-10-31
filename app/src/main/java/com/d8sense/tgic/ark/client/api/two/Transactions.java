package com.d8sense.tgic.ark.client.api.two;

import com.google.gson.internal.LinkedTreeMap;
import com.d8sense.tgic.ark.client.http.Client;

import java.io.IOException;
import java.util.Map;

public class Transactions {
    Client client;

    public Transactions(Client client) {
        this.client = client;
    }

    public LinkedTreeMap<String, Object> all() throws IOException {
        return this.client.get("transactions");
    }

    public LinkedTreeMap<String, Object> create(Map<String, Object> transactions) throws IOException {
        return this.client.post("transactions", transactions);
    }

    public LinkedTreeMap<String, Object> show(String id) throws IOException {
        return this.client.get("transactions/" + id);
    }

    public LinkedTreeMap<String, Object> allUnconfirmed() throws IOException {
        return this.client.get("transactions/unconfirmed");
    }

    public LinkedTreeMap<String, Object> showUnconfirmed(String id) throws IOException {
        return this.client.get("transactions/unconfirmed/" + id);
    }

    public LinkedTreeMap<String, Object> search(Map<String, Object> parameters) throws IOException {
        return this.client.post("blocks/search", parameters);
    }

    public LinkedTreeMap<String, Object> types() throws IOException {
        return this.client.get("transactions/types");
    }

}
