package com.raybiztech.leavemanagement.builder;

import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.raybiztech.leavemanagement.business.LeaveCategory;
import com.raybiztech.leavemanagement.business.LeaveType;
import com.raybiztech.leavemanagement.dto.LeaveCategoryDTO;

@Component("leaveCategoryBuilder")
public class LeaveCategoryBuilder {

	Logger logger = Logger.getLogger(LeaveCategoryBuilder.class);

	public LeaveCategory createLeaveCategoryEntity(LeaveCategoryDTO categoryDTO) {
		// TODO Auto-generated method stub

		LeaveCategory leaveCategory = new LeaveCategory();
		if (categoryDTO != null) {
			leaveCategory.setId(categoryDTO.getId());
			leaveCategory.setName(categoryDTO.getName());
			leaveCategory.setLeaveType(LeaveType.valueOf(categoryDTO
					.getLeaveType()));

		}

		return leaveCategory;
	}

	public LeaveCategoryDTO createLeaveCategoryDTO(LeaveCategory category) {
		// TODO Auto-generated method stub
		LeaveCategoryDTO categoryDTO = new LeaveCategoryDTO();
		if (category != null) {
			categoryDTO.setId(category.getId());
			categoryDTO.setName(category.getName());
			categoryDTO.setLeaveType(category.getLeaveType().toString());
		}

		return categoryDTO;
	}

	public SortedSet<LeaveCategoryDTO> createLeaveCategoryDTOSet(
			SortedSet<LeaveCategory> leaveCategories) {

		SortedSet<LeaveCategoryDTO> leaveCategoryDTOSet = new TreeSet<LeaveCategoryDTO>();
		for (LeaveCategory leaveCategory : leaveCategories) {
			LeaveCategoryDTO leaveCategoryDTO = new LeaveCategoryDTO();
			leaveCategoryDTO.setId(leaveCategory.getId());
			leaveCategoryDTO.setName(leaveCategory.getName());
			leaveCategoryDTO.setLeaveType(leaveCategory.getLeaveType()
					.toString());

			leaveCategoryDTOSet.add(leaveCategoryDTO);
		}

		return leaveCategoryDTOSet;
	}

}
