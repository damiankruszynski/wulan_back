package com.dk.core.domain;


import com.dk.security.login.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="PROFILE")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@SequenceGenerator(name="profile_seq", initialValue=98752)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profile_seq")
    private Long id;

    @Column(name="PROFILE_NAME")
    private String profileName;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(targetEntity = MovieTimeWatched.class,
            mappedBy = "profile",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<MovieTimeWatched> movieTimeWatchedList;

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", profileName='" + profileName + '\'' +
                '}';
    }
}
