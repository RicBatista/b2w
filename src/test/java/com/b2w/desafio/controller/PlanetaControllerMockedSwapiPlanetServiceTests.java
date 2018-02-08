package com.b2w.desafio.controller;

import com.b2w.desafio.PlanetasApplication;
import com.b2w.desafio.commons.PlanetaResponseBody;
import com.b2w.desafio.commons.Utility;
import com.b2w.desafio.model.Planeta;
import com.b2w.desafio.model.SwapiPlanet;
import com.b2w.desafio.repository.PlanetaRepository;
import com.b2w.desafio.service.SwapiPlanetService;
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
public class PlanetaControllerMockedSwapiPlanetServiceTests {

    @Autowired
    PlanetaRepository planetaRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private SwapiPlanetService swapiPlanetServiceMocked;

    @Before
    public void init() {
        planetaRepository.deleteAll();
    }

    //INICIO - TESTES DE CADASTRO
    @Test
    public void cadastrarPlanetaComSucesso() throws URISyntaxException, SwapiValidationException {

        List<Planeta> planetas = Utility.getPlanetasDefault();

        Planeta planeta = planetas.get(0);

        SwapiPlanet swapiPlanet = Utility.getSwapiPlanetDeUmPlaneta(planeta);

        Mockito.when(
                swapiPlanetServiceMocked.getPlanetInSwapiByName(Mockito.any())
        ).thenReturn(swapiPlanet);

        String planetaJson = Utility.getJsonCadastroPlaneta(planeta);

        RequestEntity<String> entity =  RequestEntity
                .post(new URI("/planetas"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(planetaJson);

        ResponseEntity<PlanetaResponseBody> exchange = restTemplate
                .exchange(entity, PlanetaResponseBody.class);

        Assert.assertNotNull(exchange);
        Assert.assertEquals(HttpStatus.CREATED, exchange.getStatusCode());
        Assert.assertEquals(planeta.getNome(), exchange.getBody().getPlaneta().getNome());
        Assert.assertEquals(planeta.getClima(), exchange.getBody().getPlaneta().getClima());
        Assert.assertEquals(planeta.getTerreno(), exchange.getBody().getPlaneta().getTerreno());
        Assert.assertEquals(
                java.util.Optional.ofNullable(swapiPlanet.getFilms().size()),
                java.util.Optional.ofNullable(exchange.getBody().getPlaneta().getFilmesTotal()));
    }

    @Test
    public void cadastrarPlanetaComErroPorDuplicidade() throws URISyntaxException, SwapiValidationException {

        List<Planeta> planetas = Utility.getPlanetasDefault();

        Planeta planeta = planetas.get(0);

        this.cadastrarPlanetaComSwapiPlanetServiceMocked(planeta);

        /*************** FIM DO CADSATRO DO PRIMEIRO PLANETA *************************/

        RequestEntity<String> entity2 =  RequestEntity
                .post(new URI("/planetas"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Utility.getJsonCadastroPlaneta(planeta));

        ResponseEntity<PlanetaResponseBody> exchange2 = restTemplate
                .exchange(entity2, PlanetaResponseBody.class);

        Assert.assertNotNull(exchange2);
        Assert.assertEquals(HttpStatus.CONFLICT, exchange2.getStatusCode());
        Assert.assertEquals("Planeta já existente.", exchange2.getBody().getDescription());

    }

    @Test
    public void cadastrarPlanetaComErroPorNomeNaoLocalizadoEmSWAPI() throws URISyntaxException, SwapiValidationException {

        Mockito.when(
                swapiPlanetServiceMocked.getPlanetInSwapiByName(Mockito.any())
        ).thenThrow(
                new SwapiValidationException(
                        HttpStatus.NOT_FOUND,
                        "Planeta inexistente na API Star Wars"
                )
        );

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
        Assert.assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode());

    }

    @Test
    public void cadastrarPlanetaComErroPorErroInternoEmSWAPI() throws URISyntaxException, SwapiValidationException {

        Mockito.when(
                swapiPlanetServiceMocked.getPlanetInSwapiByName(Mockito.any())
        ).thenThrow(
                new SwapiValidationException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Planeta inexistente na API Star Wars"
                )
        );

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
    public void cadastrar3PlanetasComSucessoListandoTodosNoFinal() throws URISyntaxException, SwapiValidationException {

        List<Planeta> planetas = Utility.getPlanetasDefault();

        for (Planeta planeta : planetas) {

            ResponseEntity<PlanetaResponseBody> exchange = this.cadastrarPlanetaComSwapiPlanetServiceMocked(planeta);
        }

        /*************** FIM DO CADSATRO DOS 3 PLANETAS PARA LISTAGEM E TESTES *************************/

        RequestEntity<Void> entity =  RequestEntity
                .get(new URI("/planetas"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseBody> exchange = restTemplate
                .exchange(entity, PlanetaResponseBody.class);

        Assert.assertEquals(HttpStatus.OK, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
        Assert.assertEquals(3, exchange.getBody().getPlanetas().size());

        for (Planeta planeta : exchange.getBody().getPlanetas()){

            Assert.assertNotNull(planeta.getId());
            Assert.assertNotNull(planeta.getNome());
            Assert.assertNotNull(planeta.getClima());
            Assert.assertNotNull(planeta.getTerreno());
            Assert.assertNotNull(planeta.getFilmesTotal());
        }
    }


    //INICIO - TESTES DE BUSCA POR NOME
    @Test
    public void buscarPlanetaPorNomeComSucesso() throws URISyntaxException, SwapiValidationException {

        List<Planeta> planetas = Utility.getPlanetasDefault();

        Planeta planeta = planetas.get(0);

        ResponseEntity<PlanetaResponseBody> exchangeCadastro = this.cadastrarPlanetaComSwapiPlanetServiceMocked(planeta);

        /*************** FIM DO CADSATRO DO PLANETA PARA BUSCA POR NOME E TESTES *************************/

        RequestEntity<Void> entity =  RequestEntity
                .get(new URI("/planetas/?nome="+planeta.getNome()))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseBody> exchangeBusca = restTemplate
                .exchange(entity, PlanetaResponseBody.class);

        Assert.assertEquals(HttpStatus.OK, exchangeBusca.getStatusCode());
        Assert.assertNotNull(exchangeBusca);

        Assert.assertEquals(
                exchangeCadastro.getBody().getPlaneta().getId(),
                exchangeBusca.getBody().getPlaneta().getId()
        );
        Assert.assertEquals(planeta.getNome(), exchangeBusca.getBody().getPlaneta().getNome());
        Assert.assertEquals(planeta.getClima(), exchangeBusca.getBody().getPlaneta().getClima());
        Assert.assertEquals(planeta.getTerreno(), exchangeBusca.getBody().getPlaneta().getTerreno());
        Assert.assertEquals(
                exchangeCadastro.getBody().getPlaneta().getFilmesTotal(),
                exchangeBusca.getBody().getPlaneta().getFilmesTotal()
        );
    }


    //INICIO - TESTES DE BUSCA POR ID
    @Test
    public void deletarPlanetaPorIdComSucesso() throws URISyntaxException, SwapiValidationException {

        List<Planeta> planetas = Utility.getPlanetasDefault();

        Planeta planeta = planetas.get(0);

        ResponseEntity<PlanetaResponseBody> exchangeCadastro = this.cadastrarPlanetaComSwapiPlanetServiceMocked(planeta);

        /*************** FIM DO CADSATRO DO PLANETA PARA DELETAR POR ID E TESTES *************************/

        RequestEntity<Void> entity =  RequestEntity
                .delete(new URI("/planetas/"+exchangeCadastro.getBody().getPlaneta().getId()))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseBody> exchangeBusca = restTemplate
                .exchange(entity, PlanetaResponseBody.class);

        Assert.assertEquals(HttpStatus.NO_CONTENT, exchangeBusca.getStatusCode());
        Assert.assertNotNull(exchangeBusca);
    }

    @Test
    public void cadastrar3PlanetasComSucessoDeletandoTodosNoFinal() throws URISyntaxException, SwapiValidationException {

        List<Planeta> planetas = Utility.getPlanetasDefault();

        for (Planeta planeta : planetas) {

            this.cadastrarPlanetaComSwapiPlanetServiceMocked(planeta);
        }

        /*************** FIM DO CADSATRO DOS 3 PLANETAS PARA DELETAR E TESTES *************************/

        RequestEntity<Void> entity =  RequestEntity
                .delete(new URI("/planetas"))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseBody> exchange = restTemplate
                .exchange(entity, PlanetaResponseBody.class);

        Assert.assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
    }


    //INICIO - TESTES DE DELETE
    @Test
    public void buscarPlanetaPorIdComSucesso() throws URISyntaxException, SwapiValidationException {

        List<Planeta> planetas = Utility.getPlanetasDefault();

        Planeta planeta = planetas.get(0);

        ResponseEntity<PlanetaResponseBody> exchangeCadastro = this.cadastrarPlanetaComSwapiPlanetServiceMocked(planeta);

        /*************** FIM DO CADSATRO DO PLANETA PARA BUSCA POR NOME E TESTES *************************/

        RequestEntity<Void> entity =  RequestEntity
                .get(new URI("/planetas/"+exchangeCadastro.getBody().getPlaneta().getId()))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<PlanetaResponseBody> exchangeBusca = restTemplate
                .exchange(entity, PlanetaResponseBody.class);

        Assert.assertEquals(HttpStatus.OK, exchangeBusca.getStatusCode());
        Assert.assertNotNull(exchangeBusca);

        Assert.assertEquals(
                exchangeCadastro.getBody().getPlaneta().getId(),
                exchangeBusca.getBody().getPlaneta().getId()
        );
        Assert.assertEquals(planeta.getNome(), exchangeBusca.getBody().getPlaneta().getNome());
        Assert.assertEquals(planeta.getClima(), exchangeBusca.getBody().getPlaneta().getClima());
        Assert.assertEquals(planeta.getTerreno(), exchangeBusca.getBody().getPlaneta().getTerreno());
        Assert.assertEquals(
                exchangeCadastro.getBody().getPlaneta().getFilmesTotal(),
                exchangeBusca.getBody().getPlaneta().getFilmesTotal()
        );
    }


    //UTIL
    /**
     * DRY (DON'T REPEAT YOURSELF)
     * Método criado para auxiliar no cadastro e testes de cada planeta sem precisar reescrever código em cada teste
     *
     * @param planeta
     * @return ResponseEntity<PlanetaResponseBody>
     * @throws SwapiValidationException
     * @throws URISyntaxException
     */
    private ResponseEntity<PlanetaResponseBody> cadastrarPlanetaComSwapiPlanetServiceMocked(Planeta planeta) throws
            SwapiValidationException, URISyntaxException {

        SwapiPlanet swapiPlanet = Utility.getSwapiPlanetDeUmPlaneta(planeta);

        Mockito.when(
                swapiPlanetServiceMocked.getPlanetInSwapiByName(Mockito.any())
        ).thenReturn(swapiPlanet);

        String planetaJson = Utility.getJsonCadastroPlaneta(planeta);

        RequestEntity<String> entity =  RequestEntity
                .post(new URI("/planetas"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(planetaJson);

        ResponseEntity<PlanetaResponseBody> exchange = restTemplate
                .exchange(entity, PlanetaResponseBody.class);

        Assert.assertEquals(HttpStatus.CREATED, exchange.getStatusCode());
        Assert.assertNotNull(exchange);
        Assert.assertNotNull(exchange.getBody().getPlaneta().getId());
        Assert.assertEquals(planeta.getNome(), exchange.getBody().getPlaneta().getNome());
        Assert.assertEquals(planeta.getClima(), exchange.getBody().getPlaneta().getClima());
        Assert.assertEquals(planeta.getTerreno(), exchange.getBody().getPlaneta().getTerreno());
        Assert.assertEquals(3, exchange.getBody().getPlaneta().getFilmesTotal().intValue());

        return exchange;
    }
}
