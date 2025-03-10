/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.raybiztech.achievement.business.Leadership;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;
import com.raybiztech.recruitment.business.CandidateInterviewCycle;
import com.raybiztech.rolefeature.business.Feature;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.rolefeature.business.URIAndFeatures;

@Service
public interface DAO {

	<T extends Serializable> Serializable save(T object);

	<T extends Serializable> void saveOrUpdate(T object);

	<T extends Serializable> void update(T object);

	<T extends Serializable> T findByKRAName(Class<T> clazz, Serializable name);

	<T extends Serializable> T findByKPIName(Class<T> clazz, Serializable name);

	<T extends Serializable> List<T> findByManagerName(Class<T> clazz);

	<T extends Serializable> T findByDesignationName(Class<T> clazz,
			Serializable name);

	<T extends Serializable> T findBy(Class<T> clazz, Serializable id);

	SessionFactory getSessionFactory();

	<T extends Serializable> List<T> get(Class<T> clazz);

	<T extends Serializable> void delete(T object);

	<T> List<String> getByProperty(Class<T> clazz, String name);

	<T extends Serializable> T findByEmployeeMailId(Class<T> clazz, String id);

	<T extends Serializable> T findByCategoryTypeName(Class<T> clazz,
			Serializable name);

	<T extends Serializable> List<T> findCandidateInterviewCycles(
			Class<T> clazz, Serializable name);

	<T extends Serializable> T findByPageName(Class<T> clazz, Serializable name);

	<T extends Serializable> T findByEmployeeName(Class<T> clazz,
			String username);

	<T extends Serializable> T findByName(Class<T> clazz, Serializable name);

	<T extends Serializable> T findByActiveEmployeeName(Class<T> clazz,
			String username);

	<T extends Serializable> T findByJobVacancyByCode(Class<T> clazz,
			String jobCode);

	<T extends Serializable> T findByUniqueProperty(Class<T> clazz,
			String propertyName, Serializable name);

	public <T extends Serializable> List<T> getAllOfProperty(Class<T> clazz,
			String propertyName, Serializable name);

	public List<Long> getRolePermissions(Long id);

	public List<URIAndFeatures> getUrlId(String url, String method);

	public Permission getPermissionType(Long roleid, Long uriid);

	public List<CandidateInterviewCycle> getAllInterviewCylesUnderEmployee(
			Long empid);

	Map<String, Date> getCustomDates(String datePeriod);

	public Permission checkForPermission(String featureName, Employee employee);

	public List<Feature> getChildFeatures(Long id);

	Map<String, List<Audit>> getPairValue(List<Audit> list);
 
	Map<String, List<Audit>> getAudit(Long projectId, String tableName);

	List<Employee> mangerUnderManager(Long empId);

	<T extends Serializable> List<T> updateHierarchyReportingManager(
			String oldmanager, String employee, Class<T> clazz,
			String propertyName, String propertyName2, String propertyName3);

	<T extends Serializable> List<T> updateHierarchyReportingManagerForAppraisal(
			String oldmanager, String employee, Class<T> clazz,
			String managesList);

	List<Employee> getReportiesUnderManager(List<Long> empIds);
	
	Map<String, Object> getReporteeExportList(List<Long> empIds);
	

	Map<String, Object> getPaginationList(Criteria criteria,Integer fromIndex , Integer toIndex); 
	
	/*here Hr LookUps Method*/
	List<Employee> getHrLookUp();

	List<Project> mangerUnderProjectPeoples(Long managerId);

	List<AllocationDetails> getAllocationDetialsForProjects(
			List<Long> projectIds);

	Employee getlastEmployeeId();

	List<Leadership> checkLeadershipDuplication(Long employeeId);

	List<CountryLookUp> getEmpCountries();
}
