package com.raybiztech.appraisalmanagement.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisalmanagement.business.AppraisalCycle;
import com.raybiztech.appraisalmanagement.business.DesignationKRAMapping;
import com.raybiztech.appraisalmanagement.business.KPI;
import com.raybiztech.appraisalmanagement.business.KRA;
import com.raybiztech.appraisalmanagement.dto.AppraisalCycleDto;
import com.raybiztech.appraisalmanagement.dto.AppraisalFormDto;
import com.raybiztech.appraisalmanagement.dto.AppraisalKPIDataDto;
import com.raybiztech.appraisalmanagement.dto.AppraisalKRADataDto;
import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.Second;
import org.apache.log4j.Logger;

@Component("appraisalCycleBuilder")
public class AppraisalCycleBuilder {

	@Autowired
	KPIBuilder kPIBuilder;
	@Autowired
	KRABuilder kRABuilder;
	@Autowired
	EmployeeBuilder employeeBuilder;
        @Autowired
        SecurityUtils securityUtils;
        
        Logger logger=Logger.getLogger(AppraisalCycle.class);

	public AppraisalCycle toEntity(AppraisalCycleDto appraisalCycleDto) {

		AppraisalCycle appraisalcycle = null;

		if (appraisalCycleDto != null) {
			appraisalcycle = new AppraisalCycle();
			appraisalcycle.setId(appraisalCycleDto.getId());
			appraisalcycle.setActive(appraisalCycleDto.isActive());
			appraisalcycle.setDescription(appraisalCycleDto.getDescription());
			appraisalcycle.setName(appraisalCycleDto.getName());
                        appraisalcycle.setAppraisalType(appraisalCycleDto.getAppraisalType());
                        appraisalcycle.setAppraisalDuration(appraisalCycleDto.getAppraisalDuration());
                        appraisalcycle.setLevelOfHierarchy(appraisalCycleDto.getLevel());
                        appraisalcycle.setServicePeriod(appraisalCycleDto.getServicePeriod());

			Date fromDate = null;
			Date toDate = null;
			Date appraisalStartDate=null;
			Date appraisalEndDate=null;
			try {
				fromDate = Date.parse(appraisalCycleDto.getFromDate(),
						"dd/MM/yyyy");
				toDate = Date
						.parse(appraisalCycleDto.getToDate(), "dd/MM/yyyy");
				appraisalStartDate=Date.parse(appraisalCycleDto.getAppraisalStartDate(), "MM/yyyy");
				appraisalEndDate=Date.parse(appraisalCycleDto.getAppraisalEndDate(), "MM/yyyy");
				DateRange dateRange = new DateRange(fromDate, toDate);
				appraisalcycle.setConfigurationPeriod(dateRange);
				DateRange appraisalDateRange=new DateRange(appraisalStartDate, appraisalEndDate);
				appraisalcycle.setAppraisalPeriod(appraisalDateRange);
			}
			catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
                        appraisalcycle.setCreatedBy(securityUtils.getLoggedEmployeeIdforSecurityContextHolder());
                        appraisalcycle.setCreatedDate(new Second());
		}
		return appraisalcycle;
	}

	public AppraisalCycleDto toDto(AppraisalCycle appraisalCycle) {
		AppraisalCycleDto appraisalCycleDto = null;
		if (appraisalCycle != null) {
			appraisalCycleDto = new AppraisalCycleDto();
			appraisalCycleDto.setActive(appraisalCycle.isActive());
			appraisalCycleDto.setDescription(appraisalCycle.getDescription());
			appraisalCycleDto.setFromDate(appraisalCycle.getConfigurationPeriod()!=null?appraisalCycle.getConfigurationPeriod()
					.getMinimum().toString("dd/MM/yyyy"):null);
			appraisalCycleDto.setId(appraisalCycle.getId());
			appraisalCycleDto.setName(appraisalCycle.getName());

			appraisalCycleDto.setToDate(appraisalCycle.getConfigurationPeriod()!=null?appraisalCycle.getConfigurationPeriod().getMaximum()
					.toString("dd/MM/yyyy"):null);
			appraisalCycleDto.setAppraisalStartDate(appraisalCycle.getAppraisalPeriod().getMinimum().toString("MM/yyyy"));
			appraisalCycleDto.setAppraisalEndDate(appraisalCycle.getAppraisalPeriod().getMaximum().toString("MM/yyyy"));
                        appraisalCycleDto.setAppraisalType(appraisalCycle.getAppraisalType());
                        appraisalCycleDto.setAppraisalDuration(appraisalCycle.getAppraisalDuration());
                        appraisalCycleDto.setLevel(appraisalCycle.getLevelOfHierarchy());
                        appraisalCycleDto.setServicePeriod(appraisalCycle.getServicePeriod());
                        Date currentDate=new Date();
                        if(appraisalCycle.getConfigurationPeriod()!=null)  {
                       if( (currentDate.equals(appraisalCycle.getConfigurationPeriod().getMinimum())) || (currentDate.isAfter(appraisalCycle.getConfigurationPeriod()
					.getMinimum()))){
                           appraisalCycleDto.setCycleStartedFlag(Boolean.TRUE);
                       }else{
                           appraisalCycleDto.setCycleStartedFlag(Boolean.FALSE);
                       }
                        }
		}

		return appraisalCycleDto;
	}

	public List<AppraisalCycleDto> toDtoList(
			List<AppraisalCycle> appraisalCycleList) {

		List<AppraisalCycleDto> appraisalCycleDtoList = new ArrayList<AppraisalCycleDto>();

		for (AppraisalCycle appraisalCycle : appraisalCycleList) {
			appraisalCycleDtoList.add(toDto(appraisalCycle));
		}

		return appraisalCycleDtoList;

	}

	public Set<AppraisalKRADataDto> buildKraData(Set<KRA> kraLookups) {
		Set<AppraisalKRADataDto> kraDtos = new HashSet<AppraisalKRADataDto>();
		for (KRA kra : kraLookups) {
			AppraisalKRADataDto aDataDto = new AppraisalKRADataDto();
			aDataDto.setId(kra.getId());
			aDataDto.setName(kra.getName());
			aDataDto.setDescription(kra.getDescription());
			Set<AppraisalKPIDataDto> akpidds = buildKpiData(kra.getKpiLookps());
			aDataDto.setKpis(akpidds);
			kraDtos.add(aDataDto);
		}

		return kraDtos;

	}

	private Set<AppraisalKPIDataDto> buildKpiData(Set<KPI> kpiLookps) {
		Set<AppraisalKPIDataDto> kipDtos = new HashSet<AppraisalKPIDataDto>();
		for (KPI kpi : kpiLookps) {
			AppraisalKPIDataDto aDataDto = new AppraisalKPIDataDto();
			aDataDto.setId(kpi.getId());
			aDataDto.setName(kpi.getName());
			aDataDto.setDescription(kpi.getDescription());
			kipDtos.add(aDataDto);
		}
		return kipDtos;
	}

	public AppraisalFormDto buildEmployeeAppraisalForm(
			AppraisalCycle appraisalCycle, Employee employee,
			DesignationKRAMapping kRAMapping) {

		AppraisalFormDto appraisalFormDto = new AppraisalFormDto();

		// step:1 build AppraisalCycle to AppraisalForm
		AppraisalCycleDto appraisalCycleDto = toDto(appraisalCycle);
		appraisalFormDto.setAppraisalCycle(appraisalCycleDto);

		// step:2 build KRAs and KPIs to AppraisalForm
		Set<AppraisalKRADataDto> aDataDtos = buildKraData(kRAMapping
				.getKraLookups());
		appraisalFormDto.setKra(aDataDtos);

		// step:2 build owner to AppraisalForm
		EmployeeDTO employeeDTO = employeeBuilder.createEmployeeDTO(employee);
		appraisalFormDto.setEmployee(employeeDTO);

		return appraisalFormDto;
	}

	public AppraisalCycle createCycle(AppraisalCycle appraisalCycle,
			AppraisalCycleDto appraisalCycleDto) {
		appraisalCycle.setActive(appraisalCycleDto.isActive());
		appraisalCycle.setDescription(appraisalCycleDto.getDescription());
		appraisalCycle.setName(appraisalCycleDto.getName());
                appraisalCycle.setAppraisalType(appraisalCycleDto.getAppraisalType());
                appraisalCycle.setAppraisalDuration(appraisalCycleDto.getAppraisalDuration());
                appraisalCycle.setLevelOfHierarchy(appraisalCycleDto.getLevel());
                appraisalCycle.setServicePeriod(appraisalCycleDto.getServicePeriod());
		Date fromDate = null;
		Date toDate = null;
		Date appraisalStartDate=null;
		Date appraisalEndDate=null;
		try {
			fromDate = Date
					.parse(appraisalCycleDto.getFromDate(), "dd/MM/yyyy");
			toDate = Date.parse(appraisalCycleDto.getToDate(), "dd/MM/yyyy");
			DateRange dateRange = new DateRange(fromDate, toDate);
			appraisalCycle.setConfigurationPeriod(dateRange);
			appraisalStartDate=Date.parse(appraisalCycleDto.getAppraisalStartDate(), "MM/yyyy");
			appraisalEndDate=Date.parse(appraisalCycleDto.getAppraisalEndDate(), "MM/yyyy");
			DateRange appraisalDateRange=new DateRange(appraisalStartDate, appraisalEndDate);
			appraisalCycle.setAppraisalPeriod(appraisalDateRange);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
                appraisalCycle.setUpdatedBy(securityUtils.getLoggedEmployeeIdforSecurityContextHolder());
                appraisalCycle.setUpdatedDate(new Second());
		return appraisalCycle;

	}
}
