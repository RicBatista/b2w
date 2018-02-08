package com.b2w.desafio.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * Created by matto on 05/02/2018.
 */
//Comentei o Lombok abaixo e coloquei manualmente o Getter e Setter para facilitar o teste pela B2W - Bit
//@Data
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getClima() {
        return clima;
    }

    public void setClima(String clima) {
        this.clima = clima;
    }

    public String getTerreno() {
        return terreno;
    }

    public void setTerreno(String terreno) {
        this.terreno = terreno;
    }

    public Integer getFilmesTotal() {
        return filmesTotal;
    }

    public void setFilmesTotal(Integer filmesTotal) {
        this.filmesTotal = filmesTotal;
    }
}
