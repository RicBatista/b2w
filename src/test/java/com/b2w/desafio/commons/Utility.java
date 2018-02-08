package com.b2w.desafio.commons;

import com.b2w.desafio.model.Planeta;
import com.b2w.desafio.model.SwapiPlanet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matto on 07/02/2018.
 */
@Service
public class Utility {

    public static List<Planeta> getPlanetasDefault() {

        List<Planeta> planetas = new ArrayList<>();

        Planeta planeta1 = new Planeta();
        planeta1.setNome("Tatooine");
        planeta1.setClima("arid");
        planeta1.setTerreno("desert");

        Planeta planeta2 = new Planeta();
        planeta2.setNome("Alderaan");
        planeta2.setClima("temperate");
        planeta2.setTerreno("grasslands, mountains");

        Planeta planeta3 = new Planeta();
        planeta3.setNome("Yavin IV");
        planeta3.setClima("temperate, tropical");
        planeta3.setTerreno("jungle, rainforests");

        planetas.add(planeta1);
        planetas.add(planeta2);
        planetas.add(planeta3);

        return planetas;
    }

    public static String getJsonCadastroPlaneta(Planeta planeta) {

        String jsonPlaneta = "{  \n" +
                "   \"planeta\":{  \n" +
                "      \"nome\":\""+ planeta.getNome() +"\",\n" +
                "      \"clima\": \""+ planeta.getClima() +"\",\n" +
                "      \"terreno\": \""+ planeta.getTerreno() +"\"\n" +
                "   }\n" +
                "}";

        return jsonPlaneta;
    }

    public static String getJsonCadastroPlanetaBadRequest(Planeta planeta) {

        String jsonPlaneta = "{  \n" +
                "   \"planeta\":{  \n" +
                "      \"nome\":\""+ planeta.getNome() +"\",\n" +
                "      \"climas\": \""+ planeta.getClima() +"\",\n" +
                "      \"terreno\": \""+ planeta.getTerreno() +"\"\n" +
                "   }\n" +
                "}";

        return jsonPlaneta;
    }

    public static SwapiPlanet getSwapiPlanetDeUmPlaneta(Planeta planeta) {

        List<Object> filmes = new ArrayList<>();
        filmes.add("filme 1");
        filmes.add("filme 2");
        filmes.add("filme 3");

        SwapiPlanet swapiPlanet = new SwapiPlanet();
        swapiPlanet.setName(planeta.getNome());
        swapiPlanet.setClimate(planeta.getClima());
        swapiPlanet.setTerrain(planeta.getTerreno());
        swapiPlanet.setFilms(filmes);

        return swapiPlanet;
    }
}
