package com.caridade.exception.beneficiario;

import com.caridade.controller.BeneficiarioController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(assignableTypes = BeneficiarioController.class)
public class BeneficiarioExceptionHandler {

    @ExceptionHandler(BeneficiarioNaoEncontradoException.class)
    public ResponseEntity<BeneficiarioErroResponse> handleNaoEncontrado(BeneficiarioNaoEncontradoException ex) {
        BeneficiarioErroResponse erro = new BeneficiarioErroResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Beneficiário não encontrado",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(BeneficiarioJaExisteException.class)
    public ResponseEntity<BeneficiarioErroResponse> handleJaExiste(BeneficiarioJaExisteException ex) {
        BeneficiarioErroResponse erro = new BeneficiarioErroResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Beneficiário já existe",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidacao(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(erro ->
                erros.put(erro.getField(), erro.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(erros);
    }
}