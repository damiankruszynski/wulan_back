package com.dk.core.controller;

import com.dk.core.mapper.HomeMapper;
import com.dk.core.payload.FilesJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequestMapping
public class HomeController {

    @Autowired
    public HomeController(HomeMapper homeMapper){
        this.homeMapper = homeMapper;
    }

    private HomeMapper homeMapper;

    @Value("${publicFolder}")
    private String pathToPublicFolder;


    @GetMapping("/files")
    public List<FilesJson> getFilesNamesAndTypes(@RequestParam String path) throws IOException {
        List<String> listFileByPath;
        if(path.equals("")){
           listFileByPath= homeMapper.getListFileByPath(pathToPublicFolder);
        }else{
           listFileByPath= homeMapper.getListFileByPath(path);
        }
        return  listFileByPath.stream()
                .map( s -> new FilesJson(homeMapper.getFileName(s), homeMapper.getTypeOfFileByName(s), s, homeMapper.getSubPathForFile(homeMapper.getFileName(s),s) ))
                .collect(Collectors.toList());
    }



}
