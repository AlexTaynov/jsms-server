package ru.jsms.backend.security.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class EmailConfirmation {

    @Id
    private String email;

    @NotNull
    private UUID code;

    @Column(columnDefinition = "boolean default false")
    private boolean confirmed;

    @NotNull
    private Instant expiryDate;
}


