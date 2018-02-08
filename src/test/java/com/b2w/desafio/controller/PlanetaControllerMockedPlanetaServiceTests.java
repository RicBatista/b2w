package com.b2w.desafio.controller;

import com.b2w.desafio.PlanetasApplication;
import com.b2w.desafio.commons.PlanetaResponseBody;
import com.b2w.desafio.commons.Utility;
import com.b2w.desafio.model.Planeta;
import com.b2w.desafio.repository.PlanetaRepository;
import com.b2w.desafio.service.PlanetaService;
import com.b2w.desafio.validation.SwapiValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
public class PlanetaControllerMockedPlanetaServiceTests {

    @Autowired
    PlanetaRepository planetaRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private PlanetaService planetaServiceMocked;

    @Before
    public void init() {
        planetaRepository.deleteAll();
    }

    //INICIO - TESTES DE CADASTRO
    @Test
    public void cadastrarPlanetaComErroPorDuplicidade() throws URISyntaxException, SwapiValidationException {

        Mockito.when(
                planetaServiceMocked.isThereAPlanetNamed(Mockito.any())
        ).thenReturn(true);

        List<Planeta> planetas = Utility.getPlanetasDefault();

        Planeta planeta = planetas.get(0);

        String planetaJson = Utility.getJsonCadastroPlaneta(planeta);

        RequestEntity<String> entity =  RequestEntity
                .post(new URI("/planetas"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(planetaJson);

        ResponseEntity<PlanetaResponseBody> exchange = restTemplate
                .exchange(entity, PlanetaResponseBody.class);

        Assert.assertNotNull(exchange);
        Assert.assertEquals(HttpStatus.CONFLICT, exchange.getStatusCode());
        Assert.assertEquals("Planeta já existente.", exchange.getBody().getDescription());
    }

    @Test
    public void cadastrarPlanetaComErroInterno() throws URISyntaxException, SwapiValidationException {

        Mockito.when(
                planetaServiceMocked.isThereAPlanetNamed(Mockito.any())
        ).thenReturn(false);

        Mockito.when(
                planetaServiceMocked.create(Mockito.any())
        ).thenThrow(new RuntimeException());

        List<Planeta> planetas = Utility.getPlanetasDefault();

        Planeta planeta = planetas.get(0);

        String planetaJson = Utility.getJsonCadastroPlaneta(planeta);

        RequestEntity<String> entity =  RequestEntity
                .post(new URI("/planetas"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(planetaJson);

        ResponseEntity<PlanetaResponseBody> exchange = restTemplate
                .exchange(entity, PlanetaResponseBody.class);

        Assert.assertNotNull(exchange);
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exchange.getStatusCode());
    }


    //INICIO - TESTES DE LISTAGEM
    @Test
    public void listarPlanetasComErroInterno() throws URISyntaxException, SwapiValidationException {

        Mockito.when(
                planetaServiceMocked.findAll()
        ).thenThrow(new RuntimeException());

        RequestEntity<Void> entity =  RequestEntity
                .get(new URI("/planetas"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseBody> exchange = restTemplate
                .exchange(entity, PlanetaResponseBody.class);

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
    }


    //INICIO - TESTES DE BUSCA POR NOME
    @Test
    public void buscarPlanetaPorNomeComErroInterno() throws URISyntaxException {

        Mockito.when(
                planetaServiceMocked.findByNome(Mockito.any())
        ).thenThrow(new RuntimeException());

        RequestEntity<Void> entity =  RequestEntity
                .get(new URI("/planetas/?nome=Tatooine"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseBody> exchange = restTemplate
                .exchange(entity, PlanetaResponseBody.class);

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
    }


    //INICIO - TESTES DE BUSCA POR ID
    @Test
    public void buscarPlanetaPorIdComErroInterno() throws URISyntaxException {

        Mockito.when(
                planetaServiceMocked.findByNome(Mockito.any())
        ).thenThrow(new RuntimeException());

        RequestEntity<Void> entity =  RequestEntity
                .get(new URI("/planetas/2"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseBody> exchange = restTemplate
                .exchange(entity, PlanetaResponseBody.class);

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
    }


    //INICIO - TESTES DE DELETE
    @Test
    public void deletarPlanetaPorIdComErroInterno() throws URISyntaxException {

        Mockito.when(
                planetaServiceMocked.findById(Mockito.any())
        ).thenThrow(new RuntimeException());

        RequestEntity<Void> entity =  RequestEntity
                .delete(new URI("/planetas/2"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseBody> exchange = restTemplate
                .exchange(entity, PlanetaResponseBody.class);

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
    }

    @Test
    public void deletarTodosOsPlanetasComErroInterno() throws URISyntaxException {

        Mockito.when(
                planetaServiceMocked.findAll()
        ).thenThrow(new RuntimeException());

        RequestEntity<Void> entity =  RequestEntity
                .delete(new URI("/planetas"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseBody> exchange = restTemplate
                .exchange(entity, PlanetaResponseBody.class);

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
    }

}
