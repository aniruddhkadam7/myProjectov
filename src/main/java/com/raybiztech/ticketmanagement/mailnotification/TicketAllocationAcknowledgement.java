/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.ticketmanagement.mailnotification;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.mail.sender.MessageSender;
import com.raybiztech.ticketmanagement.DTO.TicketDTO;
import com.raybiztech.ticketmanagement.builder.TicketBuilder;
import com.raybiztech.ticketmanagement.business.Ticket;
import com.raybiztech.ticketmanagement.business.TicketHistory;
import com.raybiztech.ticketmanagement.dao.TicketDAO;
import java.util.ArrayList;
import java.util.List;
import javax.jms.JMSException;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author anil
 */
@Component("ticketAllocationAcknowledgement")
public class TicketAllocationAcknowledgement {

    @Autowired
    MessageSender messageSender;

    @Autowired
    PropBean propBean;
    @Autowired
    SecurityUtils securityUtils;
    @Autowired
    DAO dao;
    @Autowired
    TicketDAO ticketdaoImpl;
    @Autowired
    TicketBuilder ticketBuilder;
    @Autowired
    SessionFactory sessionFactory;

    public static Logger logger = Logger.getLogger(TicketAllocationAcknowledgement.class);

    public PropBean getPropBean() {
        return propBean;
    }

    public void setPropBean(PropBean propBean) {
        this.propBean = propBean;
    }

    public void sendRaiseTicketAcknowledgement(TicketDTO ticketDTO, Ticket ticket) {

        Long id = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
        Employee loggedInEmp = dao.findBy(Employee.class, id);
        String cc = loggedInEmp.getEmail();
        String managerName = loggedInEmp.getFullName();
        List<Long> empId = new ArrayList<Long>();

        if (ticketDTO != null) {
            empId = ticketDTO.getEmployeeIds();
        } else {
            List<TicketHistory> ticketHistory1 = ticketdaoImpl.getTicketHistorys(ticket.getTicketNumber());

            for (TicketHistory ticketHistory : ticketHistory1) {

                empId.add(ticketHistory.getEmployee().getEmployeeId());
            }
        }
        String mealType = ticket.getMealLookUp().getMealName();

        String to = null;
        String bcc = " ";

        String subject = "<![CDATA[ Ticket status for ]]>" + mealType;
        StringBuffer buffer = new StringBuffer();
       // logger.warn("the ticket raise date is"+ticket.getRaisedDate());
            //If Employee raises the ticket
        if (loggedInEmp.getRole().equalsIgnoreCase("Employee")) {
            to = loggedInEmp.getManager().getEmail();
            buffer.append("<![CDATA[Dear " + loggedInEmp.getManager().getFullName())
                    // .append(employee.getFullName())
                    .append(", <br><br><br> The " + mealType + " ticket has been raised by Mr. / Ms. " + loggedInEmp.getFullName()+" on " +ticket.getRaisedDate().toString("dd/MM/yyyy")+ " .Request you to address the ticket. ")
                    .append(" <br><br><br> Regards,<br>").append(loggedInEmp.getFullName() +" <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA." + "]]>");
        } 
        //If the manager raises the ticket for their respected team
        else {
            to = (String) propBean.getPropData().get("mail.supportMailId");
            for (Long eid : empId) {
                Employee employee = dao.findBy(Employee.class, eid);
                cc = cc + " ," + employee.getEmail();
            }
            //logger.warn("cc mail:" + cc);
            buffer.append("<![CDATA[Dear Team")
                    // .append(employee.getFullName())
                    .append(", <br><br><br> The " + mealType + " ticket has been raised by Mr. / Ms. " + managerName + " for the mentioned associates on "+ticket.getRaisedDate().toString("dd/MM/yyyy") +".Request you to address the ticket. ")
                    .append(" <br><br><br> Regards,<br>").append(managerName+"  <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA." + "]]>");
        }

        String body = buffer.toString();
        logger.warn("Sending Mail.....");

        try {

            messageSender.sendMessage("<email><to>" + to + "</to>" + "<cc>"
                    + cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>"
                    + subject + "</subject>" + "<body>" + body
                    + "</body></email>");
            logger.warn("mail sent successfully");

        } catch (JMSException ex) {
            logger.error(ex.getMessage());
        }

    }

    public void sendCancelTicketAcknowledgement(Ticket ticket) {

        Long id = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
        Employee loggedInEmp = dao.findBy(Employee.class, id);
        String cc = loggedInEmp.getEmail();
        String managerName = loggedInEmp.getFullName();
        String mealType = ticket.getMealLookUp().getMealName();
        List<TicketHistory> historys = ticketdaoImpl.getTicketHistorys(ticket.getTicketNumber());

        for (TicketHistory ticketHistory : historys) {
            Long empId = ticketHistory.getEmployee().getEmployeeId();
            Employee employee = dao.findBy(Employee.class, empId);
            cc = cc + " ," + employee.getEmail();
        }
     //   logger.warn("cc mail:" + cc);

        String to = "";
        String bcc = " ";

        String subject = "<![CDATA[ Ticket status for ]]>" + mealType;
        StringBuffer buffer = new StringBuffer();
         
        if (loggedInEmp.getRole().equalsIgnoreCase("Manager")) {
          to=(String) propBean.getPropData().get("mail.supportMailId");
           buffer.append("<![CDATA[Dear All")
                    // .append(employee.getFullName())
                    .append(", <br><br><br> Please be informed that the "+mealType+" ticket has been cancelled by Mr./Ms. " + managerName + "on "+ticket.getRaisedDate().toString("dd/MM/yyyy")+". ")
                    .append("  <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA." + "]]>");
          
        } 
//        else {
//          to=loggedInEmp.getManager().getEmail();
//           buffer.append("<![CDATA[Dear "+loggedInEmp.getManager().getFullName())
//                    // .append(employee.getFullName())
//                    .append(", <br><br><br> Please be informed that the ticket has been cancelled by Mr./Ms. " + managerName + ". ")
//                    .append("  <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA." + "]]>");
//            
//        }

        String body = buffer.toString();
        logger.warn("Sending Mail.....");

        try {
            messageSender.sendMessage("<email><to>" + to + "</to>" + "<cc>"
                    + cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>"
                    + subject + "</subject>" + "<body>" + body
                    + "</body></email>");
            logger.warn("mail sent successfully");
        } catch (JMSException ex) {
            logger.error(ex.getMessage());
        }
    }

    public void sendAcceptTicketAcknowledgement(Ticket ticket) {

//             Long id=securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
//            Employee loggedInEmp=dao.findBy(Employee.class, id);
//            String cc=loggedInEmp.getEmail();
//             logger.warn("to mail:"+cc);
        Long authorId = ticket.getAuthorEmpId();
        Employee e = dao.findBy(Employee.class, authorId);

        String to = e.getEmail();
        String cc = (String) propBean.getPropData().get("mail.supportMailId");
        String managerName = e.getFullName();
        String mealType = ticket.getMealLookUp().getMealName();
        List<TicketHistory> historys = ticketdaoImpl.getTicketHistorys(ticket.getTicketNumber());

        for (TicketHistory ticketHistory : historys) {
            Long empId = ticketHistory.getEmployee().getEmployeeId();
            Employee employee = dao.findBy(Employee.class, empId);
            cc = cc + " ," + employee.getEmail();
        }
      //  logger.warn("to mail:" + to);
      
        String bcc = " ";

        String subject = "<![CDATA[ Ticket status for ]]>" + mealType;
        StringBuffer buffer = new StringBuffer();

        buffer.append("<![CDATA[Dear " + managerName)
                // .append(employee.getFullName())
                .append(", <br><br><br> Please be informed that your "+mealType+" ticket has been accepted by the admin team on "+ticket.getRaisedDate().toString("dd/MM/yyyy")+" . Request you to contact the admin team for further queries. ")
                .append(" <br><br><br> Regards,<br>").append(" Admin Team <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA." + "]]>");

        String body = buffer.toString();
        logger.warn("Sending Mail.....");

        try {
            messageSender.sendMessage("<email><to>" + to + "</to>" + "<cc>"
                    + cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>"
                    + subject + "</subject>" + "<body>" + body
                    + "</body></email>");
            logger.warn("mail sent successfully");
        } catch (JMSException ex) {
            logger.error(ex.getMessage());
        }
    }

    public void sendRejectTicketAcknowledgement(Ticket ticket) {
        Long id = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
        Employee loggedInEmp = dao.findBy(Employee.class, id);
//            Long id=securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
//            Employee loggedInEmp=dao.findBy(Employee.class, id);
//            String cc=loggedInEmp.getEmail();
//             logger.warn("to mail:"+cc);
        Long authorId = ticket.getAuthorEmpId();
        Employee e = dao.findBy(Employee.class, authorId);

        String to = "";
        String managerName = e.getFullName();
        String mealType = ticket.getMealLookUp().getMealName();
        List<TicketHistory> historys = ticketdaoImpl.getTicketHistorys(ticket.getTicketNumber());

        for (TicketHistory ticketHistory : historys) {
            Long empId = ticketHistory.getEmployee().getEmployeeId();
            Employee employee = dao.findBy(Employee.class, empId);
            to = to + " ," + employee.getEmail();
        }
      //  logger.warn("to mail:" + to);
        String cc = "";
        String bcc = " ";

        String subject = "<![CDATA[ Ticket status for ]]>" + mealType;
        StringBuffer buffer = new StringBuffer();
        if (loggedInEmp.getRole().equalsIgnoreCase("Manager")) {

            cc = e.getEmail();
            buffer.append("<![CDATA[Dear Associates")
                    // .append(employee.getFullName())
                    .append(", <br><br><br> Please be informed that your "+mealType+" ticket has been rejected by the " + loggedInEmp.getFullName() +" on  "+ticket.getRaisedDate().toString("dd/MM/yyyy")+ ". Request you to contact your manager for further queries. ")
                    .append(" <br><br><br> Regards,<br>").append(loggedInEmp.getFullName()+" <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA." + "]]>");
        } else {

            cc = (String) propBean.getPropData().get("mail.supportMailId");
            to = to + "," + e.getEmail();
            buffer.append("<![CDATA[Dear Associates" )
                    // .append(employee.getFullName())
                    .append(", <br><br><br> Please be informed that your "+mealType+" ticket has been rejected by the team "+ticket.getRaisedDate().toString("dd/MM/yyyy")+" . Request you to contact the team for further queries. ")
                    .append(" <br><br><br> Regards,<br>").append(" Admin Team <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA." + "]]>");
        }

        String body = buffer.toString();
        logger.warn("Sending Mail.....");

        try {
            messageSender.sendMessage("<email><to>" + to + "</to>" + "<cc>"
                    + cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>"
                    + subject + "</subject>" + "<body>" + body
                    + "</body></email>");
            logger.warn("mail sent successfully");
        } catch (JMSException ex) {
            logger.error(ex.getMessage());
        }

    }

//    public void sendAcceptMultipleTickets(Ticket ticket) {
//
//        Long id = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
//        Employee loggedInEmp = dao.findBy(Employee.class, id);
//        String cc = loggedInEmp.getEmail();
//        logger.warn("to mail:" + cc);
//        Long authorId = ticket.getAuthorEmpId();
//        Employee e = dao.findBy(Employee.class, authorId);
//
//        String to = e.getEmail();
//        List<TicketHistory> historys = ticketdaoImpl.getTicketHistorys(ticket.getTicketNumber());
//
//        for (TicketHistory ticketHistory : historys) {
//            Long empId = ticketHistory.getEmployee().getEmployeeId();
//            Employee employee = dao.findBy(Employee.class, empId);
//            to = to + " ," + employee.getEmail();
//        }
//        logger.warn("to mail:" + to);
//
//    }
    //Method is to send the mail to employee when a manager accepts the ticket
    public void sendAcceptTicketAckByMgrToEmployee(Ticket ticket) {
        Long authorId = ticket.getAuthorEmpId();
        Employee e = dao.findBy(Employee.class, authorId);
        String empName = null;
        String to = "";
        String cc = e.getEmail();
        String managerName = e.getFullName();
        String mealType = ticket.getMealLookUp().getMealName();
        List<TicketHistory> historys = ticketdaoImpl.getTicketHistorys(ticket.getTicketNumber());

        for (TicketHistory ticketHistory : historys) {
          //  logger.warn("the ticket history isemployee idx is"+ticketHistory.getEmployee().getEmployeeId());
            Long empId = ticketHistory.getEmployee().getEmployeeId();
            Employee employee = dao.findBy(Employee.class, empId);
            empName = employee.getFullName();
            to = to + " ," + employee.getEmail();
        }
        //logger.warn("to mail:" + to);
        //String cc = "";
        String bcc = " ";

        String subject = "<![CDATA[ Ticket status for ]]>" + mealType;
        StringBuffer buffer = new StringBuffer();

        buffer.append("<![CDATA[Dear " + empName)
                // .append(employee.getFullName())
                .append(", <br><br><br> Please be informed that your "+mealType+" ticket has been accepted by " + managerName + " on "+ticket.getRaisedDate().toString("dd/MM/yyyy") + " . ")
                .append(" <br><br><br> Regards,<br>")
                .append(managerName+" <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA." + "]]>");

        String body = buffer.toString();
        logger.warn("Sending Mail.....");

        try {
            messageSender.sendMessage("<email><to>" + to + "</to>" + "<cc>"
                    + cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>"
                    + subject + "</subject>" + "<body>" + body
                    + "</body></email>");
            logger.warn("mail sent successfully");
        } catch (JMSException ex) {
            logger.error(ex.getMessage());
        }
    }
}
