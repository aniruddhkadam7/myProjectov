package com.raybiztech.appraisalmanagement.builder;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisalmanagement.business.AppraisalKRAData;
import com.raybiztech.appraisalmanagement.business.KRA;
import com.raybiztech.appraisalmanagement.dto.AppraisalKRADataDto;
import com.raybiztech.appraisals.dao.DAO;

@Component("appraisalKRADataBuilder")
public class AppraisalKRADataBuilder {
	@Autowired
	DAO dao;

	@Autowired
	AppraisalKPIDataBuilder appraisalKPIDataBuilder;

	public AppraisalKRAData toEntity(AppraisalKRADataDto appraisalKraDataDto) {
		AppraisalKRAData appraisalKraDataEntity = null;
		if (appraisalKraDataDto != null) {
			appraisalKraDataEntity = new AppraisalKRAData();
			appraisalKraDataEntity.setDescription(appraisalKraDataDto
					.getDescription());
			// appraisalKraDataEntity.setId(appraisalKraDataDto.getId());
			appraisalKraDataEntity.setName(appraisalKraDataDto.getName());
			appraisalKraDataEntity.setKpis(appraisalKPIDataBuilder
					.toEntitySet(appraisalKraDataDto.getKpis()));

		}
		return appraisalKraDataEntity;
	}

	public AppraisalKRADataDto toDto(AppraisalKRAData appraisalKraDataEntity) {
		AppraisalKRADataDto appraisalKraDataDto = null;
		if (appraisalKraDataEntity != null) {
			appraisalKraDataDto = new AppraisalKRADataDto();
			appraisalKraDataDto.setId(appraisalKraDataEntity.getId());
			appraisalKraDataDto.setName(appraisalKraDataEntity.getName());
			appraisalKraDataDto.setCount(appraisalKraDataEntity.getKpis()
					.size());
			appraisalKraDataDto.setDesignationKraPercentage(appraisalKraDataEntity
					.getDesignationKraPercentage());
			appraisalKraDataDto.setKpis(appraisalKPIDataBuilder
					.toDtoSet(appraisalKraDataEntity.getKpis()));
		}
		return appraisalKraDataDto;
	}

	public Set<AppraisalKRAData> toEntitySet(
			Set<AppraisalKRADataDto> appraisalKraDataDtoSet) {
		Set<AppraisalKRAData> appraisalKraDataSet = new HashSet<AppraisalKRAData>();
		for (AppraisalKRADataDto appraisalKraDataDto : appraisalKraDataDtoSet) {
			appraisalKraDataSet.add(toEntity(appraisalKraDataDto));
		}

		return appraisalKraDataSet;
	}

	public Set<AppraisalKRADataDto> toDtoSet(
			Set<AppraisalKRAData> appraisalKraDataEntity) {
		Set<AppraisalKRADataDto> appraisalKraDataEntitySet = new HashSet<AppraisalKRADataDto>();
		for (AppraisalKRAData appraisalKraData : appraisalKraDataEntity) {
			appraisalKraDataEntitySet.add(toDto(appraisalKraData));
		}
		return appraisalKraDataEntitySet;
	}
}
