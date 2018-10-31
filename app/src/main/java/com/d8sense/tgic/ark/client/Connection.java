package com.d8sense.tgic.ark.client;

import com.d8sense.tgic.ark.client.api.AbstractAPI;
import com.d8sense.tgic.ark.client.api.one.One;
import com.d8sense.tgic.ark.client.api.two.Two;
import com.d8sense.tgic.ark.client.http.Client;

import java.util.Map;

public class Connection<T extends AbstractAPI> {

    private Client client;
    private int version;

    private T api;

    public Connection(Map<String, Object> config) {
        this.version = ((int) (config.get("version")));
        this.client = new Client(config.get("host").toString(), (int) config.get("version"));

        this.api = (T) ((this.version == 1) ? new One(this.client) : new Two(this.client));
    }

    public T api() {
        return this.api;
    }

}
