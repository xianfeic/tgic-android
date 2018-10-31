package com.d8sense.tgic.ark.client.api.two;

import com.google.gson.internal.LinkedTreeMap;
import com.d8sense.tgic.ark.client.http.Client;

import java.io.IOException;
import java.util.Map;

public class Wallets {
    Client client;

    public Wallets(Client client) {
        this.client = client;
    }

    public LinkedTreeMap<String, Object> all() throws IOException {
        return this.client.get("wallets");
    }

    public LinkedTreeMap<String, Object> show(String ip) throws IOException {
        return this.client.get("wallets/" + ip);
    }

    public LinkedTreeMap<String, Object> transactions(String ip) throws IOException {
        return this.client.get("wallets/" + ip + "/transactions");
    }

    public LinkedTreeMap<String, Object> sentTransactions(String ip) throws IOException {
        return this.client.get("wallets/" + ip + "/transactions/sent");
    }

    public LinkedTreeMap<String, Object> receivedTransactions(String ip) throws IOException {
        return this.client.get("wallets/" + ip + "/transactions/received");
    }

    public LinkedTreeMap<String, Object> votes(String ip) throws IOException {
        return this.client.get("wallets/" + ip + "/votes");
    }

    public LinkedTreeMap<String, Object> search(Map<String, Object> parameters) throws IOException {
        return this.client.post("wallets/search", parameters);
    }

    public LinkedTreeMap<String, Object> top() throws IOException {
        return this.client.get("wallets/top");
    }

}
