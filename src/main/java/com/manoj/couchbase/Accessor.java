package com.manoj.couchbase;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.*;
import com.google.gson.Gson;
import com.manoj.models.User;

public class Accessor {
    public static void main(String[] args) {
        Connector connector = new Connector();
        Gson gson = new Gson();

        connector.connect();

        CouchbaseClient client = connector.getClient();

        View view = client.getView("user", "user_view");
        System.out.println(view.getURI());

        Query query = new Query();
        query.setIncludeDocs(true).setLimit(20);
        query.setStale(Stale.FALSE);

        ViewResponse userQuery = client.query(view, query);

        for (ViewRow viewRow : userQuery) {
            Object document = viewRow.getDocument();
            User user = gson.fromJson(document.toString(), User.class);
            System.out.println(document.toString());
            System.out.println(user.name);
            System.out.println(user.type);
            System.out.println(user.interest);
        }


        connector.disconnect();
    }
}
