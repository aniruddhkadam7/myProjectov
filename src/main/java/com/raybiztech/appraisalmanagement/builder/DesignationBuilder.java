package com.raybiztech.appraisalmanagement.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.raybiztech.appraisalmanagement.business.Designation;
import com.raybiztech.appraisalmanagement.dto.DesignationDto;

@Component("designationsBuilder")
public class DesignationBuilder {

	public Designation toEntity(DesignationDto designationDto) {
		Designation designation = null;
		if (designationDto != null) {
			designation = new Designation();
			designation.setId(designationDto.getId());
			designation.setCode(designationDto.getCode());
			designation.setName(designationDto.getName());
		}

		return designation;
	}

	public DesignationDto todto(Designation designation) {
		DesignationDto designationDto = null;
		if (designation != null) {
			designationDto = new DesignationDto();
			designationDto.setId(designation.getId());
			designationDto.setCode(designation.getCode());
			designationDto.setName(designation.getName());
                        designationDto.setDepartmentName(designation.getEmpDepartment().getDepartmentName());
                        designationDto.setDepartmentId(designation.getEmpDepartment().getDepartmentId());

		}
		return designationDto;
	}

	public List<DesignationDto> toDtoList(List<Designation> designationList) {
		List<DesignationDto> designationDtoList = new ArrayList<DesignationDto>();
		for (Designation designation : designationList) {
			designationDtoList.add(todto(designation));
		}
		return designationDtoList;
	}
}
