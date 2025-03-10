package com.raybiztech.employeeprofile.dao;


import java.util.List;
import java.util.Map;

import com.raybiztech.appraisals.certification.business.CertificateType;
import com.raybiztech.appraisals.certification.business.Certification;
import com.raybiztech.appraisals.dao.DAO;

import com.raybiztech.appraisals.business.VisaDetails;
import com.raybiztech.appraisals.business.VisaLookUp;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;





public interface EmployeeSkillDao extends DAO{

	Map<String, Object> getEmployeesList(Integer startIndex,Integer endIndex, String categoryId, String skillId, String searchstr,String Competency,String year,String month);

	Map<String, Object> getAllEmployeesCertficates(String selectionTechnology, String selectedCertificate, String multipleSearch);

	List<Certification> getCertificatesForEmployee(Long empId,String selectionTechnology, String selectedCertificate, String multipleSearch);

	List<VisaLookUp> getVisaTypeList();

	List<CountryLookUp> getCountries();

	Map<String, Object> getEmployeeVisaDetailsReport(Integer countryId, Long visaTypeId, String multipleSearch);

	List<VisaDetails> getEmployeeVisaDetailsList(Long empId, Integer countryId, Long visaTypeId, String multipleSearch);
	
	List<Certification> getExportEmployeesCertficates(String selectionTechnology, String selectedCertificate, String multipleSearch);

	Map<String, Object> getAllVisaDetails(Integer countryId, Long visaTypeId, String multipleSearch);

	List<CertificateType> getCertificateTypeList();

	List<CertificateType> getCertificateByTechnology(String technologyName);

	Boolean checkForDuplicateCertificate(Long technologyId, String certificateType);

}
