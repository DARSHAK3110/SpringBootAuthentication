package com.training.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.training.authentication.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
