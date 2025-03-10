package com.raybiztech.recruitment.dao;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.raybiztech.appraisalmanagement.business.Designation;
import com.raybiztech.appraisals.PIPManagement.business.PIP;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.Status;
import com.raybiztech.appraisals.business.TimeSlot;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.assetmanagement.business.VendorDetails;
import com.raybiztech.date.Date;
import com.raybiztech.date.DayOfMonth;
import com.raybiztech.date.Duration;
import com.raybiztech.date.HourOfDay;
import com.raybiztech.date.MinuteOfHour;
import com.raybiztech.date.MonthOfYear;
import com.raybiztech.date.Second;
import com.raybiztech.date.SecondOfMinute;
import com.raybiztech.date.TimeUnit;
import com.raybiztech.date.YearOfEra;
import com.raybiztech.lookup.business.SkillLookUp;
import com.raybiztech.lookup.business.VacancyLookUp;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;
import com.raybiztech.projectmanagement.invoice.utility.HibernateSupressWaringsUtil;
import com.raybiztech.recruitment.business.BloodGroup;
import com.raybiztech.recruitment.business.Candidate;
import com.raybiztech.recruitment.business.CandidateInterviewCycle;
import com.raybiztech.recruitment.business.Companies;
import com.raybiztech.recruitment.business.EmploymentType;
import com.raybiztech.recruitment.business.Gender;
import com.raybiztech.recruitment.business.JobType;
import com.raybiztech.recruitment.business.JobVacancy;
import com.raybiztech.recruitment.business.MaritalStatus;
import com.raybiztech.recruitment.business.NewJoinee;
import com.raybiztech.recruitment.business.Panel;
import com.raybiztech.recruitment.business.Person;
import com.raybiztech.recruitment.business.QualificationCategory;
import com.raybiztech.recruitment.business.Relations;
import com.raybiztech.recruitment.business.Schedule;
import com.raybiztech.recruitment.dto.CompanyDTO;
import com.raybiztech.recruitment.dto.SearchQueryParams;
import com.raybiztech.recruitment.utils.DateParser;

@Repository("jobPortalDAOImpl")
public class JobPortalDAOImpl extends DAOImpl implements JobPortalDAO {

	Logger logger = Logger.getLogger(JobPortalDAOImpl.class);

	@Override
	public <T extends Serializable> T findByMobileNumber(Class<T> clazz, Serializable name) {
		// TODO Auto-generated method stub
		return (T) sessionFactory.getCurrentSession().createCriteria(clazz).add(Restrictions.eq("mobile", name))
				.uniqueResult();
	}

	@Override
	public <T extends Serializable> T findByEmailName(Class<T> clazz, Serializable name) {
		return (T) sessionFactory.getCurrentSession().createCriteria(clazz).add(Restrictions.eq("email", name))
				.uniqueResult();
	}

	@Override
	public List<Employee> getAllEmployeeData() {
		Criteria createCriteria = sessionFactory.getCurrentSession().createCriteria(Employee.class);
		return createCriteria.list();
	}

	private String checkForNull(String value) {
		String newValue = value;
		if (newValue == null || newValue.equalsIgnoreCase("")) {
			newValue = "";
		} else {
			newValue = "%" + newValue + "%";
		}
		return newValue;
	}

	private String checkForDateNull(String value) {
		String newValue = value;
		if (newValue == null || newValue.equalsIgnoreCase("")) {
			newValue = "";
		} else {
			newValue = value;
		}
		return newValue;
	}

	public String convertStringDatetoMysqlDate(String start) {
		// String start = "01/02/2014";
		SimpleDateFormat startFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		try {
			date = startFormat.parse(start);
		} catch (ParseException ex) {
			java.util.logging.Logger.getLogger(JobPortalDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		String mysqlString = toFormat.format(date);
		return mysqlString;
	}

	/*
	 * @Override public List<Schedule> searchCadidates( String fromDate, String
	 * toDate,String skill) { // String skil = checkForNull(skill); String fromdate
	 * = checkForDateNull(fromDate); String todate = checkForDateNull(toDate);
	 * 
	 * String whereCondition = "";
	 * 
	 * if (!fromDate.equalsIgnoreCase("") && !toDate.equalsIgnoreCase("")) {
	 * 
	 * whereCondition += " and si.scheduleDate >= '" +
	 * convertStringDatetoMysqlDate(fromdate) + "'"; whereCondition +=
	 * " and si.scheduleDate <= '" + convertStringDatetoMysqlDate(todate) + "'";
	 * 
	 * } else { if (!fromDate.equalsIgnoreCase("")) { whereCondition +=
	 * " and si.scheduleDate >= '" + convertStringDatetoMysqlDate(fromdate) + "'"; }
	 * if (!toDate.equalsIgnoreCase("")) { whereCondition +=
	 * " and si.scheduleDate <= '" + convertStringDatetoMysqlDate(todate) + "'"; } }
	 * String hql = " select si from Schedule si";
	 * 
	 * if (!whereCondition.equalsIgnoreCase("")) { hql += " where 1=1" +
	 * whereCondition; }
	 * 
	 * Query query = getSessionFactory().getCurrentSession().createQuery(hql);
	 * System.out.println("query :" + query); List<Schedule> candidates =
	 * query.list(); return candidates; }
	 */

	@Override
	public List<SkillLookUp> getAllSkillsLookUp() {
		Criteria createCriteria = sessionFactory.getCurrentSession().createCriteria(SkillLookUp.class);
		return createCriteria.list();
	}

	@Override
	public List<Candidate> getAllUpcomingJoineeList() {
		String todayDate = new Date().toString("dd/MM/yyyy");
		String whereCondition = "";
		String result = "selected";
		whereCondition += " and c.candidateInterviewStatus = '" + result + "'";
		whereCondition += " and c.candidateJoinDate > '" + convertStringDatetoMysqlDate(todayDate) + "'";
		String hql = " select c from Candidate c";
		if (!whereCondition.equalsIgnoreCase("")) {
			hql += " where 1=1" + whereCondition;
		}
		Query query = getSessionFactory().getCurrentSession().createQuery(hql);
		List<Candidate> candidatesList = query.list();
		return candidatesList;

	}

	@Override
	public Boolean isCandidateMailExists(String candidateEmail) {
		Boolean flag = true;
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Candidate.class)
				.add(Restrictions.eq("email", candidateEmail).ignoreCase());
		if (criteria.list().isEmpty()) {
			flag = false;
		}
		return flag;
	}

	@Override
	public Boolean isCandidateMobileNumberExists(String candidateMobileNumber) {
		Boolean flag = true;
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Candidate.class)
				.add(Restrictions.eq("mobile", candidateMobileNumber).ignoreCase());
		if (criteria.list().isEmpty()) {
			flag = false;
		}
		return flag;
	}

	@Override
	public Boolean isEditCandidateMailExists(String candidateId, String mailId) {
		Boolean flag = true;
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Candidate.class)
				.add(Restrictions.ne("personId", Long.parseLong(candidateId)))
				.add(Restrictions.eq("email", mailId).ignoreCase());
		if (criteria.list().isEmpty()) {
			flag = false;
		}
		return flag;
	}

	@Override
	public Boolean isEditCandidateMobileNumberExists(String candidateId, String mobileNum) {
		Boolean flag = true;
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Candidate.class)
				.add(Restrictions.ne("personId", Long.parseLong(candidateId)))
				.add(Restrictions.eq("mobile", mobileNum).ignoreCase());
		if (criteria.list().isEmpty()) {
			flag = false;
		}
		return flag;
	}

	@Override
	public Map<String, Object> getProfilePaginationEmployeesData(String selectionStatus, Integer startIndex,
			Integer endIndex, String searchStr) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Employee.class);
		criteria.add(Restrictions.ne("employeeId", 1000L));

		if ("Active".equalsIgnoreCase(selectionStatus) || "InActive".equalsIgnoreCase(selectionStatus)) {
			criteria.add(Restrictions.eq("statusName", selectionStatus));
			if (!searchStr.isEmpty()) {
				criteria.add(Restrictions.ilike("employeeFullName", searchStr, MatchMode.ANYWHERE));
			}
			
		

		} else if ("underNotice".equalsIgnoreCase(selectionStatus)) {
			criteria.add(Restrictions.eq("statusName", "Active"));
			criteria.add(Restrictions.eq("underNotice", Boolean.TRUE));

			if (!searchStr.isEmpty()) {
				criteria.add(Restrictions.ilike("employeeFullName", searchStr, MatchMode.ANYWHERE));
			}
			criteria.addOrder(Order.desc("releavingDate"));

		}
		else if("Contract".equalsIgnoreCase(selectionStatus)){
		   criteria.add(Restrictions.eq("contractExists", Boolean.TRUE));
		   criteria.add(Restrictions.eq("statusName", "Active"));
		   if (!searchStr.isEmpty()) {
				criteria.add(Restrictions.ilike("employeeFullName", searchStr, MatchMode.ANYWHERE));
			}
		}

		/*
		 * else if ("All".equalsIgnoreCase(selectionStatus)) { if (!searchStr.isEmpty())
		 * { criteria.add(Restrictions.ilike("employeeFullName", searchStr,
		 * MatchMode.ANYWHERE)); }
		 * 
		 * }
		 */
		criteria.add(Restrictions.ne("employmentTypeName", "On Contract Hire"));
		criteria.add(Restrictions.ne("employmentTypeName", "On Contract"));
		criteria.addOrder(Order.asc("employeeId"));

		/*
		 * Integer noOfEmplyoees = criteria.list().size();
		 * 
		 * if (startIndex != null && endIndex != null) {
		 * criteria.setFirstResult(startIndex); criteria.setMaxResults(endIndex -
		 * startIndex); }
		 * 
		 * Map<String, Object> employeeMap = new HashMap<String, Object>();
		 * employeeMap.put("employeeList", criteria.list()); employeeMap.put("size",
		 * noOfEmplyoees);
		 */

		/* pagination */

		Map<String, Object> paginationMap = getPaginationList(criteria, startIndex, endIndex);

		Map<String, Object> employeeMap = new HashMap<String, Object>();
		employeeMap.put("employeeList", paginationMap.get("list"));
		System.out.println("s:" + paginationMap.get("listSize") );
		employeeMap.put("size", paginationMap.get("listSize"));

		return employeeMap;

	}

	@Override
	public List<Panel> panelDepartmentEmployeeData(String departmentId) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Panel.class)
				.add(Restrictions.eq("deptId", departmentId));
		return criteria.list();
	}

	@Override
	public List<Employee> getActiveEmployeeData() {
		Criteria criteria = getSessionFactory()
				.getCurrentSession().createCriteria(Employee.class).add(Restrictions
						.or(Restrictions.eq("statusName", "Active"), Restrictions.eq("statusName", "underNotice")))
				.add(Restrictions.ne("employeeId", 1000L));
		return criteria.list();
	}

	@Override
	public CandidateInterviewCycle reSheduleInterviewForCandidate(String candidateId) {
		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(CandidateInterviewCycle.class)
				.setProjection(Projections.max("interviewCycleId"));
		criteria1.createAlias("candidate", "candidate");
		criteria1.setFetchMode("candidate", FetchMode.JOIN);
		criteria1.add(Restrictions.eq("candidate.personId", Long.parseLong(candidateId)));

		Long cycleId = (Long) criteria1.uniqueResult();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(CandidateInterviewCycle.class);
		criteria.add(Restrictions.eq("interviewCycleId", cycleId));
		return (CandidateInterviewCycle) criteria.uniqueResult();
	}

	@Override
	public Schedule getCadidateSchedule(Long interviewId) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Schedule.class);
		criteria.createAlias("interview", "interview");
		criteria.setFetchMode("interview", FetchMode.JOIN);
		criteria.add(Restrictions.eq("interview.InterviewId", interviewId));
		return (Schedule) criteria.uniqueResult();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CandidateInterviewCycle> getCandidateTimeLineDetails(String candidateId) {
		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(CandidateInterviewCycle.class);
		criteria1.createAlias("candidate", "candidate");
		criteria1.setFetchMode("candidate", FetchMode.JOIN);
		criteria1.add(Restrictions.eq("candidate.personId", Long.parseLong(candidateId)));
		// criteria1.add(Restrictions.eq("status", "finished"));
		criteria1.addOrder(Order.desc("interviewRound"));
		return criteria1.list();
	}

	@Override
	public Map<String, Object> getScheduledCandidatesForEmployee(Long employeeId, Integer startIndex,
			Integer endIndex) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(CandidateInterviewCycle.class);
		criteria.createCriteria("employeeList", "employeeList");
		criteria.add(Restrictions.eq("employeeList.employeeId", employeeId));
		criteria.add(Restrictions.eq("status", "pending"));

		Integer noOfRecords = criteria.list().size();
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(endIndex - startIndex);
		List<CandidateInterviewCycle> cics = criteria.list();
		Map<String, Object> cicsMap = new HashMap<String, Object>();
		cicsMap.put("list", cics);
		cicsMap.put("size", noOfRecords);

		return cicsMap;
	}

	@Override
	public Map<String, Object> getSearchScheduledCandidatesForEmployee(Long employeeId, String status, Date fromDate,
			Date toDate, Integer startIndex, Integer endIndex) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(CandidateInterviewCycle.class);
		criteria.createCriteria("employeeList", "employeeList");
		criteria.add(Restrictions.eq("employeeList.employeeId", employeeId)).addOrder(Order.desc("interviewCycleId"));
		if (!"".equals(status)) {
			criteria.add(Restrictions.eq("status", status));
		}
		if (fromDate != null) {
			criteria.add(Restrictions.ge("interviewDate", fromDate));
		}
		if (toDate != null) {
			criteria.add(Restrictions.le("interviewDate", toDate));
		}
        criteria.createAlias("candidate", "candidate");
        criteria.add(Restrictions.or(Restrictions.eq("candidate.candidateInterviewStatus",Status.IN_PROCESS), Restrictions.eq("candidate.candidateInterviewStatus", Status.RESCHEDULED)));

		Integer noOfRecords = criteria.list().size();
		List<CandidateInterviewCycle> cics = criteria.list();

		/*
		 * if (cics != null) {
		 * 
		 * Collections.sort(cics, new Comparator<CandidateInterviewCycle>() {
		 * 
		 * @Override public int compare(CandidateInterviewCycle c1,
		 * CandidateInterviewCycle c2) { int k = 0; if
		 * (c1.getInterviewDate().isAfter(c2.getInterviewDate())) { k = -1; } if
		 * (c1.getInterviewDate().isBefore(c2.getInterviewDate())) { k = 1; } return k;
		 * 
		 * } }); }
		 */

		Map<String, Object> cicsMap = new HashMap<String, Object>();
		cicsMap.put("list", cics);
		cicsMap.put("size", noOfRecords);

		return cicsMap;
	}

	@Override
	public CandidateInterviewCycle getEmpScheduleInterviewDetailsByID(Long interviewCycleId) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(CandidateInterviewCycle.class);
		criteria.add(Restrictions.eq("interviewCycleId", interviewCycleId));
		return (CandidateInterviewCycle) criteria.uniqueResult();
	}

	@Override
	public List<CandidateInterviewCycle> viewScheduleCompletedCandidates(String candidateId) {
		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(CandidateInterviewCycle.class);
		criteria1.createAlias("candidate", "candidate");
		criteria1.setFetchMode("candidate", FetchMode.JOIN);
		criteria1.add(Restrictions.eq("candidate.personId", Long.parseLong(candidateId)));
		return criteria1.list();
	}

	@Override
	public Map<String, Object> getAllnonScheduledCadidates(Integer startIndex, Integer endIndex) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Candidate.class);
		criteria.add(Restrictions.eq("status", "1"));
		criteria.addOrder(Order.desc("personId"));
		Integer noOfRecotds = criteria.list().size();
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(endIndex - startIndex);
		List<Candidate> candidates = criteria.list();
		Map<String, Object> allCandidatesMap = new HashMap<String, Object>();
		allCandidatesMap.put("list", candidates);
		allCandidatesMap.put("size", noOfRecotds);
		return allCandidatesMap;
	}

	@Override
	public Map<String, Object> getScheduledCadidatesBySearch(Integer startIndex, Integer endIndex, String searchStr,String country) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Candidate.class);
		criteria.addOrder(Order.desc("personId"));
		criteria.createAlias("appliedFor", "jobvacancy");
		if(searchStr.equalsIgnoreCase("")){
		criteria.createAlias("country", "country");
		criteria.add(Restrictions.eq("country.name", country));
		}
		Criterion byIndex = Restrictions.eq("status", "1");
		Criterion bySkillOrExprience = Restrictions.or(Restrictions.ilike("experience", searchStr, MatchMode.ANYWHERE),
				Restrictions.ilike("skill", searchStr, MatchMode.ANYWHERE));
		Criterion byNameOrEmail = Restrictions.or(Restrictions.ilike("fullName", searchStr, MatchMode.ANYWHERE),
				Restrictions.ilike("email", searchStr, MatchMode.ANYWHERE));
		Criterion byMobile = Restrictions.or(Restrictions.ilike("mobile", searchStr, MatchMode.ANYWHERE),
				Restrictions.ilike("phone", searchStr, MatchMode.ANYWHERE));
		Criterion byAppliedforOrJobcode = Restrictions.or(
				Restrictions.ilike("jobvacancy.jobCode", searchStr, MatchMode.ANYWHERE),
				Restrictions.ilike("jobvacancy.positionVacant", searchStr, MatchMode.ANYWHERE));
		Criterion result = Restrictions.or(byMobile, byNameOrEmail);
		Criterion result1 = Restrictions.or(bySkillOrExprience, byAppliedforOrJobcode);
		Criterion common = Restrictions.or(result, result1);

		Criterion finalList = Restrictions.and(common, byIndex);
		criteria.add(finalList);
		Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);

		/*
		 * Integer noOfRecords = criteria.list().size();
		 * 
		 * criteria.setFirstResult(startIndex); criteria.setMaxResults(endIndex -
		 * startIndex); List<Candidate> candidates = criteria.list();
		 */
		System.out.println("size:" + criteria.list().size());
		Map<String, Object> allCandidatesMap = new HashMap<String, Object>();
		allCandidatesMap.put("list", criteriaMap.get("list"));
		allCandidatesMap.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));
		return allCandidatesMap;
	}

	@Override
	public Boolean isJobCodeExist(String jobCode) {
		List<VacancyLookUp> vacancyLookUpList = getSessionFactory().getCurrentSession().createCriteria(JobVacancy.class)
				.add(Restrictions.eq("jobCode", jobCode)).list();
		return vacancyLookUpList.isEmpty();
	}

	@Override
	public List<Employee> getSearchEmployeeData(String searchStr) {

		Query query = null;

		String active = "active";
		query = sessionFactory.getCurrentSession().createQuery(
				"from Employee emp where concat(emp.firstName, ' ', emp.lastName) like :fullName and emp.statusName=:active");
		query.setString("fullName", "%" + searchStr + "%");
		query.setString("active", active);
		// query.setMaxResults(20);
		return query.list();
	}

	/*
	 * @Override public Map<String, Object> searchCandidates(String skill, String
	 * fromDate, String toDate, Integer startIndex, Integer endIndex) {
	 * 
	 * logger.info("^^^^^^ candidates  DAO Impl : skill: " + skill + " fromDate: " +
	 * fromDate + " to date: " + toDate + "startIndex: " + startIndex +
	 * " end Index: " + endIndex); Criteria criteria =
	 * sessionFactory.getCurrentSession().createCriteria( Schedule.class);
	 * criteria.createAlias("interview", "interview");
	 * criteria.setFetchMode("interview", FetchMode.JOIN);
	 * criteria.createAlias("interview.candidates", "candidates");
	 * criteria.setFetchMode("candidates", FetchMode.JOIN);
	 * 
	 * if (!fromDate.equalsIgnoreCase("") && !toDate.equalsIgnoreCase("")) { try {
	 * criteria.add(Restrictions.ge("scheduleDate", DateParser.toDate(fromDate)));
	 * criteria.add(Restrictions.le("scheduleDate", DateParser.toDate(toDate))); }
	 * catch (ParseException ex) { java.util.logging.Logger.getLogger(
	 * JobPortalDAOImpl.class.getName()).log(Level.SEVERE, null, ex); }
	 * 
	 * } else { if (!fromDate.equalsIgnoreCase("")) { try {
	 * criteria.add(Restrictions.ge("scheduleDate", DateParser.toDate(fromDate))); }
	 * catch (ParseException ex) { java.util.logging.Logger.getLogger(
	 * JobPortalDAOImpl.class.getName()).log(Level.SEVERE, null, ex); } } if
	 * (!toDate.equalsIgnoreCase("")) { try {
	 * criteria.add(Restrictions.le("scheduleDate", DateParser.toDate(toDate))); }
	 * catch (ParseException ex) { java.util.logging.Logger.getLogger(
	 * JobPortalDAOImpl.class.getName()).log(Level.SEVERE, null, ex); } } }
	 * 
	 * // criteria.add(Restrictions.ne("interview.status", Status.COMPLETED));
	 * criteria.add(Restrictions.ilike("candidates.skill", skill,
	 * MatchMode.ANYWHERE)); Integer noOfRecords = criteria.list().size();
	 * logger.info("^^^^^^ Number of records DAO: @@ " + noOfRecords);
	 * criteria.setFirstResult(startIndex); criteria.setMaxResults(endIndex -
	 * startIndex); List<Schedule> candidates = criteria.list();
	 * logger.info("^^^^^^ candidates sizeee DAO: @@ " + candidates.size());
	 * Map<String, Object> srchScheduleCandMap = new HashMap<String, Object>();
	 * srchScheduleCandMap.put("list1", candidates);
	 * srchScheduleCandMap.put("size1", noOfRecords); return srchScheduleCandMap; }
	 */
	@Override
	public Map<String, Object> searchCandidates(String skill, Date fromDate, Date toDate, Integer startIndex,
			Integer endIndex) {

		String fDate = "";
		String tDate = "";
		if (fromDate != null) {
			fDate = " and c1.interviewDate >= '" + fromDate.toString("yyyy-MM-dd") + "' ";
		}
		if (toDate != null) {
			tDate = " and c1.interviewDate <= '" + toDate.toString("yyyy-MM-dd") + "' ";
		}
		String queryString = "SELECT c1 " + "  FROM CandidateInterviewCycle AS c1"
				+ "  WHERE c1.candidate.technology like :Skill" + fDate + tDate
				+ " and (c1.interviewRound, c1.candidate.personId) IN ("
				+ "    SELECT MAX(c2.interviewRound), c2.candidate.personId"
				+ "      FROM CandidateInterviewCycle AS c2 " + "      GROUP BY c2.candidate.personId"
				+ "  ) ORDER BY c1.interviewDate DESC";

		// String queryString = "SELECT c1 FROM CandidateInterviewCycle AS c1"
		// + " WHERE c1.candidate.technology like :Skill" + fDate +
		// tDate+" ORDER BY c1.interviewDate DESC";

		Query criteria = getSessionFactory().getCurrentSession().createQuery(queryString);
		criteria.setParameter("Skill", "%" + skill + "%");

		// }
		// DetachedCriteria maxDateQuery =
		// DetachedCriteria.forClass(CandidateInterviewCycle.class);
		// maxDateQuery.createAlias("candidate", "candidate");
		// ProjectionList proj = Projections.projectionList();
		// proj.add(Projections.groupProperty("candidate.personId"));
		// proj.add(Projections.max("interviewRound"));
		// maxDateQuery.setProjection(proj);
		//
		// Criteria criteria =
		// getSessionFactory().getCurrentSession().createCriteria(CandidateInterviewCycle.class);
		// criteria.createAlias("candidate", "candidate");
		// criteria.add(Subqueries.propertiesEq(new String[]{"interviewRound",
		// "candidate.personId"}, maxDateQuery));
		//
		Integer noOfRecords = criteria.list().size();
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(endIndex - startIndex);
		List<CandidateInterviewCycle> cics = criteria.list();
		Map<String, Object> cicsMap = new HashMap<String, Object>();
		cicsMap.put("list", cics);
		cicsMap.put("size", noOfRecords);
		return cicsMap;
	}

	@Override
	public CandidateInterviewCycle getScheduledInterviewDataById(Long cycleId) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(CandidateInterviewCycle.class);
		criteria.add(Restrictions.eq("interviewCycleId", cycleId));
		return (CandidateInterviewCycle) criteria.uniqueResult();
	}

	@Override
	public NewJoinee getJoineeByCandidateId(Long candidateId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(NewJoinee.class);
		criteria.add(Restrictions.eq("candidateId", candidateId));
		return (NewJoinee) criteria.uniqueResult();

	}

	@Override
	public List<CandidateInterviewCycle> pendingInterviewList(String candidateId) {
		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(CandidateInterviewCycle.class);
		criteria1.createAlias("candidate", "candidate");
		criteria1.setFetchMode("candidate", FetchMode.JOIN);
		criteria1.add(Restrictions.eq("candidate.personId", Long.parseLong(candidateId)));
		criteria1.add(Restrictions.eq("status", "pending"));
		return criteria1.list();
	}

	@Override
	public Map<String, Object> interviewStatusReport(SearchQueryParams queryParams) {
		
		Criterion nameCriterion = Restrictions.ilike("candidate.fullName", queryParams.getSearchByCandidateName(),
				MatchMode.ANYWHERE);

		Criterion phoneCriterion = Restrictions.ilike("candidate.mobile", queryParams.getSearchByCandidateName(),
				MatchMode.ANYWHERE);

		Criterion mailCriterion = Restrictions.ilike("candidate.email", queryParams.getSearchByCandidateName(),
				MatchMode.ANYWHERE);

		Criterion techCriterion = Restrictions.ilike("jobvacancy.positionVacant",
				queryParams.getSearchByCandidateName(), MatchMode.ANYWHERE);
		 Criterion experienceCriterion;
         if(queryParams.getSearchByCandidateName().contains("-")) {
        	 
        	 String[] exp = queryParams.getSearchByCandidateName().split("-", 0);
        
        	 experienceCriterion = Restrictions.and(Restrictions.ge("candidate.experience", exp[0]),
        			                 Restrictions.le("candidate.experience", exp[1]));
         }else {
		 experienceCriterion =Restrictions.and(Restrictions.ge("candidate.experience",queryParams.getSearchByCandidateName())
				                    ,Restrictions.le("candidate.experience", queryParams.getSearchByCandidateName()+1));
		 
         }
	
		Criterion nameAndPhoneCriterion = Restrictions.or(nameCriterion, phoneCriterion);

		Criterion mailAndTechCriterion = Restrictions.or(techCriterion, mailCriterion);
		Criterion recruiterCriterion = Restrictions.ilike("candidate.recruiter", queryParams.getSearchByCandidateName(),
				MatchMode.ANYWHERE);
		Criterion sourceCriterion = Restrictions.ilike("source.sourceName", queryParams.getSearchByCandidateName(),
				MatchMode.ANYWHERE);

		List<CandidateInterviewCycle> interviewCyclesList = null;
		Map<String, Object> allInterviewReportMap = new HashMap<String, Object>();
		Date fromDate = null;
		Date toDate = null;
		try {
			fromDate = DateParser.toDate(queryParams.getFromDate());
			toDate = DateParser.toDate(queryParams.getToDate());
		} catch (ParseException ex) {
			java.util.logging.Logger.getLogger(JobPortalDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
		}

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(CandidateInterviewCycle.class);
		criteria.createAlias("candidate", "candidate");
		criteria.createAlias("candidate.appliedFor", "jobvacancy");
		criteria.createAlias("candidate.sourcelookUp", "source");

		if (queryParams.getSelectionStatus().equals("Today")) {
			criteria.add(Restrictions.eq("interviewDate", new Date()));
		} else if (queryParams.getSelectionStatus().equals("Yesterday")) {
			criteria.add(Restrictions.eq("interviewDate", new Date().previous()));
		} else if (queryParams.getSelectionStatus().equals("Custom")) {
			criteria.add(Restrictions.between("interviewDate", fromDate, toDate));
		} else if (queryParams.getSelectionStatus().equalsIgnoreCase("This week")) {
			Date weekStart = getFirstDayOfWeek();
			Date weekend = weekStart.shift(new Duration(com.raybiztech.date.TimeUnit.DAY, +6));

			criteria.add(Restrictions.between("interviewDate", weekStart, weekend));

		} else if (queryParams.getSelectionStatus().equalsIgnoreCase("Last week")) {
			Date currentWeekStart = getFirstDayOfWeek();
			Date lastWeekStart = currentWeekStart.shift(new Duration(com.raybiztech.date.TimeUnit.DAY, -7));
			Date lastWeekEnd = lastWeekStart.shift(new Duration(com.raybiztech.date.TimeUnit.DAY, 6));

			criteria.add(Restrictions.between("interviewDate", lastWeekStart, lastWeekEnd));
		} else if (queryParams.getSelectionStatus().equalsIgnoreCase("Last month")) {

			Date date = new Date(DayOfMonth.valueOf(1), MonthOfYear.valueOf(new Date().getMonthOfYear().getValue()),
					YearOfEra.valueOf(new Date().getYearOfEra().getValue()));

			Date lastMonthLastDate = date.shift(new Duration(com.raybiztech.date.TimeUnit.DAY, -1));

			Date lastMonthFirstDate = new Date(DayOfMonth.valueOf(1),
					MonthOfYear.valueOf(lastMonthLastDate.getMonthOfYear().getValue()),
					YearOfEra.valueOf(lastMonthLastDate.getYearOfEra().getValue()));

			criteria.add(Restrictions.between("interviewDate", lastMonthFirstDate, lastMonthLastDate));

		} else if (queryParams.getSelectionStatus().equalsIgnoreCase("Current Month")) {

			Date firstDate = new Date(DayOfMonth.valueOf(1),
					MonthOfYear.valueOf(new Date().getMonthOfYear().getValue()),
					YearOfEra.valueOf(new Date().getYearOfEra().getValue()));

			Date lastDate = new Date(DayOfMonth.valueOf(31),
					MonthOfYear.valueOf(new Date().getMonthOfYear().getValue()),
					YearOfEra.valueOf(new Date().getYearOfEra().getValue()));

			criteria.add(Restrictions.between("interviewDate", firstDate, lastDate));

		}
		
		
           if(!queryParams.getCandidateStatus().equalsIgnoreCase("All")) {
		Status status = Status.valueOf(queryParams.getCandidateStatus());
           
		if (status != null) 
			criteria.add(Restrictions.eq("candidate.candidateInterviewStatus", status));
		}
           
		if (!queryParams.getSelectionTechnology().isEmpty() && queryParams.getSelectionTechnology() != null) {
			criteria.add(
					Restrictions.or(Restrictions.ilike("candidate.technology", queryParams.getSelectionTechnology()),
							Restrictions.ilike("jobvacancy.positionVacant", queryParams.getSelectionTechnology())));
		}
		if(queryParams.getSelectionCountry()!=null){
			criteria.add(Restrictions.eq("candidate.country.id", queryParams.getSelectionCountry()));
		}
		//
		
		if (queryParams.getSearchByMultipleFlag().equals("true")
				&& queryParams.getSearchByRecruiterName().equals("false")
				&& queryParams.getSearchBySourceName().equals("false")
				 && queryParams.getSearchByExperience().equals("false")) 
		{
			criteria.add(Restrictions.or(experienceCriterion,
					Restrictions.or(nameAndPhoneCriterion, mailAndTechCriterion)));
		}
		 if (queryParams.getSearchByMultipleFlag().equals("false")
				&& queryParams.getSearchByRecruiterName().equals("true")
				&& queryParams.getSearchBySourceName().equals("false")
				&& queryParams.getSearchByExperience().equals("false"))
		 {
			criteria.add(recruiterCriterion);
		 } 
		  if (queryParams.getSearchByMultipleFlag().equals("false")
				&& queryParams.getSearchByRecruiterName().equals("false")
				&& queryParams.getSearchBySourceName().equals("true")
				&& queryParams.getSearchByExperience().equals("false")) 
		  {
			criteria.add(sourceCriterion);
		 }
		  if(queryParams.getSearchByExperience()!=null) {
		  if(queryParams.getSearchByMultipleFlag().equals("false")
				&& queryParams.getSearchByRecruiterName().equals("false")
				&& queryParams.getSearchBySourceName().equals("false")&&
				queryParams.getSearchByExperience().equals("true"))
		  {
			criteria.add(experienceCriterion);
		  }
		  }
		  if (queryParams.getSearchByMultipleFlag().equals("true")
				&& queryParams.getSearchByRecruiterName().equals("true")
				&& queryParams.getSearchBySourceName().equals("true")
				&& queryParams.getSearchByExperience().equals("true"))
		  {
			// PREVIOUS CRITERIA
			/*
			 * criteria.add(Restrictions.or( Restrictions.or(experienceCriterion,
			 * Restrictions.or(nameAndPhoneCriterion, mailAndTechCriterion)),
			 * recruiterCriterion));
			 */
			criteria.add(Restrictions.or(
					Restrictions.or(Restrictions.or(experienceCriterion,
							Restrictions.or(nameAndPhoneCriterion, mailAndTechCriterion)), recruiterCriterion),
					sourceCriterion));
		}
		
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);

		Integer noOfRecords = criteria.list().size();
		criteria.addOrder(Order.asc("interviewDate"));
		// criteria.setFirstResult(queryParams.getStartIndex());
		// criteria.setMaxResults(queryParams.getEndIndex()
		// - queryParams.getStartIndex());
		interviewCyclesList = criteria.list();
		allInterviewReportMap.put("list", interviewCyclesList);
		allInterviewReportMap.put("size", noOfRecords);
		return allInterviewReportMap;
	}

	public Map<String, Object> getCountryWiseCandidatesList(SearchQueryParams queryParams) {
		System.out.println("sta:" +queryParams.getCandidateStatus() + "con:" + queryParams.getSelectionCountry() + "tech:" + queryParams.getSelectionTechnology());
		//List<Candidate> list = null;

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Candidate.class);
		criteria.createAlias("appliedFor", "jobvacancy");
		criteria.createAlias("country", "country");

		   if(queryParams.getCandidateStatus()!=null && !queryParams.getCandidateStatus().isEmpty()){
           if(!queryParams.getCandidateStatus().equalsIgnoreCase("All")) {
        	   System.out.println("in if");
		       Status status = Status.valueOf(queryParams.getCandidateStatus());
           
		    //if (queryParams.getCandidateStatus() != null) 
			criteria.add(Restrictions.eq("candidateInterviewStatus", status));
		}
           }
           
		if (queryParams.getSelectionTechnology()!= null  && !queryParams.getSelectionTechnology().isEmpty()) {
			System.out.println("in tech");
			criteria.add(
					Restrictions.or(Restrictions.ilike("technology", queryParams.getSelectionTechnology()),
							Restrictions.ilike("jobvacancy.positionVacant", queryParams.getSelectionTechnology())));
		}
		if(queryParams.getSelectionCountry()!=null){
			System.out.println("in country");
			criteria.add(Restrictions.eq("country.id",queryParams.getSelectionCountry()));
		}
		criteria.addOrder(Order.desc("personId"));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		Map<String, Object> candidateReportMap = getPaginationList(criteria, queryParams.getStartIndex(),queryParams.getEndIndex());

	  System.out.println("index:" +  queryParams.getStartIndex() + queryParams.getEndIndex());
		Map<String, Object> allCandidatesMap = new HashMap<String, Object>();
	
		// list = criteria.list();
		allCandidatesMap.put("list", candidateReportMap.get("list"));
		System.out.println(candidateReportMap.get("list"));
		allCandidatesMap.put("size", Integer.parseInt((candidateReportMap.get("listSize").toString())));
		System.out.println(Integer.parseInt((candidateReportMap.get("listSize").toString())));
		return allCandidatesMap;
	}
	Date getFirstDayOfWeek() {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date.getJavaDate());
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
		c.add(Calendar.DAY_OF_MONTH, -dayOfWeek);

		return new Date(c.getTimeInMillis());
	}

	@Override
	public Boolean isCandidateMappedWithJob(String jobCode) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Candidate.class);
		criteria.createAlias("appliedFor", "appliedFor");

		criteria.add(Restrictions.eq("appliedFor.jobCode", jobCode));
		return criteria.list().isEmpty();

	}

	@Override
	public Map<String, Object> getUpcomingJoineeList(Integer startIndex, Integer endIndex, String searchName) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(NewJoinee.class).addOrder(Order.asc("dateOfJoining"));
				//.add(Restrictions.ge("dateOfJoining", new Date())).addOrder(Order.asc("dateOfJoining"));
		//criteria.addOrder(Order.desc("dateOfJoining"));

		criteria.add(Restrictions.ilike("joineeName", searchName, MatchMode.ANYWHERE));
		/*
		 * Integer noOfRecords = criteria.list().size();
		 * criteria.setFirstResult(startIndex); criteria.setMaxResults(endIndex -
		 * startIndex);
		 * 
		 * List<NewJoinee> newJoinees = criteria.list();
		 */
		System.out.println("size of list:" + criteria.list().size());
		Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);

		Map<String, Object> upcmngJoineeMap = new HashMap<String, Object>();
		upcmngJoineeMap.put("list", criteriaMap.get("list"));
		upcmngJoineeMap.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));

		return upcmngJoineeMap;
	}

	@Override
	public Map<String, Object> searchScheduledCandidates(String skill, Date fromDate, Date toDate, String search,
			Integer startIndex, Integer endIndex) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(CandidateInterviewCycle.class);
		criteria.createAlias("candidate", "candidate");
        criteria.add(Restrictions.or(Restrictions.eq("candidate.candidateInterviewStatus",Status.IN_PROCESS), Restrictions.eq("candidate.candidateInterviewStatus", Status.RESCHEDULED)));
		criteria.add(Restrictions.and(Restrictions.ilike("candidate.technology", skill, MatchMode.ANYWHERE),
				Restrictions.ilike("candidate.fullName", search, MatchMode.ANYWHERE)))
				.addOrder(Order.desc("interviewCycleId"));

		if (fromDate != null) {
			criteria.add(Restrictions.ge("interviewDate", fromDate));
		}
		if (toDate != null) {
			criteria.add(Restrictions.le("interviewDate", toDate));
		}
		Integer noOfRecords = criteria.list().size();
		// query.setFirstResult(startIndex);
		// query.setMaxResults(endIndex - startIndex);
		List<CandidateInterviewCycle> cics = criteria.list();
		Map<String, Object> cicsMap = new HashMap<String, Object>();
		cicsMap.put("list", cics);
		cicsMap.put("size", noOfRecords);
		/*
		 * SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery
		 * ("select * from (SELECT * FROM `CANDIDATE_INTERVIEW_CYCLE` order by `CYCLEID` desc) as temp group by temp.`CANDIDATEID`"
		 * ); query.addEntity(CandidateInterviewCycle.class); Integer noOfRecords =
		 * query.list().size(); query.setFirstResult(startIndex);
		 * query.setMaxResults(endIndex - startIndex); List<CandidateInterviewCycle>
		 * cics = query.list(); Map<String, Object> cicsMap = new HashMap<String,
		 * Object>(); cicsMap.put("list", cics); cicsMap.put("size", noOfRecords);
		 */
		return cicsMap;

	}

	@Override
	public NewJoinee findnewJoinee(String email) {
		NewJoinee newJoinee = null;
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(NewJoinee.class);
		criteria.add(Restrictions.eq("email", email));
		if (!criteria.list().isEmpty()) {
			newJoinee = (NewJoinee) criteria.uniqueResult();
		}
		return newJoinee;
	}

	@Override
	public Boolean checkemail(String email) {
		Boolean flag = true;
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Candidate.class)
				.add(Restrictions.eq("email", email).ignoreCase());
		if (criteria.list().isEmpty()) {
			flag = false;
		}
		return flag;
	}

	@Override
	public CandidateInterviewCycle getrescduleDetails(String candidateId) {
		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(CandidateInterviewCycle.class);
		criteria1.createAlias("candidate", "candidate");
		criteria1.setFetchMode("candidate", FetchMode.JOIN);
		criteria1.add(Restrictions.eq("candidate.personId", Long.parseLong(candidateId)));
		// criteria1.add(Restrictions.eq("status", "finished"));
		// criteria1.addOrder(Order.desc("interviewRound"));
		criteria1.addOrder(Order.desc("interviewCycleId"));
		criteria1.setMaxResults(1);
		CandidateInterviewCycle candidateInterviewCycle = (CandidateInterviewCycle) criteria1.uniqueResult();
		return candidateInterviewCycle;
	}

	@Override
	public Designation designtionIsExisted(Long departmentId, String designationName) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Designation.class);
		criteria.createAlias("empDepartment", "empDepartment");
		criteria.add(Restrictions.eq("empDepartment.departmentId", departmentId));
		criteria.add(Restrictions.eq("name", designationName));
		return (Designation) criteria.uniqueResult();
	}

	@Override
	public Boolean designationIsAssigned(Designation designation) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Employee.class);
		criteria.add(Restrictions.eq("departmentName", designation.getEmpDepartment().getDepartmentName()));
		criteria.add(Restrictions.eq("designation", designation.getName()));
		criteria.add(Restrictions.ne("statusName", "InActive"));
		if (criteria.list().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Long highestInterviewRound(Long candidateId) {
		Long highestInterviewRound = null;
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(CandidateInterviewCycle.class)
				.add(Restrictions.eq("candidate.personId", candidateId)).addOrder(Order.desc("interviewCycleId"))
				.setMaxResults(1);

		CandidateInterviewCycle candidateInterviewCycle = (CandidateInterviewCycle) criteria.uniqueResult();

		highestInterviewRound = candidateInterviewCycle != null ? candidateInterviewCycle.getInterviewRound() : 0L;
		return highestInterviewRound;
	}

	@Override
	public int checkSlot(String name) {
		return sessionFactory.getCurrentSession().createCriteria(TimeSlot.class).add(Restrictions.ilike("name", name))
				.list().size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TimeSlot> getAllShifting() {
		return sessionFactory.getCurrentSession().createCriteria(TimeSlot.class).list();
	}

	// SouceType internal employee name
	@Override
	public Boolean internalSource(String firstName, String lastName) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Employee.class);
		criteria.add(Restrictions.eq("firstName", firstName));
		criteria.add(Restrictions.eq("lastName", lastName));
		Employee employee = (Employee) criteria.uniqueResult();
		if (employee != null) {
			return true;
		} else {
			return false;
		}

	}

	// reports page code starts here
	@Override
	public List<Object> getCandidatesListCount(String selectionStatus, String fromDate, String toDate) {

		Criteria criteria = dateCriteria(selectionStatus, fromDate, toDate);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.groupProperty("candidateInterviewStatus"));
		projList.add(Projections.count("candidateInterviewStatus"));
		criteria.setProjection(projList);
		// criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		/*
		 * String basicQuery =
		 * "SELECT CANDIDATE_INTERVIEW_STATUS,COUNT(*) as count FROM CANDIDATE INNER JOIN PERSON ON CANDIDATE.PERSONID = PERSON.PERSONID"
		 * ; if(selectionStatus.equalsIgnoreCase("Today")) { basicQuery = basicQuery +
		 * " WHERE date(PERSON.Created_Date) = '"+new Date().toString("yyyy/MM/dd")+"'";
		 * 
		 * }else if(selectionStatus.equalsIgnoreCase("Yesterday")) { basicQuery =
		 * basicQuery + " WHERE date(PERSON.Created_Date) = '"+new
		 * Date().previous().toString("yyyy/MM/dd")+"'"; }else { basicQuery = basicQuery
		 * + " WHERE date(PERSON.Created_Date) BETWEEN '2017-09-01' AND '2017-10-18'"; }
		 * basicQuery = basicQuery + " GROUP BY CANDIDATE_INTERVIEW_STATUS"; SQLQuery
		 * query = sessionFactory.getCurrentSession().createSQLQuery(basicQuery);
		 * query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		 */
		return criteria.list();
	}

	// method for getting
	public Criteria dateCriteria(String selectionStatus, String fromDate, String toDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Candidate.class);
		Criterion criterionNew = Restrictions.eq("candidateInterviewStatus", Status.NEW);
		logger.warn(criterionNew);
		Criterion criterionNotNew = Restrictions.not(criterionNew);
			//Criterion newStatus = Restrictions.and(criterionNew, "createdDate");
		//Criterion notNewStatus = Restrictions.and(criterionNew, "updatedDate");
		//Criterion wholeStatus = Restrictions.or(newStatus, notNewStatus);
			criteria.add(Restrictions.isNotNull("createdDate"));
			criteria.add(Restrictions.isNotNull("updatedDate"));
			switch (selectionStatus) {
			case "Today":
				Date today = new Date();
				Criterion createdDate = Restrictions.between("createdDate", getSecond(today, "initial"), getSecond(today, "end"));
				Criterion updatedDate = Restrictions.between("updatedDate", getSecond(today, "initial"), getSecond(today, "end"));
				Criterion newStatus = Restrictions.and(criterionNew, createdDate);
				logger.warn(newStatus);
				Criterion notNewStatus = Restrictions.and(criterionNotNew, updatedDate);
				logger.warn(notNewStatus);
				Criterion whole = Restrictions.or(newStatus, notNewStatus);
				criteria.add(whole);
				logger.warn(whole);
				
				break;
			case "Yesterday":
				Date yesterday = new Date().previous();
				Criterion createdDate1 = Restrictions.between("createdDate", getSecond(yesterday, "initial"), getSecond(yesterday, "end"));
				Criterion updatedDate1 = Restrictions.between("updatedDate", getSecond(yesterday, "initial"), getSecond(yesterday, "end"));		
				Criterion newStatus1 = Restrictions.and(criterionNew, createdDate1);
				Criterion notNewStatus1 = Restrictions.and(criterionNotNew, updatedDate1);
				Criterion whole1 = Restrictions.or(newStatus1, notNewStatus1);
				criteria.add(whole1);
				break;
			case "Custom":
				Date startDate = null, endDate = null;
				try {
					startDate = DateParser.toDate(fromDate);
					endDate = DateParser.toDate(toDate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				criteria.add(
						Restrictions.between("createdDate", getSecond(startDate, "initial"), getSecond(endDate, "end")));
				break;
			case "This Week":
				Date weekStart = getFirstDayOfWeek();
				Date weekend = weekStart.shift(new Duration(com.raybiztech.date.TimeUnit.DAY, +6));
				criteria.add(
						Restrictions.between("createdDate", getSecond(weekStart, "initial"), getSecond(weekend, "end")));
				break;
			case "Last Week":
				Date currentWeekStart = getFirstDayOfWeek();
				Date lastWeekStart = currentWeekStart.shift(new Duration(com.raybiztech.date.TimeUnit.DAY, -7));
				Date lastWeekEnd = lastWeekStart.shift(new Duration(com.raybiztech.date.TimeUnit.DAY, 6));
				criteria.add(Restrictions.between("createdDate", getSecond(lastWeekStart, "initial"),
						getSecond(lastWeekEnd, "end")));
				break;
			case "Last Month":
				Date date = new Date(DayOfMonth.valueOf(1), MonthOfYear.valueOf(new Date().getMonthOfYear().getValue()),
						YearOfEra.valueOf(new Date().getYearOfEra().getValue()));

				Date lastMonthLastDate = date.shift(new Duration(com.raybiztech.date.TimeUnit.DAY, -1));

				Date lastMonthFirstDate = new Date(DayOfMonth.valueOf(1),
						MonthOfYear.valueOf(lastMonthLastDate.getMonthOfYear().getValue()),
						YearOfEra.valueOf(lastMonthLastDate.getYearOfEra().getValue()));
				criteria.add(Restrictions.between("createdDate", getSecond(lastMonthFirstDate, "initial"),
						getSecond(lastMonthLastDate, "end")));
				break;
			case "Current Month":
				Date firstDate = new Date(DayOfMonth.valueOf(1),
						MonthOfYear.valueOf(new Date().getMonthOfYear().getValue()),
						YearOfEra.valueOf(new Date().getYearOfEra().getValue()));

				Date lastDate = new Date(DayOfMonth.valueOf(31),
						MonthOfYear.valueOf(new Date().getMonthOfYear().getValue()),
						YearOfEra.valueOf(new Date().getYearOfEra().getValue()));
				criteria.add(
						Restrictions.between("createdDate", getSecond(firstDate, "initial"), getSecond(lastDate, "end")));
				break;
			}

		
		
		return criteria;
	}

	// methods for getting date from a datetime format
	public Second getSecond(Date date, String flag) {
		Second second = null;
		if (flag.equals("initial")) {
			second = new Second(date.getYearOfEra(), date.getMonthOfYear(), date.getDayOfMonth(), HourOfDay.valueOf(0),
					MinuteOfHour.valueOf(0), SecondOfMinute.valueOf(0));
		} else {
			second = new Second(date.getYearOfEra(), date.getMonthOfYear(), date.getDayOfMonth(), HourOfDay.valueOf(23),
					MinuteOfHour.valueOf(59), SecondOfMinute.valueOf(59));
		}
		return second;

	}

	@Override
	public List<Long> getAllPipList() {
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(PIP.class);
		criteria.add(Restrictions.eq("PIPFlag", Boolean.TRUE));
		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.eq("employee.statusName", "Active"));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("employee.employeeId"));
		criteria.setProjection(projectionList);
		List<Long> list = criteria.list();
		return list;
	}

	@Override
	public Map<String, Object> getEmployeeList(List<Long> ids, Integer startIndex, Integer endIndex,
			String searchStr) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Employee.class);
		criteria.add(Restrictions.in("employeeId", ids));
		if (!searchStr.isEmpty()) {
			criteria.add(Restrictions.ilike("employeeFullName", searchStr, MatchMode.ANYWHERE));
		}
		criteria.add(Restrictions.ne("employmentTypeName", "On Contract Hire")); 
		criteria.add(Restrictions.ne("employmentTypeName", "On Contract"));
		criteria.addOrder(Order.asc("employeeId"));
		Map<String, Object> getPaginatedList = getPaginationList(criteria, startIndex, endIndex);
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("employeeList", getPaginatedList.get("list"));
		map.put("size", getPaginatedList.get("listSize"));
		return map;
	}

	@Override
	public String getInterviewerName(Long candidateId) {
		String interviewerName = null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CandidateInterviewCycle.class);
		criteria.add(Restrictions.and(Restrictions.eq("candidate.personId", candidateId), 
				Restrictions.isNull(("interviewResultStatus"))));
		criteria.addOrder(Order.desc("interviewCycleId")).setMaxResults(1);
		CandidateInterviewCycle candidateInterviewCycle = (CandidateInterviewCycle) criteria.uniqueResult();
		interviewerName = candidateInterviewCycle !=null ? candidateInterviewCycle.getInterviewers() : "" ;
		return interviewerName;
	}

	@Override
	public List<Long> getAbscondedList() {
		
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(Employee.class);
		criteria.add(Restrictions.eq("statusName", "InActive"));
		criteria.add(Restrictions.eq("isAbsconded", Boolean.TRUE));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("employeeId"));
		criteria.setProjection(projectionList);
		List<Long> list = criteria.list();
		
		return list;
	}

	
	@Override
	public Map<String, Object> getAllCompanies(Integer startIndex, Integer endIndex,String searchCompany,String selectionTechnology) {
		Criteria criteria =sessionFactory.getCurrentSession().createCriteria(Companies.class);
		//criteria.createAlias("company","company");
		//criteria.add(Restrictions.isNotNull("currentEmployer"));
		if(!searchCompany.isEmpty()){
			criteria.add(Restrictions.ilike("companyName", searchCompany, MatchMode.ANYWHERE));
			}
		//Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);
		Map<String, Object> companiesMap = new HashMap<String, Object>();
		companiesMap.put("list", criteria.list());
		companiesMap.put("size", criteria.list().size());
		
		return companiesMap;
	}
	@Override
	public Map<String, Object> getSearchCompanies(Integer startIndex, Integer endIndex,String searchCompany) {
		//logger.warn("in dao");
		Criteria criteria =sessionFactory.getCurrentSession().createCriteria(Companies.class);
		
		if(!searchCompany.isEmpty()){
		criteria.add(Restrictions.ilike("companyName", searchCompany, MatchMode.ANYWHERE));
		}
		Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);
		Map<String, Object> companiesMap = new HashMap<String, Object>();
		companiesMap.put("list", criteriaMap.get("list"));
		companiesMap.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));
		
		return companiesMap;
	}

	@Override
	public Long getCandidatesCount(String comList ,String selectionTechnology) {
		Criteria criteria =sessionFactory.getCurrentSession().createCriteria(Candidate.class);
		criteria.add(Restrictions.eq("currentEmployer", comList));
		
		if(!selectionTechnology.isEmpty()){
		criteria.add(Restrictions.eq("technology",selectionTechnology));
		}
		criteria.setProjection(Projections.rowCount());
	 	Long count = (Long) criteria.uniqueResult();
		return count;
	}

	@Override
	public Long getemployessCount(String comList ,String selectionTechnology) {
		Criteria criteria =sessionFactory.getCurrentSession().createCriteria(Candidate.class);
		criteria.add(Restrictions.eq("currentEmployer", comList));
		criteria.add(Restrictions.eq("candidateInterviewStatus", Status.OFFERED));
		if(!selectionTechnology.isEmpty()){
			criteria.add(Restrictions.eq("technology",selectionTechnology));
			}
		
		criteria.setProjection(Projections.rowCount());
	 	Long count = (Long) criteria.uniqueResult();
		return count;
	}

	@Override
	public Boolean isCompanyExits(String companyName){
		//logger.warn("vvvvv");
		Criteria criteria =sessionFactory.getCurrentSession().createCriteria(Companies.class);
		criteria.add(Restrictions.eq("companyName", companyName));
		if (criteria.list().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Map<String, Object>  getAllCandidatesInfo(Integer startIndex, Integer endIndex,String companyName) {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Candidate.class);
		criteria.add(Restrictions.eq("currentEmployer", companyName));
		/*
		 * if(selectionTechnology!=null) { criteria.add(Restrictions.eq("technology"
		 * ,selectionTechnology)); }
		 */
		
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		
		Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);
		Map<String, Object> candidatesMap = new HashMap<String, Object>();
		candidatesMap.put("list", criteriaMap.get("list"));
		candidatesMap.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));
		
		return candidatesMap;
	}
	
	@Override
	public Map<String, Object> getAllEmployeesInfo(Integer startIndex, Integer endIndex,String companyName) {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Candidate.class);
		criteria.add(Restrictions.eq("currentEmployer", companyName));
		
		criteria.add(Restrictions.eq("candidateInterviewStatus", Status.OFFERED));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);
		Map<String, Object> employeesMap = new HashMap<String, Object>();
		employeesMap.put("list", criteriaMap.get("list"));
		employeesMap.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));
		return employeesMap;
	}

	@Override
	public List<Long> getAllContractList() {
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(Employee.class);
		criteria.add(Restrictions.eq("statusName", "Active"));
		criteria.add(Restrictions.eq("contractExists", Boolean.TRUE));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("employeeId"));
		criteria.setProjection(projectionList);
		List<Long> list = criteria.list();
		return list;
	}

	@Override
	public List<Employee> getEmployeesExperiencesList() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Employee.class);
		criteria.add(Restrictions.eq("statusName", "Active"));
		//criteria.add(Restrictions.ge("joiningDate", new Date()));
		//criteria.add(Restrictions.isNotNull("experience"));
		return criteria.list();
	}
	

	@Override
	public List<CandidateInterviewCycle> getInterviewerDetails(String fromDate, String toDate ,String selectionStatus) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CandidateInterviewCycle.class);

		Date startDate = null, endDate = null;
		try {
			startDate = DateParser.toDate(fromDate);
			endDate = DateParser.toDate(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (selectionStatus.equalsIgnoreCase("Today")) {
			criteria.add(Restrictions.eq("interviewDate", new Date()));
		}

		else if (selectionStatus.equalsIgnoreCase("Yesterday")) {
			criteria.add(Restrictions.eq("interviewDate", new Date().previous()));
		}

		else if (selectionStatus.equalsIgnoreCase("This week")) {
			Date weekStart = getFirstDayOfWeek();
			Date weekEnd = weekStart.shift(new Duration(com.raybiztech.date.TimeUnit.DAY, +6));
			criteria.add(Restrictions.between("interviewDate", weekStart, weekEnd));

		} else if (selectionStatus.equalsIgnoreCase("Last week")) {
			Date currentWeekStart = getFirstDayOfWeek();
			Date lastWeekStart = currentWeekStart.shift(new Duration(com.raybiztech.date.TimeUnit.DAY, -7));
			Date lastWeekEnd = lastWeekStart.shift(new Duration(com.raybiztech.date.TimeUnit.DAY, +6));
			criteria.add(Restrictions.between("interviewDate", lastWeekStart, lastWeekEnd));

		} else if (selectionStatus.equalsIgnoreCase("Last month")) {

			Date date = new Date(DayOfMonth.valueOf(1), MonthOfYear.valueOf(new Date().getMonthOfYear().getValue()),
					YearOfEra.valueOf(new Date().getYearOfEra().getValue()));
			Date lastMonthLastDate = date.shift(new Duration(com.raybiztech.date.TimeUnit.DAY, -1));
			Date lastMonthFirstDate = new Date(DayOfMonth.valueOf(1),
					MonthOfYear.valueOf(lastMonthLastDate.getMonthOfYear().getValue()),
					YearOfEra.valueOf(lastMonthLastDate.getYearOfEra().getValue()));

			criteria.add(Restrictions.between("interviewDate", lastMonthFirstDate, lastMonthLastDate));

		} else if (selectionStatus.equalsIgnoreCase("Current month")) {

			Date firstDate = new Date(DayOfMonth.valueOf(1),
					MonthOfYear.valueOf(new Date().getMonthOfYear().getValue()),
					YearOfEra.valueOf(new Date().getYearOfEra().getValue()));

			Date lastDate = new Date(DayOfMonth.valueOf(31),
					MonthOfYear.valueOf(new Date().getMonthOfYear().getValue()),
					YearOfEra.valueOf(new Date().getYearOfEra().getValue()));

			criteria.add(Restrictions.between("interviewDate", firstDate, lastDate));

		} else if (selectionStatus.equalsIgnoreCase("Custom")) {
			criteria.add(Restrictions.between("interviewDate", startDate, endDate));
			
		}

		criteria.add(Restrictions.eq("status", "finished"));
	
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		List<CandidateInterviewCycle> list1 = criteria.list();
		

		return list1;

	}

	@Override
	public List<Employee> getActiveEmployees() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Employee.class);
		criteria.add(Restrictions.eq("statusName" ,"Active"));
	    List<Employee> empList = criteria.list();
		return empList;
	}

	/*
	 * @Override public List<CandidateInterviewCycle> getCandidates(String
	 * selectionDate, String from, String to) { Date startDate = null, endDate =
	 * null; try { startDate = DateParser.toDate(from); endDate =
	 * DateParser.toDate(to); } catch (ParseException e) { e.printStackTrace(); }
	 * Criteria criteria =
	 * sessionFactory.getCurrentSession().createCriteria(CandidateInterviewCycle.
	 * class); criteria.createAlias("candidate", "candidate");
	 * criteria.add(Restrictions.between("interviewDate",startDate, endDate));
	 * criteria.add(Restrictions.eq("candidate.personId","candidateId"));
	 * criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY); return
	 * criteria.list(); }
	 */
	@Override
	public Map<String, Date> getDates(String dateSelection,String from,String to) {
		
		Date startDate = null, endDate = null;
		try {
			startDate = DateParser.toDate(from);
			endDate = DateParser.toDate(to);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	     Map<String,Date> map = new HashMap<String,Date>();
	     
	     if (dateSelection.equalsIgnoreCase("Today")) {
	    	
				startDate = new Date();
				endDate = new Date();

			} else if (dateSelection.equalsIgnoreCase("Yesterday")) {
				
				startDate = new Date().previous();
				endDate = new Date().previous();
			} else if (dateSelection.equalsIgnoreCase("This week")) {
				
				startDate = getFirstDayOfWeek();
				endDate = startDate.shift(new Duration(
						com.raybiztech.date.TimeUnit.DAY, +6));

			} else if (dateSelection.equalsIgnoreCase("Last week")) {
				
				Date currentWeekStart = getFirstDayOfWeek();
				startDate = currentWeekStart.shift(new Duration(
						com.raybiztech.date.TimeUnit.DAY, -7));

				Date lastWeekStart = currentWeekStart.shift(new Duration(
						com.raybiztech.date.TimeUnit.DAY, -7));
				endDate = lastWeekStart.shift(new Duration(
						com.raybiztech.date.TimeUnit.DAY, 6));

			} else if (dateSelection.equalsIgnoreCase("Last month")) {
				
				Date date1 = new Date(
						DayOfMonth.valueOf(1),
						MonthOfYear.valueOf(new Date().getMonthOfYear().getValue()),
						YearOfEra.valueOf(new Date().getYearOfEra().getValue()));

				endDate = date1.shift(new Duration(com.raybiztech.date.TimeUnit.DAY,
						-1));

				startDate = new Date(DayOfMonth.valueOf(1),
						MonthOfYear.valueOf(endDate.getMonthOfYear().getValue()),
						YearOfEra.valueOf(endDate.getYearOfEra().getValue()));

			} else if (dateSelection.equalsIgnoreCase("Current Month")) {
				
				startDate = new Date(
						DayOfMonth.valueOf(1),
						MonthOfYear.valueOf(new Date().getMonthOfYear().getValue()),
						YearOfEra.valueOf(new Date().getYearOfEra().getValue()));

				Calendar c = Calendar.getInstance();
				int monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);

				endDate = new Date(
						DayOfMonth.valueOf(monthMaxDays),
						MonthOfYear.valueOf(new Date().getMonthOfYear().getValue()),
						YearOfEra.valueOf(new Date().getYearOfEra().getValue()));

			} 
		
			return map;
		}

	
	  public List<Candidate> getStatus(String dateSelection,String from,String to){
	  
	  Date startDate = null, endDate = null; 
	  try { 
		  startDate =DateParser.toDate(from); 
		  endDate = DateParser.toDate(to); 
		  } catch(ParseException e)
	         { 
			  e.printStackTrace();
			  }
	  
	  Criteria criteria= sessionFactory.getCurrentSession().createCriteria(Candidate.class);
	  criteria.add(Restrictions.between("candidateInterviewStatus", startDate,endDate)); 
	  return criteria.list();
	  }

	@Override
	public List<Object[]> getInterviewStatusCount(String dateSelection, Date fromDate, Date toDate) {
	
	String fDate = "";
	String tDate = "";
	String where ="";
	if (fromDate != null) {
		fDate =fromDate.toString("yyyy-MM-dd");
	}
	if (toDate != null) {
		tDate = toDate.toString("yyyy-MM-dd");
	}
	
	
	
		String sql= " SELECT c.CANDIDATE_INTERVIEW_STATUS AS STATUS , COUNT(c.PERSONID) AS COUNT FROM CANDIDATE  c"
				     + " INNER JOIN "  
				     + " (SELECT DISTINCT CANDIDATEID,MAX(CYCLEID),MAX(INTERVIEWROUND)" 
				     + " FROM CANDIDATE_INTERVIEW_CYCLE" 
				     + " WHERE INTERVIEWDATE >="+"'"+fDate+"'"+"AND INTERVIEWDATE<="+"'"+tDate+"'"
				     + " GROUP BY CANDIDATEID "
				     + " ORDER BY CYCLEID DESC) as c1" 
				     + " ON c.PERSONID = c1.CANDIDATEID" 
				     + " GROUP BY c.CANDIDATE_INTERVIEW_STATUS" ;
				
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		
		return query.list();
	}
	@Override
	public List<CandidateInterviewCycle> getCandidateTimeLinedetails(String candidateId,String name) {
		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(CandidateInterviewCycle.class);
		criteria1.add(Restrictions.eq("interviewers", name));
		criteria1.createAlias("candidate", "candidate");
		criteria1.setFetchMode("candidate", FetchMode.JOIN);
		criteria1.add(Restrictions.eq("candidate.personId", Long.parseLong(candidateId)));
		// criteria1.add(Restrictions.eq("status", "finished"));
		criteria1.addOrder(Order.desc("interviewRound"));
		return criteria1.list();
	}

	@Override
	public List<CandidateInterviewCycle> searchCadidates( String fromDate, String toDate, String skill) {
	

		  Date startDate = null, endDate = null; 
		  try { 
			  startDate =DateParser.toDate(fromDate); 
			  endDate = DateParser.toDate(toDate); 
			  } catch(ParseException e)
		         { 
				  e.printStackTrace();
				  }
		  
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(CandidateInterviewCycle.class);
		if (startDate != null) {
			criteria.add(Restrictions.ge("interviewDate", startDate));
		}
		if (endDate != null) {
			criteria.add(Restrictions.le("interviewDate", endDate));
		}
		criteria.createAlias("candidate", "candidate");
		if(skill!=null) {
		criteria.add(Restrictions.ilike("candidate.technology", skill,MatchMode.ANYWHERE));
		}
		return criteria.list();

	}

	@Override
	public Map<String, Object> getSelectedTypeEmployeeData(
			String selectionStatus, String selectionType, Integer startIndex,
			Integer endIndex, String searchStr,String country) {

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Employee.class);
		criteria.add(Restrictions.ne("employeeId", 1000L));
		System.out.println("type:" + selectionType);
		if(selectionStatus!=null && !selectionStatus.isEmpty()){
			criteria.add(Restrictions.eq("statusName", selectionStatus));
		}

			if (!searchStr.isEmpty()) {
				System.out.println("in if" + searchStr);
				criteria.add(Restrictions.ilike("employeeFullName", searchStr, MatchMode.ANYWHERE));
			
			}
		

		if(selectionType!=null && !selectionType.isEmpty()) {
		if("Active".equalsIgnoreCase(selectionStatus) && (selectionType.equalsIgnoreCase("Home") || selectionType.equalsIgnoreCase("Office"))){
			criteria.add(Restrictions.eq("workStatus", selectionType));
		}
		if("InActive".equalsIgnoreCase(selectionStatus) && (selectionType.equalsIgnoreCase("Home") || selectionType.equalsIgnoreCase("Office")))
         {
           criteria.add(Restrictions.eq("workStatus", selectionType));
	    }
       if("Active".equalsIgnoreCase(selectionStatus) && selectionType.equalsIgnoreCase("External Vendor")){
			criteria.add(Restrictions.eq("jobTypeName", selectionType));
		}
       if("InActive".equalsIgnoreCase(selectionStatus) && selectionType.equalsIgnoreCase("External Vendor")){
			criteria.add(Restrictions.eq("jobTypeName", selectionType));
		}
       if("Active".equalsIgnoreCase(selectionStatus) && selectionType.equalsIgnoreCase("Employment Contract")){
			criteria.add(Restrictions.eq("contractExists", Boolean.TRUE));
		}
       if("InActive".equalsIgnoreCase(selectionStatus) && selectionType.equalsIgnoreCase("Employment Contract")){
			criteria.add(Restrictions.eq("contractExists", Boolean.TRUE));
		}
		}
		if(!country.isEmpty()){
			if("Active".equalsIgnoreCase(selectionStatus)){
				criteria.add(Restrictions.eq("country", country));
			}
			if("InActive".equalsIgnoreCase(selectionStatus)){
				criteria.add(Restrictions.eq("country", country));
			}
		}
	 
		criteria.addOrder(Order.asc("employeeId"));

		Map<String, Object> paginationMap = getPaginationList(criteria, startIndex, endIndex);

		Map<String, Object> employeeMap = new HashMap<String, Object>();
		employeeMap.put("employeeList", paginationMap.get("list"));
		employeeMap.put("size", paginationMap.get("listSize"));

		return employeeMap;

	
	}
	
	@Override
	public Map<String, Object> getEmployeeCategoryData(String selectionDesignation, Integer startIndex,
			Integer endIndex) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Employee.class);
	
		
		System.out.println("de:" +selectionDesignation + startIndex + endIndex);
		criteria.add(Restrictions.eq("statusName", "Active"));
		if(!selectionDesignation.isEmpty()){
			System.out.println("in if");
			criteria.add(Restrictions.ne("employeeId", 1000L));
		 criteria.add(Restrictions.eq("designation", selectionDesignation));
		 criteria.addOrder(Order.asc("employeeId"));
		}
		System.out.println("size:" +criteria.list().size());

		Map<String, Object> paginationMap = getPaginationList(criteria, startIndex, endIndex);

		Map<String, Object> employeeMap = new HashMap<String, Object>();
		employeeMap.put("employeeList", paginationMap.get("list"));
		System.out.println("s:" + paginationMap.get("listSize"));
		employeeMap.put("size", paginationMap.get("listSize"));

		return employeeMap;

	}
	@Override
	public List<VendorDetails> getVendorDetails() {
		 Criteria criteria = sessionFactory.getCurrentSession().createCriteria(VendorDetails.class);
		 criteria.createAlias("empDepartment", "empDepartment");
		 criteria.add(Restrictions.eq("empDepartment.departmentName", "Development"));
		 System.out.println(criteria.list().size());
		return criteria.list();
	}
	@Override
	public List<Gender> getGenders() {
		 Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Gender.class);
		 System.out.println("genders: " + criteria.list().size());
		return criteria.list();
	}
	@Override
	public List<BloodGroup> getBloodgroups() {
		 Criteria criteria = sessionFactory.getCurrentSession().createCriteria(BloodGroup.class);
		 System.out.println("groups: " + criteria.list().size());
		return criteria.list();
	}
	@Override
	public List<MaritalStatus> getMaritalStatus() {
		 Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MaritalStatus.class);
		 System.out.println("status: " + criteria.list().size());
		return criteria.list();
	}

	@Override
	public List<Relations> getRelations() {
		 Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Relations.class);
		 System.out.println("relations: " + criteria.list().size());
		return criteria.list();
	}

	@Override
	public List<QualificationCategory> getQualificationCatergory() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(QualificationCategory.class);
		 System.out.println("QualificationCatergory: " + criteria.list().size());
		return criteria.list();
	}

	@Override
	public List<EmploymentType> getEmploymentType() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EmploymentType.class);
		 System.out.println("EmploymentType: " + criteria.list().size());
		return criteria.list();
	}
	@Override
	public List<JobType> getJobType() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(JobType.class);
		 System.out.println("JobType: " + criteria.list().size());
		return criteria.list();
	}


}
