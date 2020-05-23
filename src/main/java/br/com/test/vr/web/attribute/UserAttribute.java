package br.com.test.vr.web.attribute;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAttribute {
	private Long id;
	private String name;
	private String email;
	
}
