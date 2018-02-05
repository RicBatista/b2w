package com.b2w.desafio.controller;

import com.b2w.desafio.service.PlanetaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by matto on 05/02/2018.
 */
@Slf4j
@RestController
public class PlanetaController {

    @Autowired
    private PlanetaService planetaService;
}
