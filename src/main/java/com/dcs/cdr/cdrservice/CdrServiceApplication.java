package com.dcs.cdr.cdrservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.SystemMetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class CdrServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CdrServiceApplication.class, args);
    }

}
