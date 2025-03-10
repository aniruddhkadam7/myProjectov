package com.raybiztech.handbook.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.checklist.business.ChecklistSection;
import com.raybiztech.handbook.business.HandbookCountry;
import com.raybiztech.handbook.business.HandbookItem;
import com.raybiztech.handbook.dao.HandbookItemDAO;
import com.raybiztech.handbook.dto.HandbookItemDTO;
import com.raybiztech.projectmanagement.invoice.dto.CountryLookUpDTO;

@Component("handbookBuilder")
public class HandbookItemBuilder {

	@Autowired
	DAO dao;
	@Autowired
	HandbookItemDAO handbookDao;
	@Autowired
	SecurityUtils securityUtils;

	public HandbookItemDTO getDto(HandbookItem entity) {
		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		List<CountryLookUpDTO> dtoList = new ArrayList<CountryLookUpDTO>();
		HandbookItem handbook = dao.findBy(HandbookItem.class, entity.getId());
		List<HandbookCountry> country = handbookDao.getHandbookList(handbook);
		HandbookItemDTO dto = null;
		if (entity != null) {
			dto = new HandbookItemDTO();
			dto.setId(entity.getId());
			dto.setTitle(entity.getTitle());
			dto.setDescription(entity.getDescription());
			dto.setDisplayOrder(entity.getDisplayOrder());
			dto.setEmpCountry(employee.getCountry()!=null?employee.getCountry():null);
			if (entity.getPageName() != null) {
				dto.setPageName(entity.getPageName());
			}
			dto.setType(entity.getType());
			/*
			 * if (entity.getEmpDepartment() == null) {
			 * dto.setDepartmentName(entity.getEmpDepartment().getDepartmentName());
			 * dto.setDepartmentId(entity.getEmpDepartment().getDepartmentId()); }
			 */
			if (entity.getEmpDepartment() != null) {
				dto.setDepartmentName(entity.getEmpDepartment().getDepartmentName());
				dto.setDepartmentId(entity.getEmpDepartment().getDepartmentId());

			}
			if (entity.getSection() != null) {
				dto.setSectionId(entity.getSection().getChecklistsectionId());
				dto.setSectionName(entity.getSection().getSectionName());
			}
			for(HandbookCountry han : country){
				dtoList.add(dto(han));
			}
			dto.setHandCountry(dtoList);
			
			
		}
		return dto;
	}
	public HandbookItemDTO getdto(HandbookCountry entity) {
		List<CountryLookUpDTO> dtoList = new ArrayList<CountryLookUpDTO>();
		HandbookItem handbook = dao.findBy(HandbookItem.class, entity.getHandbook().getId());
		List<HandbookCountry> country = handbookDao.getHandbookList(handbook);
		HandbookItemDTO dto = null;
		if (entity != null) {
			dto = new HandbookItemDTO();
			dto.setId(entity.getHandbook().getId());
			dto.setTitle(entity.getHandbook().getTitle());
			dto.setDescription(entity.getHandbook().getDescription());
			dto.setDisplayOrder(entity.getHandbook().getDisplayOrder());
			if (entity.getHandbook().getPageName() != null) {
				dto.setPageName(entity.getHandbook().getPageName());
			}
			dto.setType(entity.getHandbook().getType());
			/*
			 * if (entity.getEmpDepartment() == null) {
			 * dto.setDepartmentName(entity.getEmpDepartment().getDepartmentName());
			 * dto.setDepartmentId(entity.getEmpDepartment().getDepartmentId()); }
			 */
			if (entity.getHandbook().getEmpDepartment() != null) {
				dto.setDepartmentName(entity.getHandbook().getEmpDepartment().getDepartmentName());
				dto.setDepartmentId(entity.getHandbook().getEmpDepartment().getDepartmentId());

			}
			if (entity.getHandbook().getSection() != null) {
				dto.setSectionId(entity.getHandbook().getSection().getChecklistsectionId());
				dto.setSectionName(entity.getHandbook().getSection().getSectionName());
			}
			for(HandbookCountry han : country){
				dtoList.add(dto(han));
			}
			dto.setHandCountry(dtoList);
			
		}
		return dto;
	}

	public HandbookItem getEntity(HandbookItemDTO dto) {
		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		HandbookItem entity = null;
		if (dto != null) {
			entity = new HandbookItem();
			entity.setId(dto.getId());
			entity.setTitle(dto.getTitle());
			entity.setDescription(dto.getDescription());
			entity.setDisplayOrder(dto.getDisplayOrder());
			if (dto.getPageName() != null) {
				entity.setPageName(dto.getPageName());
			}
			entity.setType(dto.getType());
			if (dto.getDepartmentId() != null) {
				entity.setEmpDepartment(
						dto.getDepartmentId() != null ? dao.findBy(EmpDepartment.class, dto.getDepartmentId())
								: dao.findByUniqueProperty(EmpDepartment.class, "departmentName",
										employee.getDepartmentName()));

			}
			if (dto.getSectionId() != null) {
				entity.setSection(dto.getSectionId() != null ? dao.findBy(ChecklistSection.class, dto.getSectionId())
						: dao.findByUniqueProperty(ChecklistSection.class, "sectionName", dto.getSectionName()));
			}
		}
		return entity;
	}

	public List<HandbookItemDTO> getDtoList(List<HandbookItem> entityList) {
		List<HandbookItemDTO> dtoList = new ArrayList<HandbookItemDTO>();

		for (HandbookItem entity : entityList) {
			dtoList.add(getDto(entity));
		}

		return dtoList;
	}
	public List<HandbookItemDTO> getdtoList(List<HandbookCountry> entityList) {
		List<HandbookItemDTO> dtoList = new ArrayList<HandbookItemDTO>();

		for (HandbookCountry entity : entityList) {
			dtoList.add(getdto(entity));
		}

		return dtoList;
	}

	public List<HandbookItem> getEntityList(List<HandbookItemDTO> dtoList) {
		List<HandbookItem> entityList = new ArrayList<HandbookItem>();

		for (HandbookItemDTO dto : dtoList) {
			entityList.add(getEntity(dto));
		}

		return entityList;
	}
	public List<CountryLookUpDTO> getCountriesList(Integer id){
		List<CountryLookUpDTO> dtoList = new ArrayList<CountryLookUpDTO>();
		HandbookItem handbook = dao.findBy(HandbookItem.class, id);
		List<HandbookCountry> hcountry = handbookDao.getHandbookList(handbook);
		for(HandbookCountry h:hcountry){
		  dtoList.add(dto(h));
		}
		return dtoList;
	
	}
	public CountryLookUpDTO dto(HandbookCountry entity) {
		CountryLookUpDTO dto = null;
		if (entity != null) {
			dto = new CountryLookUpDTO();
			dto.setId(entity.getCountry().getId());
			dto.setName(entity.getCountry().getName());
			
		}
		return dto;
	}

}
