package com.beaconfire.auth.domain.entity;

import lombok.*;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name="user_role")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User_Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer userRole_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    @ToString.Exclude
    private Role role;

    @Column(name = "active_flag", nullable = false)
    private boolean ActiveFlag;

    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Column(name = "last_modification_date", nullable = false)
    private Date lastModificationDate;

}
