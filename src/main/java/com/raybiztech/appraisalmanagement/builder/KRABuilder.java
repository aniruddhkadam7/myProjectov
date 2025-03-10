package com.raybiztech.appraisalmanagement.builder;

import com.raybiztech.appraisalmanagement.business.Designation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisalmanagement.business.KRA;
import com.raybiztech.appraisalmanagement.dto.KRADto;
import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Second;

@Component("kRABuilder")
public class KRABuilder {

	@Autowired
	KPIBuilder kPIBuilder;
        @Autowired
        DAO dao;
        @Autowired
        SecurityUtils securityUtils;

	public KRA toEntity(KRADto KRAdTO) {
		KRA kra = null;
		if (KRAdTO != null) {
			kra = new KRA();
			kra.setDescription(KRAdTO.getDescription());
			kra.setId(KRAdTO.getId());
			kra.setName(KRAdTO.getName());
                        kra.setDesignationKraPercentage(KRAdTO.getDesignationKraPercentage());
                        kra.setEmpDepartment(dao.findBy(EmpDepartment.class, KRAdTO.getDepartmentId()));
                        kra.setDesignation(dao.findBy(Designation.class, KRAdTO.getDesignationId()));
                        kra.setCreatedBy(securityUtils.getLoggedEmployeeIdforSecurityContextHolder());
                        kra.setCreatedDate(new Second());
			//kra.setKpiLookps(kPIBuilder.toEntitySet(KRAdTO.getKpiLookps()));
		}

		return kra;
	}
        public KRA toEditEntity(KRADto KRAdTO){
            KRA kra=null;
            if(KRAdTO!=null){
                 kra=dao.findBy(KRA.class, KRAdTO.getId());
                kra.setDescription(KRAdTO.getDescription());
                kra.setName(KRAdTO.getName());
                kra.setDesignationKraPercentage(KRAdTO.getDesignationKraPercentage());
                kra.setEmpDepartment(dao.findBy(EmpDepartment.class, KRAdTO.getDepartmentId()));
                kra.setDesignation(dao.findBy(Designation.class, KRAdTO.getDesignationId()));
                kra.setUpdatedBy(securityUtils.getLoggedEmployeeIdforSecurityContextHolder());
                kra.setUpdatedDate(new Second());
            }
            return kra;
        }
	public KRADto toDto(KRA kra) {
		KRADto kraDto = null;
		if (kra != null) {
			kraDto = new KRADto();
			kraDto.setDescription(kra.getDescription());
			kraDto.setId(kra.getId());
			kraDto.setName(kra.getName());
			kraDto.setCount(kra.getKpiLookps().size());
                        kraDto.setDepartmentId(kra.getEmpDepartment().getDepartmentId());
                        kraDto.setDepartmentName(kra.getEmpDepartment().getDepartmentName());
                        kraDto.setDesignationId(kra.getDesignation().getId());
                        kraDto.setDesignationName(kra.getDesignation().getName());
                        kraDto.setDesignationKraPercentage(kra.getDesignationKraPercentage());
		//	kraDto.setKpiLookps(kPIBuilder.toDtoSet(kra.getKpiLookps()));
		}
		return kraDto;
	}

	public Set<KRA> toEntitySet(Set<KRADto> kraDto) {
		Set<KRA> kraEntitySet = new HashSet<KRA>();
		for (KRADto kraDtoSet : kraDto) {
			kraEntitySet.add(toEntity(kraDtoSet));
		}
		return kraEntitySet;
	}

	public Set<KRADto> toDtoSet(Set<KRA> kra) {
		Set<KRADto> kraDtoSet = new HashSet<KRADto>();
		for (KRA kraEntitySet : kra) {
			kraDtoSet.add(toDto(kraEntitySet));
		}
		return kraDtoSet;
	}
	
	public List<KRADto> toDtoList(List<KRA> list){
		List<KRADto> dtos=new ArrayList<KRADto>();
		for(KRA kra:list){
			dtos.add(toDto(kra));	
		}
		return dtos;
	}

}
