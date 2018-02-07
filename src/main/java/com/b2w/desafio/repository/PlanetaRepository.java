package com.b2w.desafio.repository;

import com.b2w.desafio.model.Planeta;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by matto on 05/02/2018.
 */
public interface PlanetaRepository extends MongoRepository<Planeta, String> {

    Planeta findByNome(String nome);

    Planeta findById(long id);

    Planeta findByIdAndNome(String id, String nome);

    /*
    //Supports native JSON query string
    @Query("{nome:'?0'}")
    Planeta findCustomByNome(String nome);
    */
}
