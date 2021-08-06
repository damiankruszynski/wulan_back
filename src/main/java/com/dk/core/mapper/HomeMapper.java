package com.dk.core.mapper;




import com.dk.core.payload.FileType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
@Component
public class HomeMapper {

    public String getTypeOfFileByName(String filePath){
            return setFileType(filePath).name();
    }

    public FileType setFileType(String fileName){
        String extension = FilenameUtils.getExtension(fileName);
        extension = extension.toUpperCase();
        if(extension.isEmpty()){
            return FileType.FOLDER;
        }
        else if(extension.equals("MP4")){
            return FileType.MP4;
        }
        return FileType.FILE;
    }

    public List<String> getListFileByPath(String path)  {
        File f[] = new File(path).listFiles();
        if(f == null){ return new ArrayList<>();}
        return Arrays.stream(f).map( s -> s.getAbsolutePath()).collect(Collectors.toList());
    }

    public String getFileName(String filePath){
        return FilenameUtils.getName(filePath);
    }
}
