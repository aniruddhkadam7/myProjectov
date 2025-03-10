package com.raybiztech.ticketmanagement.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.date.Date;
import com.raybiztech.ticketmanagement.DTO.MealLookUpDTO;
import com.raybiztech.ticketmanagement.business.MealLookUp;
import com.raybiztech.ticketmanagement.business.Ticket;
import com.raybiztech.ticketmanagement.business.TicketHistory;
import com.raybiztech.ticketmanagement.business.TicketStatus;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

@Repository("ticketdaoImpl")
public class TicketDAOImpl extends DAOImpl implements TicketDAO {

    Logger logger = Logger.getLogger(TicketDAOImpl.class);

    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    DAO dao;

    @SuppressWarnings("unchecked")
    @Transactional
    @Override
    public List<MealLookUpDTO> getmeallookup() {
        // TODO Auto-generated method stub
        return sessionFactory.getCurrentSession()
                .createCriteria(MealLookUp.class).list();

    }

    @Transactional
    @Override
    public Map<String, Object> getAllTicketsList(String ticketStatus, Date date,String role) {

        Criteria criteria = getSessionFactory().getCurrentSession()
                .createCriteria(Ticket.class);
        if(role.equalsIgnoreCase("Admin Support")){
        	criteria.add(Restrictions.ilike("isApproved", "TRUE"));
        }
        criteria.add(Restrictions.eq("raisedDate", date));
        // criteria.createAlias("ticket", "ticketStatus");
        if (!ticketStatus.equalsIgnoreCase("ALL")) {
            criteria.add(Restrictions.eq("ticketStatus",
                    TicketStatus.valueOf(ticketStatus)));
         //   criteria.add(Restrictions.eq("raisedDate", date));
        }

		
        // criteria.add(Restrictions.eq("raisedDate", date)).list();


        Map<String, Object> ticketMap = new HashMap<String, Object>();
        ticketMap.put("ticketList", criteria.list());
        ticketMap.put("size", criteria.list().size());
        return ticketMap;

		// return criteria.list();
    }

    @Transactional
    @Override
    public List<TicketHistory> getTicketHistorys(Long ticketHistoryId) {
        Criteria criteria = getSessionFactory().getCurrentSession()
                .createCriteria(TicketHistory.class);
        criteria.createAlias("ticket", "ticket");
        criteria.add(Restrictions.eq("ticket.TicketNumber", ticketHistoryId));
        return criteria.list();

    }

    @Override
    public Map<String, Object> getManagerTickets(Long employeeid,
            String ticketStatus, Date date) {
        Employee e = dao.findBy(Employee.class, employeeid);
        Long empId;
       
       
        Criteria criteria = getSessionFactory().getCurrentSession()
                .createCriteria(Ticket.class);
      //  if(e.getRole().equalsIgnoreCase("Manager")){
       	 empId = e.getEmployeeId();
       
        if (!ticketStatus.equalsIgnoreCase("ALL")) {
            criteria.add(Restrictions.eq("ticketStatus",
                    TicketStatus.valueOf(ticketStatus)));
            criteria.add(Restrictions.eq("authorEmpId", empId));
            criteria.add(Restrictions.eq("raisedDate", date));
        }
        criteria.add(Restrictions.eq("authorEmpId", empId)).add(Restrictions.eq("raisedDate", date)).list();
    
       /* else{
        	empId= e.getManager().getEmployeeId();
        	criteria.createAlias("ticketHistory", "ticketHistory").createAlias("ticketHistory.employee", "employee");
        	 criteria.add(Restrictions.eq("authorEmpId", empId)).add(Restrictions.eq("employee.employeeId",employeeid))
        	 .add(Restrictions.eq("raisedDate", date)).list();
       
        }*/

		
         

        Map<String, Object> ticketMap = new HashMap<String, Object>();
        ticketMap.put("ticketList", criteria.list());
        ticketMap.put("size", criteria.list().size());
        return ticketMap;

    }
    @Override
    public Map<String, Object> getIndividualTickets(Long employeeid, String status,Date date) {
    	Criteria criteria=getSessionFactory().getCurrentSession().createCriteria(TicketHistory.class).createAlias("employee", "employee")
    			.add(Restrictions.eq("employee.employeeId", employeeid));
    	List<TicketHistory> histories=criteria.list();
    	Criteria criteria2=getSessionFactory().getCurrentSession().createCriteria(Ticket.class)
    			.add(Restrictions.eq("raisedDate", date));
    	if (!status.equalsIgnoreCase("ALL")) {
        criteria2.add(Restrictions.eq("ticketStatus",
                    TicketStatus.valueOf(status)));
    	}
    	List<Ticket> list=criteria2.list();
    	List<Ticket> tickets=new ArrayList<Ticket>();
    	for(TicketHistory history:histories){
    	     for(Ticket ticket:list){
    		
    		if(history.getTicket().equals(ticket)){
    				tickets.add(ticket);	
    			}
    		}
    		
    	}
    	 Map<String, Object> ticketMap = new HashMap<String, Object>();
         ticketMap.put("ticketList", tickets);
         ticketMap.put("size", tickets.size());
         return ticketMap;
    	
    }

    @Override
    public List<Ticket> getExportTicketData(Date date, String ticketStatus) {
		// TODO Auto-generated method stub

        Criteria criteria = getSessionFactory().getCurrentSession()
                .createCriteria(Ticket.class);
        // criteria.createAlias("ticket", "ticketStatus");
        if (!ticketStatus.equalsIgnoreCase("ALL")) {
            criteria.add(Restrictions.eq("ticketStatus",
                    TicketStatus.valueOf(ticketStatus)));
            criteria.add(Restrictions.eq("raisedDate", date));
        }

        criteria.add(Restrictions.eq("isApproved", "TRUE"));
		//logger.warn("criteria list:" + criteria.list());
        return criteria.add(Restrictions.eq("raisedDate", date)).list();
    }

    @Override
    public List<Ticket> getManagerExportlist(Long empId, Date date,
            String ticketStatus, HttpServletResponse response) {
        Employee e = dao.findBy(Employee.class, empId);
        Long empid = e.getEmployeeId();
        Criteria criteria = getSessionFactory().getCurrentSession()
                .createCriteria(Ticket.class);
        // criteria.createAlias("ticket", "ticketStatus");

        if (!ticketStatus.equalsIgnoreCase("ALL")) {
            criteria.add(Restrictions.eq("ticketStatus",
                    TicketStatus.valueOf(ticketStatus)));
            criteria.add(Restrictions.eq("authorEmpId", empid));
            criteria.add(Restrictions.eq("raisedDate", date));
        }

		//logger.warn("criteria list:" + criteria.list());
        List noOfTickets = criteria.add(Restrictions.eq("authorEmpId", empid)).add(Restrictions.eq("raisedDate", date)).list();

        return noOfTickets;

    }

    @Override
    public Boolean getticketlist(Date date, MealLookUp mealLookup, Employee employee) {

        Criteria cr = getSessionFactory().getCurrentSession().createCriteria(TicketHistory.class);

        cr.createAlias("ticket", "ticket");
        cr.add(Restrictions.eq("ticket.raisedDate", date));
        cr.add(Restrictions.eq("ticket.mealLookUp", mealLookup));
        cr.add(Restrictions.eq("employee", employee));
        cr.add(Restrictions.or(Restrictions.eq("ticket.ticketStatus", TicketStatus.InProcess), Restrictions.eq("ticket.ticketStatus", TicketStatus.Accepted)));
        List<TicketHistory> tickethistory = new ArrayList<TicketHistory>();
        tickethistory = cr.list();
        
        if (cr.list().isEmpty()) {
            //logger.warn("in if indao impl is");
            return true;
        } else {
            // logger.warn("in else");
            return false;
        }

    }
}
