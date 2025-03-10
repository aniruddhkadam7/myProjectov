/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.security.aop;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.exceptions.UnauthorizedUserException;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.ticketmanagement.DTO.TicketDTO;
import com.raybiztech.ticketmanagement.business.Ticket;
import com.raybiztech.ticketmanagement.business.TicketHistory;
import com.raybiztech.ticketmanagement.dao.TicketDAO;
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
public class TicketManagementAop {

    @Autowired
    DAO dao;
    @Autowired
    SecurityUtils securityUtils;
    @Autowired
    TicketDAO ticketdaoImpl;

    Logger logger = Logger.getLogger(TicketManagementAop.class);

    @Before("execution(* com.raybiztech.ticketmanagement.controller.*.*(..))")
    public void ticketManagementController(JoinPoint joinPoint) throws IOException {

        String methodName = joinPoint.getSignature().getName();
        Map<String, Object> employeeDeatils = securityUtils.getLoggedEmployeeDetailsSecurityContextHolder();
        Employee employee = (Employee) employeeDeatils.get("employee");
        Map<String, Object> methodParams = securityUtils.getMethodParams(joinPoint.getArgs());
        HttpServletResponse httpServletResponse = (HttpServletResponse) methodParams
                .get("httpServletResponse");
        TicketDTO ticketDTO = (TicketDTO) methodParams.get("ticketDTO");

        if (methodName.equalsIgnoreCase("raiseticket")) {

            List<Long> employees = ticketDTO.getEmployeeIds();
            Employee ticketEmpId = null;
            for (Long e : employees) {
                ticketEmpId = dao.findBy(Employee.class, e);
            }
            if (!(employee.getRole().equalsIgnoreCase("admin") || employee.getRole().equalsIgnoreCase("Manager"))) {
                securityUtils.checkAccessForRespectiveEmployee(employee.getEmployeeId(), ticketEmpId.getEmployeeId(), httpServletResponse);
            }
        }

        if (methodName.equalsIgnoreCase("cancelticket")) {
            Long paramId = (Long) methodParams.get("longParam");
            Ticket ticket = dao.findBy(Ticket.class, paramId);
            List<TicketHistory> historys = ticketdaoImpl.getTicketHistorys(ticket.getTicketNumber());
            if (historys.size() == 1) {
                for (TicketHistory ticketHistory : historys) {
                    Long empId = ticketHistory.getEmployee().getEmployeeId();

                    if (!(employee.getRole().equalsIgnoreCase("admin") || employee.getRole().equalsIgnoreCase("Manager"))) {
                        securityUtils.checkAccessForRespectiveEmployee(employee.getEmployeeId(), empId, httpServletResponse);
                    }
                }
            } else {
                if (!(employee.getRole().equalsIgnoreCase("admin") || employee.getRole().equalsIgnoreCase("Manager"))) {
                    securityUtils.printIpAddress();
                    httpServletResponse
                            .sendError(httpServletResponse.SC_FORBIDDEN);
                    throw new UnauthorizedUserException("Employee with name  "
                            + employee.getFullName() + " Trying to cancel the ticket" + paramId);
                }
            }
        }

    }

}
