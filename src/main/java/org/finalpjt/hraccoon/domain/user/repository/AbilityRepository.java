package org.finalpjt.hraccoon.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.finalpjt.hraccoon.domain.user.data.entity.Ability;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AbilityRepository extends JpaRepository<Ability,Long> {

	@Query("select a from Ability a where a.user.userId = :userId")
	List<Ability> findByUserId(String userId);

	//안 쓸듯
	@Query("select a.user from Ability a where a.abilityName = :abilityName")
	List<User> findUserByAbilityName(String abilityName);
}
