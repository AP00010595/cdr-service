package com.dcs.cdr.cdrservice.config;

import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracingConfig {

    @Bean
    public OtlpHttpSpanExporter otlpHttpSpanExporter(@Value("${tracing.url}") String endpoint) {
       return OtlpHttpSpanExporter.builder()
                .setEndpoint(endpoint)
                .build();

//        SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
//                .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
//                .build();
//
//        return OpenTelemetrySdk.builder()
//                .setTracerProvider(tracerProvider)
//                .build();
    }

//    @Bean
//    public Tracer tracer(OpenTelemetrySdk openTelemetrySdk) {
//        return openTelemetrySdk.getTracer("com.dcs.cdr.cdrservice");
//    }
}
