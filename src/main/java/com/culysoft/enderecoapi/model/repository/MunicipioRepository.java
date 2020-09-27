package com.culysoft.enderecoapi.model.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.culysoft.enderecoapi.model.domain.Municipio;

@Repository
public interface MunicipioRepository extends PagingAndSortingRepository<Municipio, Long> {

	Optional<Municipio> findByNome(String nome);

}
