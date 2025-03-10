package com.raybiztech.appraisals.certification.Dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.raybiztech.appraisals.certification.business.Certification;
import com.raybiztech.appraisals.dao.DAOImpl;
@Repository("certificateDao")
public class CertificateDaoImpl extends DAOImpl implements CertificateDao {

	@Override
	public List<Certification> getCertificatesForEmployee(Long employeeId) {
		 Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				 Certification.class);
	        criteria.add(Restrictions.eq("employee.employeeId", employeeId));
		return criteria.list();
	}

}
