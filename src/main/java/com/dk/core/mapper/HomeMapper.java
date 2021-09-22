package com.dk.core.mapper;




import com.dk.core.payload.FileType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;
import java.util.stream.Collectors;


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

    public String getSubPathForFile(String fileName, String path){
        File f = new File(path);
        if(f == null){ return "";}
        log.error("SUBPATH: "+path);
        String fileNameWithOutExtension  = FilenameUtils.removeExtension(fileName);
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept (File dir, String name) {
                return name.startsWith(fileNameWithOutExtension) != name.endsWith("mp4");
            }
        };
        File[] matchingFiles = f.listFiles(filter);
        if(matchingFiles != null){
            Optional<File> firstFile = Arrays.stream(matchingFiles).findFirst();
            try{
                return firstFile.get().getAbsolutePath();
            }catch (NoSuchElementException noSuchElementException){
                return "";
            }
        }else{
            return "";
        }

    }
}
