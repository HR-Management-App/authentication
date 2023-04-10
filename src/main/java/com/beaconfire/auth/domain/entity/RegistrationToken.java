package com.beaconfire.auth.domain.entity;


import com.beaconfire.auth.domain.entity.User;
import lombok.*;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "registration_token")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RegistrationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registrationToken_id")
    private int registrationTokenID;

    @Column(name = "token", unique = true,  nullable = false)
    private String token;

    @Column(name = "email",  nullable = false)
    private String email;

    @Column(name = "expiration_date",  nullable = false)
    private Date expirationDate;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "createBy",  nullable = false)
    private User createBy;
}
