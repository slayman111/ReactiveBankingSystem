package com.example.bankingservice.kafka.producer;

import com.example.bankingservice.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionProducer {

  private final StreamBridge streamBridge;

  public void sendMessage(Transaction transaction) {
    Message<Transaction> msg = MessageBuilder.withPayload(transaction)
      .setHeader(KafkaHeaders.KEY, transaction.getTransactionId().getBytes(StandardCharsets.UTF_8))
      .build();
    log.info("Transaction processed to dispatch: {}; Message dispatch successful: {}",
      msg,
      streamBridge.send("transaction-out-0", msg));
  }
}
