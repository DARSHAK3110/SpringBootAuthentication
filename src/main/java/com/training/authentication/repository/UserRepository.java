package com.training.authentication.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.training.authentication.entity.User;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	@Modifying
	@Query(value = "update user set deleted_at = now() where phone_number= :phoneNumber", nativeQuery = true)
	void deleteUser(Long phoneNumber);
	
	public Page<User> findAll(Specification<User> spec, Pageable pageable);
	
	Optional<User> findByPhoneNumberAndDeletedAtIsNull(Long phoneNumber);
	
	Optional<User> findByUserIdAndDeletedAtIsNull(Long userId);
	
}
