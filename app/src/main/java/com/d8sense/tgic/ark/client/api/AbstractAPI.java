package com.d8sense.tgic.ark.client.api;

import com.d8sense.tgic.ark.client.http.Client;

public abstract class AbstractAPI {

    protected Client client;

    public AbstractAPI(Client client) {
        this.client = client;
    }
}
