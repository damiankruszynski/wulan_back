package com.dk.security.login.domain;


import com.dk.core.domain.Profile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(	name = "USERS")
public class User {
    public User(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="ID")
    private Long id;

    @Column(unique = true, name="USER_NAME")
    private String username;

    @Column(name="PASSWORD")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "USER_ROLES",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private Set<Role> roles = new HashSet<>();

    @Column(name="DATE_ACCOUNT_CREATED")
    private LocalDate dateAccountCreated;

    @Column(name="DATE_LAST_LOGIN")
    private LocalDate dateLastLogin;

    @OneToMany(targetEntity = Profile.class,
            mappedBy = "user",
            fetch = FetchType.EAGER)
    private List<Profile> profileList;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles.toString() +
                ", dateAccountCreated=" + dateAccountCreated +
                ", dateLastLogin=" + dateLastLogin +
                ", profileList=" + profileList +
                '}';
    }
}