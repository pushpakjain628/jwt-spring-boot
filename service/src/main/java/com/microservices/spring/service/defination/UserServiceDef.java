package com.microservices.spring.service.defination;

import java.util.List;

import com.microservices.spring.model.User;

public interface UserServiceDef {
	User save(User entity);
	List<User> findAll();
	//User findByUsername(String username);
	User findByuserId(Long id);
}
