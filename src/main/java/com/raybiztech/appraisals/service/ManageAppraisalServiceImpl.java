package com.raybiztech.appraisals.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.builder.CycleBuilder;
import com.raybiztech.appraisals.business.Appraisal;
import com.raybiztech.appraisals.business.Cycle;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.CycleDTO;

@Service("manageAppraisalService")
@Transactional
public class ManageAppraisalServiceImpl implements ManageAppraisalService {

	@Autowired
	CycleBuilder cycleBuilder;

	@Autowired
	DAO dao;

	Logger logger = Logger.getLogger(ManageAppraisalServiceImpl.class);

	@Override
	public void addCycle(CycleDTO cycleDto) {
		logger.info("In Ssrevice Cycle dto is: " + cycleDto);
		cycleDto.setPercentage_Done(0);
		cycleDto.setStatus("NEW");
		Cycle cycle = cycleBuilder.createCycleEntity(cycleDto);
		logger.info("In Ssrevice Cycle  is: " + cycle);
		
		List<Employee> employees = dao.get(Employee.class);
		
		for(Employee employee : employees){
		    Appraisal appraisal = new Appraisal();
		    appraisal.setCycle(cycle);
		    appraisal.setEmployee(dao.findBy(Employee.class, employee.getEmployeeId()));
		    dao.save(appraisal);
		}
		
		dao.save(cycle);
	}

	@Override
	public List<CycleDTO> getAllCycles() {

		List<CycleDTO> cycleDtoList = cycleBuilder.getCycleDTOList(dao
				.get(Cycle.class));
		return cycleDtoList;
	}
	
	@Override
	public void changeCycleStatus(Long cycleId, String status){
	    Cycle cycle = dao.findBy(Cycle.class, cycleId);
	    cycle.setStatus(status);
	    dao.saveOrUpdate(cycle);
	}

}
