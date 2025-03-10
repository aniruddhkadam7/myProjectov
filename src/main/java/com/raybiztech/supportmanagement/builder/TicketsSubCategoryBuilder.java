package com.raybiztech.supportmanagement.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Second;
import com.raybiztech.supportmanagement.business.TicketsCategory;
import com.raybiztech.supportmanagement.business.TicketsSubCategory;
import com.raybiztech.supportmanagement.dto.TicketsSubCategoryDTO;

@Component("ticketsSubCategoryBuilder")
public class TicketsSubCategoryBuilder {

	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	DAO dao;

	public TicketsSubCategoryDTO convertEntityToDTO(
			TicketsSubCategory ticketsSubCategory) {
		TicketsSubCategoryDTO ticketssubCategoryDTO = null;
		if (ticketsSubCategory != null) {
			ticketssubCategoryDTO = new TicketsSubCategoryDTO();
			ticketssubCategoryDTO.setCategoryId(ticketsSubCategory
					.getTicketsCategory().getId());
			ticketssubCategoryDTO.setDepartmentId(ticketsSubCategory
					.getTicketsCategory().getEmpDepartment().getDepartmentId());
			ticketssubCategoryDTO.setDepartmentName(ticketsSubCategory
					.getTicketsCategory().getEmpDepartment()
					.getDepartmentName());
			ticketssubCategoryDTO.setCategoryName(ticketsSubCategory
					.getTicketsCategory().getCategoryName());
			ticketssubCategoryDTO.setSubCategoryId(ticketsSubCategory.getId());
			ticketssubCategoryDTO.setSubCategoryName(ticketsSubCategory
					.getSubCategoryName());
			ticketssubCategoryDTO.setEstimatedTime(ticketsSubCategory
					.getEstimatedTime());
			ticketssubCategoryDTO.setWorkFlow(ticketsSubCategory.getWorkFlow());
			if (ticketsSubCategory.getLevelOfHierarchy() != null)
				ticketssubCategoryDTO.setLevelOfHierarchy(ticketsSubCategory
						.getLevelOfHierarchy().toString());

		}
		return ticketssubCategoryDTO;
	}

	public TicketsSubCategory convertDTOToEntity(
			TicketsSubCategoryDTO ticketsSubCategoryDto) {
		TicketsSubCategory ticketsSubCategory = null;

		if (ticketsSubCategoryDto != null) {
			Long loggedInEmpId = securityUtils
					.getLoggedEmployeeIdforSecurityContextHolder();
			ticketsSubCategory = new TicketsSubCategory();
			ticketsSubCategory.setEstimatedTime(ticketsSubCategoryDto
					.getEstimatedTime());
			ticketsSubCategory.setSubCategoryName(ticketsSubCategoryDto
					.getSubCategoryName());
			ticketsSubCategory.setCreatedDate(new Second());
			ticketsSubCategory.setCreatedBy(loggedInEmpId);
			ticketsSubCategory.setTicketsCategory(dao.findBy(
					TicketsCategory.class,
					ticketsSubCategoryDto.getCategoryId()));
			ticketsSubCategory.setWorkFlow(ticketsSubCategoryDto.getWorkFlow());
			if (ticketsSubCategoryDto.getWorkFlow()) {
				if (ticketsSubCategoryDto.getLevelOfHierarchy() != null) {
					if (!(ticketsSubCategoryDto.getLevelOfHierarchy().isEmpty())) {
						ticketsSubCategory.setLevelOfHierarchy(Integer
								.parseInt(ticketsSubCategoryDto
										.getLevelOfHierarchy()));
					} else {
						ticketsSubCategory.setLevelOfHierarchy(1);
					}
				} else {
					ticketsSubCategory.setLevelOfHierarchy(1);
				}

			}
		}
		return ticketsSubCategory;
	}

	public List<TicketsSubCategoryDTO> getDTOList(
			List<TicketsSubCategory> ticketsSubCategories) {

		List<TicketsSubCategoryDTO> ticketsSubCategoryDTOs = null;
		if (ticketsSubCategories != null) {

			ticketsSubCategoryDTOs = new ArrayList<TicketsSubCategoryDTO>();
			for (TicketsSubCategory ticketsSubCategory : ticketsSubCategories) {
				ticketsSubCategoryDTOs
						.add(convertEntityToDTO(ticketsSubCategory));
			}
		}
		return ticketsSubCategoryDTOs;
	}

	public TicketsSubCategory toEditEntity(
			TicketsSubCategoryDTO tiSubCategoryDTO) {
		TicketsSubCategory ticketsSubCategory = null;
		if (tiSubCategoryDTO != null) {
			ticketsSubCategory = dao.findBy(TicketsSubCategory.class,
					tiSubCategoryDTO.getSubCategoryId());
			ticketsSubCategory.setSubCategoryName(tiSubCategoryDTO
					.getSubCategoryName());
			ticketsSubCategory.setEstimatedTime(tiSubCategoryDTO
					.getEstimatedTime());
			ticketsSubCategory.setWorkFlow(tiSubCategoryDTO.getWorkFlow());
			if (tiSubCategoryDTO.getWorkFlow()) {
				if (tiSubCategoryDTO.getLevelOfHierarchy() != null) {
					if (!tiSubCategoryDTO.getLevelOfHierarchy().isEmpty()) {

						ticketsSubCategory.setLevelOfHierarchy(Integer
								.parseInt(tiSubCategoryDTO
										.getLevelOfHierarchy()));
					} else {
						ticketsSubCategory.setLevelOfHierarchy(1);
					}

				} else {
					ticketsSubCategory.setLevelOfHierarchy(1);
				}
			} else {
				ticketsSubCategory.setLevelOfHierarchy(null);
			}
		}

		return ticketsSubCategory;
	}

	public List<TicketsSubCategoryDTO> ToDTOList(List<TicketsSubCategory> tiList) {
		List<TicketsSubCategoryDTO> tiSubCategoryDTOsList = null;
		if (tiList != null) {
			tiSubCategoryDTOsList = new ArrayList<TicketsSubCategoryDTO>();
			for (TicketsSubCategory ticketsSubCategory : tiList) {
				tiSubCategoryDTOsList
						.add(convertEntityToDTO(ticketsSubCategory));
			}
		}
		return tiSubCategoryDTOsList;

	}

}
