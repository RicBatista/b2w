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
    private long id;

    @Indexed(unique = true)
    @NotNull(message = "O campo nome deve ser informado")
    private String nome;

    @NotNull(message = "O campo clima deve ser informado")
    private String clima;

    @NotNull(message = "O campo terreno deve ser informado")
    private String terreno;

    private Integer filmesTotal;

}
