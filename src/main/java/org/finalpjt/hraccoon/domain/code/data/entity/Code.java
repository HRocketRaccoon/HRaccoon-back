package org.finalpjt.hraccoon.domain.code.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Code {

	@Id
	@Column(name = "code_no")
	private String codeNo;

	@Column(name = "code_name", nullable = false)
	private String codeName;
}
