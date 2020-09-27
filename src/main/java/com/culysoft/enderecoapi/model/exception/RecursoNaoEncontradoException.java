package com.culysoft.enderecoapi.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Recurso não encontrado.")
public class RecursoNaoEncontradoException extends RuntimeException   {

	private static final long serialVersionUID = 1L;
	
	public RecursoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
}
