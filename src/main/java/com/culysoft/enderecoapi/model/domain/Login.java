package com.culysoft.enderecoapi.model.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class Login {

	@NotBlank
	@Size(max = 30)
	private String username;
	@NotBlank
	@Size(max = 30)
	private String password;
}
