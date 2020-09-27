package com.culysoft.enderecoapi.api.model;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.culysoft.enderecoapi.model.domain.Provincia;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false, of = {"id"})
@Data
public class PaisModel extends RepresentationModel<PaisModel>{
	
	private Long id;
	private String nome;
	private String capitalNome;
	private String fusoHorario;
	private String codigoInternet;
	private String codigoTelefone;
	private List<Provincia> provincias;
}
