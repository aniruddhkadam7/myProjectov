package com.raybiztech.projectmanagement.invoicesummary.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.raybiztech.projectmanagement.dto.ProjectDTO;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceDTO;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceSummaryDTO;

public interface InvoiceSummaryService {

	List<ProjectDTO> getAllProjects();

	List<InvoiceSummaryDTO> getInvoiceSummary(Long projectId, Long clientId);

	Map<String, Object> getInvoiceSummaryWithTotalRoundUpamounts(
			Long projectId, Long clientId);

	List<InvoiceDTO> getSentInvoiceForSummary(Long summaryId);

	List<InvoiceDTO> getReceviedInvoicesForSummary(Long summaryId);

	public ByteArrayOutputStream exportInvoiceSummary(Long projectId,
			Long clientId) throws IOException;

	public void calculateInvoiceSummary();

}
