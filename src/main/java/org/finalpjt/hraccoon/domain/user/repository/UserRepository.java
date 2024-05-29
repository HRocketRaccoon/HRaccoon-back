package org.finalpjt.hraccoon.domain.user.repository;

import java.util.Optional;

import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUserNo(Long userNo);

	User findByUserId(String userId);
}
