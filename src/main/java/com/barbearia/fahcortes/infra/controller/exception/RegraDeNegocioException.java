package com.barbearia.fahcortes.infra.controller.exception;

public class RegraDeNegocioException extends RuntimeException {

    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
