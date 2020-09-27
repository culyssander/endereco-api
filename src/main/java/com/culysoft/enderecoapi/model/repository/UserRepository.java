package com.culysoft.enderecoapi.model.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.culysoft.enderecoapi.model.domain.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long>{
	User findByUsername(String username);
}
