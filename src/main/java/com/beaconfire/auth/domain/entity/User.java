package com.beaconfire.auth.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import java.util.Date;

@Entity
@Table(name="user_table")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userID;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Column(name = "last_modification_date", nullable = false)
    private Date lastModificationDate;

    @Column(name = "active_flag", nullable = false)
    private boolean activeFlag;

    @OneToMany(targetEntity = User_Role.class, fetch = FetchType.LAZY, mappedBy = "user")
    @ToString.Exclude
    @JsonIgnore
    private Set<User_Role> user_roles = new HashSet<>();

}
