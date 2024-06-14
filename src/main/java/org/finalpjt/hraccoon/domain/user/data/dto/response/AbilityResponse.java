package org.finalpjt.hraccoon.domain.user.data.dto.response;

import org.finalpjt.hraccoon.domain.user.data.entity.Ability;
import org.finalpjt.hraccoon.domain.user.data.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AbilityResponse {

	private Long abilityNo;
	private String abilityCode;
	private String abilityName;

	public AbilityResponse(Ability ability) {
		this.abilityNo = ability.getAbilityNo();
		this.abilityCode = ability.getAbilityName();
	}

	public void insertAbilityName(String abilityName) {
		this.abilityName = abilityName;
	}
	}
