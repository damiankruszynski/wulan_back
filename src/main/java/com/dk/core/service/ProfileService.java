package com.dk.core.service;


import com.dk.core.domain.Profile;
import com.dk.core.exception.NoProfileException;
import com.dk.core.exception.NoUserException;
import com.dk.core.repository.ProfileRepository;
import com.dk.security.login.domain.User;
import com.dk.security.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    private ProfileRepository profileRepository;
    private UserRepository userRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository){
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    public Optional<Profile> getProfileById(Long profileId){
        return profileRepository.findById(profileId);
    }

    public Profile save(Profile profile){return profileRepository.save(profile);}

    public Optional<Profile> getProfileByProfileNameAndUser(User user, String profileName){
        return profileRepository.findByProfileNameAndUser(profileName, user);
    }

    public Optional<Profile> getProfileByProfileIdAndUserId(Long userId, Long profileID){
        return profileRepository.findByIdAndUserId(profileID, userId);
    }

    @Transactional
    public void deleteProfile(Long profileId){
        Optional<Profile> profile = profileRepository.findById(profileId);
        if(profile.isPresent()) {
            profileRepository.deleteById(profile.get().getId());
        }else{
            throw new NoProfileException();
        }
    }

    public List<Profile> getProfilesByUserName(String userName){
        Optional<User> user = userRepository.findByUsername(userName);
        if(user.isPresent()){
            return profileRepository.findAllByUser(user.get());
        }else{
            throw new NoUserException();
        }
    }


}
