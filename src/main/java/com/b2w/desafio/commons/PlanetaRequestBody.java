package com.b2w.desafio.commons;

import com.b2w.desafio.model.Planeta;
import lombok.Data;

import javax.validation.Valid;

/**
 * Created by matto on 05/02/2018.
 */
@Data
public class PlanetaRequestBody {

    @Valid
    private Planeta planeta;
}
