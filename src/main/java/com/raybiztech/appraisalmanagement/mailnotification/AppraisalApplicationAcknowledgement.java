/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisalmanagement.mailnotification;

import com.raybiztech.appraisalmanagement.business.AppraisalForm;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.mail.sender.MessageSender;
import static com.raybiztech.projectmanagement.mailnotifcation.ProjectAllocationAcknowledgement.logger;
import javax.jms.JMSException;
import org.apache.log4j.Logger;
import org.jfree.data.time.Year;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author anil
 */
@Component("appraisalApplicationAcknowledgement")
public class AppraisalApplicationAcknowledgement {

    @Autowired
    PropBean propBean;

    @Autowired
    MessageSender messageSender;
    @Autowired
    SecurityUtils securityUtils;
    @Autowired
    DAO dao;

    public PropBean getPropBean() {
        return propBean;
    }

    public void setPropBean(PropBean propBean) {
        this.propBean = propBean;
    }

    public static Logger logger = Logger.getLogger(AppraisalApplicationAcknowledgement.class);

    public void sendEmpAppraisalApplicationAcknowledgement(AppraisalForm appraisalForm) {
          String to = "";
          String cc = (String) propBean.getPropData().get("mail.hrMailId");
     
        if(appraisalForm.getEmployee().getEmployeeId().equals(securityUtils.getLoggedEmployeeIdforSecurityContextHolder())){
              
            to = appraisalForm.getEmployee().getManager().getEmail();
           
            cc=cc + " ," + appraisalForm.getEmployee().getEmail();
        }
        
       
        String startDate=appraisalForm.getAppraisalCycle().getAppraisalPeriod().getMinimum().toString();
        String[] startDate2=startDate.split(" ");
        String endDate=appraisalForm.getAppraisalCycle().getAppraisalPeriod().getMaximum().toString();
        String[] endDate2=endDate.split(" ");

     
        // String to=appraisalForm.getEmployee().getManager().getEmail();

        logger.warn("to mail:" + to);
        
        logger.warn("cc mail:" + cc);
         logger.warn("appraisal period:"+startDate2[0]+startDate2[1]+startDate2[2]);
        String bcc = " ";

        String subject = "<![CDATA[ Performance Appraisal - ]]>" + startDate2[1]+" "+startDate2[2]+" "+"to"+" "+endDate2[1]+" "+endDate2[2];
        StringBuffer buffer = new StringBuffer();

        buffer.append("<![CDATA[Dear " + appraisalForm.getEmployee().getManager().getFirstName())
                // .append(employee.getFullName())
                .append(", <br><br><br> Please be informed that ")
                .append(appraisalForm.getEmployee().getFullName() + " has updated Appraisal Form ")
                .append(". <br><br> You are requested to review it and update the feedback. ")
                .append("<br><br><br> For further queries feel free to contact HR department")
                // .append(" to ")
                // .append(allocationDetails.getPeriod().getMaximum().toString())
                // .append(comments)
                //.append(". You will be reporting to Mr."
                //+ project.getProjectManager().getFullName())
               .append(". <br><br><br> Regards,<br>").append(" HR <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA."+"]]>");

        String body = buffer.toString();
        // System.out.println("body:"+body);
        // System.out.println("sending......");
        logger.warn("Sending Mail.....");

        try {
            messageSender.sendMessage("<email><to>" + to + "</to>" + "<cc>"
                    + cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>"
                    + subject + "</subject>" + "<body>" + body
                    + "</body></email>");
        } catch (JMSException ex) {
            logger.error(ex.getMessage());
        }

    }

    public void sendMangerAppraisalApplicationAcknowledgement(AppraisalForm appraisalForm) {
        String to = "";
        System.out.println("emp mail:" + to);

        String cc = (String) propBean.getPropData().get("mail.hrMailId");

            String empIds[]=appraisalForm.getManagesList().split(",");
            Employee empManager=dao.findBy(Employee.class, Long.parseLong(empIds[empIds.length-2]));
            to=empManager.getManager().getEmail();
            cc=cc + " ," + appraisalForm.getEmployee().getEmail()+" ,"+empManager.getEmail();
      
        String bcc = " ";
        
  String startDate=appraisalForm.getAppraisalCycle().getAppraisalPeriod().getMinimum().toString();
        String[] startDate2=startDate.split(" ");
        String endDate=appraisalForm.getAppraisalCycle().getAppraisalPeriod().getMaximum().toString();
        String[] endDate2=endDate.split(" ");
       // System.out.println(splitdate[0]+"-"+splitdate[1]+"-"+splitdate[2]);
          logger.warn("to mail:" + to);
        
        logger.warn("cc mail:" + cc);
   logger.warn("appraisal period:"+startDate2[0]+startDate2[1]+startDate2[2]);

        String subject = "<![CDATA[  Performance Appraisal - ]]>" + startDate2[1]+" "+startDate2[2]+" "+"to"+" "+endDate2[1]+" "+endDate2[2];
        StringBuffer buffer = new StringBuffer();

        buffer.append("<![CDATA[Dear " + appraisalForm.getEmployee().getFirstName())
                // .append(employee.getFullName())
                .append(", <br><br><br> Please be informed that your Performance rating has been updated by " + appraisalForm.getEmployee().getManager().getFullName())
                //.append(project.getProjectName())
                .append(". <br><br><br> For further queries feel free to contact HR department")
                //.append(allocationDetails.getPeriod().getMinimum().toString())
                // .append(" to ")
                // .append(allocationDetails.getPeriod().getMaximum().toString())
                // .append(comments)
                //.append(". You will be reporting to Mr."
                //	+ project.getProjectManager().getFullName())
                 .append(". <br><br><br> Regards,<br>").append(" HR <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA."+"]]>");

        String body = buffer.toString();
        // System.out.println("body:"+body);
        // System.out.println("sending......");
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
    public void sendMangerForOpenForDiscussion(AppraisalForm appraisalForm){
         String to = "";
          String cc = (String) propBean.getPropData().get("mail.hrMailId");
         to=appraisalForm.getEmployee().getEmail();
            String empIds[]=appraisalForm.getManagesList().split(",");
            for(int i=0;i<=empIds.length-1; i++){
                Employee managers=dao.findBy(Employee.class, Long.parseLong(empIds[i]));
                to=to+" ,"+managers.getEmail();
            }
            
            
            
             String startDate=appraisalForm.getAppraisalCycle().getAppraisalPeriod().getMinimum().toString();
        String[] startDate2=startDate.split(" ");
        String endDate=appraisalForm.getAppraisalCycle().getAppraisalPeriod().getMaximum().toString();
        String[] endDate2=endDate.split(" ");

        logger.warn("to mail:" + to);
        
        logger.warn("cc mail:" + cc);
        logger.warn("appraisal period:"+startDate2[0]+startDate2[1]+startDate2[2]);
        String bcc = " ";

        String subject = "<![CDATA[ Performance Appraisal - ]]>" + startDate2[1]+" "+startDate2[2]+" "+"to"+" "+endDate2[1]+" "+endDate2[2];
        StringBuffer buffer = new StringBuffer();

        buffer.append("<![CDATA[Dear " + appraisalForm.getEmployee().getManager().getFirstName())
                // .append(employee.getFullName())
                .append(", <br><br><br> Please be informed that ")
                .append(appraisalForm.getEmployee().getFullName() + " has updated Appraisal Form ")
                .append(". <br><br> You are requested to review it and update the feedback. ")
                .append("<br><br><br> For further queries feel free to contact HR department")
                // .append(" to ")
                // .append(allocationDetails.getPeriod().getMaximum().toString())
                // .append(comments)
                //.append(". You will be reporting to Mr."
                //+ project.getProjectManager().getFullName())
               .append(". <br><br><br> Regards,<br>").append(" HR <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA."+"]]>");

        String body = buffer.toString();
        // System.out.println("body:"+body);
        // System.out.println("sending......");
        logger.warn("Sending Mail.....");

        try {
            messageSender.sendMessage("<email><to>" + to + "</to>" + "<cc>"
                    + cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>"
                    + subject + "</subject>" + "<body>" + body
                    + "</body></email>");
        } catch (JMSException ex) {
            logger.error(ex.getMessage());
        }
    }

}
