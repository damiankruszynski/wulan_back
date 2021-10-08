package com.dk.core.domain;

import com.dk.security.login.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProfileDTO {
    private Long id;
    private String profileName;
    @JsonIgnore
    private User user;
}
