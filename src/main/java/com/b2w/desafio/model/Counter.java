package com.b2w.desafio.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by matto on 06/02/2018.
 */
//Comentei o Lombok abaixo e coloquei manualmente o Getter e Setter para facilitar o teste pela B2W - Bit
//@Data
@Document(collection = "Counter")
public class Counter {
    @Id
    private String id;
    private long seq;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }
}
