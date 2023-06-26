package com.training.authentication.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.training.authentication.entity.User;
import com.training.authentication.entity.User_;

import jakarta.persistence.criteria.Predicate;

@Component
public class UserSpecifications {

	public static Specification<User> searchSpecification(String keyword){
		return ((root,criteriaQuery, criteriaBuilder)->{
			List<Predicate> pred = new ArrayList<>();
			pred.add(criteriaBuilder.like(root.get(User_.PHONE_NUMBER).as(String.class), "%"+keyword.toLowerCase()+"%"));
			pred.add(criteriaBuilder.like(root.get(User_.FIRST_NAME).as(String.class), "%"+keyword.toLowerCase()+"%"));
			pred.add(criteriaBuilder.like(root.get(User_.LAST_NAME).as(String.class), "%"+keyword.toLowerCase()+"%"));
			
			return criteriaBuilder.or(pred.toArray(new Predicate[pred.size()]));
		});
	}
}
