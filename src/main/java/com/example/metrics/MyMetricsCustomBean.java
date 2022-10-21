package com.example.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.MultiGauge;
import io.micrometer.core.instrument.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class MyMetricsCustomBean {

    // multigauge for low inventory (dimensions on product id and name)
    MultiGauge multiGauge = null;

    @Lazy
    @Autowired
    private final CertificateController certificateController;

    public Supplier<Number> fetchUserCount() {
        return certificateController.fetchCount();
    }

    // metrics with name and date certificates
    @Bean
    public void updateCertificatesData() {
        multiGauge.register(
                certificateController.getCertificates().stream()
                .map(
                         cert -> MultiGauge.Row.of(
                                Tags.of(
                                        "name", cert.getIssuerDN().getName(),
                                        "validity.time.days",
                                        // different days
                                        String.valueOf((cert.getNotAfter().getTime() - cert.getNotBefore().getTime()) / (24 * 60 * 60 * 1000))
                                ), 1
                        )
                )
                .collect(Collectors.toList()), true
        );
    }

    public MyMetricsCustomBean(MeterRegistry registry, CertificateController certificateController) {
        this.certificateController = certificateController;


        // numbers of certificates
        Gauge.builder("numbers.of.certificates", fetchUserCount())
                .register(registry);

        multiGauge = MultiGauge.builder("certificates.data")
                .tag("name", "start.date")
                .register(registry);
    }
}
