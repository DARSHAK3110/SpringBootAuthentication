package com.training.authentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.training.authentication.entity.RefreshToken;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	@Query(value = "select * from refresh_token where token= :token", nativeQuery = true)
	Optional<RefreshToken> findByToken(String token);
	
	@Transactional
	@Modifying
	@Query(value = "delete from refresh_token where user_id= :userId", nativeQuery = true)
	void deleteAllByUserId(Long userId);
}
