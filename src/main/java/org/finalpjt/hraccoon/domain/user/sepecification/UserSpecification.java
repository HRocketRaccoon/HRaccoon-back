package org.finalpjt.hraccoon.domain.user.sepecification;


import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Subquery;

import org.finalpjt.hraccoon.domain.user.data.entity.Ability;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
	public static Specification<User> likeUserName(String user_name) {
		return new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				// 2) like
				return criteriaBuilder.like(root.get("userName"), "%" + user_name + "%");
			}
		};
	}

	public static Specification<User> likeUserId(String user_id) {
		return new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				// 2) like
				return criteriaBuilder.like(root.get("userId"), "%" + user_id + "%");
			}
		};
	}

	public static Specification<User> findByDepartment(String department) {
		return new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("userDepartment"), department);
			}
		};
	}

	public static Specification<User> findByAbility(List<Long> userNos) {
		return new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return root.get("userNo").in(userNos);
			}
		};
	}

}
