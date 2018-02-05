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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
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
            responseBody.setDescription("Erro interno: " + errorCode);

            log.error("Erro no cadastro do planeta: {} - {}", errorCode, e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }

    }

    private PlanetaResponseBody buildErrorResponse(BindingResult bindingResult) {
        List<String> errors = bindingResult.getFieldErrors()
                .stream()
                .map(fieldError -> bindingResult.getFieldError(fieldError.getField()).getDefaultMessage())
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
