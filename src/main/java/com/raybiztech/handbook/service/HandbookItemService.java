package com.raybiztech.handbook.service;

import java.util.List;
import java.util.Map;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.assetmanagement.dto.ProductDto;
import com.raybiztech.checklist.dto.ChecklistSectionDTO;
import com.raybiztech.handbook.dto.HandbookItemDTO;
import com.raybiztech.projectmanagement.invoice.dto.CountryLookUpDTO;

public interface HandbookItemService {
	HandbookItemDTO get(String pageName);

	Map<String, Object> getAll(Integer startIndex, Integer endIndex);

	void add(HandbookItemDTO book, List<Integer> list);

	void update(HandbookItemDTO book);

	void delete(int id);

	//boolean duplicatePageTitleCheck(HandbookItemDTO book);

	void updateHandbookItem(HandbookItemDTO handBookItem, List<Integer> list);

	void addCheckListPage(HandbookItemDTO checkList);

	Map<String, Object> getAllCheckList(Integer startIndex, Integer endIndex);

	void updateCheckList(HandbookItemDTO checkList);

	List<ChecklistSectionDTO> getSectionsByDeptId();

	List<EmpDepartment> getEmpDepartments();

	void deleteCheckList(Integer checkListId);

	List<ChecklistSectionDTO> getChangedDepartmentList(Long departmentId);

	HandbookItemDTO getTittle(String title);
	
	//for getting total handbook list
	List<HandbookItemDTO> getTotalHandbookList();
	
	List<CountryLookUpDTO> getSelectedCountries(Integer id);
	
	List<HandbookItemDTO> getTotalList();

	List<HandbookItemDTO> getHandbookNamesCountryWise();


}
