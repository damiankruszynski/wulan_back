package com.dk.core.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileResponse {
    private String fileName;
    private String fileType;
    private String filePath;
    private String subPath;
}
