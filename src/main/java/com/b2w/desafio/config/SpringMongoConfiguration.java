package com.b2w.desafio.config;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by matto on 07/02/2018.
 */
@EnableMongoRepositories
public class SpringMongoConfiguration {

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(
                new MongoClient("localhost"),
                "planetas"
        );
    }
}
