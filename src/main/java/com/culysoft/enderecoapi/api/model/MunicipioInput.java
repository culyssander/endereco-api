package com.culysoft.enderecoapi.api.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MunicipioInput {

	private Long id;
	
	private String pais;

	private String provincia;
	
	@NotBlank
	@Size(max = 30)
	private String municipioNome;
}
