package com.github.rjkrish.vimjson.util;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public final class TrustAllTrustManager implements TrustManager, X509TrustManager {
    public TrustAllTrustManager() {
    }

    public void register() throws NoSuchAlgorithmException, KeyManagementException {
        register(this);
    }

    public static void register(TrustManager tm) throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[]{tm};
        SSLContext sc = SSLContext.getInstance("TLSv1");
        SSLSessionContext sslsc = sc.getServerSessionContext();
        sslsc.setSessionTimeout(0);
        sc.init((KeyManager[])null, trustAllCerts, (SecureRandom)null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
    }

    public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
    }
}