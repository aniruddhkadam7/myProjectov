package com.raybiztech.ticketmanagement.service;

import com.raybiztech.appraisals.business.Employee;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.recruitment.utils.DateParser;
import com.raybiztech.ticketmanagement.DTO.MealLookUpDTO;
import com.raybiztech.ticketmanagement.DTO.TicketDTO;
import com.raybiztech.ticketmanagement.builder.TicketBuilder;
import com.raybiztech.ticketmanagement.business.MealLookUp;
import com.raybiztech.ticketmanagement.business.Ticket;
import com.raybiztech.ticketmanagement.business.TicketHistory;
import com.raybiztech.ticketmanagement.business.TicketStatus;
import com.raybiztech.ticketmanagement.dao.TicketDAO;
import com.raybiztech.ticketmanagement.exceptions.DateException;
import com.raybiztech.ticketmanagement.exceptions.DateTimeException;
import com.raybiztech.ticketmanagement.exceptions.RequestNotFoundException;
import com.raybiztech.ticketmanagement.mailnotification.TicketAllocationAcknowledgement;
import java.text.ParseException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.transaction.annotation.Transactional;

@Service("ticketServiceImpl")

public class TicketServiceImpl implements TicketService {

    @Autowired
    DAO dao;
    @Autowired
    TicketDAO ticketdaoImpl;
    @Autowired
    TicketBuilder ticketBuilder;
    @Autowired
    SecurityUtils securityUtils;
    @Autowired
    TicketAllocationAcknowledgement allocationAcknowledgement;

    Logger logger = Logger.getLogger(TicketServiceImpl.class);

    @Override
    @Transactional
    public List<String> raiseTicket(TicketDTO ticketDTO) {

       String date = ticketDTO.getRaisedDate();
		List<String> dublicateNames = new ArrayList<String>();
		String serverdate = this.getdateandtime();

		String[] dateonly = serverdate.split(" ");
		String timeonly = dateonly[1];

		String[] houronly = timeonly.split(":");
		DateFormat dfo = new SimpleDateFormat("dd/MM/yyyy");

		
		Date servaerDate = null;
		try {
			servaerDate = DateParser.toDate(dateonly[0]);

		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		Date frontdate = null;
		try {
			frontdate = DateParser.toDate(date);

		} catch (ParseException e1) {
			e1.printStackTrace();
		}

        if (dateonly[0].equalsIgnoreCase(date)) {
            if (Integer.parseInt(houronly[0]) >= 17) {
                throw new DateTimeException("After 5 you can't raise Ticket");
            } else {

                TicketHistory ticketHistory = null;

           //     List<Long> ticketdtolist1 = ticketDTO.getEmployeeIds();
                Set<Long> set=new HashSet<Long>(ticketDTO.getEmployeeIds());
                List<Long> ticketdtolist = new ArrayList<Long>(set);

                Date raisedDate = null;
                Boolean raiseTicketFlag = false;
                
                for (Long ticketdtoempoyeeIds : ticketdtolist) {
                    Employee employee = dao.findBy(Employee.class,
                            ticketdtoempoyeeIds);
                   
                    MealLookUp lookUp = dao.findBy(MealLookUp.class, ticketDTO.getMealId());
                  
                    try {
                        raisedDate = DateParser.toDate(ticketDTO.getRaisedDate());
                    } catch (ParseException ex) {
                        java.util.logging.Logger.getLogger(TicketServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Boolean flag=true;
                    if(ticketDTO.getTicketNumber()==null){
                     flag = ticketdaoImpl.getticketlist(raisedDate, lookUp, employee);
                   
                    }                    

                    if (!flag) 
                    {
                       
                        raiseTicketFlag = true;
                        dublicateNames.add(employee.getFullName());
                    }

                }
                if (!raiseTicketFlag) {
                    
                    Ticket ticket = ticketBuilder.convertTicketDTOTicket(ticketDTO);

                    MealLookUp mealLookUp = dao.findBy(MealLookUp.class,
                            ticketDTO.getMealId());
                    ticket.setMealLookUp(mealLookUp);
                    Long id = securityUtils
                            .getLoggedEmployeeIdforSecurityContextHolder();
                    Employee loggedInEmp = dao.findBy(Employee.class, id);
                    
                    //Here Author name is setting
                    
                    ticket.setGenarateType(Boolean.FALSE);
                    if(!loggedInEmp.getRole().equalsIgnoreCase("Employee")){
                    ticket.setAuthorEmpId(loggedInEmp.getEmployeeId());
                    }else
                    {
                    	ticket.setAuthorEmpId(loggedInEmp.getManager().getEmployeeId());
                    }
                    if(ticketDTO.getTicketNumber()==null){
                      
                    Long t = (Long) dao.save(ticket);
                    Ticket ticket1 = dao.findBy(Ticket.class, t);

                    for (Long ticketdtoempoyeeIds : ticketdtolist) {
                        ticketHistory = new TicketHistory();
                        ticketHistory.setTicket(ticket1);
                        Employee employee = dao.findBy(Employee.class,
                                ticketdtoempoyeeIds);
                        ticketHistory.setEmployee(employee);
                        dao.save(ticketHistory);
                    }
                    }else{
                       
                    	dao.saveOrUpdate(ticket);
                    }
                    try {
                        allocationAcknowledgement
                                .sendRaiseTicketAcknowledgement(ticketDTO, ticket);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    

                } else {

                    logger.warn("the ticket is already rasied");

                }

            }

        } else if (frontdate.isAfter(servaerDate)) {

             TicketHistory ticketHistory = null;

                 List<Long> ticketdtolist1 = ticketDTO.getEmployeeIds();
                Set<Long> set=new HashSet<Long>(ticketdtolist1);
                List<Long> ticketdtolist = new ArrayList<Long>(set);
                
                Date raisedDate = null;
                Boolean raiseTicketFlag = false;
                for (Long ticketdtoempoyeeIds : ticketdtolist) {
                
                    Employee employee = dao.findBy(Employee.class,
                            ticketdtoempoyeeIds);
                    MealLookUp lookUp = dao.findBy(MealLookUp.class, ticketDTO.getMealId());
                    try {
                        raisedDate = DateParser.toDate(ticketDTO.getRaisedDate());
                    } catch (ParseException ex) {
                        java.util.logging.Logger.getLogger(TicketServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   Boolean flag=true;
                    if(ticketDTO.getTicketNumber()==null){
                     flag = ticketdaoImpl.getticketlist(raisedDate, lookUp, employee);
                   
                    }                    

                    if (!flag) 
                    {
                       
                        raiseTicketFlag = true;
                        dublicateNames.add(employee.getFullName());
                    }

                }
                if (!raiseTicketFlag) {
                    

                    Ticket ticket = ticketBuilder.convertTicketDTOTicket(ticketDTO);

                    MealLookUp mealLookUp = dao.findBy(MealLookUp.class,
                            ticketDTO.getMealId());
                    ticket.setMealLookUp(mealLookUp);
                    Long id = securityUtils
                            .getLoggedEmployeeIdforSecurityContextHolder();
                    Employee loggedInEmp = dao.findBy(Employee.class, id);
                    ticket.setGenarateType(Boolean.FALSE);
                    // Here Author name is setting 
                    
                    if(!loggedInEmp.getRole().equalsIgnoreCase("Employee"))
                    {
                        ticket.setAuthorEmpId(loggedInEmp.getEmployeeId());
                        }
                    else
                        {
                        	ticket.setAuthorEmpId(loggedInEmp.getManager().getEmployeeId());
                        }
                    if(ticketDTO.getTicketNumber()==null){
                       
                    Long t = (Long) dao.save(ticket);
                    Ticket ticket1 = dao.findBy(Ticket.class, t);

                    for (Long ticketdtoempoyeeIds : ticketdtolist) {
                        ticketHistory = new TicketHistory();
                        ticketHistory.setTicket(ticket1);
                        Employee employee = dao.findBy(Employee.class,
                                ticketdtoempoyeeIds);
                        ticketHistory.setEmployee(employee);
                        dao.save(ticketHistory);
                    }
                }else{
                       
                	dao.saveOrUpdate(ticket);
                }
                    try {
                        allocationAcknowledgement
                                .sendRaiseTicketAcknowledgement(ticketDTO, ticket);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    logger.warn("the ticket is already rasied");

                }


        } else {
            throw new DateException("you can't raise ticket for previous dates");
        }
        return dublicateNames;

    }

    // dao.getSessionFactory().getCurrentSession().flush();
    @Override
     @Transactional
    public void updateTicket(Long ticketNumber) {
        Ticket ticket=ticketdaoImpl.findBy(Ticket.class, ticketNumber);
        ticket.setIsApproved("TRUE");
       
        ticketdaoImpl.update(ticket);
        allocationAcknowledgement.sendAcceptTicketAckByMgrToEmployee(ticket);
        allocationAcknowledgement.sendRaiseTicketAcknowledgement(null, ticket);
        
        
        // return ticket;
    }

    @Override
    @Transactional
    public Map<String, Object> getAllTickets(Long employeeid,
            String ticketStatus, Date date) {
        Employee employee = dao.findBy(Employee.class, employeeid);

        Map<String, Object> map = null;

      
      if("admin".equalsIgnoreCase(employee.getRole()) || ("Admin Support".equalsIgnoreCase(employee.getRole()))){
            map = ticketdaoImpl.getAllTicketsList(ticketStatus, date,employee.getRole());

            List<Ticket> tickets = null;
            Integer listSize = null;
            List<TicketDTO> ticketDTOs = new ArrayList<TicketDTO>();
            Object obj = map.get("ticketList");

            tickets = (List<Ticket>) obj;

            listSize = (Integer) map.get("size");

            ticketDTOs = ticketBuilder.createTicketListTOTicketDTO(tickets);
            map.put("ticketList", ticketDTOs);
            map.put("size", listSize);

        }
        else if("Manager".equalsIgnoreCase(employee.getRole())){
        	  map = ticketdaoImpl.getManagerTickets(employeeid, ticketStatus,date);
              List<Ticket> tickets = null;
              Integer listSize = null;
              List<TicketDTO> ticketDTOs = new ArrayList<TicketDTO>();
              Object obj = map.get("ticketList");

              tickets = (List<Ticket>) obj;

              listSize = (Integer) map.get("size");

              ticketDTOs = ticketBuilder.createTicketListTOTicketDTO(tickets);
              map.put("ticketList", ticketDTOs);
              map.put("size", listSize);
        }else{
        	 map = ticketdaoImpl.getIndividualTickets(employeeid, ticketStatus,date);
        	// map = ticketdaoImpl.getManagerTickets(employeeid, ticketStatus,date);
             List<Ticket> tickets = null;
             Integer listSize = null;
             List<TicketDTO> ticketDTOs = new ArrayList<TicketDTO>();
             Object obj = map.get("ticketList");

             tickets = (List<Ticket>) obj;

             listSize = (Integer) map.get("size");

             ticketDTOs = ticketBuilder.createTicketListTOTicketDTO(tickets);
             map.put("ticketList", ticketDTOs);
             map.put("size", listSize);
        }
        
        return map;

    }

    @Override
    
    public List<MealLookUpDTO> getmeallookup() {
        // TODO Auto-generated method stub
        return ticketdaoImpl.getmeallookup();
    }

    @Override
   
    public void deleteTicket() {
        // TODO Auto-generated method stub

    }

    @Override
    @Transactional
    public void cancelTicket(Long ticketNumber) {
        // TODO Auto-generated method stub
        Ticket ticket = dao.findBy(Ticket.class, ticketNumber);
        if (ticket.getTicketStatus() == TicketStatus.InProcess) {
            ticket.setTicketStatus(TicketStatus.Cancelled);
            ticket.setGenarateType(Boolean.TRUE);
            dao.saveOrUpdate(ticket);
            try {
                allocationAcknowledgement.sendCancelTicketAcknowledgement(ticket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new RequestNotFoundException(
                    "the ticket is not in In_Process status");
        }
    }

    @Override
    @Transactional
    public void approveTicket(Long TicketNumber) {

        Ticket ticket = dao.findBy(Ticket.class, TicketNumber);
        if (ticket.getTicketStatus() == TicketStatus.InProcess) {
            ticket.setTicketStatus(TicketStatus.Accepted);
            ticket.setGenarateType(Boolean.TRUE);
            ticket.setIsApproved("TRUE");
            dao.saveOrUpdate(ticket);
            try {
                allocationAcknowledgement.sendAcceptTicketAcknowledgement(ticket);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            throw new RequestNotFoundException(
                    "the ticket is not in In_Process status");
        }

    }

    @Override
    @Transactional
    public void approveMultipleTickets(List<Long> ticketNumber) {
        for (Long ticketnum : ticketNumber) {
            Ticket ticket = dao.findBy(Ticket.class, ticketnum);
            if (ticket.getTicketStatus() == TicketStatus.InProcess) {
                ticket.setTicketStatus(TicketStatus.Accepted);
                ticket.setGenarateType(Boolean.TRUE);
                dao.saveOrUpdate(ticket);
                try {
                    allocationAcknowledgement.sendAcceptTicketAcknowledgement(ticket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                throw new RequestNotFoundException(
                        "the ticket is not in In_Process status");
            }

        }

    }

    @Override
     @Transactional
    public void rejectTicket(Long ticketNumber) {
        // TODO Auto-generated method stub

        Ticket ticket = dao.findBy(Ticket.class, ticketNumber);
        Employee employee=dao.findBy(Employee.class,securityUtils.getLoggedEmployeeIdforSecurityContextHolder());
        
        if (ticket.getTicketStatus() == TicketStatus.InProcess) {
            ticket.setTicketStatus(TicketStatus.Rejected);
            ticket.setGenarateType(Boolean.TRUE);
          
            dao.saveOrUpdate(ticket);
            try {
                allocationAcknowledgement.sendRejectTicketAcknowledgement(ticket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new RequestNotFoundException(
                    "the ticket is not in In_Process status");
        }

    }

    @Override
    public String getdateandtime() {
        // TODO Auto-generated method stub
		/*
         * Date d = new Date(); System.out.println("date is"+d);
         * Map<String,Object> map=new HashMap<String,Object>(); Time time=new
         * Time(); Unit<Time>[] th=time.getUnits();
         * //System.out.println("time units are"+th);
         * System.out.println("time units are"+d.getTimeUnit());
         * System.out.println(time); return DateParser.toString(d);
         */
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar calobj = Calendar.getInstance();
        String date = df.format(calobj.getTime());
        return date;

    }

    @Override
    public ByteArrayOutputStream exporttickets(Long empId, Date date,
            String ticketStatus, HttpServletResponse response) {
      //  logger.warn("set property");
        System.setProperty("java.awt.headless", "true");
        // TODO Auto-generated method stub
        Employee employee1 = dao.findBy(Employee.class, empId);

        if (employee1.getRole().equalsIgnoreCase("Manager")) {
            List<Ticket> ticket = ticketdaoImpl.getManagerExportlist(empId,
                    date, ticketStatus, response);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                Workbook workbook = new HSSFWorkbook();
                Sheet sheet = workbook.createSheet();
                // int rowMainIndex=1;
                Row row1 = sheet.createRow(0);

                Font font = workbook.createFont();
                font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                font.setFontHeightInPoints((short) 10);
                CellStyle style = workbook.createCellStyle();

                style.setAlignment(CellStyle.ALIGN_CENTER);
                style.setFont(font);

                Cell cell0 = row1.createCell(0);
                cell0.setCellValue("Ticket Number");
                cell0.setCellStyle(style);

                Cell cell1 = row1.createCell(1);
                cell1.setCellValue("Manager Name");
                cell1.setCellStyle(style);

                Cell cell2 = row1.createCell(2);
                cell2.setCellValue("Employee Name");
                cell2.setCellStyle(style);

                Cell cell3 = row1.createCell(3);
                cell3.setCellValue("Ticket Status");
                cell3.setCellStyle(style);

                // Cell cell4 = row1.createCell(4);
                // cell4.setCellValue("Project Allocation(%)");
                // cell4.setCellStyle(style);
                Cell cell4 = row1.createCell(4);
                cell4.setCellValue("Date");
                cell4.setCellStyle(style);

                Cell cell5 = row1.createCell(5);
                cell5.setCellValue("Meal Type");
                cell5.setCellStyle(style);

                Cell cell6 = row1.createCell(6);
                cell6.setCellValue("Signature");
                cell6.setCellStyle(style);

                int rowIndex = 1;
                int empTicketList = 0;

                for (Ticket ticketlist : ticket) {

                    List<TicketHistory> ticketHistory = ticketdaoImpl
                            .getTicketHistorys(ticketlist.getTicketNumber());
                    Employee e = dao.findBy(Employee.class, ticketlist.getAuthorEmpId());

                    try {

                        for (TicketHistory history : ticketHistory) {
                            Row row = sheet.createRow(rowIndex++);
                            Employee employee = dao.findBy(Employee.class,
                                    history.getEmployee().getEmployeeId());
                            Cell cel0 = row.createCell(0);
                            cel0.setCellValue(ticketlist.getTicketNumber());

                            Cell cel1 = row.createCell(1);
                            cel1.setCellValue(e.getFullName());

                            Cell cel2 = row.createCell(2);
                            cel2.setCellValue(employee.getFullName());

                            Cell cel3 = row.createCell(3);
                            cel3.setCellValue(ticketlist.getTicketStatus()
                                    .getTicketStatus());

                            Cell cel4 = row.createCell(4);
                            cel4.setCellValue(ticketlist.getRaisedDate()
                                    .toString());

                            Cell cel5 = row.createCell(5);
                            cel5.setCellValue(ticketlist.getMealLookUp()
                                    .getMealName());
                            empTicketList++;

                        }
                        sheet.autoSizeColumn(0);
                        sheet.autoSizeColumn(1);
                        sheet.autoSizeColumn(2);
                        sheet.autoSizeColumn(3);
                        sheet.autoSizeColumn(4);
                        sheet.autoSizeColumn(5);
                        sheet.autoSizeColumn(6);

                        // }
                    } catch (NullPointerException npe) {
                    }

                }

                int lastRow = empTicketList + 2;
                Row row2 = sheet.createRow(lastRow);

                Cell cel = row2.createCell(0);
                cel.setCellValue("Total Employees");
                cel.setCellStyle(style);

                Cell cel1 = row2.createCell(1);
                cel1.setCellValue(empTicketList);
                cel1.setCellStyle(style);

                workbook.write(bos);
                bos.flush();
                bos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return bos;

        } else {
            List<Ticket> ticket = ticketdaoImpl.getExportTicketData(date,
                    ticketStatus);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                Workbook workbook = new HSSFWorkbook();
                Sheet sheet = workbook.createSheet();
                // int rowMainIndex=1;
                Row row1 = sheet.createRow(0);

                Font font = workbook.createFont();
                font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                font.setFontHeightInPoints((short) 10);
                CellStyle style = workbook.createCellStyle();

                style.setAlignment(CellStyle.ALIGN_CENTER);
                style.setFont(font);

                Cell cell0 = row1.createCell(0);
                cell0.setCellValue("Ticket Number");
                cell0.setCellStyle(style);

                Cell cell1 = row1.createCell(1);
                cell1.setCellValue("Manager Name");
                cell1.setCellStyle(style);

                Cell cell2 = row1.createCell(2);
                cell2.setCellValue("Employee Name");
                cell2.setCellStyle(style);

                Cell cell3 = row1.createCell(3);
                cell3.setCellValue("Ticket Status");
                cell3.setCellStyle(style);

                // Cell cell4 = row1.createCell(4);
                // cell4.setCellValue("Project Allocation(%)");
                // cell4.setCellStyle(style);
                Cell cell4 = row1.createCell(4);
                cell4.setCellValue("Date");
                cell4.setCellStyle(style);

                Cell cell5 = row1.createCell(5);
                cell5.setCellValue("Meal Type");
                cell5.setCellStyle(style);

                Cell cell6 = row1.createCell(6);
                cell6.setCellValue("Signature");
                cell6.setCellStyle(style);

                int rowIndex = 1;
                int empTicketList = 0;

                for (Ticket ticketlist : ticket) {

                    List<TicketHistory> ticketHistory = ticketdaoImpl
                            .getTicketHistorys(ticketlist.getTicketNumber());
                    Employee e = dao.findBy(Employee.class, ticketlist.getAuthorEmpId());

                    try {

                        for (TicketHistory history : ticketHistory) {
                            Row row = sheet.createRow(rowIndex++);
                            Employee employee = dao.findBy(Employee.class,
                                    history.getEmployee().getEmployeeId());
                            Cell cel0 = row.createCell(0);
                            cel0.setCellValue(ticketlist.getTicketNumber());

                            Cell cel1 = row.createCell(1);
                            cel1.setCellValue(e.getFullName());

                            Cell cel2 = row.createCell(2);
                            cel2.setCellValue(employee.getFullName());

                            Cell cel3 = row.createCell(3);
                            cel3.setCellValue(ticketlist.getTicketStatus()
                                    .getTicketStatus());

                            Cell cel4 = row.createCell(4);
                            cel4.setCellValue(ticketlist.getRaisedDate()
                                    .toString());

                            Cell cel5 = row.createCell(5);
                            cel5.setCellValue(ticketlist.getMealLookUp()
                                    .getMealName());
                            empTicketList++;

                        }
                        sheet.autoSizeColumn(0);
                        sheet.autoSizeColumn(1);
                        sheet.autoSizeColumn(2);
                        sheet.autoSizeColumn(3);
                        sheet.autoSizeColumn(4);
                        sheet.autoSizeColumn(5);
                        sheet.autoSizeColumn(6);

                        // }
                    } catch (NullPointerException npe) {
                    }

                }

                int lastRow = empTicketList + 2;
                Row row2 = sheet.createRow(lastRow);

                Cell cel = row2.createCell(0);
                cel.setCellValue("Total Employees");
                cel.setCellStyle(style);

                Cell cel1 = row2.createCell(1);
                cel1.setCellValue(empTicketList);
                cel1.setCellStyle(style);

                workbook.write(bos);
                bos.flush();
                bos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return bos;

        }
    }
   /* @Override
    public List<String> needApproval(TicketDTO ticketDTO) {
		return null; }*/
}
