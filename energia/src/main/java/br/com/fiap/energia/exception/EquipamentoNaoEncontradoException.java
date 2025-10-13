package br.com.fiap.energia.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EquipamentoNaoEncontradoException extends RuntimeException {

    public EquipamentoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

}
