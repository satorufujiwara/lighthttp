package jp.satorufujiwara.http;

import java.net.HttpURLConnection;

public abstract class ConnectionListener {

    protected void onPreConnect(Request request, HttpURLConnection connection) {

    }

    protected void onPostConnect(Request request, HttpURLConnection connection) {

    }

}
