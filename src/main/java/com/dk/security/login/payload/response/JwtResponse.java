package com.dk.security.login.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private  String token;
    private  String type = "Bearer";
    private  Long id;
    private  String username;
    private  List<String> roles;

    public JwtResponse(String jwt, Long id, String username, List<String> roles) {
       this.token = jwt;
       this.id = id;
       this.username = username;
       this.roles = roles;
    }
}
