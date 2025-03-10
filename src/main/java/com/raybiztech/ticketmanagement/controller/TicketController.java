package com.raybiztech.ticketmanagement.controller;

import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.recruitment.utils.DateParser;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.ticketmanagement.DTO.MealLookUpDTO;
import com.raybiztech.ticketmanagement.DTO.TicketDTO;
import com.raybiztech.ticketmanagement.service.TicketService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/ticket-mgmt")
public class TicketController {
	@Autowired
	TicketService ticketServiceImpl;
	@Autowired
	SecurityUtils securityUtils;
	Logger logger = Logger.getLogger(TicketController.class);

	@RequestMapping(value = "/getmeallookup", method = RequestMethod.GET)
	public @ResponseBody List<MealLookUpDTO> getMealLookup() {
		return ticketServiceImpl.getmeallookup();
	}

	@RequestMapping(value = "/raiseticket", method = RequestMethod.POST)
	public @ResponseBody List<String> raiseTicket(@RequestBody TicketDTO ticketDTO,HttpServletResponse httpServletResponse) {
		return ticketServiceImpl.raiseTicket(ticketDTO);
	}

	@RequestMapping(value = "/updateticket",params = {"ticketNumber"}, method = RequestMethod.PUT)
	public @ResponseBody void updateTicket(@RequestParam Long ticketNumber) {
		 ticketServiceImpl.updateTicket(ticketNumber);
	}

	@RequestMapping(value = "/cancelticket", params = { "ticketNumber" }, method = RequestMethod.PUT)
	public @ResponseBody void cancelTicket(@RequestParam Long ticketNumber,HttpServletResponse httpServletResponse)
			throws Exception {
		ticketServiceImpl.cancelTicket(ticketNumber);
	}

	@RequestMapping(value = "/approveticket", params = { "ticketNumber" }, method = RequestMethod.PUT)
	public @ResponseBody void approve(@RequestParam Long ticketNumber) {
		ticketServiceImpl.approveTicket(ticketNumber);

	}
        @RequestMapping(value = "/approveMultipleTickets",params = {"ticketNumber"},method = RequestMethod.PUT)
        public @ResponseBody void approveMultipleTickets(@RequestParam List<Long> ticketNumber){
            ticketServiceImpl.approveMultipleTickets(ticketNumber);
        }

	@RequestMapping(value = "/rejectticket", params = { "ticketNumber" }, method = RequestMethod.PUT)
	public @ResponseBody void rejectTicket(@RequestParam Long ticketNumber)
			throws Exception {
		ticketServiceImpl.rejectTicket(ticketNumber);
	}

	@RequestMapping(value = "/getAllTicketsList", params = { "ticketStatus",
			"date" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAllTicketsList(
			@RequestParam String ticketStatus, @RequestParam String date) {

		Date parsedDate = null;

		try {
			parsedDate = DateParser.toDate(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ticketServiceImpl.getAllTickets(
				securityUtils.getLoggedEmployeeIdforSecurityContextHolder(),
				ticketStatus, parsedDate);
	}

	@RequestMapping(value = "/getdateandtime", method = RequestMethod.GET)
	public @ResponseBody String getdateandtime() {

		return ticketServiceImpl.getdateandtime();

	}

	@RequestMapping(value = "/exporttickets",params = { "date","ticketStatus"},  method = RequestMethod.GET)
	public @ResponseBody void exporttickets(@RequestParam String date,@RequestParam String ticketStatus,HttpServletResponse response) throws IOException {
		
		
		Date parsedDate = null;

		try {
			parsedDate = DateParser.toDate(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	// ticketServiceImpl.exporttickets(parsedDate,ticketStatus);
	 
	 
	 response.setContentType("text/csv");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"TicketList.csv\"");
		ByteArrayOutputStream os = ticketServiceImpl.exporttickets(securityUtils.getLoggedEmployeeIdforSecurityContextHolder(),parsedDate,ticketStatus,response);
		response.getOutputStream().write(os.toByteArray());
	 
	 

	}
	/*@RequestMapping(value = "/needApproval", method = RequestMethod.POST)
	public @ResponseBody List<String> needApproval(@RequestBody TicketDTO ticketDTO) {
		return ticketServiceImpl.needApproval(ticketDTO);
	}*/

}
