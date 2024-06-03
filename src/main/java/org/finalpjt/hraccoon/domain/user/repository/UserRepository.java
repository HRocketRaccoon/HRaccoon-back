package org.finalpjt.hraccoon.domain.user.repository;

import java.util.Optional;

import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUserNo(Long userNo);

	Optional<User> findByUserId(String userId);

	boolean existsByUserEmail(String userEmail);

	Page<User> findAll(Specification<User> spec, Pageable pageable);
}
