package com.raybiztech.separation.service;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.raybiztech.separation.business.PrimaryReason;
import com.raybiztech.separation.chart.SeparationChart;
import com.raybiztech.separation.dto.ClearanceCertificateDTO;
import com.raybiztech.separation.dto.ExitFeedBackDTO;
import com.raybiztech.separation.dto.SeparationDTO;

public interface SeparationService {

	void saveResignationDetails(SeparationDTO separationDTO, HttpServletResponse response);

	List<PrimaryReason> getPrimaryReasonList();

	SeparationDTO getEmployeeInfo();

	List<SeparationDTO> getResignationList();

	Map<String, Object> getSeparationForm();

	/*SeparationDTO getSeparation(Long separationId,HttpServletResponse res);*/

	/*void revokeResignation(Long separationId);*/
	SeparationDTO getSeparation(Long separationId);
	
	
	void revokeResignation(SeparationDTO separation);

	void updateSeparation(SeparationDTO separation);

	/* void checkComments(SeparationCommentsDTO separationCommentsDTO); */

	void update(SeparationDTO separation);

	void saveClearanceCertificateComments(ClearanceCertificateDTO clearanceDTO);

	Map<String, Object> getResignationList(String multipleSearch, Integer startIndex, Integer endIndex,
			String dateSelection, String from, String to, String status, String empStatus);

	void IntitiateCC(Long separationId);

	List<ClearanceCertificateDTO> getClearanceDetails(Long separationId, String submittedBy);

	Long saveExitFeedBackForm(ExitFeedBackDTO exitFeedBackDTO);

	SeparationDTO getEmpDetails(Long separationId);

	ExitFeedBackDTO checkExitFeedBackForm(Long separationId);

	void uploadrelievingletter(MultipartFile mpf, Long exitFeedbakformId);
	
	void uploadExitfeedBackFile(MultipartFile mpf, Long exitFeedbakformId);

	SeparationDTO getEmployeeSepartion(Long employeeId);

	void updateCC(ClearanceCertificateDTO clearanceCertificateDTO);

	void downloadFile(HttpServletResponse response, String fileName);
	
	void downloadForm(HttpServletResponse response, String fileName);

	public SeparationChart getSeparationChart(String dateselection, String from, String to);

	ByteArrayOutputStream exportResignationList( String status, String dateSelection, String from,
			String to, String multiplesearch,String empStatus) throws Exception;

	SeparationDTO getEmployeeResg();
	
	SeparationDTO getEmployeePIP(Long employeeId);

	/*void abscondedEmployee(SeparationDTO separationDTO) throws ParseException;
	*/
    void sendMailToEmployeePersonalEmail(Long separationId);

}
