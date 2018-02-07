package com.b2w.desafio.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by matto on 06/02/2018.
 */
@Data
@Document(collection = "Counter")
public class Counter {
    @Id
    private String id;
    private long seq;
}
