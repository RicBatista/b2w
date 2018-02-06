package com.b2w.desafio.model;

import lombok.Data;

import java.net.URI;
import java.util.Date;
import java.util.List;

/**
 * Created by matto on 06/02/2018.
 */
@Data
public class SwapiPlanet {

    /**
     * A number denoting the gravity of this planet. Where 1 is normal.
     * (Required)
     *
     */
    public String gravity;
    /**
     * the terrain of this planet. Comma-seperated if diverse.
     * (Required)
     *
     */
    public String terrain;
    /**
     * The ISO 8601 date format of the time that this resource was created.
     * (Required)
     *
     */
    public Date created;
    /**
     * An array of People URL Resources that live on this planet.
     * (Required)
     *
     */
    public List<Object> residents = null;
    /**
     * The percentage of the planet surface that is naturally occuring water or bodies of water.
     * (Required)
     *
     */
    public String surface_water;
    /**
     * the ISO 8601 date format of the time that this resource was edited.
     * (Required)
     *
     */
    public Date edited;
    /**
     * An array of Film URL Resources that this planet has appeared in.
     * (Required)
     *
     */
    public List<Object> films = null;
    /**
     * The climate of this planet. Comma-seperated if diverse.
     * (Required)
     *
     */
    public String climate;
    /**
     * The name of this planet.
     * (Required)
     *
     */
    public String name;
    /**
     * The diameter of this planet in kilometers.
     * (Required)
     *
     */
    public String diameter;
    /**
     * The average populationof sentient beings inhabiting this planet.
     * (Required)
     *
     */
    public String population;
    /**
     * The number of standard hours it takes for this planet to complete a single rotation on its axis.
     * (Required)
     *
     */
    public String rotation_period;
    /**
     * The hypermedia URL of this resource.
     * (Required)
     *
     */
    public URI url;
    /**
     * The number of standard days it takes for this planet to complete a single orbit of its local star.
     * (Required)
     *
     */
    public String orbital_period;

    public SwapiPlanet() {}
}
