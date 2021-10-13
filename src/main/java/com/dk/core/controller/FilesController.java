package com.dk.core.controller;

import com.dk.core.constants.FileType;
import com.dk.core.domain.MovieTimeWatchedDTO;
import com.dk.core.domain.Profile;
import com.dk.core.mapper.FileMapper;
import com.dk.core.mapper.MovieTimeWatchedMapper;
import com.dk.core.payload.FileResponse;
import com.dk.core.service.MovieTimeWatchedService;
import com.dk.core.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequestMapping
public class FilesController {

    @Autowired
    public FilesController(FileMapper fileMapper, MovieTimeWatchedMapper movieTimeWatchedMapper, MovieTimeWatchedService movieTimeWatchedService,
                            ProfileService profileService){
        this.fileMapper = fileMapper;
        this.movieTimeWatchedMapper = movieTimeWatchedMapper;
        this.movieTimeWatchedService = movieTimeWatchedService;
        this.profileService = profileService;
    }

    private final FileMapper fileMapper;
    private final MovieTimeWatchedMapper movieTimeWatchedMapper;
    private final MovieTimeWatchedService movieTimeWatchedService;
    private final ProfileService profileService;

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
                .map( s -> new FileResponse(fileMapper.getFileName(s), fileMapper.getTypeOfFileByName(s), s,
                        fileMapper.getSubPathForFile(fileMapper.getFileName(s),path), null))
                .collect(Collectors.toList());
        ResponseList.stream().forEach(f -> {
            if(f.getFileType() == FileType.MP4.name()){
                try{
                MovieTimeWatchedDTO movieTimeWatchedDTO = movieTimeWatchedMapper.mapToMovieTimeWatchedDTO(
                        movieTimeWatchedService.findByPathAndProfile(f.getFilePath(),profile.get()).orElseGet((null)));
                f.setMovieTimeWatchedDTO(movieTimeWatchedDTO);
                }catch (NullPointerException nullPointerException){
                    log.info(nullPointerException.getMessage());
                }
            }});
       return ResponseList;
    }



}
