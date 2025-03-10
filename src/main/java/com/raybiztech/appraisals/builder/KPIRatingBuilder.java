package com.raybiztech.appraisals.builder;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Cycle;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.KPI;
import com.raybiztech.appraisals.business.KPIRating;
import com.raybiztech.appraisals.dto.CycleDTO;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.dto.KPIDTO;
import com.raybiztech.appraisals.dto.KPIRatingDTO;

@Component("kpiRatingBuilder")
public class KPIRatingBuilder {
    public static Logger logger = Logger
            .getLogger(KPIRatingBuilder.class);
    
	public KPIRating createKPIRatingEntity(KPIRatingDTO ratingDTO) {

		KPIRating kpiRating = new KPIRating();
		if(ratingDTO!=null){
			
			logger.info("in kpi rating builder :.....kpirating dto is :"+ratingDTO.getEmployeeFileName()+"--- "+ratingDTO.getEmployeeDummyFileName());
			
			
		    kpiRating.setKpiRatingId(ratingDTO.getId());
		    kpiRating.setKpiRatingName(ratingDTO.getName());
	        kpiRating.setEmployee_Rating(ratingDTO.getEmployee_Rating());
	        kpiRating.setEmployee_Comment(ratingDTO.getEmployee_Comment());
	        kpiRating.setManage_comment(ratingDTO.getManage_comment());
	        kpiRating.setManager_Rating(ratingDTO.getManager_Rating());
	        kpiRating.setEmployeeFileName(ratingDTO.getEmployeeFileName());
	        kpiRating.setManagerFileName(ratingDTO.getManagerFileName());
	        kpiRating.setEmployeeDummyFileName(ratingDTO.getEmployeeDummyFileName());
	        kpiRating.setManagerDummyFileName(ratingDTO.getManagerDummyFileName());
	        
	        Cycle cycle = new Cycle();
	        if(ratingDTO.getCycle()!=null){
	            cycle.setCycleId(ratingDTO.getCycle().getId());
	        }
	        kpiRating.setCycle(cycle);
	        
	        Employee employee = new Employee();
	        if(ratingDTO.getEmployee()!=null){
	            employee.setEmployeeId(ratingDTO.getEmployee().getId());
	        }
	        kpiRating.setEmployee(employee);
	        
	        KPI kpi = new KPI();
	        if(ratingDTO.getKpi()!=null){
	            kpi.setKpiId(ratingDTO.getKpi().getId());
	        }
	        kpiRating.setKpi(kpi);
		}
		
		return kpiRating;

	}

	public KPIRatingDTO createKPIRatingDTO(KPIRating kpiRating) {
		KPIRatingDTO kpiRatingDTO = null;
		if (kpiRating != null) {
			kpiRatingDTO = new KPIRatingDTO();
			kpiRatingDTO.setId(kpiRating.getKpiRatingId());
			kpiRatingDTO.setName(kpiRating.getKpiRatingName());
			kpiRatingDTO.setEmployee_Rating(kpiRating.getEmployee_Rating());
			kpiRatingDTO.setEmployee_Comment(kpiRating.getEmployee_Comment());
			kpiRatingDTO.setManager_Rating(kpiRating.getManager_Rating());
			kpiRatingDTO.setManage_comment(kpiRating.getManage_comment());
			kpiRatingDTO.setEmployeeFileName(kpiRating.getEmployeeFileName());
			kpiRatingDTO.setManagerFileName(kpiRating.getManagerFileName());
			
			CycleDTO cycleDto = new CycleDTO();
            if(kpiRating.getCycle()!=null){
                cycleDto.setId(kpiRating.getCycle().getCycleId());
            }
            kpiRatingDTO.setCycle(cycleDto);
            
            EmployeeDTO employeeDto = new EmployeeDTO();
            if(kpiRating.getEmployee()!=null){
                employeeDto.setId(kpiRating.getEmployee().getEmployeeId());
            }
            kpiRatingDTO.setEmployee(employeeDto);
            
            KPIDTO kpiDto = new KPIDTO();
            if(kpiRating.getKpi()!=null){
                kpiDto.setId(kpiRating.getKpi().getKpiId());
            }
            kpiRatingDTO.setKpi(kpiDto);
		}
		return kpiRatingDTO;
	}

	public Set<KPIRating> getKPIRatings(Set<KPIRatingDTO> kpiRatingDtos) {

		Set<KPIRating> kpiRatings = new HashSet<KPIRating>();

		if (kpiRatingDtos != null) {
			for (KPIRatingDTO kpiRatingDto : kpiRatingDtos) {
				KPIRating kpiRating = createKPIRatingEntity(kpiRatingDto);
				kpiRatings.add(kpiRating);
			}
		}
		return kpiRatings;
	}

	public Set<KPIRatingDTO> getKPIRatingDtos(Set<KPIRating> kpiRatings) {

		Set<KPIRatingDTO> kpiRatingDtos = new HashSet<KPIRatingDTO>();

		if (kpiRatings != null) {
			for (KPIRating kpiRating : kpiRatings) {
				KPIRatingDTO kpiRatingDto = createKPIRatingDTO(kpiRating);
				kpiRatingDtos.add(kpiRatingDto);
			}
		}
		return kpiRatingDtos;
	}
}
