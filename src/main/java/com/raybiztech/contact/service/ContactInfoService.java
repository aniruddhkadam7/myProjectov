package com.raybiztech.contact.service;

import java.util.List;
import java.util.Map;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.assetmanagement.dto.ProductDto;
import com.raybiztech.checklist.dto.ChecklistSectionDTO;
import com.raybiztech.contact.dto.ContactInfoDTO;

public interface ContactInfoService {
	ContactInfoDTO get(String pageName);

	Map<String, Object> getAll(Integer startIndex, Integer endIndex);

	void add(ContactInfoDTO book);

	void update(ContactInfoDTO book);

	void delete(int id);

	//boolean duplicatePageTitleCheck(HandbookItemDTO book);

	void updateHandbookItem(ContactInfoDTO handBookItem);

	void addCheckListPage(ContactInfoDTO checkList);

	Map<String, Object> getAllCheckList(Integer startIndex, Integer endIndex);

	void updateCheckList(ContactInfoDTO checkList);

	List<ChecklistSectionDTO> getSectionsByDeptId();

	List<EmpDepartment> getEmpDepartments();

	void deleteCheckList(Integer checkListId);

	List<ChecklistSectionDTO> getChangedDepartmentList(Long departmentId);

	ContactInfoDTO getTittle(String title);
	
	//for getting total handbook list
	List<ContactInfoDTO> getTotalHandbookList();


}
