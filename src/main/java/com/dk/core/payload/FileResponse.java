package com.dk.core.payload;

import com.dk.core.domain.MovieTimeWatchedDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileResponse {
    private String fileName;
    private String fileType;
    private String filePath;
    private String subPath;
    private MovieTimeWatchedDTO movieTimeWatchedDTO;
}
