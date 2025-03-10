package com.raybiztech.itdeclaration.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.Finance;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
import com.raybiztech.employeeprofile.dao.EmployeeProfileDAOI;
import com.raybiztech.itdeclaration.business.FinanceCycle;
import com.raybiztech.itdeclaration.business.ITDeclarationForm;
import com.raybiztech.itdeclaration.business.ITDeclarationFormInvestments;
import com.raybiztech.itdeclaration.business.ITDeclarationFormSections;
import com.raybiztech.itdeclaration.business.Investment;
import com.raybiztech.itdeclaration.business.Section;
import com.raybiztech.itdeclaration.dao.ITDeclarationDao;
import com.raybiztech.itdeclaration.dto.ITDeclarationFormDTO;
import com.raybiztech.itdeclaration.dto.ITDeclarationFormInvestmentsDTO;
import com.raybiztech.itdeclaration.dto.ITDeclarationFormSectionsDTO;

@Component("itDeclarationFormBuilder")
public class ITDeclarationFormBuilder {
	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	ITDeclarationDao iTDeclarationDaoImpl;

	@Autowired
	ITDeclarationInvestmentBuilder itInvestBuild;

	@Autowired
	ITDeclarationSectionBuilder itSectionBuild;

	@Autowired
	EmployeeProfileDAOI employeeProfileDAOIMPL;
	Logger logger = Logger.getLogger(ITDeclarationFormBuilder.class);

	// converting formDTO to business
	public ITDeclarationForm toEntity(ITDeclarationFormDTO formDTO) {
		ITDeclarationForm form = new ITDeclarationForm();
		Employee loggedInEmployee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		form.setEmployee(loggedInEmployee);
		form.setCreatedBy(loggedInEmployee.getEmployeeId());
		form.setCreatedDate(new Second());
		form.setFromDate(toDate(formDTO.getFromDate()));
		form.setToDate(toDate(formDTO.getToDate()));
		form.setIsAgree(formDTO.getIsAgree());
		form.setOrganisationName(formDTO.getOrganisationName());
		form.setItSections(toEntitySet(formDTO.getFormSectionsDTOs()));
		form.setGrandTotal(formDTO.getGrandTotal());
		form.setFilePath(formDTO.getFilePath());

		// form.setFinanceCycle(financeCycle);
		return form;
	}

	// converting String into Ray Biz Tech Date
	private Date toDate(String stringDate) {
		Date date = null;
		try {
			date = Date.parse(stringDate, "dd/MM/yyyy");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return date;
	}

	// converting SetitformSections to Business
	public Set<ITDeclarationFormSections> toEntitySet(
			Set<ITDeclarationFormSectionsDTO> formSectionsDTOs) {
		
		
		Set<ITDeclarationFormSections> formSectionsSet = new HashSet<ITDeclarationFormSections>();

		for (ITDeclarationFormSectionsDTO formSectionsDTO : formSectionsDTOs) {
			ITDeclarationFormSections formSections = null;
			if (formSectionsDTO.getItSectionsId() == null) {
				formSections = new ITDeclarationFormSections();
			} else {
				formSections = iTDeclarationDaoImpl
						.findBy(ITDeclarationFormSections.class,
								formSectionsDTO.getItSectionsId());
			}

			formSections.setIsOld(formSectionsDTO.getIsOld());
			formSections.setItInvests(toEntityInvestments(formSectionsDTO
					.getFormInvestmentDTO()));
			formSections.setSection(iTDeclarationDaoImpl.findBy(Section.class,
					formSectionsDTO.getSectionId()));
			formSectionsSet.add(formSections);

		}

		return formSectionsSet;

	}

	// converting SetitformInvestments to Business
	public Set<ITDeclarationFormInvestments> toEntityInvestments(
			Set<ITDeclarationFormInvestmentsDTO> declarationFormInvestmentsDTOs) {
		Set<ITDeclarationFormInvestments> formInvestmentsSet = new HashSet<ITDeclarationFormInvestments>();
		for (ITDeclarationFormInvestmentsDTO declarationFormInvestmentDTO : declarationFormInvestmentsDTOs) {
			ITDeclarationFormInvestments form = null;
			if(declarationFormInvestmentDTO.getFormInvestmentId()==null){
				form= new ITDeclarationFormInvestments();
			}else{
				form = iTDeclarationDaoImpl.findBy(ITDeclarationFormInvestments.class, declarationFormInvestmentDTO.getFormInvestmentId());
			}
			 
			form.setCustomAmount(declarationFormInvestmentDTO.getCustomAmount());
		
			// System.out.println("id:"+declarationFormInvestmentDTO.getInvestmentId());
			form.setInvestment(iTDeclarationDaoImpl.findBy(Investment.class,
					declarationFormInvestmentDTO.getInvestmentId()));

			formInvestmentsSet.add(form);

		}
		return formInvestmentsSet;
	}

	public EmployeeDTO toEmpDTO(Employee employee) {
		EmployeeDTO employeeDTO = new EmployeeDTO();
		return null;
	}

	// converting business to FormDTO
	public ITDeclarationFormDTO toDTO(ITDeclarationForm form) {
		ITDeclarationFormDTO formDTO = new ITDeclarationFormDTO();

		formDTO.setEmployeeId(form.getEmployee().getEmployeeId());
		formDTO.setEmployeeName(form.getEmployee().getEmployeeFullName());
		Finance finance = employeeProfileDAOIMPL.getEmplopyeeFinanceInfo(form
				.getEmployee().getEmployeeId());
	
		formDTO.setPanNumber(finance!=null?finance.getPanCardAccountNumber():"N/A");
		formDTO.setDesignation(form.getEmployee().getDesignation());
		// formDTO.setCycleDTO(iTDeclarationDaoImpl.findBy(FinanceCycleDTO.class,
		// form.getFinanceCycle().getCycleId()));
		formDTO.setItDeclarationFormId(form.getItDeclarationFormId());
		if (form.getFromDate() == null || form.getToDate() == null) {
			formDTO.setFromDate("n/a");
			formDTO.setToDate("n/a");
		} else {
			formDTO.setFromDate((form.getFromDate().toString("dd/MM/yyyy")));
			formDTO.setToDate(form.getToDate().toString("dd/MM/yyyy"));
		}
		if (form.getFilePath() != null) {
			formDTO.setFilePath(form.getFilePath().substring(
					form.getFilePath().lastIndexOf("/") + 1));
		}
		formDTO.setOrganisationName(form.getOrganisationName());
		formDTO.setFormSectionsDTOs(toDTOSections(form.getItSections()));
		// formDTO.setFormSectionsDTOs(toEntitySet(itFormSectionsDTO));
		formDTO.setGrandTotal(form.getGrandTotal());
		formDTO.setCycleId(form.getFinanceCycle().getCycleId());
		return formDTO;

	}

	// converting SetformitSectionsBusiness to DTO
	public Set<ITDeclarationFormSectionsDTO> toDTOSections(
			Set<ITDeclarationFormSections> formSections) {
		Set<ITDeclarationFormSectionsDTO> formSectionsDTOs = new HashSet<ITDeclarationFormSectionsDTO>();
		for (ITDeclarationFormSections formsection : formSections) {
			ITDeclarationFormSectionsDTO formSectionsDTO = new ITDeclarationFormSectionsDTO();
			formSectionsDTO.setItSectionsId(formsection.getItSectionsId());
			formSectionsDTO.setSectionId(formsection.getSection()
					.getSectionId());
			formSectionsDTO.setSectionName(formsection.getSection()
					.getSectionName());
			formSectionsDTO.setMaxLimit(formsection.getSection()
					.getSectionLimit());
			formSectionsDTO.setIsOld(formsection.getIsOld());
			formSectionsDTO.setFormInvestmentDTO(toDTOInvestments(formsection
					.getItInvests()));
			formSectionsDTOs.add(formSectionsDTO);

		}
		return formSectionsDTOs;

	}

	// converting SetformitInvestmentsBusiness to DTO
	public Set<ITDeclarationFormInvestmentsDTO> toDTOInvestments(
			Set<ITDeclarationFormInvestments> formInvestments) {
		Set<ITDeclarationFormInvestmentsDTO> investmentsDTOs = new HashSet<ITDeclarationFormInvestmentsDTO>();
		for (ITDeclarationFormInvestments forminvestment : formInvestments) {
			ITDeclarationFormInvestmentsDTO investmentsDTO = new ITDeclarationFormInvestmentsDTO();
			investmentsDTO.setFormInvestmentId(forminvestment
					.getFormInvestmentId());
			investmentsDTO.setCustomAmount(forminvestment.getCustomAmount());
			// Investment
			// declarationFormInvestments=iTDeclarationDaoImpl.findBy(Investment.class,
			// forminvestment.getInvestmentId());
			investmentsDTO.setInvestmentId(forminvestment.getInvestment()
					.getInvestmentId());
			investmentsDTO.setInvestmentName(forminvestment.getInvestment()
					.getInvestmentName());
			investmentsDTOs.add(investmentsDTO);
		}
		return investmentsDTOs;
	}

	// converting listITDeclarationFormList to DTO
	public List<ITDeclarationFormDTO> toDTOFormList(
			List<ITDeclarationForm> formsList) {
		List<ITDeclarationFormDTO> formDTOsList = new ArrayList<ITDeclarationFormDTO>();
		for (ITDeclarationForm form : formsList) {
			formDTOsList.add(toDTO(form));
		}
		return formDTOsList;
	}

	// edit ITDeclareForm
	public ITDeclarationForm toEditEntity(ITDeclarationFormDTO formDTO) {
		ITDeclarationForm form = iTDeclarationDaoImpl.findBy(
				ITDeclarationForm.class, formDTO.getItDeclarationFormId());

		Employee loggedInEmployee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		form.setEmployee(loggedInEmployee);
		form.setUpdatedBy(loggedInEmployee.getEmployeeId());
		form.setUpdatedDate(new Second());
		
		if(!(formDTO.getToDate().equalsIgnoreCase("n/a") || formDTO.getToDate().equalsIgnoreCase("n/a"))){
			form.setFromDate(toDate(formDTO.getFromDate()));
			form.setToDate(toDate(formDTO.getToDate()));
		}
		form.setIsAgree(formDTO.getIsAgree());
		form.setOrganisationName(formDTO.getOrganisationName());
		form.setItSections(toEntitySet(formDTO.getFormSectionsDTOs()));
		form.setGrandTotal(formDTO.getGrandTotal());
		form.setFilePath(formDTO.getFilePath());
		return form;
	}
}
