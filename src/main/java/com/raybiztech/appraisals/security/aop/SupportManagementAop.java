/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.security.aop;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.supportmanagement.business.SupportTickets;
import com.raybiztech.supportmanagement.dao.SupportManagementDAO;
import com.raybiztech.supportmanagement.dto.SupportTicketsDTO;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author anil
 */
@Aspect
@Component
public class SupportManagementAop {

    Logger logger = Logger.getLogger(SupportManagementAop.class);
    @Autowired
    DAO dao;
    @Autowired
    SecurityUtils securityUtils;
    @Autowired
    SupportManagementDAO dAOImpl;

    @Before("execution(* com.raybiztech.supportmanagement.controller.*.*(..))")
    public void supportManagementController(JoinPoint joinPoint) throws IOException {
        String methodName = joinPoint.getSignature().getName();
        Map<String, Object> employeeDeatils = securityUtils.getLoggedEmployeeDetailsSecurityContextHolder();
        Employee employee = (Employee) employeeDeatils.get("employee");
        Map<String, Object> methodParams = securityUtils.getMethodParams(joinPoint.getArgs());
        HttpServletResponse httpServletResponse = (HttpServletResponse) methodParams
                .get("httpServletResponse");
        SupportTicketsDTO ticketsDTO = (SupportTicketsDTO) methodParams.get("supportTicketsDTO");
        Permission updateTickets = dao.checkForPermission("Ticket Approvals",
                employee);

        if (methodName.equalsIgnoreCase("createTickets")) {
            List<Long> employees = ticketsDTO.getWatcherIds();
            Employee ticketEmpId = null;
            for (Long e : employees) {
                ticketEmpId = dao.findBy(Employee.class, e);
            }
            if (!updateTickets.getView() && ticketEmpId!=null) {
                securityUtils.checkAccessForRespectiveEmployee(employee.getEmployeeId(), ticketEmpId.getEmployeeId(), httpServletResponse);
            }
        }
       else if (methodName.equalsIgnoreCase("editIndividualTickets")) {
            SupportTickets tickets = dao.findBy(SupportTickets.class, ticketsDTO.getId());
            if (!updateTickets.getView()) {
                securityUtils.checkAccessForRespectiveEmployee(employee.getEmployeeId(), tickets.getCreatedBy(), httpServletResponse);
            }
        }
        else if (methodName.equalsIgnoreCase("cancelTicketRequest") || methodName.equalsIgnoreCase("getAudit")) {
            Long paramId = (Long) methodParams.get("longParam");
            SupportTickets tickets = dao.findBy(SupportTickets.class, paramId);
            if (!updateTickets.getView()) {
                securityUtils.checkAccessForRespectiveEmployee(employee.getEmployeeId(), tickets.getCreatedBy(), httpServletResponse);
            }
        } 
    }

}
