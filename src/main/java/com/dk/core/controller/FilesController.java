package com.dk.core.controller;

import com.dk.core.constants.FileType;
import com.dk.core.domain.MovieTimeWatchedDTO;
import com.dk.core.domain.Profile;
import com.dk.core.mapper.FileMapper;
import com.dk.core.mapper.MovieTimeWatchedMapper;
import com.dk.core.payload.FileResponse;
import com.dk.core.service.MovieTimeWatchedService;
import com.dk.core.service.ProfileService;
import com.dk.core.service.VideoStreamService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequestMapping
public class FilesController {

    @Autowired
    public FilesController(FileMapper fileMapper, MovieTimeWatchedMapper movieTimeWatchedMapper, MovieTimeWatchedService movieTimeWatchedService,
                            ProfileService profileService, VideoStreamService videoStreamService){
        this.fileMapper = fileMapper;
        this.movieTimeWatchedMapper = movieTimeWatchedMapper;
        this.movieTimeWatchedService = movieTimeWatchedService;
        this.profileService = profileService;
        this.videoStreamService = videoStreamService;
    }

    private final FileMapper fileMapper;
    private final MovieTimeWatchedMapper movieTimeWatchedMapper;
    private final MovieTimeWatchedService movieTimeWatchedService;
    private final ProfileService profileService;
    private final VideoStreamService videoStreamService;

    @Value("${publicFolder}")
    private String pathToPublicFolder;


    @GetMapping("/files")
    public List<FileResponse> getFilesNamesAndTypes(@RequestParam String path, @RequestParam Long profileId)  {
        List<String> listFileByPath;
        if(path.equals("")){
           listFileByPath= fileMapper.getListFileByPath(pathToPublicFolder);
        }else{
           listFileByPath= fileMapper.getListFileByPath(path);
        }
        Optional<Profile> profile = profileService.getProfileById(profileId);
        List<FileResponse> ResponseList = listFileByPath.stream()
                .filter(s -> !fileMapper.getFileName(s).toUpperCase(Locale.ROOT).equals("PREWIEW"))
                .map( s -> new FileResponse(fileMapper.getFileName(s), fileMapper.getTypeOfFileByName(s), s,
                        fileMapper.getSubPathForFile(fileMapper.getFileName(s),path), null))
                .collect(Collectors.toList());
        try {
            ResponseList.stream().forEach(f -> {
                if (f.getFileType() == FileType.MP4.name()) {
                    MovieTimeWatchedDTO movieTimeWatchedDTO = movieTimeWatchedMapper.mapToMovieTimeWatchedDTO(
                            movieTimeWatchedService.findByPathAndProfile(f.getFilePath(), profile.get()).orElse((null)));
                    f.setMovieTimeWatchedDTO(movieTimeWatchedDTO);
                }
            });
        }
        catch (Exception e){
            log.info(e.getMessage());
        }
       return ResponseList;
    }



}
