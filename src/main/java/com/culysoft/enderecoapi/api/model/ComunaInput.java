package com.culysoft.enderecoapi.api.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ComunaInput {
	
	private Long comunaId;
	private String pais;
	private String provincia;
	private String municipio;
	
	@NotBlank
	@Size(max = 30)
	private String comunaNome;
}
