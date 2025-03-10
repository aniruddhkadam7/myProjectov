package com.raybiztech.ticketmanagement.dao;

import com.raybiztech.appraisals.business.Employee;

import java.util.List;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.date.Date;
import com.raybiztech.ticketmanagement.DTO.MealLookUpDTO;
import com.raybiztech.ticketmanagement.business.MealLookUp;
import com.raybiztech.ticketmanagement.business.Ticket;
import com.raybiztech.ticketmanagement.business.TicketHistory;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public interface TicketDAO extends DAO {

	List<MealLookUpDTO> getmeallookup();

        Map<String, Object> getAllTicketsList(String ticketStatus,Date date,String role);
        List<Ticket> getExportTicketData(Date date,String ticketStatus);
        List<TicketHistory> getTicketHistorys(Long ticketHistoryId);

	Map<String, Object> getManagerTickets(Long employeeid, String ticketStatus,Date date);

	List<Ticket> getManagerExportlist(Long empId, Date date,
			String ticketStatus, HttpServletResponse response);
       Boolean getticketlist(Date date,MealLookUp mealLookup,Employee employee);
       public Map<String, Object> getIndividualTickets(Long employeeid,String status, Date date);
        
}
