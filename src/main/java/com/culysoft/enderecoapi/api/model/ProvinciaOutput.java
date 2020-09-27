package com.culysoft.enderecoapi.api.model;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class ProvinciaOutput extends RepresentationModel<ProvinciaOutput>{

	private Long id;
	private String nome;
}
