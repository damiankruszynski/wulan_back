package com.dk.core.controller;

import com.dk.core.constants.FileType;
import com.dk.core.domain.MovieTimeWatched;
import com.dk.core.domain.MovieTimeWatchedDTO;
import com.dk.core.exception.NoDefinedTimeWatchedException;
import com.dk.core.mapper.FileMapper;
import com.dk.core.mapper.MovieTimeWatchedMapper;
import com.dk.core.payload.FileResponse;
import com.dk.core.service.MovieTimeWatchedService;
import com.dk.core.service.VideoStreamService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.dk.core.constants.ApplicationConstants.BYTE_RANGE;


@Slf4j
@RestController
@RequestMapping()
public class VideoStreamController {

    private final VideoStreamService videoStreamService;
    private final MovieTimeWatchedService movieTimeWatchedService;
    private final MovieTimeWatchedMapper movieTimeWatchedMapper;

    @Value("${publicFolder}")
    private String pathToPublicFolder;

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

    @GetMapping(value = "/getImage",
                produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType(@RequestParam("filePath") String filePath) throws IOException {
        Path path = Paths.get(filePath);
        InputStream in = Files.newInputStream(path);
        return IOUtils.toByteArray(in);
    }


    @GetMapping(value = "/getImagePreview",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageImagePreview(@RequestParam("filePath") String filePath) throws IOException {
        String fileName = FilenameUtils.getName(filePath);
        try {
            Path path = Paths.get(pathToPublicFolder + "PREWIEW\\" + fileName);
            InputStream in = Files.newInputStream(path);
            return IOUtils.toByteArray(in);
        }catch (Exception e){
            log.info(e.getMessage());
            return new byte[BYTE_RANGE];
        }
    }

    @GetMapping("/getWatchedTimeMovie")
    public Long getFileTimeWatched(@RequestParam String filePath, @RequestParam Long profileId){
           @Valid MovieTimeWatchedDTO movieTimeWatchedDTO = new MovieTimeWatchedDTO(filePath,null,profileId);
           return movieTimeWatchedService.getTimeWatched(movieTimeWatchedMapper.mapToMovieTimeWatched(movieTimeWatchedDTO));
    }



}
