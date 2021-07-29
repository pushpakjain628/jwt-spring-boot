package com.microservices.spring.model;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private String name;

	// many users has many roles
	@ManyToMany(mappedBy = "roles")
	private Collection<User> users;


	//one role has many privileges
	@ManyToMany
	@JoinTable(
			name="role_privileges",
			joinColumns = @JoinColumn(
					name="role_id", referencedColumnName = "id"
					),
			inverseJoinColumns = @JoinColumn(
					name="privilege_id",referencedColumnName = "id"
					)
			)
	private Collection<Privilege> privileges;

}
