package com.crossover.trial.journals.service.email;


import java.security.cert.X509Certificate;

public class MyTrustManager implements com.sun.net.ssl.X509TrustManager {


    @Override
    public boolean isClientTrusted(X509Certificate[] x509Certificates) {
        return true;
    }

    @Override
    public boolean isServerTrusted(X509Certificate[] x509Certificates) {
        return true;
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}