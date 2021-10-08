package com.dk.core.controller;

import com.dk.core.domain.Profile;
import com.dk.core.domain.ProfileDTO;
import com.dk.core.mapper.ProfileMapper;
import com.dk.core.payload.ProfileRequest;
import com.dk.core.service.ProfileService;
import com.dk.security.jwt.JwtUtils;
import com.dk.security.login.domain.User;
import com.dk.security.login.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("user")
@Slf4j
public class UserController {

    private ProfileService profileService;
    private ProfileMapper profileMapper;

    @Autowired
    public UserController(ProfileService profileService, ProfileMapper profileMapper){
        this.profileService = profileService;
        this.profileMapper = profileMapper;
    }

    @PostMapping("/addOrUpdateProfile")
    public ResponseEntity<ProfileDTO> addOrUpdateProfile(@RequestBody ProfileRequest profileRequest, HttpServletRequest request){
        Profile profile = profileMapper.mapToProfile(profileRequest, request );
        return ResponseEntity.ok().body(profileMapper.mapToProfileDTO(profileService.save(profile)));
    }

    @GetMapping("/profileList")
    public ResponseEntity<List<ProfileDTO>> getProfileList(HttpServletRequest request){
        String userName = profileMapper.getUserNameFromRequest(request);
        return ResponseEntity.ok().body( profileMapper.mapToProfileDtoList(profileService.getProfilesByUserName(userName)));
    }

}
