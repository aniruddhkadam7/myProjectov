package com.raybiztech.appraisalmanagement.builder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisalmanagement.business.Frequency;
import com.raybiztech.appraisalmanagement.business.KPI;
import com.raybiztech.appraisalmanagement.dao.AppraisalDao;
import com.raybiztech.appraisalmanagement.dto.KPIDto;
import com.raybiztech.appraisalmanagement.dto.KRADto;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Second;

@Component("kPIBuilder")
public class KPIBuilder {
	@Autowired
	AppraisalDao appraisalDao;
	@Autowired
	SecurityUtils securityUtils;

	Logger logger = Logger.getLogger(KPIBuilder.class);

	public KPI toEntity(KPIDto kpidto)

	{
		KPI kpi = null;
		if (kpidto != null) {
			kpi = new KPI();
			kpi.setId(kpidto.getId());
			kpi.setName(kpidto.getName());
			kpi.setFrequency(appraisalDao.findBy(Frequency.class,
					kpidto.getFrequencyId()));
			kpi.setTarget(kpidto.getTarget());
			kpi.setDescription(kpidto.getDescription());
			kpi.setCreatedBy(securityUtils
					.getLoggedEmployeeIdforSecurityContextHolder());
			kpi.setCreatedDate(new Second());
		}
		return kpi;
	}

	public KPI toeditEntity(KPIDto kpidto)

	{
		KPI kpi = appraisalDao.findBy(KPI.class, kpidto.getId());

		if (kpidto != null) {

			kpi.setId(kpidto.getId());
			kpi.setName(kpidto.getName());
			kpi.setDescription(kpidto.getDescription());
			kpi.setUpdatedBy(securityUtils
					.getLoggedEmployeeIdforSecurityContextHolder());
			kpi.setUpdatedDate(new Second());
		}
		return kpi;
	}

	public KPIDto toDto(KPI kpi) {
		KPIDto kpiDto = null;
		if (kpi != null) {
			kpiDto = new KPIDto();
			kpiDto.setDescription(kpi.getDescription());
			kpiDto.setId(kpi.getId());
			kpiDto.setName(kpi.getName());
			Frequency frequency = kpi.getFrequency();
			if (kpi.getFrequency() != null) {
				kpiDto.setFrequency(frequency != null ? frequency
						.getFrequencyname() : "N/A");
				kpiDto.setFrequencyId(frequency.getId());
			}
			kpiDto.setTarget(kpi.getTarget());
			KRADto kraDto = new KRADto();

			if (kpi.getKra() != null) {
				kraDto.setId(kpi.getKra().getId());
				kraDto.setName(kpi.getKra().getName());

			}
			kpiDto.setKraDto(kraDto);
		}
		return kpiDto;
	}

	public KPI toUpdateEntity(KPIDto kpidto) {
		KPI kpi = null;

		if (kpidto != null) {
			kpi = appraisalDao.findBy(KPI.class, kpidto.getId());
			kpi.setTarget(kpidto.getTarget());
			kpi.setName(kpidto.getName());
			kpi.setFrequency(appraisalDao.findBy(Frequency.class,
					kpidto.getFrequencyId()));
			kpi.setDescription(kpidto.getDescription());
			kpi.setUpdatedBy(securityUtils
					.getLoggedEmployeeIdforSecurityContextHolder());
			kpi.setUpdatedDate(new Second());

		}

		return kpi;
	}

	public Set<KPIDto> toDtoSet(Set<KPI> kpiSet) {
		Set<KPIDto> kpiDtoSet = new HashSet<KPIDto>();
		for (KPI kpi : kpiSet) {
			kpiDtoSet.add(toDto(kpi));
		}
		return kpiDtoSet;

	}

	public Set<KPI> toEntitySet(Set<KPIDto> kpiDtoSet) {
		Set<KPI> kpiEntitySet = null;
		if (kpiDtoSet != null && kpiDtoSet.size() > 0) {
			kpiEntitySet = new HashSet<KPI>();
			for (KPIDto kpiDto : kpiDtoSet) {
				kpiEntitySet.add(toEntity(kpiDto));
			}
		}
		return kpiEntitySet;
	}

	public List<KPIDto> toDtoList(List<KPI> list) {

		List<KPIDto> listdtos = new ArrayList<KPIDto>();
		for (KPI kpi : list) {
			listdtos.add(toDto(kpi));
		}
		return listdtos;
	}

}
