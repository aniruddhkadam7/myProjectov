/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.projectmanagement.mailnotifcation;

import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.date.Date;
import com.raybiztech.mail.sender.MessageSender;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.business.ProjectRequest;
import com.raybiztech.projectmanagement.dao.ResourceManagementDAO;
import javax.jms.JMSException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author anil
 */
@Component("projectAcknowledgement")
public class ProjectAcknowledgement {

    @Autowired
    MessageSender messageSender;

    @Autowired
    PropBean propBean;
    @Autowired
     ResourceManagementDAO resourceManagementDAO;

    Logger logger = Logger.getLogger(ProjectAcknowledgement.class);

    public PropBean getPropBean() {
        return propBean;
    }

    public void setPropBean(PropBean propBean) {
        this.propBean = propBean;
    }

    public void managerRaisedProjectRequest(ProjectRequest projectRequest) {

        String to = (String) propBean.getPropData().get("mail.hrMailId");
        String cc = null;
        if (projectRequest.getProjectManager()!= null
                && !projectRequest.getProjectManager().getEmployeeId().equals(1001L)) {
            cc = projectRequest.getProjectManager().getEmail();
        }
//        if(cc!=null)
//        cc=cc+" ,"+(String) propBean.getPropData().get("mail.sqaMailId");
//        else
//          cc=(String) propBean.getPropData().get("mail.sqaMailId");   
      //  logger.warn("to mail id :"+to+"cc mail id:"+cc);
        
        String bcc = " ";

		String subject = "<![CDATA[ Project Request ]]>";
		StringBuffer buffer = new StringBuffer();

		buffer.append("<![CDATA[Dear Admin Team,")
				.append(" <br><br> Request you to create a new project in OVH. The details are been updated, please do the needful. ")

				.append(" <br><br><br> Regards,<br>"+projectRequest.getProjectManager().getFullName()).append(" <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA.]]>");

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
    
    public void approvedProjectRequest(Project project){
        
        String cc = (String) propBean.getPropData().get("mail.hrMailId")+" ,"+(String) propBean.getPropData().get("mail.sqaMailId")+" ,"+(String) propBean.getPropData().get("mail.itAdminMailId")+" ,"+(String) propBean.getPropData().get("mail.financeMailId");
        String to=null;
        
         if (project.getProjectManager()!= null
                && !project.getProjectManager().getEmployeeId().equals(1001L)) {
            to = project.getProjectManager().getEmail();
        }
       //logger.warn("to mail id :"+to+"cc mail id:"+cc);
       
       String bcc = " ";

		String subject = "<![CDATA[ Project Acceptance ]]>";
		StringBuffer buffer = new StringBuffer();

		buffer.append("<![CDATA[Dear "+project.getProjectManager().getFullName())
				.append(", <br><br> Your request has been accepted by the department. The project details are updated in OVH. ")
                                .append(" <br><br> Request you to check and let us know if any more information to be added. ")
				.append(" <br><br><br> Regards,<br>").append("Admin Team <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA.]]>");

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
    
  public void closeProject(Long projectId){
      Project project = resourceManagementDAO
				.findBy(Project.class, projectId);
      
       String cc = (String) propBean.getPropData().get("mail.hrMailId")+" ,"+(String) propBean.getPropData().get("mail.sqaMailId")+" ,"+(String) propBean.getPropData().get("mail.itAdminMailId")+" ,"+(String) propBean.getPropData().get("mail.financeMailId");
        String to=null;
        
         if (project.getProjectManager()!= null
                && !project.getProjectManager().getEmployeeId().equals(1001L)) {
            to = project.getProjectManager().getEmail();
        }
       //logger.warn("to mail id :"+to+"cc mail id:"+cc);
       
       String bcc = " ";

		String subject = "<![CDATA[ Project Closure ]]>";
		StringBuffer buffer = new StringBuffer();

		buffer.append("<![CDATA[Dear "+project.getProjectManager().getFullName())
				.append(", <br><br> We would like to inform you that "+project.getProjectName()+"  is closed on "+new Date().toString("dd/MM/yyyy")+".")
                                .append(" <br><br> Feel free to contact HR team for further queries. ")
				.append(" <br><br><br> Regards,<br>").append("Admin Team <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA.]]>");

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
  
  public void rejectProjectRequest(Long requestId){
      ProjectRequest projectRequest=resourceManagementDAO.findBy(ProjectRequest.class, requestId);
      
       String cc = (String) propBean.getPropData().get("mail.hrMailId");
        String to=null;
        
         if (projectRequest.getProjectManager()!= null
                && !projectRequest.getProjectManager().getEmployeeId().equals(1001L)) {
            to = projectRequest.getProjectManager().getEmail();
        }
       //logger.warn("to mail id :"+to+"cc mail id:"+cc);
       
       String bcc = " ";

		String subject = "<![CDATA[ Project Cancellation ]]>";
		StringBuffer buffer = new StringBuffer();

		buffer.append("<![CDATA[Dear "+projectRequest.getProjectManager().getFullName())
				.append(", <br><br> Your project request for " +projectRequest.getProjectName()+" has been cancelled by the department. ")
                                .append(" <br><br> Feel free to contact concern department for further queries. ")
				.append(" <br><br><br> Regards,<br>").append("Admin Team <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA.]]>");

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
}
