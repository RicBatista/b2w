package com.b2w.desafio.repository;

import com.b2w.desafio.model.Counter;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by matto on 07/02/2018.
 */
public interface CounterRepository extends MongoRepository<Counter, String> {

}
