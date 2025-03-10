/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.employeeprofile.builder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.EmployeeBankInformation;
import com.raybiztech.appraisals.business.EmployeeFamilyInformation;
import com.raybiztech.appraisals.business.Finance;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.employeeprofile.dao.EmployeeProfileDAOIMPL;
import com.raybiztech.employeeprofile.dto.EmployeeBankInformationDTO;
import com.raybiztech.employeeprofile.dto.EmployeeFamilyInformationDTO;
import com.raybiztech.employeeprofile.dto.EmployeeFinanceDTO;
import com.raybiztech.employeeprofile.dto.FinanceDTO;
import com.raybiztech.employeeprofile.dto.ProvisionPeriodDTO;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;
import com.raybiztech.recruitment.business.Holidays;
import com.raybiztech.recruitment.dto.AnniversariesDTO;
import com.raybiztech.recruitment.dto.HolidaysDTO;
import com.raybiztech.recruitment.utils.DateParser;

/**
 * 
 * @author naresh
 */
@Component("employeeProfileBuilder")
public class EmployeeProfileBuilder {

	@Autowired
	DAO dao;

	@Autowired
	EmployeeBuilder employeeBuilder;

	Logger logger = Logger.getLogger(EmployeeProfileBuilder.class);

	public List<EmployeeFamilyInformationDTO> convertEmployeeFamilyInformationDTO(
			Set<EmployeeFamilyInformation> employeeFamilyDetails) {

		List<EmployeeFamilyInformationDTO> informationDTOs = null;
		if (!employeeFamilyDetails.isEmpty()) {

			informationDTOs = new ArrayList<EmployeeFamilyInformationDTO>();

			for (EmployeeFamilyInformation information : employeeFamilyDetails) {
				EmployeeFamilyInformationDTO informationDTO = new EmployeeFamilyInformationDTO();
				informationDTO.setFamilyId(information.getFamilyId());
				informationDTO.setPersonName(information.getPersonName());
				informationDTO.setRelationShip(information.getRelationShip());
				//informationDTO.setContactNumber(information.getContactNumber());
				informationDTO.setDateOfBirth(DateParser.toString(information.getDateOfBirth()));
				if(information.getCountryCodeContact()!=null){
					CountryLookUp country = dao.findBy(CountryLookUp.class, information.getCountryCodeContact());
					informationDTO.setContactNumber(country.getMobileCode() + information.getContactNumber());
				}else{
					informationDTO.setContactNumber(information.getContactNumber());
				}
				// informationDTO.setEmployeeId(information.getEmployee().getEmployeeId().toString());
				informationDTO.setEmployeeId(information.getEmployee().getEmployeeId());
				informationDTOs.add(informationDTO);
			}
		}
		return informationDTOs;
	}

	public EmployeeFamilyInformationDTO convertEntityToDTO(EmployeeFamilyInformation familyInformation) {
		EmployeeFamilyInformationDTO informationDTO = null;
		if (familyInformation != null) {
			informationDTO = new EmployeeFamilyInformationDTO();
			informationDTO.setFamilyId(familyInformation.getFamilyId());
			// informationDTO.setEmployeeId(familyInformation.getEmployee().getEmployeeId().toString());
			informationDTO.setEmployeeId(familyInformation.getEmployee().getEmployeeId());
			informationDTO.setPersonName(familyInformation.getPersonName());
			informationDTO.setRelationShip(familyInformation.getRelationShip());
			informationDTO.setContactNumber(familyInformation.getContactNumber());
			informationDTO.setDateOfBirth(DateParser.toString(familyInformation.getDateOfBirth()));
			if(familyInformation.getCountryCodeContact()!=null){
				informationDTO.setCountryCodeContact(familyInformation.getCountryCodeContact());
			}
		}
		return informationDTO;
	}

	public List<HolidaysDTO> convertHolidaysEntityToDTO(List<Holidays> hollydays) {
		List<HolidaysDTO> holidaysDTOs = new ArrayList<HolidaysDTO>();
		for (Holidays h : hollydays) {
			HolidaysDTO hdto = new HolidaysDTO();
			hdto.setId(h.getId());
			hdto.setName(h.getName());
			hdto.setDate(h.getDate().toString());
			hdto.setFullDate(h.getDate().toString("dd MMM YYYY"));
			hdto.setWeek(h.getDate().toString("E"));
			hdto.setCountry(h.getCountry()!=null?h.getCountry():"N/A");
		    holidaysDTOs.add(hdto);
		}
		return holidaysDTOs;
	}

	public List<AnniversariesDTO> convertBirthdayAnniversariesEntityToDTO(List<Object[]> employees) {
		List<AnniversariesDTO> anniversariesDTOs = null;
		if (!employees.isEmpty()) {
			anniversariesDTOs = new ArrayList<AnniversariesDTO>();
			for (Object[] obj : employees) {
				AnniversariesDTO adto = new AnniversariesDTO();
				/* adto.setId(e.getEmployeeId()); */
				if (String.valueOf(obj[2]) != null) {
					adto.setDate(String.valueOf(obj[2]).substring(0, 6));
				}
				String orignalPath = String.valueOf(obj[3]);
				String splitedPath = orignalPath.substring(orignalPath.lastIndexOf("/") + 1);
				adto.setImagePath("../profilepics/" + splitedPath);
				adto.setName(String.valueOf(obj[0]) + " " + String.valueOf(obj[1]));
				anniversariesDTOs.add(adto);

			}
		}

		return anniversariesDTOs;
	}

	public List<AnniversariesDTO> convertMarriedAnniversariesEntityToDTO(List<Employee> employees) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new java.util.Date());
		int today = cal.get(Calendar.DATE);
		int monthday = (cal.get(Calendar.MONTH) + 1);

		List<AnniversariesDTO> anniversariesDTOs = null;
		if (!employees.isEmpty()) {
			int dayvalue = 0;
			int daymonth = 0;
			anniversariesDTOs = new ArrayList<AnniversariesDTO>();
			for (Employee e : employees) {
				dayvalue = e.getMarriageDate().getDayOfMonth().getValue();
				daymonth = e.getMarriageDate().getMonthOfYear().getValue() + 1;
				AnniversariesDTO adto = new AnniversariesDTO();
				adto.setId(e.getEmployeeId());
				if (e.getMarriageDate() != null) {
					adto.setDate(e.getMarriageDate().getDayOfMonth().getValue() + "-"
							+ (e.getMarriageDate().toString("MMM")));
				}
				adto.setName(e.getFirstName() + " " + e.getLastName());
				adto.setType("marriageDay");
				if (daymonth == monthday && dayvalue >= today) {
					anniversariesDTOs.add(adto);
				}
				if (daymonth > monthday) {
					anniversariesDTOs.add(adto);
				}
			}
		}

		return anniversariesDTOs;

	}

	public List<EmployeeBankInformationDTO> convertEmployeeBankInformationDTO(
			Set<EmployeeBankInformation> bankInformations) {
		List<EmployeeBankInformationDTO> informationDTOs = null;
		if (!bankInformations.isEmpty()) {

			informationDTOs = new ArrayList<EmployeeBankInformationDTO>();

			for (EmployeeBankInformation information : bankInformations) {
				EmployeeBankInformationDTO informationDTO = new EmployeeBankInformationDTO();
				informationDTO.setBankId(information.getBankId());
				informationDTO.setBankName(information.getBankName());
				informationDTO.setBankAccountNumber(information.getBankAccountNumber());
				if(information.getIfscCode()!=null){
					informationDTO.setIfscCode(information.getIfscCode());
				}
				/*
				 * informationDTO.setPanCardAccountNumber(information
				 * .getPanCardAccountNumber()); informationDTO.setPfAccountNumber(information
				 * .getPfAccountNumber());
				 */
				// informationDTO.setEmployeeId(information.getEmployee().getEmployeeId().toString());
				informationDTO.setEmployeeId(information.getEmployee().getEmployeeId());
				informationDTOs.add(informationDTO);
			}
		}
		return informationDTOs;
	}

	public EmployeeBankInformationDTO convertBankInformationEntityToDTO(EmployeeBankInformation information) {
		EmployeeBankInformationDTO informationDTO = null;
		if (information != null) {
			informationDTO = new EmployeeBankInformationDTO();
			informationDTO.setBankId(information.getBankId());
			informationDTO.setBankName(information.getBankName());
			informationDTO.setBankAccountNumber(information.getBankAccountNumber());
			if(information.getIfscCode()!=null){
				informationDTO.setIfscCode(information.getIfscCode());
			}
			/*
			 * informationDTO.setPanCardAccountNumber(information
			 * .getPanCardAccountNumber()); informationDTO.setPfAccountNumber(information
			 * .getPfAccountNumber());
			 */
			/*
			 * informationDTO.setEmployeeId(information.getEmployee().getEmployeeId
			 * ().toString());
			 */
			informationDTO.setEmployeeId(information.getEmployee().getEmployeeId());
		}
		return informationDTO;

	}

	public List<ProvisionPeriodDTO> convertEntityToDTO(List<Object[]> list) {

		List<ProvisionPeriodDTO> provisionPeriodDTOs = null;
		if (list != null) {
			provisionPeriodDTOs = new ArrayList<ProvisionPeriodDTO>();
			for (Object[] obj : list) {
				ProvisionPeriodDTO dTO = new ProvisionPeriodDTO();
				String firstName = String.valueOf(obj[0]);
				String lastName = String.valueOf(obj[1]);
				String numberofMonths = String.valueOf(obj[2]);
				String joinDate = DateConversion(String.valueOf(obj[3]));
				String provisionDate = DateConversion(String.valueOf(obj[4]));
				dTO.setUsername(firstName + " " + lastName);
				dTO.setMonth(numberofMonths);
				dTO.setJoinDate(joinDate);
				dTO.setProvisionDate(provisionDate);
				provisionPeriodDTOs.add(dTO);

			}
		}
		return provisionPeriodDTOs;
	}

	public String DateConversion(String pdate) {
		String nextDate = "";
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("dd MMM yyyy");
		java.util.Date date = null;
		try {
			date = format1.parse(pdate);
		} catch (ParseException ex) {
			java.util.logging.Logger.getLogger(EmployeeProfileDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
		}
		nextDate = format2.format(date);
		return nextDate;
	}

	public FinanceDTO convertFinanceEntityToDTO(Finance finance) {
		FinanceDTO financeDTO = null;
		if (finance != null) {
			financeDTO = new FinanceDTO();
			financeDTO.setFinanceId(finance.getFinanceId());
			financeDTO.setEmployeeId(finance.getEmployee().getEmployeeId());
			financeDTO.setPanCardAccountNumber(finance.getPanCardAccountNumber());
			financeDTO.setPfAccountNumber(finance.getPfAccountNumber());
			financeDTO.setUaNumber(finance.getUaNumber());
			financeDTO.setAadharCardNumber(finance.getAadharCardNumber());
			// financeDTO.setFinanceFilePath(finance.getFinanceFilePath());

			String originalPath = finance.getFinanceFilePath();
			if (originalPath != null) {
				String splitedPath = originalPath.substring(originalPath.lastIndexOf("/") + 1);
				financeDTO.setFinanceFilePath(splitedPath.replace("null", ""));
			}
		}
		return financeDTO;

	}

	public Finance convertFinanceDTOToEntity(Finance finance, FinanceDTO financeDTO) {
		finance.setPfAccountNumber(financeDTO.getPfAccountNumber());
		finance.setPanCardAccountNumber(financeDTO.getPanCardAccountNumber());
		finance.setAadharCardNumber(financeDTO.getAadharCardNumber());
		finance.setUaNumber(financeDTO.getUaNumber());

		return finance;
	}

	public Set<EmployeeFinanceDTO> getFinanceDetailsOfAllEmployees(List<Finance> finances) {

		Set<EmployeeFinanceDTO> dtos = null;
		if (finances != null) {
			dtos = new HashSet<EmployeeFinanceDTO>();
			for (Finance finance : finances) {
				EmployeeFinanceDTO employeeFinanceDTO = new EmployeeFinanceDTO();
				Employee employee = finance.getEmployee();
				employeeFinanceDTO.setEmployeeId(employee.getEmployeeId());
				employeeFinanceDTO.setEmployeeName(employeeBuilder.getCompleteFullName(employee));
				employeeFinanceDTO.setFinanceDetails(convertFinanceEntityToDTO(finance));
				List<EmployeeBankInformation> bankInformations = dao.getAllOfProperty(EmployeeBankInformation.class,
						"employee", employee);
				employeeFinanceDTO.setBankDetails(
						convertEmployeeBankInformationDTO(new HashSet<EmployeeBankInformation>(bankInformations)));
				dtos.add(employeeFinanceDTO);
			}
		}

		return dtos;
	}
}
