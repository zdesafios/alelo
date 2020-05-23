package br.com.test.vr.mapper;

import org.springframework.stereotype.Component;

import br.com.test.vr.model.entity.Sale;
import br.com.test.vr.web.attribute.BusinessAttribute;
import br.com.test.vr.web.dto.SaleDto;
import br.com.test.vr.web.dto.request.CreateSaleRequest;

@Component
public class SaleMapper {

	public SaleDto entityToDto(Sale sale) {
		return SaleDto.builder()
			.id(sale.getId())
			.businessId(sale.getBusinessId())
			.userId(sale.getUserId())
			.price(sale.getPrice())
			.date(sale.getDate())
			.status(sale.getStatus().name())
			.tags(sale.getTags())
			.build();
	}

	public Sale requestToEntity(BusinessAttribute businesAttribute, CreateSaleRequest request) {
		var sale = new Sale();
		sale.setBusinessId(businesAttribute.getId());
		sale.setUserId(request.getUserId());
		sale.setPrice(request.getPrice());
		return sale;
	}
	
}
