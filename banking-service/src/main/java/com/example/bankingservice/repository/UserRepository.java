package com.example.bankingservice.repository;

import com.example.bankingservice.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

  Mono<User> findByCardId(String cardId);
}
