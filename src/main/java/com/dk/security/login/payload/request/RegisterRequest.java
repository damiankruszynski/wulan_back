package com.dk.security.login.payload.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class RegisterRequest {
    private  String username;
    private  Set<String> role;
    private  String password;
    private  LocalDate dateAccountCreated;
    private  String code;

    public RegisterRequest(String username, Set<String> role, String password, String code){
        this.username = username;
        this.role = role;
        this.password = password;
        this.code = code;
    }

}
