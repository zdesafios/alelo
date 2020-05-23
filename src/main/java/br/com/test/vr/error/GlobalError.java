package br.com.test.vr.error;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.ServerRequest;

@Component
public class GlobalError extends DefaultErrorAttributes {

	@Override
	public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
		var throwable = getError(request);
		var map = new HashMap<String, Object>();
		var originalMap = super.getErrorAttributes(request, options);
		
		if(throwable instanceof WebExchangeBindException) {
			var webExchangeBindException = (WebExchangeBindException) throwable;
			map.put("status", webExchangeBindException.getStatus().value());
			map.put("erros", webExchangeBindException.getAllErrors());
			map.put("message", "Validations");
			return map;			
		} else {
			map.put("status", originalMap.get("status"));
			map.put("message", originalMap.get("message"));
		}
		
		return map;
	}

}
