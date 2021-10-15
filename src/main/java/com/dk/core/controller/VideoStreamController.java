package com.dk.core.controller;

import com.dk.core.domain.MovieTimeWatched;
import com.dk.core.domain.MovieTimeWatchedDTO;
import com.dk.core.exception.NoDefinedTimeWatchedException;
import com.dk.core.mapper.MovieTimeWatchedMapper;
import com.dk.core.service.MovieTimeWatchedService;
import com.dk.core.service.VideoStreamService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
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
    public ResponseEntity<MovieTimeWatchedDTO>  saveFileTimeWatched(@Valid @RequestBody MovieTimeWatchedDTO movieTimeWatchedDTO){
        if(movieTimeWatchedDTO.getTimeWatched() == null){
            throw new NoDefinedTimeWatchedException();
        }
        MovieTimeWatched movieTimeWatchedResponse = movieTimeWatchedService.setTimeWatched(
                movieTimeWatchedMapper.mapToMovieTimeWatched(movieTimeWatchedDTO)).get();
        return ResponseEntity.ok(movieTimeWatchedMapper.mapToMovieTimeWatchedDTO(movieTimeWatchedResponse));

    }

    @GetMapping("/getWatchedTimeMovie")
    public Long getFileTimeWatched(@RequestParam String filePath, @RequestParam Long profileId){
           @Valid MovieTimeWatchedDTO movieTimeWatchedDTO = new MovieTimeWatchedDTO(filePath,null,profileId);
           return movieTimeWatchedService.getTimeWatched(movieTimeWatchedMapper.mapToMovieTimeWatched(movieTimeWatchedDTO));
    }



}
