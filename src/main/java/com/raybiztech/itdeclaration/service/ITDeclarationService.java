package com.raybiztech.itdeclaration.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.raybiztech.itdeclaration.dto.FinanceCycleDTO;
import com.raybiztech.itdeclaration.dto.ITDeclarationFormDTO;
import com.raybiztech.itdeclaration.dto.ITDeclareInfoDTO;
import com.raybiztech.itdeclaration.dto.InvestmentDTO;
import com.raybiztech.itdeclaration.dto.SectionDTO;
import com.raybiztech.itdeclaration.exception.DuplicateITDeclareForm;
import com.raybiztech.itdeclaration.exception.NoFinanceCycle;
import com.raybiztech.itdeclaration.exception.UnAuthorisedITFormEdit;
import com.raybiztech.leavemanagement.exceptions.MailCantSendException;

public interface ITDeclarationService {
	ITDeclareInfoDTO getEmployeeData();
	
	void addCycle(FinanceCycleDTO financeCycleDTO);

	List<FinanceCycleDTO> getCycleDTOs();

	void editCycle(FinanceCycleDTO financeCycleDTO);

	void deleteCycle(Long cycleId);

	void addSection(SectionDTO sectionDTO);

	List<SectionDTO> listSectionDTOs();

	void editSection(SectionDTO sectionDTO);

	void deleteSection(Long sectionId);

	void addInvestment(InvestmentDTO investmentDTO)  throws Exception;

	List<InvestmentDTO> getInvestmentDTOs();

	void updateInvestment(InvestmentDTO investmentDTO);

	void deleteInvestment(Long investmentId);

	List<InvestmentDTO> getInvestsBySecId(Long sectionId);

	Long addDeclarationForm(@RequestBody ITDeclarationFormDTO declarationFormDTO)throws DuplicateITDeclareForm,MailCantSendException,NoFinanceCycle;
	
	List<ITDeclarationFormDTO> getITDeclarationFormDTOs();
	
	Boolean isSectionExist(String sectionName,Long sectionId);
	
	Boolean isInvestmentExist(Long sectionId,String investmentName,Long investmentId);
	
	Boolean isCycleExist(String cycleName,Long cycleId);
	
	Boolean isItDeclareExist()throws NoFinanceCycle;
	
	Boolean cycleOtherThanThis(Long cycleId);
	
	Map<String,Object> getITDeclarationPaginationList(Integer startIndex, Integer endIndex, Long cycleId,String employeeName);
	
	List<SectionDTO> getSectionsHavingInvests();
	
	void downloadITFormFile(HttpServletResponse response, String fileName);
	
	void uploadItFormDocs(MultipartFile mpf, String itDeclarationFormId);
	
	Long editItDeclareForm(ITDeclarationFormDTO itForm)throws MailCantSendException,UnAuthorisedITFormEdit;
	
	void isItFormEditable(Long itFormId)throws UnAuthorisedITFormEdit;

    void processAndStoreFileAsPdf(MultipartFile file, String userId, String year) throws Exception;
    byte[] getPdfFromDatabase(String userId, String year) throws SQLException;

}
