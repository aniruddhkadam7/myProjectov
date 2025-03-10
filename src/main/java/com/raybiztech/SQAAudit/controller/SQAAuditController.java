package com.raybiztech.SQAAudit.controller;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.SQAAudit.Exception.SQAAuditAlreadyExistsException;
import com.raybiztech.SQAAudit.dto.SQAAuditFormDto;
import com.raybiztech.SQAAudit.dto.SQAAuditTimelineDto;
import com.raybiztech.SQAAudit.service.SQAAuditService;

@Controller
@RequestMapping("/sqaAuditController")
public class SQAAuditController {

	@Autowired
	SQAAuditService sqaAuditServiceImpl;

	@RequestMapping(value = "/saveAuditForm", method = RequestMethod.POST)
	public @ResponseBody void saveAuditForm(@RequestBody SQAAuditFormDto dto) {
		sqaAuditServiceImpl.saveAuditForm(dto);
	}

	@RequestMapping(value = "/getProjectAuditList", params = { "projectId",
			"startIndex", "endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getProjectAuditList(
			@RequestParam Long projectId, @RequestParam Integer startIndex,
			@RequestParam Integer endIndex) {
		return sqaAuditServiceImpl.getProjectAuditList(projectId, startIndex,
				endIndex);
	}

	@RequestMapping(value = "/getAuditDetails", params = { "auditId" }, method = RequestMethod.GET)
	public @ResponseBody SQAAuditFormDto getAuditDetails(
			@RequestParam Long auditId) {
		return sqaAuditServiceImpl.getAuditDetails(auditId);
	}

	@RequestMapping(value = "/saveOrSubmitAuditForm", method = RequestMethod.POST)
	public @ResponseBody void saveOrSubmitAuditForm(
			@RequestBody SQAAuditFormDto dto,
			HttpServletResponse httpServletResponse) {
		try {
			sqaAuditServiceImpl.submitAuditForm(dto);
		} catch (SQAAuditAlreadyExistsException e) {
			httpServletResponse.setStatus(HttpServletResponse.SC_CONFLICT);
		}
	}

	@RequestMapping(value = "/updateAuditForm", method = RequestMethod.PUT)
	public @ResponseBody void updateAuditForm(@RequestBody SQAAuditFormDto dto) {
		sqaAuditServiceImpl.updateAuditForm(dto);
	}

	@RequestMapping(value = "/downloadSQAAuditFile", params = { "fileName" }, method = RequestMethod.GET)
	public @ResponseBody void downloadSQAAuditFile(
			@RequestParam String fileName, HttpServletResponse response) {
		sqaAuditServiceImpl.downloadSQAAuditFile(fileName, response);
	}

	@RequestMapping(value = "/deleteProjectAuditDetails", params = { "auditId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteProjectAuditDetails(
			@RequestParam Long auditId) {
		sqaAuditServiceImpl.deleteProjectAuditDetails(auditId);
	}

	@RequestMapping(value = "/getSQAAuditReport", params = { "startIndex",
			"endIndex", "multiSearch", "from", "to", "SQAAuditSelectionDate",
			"auditStatus", "auditRescheduleStatus" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getSQAAuditReport(
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam String multiSearch, @RequestParam String from,
			@RequestParam String to,
			@RequestParam String SQAAuditSelectionDate,
			@RequestParam String auditStatus,
			@RequestParam String auditRescheduleStatus) {
		return sqaAuditServiceImpl.getSQAAuditReport(startIndex, endIndex,
				multiSearch, from, to, SQAAuditSelectionDate, auditStatus,
				auditRescheduleStatus);

	}

	@RequestMapping(value = "/getSQAAuditTimelineDetails", params = { "auditId" }, method = RequestMethod.GET)
	public @ResponseBody List<SQAAuditTimelineDto> getSQAAuditTimelineDetails(
			@RequestParam Long auditId) {
		return sqaAuditServiceImpl.getSQAAuditTimelineDetails(auditId);
	}

	@RequestMapping(value = "/closeAudit", params = { "auditId" }, method = RequestMethod.PUT)
	public @ResponseBody void closeAudit(@RequestParam Long auditId) {
		sqaAuditServiceImpl.closeAudit(auditId);
	}

	@RequestMapping(value = "/exportSqaAuditReport", params = {
			"SQAAuditSelectionDate", "auditStatus", "auditRescheduleStatus",
			"startdate", "enddate", "multiSearch" }, method = RequestMethod.GET)
	public @ResponseBody void exportProjectList(
			@RequestParam String SQAAuditSelectionDate,
			@RequestParam String auditStatus,
			@RequestParam String auditRescheduleStatus,
			@RequestParam String startdate, @RequestParam String enddate,
			@RequestParam String multiSearch,
			HttpServletResponse httpServletResponse) throws Exception {

		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition",
				"attachment; filename=\"ProjectList.csv\"");

		ByteArrayOutputStream os = sqaAuditServiceImpl.exportSQAReport(
				SQAAuditSelectionDate, auditStatus, auditRescheduleStatus,
				startdate, enddate, multiSearch);

		httpServletResponse.getOutputStream().write(os.toByteArray());
	}

	@RequestMapping(value = "/getExportProjectAuditList", params = { "projectId" }, method = RequestMethod.GET)
	public @ResponseBody void exportProjectAuditList(
			@RequestParam Long projectId,
			HttpServletResponse httpServletResponse) throws Exception {
		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition",
				"attachment; filename=\"ProjectAuditList.csv\"");

		ByteArrayOutputStream os = sqaAuditServiceImpl
				.exportProjectAuditList(projectId);

		httpServletResponse.getOutputStream().write(os.toByteArray());
	}
	
	@RequestMapping(value = "/getNewSQAAuditTimelineDetails", params = { "auditId" }, method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> getNewSQAAuditTimelineDetails(
			@RequestParam Long auditId) {
		return sqaAuditServiceImpl.getNewSQAAuditTimelineDetails(auditId);
	}

}
