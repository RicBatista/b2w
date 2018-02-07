package com.b2w.desafio.controller;

import com.b2w.desafio.PlanetasApplication;
import com.b2w.desafio.model.Planeta;
import com.b2w.desafio.repository.PlanetaRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by matto on 06/02/2018.
 */
@RunWith(SpringRunner.class)
@DataMongoTest
@SpringBootTest(classes = PlanetasApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PlanetaControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PlanetaRepository planetaRepository;

    @Before
    public void setUp() {

        /*
        Planeta planeta = new Planeta();
        planeta.setNome("Alderaan");
        planeta.setClima("temperate");
        planeta.setTerreno("grasslands, mountains");

        Planeta planetaSaved = planetaRepository.save(planeta);
        */
    }

    @Test
    public void cadastrarNovoPlanetaComSucesso() {

        /*
        String planetaJson = "{  \n" +
                "   \"planeta\":{  \n" +
                "      \"nome\":\"Alderaan\",\n" +
                "      \"clima\": \"temperate\",\n" +
                "      \"terreno\": \"grasslands, mountains\"\n" +
                "   }\n" +
                "}";
        */

        Planeta planeta = new Planeta();
        planeta.setNome("Alderaan");
        planeta.setClima("temperate");
        planeta.setTerreno("grasslands, mountains");

        Planeta planetaSaved = planetaRepository.save(planeta);

        Assert.assertNotNull(planetaSaved);

    }

    /*
    @Test
    public void create_should_create_new_student() {
        studentService.create("James Doe");
        List studs = studentRepository.findAll();
        assertTrue(studs.size() == 1);
        assertEquals("James Doe", studs.get(0).getName());
        assertNotNull(studs.get(0).getEnrollmentDate());
    }

    public void createCollection() {
        Counter counter = new Counter();
        counter.setSeq(0);
        counter.setId("Counter");
        mongoOperations.insert(counter);
    }
    */
}
