package com.beaconfire.auth.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="role_table")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleID;

    @Column(name = "role_name", nullable = false, insertable=false)
    private String roleName;

    @Column(name = "role_description", nullable = false, insertable=false)
    private String roleDescription;

    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Column(name = "last_modification_date", nullable = false)
    private Date lastModificationDate;


}
