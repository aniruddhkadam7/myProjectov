package com.raybiztech.appraisals.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.raybiztech.appraisals.builder.DesignationKrasBuilder;
import com.raybiztech.appraisals.builder.KRABuilder;
import com.raybiztech.appraisals.business.DesignationKras;
import com.raybiztech.appraisals.business.KRA;
import com.raybiztech.appraisals.business.KraWithWeightage;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dao.kra.KRADao;
import com.raybiztech.appraisals.dto.KRADTO;

@Service("kraToRoleService")
public class AssignKraToDesignationServiceImpl implements
		AssignKraToDesignationService {

	@Autowired
	DAO dao;
	@Autowired
	DesignationKrasBuilder designationBuilder;
	@Autowired
	KRABuilder kraBuilder;
	
	@Autowired
	private KRADao kraDao;

	public static Logger logger = Logger
			.getLogger(AssignKraToDesignationServiceImpl.class);

	@Override
	public void assignKras(String designationName, KRADTO[] krasWithWeightage) {

		DesignationKras designation = dao.findByDesignationName(
				DesignationKras.class, designationName);
		logger.info("Supplied designation name : "
				+ designation.getDesignationName());
		int count = kraDao.removeKRAWithWeightage(designation);
		logger.info(count +"records removed from KRAWithWeightage");
		for (KRADTO dto : krasWithWeightage) {
		/*	KraWithWeightage existingKraWeightage = dao.findBy(
					KraWithWeightage.class, dto.getKraWithWeightageDTO()
							.getId());
			logger.info("Existing designation Name : "
					+ existingKraWeightage.getDesignation()
							.getDesignationName());

			if (existingKraWeightage.getDesignation().getDesignationName()
					.equals(designation.getDesignationName())) {
				logger.info("inside if...");

				existingKraWeightage.setWeightage(dto.getKraWithWeightageDTO()
						.getWeightage());
				existingKraWeightage.setDesignation(designation);
				dao.update(existingKraWeightage);

			} else {*/

				logger.info("inside else...");
				KraWithWeightage newKraWithWeightage = new KraWithWeightage();

				newKraWithWeightage.setDesignation(designation);
				newKraWithWeightage.setKra(dao.findBy(KRA.class, dto.getId()));
				newKraWithWeightage.setWeightage(dto.getKraWithWeightageDTO()
						.getWeightage());

				dao.save(newKraWithWeightage);

			}
		}
	}
