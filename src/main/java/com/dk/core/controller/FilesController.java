package com.dk.core.controller;

import com.dk.core.mapper.FileMapper;
import com.dk.core.payload.FileResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequestMapping
public class FilesController {

    @Autowired
    public FilesController(FileMapper fileMapper){
        this.fileMapper = fileMapper;
    }

    private final FileMapper fileMapper;

    @Value("${publicFolder}")
    private String pathToPublicFolder;


    @GetMapping("/files")
    public List<FileResponse> getFilesNamesAndTypes(@RequestParam String path)  {
        List<String> listFileByPath;
        if(path.equals("")){
           listFileByPath= fileMapper.getListFileByPath(pathToPublicFolder);
        }else{
           listFileByPath= fileMapper.getListFileByPath(path);
        }
        return  listFileByPath.stream()
                .map( s -> new FileResponse(fileMapper.getFileName(s), fileMapper.getTypeOfFileByName(s), s, fileMapper.getSubPathForFile(fileMapper.getFileName(s),path) ))
                .collect(Collectors.toList());
    }



}
