package com.raybiztech.appraisals.PIPManagement.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.PIPManagement.business.PIP;
import com.raybiztech.appraisals.PIPManagement.dto.PIPDTO;
import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Second;
import com.raybiztech.leavemanagement.business.algorithm.DateParser;

/**
 * @author Aprajita
 */

@Component("PIPBuilder")
public class PIPBuilder {

	@Autowired
	EmployeeBuilder employeeBuilder;
	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	DAO dao;

	Logger logger = Logger.getLogger(PIP.class);

	public PIP convertPIPDtoToPIPEntity(PIPDTO pipdto) {

		Long loggedInEmp = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();

		PIP pip = null;
		if (pipdto != null) {
			pip = new PIP();
			pip.setEmployee(dao.findBy(Employee.class, pipdto.getEmpId()));
			try {
				pip.setStartDate(DateParser.toDate(pipdto.getStartDate()));
				pip.setEndDate(DateParser.toDate(pipdto.getEndDate()));
				pip.setExtendDate(DateParser.toDate(pipdto.getExtendDate()));
			} catch (ParseException pe) {
				pe.printStackTrace();
			}
			pip.setRating(pipdto.getRating());
			if (pipdto.getRemarks() != null) {
				if (pipdto.getRemarks().equalsIgnoreCase("<br/>")) {
					pip.setRemarks(null);
				} else {
					pip.setRemarks(pipdto.getRemarks());
				}
			}
			if (pipdto.getImprovement() != null) {
				if (pipdto.getImprovement().equalsIgnoreCase("<br/>")) {
					pip.setImprovement(null);
				} else {
					pip.setImprovement(pipdto.getImprovement());
				}
			}
			pip.setCreatedBy(loggedInEmp);
			pip.setCreatedDate(new Second());
			pip.setPIPFlag(Boolean.TRUE);
		}
		return pip;
	}

	public PIPDTO convertPIPToPIPDTO(PIP pip) {
		Employee createdBy = null, updatedBy = null;
		createdBy = pip.getCreatedBy() != null ? dao.findBy(Employee.class,
				pip.getCreatedBy()) : null;
		updatedBy = pip.getUpdatedBy() != null ? dao.findBy(Employee.class,
				pip.getUpdatedBy()) : null;

		PIPDTO pipdto = null;
		if (pip != null) {
			pipdto = new PIPDTO();
			pipdto.setId(pip.getId());
			Employee employee = pip.getEmployee();
			pipdto.setEmpId(employee.getEmployeeId());
			pipdto.setEmployeeName(employee.getEmployeeFullName());
			pipdto.setStartDate(pip.getStartDate() != null ? pip.getStartDate()
					.toString("dd/MM/yyyy") : null);
			pipdto.setEndDate(pip.getEndDate() != null ? pip.getEndDate()
					.toString("dd/MM/yyyy") : null);
			pipdto.setExtendDate(pip.getExtendDate() != null ? pip
					.getExtendDate().toString("dd/MM/yyyy") : null);
			pipdto.setRating(pip.getRating());
			pipdto.setRemarks(pip.getRemarks());
			pipdto.setImprovement(pip.getImprovement());
			pipdto.setCreatedBy(createdBy != null ? createdBy
					.getEmployeeFullName() : "");
			pipdto.setCreatedDate(pip.getCreatedDate().toString("dd/MM/yyyy"));
			pipdto.setUpdatedBy(updatedBy != null ? updatedBy
					.getEmployeeFullName() : "");
			pipdto.setUpdatedDate(pip.getUpdatedDate() != null ? pip
					.getUpdatedDate().toString("dd/MM/yyyy") : null);
			pipdto.setPIPFlag(pip.getPIPFlag());
		}
		return pipdto;
	}

	public List<PIPDTO> toDTOList(List<PIP> pips) {
		List<PIPDTO> doTOList = null;
		if (pips != null) {
			doTOList = new ArrayList<PIPDTO>();
			for (PIP pip : pips) {
				doTOList.add(convertPIPToPIPDTO(pip));
			}
		}
		return doTOList;
	}

	public PIP editPIPDtoTOPIP(PIPDTO pipdto) {
		Long loggedInEmpl = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();

		PIP pips = null;
		if (pipdto != null) {
			pips = dao.findBy(PIP.class, pipdto.getId());

			if (pipdto.getRemarks() != null) {
				if (pipdto.getRemarks().equalsIgnoreCase("<br/>")) {
					pips.setRemarks(null);
				} else {
					pips.setRemarks(pipdto.getRemarks());
				}
			}
			if (pipdto.getImprovement() != null) {
				if (pipdto.getImprovement().equalsIgnoreCase("<br/>")) {
					pips.setImprovement(null);
				} else {
					pips.setImprovement(pipdto.getImprovement());
				}
			}
			pips.setRating(pipdto.getRating());
			try {
				pips.setStartDate(pipdto.getStartDate() != null ? DateParser
						.toDate(pipdto.getStartDate()) : null);
				pips.setEndDate(pipdto.getEndDate() != null ? DateParser
						.toDate(pipdto.getEndDate()) : null);
				pips.setExtendDate(pipdto.getExtendDate() != null ? DateParser
						.toDate(pipdto.getExtendDate()) : null);
			} catch (ParseException pe) {
				pe.printStackTrace();
			}
			pips.setPIPFlag(pipdto.getPIPFlag());
			pips.setUpdatedBy(loggedInEmpl);
			pips.setUpdatedDate(new Second());
		}

		return pips;
	}
}
