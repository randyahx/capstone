package com.randyahx.authorizationserver.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;

@Entity @AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class User {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="user_role",
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name="role_id")}
    )
    private Set<Role> roles;
}