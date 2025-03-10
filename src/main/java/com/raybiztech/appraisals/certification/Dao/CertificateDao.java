package com.raybiztech.appraisals.certification.Dao;

import java.util.List;

import com.raybiztech.appraisals.certification.business.Certification;
import com.raybiztech.appraisals.dao.DAO;

public interface CertificateDao extends DAO {

	List<Certification> getCertificatesForEmployee(Long employeeId);

}
