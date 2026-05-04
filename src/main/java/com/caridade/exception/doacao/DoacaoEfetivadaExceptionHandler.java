package com.caridade.exception.doacao;

import com.caridade.controller.RelatorioController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice(assignableTypes = RelatorioController.class)
public class DoacaoEfetivadaExceptionHandler {

    @ExceptionHandler(DoacaoEfetivadaNaoEncontradaException.class)
    public ResponseEntity<DoacaoEfetivadaErroResponse> handleDoacaoNaoEncontrada(DoacaoEfetivadaNaoEncontradaException ex) {
        DoacaoEfetivadaErroResponse erro = new DoacaoEfetivadaErroResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Doação efetivada não encontrada",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }
}
