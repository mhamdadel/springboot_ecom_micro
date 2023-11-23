package ecommercemicroservices.order.configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class KafkaConfiguration {
    private final KafkaTemplate kafkaTemplate;
    @PostConstruct
    void setup() {
        this.kafkaTemplate.setObservationEnabled(true);
    }

    }