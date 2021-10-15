package com.dk.core.mapper;




import com.dk.core.constants.FileType;
import com.dk.core.domain.MovieTimeWatchedDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Component
public class FileMapper {

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
        else if(extension.equals("JPEG") | extension.equals("JPG")){
            return FileType.PICTURE;
        }
        return FileType.FILE;
    }


    public List<String> getListFileByPath(String path)  {
        File[] f = new File(path).listFiles();
        if(f == null){ return new ArrayList<>();}
        return Arrays.stream(f).map(File::getAbsolutePath).collect(Collectors.toList());
    }

    public String getFileName(String filePath){
        return FilenameUtils.getName(filePath);
    }

    public String getSubPathForFile(String fileName, String path){
        File f = new File(path);
        String fileNameWithOutExtension  = FilenameUtils.removeExtension(fileName);
        FilenameFilter filter = (dir, name) -> name.startsWith(fileNameWithOutExtension) != name.endsWith("mp4");
        File[] matchingFiles = f.listFiles(filter);
        if(matchingFiles != null){
            Optional<File> firstFile = Arrays.stream(matchingFiles).findFirst();
            try{
                return firstFile.map(File::getAbsolutePath).orElse("");
            }catch (NoSuchElementException noSuchElementException){
                return "";
            }
        }else{
            return "";
        }

    }
}
