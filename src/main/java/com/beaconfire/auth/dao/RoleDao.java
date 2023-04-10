package com.beaconfire.auth.dao;

import com.beaconfire.auth.domain.entity.Role;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class RoleDao extends AbstractHibernateDao<Role> {

    public RoleDao() {
        setClazz(Role.class);
    }

    public Role getRoleById(int role_id) {return this.findById(role_id);}

    public Optional<Role> getRoleByName(String roleName) {
        Session session = getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Role> criteria = builder.createQuery(Role.class);
        Root<Role> root = criteria.from(Role.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("roleName"), roleName));
        List<Role> resultList = session.createQuery(criteria).getResultList();
        return Optional.ofNullable(resultList.size() == 0 ? null : resultList.get(0));
    }

    public List<Role> getAllRoles() {return this.getAll();}

    public int addRole(Role role) {
        role.setCreateDate(new Date(System.currentTimeMillis()));
        role.setLastModificationDate(new Date(System.currentTimeMillis()));
        return this.add(role);
    }


}
