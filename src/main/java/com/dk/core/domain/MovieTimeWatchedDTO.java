package com.dk.core.domain;


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

    @Override
    public String toString() {
        return "MovieTimeWatchedDTO{" +
                "filePath='" + filePath + '\'' +
                ", timeWatched=" + timeWatched +
                '}';
    }
}
