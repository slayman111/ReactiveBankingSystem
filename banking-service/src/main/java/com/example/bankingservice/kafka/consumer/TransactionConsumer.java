package com.example.bankingservice.kafka.consumer;

import com.example.bankingservice.model.Transaction;
import com.example.bankingservice.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class TransactionConsumer {

  @Bean
  public Consumer<Transaction> consumeTransaction(TransactionService transactionService) {
    return transactionService::asyncProcess;
  }
}
