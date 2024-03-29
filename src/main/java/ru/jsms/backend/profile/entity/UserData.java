package ru.jsms.backend.profile.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

@Getter
@Setter
@NoArgsConstructor
@Builder
@Entity(name = "user_data")
@AllArgsConstructor
public class UserData {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @MapsId
    @OneToOne(mappedBy = "userData")
    @JoinColumn(name = "user_id")
    private User user;

    private String email;
    private String firstName;
    private String secondName;
}
