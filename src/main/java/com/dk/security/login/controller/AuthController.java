package com.dk.security.login.controller;

import com.dk.core.exception.UserWithThisNameExist;
import com.dk.security.exception.NoCodeOrWasUsedException;
import com.dk.security.jwt.JwtUtils;
import com.dk.security.login.domain.Code;
import com.dk.security.login.domain.ERole;
import com.dk.security.login.domain.Role;
import com.dk.security.login.domain.User;
import com.dk.security.login.payload.request.LoginRequest;
import com.dk.security.login.payload.request.RegisterRequest;
import com.dk.security.login.payload.response.JwtResponse;
import com.dk.security.login.repository.RoleRepository;
import com.dk.security.login.repository.UserRepository;
import com.dk.security.login.services.CodeService;
import com.dk.security.login.services.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping
public class AuthController {

    private final  AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final CodeService codeService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository,
                          PasswordEncoder encoder, JwtUtils jwtUtils, CodeService codeService){
        this.authenticationManager =authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.codeService = codeService;
    }


    @PostMapping("/login")
    public JwtResponse authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        userRepository.setLastLoginDate(userDetails.getId());

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest) {

        if(userRepository.findByUsername(registerRequest.getUsername()).isPresent()){
            throw new UserWithThisNameExist();
        }

        Optional<Code>  code =  codeService.findCode(registerRequest.getCode());
        if(code.isPresent()){
           if(code.get().getUses() == 'T'){
               throw new NoCodeOrWasUsedException();
           }else{
               code.get().setUses('T');
           }
        }else{
           throw new NoCodeOrWasUsedException();
        }


        User user = new User(registerRequest.getUsername(),
                encoder.encode(registerRequest.getPassword()));

        log.info(encoder.encode(registerRequest.getPassword()));

        Set<String> strRoles = registerRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "super":
                        Role superUserRole = roleRepository.findByName(ERole.ROLE_SUPER_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(superUserRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        codeService.save(code.get());
        return ResponseEntity.ok("Done");
    }

}
