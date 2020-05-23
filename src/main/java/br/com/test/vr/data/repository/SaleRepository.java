package br.com.test.vr.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.test.vr.model.entity.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
	
	@Cacheable(cacheNames = "sales::user", key = "#userId", unless = "#result == null or #result.isEmpty()")
	List<Sale> findByUserId(Long userId);
	
	@Cacheable(cacheNames = "sale", key = "#saleId", unless = "#result == null")
	Optional<Sale> findById(Long saleId) ;
	
	@CacheEvict(cacheNames = "sales::user", key = "#sale.userId")
	Sale save(Sale sale);

}
