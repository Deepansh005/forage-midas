package com.jpmc.midascore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListenerService {

    private final ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core")
    public void listen(String message) throws Exception {
        Transaction transaction = mapper.readValue(message, Transaction.class);
        System.out.println("Received transaction: " + transaction.getAmount());
    }
}