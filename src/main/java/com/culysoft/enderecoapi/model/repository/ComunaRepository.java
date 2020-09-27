package com.culysoft.enderecoapi.model.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.culysoft.enderecoapi.model.domain.Comuna;

@Repository
public interface ComunaRepository extends PagingAndSortingRepository<Comuna, Long> {

}
