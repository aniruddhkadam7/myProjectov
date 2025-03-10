package com.raybiztech.appraisalmanagement.builder;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisalmanagement.business.DesignationKRAMapping;
import com.raybiztech.appraisalmanagement.dto.DesignationKRAMappingDto;
import java.util.ArrayList;
import java.util.List;

@Component("designationKRAMappingBuilder")
public class DesignationKRAMappingBuilder {

	@Autowired
	DesignationBuilder designationsBuilder;
	@Autowired
	KRABuilder kRABuilder;
        @Autowired
        AppraisalCycleBuilder appraisalCycleBuilder;

	public DesignationKRAMapping toEntity(
			DesignationKRAMappingDto designationKraMappingDto) {
		DesignationKRAMapping designationKraMapping = null;
		if (designationKraMappingDto != null) {
			designationKraMapping = new DesignationKRAMapping();
			designationKraMapping.setDesignation(designationsBuilder
					.toEntity(designationKraMappingDto.getDesignation()));
			designationKraMapping.setKraLookups(kRABuilder
					.toEntitySet(designationKraMappingDto.getKraLookups()));
			designationKraMapping.setId(designationKraMappingDto.getId());

		}

		return designationKraMapping;
	}

	public DesignationKRAMappingDto toDto(
			DesignationKRAMapping designationKraMappingEntity) {
		DesignationKRAMappingDto designationKraMappingDto = null;
		if (designationKraMappingEntity != null) {
			designationKraMappingDto = new DesignationKRAMappingDto();
			designationKraMappingDto.setDesignation(designationsBuilder
					.todto(designationKraMappingEntity.getDesignation()));
			designationKraMappingDto.setId(designationKraMappingEntity.getId());
			designationKraMappingDto.setKraLookups(kRABuilder
					.toDtoSet(designationKraMappingEntity.getKraLookups()));
                        designationKraMappingDto.setAppraisalCycleDto(appraisalCycleBuilder.toDto(designationKraMappingEntity.getCycle()));
		}
		return designationKraMappingDto;
	}
	
	public Set<DesignationKRAMapping> toEntitySet(Set<DesignationKRAMappingDto> designationKraMappingDtoSet)
	{
		Set<DesignationKRAMapping> designationKraMappingSet=new HashSet<DesignationKRAMapping>();
		for(DesignationKRAMappingDto designationKraMappingdto:designationKraMappingDtoSet)
		{
			designationKraMappingSet.add(toEntity(designationKraMappingdto));
		}
		return designationKraMappingSet;
	}
	
	public Set<DesignationKRAMappingDto> toDtoSet(Set<DesignationKRAMapping> designationKraMappingSet)
	{
		Set<DesignationKRAMappingDto> designationKraMappingDtoSet= new HashSet<DesignationKRAMappingDto>();
		for(DesignationKRAMapping designationKraMappingEntity:designationKraMappingSet)
		{
			designationKraMappingDtoSet.add(toDto(designationKraMappingEntity));
		}
		return designationKraMappingDtoSet;
	}
        public List<DesignationKRAMappingDto> toDtoList(List<DesignationKRAMapping> desginationsOfCycle){
            List<DesignationKRAMappingDto> designationKRAMappingDtos=new ArrayList<DesignationKRAMappingDto>();
            for(DesignationKRAMapping designations:desginationsOfCycle){
                designationKRAMappingDtos.add(toDto(designations));
            }
            return designationKRAMappingDtos;
        }
        
}
