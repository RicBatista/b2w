package com.b2w.desafio.controller;

import com.b2w.desafio.model.Planeta;
import com.b2w.desafio.repository.PlanetaRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by matto on 07/02/2018.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {SpringMongoConfiguration.class})

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestFindAll {

    @Autowired
    PlanetaRepository planetaRepository;

    @Before
    public void init() {
        planetaRepository.deleteAll();

        Planeta planeta = new Planeta();
        planeta.setNome("Alderaan");
        planeta.setClima("temperate");
        planeta.setTerreno("grasslands, mountains");

        Planeta planetaSaved = planetaRepository.save(planeta);
    }

    @Test
    public void happyTest() {
        List<Planeta> list = planetaRepository.findAll();

        assertEquals(1, list.size());
    }
}
