package com.dk.core.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Component
public class MovieTimeWatchedDTO {
    private String pathFile;
    private Long timeWatched;

    @Override
    public String toString() {
        return "MovieTimeWatchedDTO{" +
                "pathFile='" + pathFile + '\'' +
                ", timeWatched=" + timeWatched +
                '}';
    }
}
