package com.example.bankingservice.controller;

import com.example.bankingservice.model.Transaction;
import com.example.bankingservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/banking")
@RequiredArgsConstructor
public class TransactionController {

  private final TransactionService transactionService;

  @PostMapping(value = "/process", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Transaction> process(@RequestBody Transaction transaction) {
    log.info("Process transaction with details: {}", transaction);
    return transactionService.process(transaction);
  }
}
