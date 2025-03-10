package com.raybiztech.ticketmanagement.service;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Calendar;

import com.raybiztech.date.Date;

import java.util.List;

import com.raybiztech.ticketmanagement.DTO.MealLookUpDTO;
import com.raybiztech.ticketmanagement.DTO.TicketDTO;
import com.raybiztech.ticketmanagement.business.Ticket;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;
public interface TicketService {
	

	/**
	 * This method is used to raise ticket 
	 * @param ticketDTO *
	 *
	 * @param 
	 */
	List<String> raiseTicket(TicketDTO ticketDTO);
	
	
	/**
	 * This method is used to update raised ticket 
	 * @param ticketDTO *
	 *
	 * @param 
	 * @return 
	 */
	   void updateTicket(Long ticketNumber);
	
	/**
	 * This method is used to delete ticket *
	 *
	 * @param 
	 */
	void deleteTicket();
	
	
	/**
	 * This method is used to get all Tickets
	 */
	Map<String, Object> getAllTickets(Long employeeid,String ticketStatus,Date date);


	List<MealLookUpDTO> getmeallookup();


	void cancelTicket(Long ticketNumber);


	void approveTicket(Long ticketNumber);
        void approveMultipleTickets(List<Long> ticketNumber);


	void rejectTicket(Long ticketNumber);


	String getdateandtime();


	ByteArrayOutputStream exporttickets(Long empId,Date date, String ticketStatus,HttpServletResponse response);
        



	
	
	

}
