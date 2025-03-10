package com.raybiztech.projecttailoring.dao;



import java.util.List;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.projecttailoring.business.ProcessArea;
import com.raybiztech.projecttailoring.business.ProcessSubHead;
import com.raybiztech.projecttailoring.business.ProjectTailoring;
import com.raybiztech.projecttailoring.dto.ProcessSubHeadDto;

public interface ProjectTailoringDao extends DAO {

	ProjectTailoring getProjectTailoring(Long projectId);
	
	//Boolean checkForDuplicateOrder(Long processHeadId, String order);
	
	List<ProcessSubHead> getProcessSubHeadList(ProcessSubHeadDto processsubHeadDto,Long oldOrder);
	
	List<ProcessArea> getProcessAreas(Long categoryId);
	
	Boolean checkforDuplicateDoc(String docName);
	
	Boolean checkDuplicateProcess(String processName);
	
	Long getOrderCountOfActiveProcesses(Long categoryId);
}
