package com.microservices.spring.zuul.edge.security.configurations;

import static com.microservices.spring.zuul.edge.security.constants.SecurityConstant.PUBLIC_URLS;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.microservices.spring.zuul.edge.security.filters.JWTAccessDeniedHandler;
import com.microservices.spring.zuul.edge.security.filters.JWTAthorizationFilter;
import com.microservices.spring.zuul.edge.security.filters.JWTAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{


	private JWTAccessDeniedHandler jwtAccessDeniedHandler;

	@Autowired
	private JWTAuthenticationEntryPoint jwtAuthorizationEntryPoint;

	@Autowired
	private JWTAthorizationFilter jwtAuthorizationFilter;

	@Autowired
	private UserDetailsService userDetailsService;



	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bcryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().cors().disable()
		.sessionManagement().sessionCreationPolicy(STATELESS)
		.and().authorizeRequests().antMatchers(PUBLIC_URLS).permitAll()
		.anyRequest().authenticated()
		.and().exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler)
		.authenticationEntryPoint(jwtAuthorizationEntryPoint)
		.and()
		.addFilterBefore(jwtAuthorizationFilter,UsernamePasswordAuthenticationFilter.class);

	}


	@Bean
	public BCryptPasswordEncoder bcryptPasswordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManagerBean();
	}
}
