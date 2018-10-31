package com.d8sense.tgic.ark.client.api.one;

import com.google.gson.internal.LinkedTreeMap;
import com.d8sense.tgic.ark.client.http.Client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Blocks {
    Client client;

    public Blocks(Client client) {
        this.client = client;
    }

    public LinkedTreeMap<String, Object> all(Map query) throws IOException {
        return this.client.get("blocks", query);
    }

    public LinkedTreeMap<String, Object> all() throws IOException {
        return this.all(new HashMap());
    }

    public LinkedTreeMap<String, Object> show(String id) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        return this.client.get("blocks/get", map);
    }

    public LinkedTreeMap<String, Object> epoch() throws IOException {
        return this.client.get("blocks/getEpoch");
    }

    public LinkedTreeMap<String, Object> fee() throws IOException {
        return this.client.get("blocks/getFee");
    }

    public LinkedTreeMap<String, Object> fees() throws IOException {
        return this.client.get("blocks/getFees");
    }

    public LinkedTreeMap<String, Object> height() throws IOException {
        return this.client.get("blocks/getHeight");
    }

    public LinkedTreeMap<String, Object> milestone() throws IOException {
        return this.client.get("blocks/getMilestone");
    }

    public LinkedTreeMap<String, Object> nethash() throws IOException {
        return this.client.get("blocks/getNethash");
    }

    public LinkedTreeMap<String, Object> reward() throws IOException {
        return this.client.get("blocks/getReward");
    }

    public LinkedTreeMap<String, Object> status() throws IOException {
        return this.client.get("blocks/getStatus");
    }

    public LinkedTreeMap<String, Object> supply() throws IOException {
        return this.client.get("blocks/getSupply");
    }

}
