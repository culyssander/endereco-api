package com.culysoft.enderecoapi.api.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class PaisInput {
	
	private Long id;
	
	@NotBlank
	@Size(max = 30)
	private String nome;
	
	@NotBlank
	@Size(max = 30)
	private String capitalNome;
	
	@NotBlank
	@Size(max = 10)
	private String fusoHorario;
	
	@NotBlank
	@Size(max = 15)
	private String codigoInternet;
	
	@NotBlank
	@Size(max = 5)
	private String codigoTelefone;
	
}
