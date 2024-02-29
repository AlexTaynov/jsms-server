package ru.jsms.backend.security.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.jsms.backend.security.domain.Role;
import ru.jsms.backend.security.entity.converter.RolesConverter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    private Long id;

    private String password;

    @Convert(converter = RolesConverter.class)
    private Set<Role> roles;
}
