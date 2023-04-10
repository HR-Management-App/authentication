package com.beaconfire.auth.dao;

import com.beaconfire.auth.domain.entity.User_Role;
import com.beaconfire.auth.domain.entity.User;
import com.beaconfire.auth.domain.entity.Role;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDao extends AbstractHibernateDao<User> {

    public UserDao() {
        setClazz(User.class);
    }

    public Optional<User> getUserByUsername(String username) {
        Session session = getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> root = criteria.from(User.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("username"), username));
        List<User> resultList = session.createQuery(criteria).getResultList();
        return Optional.ofNullable(resultList.size() == 0 ? null : resultList.get(0));
    }

    public Optional<User> getUserByEmail(String email) {
        Session session = getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> root = criteria.from(User.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("email"), email));
        List<User> resultList = session.createQuery(criteria).getResultList();
        return Optional.ofNullable(resultList.size() == 0 ? null : resultList.get(0));
    }

    public Optional<User> getUserByUsernameAndPassword(String username, String password) {
        Session session = getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> root = criteria.from(User.class);
        criteria.select(root);
        Predicate predicateForUsername = builder.equal(root.get("username"), username);
        Predicate predicateForPassword = builder.equal(root.get("password"), password);
        Predicate predicate = builder.and(predicateForUsername, predicateForPassword);
        criteria.where(predicate);
        List<User> resultList = session.createQuery(criteria).getResultList();
        return Optional.ofNullable(resultList.size() == 0 ? null : resultList.get(0));
    }



    public List<User> getAllUsers() {
        return this.getAll();
    }

    public int addUser(User user) {
        user.setCreateDate(new Date(System.currentTimeMillis()));
        user.setLastModificationDate(new Date(System.currentTimeMillis()));
        return this.add(user);
    }

    public void updateUser(User user) {
        user.setLastModificationDate(new Date(System.currentTimeMillis()));
        this.update(user);
    }
}
