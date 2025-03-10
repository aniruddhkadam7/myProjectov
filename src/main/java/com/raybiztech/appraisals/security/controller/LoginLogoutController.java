/**
 * 
 */
package com.raybiztech.appraisals.security.controller;

//import org.apache.log4j.Logger;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.raybiztech.appraisals.business.PersistLogin;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.security.service.EmployeeSecurityService;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Second;
import com.raybiztech.hrm.security.rest.TokenUtils;
import com.raybiztech.hrm.security.rest.dao.UserDao;

@Controller
@RequestMapping("/auth")
public class LoginLogoutController {

	Logger logger = Logger.getLogger(LoginLogoutController.class);

	@Autowired
	EmployeeSecurityService employeeSecurityService;

	@Autowired
	UserDao userDaoImpl;
	@Autowired
	SecurityUtils securityUtils;
	
	@Autowired
	UserDetailsService userDetailsService;

	/**
	 * Handles and retrieves the login JSP page
	 * 
	 * @param principal
	 * @return the name of the JSP page
	 */
	/*
	 * @RequestMapping(value = "/login", method = RequestMethod.GET) public
	 * String getLoginPage(
	 * 
	 * @RequestParam(value = "error", required = false) boolean error, ModelMap
	 * model) {
	 * 
	 * if (error == true) { // Assign an error message model.put("error",
	 * "You have entered an invalid username or password!"); } else {
	 * model.put("error", ""); }
	 * 
	 * // This will resolve to /WEB-INF/jsp/loginpage.jsp return "loginpage"; }
	 */

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> getLoginPage(Principal principal, HttpServletRequest request) {

		EmployeeDTO employeeDTO = employeeSecurityService
				.getLoginEmployee(principal.getName());
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal
				.getName());
		String createToken = TokenUtils.createToken(userDetails);
		List<PersistLogin> userTokenList = userDaoImpl
				.getTokensByUserName(principal.getName());
		if (userTokenList.size() < 1) {
			userDaoImpl.save(new PersistLogin(principal.getName(), createToken,
					new Second(), new Second()));
		} else {
			PersistLogin pl = userTokenList.get(0);
			pl.setToken(createToken);
			pl.setLogin_Time(new Second());
			pl.setLast_Used(new Second());
			userDaoImpl.update(pl);
		}
		employeeDTO.setToken(createToken);
		//to print IP address of Logged Employee
		securityUtils.printIpAddress();
		
		Map<String, Object> map = new HashMap<>();
		map.put("employeeDto", employeeDTO);
		map.put("tenantKey", request.getHeader("tenantkey"));
		return map;

	}

	/**
	 * Handles and retrieves the denied JSP page. This is shown whenever a
	 * regular user tries to access an admin only page.
	 * 
	 * @return the name of the JSP page
	 */
	@RequestMapping(value = "/denied", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	public @ResponseBody String getDeniedPage() {

		// This will resolve to /WEB-INF/jsp/deniedpage.jsp
		return "deniedpage";
	}
}