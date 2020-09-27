package com.culysoft.enderecoapi.api.model;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class MunicipioModel extends RepresentationModel<MunicipioModel>{
	
	private String pais;
	private String provincia;
	private List<MunicipioOutput> municipios;

}
