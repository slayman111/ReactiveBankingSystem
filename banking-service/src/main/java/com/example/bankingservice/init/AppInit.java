package com.example.bankingservice.init;

import com.example.bankingservice.kafka.producer.TransactionProducer;
import com.example.bankingservice.model.Transaction;
import com.example.bankingservice.model.User;
import com.example.bankingservice.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class AppInit implements ApplicationListener<ApplicationReadyEvent> {

  private final UserRepository userRepo;
  private final TransactionProducer producer;

  @Override
  public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
    log.info("Application started");
    ObjectMapper mapper = new ObjectMapper();
    TypeReference<List<User>> typeReferenceUser = new TypeReference<>() {
    };
    InputStream inputStreamUser = TypeReference.class.getResourceAsStream("/json/users.json");
    try {
      List<User> usersList = mapper.readValue(inputStreamUser, typeReferenceUser);
      usersList.forEach(u -> {
        User user = userRepo.findByCardId(u.getCardId())
          .share()
          .block();
        if (Objects.isNull(user)) {
          userRepo.save(u).subscribe();
        }
      });
      log.info("User Saved!");
    } catch (IOException e) {
      log.error("Unable to save User: " + e.getMessage());
    }

    TypeReference<List<Transaction>> typeReferenceTransaction = new TypeReference<>() {
    };
    InputStream inputStreamTransaction = TypeReference.class.getResourceAsStream("/json/transactions.json");
    try {
      List<Transaction> transactionsList = mapper.readValue(inputStreamTransaction, typeReferenceTransaction);
      transactionsList.forEach(producer::sendMessage);
      log.info("Transactions Dispatched to Kafka topic!");
    } catch (IOException e) {
      log.error("Unable to dispatch transactions to Kafka Topic: " + e.getMessage());
    }
  }
}
