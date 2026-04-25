package com.caridade.exception.item;

import com.caridade.controller.ItemDoacaoController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(assignableTypes = ItemDoacaoController.class)
public class ItemDoacaoExceptionHandler {

    @ExceptionHandler(ItemDoacaoNaoEncontradoException.class)
    public ResponseEntity<ItemDoacaoErroResponse> handleItemNaoEncontrado(ItemDoacaoNaoEncontradoException ex) {
        ItemDoacaoErroResponse erro = new ItemDoacaoErroResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Item de doação não encontrado",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(ItemDoacaoOperacaoInvalidaException.class)
    public ResponseEntity<ItemDoacaoErroResponse> handleOperacaoInvalida(ItemDoacaoOperacaoInvalidaException ex) {
        ItemDoacaoErroResponse erro = new ItemDoacaoErroResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Operação inválida no item",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
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