package com.raybiztech.ticketmanagement.builder;

import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.recruitment.utils.DateParser;
import com.raybiztech.ticketmanagement.DTO.TicketDTO;
import com.raybiztech.ticketmanagement.business.Ticket;
import com.raybiztech.ticketmanagement.business.TicketHistory;
import com.raybiztech.ticketmanagement.business.TicketStatus;
import com.raybiztech.ticketmanagement.dao.TicketDAO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("ticketBuilder")
public class TicketBuilder {
	@Autowired
	EmployeeBuilder employeeBuilder;
	@Autowired
	TicketDAO ticketDAO;
        @Autowired
         DAO dao;

	Logger logger = Logger.getLogger(TicketBuilder.class);

	public Ticket convertTicketDTOTicket(TicketDTO ticketDTO) {

		Ticket ticket = new Ticket();
		Employee e = new Employee();

		// ticket.setEmployee(null);
		try {
			ticket.setRaisedDate(DateParser.toDate(ticketDTO.getRaisedDate()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if(ticketDTO.getTicketNumber()!=null){
			ticket.setTicketNumber(ticketDTO.getTicketNumber());
		}
		ticket.setIsApproved(ticketDTO.getIsApproved());
		ticket.setTicketNumber(ticketDTO.getTicketNumber());
		ticket.setTicketStatus(TicketStatus.InProcess);
		if (ticketDTO.getGenarateType()!= null) {
			ticket.setGenarateType(ticketDTO.getGenarateType());
		} else {
			ticket.setGenarateType(Boolean.FALSE);
			
		}

		// ticket.setTicketHistory(null);

		return ticket;

	}

	public List<TicketDTO> createTicketListTOTicketDTO(List<Ticket> ticketList) {

		List<TicketDTO> ticketDTO = new ArrayList<TicketDTO>();
                
		Iterator<Ticket> ticketIterator = ticketList.iterator();
		Long totalEmpCount=0L;
		while (ticketIterator.hasNext()) {
			Ticket ticket = ticketIterator.next();
			TicketDTO dTO = new TicketDTO();
                        String empNames=null;
			dTO.setRaisedDate(ticket.getRaisedDate().toString());
			dTO.setTicketNumber(ticket.getTicketNumber());
                        if(ticket.getTicketStatus().equals(TicketStatus.InProcess))
                        {
                            dTO.setTicketStatus("In-Process");
                        }
                        else
			dTO.setTicketStatus(ticket.getTicketStatus().toString());
			dTO.setMealId(ticket.getMealLookUp().getId());
			dTO.setMealName(ticket.getMealLookUp().getMealName());

			List<TicketHistory> ticketHistory = ticketDAO
					.getTicketHistorys(ticket.getTicketNumber());
			// ticketHistory.getEmployee().getEmployeeId();
			List<Long> empIdsList = new ArrayList<Long>();
                        List<String> empNameList = new ArrayList<String>();
			Long empCount = 0L;
			
			for (TicketHistory history : ticketHistory) {
				empIdsList.add(history.getEmployee().getEmployeeId());
                                Employee e=dao.findBy(Employee.class, history.getEmployee().getEmployeeId());
                                empNameList.add(e.getFullName());
                                if(empNames==null)
                                empNames=e.getFullName();
                                else
                                    empNames=empNames+","+e.getFullName();
				empCount++;
			}
			
			//totalEmpCount=totalEmpCount+empCount;
			if (ticket.getGenarateType() != null) {
				dTO.setGenarateType(ticket.getGenarateType());
			} else {
				dTO.setGenarateType(Boolean.FALSE);
			}

			// ticketDAO.findBy(Ticket.class,ticket.getTicketHistory());
			dTO.setTotalEmployees(totalEmpCount);
			dTO.setEmployeeIds(empIdsList);
                        dTO.setEmployeeNames(empNameList);
			dTO.setEmployeeCount(empCount);
                        dTO.setIsApproved(ticket.getIsApproved());
                        Employee authorId=dao.findBy(Employee.class, ticket.getAuthorEmpId());
			dTO.setManagerName(authorId.getFullName());
                        dTO.setEmployeeNamesList(empNames);
			ticketDTO.add(dTO);
		}

		return ticketDTO;

	}

}
