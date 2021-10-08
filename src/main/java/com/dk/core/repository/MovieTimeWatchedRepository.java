package com.dk.core.repository;

import com.dk.core.domain.MovieTimeWatched;
import com.dk.core.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface MovieTimeWatchedRepository extends JpaRepository<MovieTimeWatched, Long > {

    Optional<MovieTimeWatched> findByFilePathAndProfile(String filePath, Profile profile);
    @Override
    MovieTimeWatched save(MovieTimeWatched movieTimeWatched);
}
