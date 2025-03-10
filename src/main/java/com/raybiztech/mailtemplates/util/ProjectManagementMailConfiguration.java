package com.raybiztech.mailtemplates.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.date.Date;
import com.raybiztech.leavemanagement.exceptions.MailCantSendException;
import com.raybiztech.mailtemplates.dao.MailTemplatesDao;
import com.raybiztech.projectMetrics.dto.EffortVarianceDTO;
import com.raybiztech.projectMetrics.dto.ScheduleVarianceDto;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.business.ProjectInitiationChecklist;
import com.raybiztech.projectmanagement.business.ProjectRequest;
import com.raybiztech.projectmanagement.dao.ResourceManagementDAO;
import com.raybiztech.projectmanagement.exception.NoCheckList;

@Component("projectManagementMailConfiguration")
public class ProjectManagementMailConfiguration {

    /***
     * @author shashank
     * **/

    @Autowired
    MailTemplatesDao mailTemplatesDaoImpl;

    @Autowired
    MailContentParser mailContentParser;

    @Autowired
    PropBean propBean;
   
    @Autowired
    ResourceManagementDAO resourceManagementDAO;
    
    @Autowired
    SessionFactory sessionFactory;

    // Project Request mailing configurations//

    public void managerRaisedProjectRequest(ProjectRequest projectRequest) throws IOException {


        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        int rowIndex=1;
        Workbook workbook=new HSSFWorkbook();
        Sheet sheet=workbook.createSheet();
        Row row1=sheet.createRow(0);
        Font font=workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 10);
        CellStyle cellStyle=workbook.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setFont(font);
       
        Cell cell0=row1.createCell(0);
        cell0.setCellValue("Name");
        cell0.setCellStyle(cellStyle);
       
        Cell cell1=row1.createCell(1);
        cell1.setCellValue("Value");
        cell1.setCellStyle(cellStyle);
       
        Cell cell2=row1.createCell(2);
        cell2.setCellValue("Comments");
        cell2.setCellStyle(cellStyle);
        Set<ProjectInitiationChecklist> checkLists=new HashSet<ProjectInitiationChecklist>();
       
        if(projectRequest!=null)
        {
            checkLists=projectRequest.getChecklist();
           
            for (ProjectInitiationChecklist dto : checkLists) {

                Row row = sheet.createRow(rowIndex++);

                Cell cel0 = row.createCell(0);
                cel0.setCellValue(dto.getChecklist().getName());

                Cell cel1 = row.createCell(1);
                cel1.setCellValue(dto.getAnswer());
                   
                Cell cel2=row.createCell(2);
           
                if(dto.getComments().isEmpty())
                {
                    cel2.setCellValue("N/A");
                   
                   
                }
               
                else
                {
                    cel2.setCellValue(dto.getComments());
                }

                sheet.autoSizeColumn(0);
                sheet.autoSizeColumn(1);
                sheet.autoSizeColumn(2);

            }
        }
       
        else
        {
            try {
                throw new NoCheckList("Check list is not available for particular project");
            } catch (NoCheckList e) {
           
            }
        }
       
       
       
       
        workbook.write(byteArrayOutputStream);
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();

        FileOutputStream fos = null;
        File someFile =null;
        String filePath=null;
        try {
       
        filePath = (String) propBean.getPropData().get("ProjectInititationCheckList");


         someFile = new File(filePath + "ProjectInitiationCheckList"+".csv");
         fos=new FileOutputStream(someFile);
         fos.write(byteArrayOutputStream.toByteArray());
        }catch (FileNotFoundException fne) {
           
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                }
            } catch (IOException ie) {
           
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ie) {

            }
        }
       
       
        //String to = (String) propBean.getPropData().get("mail.hrMailId");
        String to=projectRequest.getProjectManager().getManager().getEmail();
        String deliveryManagerName = projectRequest.getProjectManager().getManager().getFullName();
        String cc = projectRequest.getCc()+" ,"+
                (String)propBean.getPropData().get("mail.sqaMailId");
        String bcc = projectRequest.getBcc();
        if (projectRequest.getProjectManager() != null
                && !projectRequest.getProjectManager().getEmployeeId()
                        .equals(1001L)) {
            cc =projectRequest.getProjectManager().getEmail() + "," + cc;
        }
        String regarding = "<![CDATA[ Project Request ]]>";
        Map<String, String> details = new HashMap<String, String>();
        details.put("regarding", regarding);
        details.put("name", projectRequest.getProjectManager().getFullName());
        details.put("cc", cc);
        details.put("to", to);
        details.put("bcc", bcc);
        details.put("path", someFile.toString());
        details.put("toName", deliveryManagerName);
        details.put("projectName", projectRequest.getProjectName());
       
        String content = mailTemplatesDaoImpl
                .getMailContent("New Project Request");

        if (content != null) {
            mailContentParser.parseAndSendMail(details, content);
        } else {
            throw new MailCantSendException(
                    "Mail Content is not available for 'New Project Request ' template Type");
        }

    }

    public void approvedProjectRequest(Project project) {

        String cc = (String) propBean.getPropData().get("mail.hrMailId") + " ,"
                + (String) propBean.getPropData().get("mail.sqaMailId") + " ,"
                + (String) propBean.getPropData().get("mail.itAdminMailId")
                + " ,"
                + (String) propBean.getPropData().get("mail.financeMailId")
                + " ,"
                + (String) propBean.getPropData().get("mail.guptaMailId");
        String to = null;

        if (project.getProjectManager() != null
                && !project.getProjectManager().getEmployeeId().equals(1001L)) {
            to = project.getProjectManager().getEmail();
        }

        Map<String, String> details = new HashMap<String, String>();
        details.put("regarding", "<![CDATA[ Project Acceptance ]]>");
        details.put("toName", project.getProjectManager().getFullName());
        details.put("projectName", project.getProjectName());
        details.put("projectCode", project.getProjectCode());
        details.put("cc", cc);
        details.put("to", to);
        details.put("bcc", "");
        details.put("name", project.getProjectRequest().getProjectManager().getManager().getFullName());

        String content = mailTemplatesDaoImpl
                .getMailContent("Project Request Approval");

        if (content != null) {
            mailContentParser.parseAndSendMail(details, content);
        } else {
            throw new MailCantSendException(
                    "Mail Content is not available for 'Project Request Approval' template Type");
        }

    }

    public void projectAddtion(Project project) {

        String cc = (String) propBean.getPropData().get("mail.hrMailId") + " ,"
                + (String) propBean.getPropData().get("mail.sqaMailId") + " ,"
                + (String) propBean.getPropData().get("mail.itAdminMailId")
                + " ,"
                + (String) propBean.getPropData().get("mail.financeMailId")
                + " ,"
                + (String) propBean.getPropData().get("mail.guptaMailId");
        String to = null;

        if (project.getProjectManager() != null
                && !project.getProjectManager().getEmployeeId().equals(1001L)) {
            to = project.getProjectManager().getEmail();
        }

        Map<String, String> details = new HashMap<String, String>();
        details.put("regarding", "<![CDATA[ Project Addition ]]>");
        details.put("toName", project.getProjectManager().getFullName());
        details.put("projectName", project.getProjectName());
        details.put("cc", cc);
        details.put("to", to);
        details.put("bcc", "");

        String content = mailTemplatesDaoImpl
                .getMailContent("Project Addition");

        if (content != null) {
            mailContentParser.parseAndSendMail(details, content);
        } else {
            throw new MailCantSendException(
                    "Mail Content is not available for 'Project Addition' template Type");
        }

    }

    public void rejectProjectRequest(Long requestId,String comment) {

        ProjectRequest projectRequest = mailTemplatesDaoImpl.findBy(
                ProjectRequest.class, requestId);
        String cc = (String) propBean.getPropData().get("mail.hrMailId")+" ,"+
                (String)propBean.getPropData().get("mail.sqaMailId");
        String to = null;
        if (projectRequest.getProjectManager() != null
                && !projectRequest.getProjectManager().getEmployeeId()
                        .equals(1001L)) {
           
           
            /*for testing purpose we are commenting this line*/
           
            to = projectRequest.getProjectManager().getEmail();
           
           
        }

        Map<String, String> details = new HashMap<String, String>();
        details.put("regarding", "<![CDATA[ Project Cancellation ]]>");
        details.put("toName", projectRequest.getProjectManager().getFullName());
        details.put("projectName", projectRequest.getProjectName());
        details.put("cc", cc);
        details.put("to", to);
        details.put("bcc", "");
        details.put("Comment", comment);

        String content = mailTemplatesDaoImpl
                .getMailContent("Project Cancellation");

        if (content != null) {
            mailContentParser.parseAndSendMail(details, content);
        } else {
            throw new MailCantSendException(
                    "Mail Content is not available for 'Project Request Reject' template Type");
        }

    }

    // Project Mailing Configurations//

    public void closeProject(Long projectId) {

        Project project = mailTemplatesDaoImpl.findBy(Project.class, projectId);

        /*String cc = (String) propBean.getPropData().get("mail.hrMailId") + " ,"
                + (String) propBean.getPropData().get("mail.sqaMailId") + " ,"
                + (String) propBean.getPropData().get("mail.itAdminMailId")
                + " ,"
                + (String) propBean.getPropData().get("mail.financeMailId");
        String to = null;

        if (project.getProjectManager() != null
                && !project.getProjectManager().getEmployeeId().equals(1001L)) {
            to = project.getProjectManager().getEmail();
        }*/
        
        
        Employee deliveryMananger = resourceManagementDAO
                .getDeliveryManagerofProject(project);
        String cc = (String)propBean.getPropData().get("mail.itAdminMailId");
        String to = (String)propBean.getPropData().get("mail.sqaMailId");
        if(project.getProjectManager() != null && deliveryMananger != null) {
        cc = cc+" ,"+project.getProjectManager().getEmail() + " ,"
        		+ deliveryMananger.getEmail();
        }
        
        String regarding = "<![CDATA[ Project Closure ]]>";

        String closingDate = new Date().toString();

        Map<String, String> details = new HashMap<String, String>();
        details.put("regarding", regarding);
        //details.put("toName", project.getProjectManager().getFullName());
        details.put("projectName", project.getProjectName());
        details.put("fromDate", closingDate);
        details.put("projectCode", project.getProjectCode());
        details.put("name", project.getProjectManager().getFullName());
        details.put("cc", cc);
        details.put("to", to);
        details.put("bcc", "");

        String content = mailTemplatesDaoImpl.getMailContent("Project Closure");

        if (content != null) {
            mailContentParser.parseAndSendMail(details, content);
        } else {
            throw new MailCantSendException(
                    "Mail Content is not available for 'Project Closure' template Type");
        }

    }

    // on Deleting project//
    public void sendprojectDeletionMail(Project project,
            Employee deletingEmployee) {

        String cc = (String) propBean.getPropData().get("mail.hrMailId") + " ,"
                + (String) propBean.getPropData().get("mail.sqaMailId") + " ,"
                + (String) propBean.getPropData().get("mail.itAdminMailId")
                + " ,"
                + (String) propBean.getPropData().get("mail.financeMailId");
        String to = null;

        if (project.getProjectManager() != null
                && !project.getProjectManager().getEmployeeId().equals(1001L)) {
            to = project.getProjectManager().getEmail();
        }
        String regarding = "<![CDATA[ Project Deletion ]]>";

        String deletingDate = new Date().toString();

        Map<String, String> details = new HashMap<String, String>();
        details.put("regarding", regarding);
        details.put("toName", project.getProjectManager().getFullName());
        details.put("projectName", project.getProjectName());
        details.put("fromDate", deletingDate);
        details.put("name", deletingEmployee.getFullName());
        details.put("cc", cc);
        details.put("to", to);
        details.put("bcc", "");

        String content = mailTemplatesDaoImpl
                .getMailContent("Project Deletion");

        if (content != null) {
            mailContentParser.parseAndSendMail(details, content);
        } else {
            throw new MailCantSendException(
                    "Mail Content is not available for 'Project Deletion' template Type");
        }

    }

    public void sendProjectUpdationAcknowledgement(
            AllocationDetails allocationDetails, Employee employee,
            Project project) {

        String cc = (String) propBean.getPropData().get("mail.hrMailId");

        if (employee.getManager() != null
                && !employee.getManager().getEmployeeId().equals(1001L)) {
            cc = cc + " ," + employee.getManager().getEmail();
        }
        if (!employee.getManager().getEmail()
                .equalsIgnoreCase(project.getProjectManager().getEmail())
                && !employee.getEmail().equalsIgnoreCase(
                        project.getProjectManager().getEmail())) {
            cc = cc + " ," + project.getProjectManager().getEmail();
        }

        String to = employee.getEmail();

        String regarding = "<![CDATA[ Project ]]>";

        String manager = project.getProjectManager().getFullName();

        Map<String, String> details = new HashMap<String, String>();
        details.put("regarding", regarding);
        details.put("toName", employee.getFirstName());
        details.put("projectName", project.getProjectName());
        details.put("fromDate", allocationDetails.getPeriod().getMinimum()
                .toString());
        details.put("toDate", allocationDetails.getPeriod().getMaximum()
                .toString());
        details.put("name", manager);
        details.put("cc", cc);
        details.put("to", to);
        details.put("bcc", "");

        String content = mailTemplatesDaoImpl.getMailContent("Project Updates");

        if (content != null) {
            mailContentParser.parseAndSendMail(details, content);
        } else {
            throw new MailCantSendException(
                    "Mail Content is not available for ' Project Updates' template Type");
        }

    }

    // Project Allocation mail configurations..//

    public void sendProjectAllocationAcknowledgement(
            AllocationDetails allocationDetails, Employee employee,
            Project project) {

        String to = employee.getEmail();
        String cc = (String) propBean.getPropData().get("mail.hrMailId");

        if (employee.getManager() != null
                && !employee.getManager().getEmployeeId().equals(1001L)) {
            cc = cc + " ," + employee.getManager().getEmail();
        }
        if (!employee.getManager().getEmail()
                .equalsIgnoreCase(project.getProjectManager().getEmail())
                && !employee.getEmail().equalsIgnoreCase(
                        project.getProjectManager().getEmail())) {

            cc = cc + " ," + project.getProjectManager().getEmail();
        }

        /*
         * String manager = null; if
         * (project.getProjectManager().getEmployeeId() != employee
         * .getEmployeeId()) { manager =
         * project.getProjectManager().getFullName(); }
         */

        String regarding = "<![CDATA[ Project Allocation ]]>";

        Map<String, String> details = new HashMap<String, String>();
        details.put("regarding", regarding);
        details.put("toName", employee.getFirstName());
        details.put("projectName", project.getProjectName());
        details.put("fromDate", allocationDetails.getPeriod().getMinimum()
                .toString());
        details.put("toDate", allocationDetails.getPeriod().getMaximum()
                .toString());
        details.put("name", project.getProjectManager().getFullName());
        details.put("cc", cc);
        details.put("to", to);
        details.put("bcc", "");

        String content = mailTemplatesDaoImpl
                .getMailContent("Resource Allocation");

        if (content != null) {
            mailContentParser.parseAndSendMail(details, content);
        } else {
            throw new MailCantSendException(
                    "Mail Content is not available for 'Resource Allocation' template Type");
        }

    }

    public void sendProjectDeAllocationAcknowledgement(AllocationDetails ad) {

        String to = ad.getEmployee().getEmail();
        String cc = (String) propBean.getPropData().get("mail.hrMailId");

        if (ad.getEmployee().getManager() != null
                && !ad.getEmployee().getManager().getEmployeeId().equals(1001L)) {
            cc = cc + " ," + ad.getEmployee().getManager().getEmail();
        }
        if (!ad.getEmployee()
                .getManager()
                .getEmail()
                .equalsIgnoreCase(
                        ad.getProject().getProjectManager().getEmail())
                && !ad.getEmployee()
                        .getEmail()
                        .equalsIgnoreCase(
                                ad.getProject().getProjectManager().getEmail())) {
            cc = cc + " ," + ad.getProject().getProjectManager().getEmail();
        }
        String regarding = "<![CDATA[ Project De-Allocation ]]>";
        Map<String, String> details = new HashMap<String, String>();
        details.put("regarding", regarding);
        details.put("toName", ad.getEmployee().getFirstName());
        details.put("projectName", ad.getProject().getProjectName());
        details.put("fromDate", new Date().toString("dd/MM/yyyy"));
        details.put("cc", cc);
        details.put("to", to);
        details.put("bcc", "");

        String content = mailTemplatesDaoImpl
                .getMailContent("Resource De-Allocation");

        if (content != null) {
            mailContentParser.parseAndSendMail(details, content);
        } else {
            throw new MailCantSendException(
                    "Mail Content is not available for 'Resource De-Allocation' template Type");
        }
    }

    public void sendMilestoneClosedMail(Project project, String milestone,
            String status) {

        String cc = (String) propBean.getPropData().get("mail.hrMailId") + ","
                + propBean.getPropData().get("mail.financeMailId");

        Map<String, String> details = new HashMap<String, String>();
        details.put("regarding", "<![CDATA[ Milestone Closed ]]>");
        details.put("toName", project.getProjectManager().getFullName());
        details.put("projectName", project.getProjectName());
        details.put("subject", milestone);
        details.put("cc", cc);
        details.put("to", project.getProjectManager().getEmail());
        details.put("bcc", "");

        String content = mailTemplatesDaoImpl
                .getMailContent("Milestone Closure");

        if (content != null) {
            mailContentParser.parseAndSendMail(details, content);
        } else {
            throw new MailCantSendException(
                    "Mail Content is not available for 'Milestone Closure' template Type");
        }

    }

    // QUARTZ MAILS STARTS HERE//

    // Sending alert to HR and Project Manager of Resource whose allocation end
    // date is in upcoming five days..

    public void allocationDateExpiryAlert(AllocationDetails allocationDetails) {

        String cc = (String) propBean.getPropData().get("mail.hrMailId");
        String to = null;
        if (allocationDetails.getEmployee().getManager() != null
                && !allocationDetails.getEmployee().getManager()
                        .getEmployeeId().equals(1001L)) {
            to = allocationDetails.getEmployee().getManager().getEmail();
        }

        Map<String, String> details = new HashMap<String, String>();
        details.put("regarding",
                "<![CDATA[ Employee Allocation Status Alert ]]>");
        details.put("name", allocationDetails.getEmployee().getFullName());
        details.put("fromDate", allocationDetails.getPeriod().getMinimum()
                .toString("dd/MM/yyyy"));
        details.put("toDate", allocationDetails.getPeriod().getMaximum()
                .toString("dd/MM/yyyy"));
        details.put("toName", allocationDetails.getEmployee().getManager()
                .getFullName());
        details.put("projectName", allocationDetails.getProject()
                .getProjectName());
        details.put("cc", cc);
        details.put("to", to);
        details.put("bcc", "");

        String content = mailTemplatesDaoImpl
                .getMailContent("Resource Allocation Status Alert");

        if (content != null) {
            mailContentParser.parseAndSendMail(details, content);
        } else {
            throw new MailCantSendException(
                    "Mail Content is not available for 'Resource Allocation Status Alert' template Type");
        }

    }

    // project end date alert//

    public void sendProjectEndDateAlert(Project project) {
        String cc = (String) propBean.getPropData().get("mail.hrMailId");
        String to = null;
        if (project.getProjectManager() != null
                && !project.getProjectManager().getEmployeeId().equals(1001L)) {
            to = project.getProjectManager().getEmail();
        }

        Map<String, String> details = new HashMap<String, String>();
        details.put("regarding", "<![CDATA[ Project End Date Alert ]]>");
        details.put("toDate",
                project.getPeriod().getMaximum().toString("dd/MM/yyyy"));
        details.put("toName", project.getProjectManager().getFullName());
        details.put("projectName", project.getProjectName());
        details.put("cc", cc);
        details.put("to", to);
        details.put("bcc", "");

        String content = mailTemplatesDaoImpl
                .getMailContent("Project End Date Alert");

        if (content != null) {
            mailContentParser.parseAndSendMail(details, content);
        } else {
            throw new MailCantSendException(
                    "Mail Content is not available for 'Project End Date Alert' template Type");
        }
    }

    // This is used to send Weekly status reports //
    // this method is used in WeeklyStatusReportReminder class which is
    // configured in QUARTZ//

    public void weeklyStatusReportReminderMail(String leads) {

        String cc = (String) propBean.getPropData().get("mail.hrMailId");

        Map<String, String> details = new HashMap<String, String>();
        details.put("regarding", "<![CDATA[Weekly status report - Reminder ]]>");
        details.put("cc", cc);
        details.put("to", leads);
        details.put("bcc", "");

        String content = mailTemplatesDaoImpl
                .getMailContent("Weekly Status Report Reminder");

        if (content != null) {
            mailContentParser.parseAndSendMail(details, content);
        } else {
            throw new MailCantSendException(
                    "Mail Content is not available for 'Weekly Status Report Reminder' template Type");
        }

    }

    public void remindMilestoneDelay(Project project, String cc,
            String milestone, String status) {

        Map<String, String> details = new HashMap<String, String>();
        details.put("regarding", "<![CDATA[ Milestone Delay ]]>");
        details.put("toName", project.getProjectManager().getFullName());
        details.put("projectName", project.getProjectName());
        details.put("subject", milestone);
        details.put("fromDate", status);
        details.put("cc", cc);
        details.put("to", project.getProjectManager().getEmail());
        details.put("bcc", "");

        String content = mailTemplatesDaoImpl
                .getMailContent("Milestone Closure Alert");

        if (content != null) {
            mailContentParser.parseAndSendMail(details, content);
        } else {
            throw new MailCantSendException(
                    "Mail Content is not available for 'Milestone Closure Alert' template Type");
        }

    }

    public void SendProjectNotesUpdationMail(Employee deliveryManager,
            Project project) {

        String cc = (String) propBean.getPropData().get("mail.guptaMailId");

        Map<String, String> details = new HashMap<String, String>();
        details.put("regarding", "<![CDATA[ Project Notes Updation Alert ]]>");
        details.put("toName", deliveryManager.getFullName());
        details.put("projectName", project.getProjectName());
        details.put("cc", cc);
        details.put("to", deliveryManager.getEmail());
        details.put("bcc", "");

        String content = mailTemplatesDaoImpl
                .getMailContent("Project Notes Updation Alert");

        if (content != null) {
            mailContentParser.parseAndSendMail(details, content);
        } else {
            throw new MailCantSendException(
                    "Mail Content is not available for 'Project Notes Updation Alert' template Type");
        }

    }

 //project request updation mail configuration
    public void sendProjectRequestUpdationMail(ProjectRequest projectRequest) {
        String to = projectRequest.getProjectManager().getManager().getEmail();
        String cc = projectRequest.getCc()+" ,"+
                (String)propBean.getPropData().get("mail.sqaMailId");
        String bcc = projectRequest.getBcc();
        String deliveryManagerName = projectRequest.getProjectManager().getManager().getFullName();
        if (projectRequest.getProjectManager() != null
                && !projectRequest.getProjectManager().getEmployeeId()
                        .equals(1001L)) {
            cc =projectRequest.getProjectManager().getEmail() + "," + cc;
        }
        Map<String, String> details = new HashMap<String, String>();
        details.put("regarding", "<![CDATA[ Project Request Updation ]]>");
        details.put("name", projectRequest.getProjectManager().getFullName());
        details.put("cc", cc);
        details.put("to", to);
        details.put("bcc", bcc);
        details.put("toName", deliveryManagerName);
        details.put("projectName", projectRequest.getProjectName());
       
        String content = mailTemplatesDaoImpl.getMailContent("Project Request Updation");
       
        if(content != null){
           
            mailContentParser.parseAndSendMail(details, content);
           
            }
        else{
            throw new MailCantSendException(
                    "Mail Content is not available for 'Project Request Updation' template Type");
        }
        }
   
   
  //project metrics mail configuration
    
    public void sendMetricsRemainderToPM(List<Project> scheduleEffortProjectList,List<Project> effortProjectList,List<Project> scheduleProjectList,Set<Long> managerIds) {
    	
    	HashMap<Long,HashMap<String, List<Project>>> projectMetricsMap = new HashMap<Long,HashMap<String, List<Project>>>();
    	
    	Set<Long> scheduleeffortsids=new HashSet<Long>();
		Set<Long> effortids=new HashSet<Long>();
		Set<Long> scheduleids=new HashSet<Long>();

    	
    	for(Long mangerId:managerIds)
    	{
    		
    		
    		
    		
    		for(Project project:scheduleEffortProjectList)
    		{
    			//Map<String,List<Project>> scheduleEffortMap1=new HashMap<String,List<Project>>();
    			
    			if(project.getProjectManager().getEmployeeId().equals(mangerId))
    			{
    				
    				
    			if(!scheduleeffortsids.contains(mangerId))
    			{
    				Map<String,List<Project>> map1=null;
    				if(projectMetricsMap.containsKey(mangerId))
    				{
    					map1= projectMetricsMap.get(mangerId);
    				}
    				else
    				{
    					map1=new HashMap<String,List<Project>>();
    				}
    				scheduleeffortsids.add(project.getProjectManager().getEmployeeId());
    				List<Project> scheduleEffortProjectsList=new ArrayList<Project>();
    				scheduleEffortProjectsList.add(project);
    				map1.put("ScheduleEffort", scheduleEffortProjectsList);
    				projectMetricsMap.put(mangerId, (HashMap<String, List<Project>>) map1);
    				
    				
    	    		
    	    
    			}
    			
    			else
    			{
    			
    				Map<String,List<Project>> map1= projectMetricsMap.get(mangerId);
    				
    				List<Project> existinPRojects=map1.get("ScheduleEffort");
    				existinPRojects.add(project);
    				map1.put("ScheduleEffort", existinPRojects);
    				projectMetricsMap.put(mangerId, (HashMap<String, List<Project>>) map1);
    			}
    			}
    		}
    		
    		
    		
    		for(Project project:effortProjectList)
    		{
    			//Map<String,List<Project>> effortMap1=new HashMap<String,List<Project>>();
    			if(project.getProjectManager().getEmployeeId().equals(mangerId))
    			{
    				if(!effortids.contains(mangerId))
        			{
    					Map<String,List<Project>> map1=null;
        				if(projectMetricsMap.containsKey(mangerId))
        				{
        					map1= projectMetricsMap.get(mangerId);
        				}
        				else
        				{
        					map1=new HashMap<String,List<Project>>();
        				}
        				
    					
        				effortids.add(project.getProjectManager().getEmployeeId());
        				List<Project> scheduleEffortProjectsList=new ArrayList<Project>();
        				scheduleEffortProjectsList.add(project);
        				map1.put("Effort", scheduleEffortProjectsList);
        				projectMetricsMap.put(mangerId,  (HashMap<String, List<Project>>) map1);
        			}
        			else
        			{
        				
        				Map<String,List<Project>> map1= projectMetricsMap.get(mangerId);
        				
        				List<Project> existinPRojects=map1.get("Effort");
        				existinPRojects.add(project);
        				map1.put("Effort", existinPRojects);
        				projectMetricsMap.put(mangerId, (HashMap<String, List<Project>>) map1);
        			}
    			}
    			
    		}
    		
    		for(Project project:scheduleProjectList)
    		{
    			//Map<String,List<Project>> scheduleMap1=new HashMap<String,List<Project>>();
    			if(project.getProjectManager().getEmployeeId().equals(mangerId))
    			{
    			if(!scheduleids.contains(mangerId))
    			{
    				
    				Map<String,List<Project>> map1=null;
    				if(projectMetricsMap.containsKey(mangerId))
    				{
    					map1= projectMetricsMap.get(mangerId);
    				}
    				else
    				{
    					map1=new HashMap<String,List<Project>>();
    				}
    				
    				scheduleids.add(project.getProjectManager().getEmployeeId());
    				List<Project> scheduleEffortProjectsList=new ArrayList<Project>();
    				scheduleEffortProjectsList.add(project);
    				map1.put("Schedule", scheduleEffortProjectsList);
    				projectMetricsMap.put(mangerId, (HashMap<String, List<Project>>) map1);
    			}
    			else
    			{
    				
    			
    				Map<String,List<Project>> map1= projectMetricsMap.get(mangerId);
    				
    				List<Project> existinPRojects=map1.get("Schedule");
    				existinPRojects.add(project);
    				map1.put("Schedule", existinPRojects);
    				projectMetricsMap.put(mangerId, (HashMap<String, List<Project>>) map1);
    			}
    		}
    		}
    		
    		
    		
    		Map<String,List<Project>> overAllProjectMangerMap =projectMetricsMap.get(mangerId);
    		

    		
    		List<Project> scheduleEfforProjects=new ArrayList<Project>();
    		List<Project> effortProjects=new ArrayList<Project>();
    		List<Project> scheduleProjects=new ArrayList<Project>();
    		
    	
    		
    		if(overAllProjectMangerMap.containsKey("ScheduleEffort"))
    		{
    		
    			scheduleEfforProjects=overAllProjectMangerMap.get("ScheduleEffort");
    		
    		}
    		
    		if(overAllProjectMangerMap.containsKey("Effort"))
    		{
    			effortProjects=overAllProjectMangerMap.get("Effort");
    			
    			
    		}
    		
    		if(overAllProjectMangerMap.containsKey("Schedule"))
    		{
    			scheduleProjects=overAllProjectMangerMap.get("Schedule");
    			
    		}
    		
    		
    
    		
    		 StringBuilder tableContent = new StringBuilder();
    		 String projectmanagerEmail=null;
    		 String deliveryManager=null;
    		 String projectMangerName=null;
    		 tableContent.append("<table>"
 	        		+ "<thead>" 
 	        		+ "<tr>"
 	        		+ "<th><b>Project Name</b></th>"
 	        		+ "<th><b>Schedule/Effort</b></th>"
 	        		+ "</tr>"
 	        		+ "</thead>");
    		 
    		for(Project p:scheduleEfforProjects)
    		{
    			
    			projectMangerName=p.getProjectManager().getFullName();
    			projectmanagerEmail=p.getProjectManager().getEmail();
    			deliveryManager=p.getProjectManager().getManager().getEmail();
    			tableContent=content("Schedule and Effort", p,tableContent);
    			
    			
    			
    		}
    		for(Project p:effortProjects)
    		{
    			projectMangerName=p.getProjectManager().getFullName();
    			projectmanagerEmail=p.getProjectManager().getEmail();
    			deliveryManager=p.getProjectManager().getManager().getEmail();
    			tableContent=content("Effort", p,tableContent);

    		}
    		for(Project p:scheduleProjects)
    		{
    			projectMangerName=p.getProjectManager().getFullName();
    			projectmanagerEmail=p.getProjectManager().getEmail();
    			deliveryManager=p.getProjectManager().getManager().getEmail();
    			tableContent=content("Schedule", p,tableContent);
    			
    			
    		}
    		 tableContent.append("</table>");
    	        tableContent.append("<style>" +
    	        					"table {border: none;border-spacing: 5px;}" + 
    	        					"tr {font-size: 14px;}" + 
    	        					"th {padding:6px 15px 0 15px;align-content:left;}" + 
    	        					"td {padding:6px 15px 0 15px;align-content:left;}" + 
    	        					"</style>");
    	        
    	        Map<String, String> details = new HashMap<String,String>();
    	        
    	       /* String cc = deliveryManager+" ,"+
    	                (String)propBean.getPropData().get("mail.sqaMailId");*/
    	        details.put("regarding", "<![CDATA[ Project Metrics Remainder ]]>");
    	        details.put("to", projectmanagerEmail);
    	        details.put("cc", deliveryManager);
    	        details.put("name",projectMangerName);
    	        details.put("bcc", "");
    
    	        details.put("table", tableContent.toString());
    	       
    	        String content = mailTemplatesDaoImpl.getMailContent("Project Metrics Remainder");
    	        if(content!=null) {
    	            mailContentParser.parseAndSendMail(details, content);
    	          
    	            //System.out.println("mail sent successfully");
    	        }
    	        
    	        else
    	        {
    	        throw new MailCantSendException(
    	                "Mail Content is not available for 'Project Metrics Remainder' template Type");
    	    }
    		
    	}
    	
    	
    	
    	
        /*String to = project.getProjectManager().getEmail();
        
        Employee deliveryMananger = resourceManagementDAO
                .getDeliveryManagerofProject(project);
        
        String cc = deliveryMananger.getEmail();
       
        Long EmpId = project.getProjectManager().getEmployeeId();
       
        //System.out.println("project manager:" + project.getProjectManager().getEmployeeFullName());
       
        List<Project> detailsList = resourceManagementDAO.getActiveProjects(EmpId);
        
        StringBuilder tableContent = new StringBuilder();
        tableContent.append("<table>"
        		+ "<thead>" 
        		+ "<tr>"
        		+ "<th><b>Project Id</b></th>"
        		+ "<th><b>Project Name</b></th>"
        		+ "</tr>"
        		+ "</thead>");
        
        for(Project list : detailsList) {
        	tableContent = tableContent.append("<tbody>"
        			+"<tr>"
        			+ "<td>"+list.getProjectCode()+"</td>"
        			+ "<td>"+list.getProjectName()+"</td>"
        			+ "</tr>"
        			+ "</tbody>");
        			
        }
        tableContent.append("</table>");
        tableContent.append("<style>" +
        					"table {border: none;border-spacing: 5px;}" + 
        					"tr {font-size: 14px;}" + 
        					"th {padding:6px 15px 0 15px;align-content:left;}" + 
        					"td {padding:6px 15px 0 15px;align-content:left;}" + 
        					"</style>");*/
        
        
        //System.out.println("projects:" + projectName);
        /*Map<String, String> details = new HashMap<String,String>();
       
       
        details.put("regarding", "<![CDATA[ Project Metrics Remainder ]]>");
        details.put("to", "sowmya.mamidi@raybiztech.com");
        details.put("cc", "");
        details.put("bcc", "");
        details.put("name", project.getProjectManager().getFullName());
        details.put("table", tableContent.toString());
       
        String content = mailTemplatesDaoImpl.getMailContent("Project Metrics Remainder");
        if(content!=null) {
            mailContentParser.parseAndSendMail(details, content);
            //System.out.println("mail sent successfully");
        }
        else
        {
        throw new MailCantSendException(
                "Mail Content is not available for 'Project Metrics Remainder' template Type");
    }*/
       
    }


    public void sendEffortVariancemailNotification(
            EffortVarianceDTO effortVarianceDTO) {
        String cc = (String) propBean.getPropData().get("mail.sqaMailId");

        Project project = mailTemplatesDaoImpl.findBy(Project.class,
                effortVarianceDTO.getProjectId());

        Employee deliveryMananger = resourceManagementDAO
                .getDeliveryManagerofProject(project);
       
       
        if(deliveryMananger!=null) {
        String to = deliveryMananger.getEmail();

       
        Map<String, String> details = new HashMap<String, String>();
        details.put("regarding", "<![CDATA[Project Effort Variance]]>");
        details.put("toName", deliveryMananger.getFullName());
        details.put("name", project.getProjectManager().getFullName());
        details.put("cc", cc);
        details.put("to", to);
        details.put("bcc", "");
        details.put("projectName", project.getProjectName());
        details.put("projectCode", project.getProjectCode());

        String content = mailTemplatesDaoImpl
                .getMailContent("Project EffortVariance");

        if (content != null) {
            mailContentParser.parseAndSendMail(details, content);
        }

        else {
            throw new MailCantSendException(
                    "Mail Content is not available for 'Project EffortVariance' template Type");
        }
       
       }

    }

    public void sendSheduleVariancemailNotification(
            ScheduleVarianceDto scheduleVarianceDTO) {
        String cc = (String) propBean.getPropData().get("mail.sqaMailId");

        Project project = mailTemplatesDaoImpl.findBy(Project.class,
                scheduleVarianceDTO.getProjectId());

        Employee deliveryMananger = resourceManagementDAO
                .getDeliveryManagerofProject(project);

        if(deliveryMananger!=null) {
        String to = deliveryMananger.getEmail();


        Map<String, String> details = new HashMap<String, String>();
        details.put("regarding", "<![CDATA[ Project Schedule Variance]]>");
        details.put("toName", deliveryMananger.getFullName());
        details.put("name", project.getProjectManager().getFullName());
        details.put("cc", cc);
        details.put("to", to);
        details.put("bcc", "");
        details.put("projectName", project.getProjectName());
        details.put("projectCode", project.getProjectCode());

        String content = mailTemplatesDaoImpl
                .getMailContent("Project ScheduleVariance");

        if (content != null) {
            mailContentParser.parseAndSendMail(details, content);
        }

        else {
            throw new MailCantSendException(
                    "Mail Content is not available for 'Project ScheduleVariance' template Type");
        }
        }

    }
    
    public StringBuilder content(String projectMetrics,Project p,StringBuilder tableContent) {
    	

		 //StringBuilder tableContent = new StringBuilder();
		  
		  
		  tableContent = tableContent.append("<tbody>"
      			+"<tr>"
      			+ "<td>"+p.getProjectName()+"</td>"
      			+ "<td>"+projectMetrics+"</td>"
      			+ "</tr>"
      			+ "</tbody>");
    	
		return tableContent;
    	
    }
    
    public void milestoneClosureReminder(Project project, String cc,
            String milestone, String status, Date milestoneEndDate) {

        Map<String, String> details = new HashMap<String, String>();
        details.put("regarding", "<![CDATA[ Milestone Closure Reminder ]]>");
        details.put("toName", project.getProjectManager().getFullName());
        details.put("projectName", project.getProjectName());
        details.put("subject", milestone);
        details.put("fromDate", status);
        details.put("date", milestoneEndDate.toString());
        details.put("cc", cc);
        details.put("to", project.getProjectManager().getEmail());
        details.put("bcc", "");

        String content = mailTemplatesDaoImpl
                .getMailContent("Milestone Closure Reminder");

        if (content != null) {
            mailContentParser.parseAndSendMail(details, content);
        } else {
            throw new MailCantSendException(
                    "Mail Content is not available for 'Milestone Closure Reminder' template Type");
        }

    }
   
}