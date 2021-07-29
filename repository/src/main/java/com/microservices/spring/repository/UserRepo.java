package com.microservices.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservices.spring.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{

	User findUserByUsername(String username);
	User findUserByEmail(String email);
}
