package com.culysoft.enderecoapi.model.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false, of = {"id"})
@Entity
public class Pais {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;
	
	@OneToOne
	@JoinColumn(name = "capital")
	private Provincia capital;
	
	private String fusoHorario;
	
	private String codigoInternet;
	
	private String codigoTelefone;
	
	@OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "pais_provincia", joinColumns = @JoinColumn(name = "pais_id"),
            inverseJoinColumns = @JoinColumn(name = "provincia_id", table = "pais"))
	private List<Provincia> provincias;
}
