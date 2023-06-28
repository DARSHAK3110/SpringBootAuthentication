package com.training.authentication.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
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
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Component
@Transactional
public class UserSpecifications {

	@PersistenceContext
	private EntityManager entityManager;

	public Page<UserResponseDto> searchSpecification(FilterDto searchWord) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserResponseDto> query = cb.createQuery(UserResponseDto.class);
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<User> root = query.from(User.class);
		root.alias("myRoot");
		Root<User> countRoot = cq.from(User.class);
		countRoot.alias("myRoot");
		cq.select(cb.count(countRoot));
		query.where(searchWord.toPredicate(root, query, cb));
		query.multiselect(root.get(User_.FIRST_NAME).alias("firstName"),
				root.get(User_.PHONE_NUMBER).alias("phoneNumber"), root.get(User_.LAST_NAME).alias("lastName"),
				root.get(User_.ROLE).alias("role"));
		Query dto = entityManager.createQuery(query);
		dto.setFirstResult((searchWord.getPageNumber()) * searchWord.getSetSize());
		dto.setMaxResults(searchWord.getSetSize());
		cq.where(searchWord.toPredicate(countRoot, cq, cb));
		Long totalUser = entityManager.createQuery(cq).getSingleResult();
		Pageable page = PageRequest.of(searchWord.getPageNumber(), searchWord.getSetSize());
		Page<UserResponseDto> users = PageableExecutionUtils.getPage(dto.getResultList(), page, ()->totalUser);
		return users;
	}
}
