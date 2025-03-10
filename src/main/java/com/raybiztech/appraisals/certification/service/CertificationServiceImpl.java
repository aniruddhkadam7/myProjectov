package com.raybiztech.appraisals.certification.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.certification.Dao.CertificateDao;
import com.raybiztech.appraisals.certification.Dto.CertificationDto;
import com.raybiztech.appraisals.certification.builder.CertificationBuilder;
import com.raybiztech.appraisals.certification.business.Certification;

@Service("certificationService")
@Transactional
public class CertificationServiceImpl implements CertificationService {
	@Autowired
	CertificateDao certificateDao;
	@Autowired
	CertificationBuilder certificationBuilder;

	@Override
	public void addCertificate(CertificationDto certificationDto) {
		Certification certification = certificationBuilder
				.toEntity(certificationDto);
		certification.setEmployee(certificateDao.findBy(Employee.class,
				certificationDto.getEmployeeId()));
		certificateDao.save(certification);
	}

	@Override
	public List<CertificationDto> getCertificatesForEmployee(Long employeeId) {
		List<Certification> list = certificateDao
				.getCertificatesForEmployee(employeeId);
		return certificationBuilder.toDtoList(list);
	}

	@Override
	public void updateCertificateForEmployee(CertificationDto certificationDto) {
		Certification certification = certificationBuilder
				.toEntity(certificationDto);
		certification.setEmployee(certificateDao.findBy(Employee.class,
				certificationDto.getEmployeeId()));
		certificateDao.update(certification);
	}

	@Override
	public CertificationDto getCertificate(Long certificateId) {
		return certificationBuilder.toDto(certificateDao.findBy(
				Certification.class, certificateId));
	}

	@Override
	public void deleteCertification(Long id) {
		certificateDao.delete(certificateDao.findBy(Certification.class, id));

	}

}
