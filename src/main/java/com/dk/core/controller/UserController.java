package com.dk.core.controller;

import com.dk.core.domain.Profile;
import com.dk.core.domain.ProfileDTO;
import com.dk.core.mapper.ProfileMapper;
import com.dk.core.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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

    @PostMapping("/addProfile")
    public void addProfile(@Valid @RequestBody ProfileDTO profileDTO, HttpServletRequest request){
        Profile profile = profileMapper.mapToProfile(profileDTO,request, false);
        profileService.save(profile);
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<ProfileDTO> updateProfile(@Valid @RequestBody ProfileDTO profileDTO, HttpServletRequest request){
        Profile profile = profileMapper.mapToProfile(profileDTO,request, true);
        return ResponseEntity.ok().body(profileMapper.mapToProfileDTO(profileService.save(profile)));
    }

    @GetMapping("/profileList")
    public ResponseEntity<List<ProfileDTO>> getProfileList(HttpServletRequest request){
        String userName = profileMapper.getUserNameFromRequest(request);
        return ResponseEntity.ok().body( profileMapper.mapToProfileDtoList(profileService.getProfilesByUserName(userName)));
    }

    @DeleteMapping("/deleteProfile")
    public ResponseEntity<String> deleteProfile(@RequestParam Long profileId, HttpServletRequest request){
        profileService.deleteProfile(profileId);
        return ResponseEntity.ok("Done");
    }

}
