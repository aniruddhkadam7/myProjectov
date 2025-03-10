package com.raybiztech.handbook.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.dto.VisaDetailDTO;
import com.raybiztech.appraisals.observation.exceptions.DuplicateObservationException;
import com.raybiztech.assetmanagement.dto.ProductDto;
import com.raybiztech.checklist.dto.ChecklistSectionDTO;
import com.raybiztech.handbook.dto.HandbookItemDTO;
import com.raybiztech.handbook.service.HandbookItemService;
import com.raybiztech.projectmanagement.invoice.dto.CountryLookUpDTO;
import com.raybiztech.supportmanagement.dto.TicketsCategoryDTO;

@Controller
@RequestMapping("/handbookItem")
public class HandbookItemController {

	Logger logger = Logger.getLogger(HandbookItemController.class);

	@Autowired
	HandbookItemService service;

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody HandbookItemDTO get(@RequestParam String pageName) {

		return service.get(pageName);
	}

	@RequestMapping(value = "/getAll", params = { "startIndex", "endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAll(@RequestParam Integer startIndex, @RequestParam Integer endIndex) {

		return service.getAll(startIndex, endIndex);
	}

	@RequestMapping(value = "/add",params={"list"}, method = RequestMethod.POST)
	public @ResponseBody void add(@RequestBody HandbookItemDTO book,@RequestParam List<Integer> list,HttpServletResponse value) {
		try {
			service.add(book,list);
		} catch (DuplicateObservationException ex) {
			value.setStatus(HttpServletResponse.SC_CONFLICT);
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody void update(@RequestBody HandbookItemDTO book) {

		service.update(book);
	}

	@RequestMapping(value = "/delete", params = { "bookId" }, method = RequestMethod.DELETE)
	public @ResponseBody void delete(@RequestParam Integer bookId) {

		service.delete(bookId);
	}

	/*
	 * @RequestMapping(value = "/duplicatecheck", method = RequestMethod.POST)
	 * 
	 * @ResponseBody public boolean duplicatePageTitleCheck(@RequestBody
	 * HandbookItemDTO book) { return service.duplicatePageTitleCheck(book); }
	 */

	// To Update Category in data base
	@RequestMapping(value = "/updateHandbookItem",params={"list"}, method = RequestMethod.PUT)
	public @ResponseBody void updateHandbookItem(@RequestBody HandbookItemDTO handbookItemDTO, @RequestParam List<Integer> list,
			HttpServletResponse value) {
		try {
			service.updateHandbookItem(handbookItemDTO,list);
		} catch (DuplicateObservationException ex) {
			value.setStatus(HttpServletResponse.SC_CONFLICT);
		}
	}

	@RequestMapping(value = "/addCheckListPage", method = RequestMethod.POST)
	public @ResponseBody void addCheckListPage(@RequestBody HandbookItemDTO checkList, HttpServletResponse value) {
		try {
			service.addCheckListPage(checkList);
		} catch (DuplicateObservationException ex) {
			value.setStatus(HttpServletResponse.SC_CONFLICT);
		}
	}

	/*
	 * @RequestMapping(value = "/duplicateCheckList", method = RequestMethod.POST)
	 * 
	 * @ResponseBody public boolean duplicateCheckList(@RequestBody HandbookItemDTO
	 * checkList) { return service.duplicateCheckList(checkList); }
	 */

	@RequestMapping(value = "/getAllCheckList", params = { "startIndex", "endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAllCheckList(@RequestParam Integer startIndex,
			@RequestParam Integer endIndex) {

		return service.getAllCheckList(startIndex, endIndex);
	}

	@RequestMapping(value = "/updateCheckList", method = RequestMethod.PUT)
	public @ResponseBody void updateCheckList(@RequestBody HandbookItemDTO checkList) {
		service.updateCheckList(checkList);
	}

	@RequestMapping(value = "/getSectionsByDeptId", method = RequestMethod.GET)
	public @ResponseBody List<ChecklistSectionDTO> getSectionsByDeptId() {
		return service.getSectionsByDeptId();
	}

	@RequestMapping(value = "/getEmpDepartments", method = RequestMethod.GET)
	public @ResponseBody List<EmpDepartment> getEmpDepartments() {
		return service.getEmpDepartments();
	}

	@RequestMapping(value = "/deleteCheckList", params = { "checkListId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteCheckList(@RequestParam Integer checkListId) {

		service.deleteCheckList(checkListId);
	}

	@RequestMapping(value = "/getChangedDepartmentList", params = { "departmentId" }, method = RequestMethod.GET)
	public @ResponseBody List<ChecklistSectionDTO> getChangedDepartmentList(@RequestParam Long departmentId) {
		return service.getChangedDepartmentList(departmentId);

	}

	@RequestMapping(value = "/getTittle", method = RequestMethod.GET)
	public @ResponseBody HandbookItemDTO getTittle(@RequestParam String title) {

		return service.getTittle(title);
	}

	@RequestMapping(value = "/updateCheckListPage", method = RequestMethod.POST)
	public @ResponseBody void updateCheckListPage(@RequestBody HandbookItemDTO checklist) {

		service.update(checklist);
	}
	@RequestMapping(value = "/getTotalHandbookList" , method = RequestMethod.GET)
	public @ResponseBody List<HandbookItemDTO> getTotalHandbookList(){
		return service.getTotalHandbookList();
		
	}
	@RequestMapping(value = "/getHandbookNamesCountryWise" , method = RequestMethod.GET)
	public @ResponseBody List<HandbookItemDTO> getHandbookNamesCountryWise(){
		return service.getHandbookNamesCountryWise();
		
	}
	@RequestMapping(value = "/getSelectedCountries" ,params={ "id" },method = RequestMethod.GET)
	public @ResponseBody List<CountryLookUpDTO> getSelectedCountries(@RequestParam Integer id){
		return service.getSelectedCountries(id);
		
	}
	
	@RequestMapping(value = "/getTotalList" , method = RequestMethod.GET)
	public @ResponseBody List<HandbookItemDTO> getTotalList(){
		return service.getTotalList();
		
	}
}
