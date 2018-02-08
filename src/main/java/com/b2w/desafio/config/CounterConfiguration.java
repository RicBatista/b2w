package com.b2w.desafio.config;

import com.b2w.desafio.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Created by matto on 07/02/2018.
 */
@Configuration
public class CounterConfiguration {

    @Autowired
    private CounterService counterService;

    @PostConstruct
    private void configureCouter() {

        if(!counterService.collectionExists("Counter")) {
            counterService.createCollection();
        }
    }
}
