package br.com.test.vr.web.error;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ValidationErrorResponse {
	private int status;
	private String message;
	private List<ValidationError> erros;
}
