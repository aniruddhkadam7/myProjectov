package com.raybiztech.appraisals.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.builder.DesignationKrasBuilder;
import com.raybiztech.appraisals.builder.KRABuilder;
import com.raybiztech.appraisals.business.DesignationKras;
import com.raybiztech.appraisals.business.KRA;
import com.raybiztech.appraisals.business.KraWithWeightage;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.KRADTO;
import com.raybiztech.appraisals.dto.KraWithWeightageDTO;
import com.raybiztech.appraisals.employee.dao.EmployeeAppraisalDAO;

@Service("/designationKrasService")
@Transactional
public class DesignationKrasServiceImpl implements DesignationKrasService {

	public static Logger logger = Logger
			.getLogger(DesignationKrasServiceImpl.class);
	@Autowired
	DAO dao;

	@Autowired
	EmployeeAppraisalDAO employeeAppraisalDAO;

	@Autowired
	DesignationKrasBuilder designationBuilder;

	@Autowired
	KRABuilder kraBuilder;

	@Override
	public List<String> getAllDesignations() {
		// TODO Auto-generated method stub

		logger.info("in desigantions service");

		List<String> designations = dao.getByProperty(DesignationKras.class,
				"designationName");
		return designations;
	}

	@Override
	public List<KRADTO> getAllKRAsWithWeightage(
			String designationName) {
		// List<KraWithWeightage> krasWithWeightage =
		// dao.get(KraWithWeightage.class);
		List<KRA> kras = dao.get(KRA.class);
		
		DesignationKras designation = dao.findByDesignationName(
				DesignationKras.class, designationName);
		List<KraWithWeightage> krasWithWeightage = employeeAppraisalDAO
				.getKraWithWeightagesForADesignation(designation
						.getDesignationKRAsId());

		List<KraWithWeightageDTO> dtos = new ArrayList<KraWithWeightageDTO>();

		/*
		 * for(KraWithWeightage kraWithWeightage : krasWithWeightage){
		 * KraWithWeightageDTO dto = new KraWithWeightageDTO();
		 * dto.setId(kraWithWeightage.getKraWithWeightageId());
		 * dto.setWeightage(kraWithWeightage.getWeightage());
		 * dto.setKradto(kraBuilder.createKRADTO(kraWithWeightage.getKra()));
		 * dtos.add(dto); }
		 */
		for (KRA kra : kras) {
			for (KraWithWeightage kw : krasWithWeightage) {
				if (kw.getKra().getKraId() == kra.getKraId()) {
					kra.setKraWithWeightage(kw);
				}
			}
		}
		List<KRADTO> kradtos = kraBuilder.getKRADTOListForDesignationMapping(kras);
		
		logger.info("SIZE before return : " + kradtos.size());
		return kradtos;
	}

}
