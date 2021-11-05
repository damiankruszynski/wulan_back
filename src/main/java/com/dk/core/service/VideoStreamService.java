package com.dk.core.service;

import com.dk.core.constants.FileType;
import com.dk.core.mapper.FileMapper;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static com.dk.core.constants.ApplicationConstants.*;

@Slf4j
@Service
public class VideoStreamService {

    private final FileMapper fileMapper;

    @Autowired
    public VideoStreamService(FileMapper fileMapper){
        this.fileMapper = fileMapper;
    }

    private String getContentType(String filePath){
        FileType fileType = fileMapper.setFileType(filePath);
        if(fileType.equals(FileType.MP4)){
            return VIDEO_CONTENT + fileType;
        }
        else if(fileType.equals(FileType.PICTURE)){
            return MediaType.IMAGE_JPEG_VALUE;
        }else {
            return VIDEO_CONTENT + fileType;
        }
    }

    /**
     * Prepare the content.
     *
     * @param filePath String.
     * @param range    String.
     * @return ResponseEntity.
     */
    public ResponseEntity<byte[]> prepareContent(String filePath, String range) {
        long rangeStart = 0;
        long rangeEnd;
        byte[] data;
        Long fileSize;
        try {
            fileSize = getFileSize(filePath);
            if (range == null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .header(CONTENT_TYPE, getContentType(filePath))
                        .header(CONTENT_LENGTH, String.valueOf(fileSize))
                        .body(readByteRange(filePath, rangeStart, fileSize - 1)); // Read the object and convert it as bytes
            }
            String[] ranges = range.split("-");
            rangeStart = Long.parseLong(ranges[0].substring(6));
            if (ranges.length > 1) {
                rangeEnd = Long.parseLong(ranges[1]);
            } else {
                rangeEnd = fileSize - 1;
            }
            if (fileSize < rangeEnd) {
                rangeEnd = fileSize - 1;
            }
            data = readByteRange(filePath, rangeStart, rangeEnd);
        } catch (IOException e) {
            log.error("Exception while reading the file {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .header(CONTENT_TYPE, VIDEO_CONTENT + fileMapper.setFileType(filePath))
                .header(ACCEPT_RANGES, BYTES)
                .header(CONTENT_LENGTH, contentLength)
                .header(CONTENT_RANGE, BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
                .body(data);


    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }


    public byte[] preparePreview(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        InputStream in = Files.newInputStream(path);
        ImageIO.setUseCache(false);
        BufferedImage img = ImageIO.read(in); // load image
        BufferedImage scaledImg = resizeImage(img, 300, 300);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(scaledImg, "png", baos);
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    /**
     * ready file byte by byte.
     *
     * @param filePath String.
     * @param start    long.
     * @param end      long.
     * @return byte array.
     * @throws IOException exception.
     */
    public byte[] readByteRange(String filePath, long start, long end) throws IOException {
        Path path = Paths.get(filePath);
        try (InputStream inputStream = (Files.newInputStream(path));
             ByteArrayOutputStream bufferedOutputStream = new ByteArrayOutputStream()) {
            byte[] data = new byte[BYTE_RANGE];
            int nRead;
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                bufferedOutputStream.write(data, 0, nRead);
            }
            bufferedOutputStream.flush();
            byte[] result = new byte[(int) (end - start) + 1];
            System.arraycopy(bufferedOutputStream.toByteArray(), (int) start, result, 0, result.length);
            return result;
        }
    }
    /**
     * Content length.
     *
     * @param filePath String.
     * @return Long.
     */
    public Long getFileSize(String filePath) {
        return Optional.ofNullable(filePath)
                .map(file -> Paths.get(filePath))
                .map(this::sizeFromFile)
                .orElse(0L);
    }

    /**
     * Getting the size from the path.
     *
     * @param path Path.
     * @return Long.
     */
    private Long sizeFromFile(Path path) {
        try {
            return Files.size(path);
        } catch (IOException ioException) {
            log.error("Error while getting the file size", ioException);
        }
        return 0L;
    }
}