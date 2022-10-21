package com.example.metrics;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.List;

public class Test {

    public static void main(String[] args) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, InvalidAlgorithmParameterException {
        Certificates certificates = new Certificates();
        List<Certificate> certificateList = certificates.getCertificates();
        System.out.println(certificateList);
    }
}
