package com.github.rjkrish.vimjson.util;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class TrustAllHostNameVerifier implements HostnameVerifier {
    public TrustAllHostNameVerifier() {
    }

    public boolean verify(String urlHostName, SSLSession session) {
        return true;
    }

    public HostnameVerifier register() {
        HttpsURLConnection.setDefaultHostnameVerifier(this);
        return this;
    }
}
