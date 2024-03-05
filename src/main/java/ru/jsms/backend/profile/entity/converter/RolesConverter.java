package ru.jsms.backend.profile.entity.converter;

import ru.jsms.backend.common.dto.Role;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class RolesConverter implements AttributeConverter<Set<Role>, String> {
    @Override
    public String convertToDatabaseColumn(Set<Role> roleSet) {
        return roleSet.stream().map(Role::getValue).collect(Collectors.joining(", "));
    }

    @Override
    public Set<Role> convertToEntityAttribute(String s) {
        return Arrays.stream(s.split(", ")).map(Role::valueOf).collect(Collectors.toSet());
    }
}
