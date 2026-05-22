package com.barbearia.fahcortes.infra.controller.exception;

public class EntidadeNaoEncontradaException extends RuntimeException {

    public EntidadeNaoEncontradaException(String entidade, Long id) {
        super(entidade + " com id: " + id + " não encontrado(a)");
    }

    public EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
