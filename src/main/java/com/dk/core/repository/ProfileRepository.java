package com.dk.core.repository;

import com.dk.core.domain.Profile;
import com.dk.security.login.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findById(Long profileId);
    Optional<Profile> findByProfileNameAndUser(String profileName, User user);

    List<Profile> findAllByUser(User user);

    Optional<Profile> findByProfileName(String profileName);
}
