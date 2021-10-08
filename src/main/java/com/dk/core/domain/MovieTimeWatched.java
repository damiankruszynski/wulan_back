package com.dk.core.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(	name = "MOVIES_TIME_WATCHED")
public class MovieTimeWatched {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID")
    private Long Id;


    @Column(name="PATH")
    private String filePath;

    @Column(name="TIME_WATCHED")
    private Long timeWatched;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROFILE_ID")
    private Profile profile;

    @Override
    public String toString() {
        return "MovieTimeWatched{" +
                "filePath='" + filePath + '\'' +
                ", timeWatched=" + timeWatched +
                '}';
    }

    public static MovieTimeWatched empty(){
        return  new MovieTimeWatched();
    }
}
