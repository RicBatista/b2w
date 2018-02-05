package com.b2w.desafio.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * Created by matto on 05/02/2018.
 */
@Data
@Document(collection = "Planeta")
public class Planeta {

    @Id
    private String id;

    @Indexed(unique = true)
    @NotNull
    private String nome;

    @NotNull
    private String clima;

    @NotNull
    private String terreno;

}
