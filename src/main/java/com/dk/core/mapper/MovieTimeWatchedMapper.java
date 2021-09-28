package com.dk.core.mapper;


import com.dk.core.domain.MovieTimeWatched;
import com.dk.core.domain.MovieTimeWatchedDTO;
import com.dk.core.repository.MovieTimeWatchedRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class MovieTimeWatchedMapper {

    private MovieTimeWatchedRepository movieTimeWatchedRepository;

    public MovieTimeWatchedMapper(MovieTimeWatchedRepository movieTimeWatchedRepository){
        this.movieTimeWatchedRepository = movieTimeWatchedRepository;
    }

    public MovieTimeWatched mapDtoToMovieTimeWatched(MovieTimeWatchedDTO movieTimeWatchedDTO){
        Optional<MovieTimeWatched> movieTimeWatchedOptional = movieTimeWatchedRepository.findByPathFile(movieTimeWatchedDTO.getPathFile());
        Long IdForMovie = null;
        if(movieTimeWatchedOptional.isPresent()){
           IdForMovie = movieTimeWatchedOptional.get().getId();
        }
        return new MovieTimeWatched(IdForMovie, movieTimeWatchedDTO.getPathFile(), movieTimeWatchedDTO.getTimeWatched());
    }

    public MovieTimeWatchedDTO mapMovieTimeWatchedToDTO(MovieTimeWatched movieTimeWatched){
        return new MovieTimeWatchedDTO(movieTimeWatched.getPathFile(), movieTimeWatched.getTimeWatched());
    }

}
