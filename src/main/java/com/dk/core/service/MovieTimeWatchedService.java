package com.dk.core.service;

import com.dk.core.domain.MovieTimeWatched;
import com.dk.core.repository.MovieTimeWatchedRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Log4j2
public class MovieTimeWatchedService {

    private final MovieTimeWatchedRepository movieTimeWatchedRepository;

    @Autowired
    public MovieTimeWatchedService(MovieTimeWatchedRepository movieTimeWatchedRepository){
        this.movieTimeWatchedRepository = movieTimeWatchedRepository;
    }

    public Optional<MovieTimeWatched> setTimeWatched(MovieTimeWatched movieTimeWatched){
        try{
             log.error("setTimeWatched = "+ movieTimeWatched.toString());
            return Optional.of(movieTimeWatchedRepository.save(movieTimeWatched));
        }catch (Exception e){
            log.error(e.getMessage());
            return Optional.ofNullable(null);
        }
    }

    public Long getTimeWatched(String pathFile){
        try {
            return movieTimeWatchedRepository.findByPathFile(pathFile).get().getTimeWatched();
        }catch (NoSuchElementException noSuchElementException){
            log.info(noSuchElementException.getMessage());
            return 0L;
        }
    }
}
