package ru.jsms.backend.security.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.jsms.backend.security.domain.Role;
import ru.jsms.backend.security.entity.converter.RolesConverter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    @Convert(converter = RolesConverter.class)
    private Set<Role> roles;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private UserData userData;

    public void setUserData(UserData userData) {
        this.userData = userData;
        userData.setUser(this);
    }

    public User(String password, Set<Role> roles) {
        this.password = password;
        this.roles = roles;
    }
}
