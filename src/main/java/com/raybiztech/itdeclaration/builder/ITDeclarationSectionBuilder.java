package com.raybiztech.itdeclaration.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Second;
import com.raybiztech.itdeclaration.business.Investment;
import com.raybiztech.itdeclaration.business.Section;
import com.raybiztech.itdeclaration.dao.ITDeclarationDao;
import com.raybiztech.itdeclaration.dto.SectionDTO;

@Component("itDeclarationSectionBuilder")
public class ITDeclarationSectionBuilder {
	// Autowiring Security Utils to get Id of Current User
	@Autowired
	SecurityUtils securityUtils;
	
	@Autowired
	ITDeclarationDao iTDeclarationDaoImpl;
	
	@Autowired
	ITDeclarationInvestmentBuilder iTDeclarationInvestmentBuilder;
	//converting SectionDTO to business
	public Section convertSectionDtotoEntity(SectionDTO sectionDTO) {
		// System.out.println("sectionDTO"+sectionDTO.getSectionName()+sectionDTO.getSectionLimit());
		Section section = new Section();
		section.setSectionName(sectionDTO.getSectionName());
		
		section.setSectionLimit(sectionDTO.getSectionLimit());
		section.setCreatedBy(securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder());
		section.setCreatedDate(new Second());
		return section;
	}
	//converting Business to SectionDTO
	public SectionDTO convertEntitytoSectionDTO(Section section) {
		SectionDTO sectionDTO = new SectionDTO();
		sectionDTO.setSectionId(section.getSectionId());
		sectionDTO.setSectionName(section.getSectionName());
		sectionDTO.setSectionLimit(section.getSectionLimit());
		sectionDTO.setInvests(iTDeclarationInvestmentBuilder.toDTOList(new ArrayList<Investment>(section.getInvests())));
		return sectionDTO;

	}

	public List<SectionDTO> toDTOList(List<Section> listSections) {
		List<SectionDTO> listSectionDTOs = new ArrayList<SectionDTO>();

		for (Section section : listSections) {
			listSectionDTOs.add(convertEntitytoSectionDTO(section));
		}
		return listSectionDTOs;
	}

	// updating section
	public Section toEditEntity(SectionDTO sectionDTO) {
		Section section = iTDeclarationDaoImpl.findBy(Section.class, sectionDTO.getSectionId());
		section.setSectionName(sectionDTO.getSectionName());
		section.setSectionLimit(sectionDTO.getSectionLimit());
		section.setUpdatedBy(securityUtils.getLoggedEmployeeIdforSecurityContextHolder() );
		section.setUpdatedDate(new Second());
		return section;
		
	}

	// deleting section
	public void deleteSection() {

	}

}
