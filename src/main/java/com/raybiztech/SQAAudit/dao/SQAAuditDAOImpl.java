package com.raybiztech.SQAAudit.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.raybiztech.SQAAudit.business.SQAAuditForm;
import com.raybiztech.SQAAudit.business.SQAAuditTimeline;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.date.Date;
import com.raybiztech.projectmanagement.business.Audit;

@Repository("sqaAuditDaoImpl")
public class SQAAuditDAOImpl extends DAOImpl implements SQAAuditDAO{

	@Override
	public Map<String, Object> getProjectAuditList(Long projectId,Integer startIndex, Integer endIndex) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SQAAuditForm.class);
		criteria.createAlias("project", "project");
		criteria.add(Restrictions.eq("project.id", projectId));
		criteria.addOrder(Order.desc("id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		//System.out.println("listSize-1:"+ criteria.list().size());
		Integer recSize = criteria.list().size();
		List<SQAAuditForm> list = criteria.list();
		if(startIndex != null && endIndex != null) {
			//System.out.println("startIndex:"+startIndex+"endIndex:"+endIndex);
			if(endIndex <= recSize-1) {
				//System.out.println("in if");
				list = list.subList(startIndex, endIndex);
			}
			else {
				//System.out.println("in else");
				list = list.subList(startIndex, recSize);
			}
		}
		//System.out.println("listSize-2:"+ list.size());
		Map<String, Object> map = new HashMap<>();
		map.put("list", list);
		map.put("size", recSize);
		return map;
	}

	@Override
	public List<SQAAuditForm> getListOfAuditsOnSelectedDate(Date sqaAuditDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SQAAuditForm.class);
		criteria.add(Restrictions.eq("auditDate", sqaAuditDate));
		criteria.add(Restrictions.eq("auditStatus", "Open"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public Map<String, Object> getSubmittedProjectAuditList(Long projectId, Integer startIndex, Integer endIndex) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SQAAuditForm.class);
		criteria.createAlias("project", "project");
		criteria.add(Restrictions.eq("project.id", projectId));
		criteria.add(Restrictions.ne("formStatus", "Save"));
		criteria.addOrder(Order.desc("id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		Integer recSize = criteria.list().size();
		List<SQAAuditForm> list = criteria.list();
		if(startIndex != null && endIndex != null) {
			//System.out.println("startIndex:"+startIndex+"endIndex:"+endIndex);
			if(endIndex <= recSize-1) {
				//System.out.println("in if");
				list = list.subList(startIndex, endIndex);
			}
			else {
				//System.out.println("in else");
				list = list.subList(startIndex, recSize);
			}
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("list", list);
		map.put("size", recSize);
		return map;
		
	}

	@Override
	public Map<String, Object> getSQAAuditsReports(Integer startIndex, Integer endIndex,String multiSearch,Date fromDate, Date toDate,
			String auditStatus,String auditRescheduleStatus) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SQAAuditForm.class);
		
		criteria.createAlias("projectManager", "projectManager");
		
		if (fromDate != null && toDate != null) {
			criteria.add(Restrictions.between("auditDate", fromDate,
					toDate));
		}
		
		if (!multiSearch.isEmpty()){
			Criterion cir1 = Restrictions.ilike("auditType", multiSearch,MatchMode.ANYWHERE);
			Criterion cir2 = Restrictions.ilike("projectName", multiSearch,MatchMode.ANYWHERE);
			Criterion cir3 = Restrictions.ilike("projectManager.employeeFullName", multiSearch,MatchMode.ANYWHERE);
			Criterion cir4 = Restrictions.or(cir1, cir2);
			criteria.add(Restrictions.or(cir3, cir4));
		}
		
		if (!auditStatus.isEmpty()) {
			criteria.add(Restrictions.eq("auditStatus", auditStatus));
		}
		
		if(!auditRescheduleStatus.isEmpty()){
			
			if(auditRescheduleStatus.equalsIgnoreCase("Yes")){
				criteria.add(Restrictions.eq("auditRescheduleStatus",Boolean.TRUE));
			}else if(auditRescheduleStatus.equalsIgnoreCase("No")){
				//logger.warn("status"+selectionStatus);
				criteria.add(Restrictions.eq("auditRescheduleStatus",Boolean.FALSE));
			}
		}
		
		/*System.out.println("loggedInEmployee"+loggedInemployeeName);
		
		
		if(loggedInemployeeName != null){
		//criteria.add(Restrictions.eq("projectManager",loggedInemployeeName ));
			criteria.add(Restrictions.eq("projectManager", loggedInemployeeName));
		}
		*/

		criteria.addOrder(Order.desc("id"));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		Map<String ,Object> map = new HashMap<String, Object>();
		Map<String, Object> SQAMap = getPaginationList(criteria,
				startIndex, endIndex);
		map.put("size", SQAMap.get("listSize"));
		map.put("list", SQAMap.get("list"));
		return map;
	}

	@Override
	public List<SQAAuditTimeline> getSQAAuditTimelineDetails(Long auditId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SQAAuditTimeline.class);
		criteria.add(Restrictions.eq("auditId", auditId));
		criteria.addOrder(Order.desc("id"));
		return criteria.list();
	}

	@Override
	public Map<String, Object> getSubmittedSQAAuditReport(Integer startIndex,
			Integer endIndex, String multiSearch, Date fromDate, Date toDate,
			String auditStatus, List<Long> managerIds,String auditRescheduleStatus) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SQAAuditForm.class);
		
		criteria.createAlias("projectManager", "projectManager");
		
		criteria.add(Restrictions.ne("formStatus", "Save"));
		
		if(managerIds != null){
			criteria.add(Restrictions.in("projectManager.employeeId",managerIds ));
		}
		
		if (fromDate != null && toDate != null) {
			criteria.add(Restrictions.between("auditDate", fromDate,
					toDate));
		}
		
		if (!multiSearch.isEmpty()){
			Criterion cir1 = Restrictions.ilike("auditType", multiSearch,MatchMode.ANYWHERE);
			Criterion cir2 = Restrictions.ilike("projectName", multiSearch,MatchMode.ANYWHERE);
			Criterion cir3 = Restrictions.ilike("projectManager.employeeFullName", multiSearch,MatchMode.ANYWHERE);
			Criterion cir4 = Restrictions.or(cir1, cir2);
			criteria.add(Restrictions.or(cir3, cir4));
		}
		
		if (!auditStatus.isEmpty()) {
			criteria.add(Restrictions.eq("auditStatus", auditStatus));
		}
		
        if(!auditRescheduleStatus.isEmpty()){
			
			if(auditRescheduleStatus.equalsIgnoreCase("Yes")){
				criteria.add(Restrictions.eq("auditRescheduleStatus",Boolean.TRUE));
			}else if(auditRescheduleStatus.equalsIgnoreCase("No")){
				//logger.warn("status"+selectionStatus);
				criteria.add(Restrictions.eq("auditRescheduleStatus",Boolean.FALSE));
			}
		}
		

		criteria.addOrder(Order.desc("id"));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		Map<String ,Object> map = new HashMap<String, Object>();
		Map<String, Object> SQAMap = getPaginationList(criteria,
				startIndex, endIndex);
		map.put("size", SQAMap.get("listSize"));
		map.put("list", SQAMap.get("list"));
		return map;
		
		
	}

	@Override
	public Map<String, List<Audit>> getAuditTimeLine(Long id) {
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(Audit.class);
		criteria.add(Restrictions.eq("referenceId", id));
		criteria.add(Restrictions.eq("tableName", "SQAAuditForm"));
		criteria.addOrder(Order.asc("modifiedDate"));
		List<Audit> audits = criteria.list();
		System.out.println("Audit value is ="+ audits);
		Map<String, List<Audit>> map = null;
		if (!audits.isEmpty()) {
			System.out.println("In if");
			map = getPairValue(audits);
	}
		return map;
	}
	
	// For Retrieving Audit whose status is OPEN
	@Override
	public List<SQAAuditForm> getAuditWhoseAuditStatusIsOpen() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SQAAuditForm.class);
				criteria.add(Restrictions.eq("auditStatus","Open"));
				criteria.add(Restrictions.isNotNull("followUpDate"));

		return criteria.list();
	}


}
