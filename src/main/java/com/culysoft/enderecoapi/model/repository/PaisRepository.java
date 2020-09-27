package com.culysoft.enderecoapi.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.culysoft.enderecoapi.model.domain.Pais;

@Repository
public interface PaisRepository extends PagingAndSortingRepository<Pais, Long> {

	List<Pais> findAll();
	
	Optional<Pais> findByNome(String nome);
	
	List<Pais> findByProvinciasNome(String provincia);
	
}
