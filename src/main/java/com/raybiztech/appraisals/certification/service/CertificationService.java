package com.raybiztech.appraisals.certification.service;

import java.util.List;

import com.raybiztech.appraisals.certification.Dto.CertificationDto;

public interface CertificationService {

	void addCertificate(CertificationDto certificationDto);

	List<CertificationDto> getCertificatesForEmployee(Long employeeId);

	void updateCertificateForEmployee(
			CertificationDto certificationDto);

	CertificationDto getCertificate(Long certificateId);

	void deleteCertification(Long id);

}
