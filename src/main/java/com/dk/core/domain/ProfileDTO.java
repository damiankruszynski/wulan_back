package com.dk.core.domain;

import com.dk.security.login.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProfileDTO {
    private Long profileId;
    @Size(max = 50,message = "profileName can have up to 50 chars")
    @NotBlank(message = "profileName can't be empty")
    private String profileName;
    @JsonIgnore
    private User user;
}
