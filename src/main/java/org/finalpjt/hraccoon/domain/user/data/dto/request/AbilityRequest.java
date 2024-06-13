package org.finalpjt.hraccoon.domain.user.data.dto.request;

import org.finalpjt.hraccoon.domain.user.data.entity.Ability;
import org.finalpjt.hraccoon.domain.user.data.entity.User;

import jakarta.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AbilityRequest {

	@NotBlank
	private String abilityName;

	public Ability toEntity(User user) {
		return Ability.builder()
			.abilityName(abilityName)
			.user(user)
			.build();
	}

	@Builder
	public AbilityRequest(String abilityName) {
		this.abilityName = abilityName;
	}
}
