package com.manoj.couchbase;

import com.couchbase.client.CouchbaseClient;
import net.spy.memcached.internal.OperationFuture;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Connector {
    private String hostname;
    private String port;
    private String bucket;
    private String password;
    private CouchbaseClient couchbaseClient;

    public Connector(){
        this.hostname = "localhost";
        this.port = "8091";
        this.bucket = "fitnfret";
        this.password = "";
    }

    public Connector(String hostname, String port, String bucket, String password){
        this.hostname = hostname;
        this.port = port;
        this.bucket = bucket;
        this.password = password;
    }

    private URI constructUrl(){
        return URI.create("http://" + this.hostname + ":" + this.port + "/pools" );
    }

    public boolean connect() {
        List<URI> uris = new ArrayList<URI>();
        boolean isConnected = false;
        uris.add(constructUrl());
        try {
            couchbaseClient = new CouchbaseClient(uris, this.bucket, this.password);
            isConnected = true;
        } catch (IOException e){
            e.printStackTrace();
        }
        return isConnected;
    }

    public boolean disconnect(){
        if (couchbaseClient == null){
            return false;
        }
        couchbaseClient.shutdown();
        return true;
    }

    public CouchbaseClient getClient(){
        return this.couchbaseClient;
    }

    public static void main(String[] args) {
        Connector connector = new Connector();
        connector.connect();

        CouchbaseClient client = connector.getClient();

        OperationFuture<Boolean> setOperationFuture = client.set("hello", "world");

        while(setOperationFuture.isDone());

        Object hello = client.get("hello");

        System.out.println(hello);

        connector.disconnect();

        return;
    }
}
