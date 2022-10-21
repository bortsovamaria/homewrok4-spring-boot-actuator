package com.example.metrics;


import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/certificate")
public class CertificateController {

    private final Certificates certificates;

    // supplies certificates count
    public Supplier<Number> fetchCount() {
        return () -> {
            try {
                return certificates.getCertificates().size();
            } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException | InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            }
            return null;
        };
    }

    public List<X509Certificate> getCertificates() {
        try {
            return certificates.getCertificates().stream().map(s -> (X509Certificate) s).collect(Collectors.toList());
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    // constructor injector for exposing metrics at Actuator /prometheus
    public CertificateController(MeterRegistry registry, Certificates certificates) {
        this.certificates = certificates;

        Gauge.builder("certificatecontroller.count", fetchCount()).
                tag("version","v1").
                description("certificatecontroller descrip").
                register(registry);
    }
}
