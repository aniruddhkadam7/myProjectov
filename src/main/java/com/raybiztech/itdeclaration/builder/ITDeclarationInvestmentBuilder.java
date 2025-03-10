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
import com.raybiztech.itdeclaration.dto.InvestmentDTO;

//ITDeclaration Investment Builder
@Component("iTDeclarationInvestmentBuilder")
public class ITDeclarationInvestmentBuilder {

	// Autowiring Security Utils to get Id of Current User
	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	ITDeclarationDao iTDeclarationDaoImpl;
	//converting DTO to entity
	public Investment toEntity(InvestmentDTO investmentDTO) {
		
		//Employee employee=(Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder().get("employee");
		//employee.getde
		Investment investment = null;
		if(investmentDTO!=null){
			investment = new Investment();
			investment.setInvestmentName(investmentDTO.getInvestmentName());
			investment.setMaxLimit(investmentDTO.getMaxLimit());
			investment.setSection(iTDeclarationDaoImpl.findBy(Section.class, investmentDTO.getSectionId()));
			investment.setDescription(investmentDTO.getDescription());
			investment.setRequiredDocs(investmentDTO.getRequiredDocs());
			investment.setCreatedBy(securityUtils.getLoggedEmployeeIdforSecurityContextHolder());
			investment.setCreatedDate(new Second());
			
		}
		return investment;
	}
	
	public Investment toUpdateEntity(InvestmentDTO investmentDTO) {
		Investment investment = null;
		if(investmentDTO!=null){
			investment = iTDeclarationDaoImpl.findBy(Investment.class, investmentDTO.getInvestmentId());
			investment.setInvestmentName(investmentDTO.getInvestmentName());
			investment.setDescription(investmentDTO.getDescription());
			investment.setRequiredDocs(investmentDTO.getRequiredDocs());
			investment.setMaxLimit(investmentDTO.getMaxLimit());
			investment.setUpdatedBy(securityUtils.getLoggedEmployeeIdforSecurityContextHolder());
			investment.setUpdatedDate(new Second());
		}
		return investment;
	}
	
	public InvestmentDTO toDTO(Investment investment) {
		InvestmentDTO investmentDTO = null;
		if(investment!= null){
			investmentDTO = new InvestmentDTO();
			investmentDTO.setInvestmentId(investment.getInvestmentId());
			investmentDTO.setInvestmentName(investment.getInvestmentName());
			investmentDTO.setMaxLimit(investment.getMaxLimit());
			investmentDTO.setSectionId(investment.getSection().getSectionId());
			investmentDTO.setDescription(investment.getDescription());
			investmentDTO.setRequiredDocs(investment.getRequiredDocs());
			investmentDTO.setSectionName(investment.getSection().getSectionName());
		}
		return investmentDTO;
	}
	
	public List<InvestmentDTO> toDTOList(List<Investment> listInvestments) {
		List<InvestmentDTO> listInvestmentDTOs = null;
		if(listInvestments!=null){
			listInvestmentDTOs = new ArrayList<InvestmentDTO>();
			for(Investment investment : listInvestments){
				listInvestmentDTOs.add(toDTO(investment));
			}
		}
		return listInvestmentDTOs;
	}
	
}
