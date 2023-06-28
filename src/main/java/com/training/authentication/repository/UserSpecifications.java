package com.training.authentication.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.stereotype.Component;
import com.training.authentication.dto.request.FilterDto;
import com.training.authentication.dto.response.UserResponseDto;
import com.training.authentication.entity.User;
import com.training.authentication.entity.User_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Component
@Transactional
public class UserSpecifications {

	@PersistenceContext
	private EntityManager entityManager;
	
	private void createRestrictions(FilterDto searchWord, CriteriaBuilder cb, Root root, List<Predicate> pred) {
		if (!searchWord.getFirstName().equals("")) {
			pred.add(cb.like(root.get(User_.FIRST_NAME).as(String.class),
					"%" + searchWord.getFirstName().toLowerCase() + "%"));
			
		}
		if (!searchWord.getPhoneNumber().equals("")) {
			pred.add(cb.like(root.get(User_.PHONE_NUMBER).as(String.class),
					"%" + searchWord.getPhoneNumber().toLowerCase() + "%"));
		}
		if (!searchWord.getLastName().equals("")) {
			pred.add(cb.like(root.get(User_.LAST_NAME).as(String.class),
					"%" + searchWord.getLastName().toLowerCase() + "%"));
		}
		pred.add(cb.isNull(root.get(User_.DELETED_AT)));
	}

	public Map<String, Object> searchSpecification(FilterDto searchWord) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserResponseDto> query = cb.createQuery(UserResponseDto.class);
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<User> root = query.from(User.class);
		root.alias("myRoot");
		Root<User> countRoot = cq.from(User.class);
		countRoot.alias("myRoot");
		cq.select(cb.count(countRoot));
		
		 
		List<Predicate> pred = new ArrayList<>();
		List<Predicate> pred2 = new ArrayList<>();
		createRestrictions(searchWord, cb, root, pred);
		createRestrictions(searchWord, cb, countRoot, pred2);
		
		query.where(pred.toArray(new Predicate[pred.size()]));
		query.multiselect(root.get(User_.FIRST_NAME).alias("firstName"),
				root.get(User_.PHONE_NUMBER).alias("phoneNumber"), root.get(User_.LAST_NAME).alias("lastName"),
				root.get(User_.ROLE).alias("role"));
		Query dto = entityManager.createQuery(query);
		dto.setFirstResult((searchWord.getPageNumber()) * searchWord.getSetSize());
		dto.setMaxResults(searchWord.getSetSize());
		List<UserResponseDto> output = dto.getResultList();

		
		cq.where(pred2.toArray(new Predicate[pred2.size()]));
		Long totalUser = entityManager.createQuery(cq).getSingleResult();
		HashMap<String, Object> hash = new HashMap<>();
		hash.put("users", output);
		hash.put("totalUser", totalUser);
		return hash;
	}
}
