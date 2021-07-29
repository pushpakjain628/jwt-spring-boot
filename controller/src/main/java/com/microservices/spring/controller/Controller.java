package com.microservices.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.spring.exceptions.ExceptionHandling;
import com.microservices.spring.model.User;
import com.microservices.spring.service.UserServiceImpl;

@RestController
@RequestMapping("/user")
public class Controller extends ExceptionHandling{

	@Autowired
	UserServiceImpl us;
	
	@GetMapping("/")
	public List<User> getAll(){
		return us.findAll();
	}
}
