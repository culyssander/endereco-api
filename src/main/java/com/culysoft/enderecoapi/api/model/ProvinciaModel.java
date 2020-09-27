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
public class ProvinciaModel extends RepresentationModel<ProvinciaModel> {
	
	private String pais;
	private List<ProvinciaOutput> provincias;

}

