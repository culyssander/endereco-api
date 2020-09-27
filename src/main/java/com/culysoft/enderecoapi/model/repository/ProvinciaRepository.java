package com.culysoft.enderecoapi.model.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.culysoft.enderecoapi.model.domain.Provincia;

@Repository
public interface ProvinciaRepository extends PagingAndSortingRepository<Provincia, Long> {
	
}
