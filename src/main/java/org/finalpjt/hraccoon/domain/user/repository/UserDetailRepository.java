package org.finalpjt.hraccoon.domain.user.repository;

import org.finalpjt.hraccoon.domain.user.data.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
}
