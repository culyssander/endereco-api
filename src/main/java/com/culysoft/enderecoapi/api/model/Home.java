package com.culysoft.enderecoapi.api.model;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class Home extends RepresentationModel<Home>{
	private String mensagem;
	private String descricao;
}
