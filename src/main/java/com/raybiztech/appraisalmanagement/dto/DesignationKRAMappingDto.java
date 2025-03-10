package com.raybiztech.appraisalmanagement.dto;

import java.util.Set;

public class DesignationKRAMappingDto {

    private Long id;

    private DesignationDto designation;

    private AppraisalCycleDto appraisalCycleDto;

    private Set<KRADto> kraLookups;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DesignationDto getDesignation() {
        return designation;
    }

    public void setDesignation(DesignationDto designation) {
        this.designation = designation;
    }

    public Set<KRADto> getKraLookups() {
        return kraLookups;
    }

    public void setKraLookups(Set<KRADto> kraLookups) {
        this.kraLookups = kraLookups;
    }

    public AppraisalCycleDto getAppraisalCycleDto() {
        return appraisalCycleDto;
    }

    public void setAppraisalCycleDto(AppraisalCycleDto appraisalCycleDto) {
        this.appraisalCycleDto = appraisalCycleDto;
    }

}
