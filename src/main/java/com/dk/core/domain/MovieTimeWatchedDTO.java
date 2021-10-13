package com.dk.core.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Component
public class MovieTimeWatchedDTO {
    @NotBlank(message = "filePath can't be blank")
    private String filePath;
    private Long timeWatched;
    @NotNull(message = "profileId can't be empty")
    private Long profileId;
    @JsonProperty
    private boolean isWatched;
    @JsonProperty
    private Long  movieTimeInSeconds;


    public MovieTimeWatchedDTO(String filePath, Long timeWatched, Long profileId) {
        this.filePath = filePath;
        this.timeWatched = timeWatched;
        this.profileId = profileId;
        this.isWatched = false;
    }


    static public MovieTimeWatchedDTO empty(){
        return new MovieTimeWatchedDTO();
    }

    @Override
    public String toString() {
        return "MovieTimeWatchedDTO{" +
                "filePath='" + filePath + '\'' +
                ", timeWatched=" + timeWatched +
                ", profileId=" + profileId +
                ", isWatched=" + isWatched +
                ", movieTimeInSeconds=" + movieTimeInSeconds +
                '}';
    }
}
