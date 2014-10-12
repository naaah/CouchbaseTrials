package com.manoj.couchbase;

import com.couchbase.client.CouchbaseClient;
import com.google.gson.Gson;
import com.manoj.models.User;
import net.spy.memcached.internal.OperationFuture;

public class Creator {

    private Connector connector;

    public Creator(){
        connector = new Connector();
    }

    public static void main(String[] args) {
        Connector myConnector = new Connector();
        Gson gson = new Gson();

        myConnector.connect();

        User user = new User();

        user.name = "manoj";
        user.interest = "nothing";
        user.type = "user";

        CouchbaseClient client = myConnector.getClient();
        OperationFuture<Boolean> add = client.add("user_" + user.hashCode(), gson.toJson(user));

        while(add.isDone());

        User user1 = gson.fromJson(client.get("user_" + user.hashCode()).toString(), User.class);

        System.out.println(user1.name);
        System.out.println(user1.interest);
        System.out.println(user1.type);

        myConnector.disconnect();
    }
}
