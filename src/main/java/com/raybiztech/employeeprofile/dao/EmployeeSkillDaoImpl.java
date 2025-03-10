package com.raybiztech.employeeprofile.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.certification.business.CertificateType;
import com.raybiztech.appraisals.certification.business.Certification;
import com.raybiztech.appraisals.business.VisaDetails;
import com.raybiztech.appraisals.business.VisaLookUp;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;

@Repository("employeeSkillDaoImpl")
public class EmployeeSkillDaoImpl extends DAOImpl implements EmployeeSkillDao {

	@Autowired
	DAO dao;

	@Override
	public Map<String, Object> getEmployeesList(Integer startIndex,
			Integer endIndex, String categoryId, String skillId,
			String searchstr, String Competency, String year, String month) {

		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(Employee.class);
		criteria.add(Restrictions.ne("employeeId", 1000L));

		criteria.add(Restrictions.eq("statusName", "Active"));

		criteria.addOrder(Order.asc("employeeId"));

		if (categoryId.isEmpty() && skillId.isEmpty() && !searchstr.isEmpty()) {

			criteria.add(Restrictions.ilike("employeeFullName", searchstr,
					MatchMode.ANYWHERE));

		}

		/*
		 * else if(skillId.isEmpty() && !categoryId.isEmpty()) {
		 * criteria.createAlias("employeeSkills", "employeeSkills");
		 * criteria.add(Restrictions.eq("employeeSkills.categoryTypeId",
		 * categoryId)); if(!searchstr.isEmpty()) {
		 * criteria.add(Restrictions.ilike("employeeFullName", searchstr,
		 * MatchMode.ANYWHERE));
		 * 
		 * }
		 * 
		 * if(!Competency.isEmpty()) {
		 * criteria.add(Restrictions.like("employeeSkills.competency",
		 * Competency)); }
		 * 
		 * 
		 * 
		 * if(!year.isEmpty() && !month.isEmpty()) {
		 * criteria.add(Restrictions.like("employeeSkills.expYear", year));
		 * criteria.add(Restrictions.like("employeeSkills.expMonth", month)); }
		 * else if(!year.isEmpty()) {
		 * criteria.add(Restrictions.like("employeeSkills.expYear", year)); } }
		 */

		else if (!skillId.isEmpty()) {
			criteria.createAlias("employeeSkills", "employeeSkills");

			criteria.add(Restrictions.eq("employeeSkills.skillType", skillId));
			if (!searchstr.isEmpty()) {
				criteria.add(Restrictions.ilike("employeeFullName", searchstr,
						MatchMode.ANYWHERE));
			}

			if (!Competency.isEmpty()) {
				criteria.add(Restrictions.like("employeeSkills.competency",
						Competency));
			}

			if (!year.isEmpty() && !month.isEmpty()) {
				criteria.add(Restrictions.like("employeeSkills.expYear", year));
				criteria.add(Restrictions
						.like("employeeSkills.expMonth", month));
			} else if (!year.isEmpty()) {
				criteria.add(Restrictions.like("employeeSkills.expYear", year));
			}

		}

		Map<String, Object> paginationMap = dao.getPaginationList(criteria,
				startIndex, endIndex);

		Map<String, Object> employeeMap = new HashMap<String, Object>();
		employeeMap.put("employeeList", paginationMap.get("list"));
		employeeMap.put("size", paginationMap.get("listSize"));

		return employeeMap;
	}

	@Override
	public Map<String, Object> getAllEmployeesCertficates( String selectionTechnology, String selectedCertificate, String multipleSearch) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Certification.class);
		
		criteria.createAlias("employee", "employee");
		
		criteria.add(Restrictions.and(Restrictions.ne("employee.employeeId", 1000L),
				Restrictions.eq("employee.statusName", "Active")));
		
		if (!selectionTechnology.isEmpty())
			criteria.add(Restrictions.ilike("technology", selectionTechnology));
		
		if(!selectedCertificate.isEmpty())
			criteria.add(Restrictions.ilike("certificateType", selectedCertificate));

		if (!multipleSearch.equalsIgnoreCase("null")
				) {
			Criterion code = Restrictions.ilike("code", multipleSearch,
					MatchMode.ANYWHERE);
			Criterion name = Restrictions.ilike("name", multipleSearch,
					MatchMode.ANYWHERE);
			Criterion employeeName = Restrictions.ilike("employee.employeeFullName", multipleSearch,MatchMode.ANYWHERE);
			
			Criterion techCertType = Restrictions.or(Restrictions.ilike("technology", multipleSearch), 
					Restrictions.ilike("certificateType", multipleSearch));
			
			Criterion criterion = Restrictions.or(code, name);
			
			Criterion criterion1 = Restrictions.or(employeeName, criterion);
			
			criteria.add(Restrictions.or(techCertType, criterion1));

		}
		
		criteria.setProjection(Projections.projectionList().add(
				Projections.groupProperty("employee.employeeId")));
		Map<String, Object> certificatesReport = new HashMap<String, Object>();
		certificatesReport.put("certificatesList", criteria.list());
		certificatesReport.put("size", criteria.list().size());

		/*
		 * String whereCondition =
		 * " WHERE e.StatusName = 'Active' and e.EmployeeId != 1001";
		 * 
		 * 
		 * String sqlQuery =
		 * "SELECT temp.* from (SELECT c.* FROM Certifications  c " +
		 * " JOIN EMPLOYEE e on c.EmployeeId = e.EmployeeId " + whereCondition +
		 * " GROUP BY e.EmployeeId ORDER BY e.EmployeeId)" + "as temp";
		 * 
		 * SQLQuery query =
		 * sessionFactory.getCurrentSession().createSQLQuery(sqlQuery);
		 * System.out.println(query);
		 * 
		 * 
		 * query.addEntity(Certification.class);
		 * 
		 * List<Certification> certificatesList = query.list(); Map<String,
		 * Object> map = new HashMap<>(); map.put("list", certificatesList);
		 */
		return certificatesReport;
	}

	@Override
	public List<Certification> getCertificatesForEmployee(Long empId,
			String selectionTechnology, String selectedCertificate, String multipleSearch) {
		
		 Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				 Certification.class);
		 
		 
		 criteria.createAlias("employee", "employee");
		 
	        criteria.add(Restrictions.eq("employee.employeeId", empId));
	        
	    	if (!selectionTechnology.isEmpty())
				criteria.add(Restrictions.ilike("technology", selectionTechnology));
	    	
	    	if(!selectedCertificate.isEmpty())
				criteria.add(Restrictions.ilike("certificateType", selectedCertificate));

			if (!multipleSearch.equalsIgnoreCase("null")
					) {
				Criterion code = Restrictions.ilike("code", multipleSearch,
						MatchMode.ANYWHERE);
				Criterion name = Restrictions.ilike("name", multipleSearch,
						MatchMode.ANYWHERE);
				Criterion employeeName = Restrictions.ilike("employee.employeeFullName", multipleSearch,MatchMode.ANYWHERE);
				Criterion criterion = Restrictions.or(code, name);
				
				Criterion techCertType = Restrictions.or(Restrictions.ilike("technology", multipleSearch, MatchMode.ANYWHERE), 
						Restrictions.ilike("certificateType", multipleSearch, MatchMode.ANYWHERE));
				Criterion criterion1 = Restrictions.or(employeeName, criterion);
				
				criteria.add(Restrictions.or(techCertType, criterion1));

			}
			
		return criteria.list();
	}
	
	@Override
	public List<VisaLookUp> getVisaTypeList() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(VisaLookUp.class);
		return criteria.list();
	}

	@Override
	public List<CountryLookUp> getCountries() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CountryLookUp.class);
		return criteria.list();
	}

	

	@Override
	public Map<String, Object> getEmployeeVisaDetailsReport(Integer countryId,
			Long visaTypeId, String multipleSearch) {
		//Criteria criteria = sessionFactory.getCurrentSession().createCriteria(VisaDetails.class);
		
		String whereCondition = " WHERE e.StatusName = 'Active' and e.EmployeeId != 1001";
		
		if(countryId != null) {
			whereCondition += " and vl.Country =" + countryId;
		}
		
		if(visaTypeId != null) {
			whereCondition += " and vl.ID =" + visaTypeId;
		}
		
		if(!multipleSearch.isEmpty()) {
			whereCondition += " and (e.employeeFullName LIKE " + "'"+multipleSearch+"%'"
								+ " or vl.VisaType = '" +multipleSearch+"')";
		}
		
		String sqlQuery = "SELECT temp.* from (SELECT v.* FROM VisaDetails  v "
							+ " JOIN EMPLOYEE e on v.EmployeeId = e.EmployeeId "
							+ " JOIN VisaLookUp vl on v.visaLookUpId = vl.ID " 
							+ whereCondition
							+ " ORDER BY e.EmployeeId)"  
							+ "as temp";
		
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery);
		query.addEntity(VisaDetails.class);
		
		/*criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.and(Restrictions.ne("employee.employeeId", 1000L),
				Restrictions.eq("employee.statusName", "Active")));
		//System.out.println("listSize:"+criteria.list().size());
		criteria.createAlias("visaLookUp", "visaType");
		
		if(visaTypeId != null) {
			//System.out.println("in visa type if");	
			criteria.add(Restrictions.eq("visaType.id", visaTypeId));
		}
		
		if(countryId != null) {
			//System.out.println("in country if");
			//criteria.createAlias("visaLookUp", "visaType");
			criteria.createAlias("visaType.country", "country");
			criteria.add(Restrictions.eq("country.id", countryId));
		}
		
		if(!multipleSearch.isEmpty()) {
			criteria.add(Restrictions.ilike("employee.employeeFullName", multipleSearch,MatchMode.ANYWHERE));
		}
		
		//System.out.println("listSize1:"+criteria.list().size());
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("employee.employeeId")));
				
		
		//System.out.println("listSize2:"+criteria.list().size());
		criteria.addOrder(Order.asc("employee.employeeId"));
		//System.out.println("listSize3:"+criteria.list().size());
		
		Map<String, Object> employeeMap = new HashMap<String, Object>();
		employeeMap.put("list", criteria.list());
		employeeMap.put("size", criteria.list().size());
		return employeeMap;*/
		
		Map<String, Object> employeeMap = new HashMap<String, Object>();
		employeeMap.put("list", query.list());
		employeeMap.put("size", query.list().size());
		return employeeMap;
	}

	@Override
	public List<VisaDetails> getEmployeeVisaDetailsList(Long empId, Integer countryId, Long visaTypeId,
			String multipleSearch) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(VisaDetails.class);
		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.eq("employee.employeeId", empId));
		criteria.createAlias("visaLookUp", "visaType");
		
		if(visaTypeId != null) {
			criteria.add(Restrictions.eq("visaType.id", visaTypeId));
		}
		
		if(countryId != null) {
			criteria.createAlias("visaType.country", "country");
			criteria.add(Restrictions.eq("country.id", countryId));
		}
		
		if(!multipleSearch.isEmpty()) {
			criteria.add(Restrictions.or(Restrictions.ilike("employee.employeeFullName", multipleSearch,MatchMode.ANYWHERE),
					Restrictions.ilike("visaType.visaType", multipleSearch)));
		}
		criteria.addOrder(Order.desc("id"));
		return criteria.list();
	}

	@Override
	public List<Certification> getExportEmployeesCertficates(
			String selectionTechnology, String selectedCertificate, String multipleSearch) {
		 Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				 Certification.class);
		criteria.createAlias("employee", "employee");
		
		criteria.add(Restrictions.and(Restrictions.ne("employee.employeeId", 1000L),
				Restrictions.eq("employee.statusName", "Active")));
		 
	       // criteria.add(Restrictions.eq("employee.employeeId", empId));
	        
	    	if (!selectionTechnology.isEmpty())
				criteria.add(Restrictions.ilike("technology", selectionTechnology));
	    	
	    	if(!selectedCertificate.isEmpty())
				criteria.add(Restrictions.ilike("certificateType", selectedCertificate));

			if (!multipleSearch.equalsIgnoreCase("null")
					) {
				Criterion code = Restrictions.ilike("code", multipleSearch,
						MatchMode.ANYWHERE);
				Criterion name = Restrictions.ilike("name", multipleSearch,
						MatchMode.ANYWHERE);
				Criterion employeeName = Restrictions.ilike("employee.employeeFullName", multipleSearch,MatchMode.ANYWHERE);
				Criterion techCertType = Restrictions.or(Restrictions.ilike("technology", multipleSearch, MatchMode.ANYWHERE), 
						Restrictions.ilike("certificateType", multipleSearch, MatchMode.ANYWHERE));
				Criterion criterion = Restrictions.or(code, name);
				Criterion criterion1 = Restrictions.or(employeeName, criterion);
				criteria.add(Restrictions.or(techCertType, criterion1));
				//criteria.add(criterion1);
			}
			criteria.addOrder(Order.desc("employee.employeeId"));
		return criteria.list();
		
	}
	public Map<String, Object> getAllVisaDetails(Integer countryId, Long visaTypeId, String multipleSearch) {
		
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(VisaDetails.class);
			criteria.createAlias("employee", "employee");
			criteria.add(Restrictions.and(Restrictions.ne("employee.employeeId", 1000L),
					Restrictions.eq("employee.statusName", "Active")));
			criteria.createAlias("visaLookUp", "visaType");
			
			if(visaTypeId != null) {
				criteria.add(Restrictions.eq("visaType.id", visaTypeId));
			}
			
			if(countryId != null) {
				criteria.createAlias("visaType.country", "country");
				criteria.add(Restrictions.eq("country.id", countryId));
			}
			
			if(!multipleSearch.isEmpty()) {
				criteria.add(Restrictions.or(Restrictions.ilike("employee.employeeFullName", multipleSearch,MatchMode.ANYWHERE),
						Restrictions.ilike("visaType.visaType", multipleSearch)));
			}
			
			criteria.addOrder(Order.asc("employee.employeeId"));
			

			Map<String, Object> employeeMap = new HashMap<String, Object>();
			employeeMap.put("list", criteria.list());
			employeeMap.put("size",criteria.list().size());
			
			return employeeMap;
		
	}

	@Override
	public List<CertificateType> getCertificateTypeList() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CertificateType.class);
		criteria.addOrder(Order.desc("id"));
		return criteria.list();
	}

	@Override
	public List<CertificateType> getCertificateByTechnology(String technologyName) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CertificateType.class);
		criteria.createAlias("technology", "technology");
		criteria.add(Restrictions.eq("technology.name", technologyName));
		return criteria.list();
		
	}

	@Override
	public Boolean checkForDuplicateCertificate(Long technologyId, String certificateType) {
		Boolean flag = null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CertificateType.class);
		criteria.createAlias("technology", "technology");
		criteria.add(Restrictions.and(Restrictions.eq("technology.id", technologyId),
						Restrictions.like("certificateType", certificateType, MatchMode.EXACT)));
		
		
		if(criteria.list().size() > 0) {
			flag = Boolean.TRUE;
		}
		else {
			flag = Boolean.FALSE;
		}
		return flag;
	}



}
