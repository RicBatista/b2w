package com.b2w.desafio.controller;

import com.b2w.desafio.PlanetasApplication;
import com.b2w.desafio.repository.PlanetaRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by matto on 06/02/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlanetasApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PlanetaControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PlanetaRepository planetaRepository;

    public void cadastrarNovoPlanetaComSucesso() {

    }
}
