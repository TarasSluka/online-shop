package com.sluka.taras.security;

import com.sluka.taras.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Authenticate a user from the database.
 */
@Service("userDetailsService")
public class MyUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(MyUserDetailsService.class);

    UserService userService;

    @Autowired
    MyUserDetailsService(UserService userService) {
        super();
        this.userService = userService;
    }


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String userName) {
        log.debug("Authenticating {}", userName);
        com.sluka.taras.common.model.User user = userService.findByUserName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("User " + userName + " was not found in the database");
        } else if (!user.getEnabled()) {
            throw new com.sluka.taras.security.UserNotEnabledException("User " + userName + " was not enabled");
        }


        return new User(user.getUserName(), user.getPassword(),
                user.getEnabled(),
                true, true, true,
                getGrantedAuthorities(user.getRole().toString()));
    }

    private List<GrantedAuthority> getGrantedAuthorities(
            final String... roles) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
