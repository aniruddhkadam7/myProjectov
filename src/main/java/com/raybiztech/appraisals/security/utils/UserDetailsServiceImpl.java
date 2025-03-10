package com.raybiztech.appraisals.security.utils;

/**
 * Created on 08-Aug-2013
 */
import java.util.ArrayList;
import java.util.Collection;

//import com.intelesant.business.Role;
//import com.intelesant.dao.UserDAO;
//import com.intelesant.utils.IntegerConstants;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.hrm.security.rest.dao.UserDao;

/**
 * @author anil
 *
 */
@Service("userDetailsService")
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDAO;

    private HttpServletRequest httpServletRequest;

    private static final Logger LOGGER = Logger.getLogger(UserDetailsServiceImpl.class);

    /**
     *
     * @return
     */
    public UserDao getUserDAO() {
        return userDAO;
    }

    /**
     *
     * @param userDAO
     */
    public void setUserDAO(UserDao userDAO) {
        this.userDAO = userDAO;
    }

    /**
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     * @throws DataAccessException
     */
    @Override
    public UserDetails loadUserByUsername(String username) {

       
        Employee user = userDAO
                .findByActiveEmployeeName(Employee.class, username);
        LOGGER.warn("username:"+user.getUserName());
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        // for (Role role : user.getRole()) {
        LOGGER.warn("roles are" + user.getRole());
        authorities.add(new GrantedAuthorityImpl(user.getRole()));

        UserDetails userDetails = new CustomUserDetails(user.getUserName(),
                user.getPassword(),authorities);
        

        return userDetails;
    }

}
