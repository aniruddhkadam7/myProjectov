package com.raybiztech.appraisals.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Appraisal;
import com.raybiztech.appraisals.business.Cycle;
import com.raybiztech.appraisals.business.DesignationKras;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dto.AppraisalDTO;
import com.raybiztech.appraisals.dto.CycleDTO;
import com.raybiztech.appraisals.dto.EmployeeDTO;

@Component("appraisalBuilder")
public class AppraisalBuilder {
  
	
	
	@Autowired
	EmployeeBuilder employeeBuilder;
	@Autowired
	CycleBuilder cycleBuilder;
	@Autowired
	KRABuilder kraBuilder;

	public AppraisalDTO createAppraisalDTO(Appraisal appraisal) {
		AppraisalDTO appraisalDTO = null;
		if (appraisal != null) {
			appraisalDTO = new AppraisalDTO();
			appraisalDTO.setId(appraisal.getAppraisalId());

			EmployeeDTO employeeDTO = employeeBuilder
					.createEmployeeDTO(appraisal.getEmployee());
			appraisalDTO.setEmployeeDTO(employeeDTO);

			CycleDTO cycleDTO = cycleBuilder.createCycleDTO(appraisal
					.getCycle());
			appraisalDTO.setCycleDto(cycleDTO);
			DesignationKras designationKras = appraisal.getEmployee()
					.getDesignationKras();
		}
		return appraisalDTO;
	}

	public Appraisal createAppraisal(AppraisalDTO appraisalDTO) {
        Appraisal appraisal = null;
        if (appraisalDTO != null) {
        	appraisal = new Appraisal();
            appraisal.setAppraisalId(appraisalDTO.getId());

            Employee employee = employeeBuilder
                    .createEmployeeEntity(appraisalDTO.getEmployeeDTO());
            appraisal.setEmployee(employee);

            Cycle cycle = cycleBuilder.createCycleEntity(appraisalDTO
                    .getCycleDto());
            appraisal.setCycle(cycle);
        }
        return appraisal;
    }

	public List<AppraisalDTO> getAppraisalDTOList(List<Appraisal> appraisalList) {

		List<AppraisalDTO> appraisalListDto = new ArrayList<AppraisalDTO>();
		if (appraisalList != null) {
			for (Appraisal appraisal : appraisalList) {

				AppraisalDTO appraisalDTO = new AppraisalDTO();

				appraisalDTO.setId(appraisal.getAppraisalId());

				EmployeeDTO employeeDTO = employeeBuilder
						.createEmployeeDTO(appraisal.getEmployee());
				appraisalDTO.setEmployeeDTO(employeeDTO);

				CycleDTO cycleDTO = cycleBuilder.createCycleDTO(appraisal
						.getCycle());
				appraisalDTO.setCycleDto(cycleDTO);
				appraisalListDto.add(appraisalDTO);
			}
		}

		return appraisalListDto;
	}

	public List<Appraisal> getAppraisalEntityList(
			List<AppraisalDTO> appraisalDtoList) {

		List<Appraisal> appraisalList = new ArrayList<Appraisal>();
		if (appraisalDtoList != null) {
			for (AppraisalDTO appraisalDto : appraisalDtoList) {

				Appraisal appraisal = new Appraisal();

				appraisal.setAppraisalId(appraisalDto.getId());

				Employee employee = employeeBuilder
						.createEmployeeEntity(appraisalDto.getEmployeeDTO());
				appraisal.setEmployee(employee);

				Cycle cycle = cycleBuilder.createCycleEntity(appraisalDto
						.getCycleDto());
				appraisal.setCycle(cycle);
				appraisalList.add(appraisal);
			}

		}
		return appraisalList;
	}
	
}
