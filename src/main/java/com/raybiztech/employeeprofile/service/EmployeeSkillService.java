package com.raybiztech.employeeprofile.service;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.raybiztech.appraisals.certification.Dto.CertificateTypeDto;
import com.raybiztech.appraisals.certification.Dto.CertificationDto;
import com.raybiztech.appraisals.dto.VisaDetailDTO;
import com.raybiztech.appraisals.dto.VisaLookUpDTO;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;

public interface EmployeeSkillService {

	Map<String, Object> EmployeeSkillReport(Integer startIndex,Integer endIndex,String categoryId, String skillId,
			String searchstr,String Competency,String experience);
	
	Map<String, Object> getAllEmployeecertificates(Integer startIndex,Integer endIndex,String selectionTechnology,String selectedCertificate, String multipleSearch);
	
	List<CertificationDto> EmployeeCertificates(Long empId,String selectionTechnology,String selectedCertificate, String multipleSearch);

	List<CountryLookUp> getCountries();

	List<VisaLookUpDTO> getVisaTypeList();

	Map<String, Object> getEmployeeVisaDetailsReport(Integer startIndex, Integer endIndex, Integer countryId,
			Long visaTypeId, String multipleSearch);

	List<VisaDetailDTO> getEmployeeVisaDetailsList(Long empId, Integer countryId, Long visaTypeId,
			String multipleSearch);
	
	ByteArrayOutputStream exportCertificatesList( String selectionTechnology, String selectedCertificate, String multipleSearch) throws Exception;

	ByteArrayOutputStream exportEmployeeVisaList(Integer countryId, Long visaTypeId, String multipleSearch) throws IOException;

	void addCertificateType(CertificateTypeDto dto);

	List<CertificateTypeDto> getCertificateTypeList();

	List<CertificateTypeDto> getCertificateByTechnology(String technologyName);

	Boolean checkForDuplicateCertificate(Long technologyId, String certificateType);

	CertificateTypeDto getCertificateTypeDetails(Long certificateId);

	void updateCertificatetype(CertificateTypeDto dto);

	void deleteCertificateType(Long certificateId);

}
