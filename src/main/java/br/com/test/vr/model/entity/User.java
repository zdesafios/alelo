package br.com.test.vr.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Table(name = "users")
@Getter @Setter
public class User implements Serializable {
	@Id
	@Setter(AccessLevel.NONE)
	private Long id;
	
	private String name;
	
	private String email;
	
	public User() {
		id = 123L;
	}
}
