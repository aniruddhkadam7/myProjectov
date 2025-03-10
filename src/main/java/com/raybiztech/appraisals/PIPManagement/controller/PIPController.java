package com.raybiztech.appraisals.PIPManagement.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.appraisals.PIPManagement.dto.PIPDTO;
import com.raybiztech.appraisals.PIPManagement.service.PIPManagementService;
import com.raybiztech.appraisals.observation.exceptions.DuplicateObservationException;
import com.raybiztech.appraisals.security.utils.SecurityUtils;

/**
 *
 * @author Aprajita
 */

@Controller
@RequestMapping("/PIPManagement")
public class PIPController {

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	PIPManagementService pipManagementServiceImpl;

	Logger logger = Logger.getLogger(PIPController.class);

	/* add PIP */
	@RequestMapping(value = "/addPIP", method = RequestMethod.POST)
	public @ResponseBody Long addPIP(@RequestBody PIPDTO pipDto,
			HttpServletResponse httpServletResponse) {
		Long id = null;
		try {
			id = pipManagementServiceImpl.addPIP(pipDto);
		} catch (DuplicateObservationException ex) {
			httpServletResponse.setStatus(HttpServletResponse.SC_CONFLICT);
		}
		return id;

	}

	/* get all PIP List */
	@RequestMapping(value = "/getAllPIPList", params = { "startIndex",
			"endIndex", "multiSearch", "searchByEmployee", "searchByAdded",
			"from", "to", "dateSelection", "selectionStatus" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAllPIPList(
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam String multiSearch,
			@RequestParam String searchByEmployee,
			@RequestParam String searchByAdded, @RequestParam String from,
			@RequestParam String to, @RequestParam String dateSelection,
			@RequestParam String selectionStatus) {
		return pipManagementServiceImpl.getAllPIPList(
				securityUtils.getLoggedEmployeeIdforSecurityContextHolder(),
				startIndex, endIndex, multiSearch, searchByEmployee,
				searchByAdded, from, to, dateSelection, selectionStatus);

	}

	/* to view PIP details */
	@RequestMapping(value = "/viewPipDetails", params = { "id" }, method = RequestMethod.GET)
	public @ResponseBody PIPDTO viewPipDetails(Long id,
			HttpServletResponse httpServletResponse) {
		return pipManagementServiceImpl.viewPipDetails(id);

	}

	/* to update PIP details */
	@RequestMapping(value = "/updatePipDetails", method = RequestMethod.PUT)
	public @ResponseBody Long updatePipDetails(@RequestBody PIPDTO pipDto,
			HttpServletResponse httpServletResponse) {
		Long id = null;
		try {
			id = pipManagementServiceImpl.updatePipDetails(pipDto);
		} catch (DuplicateObservationException ex) {
			httpServletResponse.setStatus(HttpServletResponse.SC_CONFLICT);
		}
		return id;
	}

	/* to extend PIP details */
	@RequestMapping(value = "/extendPip", method = RequestMethod.PUT)
	public @ResponseBody void extendPip(@RequestBody PIPDTO pipdto,
			HttpServletResponse httpServletResponse) {
		pipManagementServiceImpl.extendPip(pipdto);
	}

	/* to remove from PIP details */
	@RequestMapping(value = "/removeFromPip", method = RequestMethod.PUT)
	public @ResponseBody void removeFromPip(@RequestBody PIPDTO pipdtos,
			HttpServletResponse httpServletResponse) {
		pipManagementServiceImpl.removeFromPip(pipdtos);
	}

	/* to get PIP audit */
	@RequestMapping(value = "/getPIPHistory", params = { "pipId", "filterName" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getPIPHistory(
			@RequestParam Long pipId, @RequestParam String filterName,
			HttpServletResponse httpServletResponse) {
		return pipManagementServiceImpl.getPIPHistory(pipId, filterName);
	}

	@RequestMapping(value = "/exportPIPList",params = {"dateSelection","selectionStatus",
			"from","to","searchByEmployee","searchByAdded","multiSearch"}, method = RequestMethod.GET)
	public @ResponseBody void exportPIPList(@RequestParam String dateSelection,@RequestParam String selectionStatus,
			@RequestParam String from,@RequestParam String to,@RequestParam String searchByEmployee,
			@RequestParam String searchByAdded,@RequestParam String multiSearch,
			HttpServletResponse httpServletResponse)throws IOException{
		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"PIPList.csv\"");
		ByteArrayOutputStream os = pipManagementServiceImpl.exportPIPListData(securityUtils.getLoggedEmployeeIdforSecurityContextHolder(),
				dateSelection,selectionStatus,from,
				to,searchByEmployee,searchByAdded,multiSearch);
		httpServletResponse.getOutputStream().write(os.toByteArray());
	
			}
	@RequestMapping(value="/savePIPClearnceCertificate",params={"employeeId"},method=RequestMethod.POST)
	public @ResponseBody Long savePIPClearnceCertificate(@RequestParam Long employeeId)
	{
		
		Long exitFeedBackId=pipManagementServiceImpl.savePIPClearnceCertificate(employeeId);
		
		return exitFeedBackId;
	}
}
