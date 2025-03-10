package com.raybiztech.SQAAudit.quartz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.SQAAudit.business.SQAAuditForm;
import com.raybiztech.SQAAudit.mailNotification.SQAAuditMailNotification;
import com.raybiztech.SQAAudit.service.SQAAuditService;
import com.raybiztech.date.Date;

@Transactional
@Component("pendingAuditFollowUpDateAlert")
public class PendingAuditFollowUpDateAlert {

	@Autowired
	SQAAuditService sqaAuditServiceImpl;
	
	@Autowired
	SQAAuditMailNotification sqaAuditMailNotification;
	
	// getting SQA Audit details

	public void auditDetails() throws ParseException {
	List<SQAAuditForm> sqaDetails = sqaAuditServiceImpl.getAuditWhoseAuditStatusIsOpen();
	
	if(!sqaDetails.isEmpty()){
		for(SQAAuditForm form : sqaDetails){
			
			SimpleDateFormat form1 = new SimpleDateFormat("dd/MM/yy");
			/*Date date = new Date();
			String d1 = form1.format(date);
			String d2 = form1.format(form.followUpDate);
			
			java.util.Date start = form1.parse(d1);
			java.util.Date end = form1.parse(d2);
			
			long diff = start.getTime() - end.getTime();
			
			long diffDays = diff / (24 * 60 * 60 * 1000);
			
			*/
			Date d1 = new Date();
			Date d2 = form.getFollowUpDate().next();
			
			System.out.println("Folloupdate = "+form.getFollowUpDate());
			System.out.println("Audit Name ="+form.getAuditType());
			
			String end = d1.toString("dd/MM/yy");
			String start = d2.toString("dd/MM/yy");
			
			System.out.println("End Time = "+end+"  Start Time = "+start);
			
			java.util.Date start1 = form1.parse(end);
			java.util.Date end1 = form1.parse(start);
			
			long diff = start1.getTime() - end1.getTime();
			
			long diffDays = diff / (24 * 60 * 60 * 1000);
			
			System.out.println("Diff Days is = "+diffDays);
			
			
			// Every 3ed day mail will go after cross follow-up Date.
			if(diffDays == 0){
				System.out.println("IN IF condition");
				sqaAuditMailNotification.sendFollowUpEndDateAlert(form);
				System.out.println("Cron is running for same days");
			}// inside inner else if
			else{
				Date d11 = new Date();
				Date d21 = form.getFollowUpDate().next();
				
				String end11 = d11.toString("dd/MM/yy");
				String start11 = d21.toString("dd/MM/yy");
				
				java.util.Date start12 = form1.parse(end11);
				java.util.Date end12 = form1.parse(start11);
				
				long diff2= start1.getTime() - end1.getTime();
				
				long diffDay = diff2 / (24 * 60 * 60 * 1000);
				
				// If different day is only positive number
				if(diffDay > 0){
					System.out.println("Diff Days is = "+diffDays);
					// Every 3ed day mail will go after cross follow-up Date.
					if(diffDay%3 == 0){
						sqaAuditMailNotification.sendFollowUpEndDateAlert(form);
					}// inside inner else if
				}
				
			
			}// inner else
			
		} // for
	}
	
	}
	
	
	
}
