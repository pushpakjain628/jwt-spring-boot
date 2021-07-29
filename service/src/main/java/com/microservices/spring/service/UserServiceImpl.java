package com.microservices.spring.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.microservices.spring.model.User;
import com.microservices.spring.model.UserPrincipal;
import com.microservices.spring.repository.UserRepo;
import com.microservices.spring.service.defination.UserServiceDef;

@Service
@Transactional
@Qualifier("UserDetailsService")
public class UserServiceImpl implements UserServiceDef,UserDetailsService{

	private Logger LOGGER=LoggerFactory.getLogger(getClass());


	@Autowired
	UserRepo userRepo;



	@Override
	public User save(User entity) {
		return userRepo.save(entity);
	}

	@Override
	public List<User> findAll() {
		return userRepo.findAll();
	}


	public User findByEmail(String email) {
		return userRepo.findUserByEmail(email);
	}

	@Override
	public User findByuserId(Long id) {
		return userRepo.findById(id).get();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user=userRepo.findUserByUsername(username);
		if(user==null) {
			LOGGER.error("User not found by username {}",username);
			throw new UsernameNotFoundException("User not found by username"+username);
		} else {
			user.setLastLoginDateDisplay(user.getLastLoginDate());
			user.setLastLoginDate(new Date());
			userRepo.save(user);
			UserPrincipal userPrincipal=new UserPrincipal(user);
			LOGGER.info("Returning found user by username: {}",username);
			return userPrincipal;
		}

	}

}
