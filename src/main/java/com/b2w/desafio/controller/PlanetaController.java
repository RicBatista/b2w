package com.b2w.desafio.controller;

import com.b2w.desafio.commons.PlanetaRequestBody;
import com.b2w.desafio.commons.PlanetaResponseBody;
import com.b2w.desafio.model.Planeta;
import com.b2w.desafio.service.PlanetaService;
import com.b2w.desafio.validation.SwapiValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

                return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
            }

        } catch (SwapiValidationException swapiException) {

            String errorCode = String.valueOf(System.nanoTime());

            PlanetaResponseBody responseBody = new PlanetaResponseBody();

            if(swapiException.getHttpStatus() == HttpStatus.NOT_FOUND) {
                responseBody.setDescription(
                        "Não é possível adicionar um planeta inexistente na API Star Wars"
                );
            } else {
                responseBody.setDescription(
                        "Houve um erro na API Star Wars ao consultar a existência do planeta para cadastro"
                );
            }

            log.error("Erro na SWAPI ao cadastrar o planeta: {} - {}",
                    errorCode, swapiException.getMessage(), swapiException);

            return ResponseEntity.status(swapiException.getHttpStatus()).body(responseBody);

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

        try {

            Optional<Planeta> planetaOptional = planetaService.findById(id);

            if (planetaOptional.isPresent()) {

                Planeta planeta = planetaOptional.get();

                planetaService.delete(planeta);

                log.info("Planeta deletado pelo id: {}", id);

                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

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

    /**
     * Endpoint para deletar todos os planetas
     *
     * @return
     */
    @RequestMapping(path = "/planetas", method = RequestMethod.DELETE)
    public HttpEntity<PlanetaResponseBody> deleteAll() {

        PlanetaResponseBody responseBody = new PlanetaResponseBody();

        try {

            //TODO - TESTAR BATENDO AQUI COM NOME NULO
            List<Planeta> planetaList = planetaService.findAll();

            if (!planetaList.isEmpty()) {

                planetaService.deleteAll();

                log.info("Todos os planetas foram deletados");

                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

            } else {
                responseBody.setDescription("Não existem planetas para deletar");

                log.error("Não existem planetas para deletar");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
            }

        } catch (Exception e) {

            String errorCode = String.valueOf(System.nanoTime());

            responseBody.setDescription("Houve um erro interno no servidor: " + errorCode);

            log.error("Erro ao deletar todos os Planetas: {} - {}", errorCode, e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    /**
     * Coletamos os erros de BindingResult para setar no ResponseBody
     *
     * @param bindingResult
     * @return PlanetaResponseBody
     */
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

}
