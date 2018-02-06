package com.b2w.desafio.service;

import com.b2w.desafio.commons.SwapiPlanetResponseBody;
import com.b2w.desafio.model.SwapiPlanet;
import com.b2w.desafio.validation.SwapiValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by matto on 06/02/2018.
 */
@Slf4j
@Service
@PropertySource("classpath:application.properties")
public class SwapiPlanetService {

    @Value("${swapi.base.url}")
    private String swapiBaseUrl;

    @Value("${swapi.resource}")
    private String swapiResource;

    @Value("${swapi.resource.search}")
    private String swapiResourceSearch;

    private String getSwapiUrlResourceSearch() {
        return this.swapiBaseUrl + this.swapiResource + this.swapiResourceSearch;
    }

    /**
     * Verifica em SWAPI a Existencia de um planeta pelo nome e retorna sua instancia
     *
     * @param name
     * @return SwapiPlanet
     * @throws SwapiValidationException
     */
    public SwapiPlanet getPlanetInSwapiByName(String name) throws SwapiValidationException {

        String url = this.getSwapiUrlResourceSearch() + name;
        SwapiPlanet swapiPlanet;

        try {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    //.addHeader("Cache-Control", "no-cache")
                    .build();

            Response response = client.newCall(request).execute();

            if(response.isSuccessful()) {

                ObjectMapper mapper = new ObjectMapper();

                SwapiPlanetResponseBody swapiPlanetResponseBody =
                        mapper.readValue(response.body().bytes(), SwapiPlanetResponseBody.class);

                if(swapiPlanetResponseBody.count > 0) {

                    List<SwapiPlanet> swapiPlanetList = swapiPlanetResponseBody.getResults();

                    swapiPlanet = swapiPlanetList.get(0);

                    return swapiPlanet;

                } else {

                    throw new SwapiValidationException(
                            HttpStatus.NOT_FOUND,
                            "Planeta inexistente na API Star Wars"
                    );
                }

            } else {
                throw new SwapiValidationException(
                        HttpStatus.valueOf(response.code()),
                        response.message()
                );
            }

        } catch (SwapiValidationException SwapiException) {

            throw new SwapiValidationException(
                    SwapiException.getHttpStatus(),
                    SwapiException.getMessage(),
                    SwapiException
            );

        }  catch (Exception exception) {

            throw new SwapiValidationException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erro interno ao consultar a API Star Wars",
                    exception
            );
        }

    }



}
