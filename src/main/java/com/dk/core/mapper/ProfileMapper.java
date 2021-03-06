package com.dk.core.mapper;

import com.dk.core.domain.Profile;
import com.dk.core.domain.ProfileDTO;
import com.dk.core.exception.NoProfileException;
import com.dk.core.exception.NoUserException;
import com.dk.core.service.ProfileService;
import com.dk.security.jwt.JwtUtils;
import com.dk.security.login.domain.User;
import com.dk.security.login.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ProfileMapper {

    private UserRepository userRepository;
    private JwtUtils jwtUtils;
    private ProfileService profileService;

    public ProfileMapper(UserRepository userRepository, JwtUtils jwtUtils, ProfileService profileService){
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.profileService = profileService;
    }

    public ProfileDTO mapToProfileDTO(Profile profile){
        return new ProfileDTO(profile.getId(), profile.getProfileName(),profile.getUser());
    }

    public Profile mapToProfile(ProfileDTO profileDTO, HttpServletRequest request, boolean isUpdate){
        String username = getUserNameFromRequest(request);
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            Optional<Profile> profile = profileService.getProfileByProfileIdAndUserId(user.get().getId(), profileDTO.getProfileId());
            if(profile.isPresent()){
                profile.get().setProfileName(profileDTO.getProfileName());
                return profile.get();
            }
            else if(isUpdate){
                throw new NoProfileException();
            }
            else{
               return  new Profile(null, profileDTO.getProfileName(), user.get(), null);
            }
        }else{
            throw new NoUserException();
        }
    }

    public List<ProfileDTO> mapToProfileDtoList(List<Profile> profileList) {
        return profileList.stream().map(profile -> mapToProfileDTO(profile)).collect(Collectors.toList());
    }

    public String getUserNameFromRequest(HttpServletRequest httpServletRequest){
        return  jwtUtils.getUserNameFromJwtToken(jwtUtils.parseJwt(httpServletRequest));
    }
}
