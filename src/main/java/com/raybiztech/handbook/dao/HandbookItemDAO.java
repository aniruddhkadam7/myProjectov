package com.raybiztech.handbook.dao;

import java.util.List;
import java.util.Map;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.handbook.business.HandbookCountry;
import com.raybiztech.handbook.business.HandbookItem;
import com.raybiztech.handbook.dto.HandbookItemDTO;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;

public interface HandbookItemDAO extends DAO {

	Map<String, Object> getAll(Integer startIndex, Integer endIndex);

	boolean duplicatePageTitleCheck(HandbookItemDTO book);

	boolean duplicatePageTitleCheckUpdate(HandbookItemDTO book);

	Map<String, Object> getAllCheckList(Integer startIndex, Integer endIndex);

	boolean duplicateCheckList(HandbookItemDTO checkList);

	boolean duplicateCheckListForUpdate(HandbookItemDTO checkList);

	List<EmpDepartment> getDepartmentList(Employee employee);
	
	HandbookItem findByPageTittle(String title);
	
	List<CountryLookUp> getlookuplist(List<Integer> list);
	
	//for getting whole list
	List<HandbookItem> getTotalHandbookList();
	
	List<HandbookCountry> getHandbookList(HandbookItem handbook);
	
	List<HandbookCountry> getList(List<HandbookItem> handbook,String country);

}
