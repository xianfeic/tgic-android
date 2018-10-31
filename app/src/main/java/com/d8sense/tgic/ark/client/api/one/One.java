package com.d8sense.tgic.ark.client.api.one;

import com.d8sense.tgic.ark.client.api.AbstractAPI;
import com.d8sense.tgic.ark.client.http.Client;

public class One extends AbstractAPI {

    public Accounts accounts;
    public Blocks blocks;
    public Delegates delegates;
    public Loader loader;
    public Peers peers;
    public Signatures signatures;
    public Transactions transactions;

    public One(Client client) {
        super(client);
        this.accounts = new Accounts(client);
        this.blocks = new Blocks(client);
        this.delegates = new Delegates(client);
        this.loader = new Loader(client);
        this.peers = new Peers(client);
        this.signatures = new Signatures(client);
        this.transactions = new Transactions(client);
    }
}
