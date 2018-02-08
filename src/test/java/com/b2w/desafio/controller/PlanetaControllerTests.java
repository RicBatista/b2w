package com.b2w.desafio.controller;

import com.b2w.desafio.PlanetasApplication;
import com.b2w.desafio.commons.PlanetaResponseBody;
import com.b2w.desafio.commons.Utility;
import com.b2w.desafio.model.Planeta;
import com.b2w.desafio.repository.PlanetaRepository;
import com.b2w.desafio.validation.SwapiValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by matto on 07/02/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlanetasApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PlanetaControllerTests {

    @Autowired
    PlanetaRepository planetaRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void init() {
        planetaRepository.deleteAll();
    }

    //INICIO - TESTES DE CADASTRO
    @Test
    public void cadastrarPlanetaComErroPorBadRequest() throws URISyntaxException {

        List<Planeta> planetas = Utility.getPlanetasDefault();

        Planeta planeta = planetas.get(0);

        String planetaJson = Utility.getJsonCadastroPlanetaBadRequest(planeta);

        RequestEntity<String> entity =  RequestEntity
                .post(new URI("/planetas"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(planetaJson);

        ResponseEntity<PlanetaResponseBody> exchange = restTemplate
                .exchange(entity, PlanetaResponseBody.class);

        Assert.assertNotNull(exchange);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, exchange.getStatusCode());
    }


    //INICIO - TESTES DE LISTAGEM
    @Test
    public void listandoPlanetasSemNenhumCadastradoComStatusNotFound() throws URISyntaxException {

        RequestEntity<Void> entity =  RequestEntity
                .get(new URI("/planetas"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseBody> exchange = restTemplate
                .exchange(entity, PlanetaResponseBody.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
        Assert.assertEquals("Nenhum planeta encontrado", exchange.getBody().getDescription());
    }


    //INICIO - TESTES DE BUSCA POR NOME
    @Test
    public void buscarPlanetaPorNomeComNomeVazioComStatusNotFound() throws URISyntaxException, SwapiValidationException {

        RequestEntity<Void> entity =  RequestEntity
                .get(new URI("/planetas/?nome="))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseBody> exchangeBusca = restTemplate
                .exchange(entity, PlanetaResponseBody.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, exchangeBusca.getStatusCode());
        Assert.assertNotNull(exchangeBusca);
        Assert.assertEquals("Nenhum planeta encontrado", exchangeBusca.getBody().getDescription());
    }

    @Test
    public void buscarPlanetaPorNomeComStatusNotFound() throws URISyntaxException {

        RequestEntity<Void> entity =  RequestEntity
                .get(new URI("/planetas/?nome=Tatooine"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseBody> exchange = restTemplate
                .exchange(entity, PlanetaResponseBody.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
        Assert.assertEquals("Nenhum planeta encontrado", exchange.getBody().getDescription());
    }


    //INICIO - TESTES DE BUSCA POR ID
    @Test
    public void buscarPlanetaPorIDComIdZeroComStatusNotFound() throws URISyntaxException, SwapiValidationException {

        RequestEntity<Void> entity =  RequestEntity
                .get(new URI("/planetas/0"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseBody> exchangeBusca = restTemplate
                .exchange(entity, PlanetaResponseBody.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, exchangeBusca.getStatusCode());
        Assert.assertNotNull(exchangeBusca);
        Assert.assertEquals("Nenhum planeta encontrado", exchangeBusca.getBody().getDescription());
    }

    @Test
    public void buscarPlanetaPorIdComStatusNotFound() throws URISyntaxException {

        RequestEntity<Void> entity =  RequestEntity
                .get(new URI("/planetas/2"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseBody> exchange = restTemplate
                .exchange(entity, PlanetaResponseBody.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
        Assert.assertEquals("Nenhum planeta encontrado", exchange.getBody().getDescription());
    }


    //INICIO - TESTES DE DELETE
    @Test
    public void deletarPlanetaPeloIdComStatusNotFound() throws URISyntaxException {

        RequestEntity<Void> entity =  RequestEntity
                .delete(new URI("/planetas/99"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseBody> exchange = restTemplate
                .exchange(entity, PlanetaResponseBody.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
        Assert.assertEquals("Nenhum planeta encontrado", exchange.getBody().getDescription());
    }

    @Test
    public void deletarTodosOsPlanetasComStatusNotFound() throws URISyntaxException {

        RequestEntity<Void> entity =  RequestEntity
                .delete(new URI("/planetas"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseBody> exchange = restTemplate
                .exchange(entity, PlanetaResponseBody.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
        Assert.assertEquals("Não existem planetas para deletar", exchange.getBody().getDescription());
    }




}
