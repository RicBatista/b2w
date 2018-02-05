package com.b2w.desafio.commons;

import com.b2w.desafio.model.Planeta;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * Created by matto on 05/02/2018.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PlanetaResponseBody {
    private String description;

    private List<Planeta> planetas;

    private Planeta planeta;

    public PlanetaResponseBody() {

    }

    public PlanetaResponseBody(List<Planeta> planetas) {
        this.planetas = planetas;
    }

    public PlanetaResponseBody(Planeta planeta) {
        this.planeta = planeta;
    }
}
