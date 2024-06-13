package org.finalpjt.hraccoon.domain.user.data.dto.response;

import org.finalpjt.hraccoon.domain.user.data.entity.Ability;
import org.finalpjt.hraccoon.domain.user.data.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AbilityResponse {

	private Long abilityNo;
	private String abilityName;

	public AbilityResponse(Ability ability) {
		this.abilityNo = ability.getAbilityNo();
		this.abilityName = ability.getAbilityName();
	}

	public void transferCode(String abilityName) {
		this.abilityName = abilityName;
	}
}
