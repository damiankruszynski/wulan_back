package com.dk.core.controller;

import com.dk.core.service.VideoStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Mono;

import java.io.FileNotFoundException;
import java.io.IOException;


@RestController
@RequestMapping()
public class VideoStreamController {

    private final VideoStreamService videoStreamService;

    @Autowired
    public VideoStreamController(VideoStreamService videoStreamService) {
        this.videoStreamService = videoStreamService;
    }

    @GetMapping("/stream")
    public Mono<ResponseEntity<byte[]>> streamVideo(@RequestHeader(value = "Range", required = false) String httpRangeList,
                                                    @RequestParam("filePath") String filePath) {
        return Mono.just(videoStreamService.prepareContent(filePath, httpRangeList));
    }

    @GetMapping("/subs")
    public ResponseEntity<StreamingResponseBody>  streamSubs(@RequestParam("filePath") String filePath) throws FileNotFoundException {
        return videoStreamService.prepareSubs(filePath);
    }

}
