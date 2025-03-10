package com.raybiztech.appraisals.service;

import java.util.List;

import com.raybiztech.appraisals.dto.CycleDTO;

public interface ManageAppraisalService {

    void addCycle(CycleDTO cycleDto);
	
	List<CycleDTO> getAllCycles();
	
	void changeCycleStatus(Long cycleId, String status);
	
}
