package com.culysoft.enderecoapi.api.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProvinciaInput {
	
	private Long id;
	
	private String pais;

	@NotBlank
	@Size(max = 30)
	private String provinciaNome;
}
