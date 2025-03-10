package com.raybiztech.supportmanagement.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.EmpDepartmentDTO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.assetmanagement.business.AssetType;
import com.raybiztech.assetmanagement.business.Product;
import com.raybiztech.assetmanagement.dto.ProductDto;
import com.raybiztech.date.Second;
import com.raybiztech.supportmanagement.business.TicketsCategory;
import com.raybiztech.supportmanagement.dto.TicketsCategoryDTO;

import org.springframework.stereotype.Component;

@Component("ticketsCategoryBuilder")
public class TicketsCategoryBuilder {

	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	DAO dao;

	public TicketsCategory convertDTOToEntity(
			TicketsCategoryDTO ticketsCategoryDTO) {
		TicketsCategory ticketsCategory = null;
		if (ticketsCategoryDTO != null) {
			ticketsCategory = new TicketsCategory();
			ticketsCategory.setCategoryName(ticketsCategoryDTO
					.getCategoryName());
			ticketsCategory.setCreatedBy(securityUtils
					.getLoggedEmployeeIdforSecurityContextHolder());
			ticketsCategory.setCreatedDate(new Second());
			ticketsCategory.setEmpDepartment(dao.findBy(EmpDepartment.class,
					ticketsCategoryDTO.getDepartmentId()));
			ticketsCategory.setMealType(ticketsCategoryDTO.isMealType());

		}

		return ticketsCategory;
	}

	public TicketsCategoryDTO convertEntityToDTO(TicketsCategory ticketsCategory) {
		TicketsCategoryDTO ticketsCategoryDTO = null;
		if (ticketsCategory != null) {
			ticketsCategoryDTO = new TicketsCategoryDTO();

			ticketsCategoryDTO.setCategoryId(ticketsCategory.getId());
			ticketsCategoryDTO.setCategoryName(ticketsCategory
					.getCategoryName());
			ticketsCategoryDTO.setDepartmentId(ticketsCategory
					.getEmpDepartment().getDepartmentId());
			ticketsCategoryDTO.setDepartmentName(ticketsCategory
					.getEmpDepartment().getDepartmentName());
			ticketsCategoryDTO.setMealType(ticketsCategory.isMealType());
		}
		return ticketsCategoryDTO;
	}

	public List<TicketsCategoryDTO> getDTOList(List<TicketsCategory> categories) {
		List<TicketsCategoryDTO> ticketsCategoryDTOs = null;
		if (categories != null) {
			ticketsCategoryDTOs = new ArrayList<TicketsCategoryDTO>();
			for (TicketsCategory category : categories) {
				ticketsCategoryDTOs.add(convertEntityToDTO(category));
			}
		}
		return ticketsCategoryDTOs;
	}

	public TicketsCategory toEditEntity(TicketsCategoryDTO tiDto) {
		TicketsCategory ticketsCategory = null;
		if (tiDto != null) {
			ticketsCategory = dao.findBy(TicketsCategory.class,
					tiDto.getCategoryId());
			ticketsCategory.setCategoryName(tiDto.getCategoryName());
			ticketsCategory.setMealType(tiDto.isMealType());
		}
		return ticketsCategory;
	}

	public List<EmpDepartmentDTO> getDepartmentList(
			List<EmpDepartment> empDepartmentList) {
		List<EmpDepartmentDTO> empDepartmentDTOs = null;
		if (empDepartmentList != null) {
			empDepartmentDTOs = new ArrayList<EmpDepartmentDTO>();
			for (EmpDepartment empDepartment : empDepartmentList) {
				EmpDepartmentDTO empdepartmentDTO = new EmpDepartmentDTO();
				empdepartmentDTO.setId(empDepartment.getDepartmentId());
				empdepartmentDTO.setName(empDepartment.getDepartmentName());
				empDepartmentDTOs.add(empdepartmentDTO);
			}
		}
		return empDepartmentDTOs;

	}

}
