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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileResponse)) return false;

        FileResponse that = (FileResponse) o;

        return filePath.equals(that.filePath);
    }

    @Override
    public int hashCode() {
        return filePath.hashCode();
    }
}
