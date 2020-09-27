package com.culysoft.enderecoapi.api.model;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ComunaOutput extends RepresentationModel<ComunaOutput> {

	private Long id;
	private String nome;
}
