package com.raybiztech.projectmanagement.invoicesummary.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.projectmanagement.dto.ProjectDTO;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceDTO;
import com.raybiztech.projectmanagement.invoicesummary.service.InvoiceSummaryService;

@Controller
@RequestMapping("/invoicesummary")
public class InvoiceSummaryController {

	private final InvoiceSummaryService invoicesummaryServiceImpl;

	@Autowired
	public InvoiceSummaryController(InvoiceSummaryService invoiceSummaryService) {
		this.invoicesummaryServiceImpl = invoiceSummaryService;
	}

	@RequestMapping(value = "/getProjects", method = RequestMethod.GET)
	public @ResponseBody List<ProjectDTO> getProjects() {
		return invoicesummaryServiceImpl.getAllProjects();
	}

	/*
	 * @RequestMapping(value = "/getInvoiceSummary", params = { "projectId",
	 * "clientId" }, method = RequestMethod.GET) public @ResponseBody
	 * List<InvoiceSummaryDTO> getInvoiceSummary(
	 * 
	 * @RequestParam Long projectId, @RequestParam Long clientId) {
	 * 
	 * return invoicesummaryServiceImpl.getInvoiceSummary(projectId, clientId);
	 * }
	 */

	@RequestMapping(value = "/getInvoiceSummary", params = { "projectId",
			"clientId" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getInvoiceSummary(
			@RequestParam Long projectId, @RequestParam Long clientId) {

		return invoicesummaryServiceImpl
				.getInvoiceSummaryWithTotalRoundUpamounts(projectId, clientId);
	}

	@RequestMapping(value = "/getInvoiceSummaryInCSV", params = { "projectId",
			"clientId" }, method = RequestMethod.GET)
	public @ResponseBody void getInvoiceSummaryInCSV(
			@RequestParam Long projectId, @RequestParam Long clientId,
			HttpServletResponse httpServletResponse) throws IOException {

		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition",
				"attachment; filename=\"SummaryList.csv\"");
		ByteArrayOutputStream os = invoicesummaryServiceImpl
				.exportInvoiceSummary(projectId, clientId);
		httpServletResponse.getOutputStream().write(os.toByteArray());

	}

	@RequestMapping(value = "/getSentInvoices", params = { "summaryId" }, method = RequestMethod.GET)
	public @ResponseBody List<InvoiceDTO> getSentInvoice(
			@RequestParam Long summaryId) {
		return invoicesummaryServiceImpl.getSentInvoiceForSummary(summaryId);
	}

	@RequestMapping(value = "/getReceivedInvoices", params = { "summaryId" }, method = RequestMethod.GET)
	public @ResponseBody List<InvoiceDTO> getReceivedInvoices(
			@RequestParam Long summaryId) {
		return invoicesummaryServiceImpl
				.getReceviedInvoicesForSummary(summaryId);
	}

	// DONT DELETE//
	// This is used to calculate invoice summary ONLY INVOICE STATUS RECEVIED
	// AND SENT //
	@RequestMapping(value = "/invoiceSummaryCalculation", method = RequestMethod.GET)
	public @ResponseBody void invoiceSummaryCalculation() {
		invoicesummaryServiceImpl.calculateInvoiceSummary();
	}

}
