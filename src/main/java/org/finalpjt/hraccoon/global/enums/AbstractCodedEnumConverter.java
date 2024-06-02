package org.finalpjt.hraccoon.global.enums;

import java.util.Arrays;
import java.util.Objects;

import jakarta.persistence.AttributeConverter;

public abstract class AbstractCodedEnumConverter<T extends Enum<T> & CodedEnum<E>, E> implements AttributeConverter<T, E> {
	private final Class<T> enumClass;

	public AbstractCodedEnumConverter(Class<T> enumClass) {
		this.enumClass = enumClass;
	}

	@Override
	public E convertToDatabaseColumn(T attribute) {
		return attribute.getCode();
	}

	@Override
	public T convertToEntityAttribute(E dbData) {
		if (Objects.isNull(dbData))  {
			return null;
		}

		return Arrays.stream(enumClass.getEnumConstants())
			.filter(e -> e.getCode().equals(dbData))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unknown database value: " + dbData));
	}
}
