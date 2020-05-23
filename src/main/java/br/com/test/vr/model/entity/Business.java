package br.com.test.vr.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "business")
@Getter @Setter
public class Business implements Serializable {
	@Id
	@Setter(AccessLevel.NONE)
	private Long id;
	
	private String name;
	
	public Business() {
		id = 123L;
	}
}
