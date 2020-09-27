package com.culysoft.enderecoapi.model.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
public class Municipio extends RepresentationModel<Municipio> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Size(max = 30)
	private String nome;
	
	@OneToMany
    @JoinTable(name = "municipio_comuna", joinColumns = @JoinColumn(name = "municipio_id"),
            inverseJoinColumns = @JoinColumn(name = "comuna_id", table = "municipio"))
	private List<Comuna> comunas;
	
	public Municipio(String nome) {
		this.nome = nome;
	}
	
}
