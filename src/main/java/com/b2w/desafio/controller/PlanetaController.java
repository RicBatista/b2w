package com.b2w.desafio.controller;

import com.b2w.desafio.commons.PlanetaRequestBody;
import com.b2w.desafio.commons.PlanetaResponseBody;
import com.b2w.desafio.model.Planeta;
import com.b2w.desafio.service.PlanetaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by matto on 05/02/2018.
 */
@Slf4j
@RestController
public class PlanetaController {

    @Autowired
    private PlanetaService planetaService;

    /**
     * Endpoint para adicionar/criar um planeta (com nome, clima e terreno)
     *
     * @param planetaRequest
     *
     * @return HttpEntity<PlanetaResponseBody>
     */
    @RequestMapping(path = "/planetas", method = RequestMethod.POST)
    public HttpEntity<PlanetaResponseBody> create(@Valid @RequestBody PlanetaRequestBody planetaRequest,
                                                  BindingResult bindingResult) {

        try {

            if (bindingResult.hasErrors()) {
                log.error("Erros na requisicao do cliente: {}", bindingResult.toString());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrorResponse(bindingResult));
            }

            String planetaNome = "";

            if(!planetaRequest.getPlaneta().getNome().isEmpty()) {
                planetaNome = planetaRequest.getPlaneta().getNome();
            }

            if(planetaService.isThereAPlanetNamed(planetaNome)) {

                PlanetaResponseBody responseBody = new PlanetaResponseBody();
                responseBody.setDescription("Planeta já existente.");

                log.error("Nao e permitido criar planeta com um nome já existente.");

                return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);

            } else {

                Planeta planeta = planetaService.create(planetaRequest.getPlaneta());

                PlanetaResponseBody responseBody = new PlanetaResponseBody(planeta);

                log.info("Planeta adicionado com sucesso:  [{}]", planeta);

                return ResponseEntity.status(HttpStatus.OK).body(responseBody);

            }

        } catch (ConstraintViolationException e) {
            /**
             * Alguns dados de requisicao invalidos podem nao ser processados no request devido ao nivel de aninhamento
             * dos objetos. Entretanto, esses erros de validacao podem ser pegos em outras partes do codigo e acabam lancando
             * ConstraintViolationException. Por isso este catch foi criado.
             */
            log.error("Erro no insert: {}", e.getMessage(), e.getConstraintViolations());


            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrorResponse(e));

        } catch (Exception e) {

            String errorCode = String.valueOf(System.nanoTime());

            PlanetaResponseBody responseBody = new PlanetaResponseBody();
            responseBody.setDescription("Houve um erro interno no servidor: " + errorCode);

            log.error("Erro no cadastro do planeta: {} - {}", errorCode, e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }

    }

    /**
     * Endpoint para listar os planetas cadastrados
     *
     * @return HttpEntity<PlanetaResponseBody> planeta
     */
    @RequestMapping(path = "/planetas", method = RequestMethod.GET)
    public HttpEntity<PlanetaResponseBody> findAll() {

        PlanetaResponseBody responseBody = new PlanetaResponseBody();

        try {

            List<Planeta> planetas = planetaService.findAll();

            if (!planetas.isEmpty()) {

                responseBody.setPlanetas(planetas);

                log.info("Busca de planetas com exito");

                return ResponseEntity.status(HttpStatus.OK).body(responseBody);

            } else {

                responseBody.setDescription("Nenhum planeta encontrado");

                log.error("Nenhum planeta encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
            }

        } catch (Exception e) {
            String errorCode = String.valueOf(System.nanoTime());

            responseBody.setDescription("Houve um erro interno no servidor: " + errorCode);

            log.error("Erro ao listar os planetas cadastrados: {} - {}", errorCode, e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }

    }

    /**
     * Endpoint para buscar um planeta pelo nome
     *
     * @param nome
     *
     * @return HttpEntity<PlanetaResponseBody>
     */
    @RequestMapping(path = "/planetas/", method = RequestMethod.GET)
    public HttpEntity<PlanetaResponseBody> findByName(@RequestParam("nome") String nome) {

        PlanetaResponseBody responseBody = new PlanetaResponseBody();

        try {

            Optional<Planeta> planetaOptional = planetaService.findByNome(nome);

            if (planetaOptional.isPresent()) {
                Planeta planeta = planetaOptional.get();

                responseBody.setPlaneta(planeta);

                log.info("Busca de planeta pelo nome com exito: [{}]", planeta);

                return ResponseEntity.status(HttpStatus.OK).body(responseBody);

            } else {
                responseBody.setDescription("Nenhum planeta encontrado");

                log.error("Nenhum planeta encontrado pelo o nome: {}", nome);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
            }

        } catch (Exception e) {
            String errorCode = String.valueOf(System.nanoTime());

            responseBody.setDescription("Houve um erro interno no servidor: " + errorCode);

            log.error("Erro na exibição do Planeta solicitado com o nome: {} - {} - {}", nome, errorCode, e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }

    }

    /**
     * Endpoint para buscar um planeta pelo id
     *
     * @param id
     *
     * @return HttpEntity<PlanetaResponseBody>
     */
    @RequestMapping(path = "/planetas/{id}", method = RequestMethod.GET)
    public HttpEntity<PlanetaResponseBody> findById(@PathVariable String id) {

        PlanetaResponseBody responseBody = new PlanetaResponseBody();

        try {

            //TODO - TESTAR BATENDO AQUI COM ID NULO
            Optional<Planeta> planetaOptional = planetaService.findById(id);

            if (planetaOptional.isPresent()) {

                Planeta planeta = planetaOptional.get();

                responseBody.setPlaneta(planeta);

                log.info("Busca de planeta com exito: [{}]", planeta);

                return ResponseEntity.status(HttpStatus.OK).body(responseBody);

            } else {
                responseBody.setDescription("Nenhum planeta encontrado");

                log.error("Nenhum planeta encontrado pelo id: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
            }

        } catch (Exception e) {
            String errorCode = String.valueOf(System.nanoTime());

            responseBody.setDescription("Houve um erro interno no servidor: " + errorCode);

            log.error("Erro na exibição do Planeta solicitado com o ID: {} - {} - {}", id, errorCode, e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }

    }

    /**
     * Endpoint para deletar um planeta pelo id
     *
     * @param id
     * @return HttpEntity<PlanetaResponseBody>
     */
    @RequestMapping(path = "/planetas/{id}", method = RequestMethod.DELETE)
    public HttpEntity<PlanetaResponseBody> deleteById(@PathVariable String id) {

        PlanetaResponseBody responseBody = new PlanetaResponseBody();

        if(id.isEmpty()) {

            responseBody.setDescription("Id esta nulo");

            log.error("id esta nulo");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);

        } else {

            try {

                //TODO - TESTAR BATENDO AQUI COM ID NULO
                Optional<Planeta> planetaOptional = planetaService.findById(id);

                if (planetaOptional.isPresent()) {

                    Planeta planeta = planetaOptional.get();

                    planetaService.delete(planeta);

                    //responseBody.setDescription("Planeta deletado pelo id:" + id);

                    log.info("Planeta deletado pelo id: {}", id);

                    //return ResponseEntity.status(HttpStatus.OK).body(responseBody);
                    return ResponseEntity.status(HttpStatus.OK).build();

                } else {
                    responseBody.setDescription("Nenhum planeta encontrado");

                    log.error("Nenhum planeta encontrado pelo id: {}", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
                }

            } catch (Exception e) {

                String errorCode = String.valueOf(System.nanoTime());

                responseBody.setDescription("Houve um erro interno no servidor: " + errorCode);

                log.error("Erro ao deletar o Planeta solicitado com o ID: {} - {} - {}", id, errorCode, e.getMessage(), e);

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
            }

        }

    }

    /**
     * Endpoint para deletar um planeta pelo nome
     *
     * @param nome
     * @return
     */
    @RequestMapping(path = "/planetas/", method = RequestMethod.DELETE)
    public HttpEntity<PlanetaResponseBody> deleteByNome(@RequestParam("nome") String nome) {

        PlanetaResponseBody responseBody = new PlanetaResponseBody();

        if(nome.isEmpty()) {

            responseBody.setDescription("nome esta nulo");

            log.error("nome esta nulo");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);

        } else {

            try {

                //TODO - TESTAR BATENDO AQUI COM NOME NULO
                Optional<Planeta> planetaOptional = planetaService.findById(nome);

                if (planetaOptional.isPresent()) {

                    Planeta planeta = planetaOptional.get();

                    planetaService.delete(planeta);

                    //responseBody.setDescription("Planeta deletado pelo id:" + id);

                    log.info("Planeta deletado pelo nome: {}", nome);

                    //return ResponseEntity.status(HttpStatus.OK).body(responseBody);
                    return ResponseEntity.status(HttpStatus.OK).build();

                } else {
                    responseBody.setDescription("Nenhum planeta encontrado");

                    log.error("Nenhum planeta encontrado pelo nome: {}", nome);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
                }

            } catch (Exception e) {

                String errorCode = String.valueOf(System.nanoTime());

                responseBody.setDescription("Houve um erro interno no servidor: " + errorCode);

                log.error("Erro ao deletar o Planeta solicitado com o nome: {} - {} - {}", nome, errorCode, e.getMessage(), e);

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
            }

        }

    }

    private PlanetaResponseBody buildErrorResponse(BindingResult bindingResult) {

        List<String> errors = bindingResult.getFieldErrors()
                .stream()
                .map(
                        fieldError -> bindingResult.getFieldError(
                                fieldError.getField()
                        )
                        .getDefaultMessage()
                )
                .collect(Collectors.toList());

        PlanetaResponseBody responseBody = new PlanetaResponseBody();
        responseBody.setDescription(errors.toString());
        return responseBody;
    }

    private PlanetaResponseBody buildErrorResponse(ConstraintViolationException cve) {
        List<String> errors = cve.getConstraintViolations()
                .stream()
                .map(v -> v.getMessageTemplate())
                .collect(Collectors.toList());

        PlanetaResponseBody responseBody = new PlanetaResponseBody();
        responseBody.setDescription(errors.toString());

        return responseBody;
    }
}
