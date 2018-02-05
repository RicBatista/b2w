package com.b2w.desafio.service;

import com.b2w.desafio.model.Planeta;
import com.b2w.desafio.repository.PlanetaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by matto on 05/02/2018.
 */
@Slf4j
@Service
public class PlanetaService {

    @Autowired
    private PlanetaRepository planetaRepository;

    /**
     * Busca de todos os planetas cadastrados
     *
     * @return List<Planeta> planetas
     */
    public List<Planeta> findAll() {
        return planetaRepository.findAll();
    }

    /**
     * Busca de planeta pelo ID
     *
     * @param id
     * @return Optional<Planeta> planeta
     */
    public Optional<Planeta> findById(String id) {
        return Optional.ofNullable(planetaRepository.findOne(id));
    }

    /**
     * Busca de planeta pelo Nome
     *
     * @param nome
     * @return
     */
    public Optional<Planeta> findByNome(String nome) {
        return Optional.ofNullable(planetaRepository.findByNome(nome));
    }

    /**
     * Adicionar um planeta (com nome, clima e terreno)
     *
     * @param planetaRequest
     *
     * @return Planeta planeta
     */
    public Planeta create(Planeta planetaRequest) {

        Planeta planeta = new Planeta();
        planeta.setNome(planetaRequest.getNome());
        planeta.setClima(planetaRequest.getClima());
        planeta.setTerreno(planetaRequest.getTerreno());

        return planetaRepository.save(planeta);
    }

    /**
     * Verifica se existe algum Planeta criado com o nome passado pelo parametro
     *
     * @param planetaNome
     *
     * @return boolean
     */
    public boolean isThereAPlanetNamed(String planetaNome) {
        Optional<Planeta> planetaOptional = this.findByNome(planetaNome);

        if(planetaOptional.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

}
