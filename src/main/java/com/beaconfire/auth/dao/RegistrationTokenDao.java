package com.beaconfire.auth.dao;

import com.beaconfire.auth.domain.entity.RegistrationToken;
import com.beaconfire.auth.domain.entity.Role;
import com.beaconfire.auth.domain.entity.User;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class RegistrationTokenDao extends AbstractHibernateDao<RegistrationToken>{

    public RegistrationTokenDao() {
        setClazz(RegistrationToken.class);
    }

    public int addToken(RegistrationToken token) {
        return this.add(token);
    }

    public List<RegistrationToken> getAllToken() {return this.getAll();}

    public Optional<RegistrationToken> findByTokenString(String tokenString) {
        Session session = getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<RegistrationToken> criteria = builder.createQuery(RegistrationToken.class);
        Root<RegistrationToken> root = criteria.from(RegistrationToken.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("token"), tokenString));
        List<RegistrationToken> resultList = session.createQuery(criteria).getResultList();
        return Optional.ofNullable(resultList.size() == 0 ? null : resultList.get(0));
    }


}
