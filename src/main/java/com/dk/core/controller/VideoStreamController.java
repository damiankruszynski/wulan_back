package com.dk.core.controller;

import com.dk.core.domain.MovieTimeWatched;
import com.dk.core.domain.MovieTimeWatchedDTO;
import com.dk.core.mapper.MovieTimeWatchedMapper;
import com.dk.core.service.MovieTimeWatchedService;
import com.dk.core.service.VideoStreamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;


@Slf4j
@RestController
@RequestMapping()
public class VideoStreamController {

    private final VideoStreamService videoStreamService;
    private final MovieTimeWatchedService movieTimeWatchedService;
    private final MovieTimeWatchedMapper movieTimeWatchedMapper;

    @Autowired
    public VideoStreamController(VideoStreamService videoStreamService, MovieTimeWatchedService movieTimeWatchedService, MovieTimeWatchedMapper movieTimeWatchedMapper) {
        this.videoStreamService = videoStreamService;
        this.movieTimeWatchedService = movieTimeWatchedService;
        this.movieTimeWatchedMapper = movieTimeWatchedMapper;
    }

    @GetMapping("/stream")
    public Mono<ResponseEntity<byte[]>> streamVideo(@RequestHeader(value = "Range", required = false) String httpRangeList,
                                                    @RequestParam("filePath") String filePath) {
        return Mono.just(videoStreamService.prepareContent(filePath, httpRangeList));
    }

    @PutMapping("/saveWatchedTimeMovie")
    public ResponseEntity<MovieTimeWatchedDTO>  saveFileTimeWatched(@RequestBody MovieTimeWatchedDTO movieTimeWatchedDTO){
        log.error(movieTimeWatchedDTO.toString());
        if(movieTimeWatchedDTO.getPathFile() == null || movieTimeWatchedDTO.getTimeWatched() == null){
            return ResponseEntity.noContent().build();
        }
        try{
            MovieTimeWatched movieTimeWatchedResponse = movieTimeWatchedService.setTimeWatched(
                    movieTimeWatchedMapper.mapDtoToMovieTimeWatched(movieTimeWatchedDTO)).get();
            log.error(movieTimeWatchedResponse.toString());
            return ResponseEntity.ok(movieTimeWatchedMapper.mapMovieTimeWatchedToDTO(movieTimeWatchedResponse));
        }catch (NoSuchElementException noSuchElementException){
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/getWatchedTimeMovie")
    public Long getFileTimeWatched(@RequestParam("filePath") String filePath){
           return movieTimeWatchedService.getTimeWatched(filePath);
    }



}
