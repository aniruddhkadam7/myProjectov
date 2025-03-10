package com.raybiztech.employeeprofile.service;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFFont;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.builder.VisaDetailsBuilder;
import com.raybiztech.appraisals.builder.VisaLookUpBuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.certification.Dao.CertificateDao;
import com.raybiztech.appraisals.certification.Dto.CertificateTypeDto;
import com.raybiztech.appraisals.certification.Dto.CertificationDto;
import com.raybiztech.appraisals.certification.builder.CertificationBuilder;
import com.raybiztech.appraisals.certification.business.CertificateType;
import com.raybiztech.appraisals.certification.business.Certification;
import com.raybiztech.appraisals.business.VisaDetails;
import com.raybiztech.appraisals.dto.VisaDetailDTO;
import com.raybiztech.appraisals.dto.VisaLookUpDTO;
import com.raybiztech.employeeprofile.dao.EmployeeSkillDao;
import com.raybiztech.employeeprofile.dto.EmployeeSkillDTO;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;

@Service("employeeSkillServiceImpl")
public class EmployeeSkillServiceImpl implements EmployeeSkillService {

	@Autowired
	EmployeeSkillDao employeeSkillDaoImpl;

	@Autowired
	CertificationBuilder certificationBuilder;

	@Autowired
	VisaLookUpBuilder visaLookUpBuilder;

	@Autowired
	VisaDetailsBuilder visaDetailsBuilder;

	@Autowired
	CertificateDao certificateDao;

	@Transactional
	@Override
	public Map<String, Object> EmployeeSkillReport(Integer startIndex,
			Integer endIndex, String categoryId, String skillId,
			String searchstr, String Competency, String experience) {

		String year = "";
		String month = "";

		if (experience.contains(".")) {
			experience = experience.replace(".", " ");

			String[] skillExperience = experience.split(" ");

			year = skillExperience[0];
			month = skillExperience[1];
		}

		else {
			year = experience;
		}

		Map<String, Object> employees = null;
		List<EmployeeSkillDTO> employeeSkillDTOList = new ArrayList<EmployeeSkillDTO>();
		Map<String, Object> employeeMap = new HashMap<String, Object>();

		employees = employeeSkillDaoImpl.getEmployeesList(startIndex, endIndex,
				categoryId, skillId, searchstr, Competency, year, month);
		List<Employee> emploeeList = (List<Employee>) employees
				.get("employeeList");

		for (Employee emp : emploeeList) {
			EmployeeSkillDTO employeeSkillDTO = new EmployeeSkillDTO();
			employeeSkillDTO.setEmpName(emp.getFullName());
			employeeSkillDTO.setEmployeeId(emp.getEmployeeId());
			employeeSkillDTOList.add(employeeSkillDTO);

		}
		employeeMap.put("list", employeeSkillDTOList);
		employeeMap.put("listsize", employees.get("size"));

		return employeeMap;
	}

	@Override
	public Map<String, Object> getAllEmployeecertificates(Integer startIndex,
			Integer endIndex, String selectionTechnology, String selectedCertificate,String multipleSearch) {

		Map<String, Object> certificateEmployees = null;
		List<EmployeeSkillDTO> employeeSkillDTOList = new ArrayList<EmployeeSkillDTO>();
		Map<String, Object> certificatesMap = new HashMap<String, Object>();

		certificateEmployees = employeeSkillDaoImpl.getAllEmployeesCertficates(
				selectionTechnology, selectedCertificate, multipleSearch);

		// List<Certification> certficateList = (List<Certification>)
		// certificateEmployees.get("certificatesList");

		List<Long> certficateList = (List<Long>) certificateEmployees
				.get("certificatesList");
		//System.out.println("certficateList" + certficateList);

		for (Long cert : certficateList) {
			Employee employee = employeeSkillDaoImpl.findBy(Employee.class,
					cert);
			EmployeeSkillDTO employeeSkillDTO = new EmployeeSkillDTO();
			employeeSkillDTO.setEmpName(employee.getEmployeeFullName());
			employeeSkillDTO.setEmployeeId(employee.getEmployeeId());
			List<CertificationDto> list = EmployeeCertificates(employee.getEmployeeId(),
							selectionTechnology, selectedCertificate, multipleSearch);
			employeeSkillDTO.setCertificationDtos(list);
			employeeSkillDTOList.add(employeeSkillDTO);
		}

		/*
		 * @SuppressWarnings("unchecked") List<Object[]> rows = (List<Object[]>)
		 * certificateEmployees.get("certificatesList");
		 * 
		 * System.out.println(rows);
		 * 
		 * for (Object[] row : rows) {
		 * 
		 * Bulletin bull = new Bulletin(); bull.setBulletinId((Long) row[0]);
		 * bull.setDocumentName((String) row[1]);
		 * bull.setDocumentTypeCode((String) row[2]); list.add(bull);
		 * 
		 * EmployeeSkillDTO employeeSkillDTO = new EmployeeSkillDTO();
		 * employeeSkillDTO.setEmployeeId((Long) row[0]);
		 * employeeSkillDTO.setEmpName((String) row[2]);
		 * 
		 * System.out.println(employeeSkillDTO.getEmployeeId());
		 * System.out.println(employeeSkillDTO.getEmpName());
		 * employeeSkillDTOList.add(employeeSkillDTO);
		 * 
		 * }
		 */

		Integer noOfRecords = employeeSkillDTOList.size();

		//System.out.println("no of records" + noOfRecords);
		// pagination because if we will do pagination from DaoImpl then there
		// is a chance of
		// getting duplicate date and after restricting duplicate, pagination
		// won't work properly.
		List<EmployeeSkillDTO> certificateDTO = new ArrayList<EmployeeSkillDTO>();
		if (endIndex <= employeeSkillDTOList.size()) {
			certificateDTO = employeeSkillDTOList.subList(startIndex, endIndex);
		} else {
			certificateDTO = employeeSkillDTOList.subList(startIndex,
					employeeSkillDTOList.size());
		}

		//System.out.println("certificateDTO" + certificateDTO.size());

		certificatesMap.put("list", certificateDTO);
		certificatesMap.put("listsize", noOfRecords);

		return certificatesMap;

	}

	@Override
	public List<CertificationDto> EmployeeCertificates(Long empId,
			String selectionTechnology, String selectedCertificate, String multipleSearch) {
		List<Certification> list = employeeSkillDaoImpl
				.getCertificatesForEmployee(empId, selectionTechnology, selectedCertificate,
						multipleSearch);
		return certificationBuilder.toDtoList(list);
	}

	@Override
	public List<CountryLookUp> getCountries() {

		return employeeSkillDaoImpl.getCountries();
	}

	@Override
	public List<VisaLookUpDTO> getVisaTypeList() {
		return visaLookUpBuilder.getDTOList(employeeSkillDaoImpl
				.getVisaTypeList());
	}

	@Override
	public Map<String, Object> getEmployeeVisaDetailsReport(Integer startIndex,
			Integer endIndex, Integer countryId, Long visaTypeId,
			String multipleSearch) {
		/*Map<String, Object> visaDetailsMap = employeeSkillDaoImpl
				.getEmployeeVisaDetailsReport(countryId, visaTypeId,
						multipleSearch);
		List<Long> empIds = (List<Long>) visaDetailsMap.get("list");

		// List<Employee> employees =
		// employeeSkillDaoImpl.getEmployeeDetails(empIds);

		List<EmployeeSkillDTO> visaDetailsReport = new ArrayList<>();

		for (Long id : empIds) {
			Employee emp = employeeSkillDaoImpl.findBy(Employee.class, id);
			EmployeeSkillDTO skillDto = new EmployeeSkillDTO();
			skillDto.setEmployeeId(emp.getEmployeeId());
			skillDto.setEmpName(emp.getEmployeeFullName());
			
			List<VisaDetailDTO> list = getEmployeeVisaDetailsList(emp.getEmployeeId(), 
					countryId, visaTypeId, multipleSearch);
			skillDto.setVisaDetailsDtos(list);
			visaDetailsReport.add(skillDto);
		}
		
		Map<String, Object> visaDetailsReportMap = new HashMap<>();
		visaDetailsReportMap.put("size", visaDetailsReport.size());
		
		if(startIndex !=null && endIndex != null) {
			if(endIndex <= visaDetailsReport.size()) {
				visaDetailsReport = visaDetailsReport.subList(startIndex, endIndex);
			}
			else {
				visaDetailsReport = visaDetailsReport.subList(startIndex, visaDetailsReport.size());
			}
		}

		visaDetailsReportMap.put("list", visaDetailsReport);
		
		return visaDetailsReportMap;*/
		
		Map<String, Object> visaDetailsMap = employeeSkillDaoImpl.getEmployeeVisaDetailsReport(countryId,
											visaTypeId, multipleSearch);
		List<VisaDetails> visaDetails = (List<VisaDetails>) visaDetailsMap.get("list");
		
		List<VisaDetailDTO> visaDtoList = null;
		List<VisaDetailDTO> oldVisaDtoList = new ArrayList<>();
		VisaDetailDTO visaDto = new VisaDetailDTO();
		
		Map<Long, List<VisaDetailDTO>> empVisaDetails = new HashMap<>();
		
		for(VisaDetails details : visaDetails) {
			visaDto = visaDetailsBuilder.toDTO(details);
			if(!empVisaDetails.containsKey(details.getEmployee().getEmployeeId())) {
				
				visaDtoList = new ArrayList<>();
				visaDtoList.add(visaDto);
				empVisaDetails.put(details.getEmployee().getEmployeeId(), visaDtoList);
			}
			else {
				oldVisaDtoList= empVisaDetails.get(details.getEmployee().getEmployeeId());
				oldVisaDtoList.add(visaDto);
			}
		}
		
		List<EmployeeSkillDTO> visaDetailsReport = new ArrayList<>();
		EmployeeSkillDTO skillDto = null;
		for(Map.Entry<Long, List<VisaDetailDTO>> entry : empVisaDetails.entrySet()) {
			skillDto = new EmployeeSkillDTO();
			skillDto.setEmployeeId(entry.getKey());
			Employee emp = employeeSkillDaoImpl.findBy(Employee.class, entry.getKey());
			skillDto.setEmpName(emp.getEmployeeFullName());
			skillDto.setVisaDetailsDtos(entry.getValue());
			visaDetailsReport.add(skillDto);
			}
			
		Map<String, Object> visaDetailsReportMap = new HashMap<>();
		
		if(endIndex < visaDetailsReport.size()) {
		visaDetailsReportMap.put("list", visaDetailsReport.subList(startIndex, endIndex));
		}
		else {
			visaDetailsReportMap.put("list", visaDetailsReport.subList(startIndex, visaDetailsReport.size()));
		}
		visaDetailsReportMap.put("size", visaDetailsReport.size());
		
		
		
		return visaDetailsReportMap;
			
	}
			
			

	@Override
	public List<VisaDetailDTO> getEmployeeVisaDetailsList(Long empId,
			Integer countryId, Long visaTypeId, String multipleSearch) {
		List<VisaDetails> visaDetailsList = employeeSkillDaoImpl
				.getEmployeeVisaDetailsList(empId, countryId, visaTypeId,
						multipleSearch);
		List<VisaDetailDTO> dtoList = visaDetailsBuilder
				.toDTOList(visaDetailsList);
		return dtoList;
	}


	@Override
	public ByteArrayOutputStream exportCertificatesList(
			String selectionTechnology, String selectedCertificate, String multipleSearch) throws Exception {

		List<Certification> certificatesList = employeeSkillDaoImpl
				.getExportEmployeesCertficates(selectionTechnology,selectedCertificate,
						multipleSearch);

		List<CertificationDto> certificatesMapList = certificationBuilder
				.toDtoList(certificatesList);

		Integer noOfRecords = certificatesMapList.size();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		int rowIndex = 1;
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row1 = sheet.createRow(0);
		Font font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 10);
		CellStyle style = workbook.createCellStyle();

		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);

		Cell cell0 = row1.createCell(0);
		cell0.setCellValue("Employee Name");
		cell0.setCellStyle(style);

		Cell cell1 = row1.createCell(1);
		cell1.setCellValue("Technology");
		cell1.setCellStyle(style);
		
		Cell cell2 = row1.createCell(2);
		cell2.setCellValue("Certification Type");
		cell2.setCellStyle(style);
		
		Cell cell3 = row1.createCell(3);
		cell3.setCellValue("Certification");
		cell3.setCellStyle(style);

		Cell cell4 = row1.createCell(4);
		cell4.setCellValue("Registration No");
		cell4.setCellStyle(style);

		Cell cell5 = row1.createCell(5);
		cell5.setCellValue("Completed Date");
		cell5.setCellStyle(style);

		Cell cell6 = row1.createCell(6);
		cell6.setCellValue("Expiry Date");
		cell6.setCellStyle(style);

		Cell cell7 = row1.createCell(7);
		cell7.setCellValue("Percentage");
		cell7.setCellStyle(style);

		for (CertificationDto list : certificatesMapList) {
			Row row = sheet.createRow(rowIndex++);

			Employee employee = employeeSkillDaoImpl.findBy(Employee.class,
					list.getEmployeeId());

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(employee.getEmployeeFullName());

			Cell cel1 = row.createCell(1);
			cel1.setCellValue(list.getTechnology() != null ? list.getTechnology() : "N/A");

			Cell cel2 = row.createCell(2);
			cel2.setCellValue(list.getCertificateType() != null ? list.getCertificateType() : "N/A");

			Cell cel3 = row.createCell(3);
			cel3.setCellValue(list.getName());

			Cell cel4 = row.createCell(4);
			cel4.setCellValue(list.getCode());

			Cell cel5 = row.createCell(5);
			cel5.setCellValue(list.getCompletedDate());

			Cell cel6 = row.createCell(6);
			cel6.setCellValue(list.getExpiryDate() != null ? list.getExpiryDate() : "N/A");
			
			Cell cel7 = row.createCell(7);
			cel7.setCellValue(list.getPercent() != null ? list.getPercent() : "N/A");

			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);

		}

		workbook.write(bos);
		bos.flush();
		bos.close();

		return bos;

	}


	@Override
	public ByteArrayOutputStream exportEmployeeVisaList(Integer countryId, Long visaTypeId, String multipleSearch) throws IOException {
		
			Map<String, Object> visaDetailsMap = employeeSkillDaoImpl.getAllVisaDetails(countryId,visaTypeId,multipleSearch);
			
			List<VisaDetails> visaDetails = (List<VisaDetails>) visaDetailsMap.get("list");
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			
			int rowIndex = 1;
			
			Workbook workbook = new HSSFWorkbook();
			
			Sheet sheet = workbook.createSheet();
			
			Row row1 = sheet.createRow(0);
			
			Font font = workbook.createFont();
			
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setFontHeightInPoints((short) 10);
			
			CellStyle style = workbook.createCellStyle();
			
			style.setAlignment(CellStyle.ALIGN_CENTER);
			style.setFont(font);
			
			Cell cell0 = row1.createCell(0);
			cell0.setCellValue("S.No");
			cell0.setCellStyle(style);
			
			Cell cell1 = row1.createCell(1);
			cell1.setCellValue("EmployeeId");
			cell1.setCellStyle(style);
			
			Cell cell2 = row1.createCell(2);
			cell2.setCellValue("Employee Name");
			cell2.setCellStyle(style);
			
			Cell cell3 = row1.createCell(3);
			cell3.setCellValue("Country");
			cell3.setCellStyle(style);
			
			Cell cell4 = row1.createCell(4);
			cell4.setCellValue("Visa Type");
			cell4.setCellStyle(style);
			
			Cell cell5 = row1.createCell(5);
			cell5.setCellValue("Date Of Issue");
			cell5.setCellStyle(style);
			
			Cell cell6 = row1.createCell(6);
			cell6.setCellValue("Date Of Expire");
			cell6.setCellStyle(style);
			
			int index = 1;
			for(VisaDetails details : visaDetails) {
				
				Row row = sheet.createRow(rowIndex++);
				
				Cell cel0 = row.createCell(0);
				cel0.setCellValue(index++);
				
				Cell cel1 = row.createCell(1);
				cel1.setCellValue(details.getEmployee().getEmployeeId());
				
				Cell cel2 = row.createCell(2);
				cel2.setCellValue(details.getEmployee().getEmployeeFullName());
				
				Cell cel3 = row.createCell(3);
				cel3.setCellValue(details.getVisaLookUp().getCountry().getName());
				
				Cell cel4 = row.createCell(4);
				cel4.setCellValue(details.getVisaLookUp().getVisaType());
				
				Cell cel5 = row.createCell(5);
				cel5.setCellValue(details.getDateOfIssue().toString("dd/MM/yyyy"));
				
				Cell cel6 = row.createCell(6);
				cel6.setCellValue(details.getDateOfExpire().toString("dd/MM/yyyy"));
				
				sheet.autoSizeColumn(0);
				sheet.autoSizeColumn(1);
				sheet.autoSizeColumn(2);
				sheet.autoSizeColumn(3);
				sheet.autoSizeColumn(4);
				sheet.autoSizeColumn(5);
				sheet.autoSizeColumn(6);
				
		
	}
			workbook.write(os);
			os.flush();
			os.close();
			return os;
	}

	@Override
	public void addCertificateType(CertificateTypeDto dto) {
		CertificateType certificate = certificationBuilder.toCertificateTypeEntity(dto);
		employeeSkillDaoImpl.save(certificate);
		
	}

	@Override
	public List<CertificateTypeDto> getCertificateTypeList() {
		List<CertificateType> certificateTypeList = employeeSkillDaoImpl.getCertificateTypeList();
		return certificationBuilder.toCertificateTypeDTOList(certificateTypeList);
	}

	@Override
	public List<CertificateTypeDto> getCertificateByTechnology(String technologyName) {
		List<CertificateType> certificateTypeList = employeeSkillDaoImpl.getCertificateByTechnology(technologyName);
		return certificationBuilder.toCertificateTypeDTOList(certificateTypeList);
	}

	@Override
	public Boolean checkForDuplicateCertificate(Long technologyId, String certificateType) {
		return employeeSkillDaoImpl.checkForDuplicateCertificate(technologyId,certificateType);
	}

	@Override
	public CertificateTypeDto getCertificateTypeDetails(Long certificateId) {
		CertificateType certificate = employeeSkillDaoImpl.findBy(CertificateType.class, certificateId);
		return certificationBuilder.toCertificateTypeDto(certificate);
	}

	@Override
	public void updateCertificatetype(CertificateTypeDto dto) {
		CertificateType certificate = certificationBuilder.toCertificateTypeEntity(dto);
		employeeSkillDaoImpl.update(certificate);
		
	}

	@Override
	public void deleteCertificateType(Long certificateId) {
		employeeSkillDaoImpl.delete(employeeSkillDaoImpl.findBy(CertificateType.class, certificateId));
	}
}
