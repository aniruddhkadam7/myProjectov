package com.raybiztech.contact.service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.icu.text.SimpleDateFormat;
import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.observation.exceptions.DuplicateObservationException;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.assetmanagement.dto.ManufacturerDto;
import com.raybiztech.checklist.builder.ChecklistBuilder;
import com.raybiztech.checklist.business.ChecklistSection;
import com.raybiztech.checklist.dto.ChecklistSectionDTO;
import com.raybiztech.checklist.service.ChecklistService;
import com.raybiztech.contact.builder.ContactInfoBuilder;
import com.raybiztech.contact.business.ContactInfo;
import com.raybiztech.contact.dao.ContactInfoDAO;
import com.raybiztech.contact.dto.ContactInfoDTO;
import com.raybiztech.date.Second;
import com.raybiztech.handbook.builder.HandbookItemBuilder;
import com.raybiztech.handbook.business.HandbookItem;
import com.raybiztech.handbook.dao.HandbookItemDAO;
import com.raybiztech.handbook.dto.HandbookItemDTO;
import com.raybiztech.rolefeature.business.Permission;

@Service
public class ContactInfoServiceImpl implements ContactInfoService {

	Logger logger = Logger.getLogger(ContactInfoServiceImpl.class);

	@Autowired
	ContactInfoDAO dao;

	@Autowired
	ContactInfoBuilder builder;

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	ChecklistService ChecklistService;

	@Autowired
	ChecklistBuilder checklistBuilder;

	@Override
	public ContactInfoDTO get(String pageName) {

		return builder.getDto(dao.findByPageName(ContactInfo.class, pageName));
		// return builder.getDto(dao.findBy(HandbookItem.class, pageName));
	}

	@Override
	public Map<String, Object> getAll(Integer startIndex, Integer endIndex) {
		Map<String, Object> map = securityUtils.getLoggedEmployeeDetailsSecurityContextHolder();
		Map<String, Object> finalMap = new HashMap<String, Object>();
		Map<String, Object> handbookMap = null;
		Employee loggedInEmployee = (Employee) map.get("employee");

		Permission totalListPermission = dao.checkForPermission("All CheckList", loggedInEmployee);
		Permission departmentPermission = dao.checkForPermission("Department Wise Checklist", loggedInEmployee);
		
		if(totalListPermission!=null && totalListPermission.getView()){
			   handbookMap = dao.getAll(startIndex, endIndex);
		}else{
			if(departmentPermission!=null && departmentPermission.getView()){
			handbookMap = dao.getDepartmentsWiseChecklist(loggedInEmployee.getDepartmentName(),startIndex, endIndex);
			}
			}
			
		
		List<ContactInfo> handBookList = (List) handbookMap.get("list");
		Integer noOfRecords = (Integer) handbookMap.get("size");
		List<ContactInfoDTO> dtosList = builder.getDtoList(handBookList);
		Map<String, Object> hbookMap = new HashMap<String, Object>();
		hbookMap.put("list", dtosList);
		hbookMap.put("size", noOfRecords);

		return hbookMap;

	}

	@Override
	public void add(ContactInfoDTO item) {
		Boolean duplication = dao.duplicatePageTitleCheck(item);
		if (!duplication) {
			ContactInfo handbookItem = builder.getEntity(item);
			dao.save(handbookItem);
		} else {
			throw new DuplicateObservationException("The contact details were already added.");
		}
	}

	@Override
	public void update(ContactInfoDTO item) {

		HandbookItem handbookItem = dao.findBy(HandbookItem.class, item.getId());
		handbookItem.setDescription(item.getDescription());
		dao.update(handbookItem);

	}

	@Override
	public void delete(int id) {

		ContactInfo item = dao.findBy(ContactInfo.class, id);
		dao.delete(item);
	}

	/*
	 * @Override public boolean duplicatePageTitleCheck(HandbookItemDTO book) {
	 * 
	 * return dao.duplicatePageTitleCheck(book); }
	 */

	@Override
	public void updateHandbookItem(ContactInfoDTO contactInfoDTO) {
		Boolean duplication = dao.duplicatePageTitleCheckUpdate(contactInfoDTO);
		SimpleDateFormat formatter = new SimpleDateFormat("dd:MM:yyyy hh:mm a");
		if (!duplication) {
            Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
            Employee employee = dao.findBy(Employee.class, loggedInEmpId);
            
			ContactInfo handbookItem = dao.findBy(ContactInfo.class, contactInfoDTO.getId());
			handbookItem.setId(contactInfoDTO.getId());
			handbookItem.setTitle(contactInfoDTO.getTitle());
			handbookItem.setPageName(contactInfoDTO.getPageName());
			handbookItem.setType(contactInfoDTO.getType());
			//for description
			handbookItem.setDescription(contactInfoDTO.getDescription());
			 if (contactInfoDTO.getDepartmentId() != null) {
                 handbookItem.setEmpDepartment(
                		 contactInfoDTO.getDepartmentId() != null ? dao.findBy(EmpDepartment.class, contactInfoDTO.getDepartmentId())
                                                 : dao.findByUniqueProperty(EmpDepartment.class, "departmentName",
                                                                 employee.getDepartmentName()));
               

         }
			handbookItem.setUserName(employee.getFullName());
			String s = formatter.format(new Date());
			Date date = null;
			try {
				date = formatter.parse(s);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handbookItem.setUpdatedDate(date);
			dao.update(handbookItem);
		} else {
			throw new DuplicateObservationException("The hand book details was already added.");
		}
	}

	@Override
	public void addCheckListPage(ContactInfoDTO checkList) {

		Boolean duplication = dao.duplicateCheckList(checkList);
		if (!duplication) {
			ContactInfo checkListData = builder.getEntity(checkList);
			dao.save(checkListData);
		} else {
			throw new DuplicateObservationException("The check list details was already added.");
		}

	}

	@Override
	public Map<String, Object> getAllCheckList(Integer startIndex, Integer endIndex) {

		Map<String, Object> checkListMap = null;
		checkListMap = dao.getAllCheckList(startIndex, endIndex);
		List<ContactInfo> handBookList = (List) checkListMap.get("list");
		Integer noOfRecords = (Integer) checkListMap.get("size");
		List<ContactInfoDTO> dtosList = builder.getDtoList(handBookList);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", dtosList);
		map.put("size", noOfRecords);

		return map;

	}

	@Override
	public void updateCheckList(ContactInfoDTO checkList) {

		Boolean duplication = dao.duplicateCheckListForUpdate(checkList);
		if (!duplication) {
			ContactInfo checkLists = dao.findBy(ContactInfo.class, checkList.getId());
			checkLists.setTitle(checkList.getTitle());
			checkLists.setPageName(checkList.getPageName());
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

		ContactInfo item = dao.findBy(ContactInfo.class, checkListId);
		dao.delete(item);
	}

	@Override
	public List<ChecklistSectionDTO> getChangedDepartmentList(Long departmentId) {
		return ChecklistService.getSections(departmentId);
	}

	@Override
	public ContactInfoDTO getTittle(String title) {
		
		return builder.getDto(dao.findByPageTittle(title));
	}

	@Override
	public List<ContactInfoDTO> getTotalHandbookList() {
		List<ContactInfo> totalHandbooklist = dao.getTotalHandbookList();
		 List<ContactInfoDTO> totaldtoList = builder.getDtoList(totalHandbooklist);
		return totaldtoList;
	}

	
}