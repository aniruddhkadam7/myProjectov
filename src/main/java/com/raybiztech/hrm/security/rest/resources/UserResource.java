/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.hrm.security.rest.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.PersistLogin;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.date.Second;
import com.raybiztech.hrm.security.rest.TokenUtils;
import com.raybiztech.hrm.security.rest.dao.UserDao;
import com.raybiztech.hrm.security.rest.transfer.TokenTransfer;
import com.raybiztech.recruitment.service.JobPortalService;

/**
 *
 * @author hari
 */
@Controller
@RequestMapping("/user")
public class UserResource {

    @Autowired

    UserDao userDaoImpl;
    @Autowired
    JobPortalService jobPortalServiceImpl;

    /**
     * Retrieves the currently logged in user.
     *
     * @return A transfer containing the username and the roles.
     */
    Logger logger = Logger.getLogger(UserResource.class);

//    @RequestMapping(value = "/getLoginUser", method = RequestMethod.GET)
//    public @ResponseBody
//    UserTransfer getUser() {
//       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Object principal = authentication.getPrincipal();
//        if (principal instanceof String && ((String) principal).equals("anonymousUser")) {
//            throw new WebApplicationException(401);
//        }
//        UserDetails userDetails = (UserDetails) principal;
//
//        return new UserTransfer(userDetails.getUsername(), this.createRoleMap(userDetails));
//    }
    @RequestMapping(value = "/getLoginUser", method = RequestMethod.GET)
    public @ResponseBody
    EmployeeDTO getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof String && ((String) principal).equals("anonymousUser")) {
            throw new WebApplicationException(401);
        }
        UserDetails userDetails = (UserDetails) principal;
        return jobPortalServiceImpl.getLoggedInEmployee(userDetails.getUsername());
    }

    /**
     * Authenticates a user and creates an authentication token.
     *
     * @param username
     * @return A transfer containing the authentication token.
     */
    /*@RequestMapping(value = "/action", method = RequestMethod.POST)
    public @ResponseBody
    TokenTransfer authenticate(@RequestParam String username) {
        UserDetails userDetails = userDaoImpl.loadUserByUsername(username);
        String createToken = TokenUtils.createToken(userDetails);
        userDaoImpl.save(new PersistLogin(username, createToken, new Second(), new Second()));
        return new TokenTransfer(createToken);
    }*/

    @RequestMapping(value = "/logoutUser", params = {"authToken"}, method = RequestMethod.GET)
    public @ResponseBody
    void logoutUser(@RequestParam String authToken) {
        userDaoImpl.delete(userDaoImpl.findByTokenName(PersistLogin.class, authToken));
    }

    private Map<String, Boolean> createRoleMap(UserDetails userDetails) {
        Map<String, Boolean> roles = new HashMap<String, Boolean>();
        Employee employee = userDaoImpl.findByEmployeeName(Employee.class, userDetails.getUsername());
        List<GrantedAuthority> authorities = getAuthorities(employee.getRole());

        for (GrantedAuthority authority : authorities) {
            roles.put(authority.getAuthority(), Boolean.TRUE);
        }

        return roles;
    }

    private List getAuthorities(String role) {
        List<GrantedAuthority> authList = new ArrayList();
        authList.add(new GrantedAuthorityImpl("ROLE_USER"));
        if (role != null && role.trim().length() > 0) {
            if (role.equals("admin")) {
                authList.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
            }
        }
        if (role != null && role.trim().length() > 0) {
            if (role.equals("Manager")) {
                authList.add(new GrantedAuthorityImpl("ROLE_MANAGER"));
            }
        }
        return authList;
    }
}
