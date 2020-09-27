package com.culysoft.enderecoapi.model.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false, of = {"id"})
@Entity
public class Provincia extends RepresentationModel<Provincia> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;
	
	@OneToMany
    @JoinTable(name = "provincia_municipio", joinColumns = @JoinColumn(name = "provincia_id"),
            inverseJoinColumns = @JoinColumn(name = "municipio_id", table = "provincia"))
	private List<Municipio> municipios;
	
	public Provincia(String nome) {
		this.nome = nome;
	}
	
}
