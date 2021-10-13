package com.dk.core.repository;

import com.dk.core.domain.Profile;
import com.dk.security.login.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findById(Long profileId);
    Optional<Profile> findByProfileNameAndUser(String profileName, User user);

    List<Profile> findAllByUser(User user);

    Optional<Profile> findByProfileName(String profileName);


    @Query(value = "SELECT PROFILE.* FROM PROFILE, USERS WHERE PROFILE.ID=:profileID AND USERS.ID = :userId",
            nativeQuery = true)
    Optional<Profile> findByIdAndUserId(@Param("profileID") Long profileID, @Param("userId") Long userId);

}
