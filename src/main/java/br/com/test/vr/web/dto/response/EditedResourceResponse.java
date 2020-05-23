package br.com.test.vr.web.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EditedResourceResponse {
	private Long id;
	private final LocalDateTime date =  LocalDateTime.now();
}
