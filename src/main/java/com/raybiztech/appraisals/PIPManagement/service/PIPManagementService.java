package com.raybiztech.appraisals.PIPManagement.service;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.raybiztech.appraisals.PIPManagement.dto.PIPDTO;

/**
 *
 * @author aprajita
 */
public interface PIPManagementService {

	/* add PIP */
	Long addPIP(PIPDTO pipDto);

	/* get PIP with pagination */
	Map<String, Object> getAllPIPList(Long empId, Integer startIndex,
			Integer endIndex, String multiSearch, String searchByEmployee,
			String searchByAdded, String from, String to, String dateSelection,
			String selectionStatus);

	/* to view the PIP details */
	PIPDTO viewPipDetails(Long id);

	/* to update PIP details */
	Long updatePipDetails(PIPDTO pipDto);

	/* to extend PIP */
	void extendPip(PIPDTO pipdto);

	/* to extend PIP */
	void removeFromPip(PIPDTO pipdtos);

	/* to get pip history */
	Map<String, Object> getPIPHistory(Long projectId, String filterName);
	
	/*export pip list*/
	ByteArrayOutputStream exportPIPListData(Long empId,String dateSelection,String selectionStatus,String from,
			String to,String searchByEmployee,String searchByAdded,String multiSearch)throws IOException;

	Long savePIPClearnceCertificate(Long employeeId);
}
