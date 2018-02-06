package com.b2w.desafio.commons;

import com.b2w.desafio.model.SwapiPlanet;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * Created by matto on 06/02/2018.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class SwapiPlanetResponseBody {

    public Integer count;
    public Object next;
    public Object previous;
    public List<SwapiPlanet> results;

    public SwapiPlanetResponseBody() {
    }
}
