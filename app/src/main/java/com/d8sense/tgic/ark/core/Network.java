package com.d8sense.tgic.ark.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Jason.z on 2018/9/24.
 * Copyright (c) 2016 BeikePay. All rights reserved.
 */
public class Network {

    String nethash;
    String name;
    int port;
    byte prefix;
    String version = "1.0.1";
    int broadcastMax = 10;
    List<String> peerseed = new ArrayList<String>();
    List<Peer> peers = new ArrayList<Peer>();

    static Random random = new Random();


    static Network Mainnet = new Network(
            "6e84d08bd299ed97c212c886c98a57e36545c8f5d645ca7eeae63a8bd62d8988",
            0x17,
            4001,
            "mainnet",
            new ArrayList<String>(Arrays.asList(
                "5.39.9.240:4001",
                "5.39.9.241:4001",
                "5.39.9.242:4001",
                "5.39.9.243:4001",
                "5.39.9.244:4001",
                "5.39.9.245:4001",
                "5.39.9.246:4001",
                "5.39.9.247:4001",
                "5.39.9.248:4001",
                "5.39.9.249:4001",
                "5.39.9.250:4001",
                "5.39.9.251:4001",
                "5.39.9.252:4001",
                "5.39.9.253:4001",
                "5.39.9.254:4001",
                "5.39.9.255:4001"
            ))
            );

    static Network Devnet = new Network(
            "578e820911f24e039733b45e4882b73e301f813a0d2c31330dafda84534ffa23",
            0x1e,
            4002,
            "devnet",
            new ArrayList<String>(Arrays.asList(
                     "167.114.29.52:4002",
                     "167.114.29.53:4002",
                     "167.114.29.55:4002"
            )));

    Network(String nethash,int prefix,int port,String name,List<String> peerSeed) {
        this.nethash = nethash;
        this.prefix = (byte) prefix;
        this.port = port;
        this.peerseed = peerSeed;
    }

    public Map getHeaders(){
        Map map = new HashMap<String,String>();
        map.put("nethash",this.nethash);
        map.put("version",this.version);
        map.put("port",this.port);
        return map;
    }

    public boolean warmup(){
        if(peers.size()>0) return false;
        for(int i=1;i<=peerseed.size();i++) {
            Peer peer = Peer.create(peerseed.get(i),Mainnet.getHeaders());
            peers.add(peer);
        }
        return true;
    }

    // broadcast to many nodes
    public int leftShift(Transaction transaction){
        for(int i=1;i<broadcastMax;i++) {
//            getRandomPeer() << transaction;
        }
        return broadcastMax;
    }

    public Peer getRandomPeer(){
        return peers.get(random.nextInt(peers.size()));
    }
}
