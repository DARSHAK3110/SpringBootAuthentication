package com.training.authentication.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.training.authentication.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@Modifying
	@Query(value = "update user set deleted_at = now() where user_id= :userId", nativeQuery = true)
	void deleteUser(Long userId);

	List<User> findAllByDeletedAtIsNull();

	Optional<User> findByUserIdAndDeletedAtIsNull(Long userId);
}
