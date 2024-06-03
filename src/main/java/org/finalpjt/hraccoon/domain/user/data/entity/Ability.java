package org.finalpjt.hraccoon.domain.user.data.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ability {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long abilityNo;

	@Column(name = "ability_name", nullable = false)
	private String abilityName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_no", nullable = false)
	private User user;

	@Builder
	public Ability(String abilityName, User user) {
		this.abilityName = abilityName;
		this.user = user;
	}
}
