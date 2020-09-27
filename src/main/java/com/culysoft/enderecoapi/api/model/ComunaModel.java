package com.culysoft.enderecoapi.api.model;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class ComunaModel extends RepresentationModel<ComunaModel>{

	private String pais;
	private String provincia;
	private String municipio;
	private List<ComunaOutput> comunas;
}
