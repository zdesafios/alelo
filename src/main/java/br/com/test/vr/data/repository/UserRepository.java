package br.com.test.vr.data.repository;

import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.test.vr.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Cacheable(cacheNames = "users", key = "#userId", unless = "#result == null")
	Optional<User> findById(Long userId);
	
}
