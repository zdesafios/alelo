package br.com.test.vr.data.repository;

import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.test.vr.model.entity.Business;

@Repository
public interface BusinessRepository  extends JpaRepository<Business, Long> {

	@Query
	@Cacheable(cacheNames = "business", key = "#businessId", unless = "#result == null")
	Optional<Business> findById(Long businessId);
	
}
