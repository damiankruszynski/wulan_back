package com.dk.core.mapper;


import com.dk.core.domain.MovieTimeWatched;
import com.dk.core.domain.MovieTimeWatchedDTO;
import com.dk.core.domain.Profile;
import com.dk.core.exception.NoProfileException;
import com.dk.core.service.MovieTimeWatchedService;
import com.dk.core.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class MovieTimeWatchedMapper {

    private MovieTimeWatchedService movieTimeWatchedService;
    private ProfileService profileService;


    public MovieTimeWatchedMapper(MovieTimeWatchedService movieTimeWatchedService, ProfileService profileService){
        this.movieTimeWatchedService = movieTimeWatchedService;
        this.profileService = profileService;
    }

    public MovieTimeWatched mapToMovieTimeWatched(MovieTimeWatchedDTO movieTimeWatchedDTO){
        Optional<Profile> profile = profileService.getProfileById(movieTimeWatchedDTO.getProfileId());
        if(profile.isPresent()){
            Optional<MovieTimeWatched> movieTimeWatchedOptional = movieTimeWatchedService
                    .findByPathAndProfile(movieTimeWatchedDTO.getFilePath(), profile.get());
            Long IdForMovie = null;
            if(movieTimeWatchedOptional.isPresent()){
                IdForMovie = movieTimeWatchedOptional.get().getId();
            }
            return new MovieTimeWatched(IdForMovie, movieTimeWatchedDTO.getFilePath(),
                    movieTimeWatchedDTO.getTimeWatched(), profile.get());
        }else{
            throw new NoProfileException();
        }
    }

    public MovieTimeWatchedDTO mapToMovieTimeWatchedDTO(MovieTimeWatched movieTimeWatched){
        return new MovieTimeWatchedDTO(movieTimeWatched.getFilePath(),
                movieTimeWatched.getTimeWatched(),
                movieTimeWatched.getProfile().getId()
               );
    }

}
