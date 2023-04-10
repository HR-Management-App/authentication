package com.beaconfire.auth.dao;


import com.beaconfire.auth.domain.entity.User_Role;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class User_RoleDao extends AbstractHibernateDao<User_Role> {

    public User_RoleDao() {
        setClazz(User_Role.class);
    }

    public User_Role getUser_RoleById(int user_role_id) {return this.findById(user_role_id);}

    public int addUser_Role(User_Role user_role) {
        user_role.setCreateDate(new Date(System.currentTimeMillis()));
        user_role.setLastModificationDate(new Date(System.currentTimeMillis()));
        return this.add(user_role);
    }
}
