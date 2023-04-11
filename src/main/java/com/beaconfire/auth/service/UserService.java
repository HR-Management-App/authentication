package com.beaconfire.auth.service;

import com.beaconfire.auth.dao.RoleDao;
import com.beaconfire.auth.dao.UserDao;
import com.beaconfire.auth.dao.User_RoleDao;
import com.beaconfire.auth.domain.entity.Role;
import com.beaconfire.auth.domain.entity.User;
import com.beaconfire.auth.domain.entity.User_Role;
import com.beaconfire.auth.domain.exception.RoleNotFoundByNameException;
import com.beaconfire.auth.domain.request.HousingRequest;
import com.beaconfire.auth.security.AuthUserDetail;
import com.beaconfire.auth.util.SerializeUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private UserDao userDao;

    private RoleDao roleDao;

    private User_RoleDao user_roleDao;

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setRoleDao(RoleDao roleDao) {this.roleDao = roleDao;}

    @Autowired
    public void setUser_roleDao(User_RoleDao user_roleDao) {
        this.user_roleDao = user_roleDao;
    }

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userOptional = userDao.getUserByUsername(username);

        if (!userOptional.isPresent()){
            //System.out.println("Username does not exist");
            throw new UsernameNotFoundException("Username does not exist");
        }
        //System.out.println("Username exist");

        User user = userOptional.get(); // database user

        return AuthUserDetail.builder() // spring security's userDetail
                .user_id(user.getUserID())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(new BCryptPasswordEncoder().encode(user.getPassword()))
                .authorities(getAuthoritiesForUser(user))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();
    }

    @Transactional
    public User getUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userDao.getUserByUsername(username);
        if (!userOptional.isPresent()){
            //System.out.println("Username does not exist");
            throw new UsernameNotFoundException("Hr's username does not exist");
        }

        return userOptional.get();
    }

    @Transactional
    public List<GrantedAuthority> getAuthoritiesForUser(User user){
        List<GrantedAuthority> userAuthorities = new ArrayList<>();

        for (User_Role ur :  user.getUser_roles()){
            userAuthorities.add(new SimpleGrantedAuthority(ur.getRole().getRoleName()));
        }

        return userAuthorities;
    }

    @Transactional
    public void registration(User user) throws RoleNotFoundByNameException {


        User newUser = userDao.findById(userDao.addUser(user));

        Optional<Role> selectedRole = roleDao.getRoleByName("employee");
        if (!selectedRole.isPresent()) {
            throw new RoleNotFoundByNameException("role didn't found by name");
        }

        System.out.println("slected role is: " + selectedRole);

        User_Role newUserRole = User_Role.builder()
                .user(newUser)
                .role(selectedRole.get())
                .build();

//        newUser.getUser_roles().add(newUserRole);
//        userDao.updateUser(newUser);

        user_roleDao.addUser_Role(newUserRole);


        /*  send out message to Housing Service */

        HousingRequest housingRequest = HousingRequest.builder()
                .type("New User Assign Housing")
                .user(newUser)
                .build();

        String jsonMessage = SerializeUtil.serializeHousingRequest(housingRequest);

        rabbitTemplate.convertAndSend("authenticationExchange", "housing", jsonMessage);
    }

    @Transactional
    public boolean validateNewUser(String newUsername, String newEmail) {
        if (userDao.getUserByUsername(newUsername).isPresent() || userDao.getUserByEmail(newEmail).isPresent()) {
            return false;
        }
        return true;
    }
}

