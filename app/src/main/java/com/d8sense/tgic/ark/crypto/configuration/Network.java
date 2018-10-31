package com.d8sense.tgic.ark.crypto.configuration;

import com.d8sense.tgic.ark.crypto.networks.INetwork;
import com.d8sense.tgic.ark.crypto.networks.Mainnet;

public class Network {
    private static INetwork network = new Mainnet();

    public static INetwork get() {
        return network;
    }

    public static void set(INetwork network) {
        Network.network = network;
    }
}
