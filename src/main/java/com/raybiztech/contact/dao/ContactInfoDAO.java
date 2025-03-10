package com.raybiztech.contact.dao;

import java.util.List;
import java.util.Map;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.contact.business.ContactInfo;
import com.raybiztech.contact.dto.ContactInfoDTO;

public interface ContactInfoDAO extends DAO {

	Map<String, Object> getAll(Integer startIndex, Integer endIndex);
	
	Map<String, Object> getDepartmentsWiseChecklist(String dept,Integer startIndex, Integer endIndex);

	boolean duplicatePageTitleCheck(ContactInfoDTO book);

	boolean duplicatePageTitleCheckUpdate(ContactInfoDTO book);

	Map<String, Object> getAllCheckList(Integer startIndex, Integer endIndex);

	boolean duplicateCheckList(ContactInfoDTO checkList);

	boolean duplicateCheckListForUpdate(ContactInfoDTO checkList);

	List<EmpDepartment> getDepartmentList(Employee employee);
	
	ContactInfo findByPageTittle(String title);
	
	//for getting whole list
	List<ContactInfo> getTotalHandbookList();

}
