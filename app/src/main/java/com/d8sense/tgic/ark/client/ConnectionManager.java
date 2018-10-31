package com.d8sense.tgic.ark.client;

import com.d8sense.tgic.ark.client.api.AbstractAPI;

import java.util.HashMap;
import java.util.Map;

public class ConnectionManager {
    private Map<String, Connection<? extends AbstractAPI>> connections;
    private String defaultConnection = "main";

    public ConnectionManager() {
        this.connections = new HashMap<>();
    }

    public String getDefaultConnection() {
        return this.defaultConnection;
    }

    public void setDefaultConnection(String name) {
        this.defaultConnection = name;
    }

    public Map<String, Connection<? extends AbstractAPI>> getConnections() {
        return this.connections;
    }

    public <T extends AbstractAPI> Connection<T> connect(Map config, String name) {
        if (this.connections.containsKey(name)) {
            throw new IllegalArgumentException("Connection [" + name + "] is already configured.");
        }

        this.connections.put(name, new Connection<T>(config));

        return (Connection<T>) this.connections.get(name);
    }

    public <T extends AbstractAPI> Connection<T> connect(Map config) {
        return connect(config, "main");
    }

    public void disconnect(String name) {
        if (name == null || name.isEmpty()) {
            name = getDefaultConnection();
        }

        this.connections.remove(name);
    }

    public void disconnect() {
        disconnect(null);
    }

    public <T extends AbstractAPI> Connection<T> connection(String name) {
        if (name == null || name.isEmpty()) {
            name = getDefaultConnection();
        }

        if (!this.connections.containsKey(name)) {
            throw new IllegalArgumentException("Connection [" + name + "] not configured.");
        }

        return (Connection<T>) this.connections.get(name);
    }

    public Connection connection() {
        return connection(null);
    }

}
