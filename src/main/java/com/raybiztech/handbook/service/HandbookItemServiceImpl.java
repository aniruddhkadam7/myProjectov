package com.raybiztech.handbook.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.observation.exceptions.DuplicateObservationException;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.assetmanagement.dto.ManufacturerDto;
import com.raybiztech.checklist.builder.ChecklistBuilder;
import com.raybiztech.checklist.business.ChecklistSection;
import com.raybiztech.checklist.dto.ChecklistSectionDTO;
import com.raybiztech.checklist.service.ChecklistService;
import com.raybiztech.handbook.builder.HandbookItemBuilder;
import com.raybiztech.handbook.business.HandbookCountry;
import com.raybiztech.handbook.business.HandbookItem;
import com.raybiztech.handbook.dao.HandbookItemDAO;
import com.raybiztech.handbook.dto.HandbookItemDTO;
import com.raybiztech.projectmanagement.invoice.dto.CountryLookUpDTO;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;

@Service
public class HandbookItemServiceImpl implements HandbookItemService {

	Logger logger = Logger.getLogger(HandbookItemServiceImpl.class);

	@Autowired
	HandbookItemDAO dao;

	@Autowired
	HandbookItemBuilder builder;

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	ChecklistService ChecklistService;

	@Autowired
	ChecklistBuilder checklistBuilder;

	@Override
	public HandbookItemDTO get(String pageName) {

		return builder.getDto(dao.findByPageName(HandbookItem.class, pageName));
		// return builder.getDto(dao.findBy(HandbookItem.class, pageName));
	}

	@Override
	public Map<String, Object> getAll(Integer startIndex, Integer endIndex) {

		Map<String, Object> handbookMap = null;
		handbookMap = dao.getAll(startIndex, endIndex);
		List<HandbookItem> handBookList = (List) handbookMap.get("list");
		Integer noOfRecords = (Integer) handbookMap.get("size");
		List<HandbookItemDTO> dtosList = builder.getDtoList(handBookList);
		Map<String, Object> hbookMap = new HashMap<String, Object>();
		hbookMap.put("list", dtosList);
		hbookMap.put("size", noOfRecords);

		return hbookMap;

	}

	@Override
	public void add(HandbookItemDTO item,List<Integer>list) {
		HandbookItem handbookItem =null;
		Boolean duplication = dao.duplicatePageTitleCheck(item);
		List<CountryLookUp> list1 = dao.getlookuplist(list);
		HandbookCountry handCountry = new HandbookCountry();
		if (!duplication) {
			 handbookItem = builder.getEntity(item);
			dao.save(handbookItem);
			for(CountryLookUp clist : list1){
	        	CountryLookUp country = dao.findBy(CountryLookUp.class, clist.getId());
		        CountryLookUp Country = new CountryLookUp();
		        Country.setId(country.getId());
		        Country.setName(country.getName());
		        handCountry.setCountry(Country);
		        handCountry.setHandbook(handbookItem);
		         dao.save(handCountry);
		}
		} else {
			throw new DuplicateObservationException("The hand book details was already added.");
		}
	}

	@Override
	public void update(HandbookItemDTO item) {

		HandbookItem handbookItem = dao.findBy(HandbookItem.class, item.getId());
		handbookItem.setDescription(item.getDescription());
		dao.update(handbookItem);

	}

	@Override
	public void delete(int id) {

		HandbookItem item = dao.findBy(HandbookItem.class, id);
		List<HandbookCountry> details = dao.getHandbookList(item);
		for(HandbookCountry han: details){
			HandbookCountry  handbook = dao.findBy(HandbookCountry.class, han.getId());
			HandbookCountry hand = new HandbookCountry();
			hand.setId(han.getId());
			dao.delete(hand);
		}
		dao.delete(item);
	}

	/*
	 * @Override public boolean duplicatePageTitleCheck(HandbookItemDTO book) {
	 * 
	 * return dao.duplicatePageTitleCheck(book); }
	 */

	@Override
	public void updateHandbookItem(HandbookItemDTO handBookItemDto, List<Integer> list) {
		HandbookCountry handcountry = new HandbookCountry();
		HandbookItem handbookItem = dao.findBy(HandbookItem.class, handBookItemDto.getId());
		List<HandbookCountry> details = dao.getHandbookList(handbookItem);
        List<CountryLookUp> list1 = dao.getlookuplist(list);
		Boolean duplication = dao.duplicatePageTitleCheckUpdate(handBookItemDto);
		if (!duplication) {
			handbookItem.setId(handBookItemDto.getId());
			handbookItem.setTitle(handBookItemDto.getTitle());
			handbookItem.setPageName(handBookItemDto.getPageName());
			handbookItem.setDisplayOrder(handBookItemDto.getDisplayOrder());
			handbookItem.setType(handBookItemDto.getType());
			//for description
			handbookItem.setDescription(handBookItemDto.getDescription());
			dao.update(handbookItem);
			System.out.println("Handbbok item updated");
			
		} else {
			throw new DuplicateObservationException("The hand book details was already added.");
		}
		 for(HandbookCountry han: details){
	     	HandbookCountry  handbook = dao.findBy(HandbookCountry.class, han.getId());
	     	HandbookCountry hand = new HandbookCountry();
	     	hand.setId(han.getId());
	     	dao.delete(hand);/*
		    System.out.println("id:" + handbook.getId() +"//handbook:" + handbook.getHandbook().getId() + "//country:" + handbook.getCountry().getId());
		    dao.delete(handbook.getHandbook());
		    dao.delete(handbook.getCountry());
		    dao.delete(handbook);*/
	         }
           for(CountryLookUp clist : list1){
                  CountryLookUp Country = new CountryLookUp();
                  Country.setId(clist.getId());
                  Country.setName(clist.getName());
                  handcountry.setCountry(Country);
                  handcountry.setHandbook(handbookItem);
                  System.out.println("adding:" + handbookItem.getId() + "//coun:"+Country.getId() + Country.getName());
                  dao.save(handcountry);
             }
	}

	@Override
	public void addCheckListPage(HandbookItemDTO checkList) {

		Boolean duplication = dao.duplicateCheckList(checkList);
		if (!duplication) {
			HandbookItem checkListData = builder.getEntity(checkList);
			dao.save(checkListData);
		} else {
			throw new DuplicateObservationException("The check list details was already added.");
		}

	}

	@Override
	public Map<String, Object> getAllCheckList(Integer startIndex, Integer endIndex) {

		Map<String, Object> checkListMap = null;
		checkListMap = dao.getAllCheckList(startIndex, endIndex);
		List<HandbookItem> handBookList = (List) checkListMap.get("list");
		Integer noOfRecords = (Integer) checkListMap.get("size");
		List<HandbookItemDTO> dtosList = builder.getDtoList(handBookList);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", dtosList);
		map.put("size", noOfRecords);

		return map;

	}

	@Override
	public void updateCheckList(HandbookItemDTO checkList) {

		Boolean duplication = dao.duplicateCheckListForUpdate(checkList);
		if (!duplication) {
			HandbookItem checkLists = dao.findBy(HandbookItem.class, checkList.getId());
			checkLists.setTitle(checkList.getTitle());
			checkLists.setPageName(checkList.getPageName());
			checkLists.setDisplayOrder(checkList.getDisplayOrder());
			dao.update(checkLists);
		} else {
			throw new DuplicateObservationException("The check list details was already added.");
		}
	}

	@Override
	public List<ChecklistSectionDTO> getSectionsByDeptId() {

		Employee employee = dao.findBy(Employee.class, securityUtils.getLoggedEmployeeIdforSecurityContextHolder());
		EmpDepartment empDepartment = dao.findByUniqueProperty(EmpDepartment.class, "departmentName",
				employee.getDepartmentName());

		return ChecklistService.getSections(empDepartment.getDepartmentId());

	}

	@Override
	public List<EmpDepartment> getEmpDepartments() {

		Employee employee = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder().get("employee");

		return dao.getDepartmentList(employee);
	}

	@Override
	public void deleteCheckList(Integer checkListId) {

		HandbookItem item = dao.findBy(HandbookItem.class, checkListId);
		dao.delete(item);
	}

	@Override
	public List<ChecklistSectionDTO> getChangedDepartmentList(Long departmentId) {
		return ChecklistService.getSections(departmentId);
	}

	@Override
	public HandbookItemDTO getTittle(String title) {
		
		return builder.getDto(dao.findByPageTittle(title));
	}

	@Override
	public List<HandbookItemDTO> getTotalHandbookList() {
		List<HandbookItem> totalHandbooklist = dao.getTotalHandbookList();
		Employee employee = dao.findBy(Employee.class, securityUtils.getLoggedEmployeeIdforSecurityContextHolder());
		List<HandbookCountry> country = dao.getList(totalHandbooklist, employee.getCountry());
		 List<HandbookItemDTO> totaldtoList = builder.getdtoList(country);
		return totaldtoList;
	}
	@Override
	public List<CountryLookUpDTO> getSelectedCountries(Integer id) {
		 List<CountryLookUpDTO> totaldtoList = builder.getCountriesList(id);
		return totaldtoList;
	}
	@Override
	public List<HandbookItemDTO> getTotalList() {
		List<HandbookItem> totalHandbooklist = dao.getTotalHandbookList();
		 List<HandbookItemDTO> totaldtoList = builder.getDtoList(totalHandbooklist);
		return totaldtoList;
	}

	@Override
	public List<HandbookItemDTO> getHandbookNamesCountryWise() {
		List<HandbookItem> totalHandbooklist = dao.getTotalHandbookList();
		Employee employee = dao.findBy(Employee.class, securityUtils.getLoggedEmployeeIdforSecurityContextHolder());
		List<HandbookCountry> country = dao.getList(totalHandbooklist, employee.getCountry());
		List<HandbookItemDTO> list = new ArrayList<HandbookItemDTO>();
		for(HandbookCountry hancon : country){
			HandbookItem han =dao.findBy(HandbookItem.class, hancon.getHandbook().getId());
			System.out.println("han:" + han.getId() + han.getPageName() + han.getTitle());
			HandbookItemDTO handbook = new HandbookItemDTO();
			handbook.setTitle(han.getTitle());
			handbook.setPageName(han.getPageName());
			list.add(handbook);
					
		}
		 //List<HandbookItemDTO> totaldtoList = builder.getlist(country);
		return list;
	}
	
}