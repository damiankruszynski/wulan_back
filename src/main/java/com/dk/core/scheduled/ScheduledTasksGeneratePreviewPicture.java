package com.dk.core.scheduled;

import com.dk.core.constants.FileType;
import com.dk.core.mapper.FileMapper;
import com.dk.core.payload.FileResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ScheduledTasksGeneratePreviewPicture {

    @Value("${publicFolder}")
    private String pathToPublicFolder;
    private final FileMapper fileMapper;

    @Autowired
    public ScheduledTasksGeneratePreviewPicture(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    @Scheduled(fixedRate = 50000)
    public void generatePrewiew() {

        File theDir = new File(pathToPublicFolder+"PREWIEW");
        if (!theDir.exists()){
            theDir.mkdirs();
        }

        List<String> listFileByPath= fileMapper.getListFileByPathDeep(pathToPublicFolder);
        List<FileResponse> allFileList= listFileByPath.stream()
                .map( s -> new FileResponse(fileMapper.getFileName(s), fileMapper.getTypeOfFileByName(s), s, null, null))
                .collect(Collectors.toList());

        List<String> listPreviewFileByPath = fileMapper.getListFileByPathDeep(pathToPublicFolder+"PREWIEW");
        List<FileResponse> allFileListPreview = listPreviewFileByPath.stream()
                .map( s -> new FileResponse(fileMapper.getFileName(s), fileMapper.getTypeOfFileByName(s), s, null, null))
                .collect(Collectors.toList());


        allFileList.stream().filter(f -> f.getFileType().equals(FileType.PICTURE.toString()))
                .filter(allFile -> !hasPrewiew(allFile, allFileListPreview))
                .forEach( f -> {
                    try {
                        log.info("preparePreview for: "+ f.getFilePath());
                        preparePreview(f);
                    } catch (IOException e) {
                        log.info(e.getMessage());
                    }
                });
    }


    private boolean hasPrewiew(FileResponse orginalFile, List<FileResponse> prewiewList){
        return prewiewList.stream().anyMatch(prev -> prev.getFileName().equals(orginalFile.getFileName()));
    }

    private void preparePreview(FileResponse fr) throws IOException {
        Path path = Paths.get(fr.getFilePath());
        InputStream in = Files.newInputStream(path);
        ImageIO.setUseCache(false);
        BufferedImage img = ImageIO.read(in); // load image
        BufferedImage scaledImg = resizeImage(img, 200, 200);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(scaledImg, "png", baos);
        try(OutputStream outputStream = new FileOutputStream(pathToPublicFolder+"PREWIEW\\"+fr.getFileName())) {
            baos.writeTo(outputStream);
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }
}
