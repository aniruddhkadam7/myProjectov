package com.raybiztech.itdeclaration.controller;

import java.awt.PageAttributes.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import java.sql.SQLException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.raybiztech.itdeclaration.business.FinanceCycle;
import com.raybiztech.itdeclaration.dao.ITDeclarationDao;
import com.raybiztech.itdeclaration.dto.FinanceCycleDTO;
import com.raybiztech.itdeclaration.dto.ITDeclarationFormDTO;
import com.raybiztech.itdeclaration.dto.ITDeclareInfoDTO;
import com.raybiztech.itdeclaration.dto.InvestmentDTO;
import com.raybiztech.itdeclaration.dto.SectionDTO;
import com.raybiztech.itdeclaration.exception.DuplicateITDeclareForm;
import com.raybiztech.itdeclaration.exception.NoFinanceCycle;
import com.raybiztech.itdeclaration.exception.UnAuthorisedITFormEdit;
import com.raybiztech.itdeclaration.service.ITDeclarationService;
import com.raybiztech.leavemanagement.exceptions.MailCantSendException;
import com.raybiztech.mailtemplates.util.ITDeclarationMailConfiguration;

import io.netty.handler.codec.http.HttpHeaders;

/*
 *///Denoting Class as Controller
@Controller
// Giving Class Level Url Mapping
@RequestMapping("/itDeclaration")
public class ITDeclarationFormController {
	@Autowired
	ITDeclarationMailConfiguration itDeclarationMailConfiguration;
	
	
	@Autowired
    private ITDeclarationService itDeclarationService;


	// Autowiring Service as ServiceInterface reference and Implementing Class
	// Object
	@Autowired
	ITDeclarationService iTDeclarationServiceImpl;
	
	@Autowired
	ITDeclarationDao iTDeclarationDaoImpl;
	
	Logger logger = Logger.getLogger(ITDeclarationFormController.class);

	// method to pass CycleDTO to service Layer from angular Service
	@RequestMapping(value = "/addCycle", method = RequestMethod.POST)
	public @ResponseBody void addCycle(
			@RequestBody FinanceCycleDTO financeCycleDTO) {

		iTDeclarationServiceImpl.addCycle(financeCycleDTO);
	}

	// method to get FinanaceCycleDTOs
	@RequestMapping(value = "/getCycles", method = RequestMethod.GET)
	public @ResponseBody List<FinanceCycleDTO> getCycles() {
		return iTDeclarationServiceImpl.getCycleDTOs();

	}
	
	

	
	// Endpoint to upload the folder
    @RequestMapping(value = "/uploadFolder", method = RequestMethod.POST)
    public @ResponseBody String uploadFolder(
            MultipartHttpServletRequest request, 
            @RequestParam("userId") String userId,
            @RequestParam("year") String year) {
        try {
            Iterator<String> fileNames = request.getFileNames();
            while (fileNames.hasNext()) {
                String fileName = fileNames.next();
                MultipartFile file = request.getFile(fileName);
                if (file != null && !file.isEmpty()) {
                    // Process each file and convert to PDF
                    itDeclarationService.processAndStoreFileAsPdf(file, userId, year);
                }
            }
            return "Files are being processed and stored successfully!";
        } catch (Exception e) {
            return "Error processing files: " + e.getMessage();
        }
    }
	
	
	
	
	
    
    
    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadFile(
            @RequestParam("userId") String userId,
            @RequestParam("year") String year) {
        try {
            // Fetch the PDF data from the database
            byte[] pdfData = iTDeclarationServiceImpl.getPdfFromDatabase(userId, year);

            if (pdfData == null || pdfData.length == 0) {
                // If file is not found or empty, return a 404 response
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Build headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", "file.pdf");
            headers.set("Content-Type", "application/pdf");
            headers.setContentLength(pdfData.length);

            // Return the PDF as a response
            return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);

        } catch (SQLException e) {
            // Log the exception (not shown here) and return an error response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    
    
    
    
    
    
    
    
	
	
	
	

	// method to pass SectionDTO to Service
	@RequestMapping(value = "/addSection", method = RequestMethod.POST)
	public @ResponseBody void addSection(@RequestBody SectionDTO sectionDTO) {
		iTDeclarationServiceImpl.addSection(sectionDTO);
	}

	// method to get SectionDTO's
	@RequestMapping(value = "/getSection", method = RequestMethod.GET)
	public @ResponseBody List<SectionDTO> getSectionList() {
		return iTDeclarationServiceImpl.listSectionDTOs();

	}

	// method to put updated FinanaceCycle Record
	@RequestMapping(value = "/editCycle", method = RequestMethod.PUT)
	public @ResponseBody void editCycle(
			@RequestBody FinanceCycleDTO financeCycleDTO) {
		iTDeclarationServiceImpl.editCycle(financeCycleDTO);
	}

	// method to delete Cycle record from DB
	/*
	 * here we are passing cycleId from angular Service and deleting Cycle
	 * Record
	 */
	@RequestMapping(value = "/deleteCycle", params = { "cycleId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteCycle(@RequestParam("cycleId") Long cycleId) {
		iTDeclarationServiceImpl.deleteCycle(cycleId);
	}

	// method to update Section Record
	@RequestMapping(value = "/editSection", method = RequestMethod.PUT)
	public @ResponseBody void editSection(@RequestBody SectionDTO sectionDTO) {
		iTDeclarationServiceImpl.editSection(sectionDTO);

	}

	// method to delete Section Record
	/*
	 * here we are passing sectionId from angular Service and deleting Section
	 * Record
	 */
	@RequestMapping(value = "/deleteSection", params = { "sectionId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteSection(
			@RequestParam("sectionId") Long sectionId) {
		iTDeclarationServiceImpl.deleteSection(sectionId);
	}

	// method to add Investment
	@RequestMapping(value = "/addInvestment", method = RequestMethod.POST)
	public @ResponseBody void addInvestment(
			@RequestBody InvestmentDTO investmentDTO,HttpServletResponse httpServletResponse) {
try{
		iTDeclarationServiceImpl.addInvestment(investmentDTO);
}catch(Exception exception){
	httpServletResponse.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
	
}
	}

	// method to get InvestmentDTO's
	@RequestMapping(value = "/getInvestments", method = RequestMethod.GET)
	public @ResponseBody List<InvestmentDTO> getInvestments() {
		return iTDeclarationServiceImpl.getInvestmentDTOs();

	}

	// method to update Investment Record
	@RequestMapping(value = "/updateInvestment", method = RequestMethod.PUT)
	public @ResponseBody void updateInvestment(
			@RequestBody InvestmentDTO investmentUpdateDTO) {
		iTDeclarationServiceImpl.updateInvestment(investmentUpdateDTO);

	}

	// method to deleteInvestment through investmentId
	@RequestMapping(value = "/deleteInvestment", params = { "investmentId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteInvestment(
			@RequestParam("investmentId") Long investmentId) {
		iTDeclarationServiceImpl.deleteInvestment(investmentId);
	}

	@RequestMapping(value = "/getInvestsBySecId", params = { "sectionId" }, method = RequestMethod.GET)
	public @ResponseBody List<InvestmentDTO> getInvestsBySecId(
			@RequestParam("sectionId") Long sectionId) {

		return iTDeclarationServiceImpl.getInvestsBySecId(sectionId);
	}

	// method to add itdeclarationform details
	@RequestMapping(value = "/addITDeclarationForm", method = RequestMethod.POST)
	public @ResponseBody Long addDeclarationForm(
			@RequestBody ITDeclarationFormDTO itFormDTO,HttpServletResponse response) {
		Long itFormId = null;
		try {
			itFormId= iTDeclarationServiceImpl.addDeclarationForm(itFormDTO);
			logger.info("try block");
		} catch (DuplicateITDeclareForm e) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			
		} catch (MailCantSendException e) {
			
			e.printStackTrace();
		}catch (Exception e) {
			
		}
		return itFormId;
	}


	@RequestMapping(value = "/getEmployee", method = RequestMethod.GET)
	public @ResponseBody ITDeclareInfoDTO getEmployeeData() {
		return iTDeclarationServiceImpl.getEmployeeData();
	}

	// method to get ITdeclaration pagination list
	@RequestMapping(value = "/getItDeclarationForm", params = { "startIndex",
			"endIndex", "cycleId", "employeeName" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getITDeclarationPaginationList(
			@RequestParam int startIndex, @RequestParam int endIndex,
			@RequestParam Long cycleId, @RequestParam String employeeName) {
		return iTDeclarationServiceImpl.getITDeclarationPaginationList(
				startIndex, endIndex, cycleId, employeeName);
	}
	@RequestMapping(value = "/exportITDeclarationList", params = { "cycleId",
			"searchname"}, method = RequestMethod.GET)
	@ResponseBody
	public void exportITDeclarationList(@RequestParam Long cycleId,
			@RequestParam String searchname,HttpServletResponse httpServletResponse)
			throws IOException {
		FinanceCycle cycle = iTDeclarationDaoImpl.activeFinanceCycle();
		httpServletResponse.setHeader("Content-Disposition",
				"attachment; filename=\"ITDECLARATIONFORMLIST.csv\"");
		if(cycle!=null){
			httpServletResponse.setHeader("Content-Disposition",
					"attachment; filename=\"ITDECLARATIONFORMLIST_"+cycle.getCycleName()+".csv\"");
		}
		httpServletResponse.setContentType("text/csv");
		ByteArrayOutputStream os = iTDeclarationServiceImpl.exportITDeclarationList(cycleId, searchname);
		httpServletResponse.getOutputStream().write(os.toByteArray());

	}

	@RequestMapping(value = "/isSectionExist", params = { "sectionName","sectionId" }, method = RequestMethod.GET)
	public @ResponseBody Boolean isSectionExist(@RequestParam String sectionName,@RequestParam Long sectionId) {
		return iTDeclarationServiceImpl.isSectionExist(sectionName,sectionId);
	}

	@RequestMapping(value = "/isInvestmentExist", params = { "sectionId","investmentName" ,"investmentId" }, method = RequestMethod.GET)
	public @ResponseBody Boolean isInvestmentExist(@RequestParam Long sectionId,
			@RequestParam String investmentName,@RequestParam Long investmentId) {
		return iTDeclarationServiceImpl.isInvestmentExist(sectionId, investmentName, investmentId);
	}

	@RequestMapping(value = "/isCycleExist", params = { "cycleName","cycleId" }, method = RequestMethod.GET)
	public @ResponseBody Boolean isCycleExist(@RequestParam String cycleName,@RequestParam Long cycleId) {
		return iTDeclarationServiceImpl.isCycleExist(cycleName,cycleId);

	}
	
	@RequestMapping(value = "/isItDeclareExist", method = RequestMethod.GET)
	public @ResponseBody Boolean isItDeclareExist(HttpServletResponse response) {
		Boolean isItDeclare = null;
		try {
			isItDeclare =  iTDeclarationServiceImpl.isItDeclareExist();
		} catch (NoFinanceCycle e) {
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		}
		return isItDeclare;
	}
	
	@RequestMapping(value = "/getSectionsHavingInvests" , method = RequestMethod.GET)
	public @ResponseBody List<SectionDTO> getSectionsHavingInvests() {
		return iTDeclarationServiceImpl.getSectionsHavingInvests();
	}

	@RequestMapping(value = "/uploadItDeclareDocuments", method = RequestMethod.POST)
	@Transactional
	@ResponseBody
	public void uploadItDeclareDocuments(MultipartHttpServletRequest request,
			HttpServletResponse response) {
		//HttpHeaders heade = request.getRequestHeaders();

		Iterator<String> itr = request.getFileNames();
		if (itr.hasNext()) {
			String file = itr.next();
			MultipartFile mpf = request.getFile(file);

			// request.getParameter("empId")
			iTDeclarationServiceImpl.uploadItFormDocs(mpf,
					request.getParameter("itDeclarationFormId"));
		}
	}
	//downloading browsed file
	@RequestMapping(value= "/downloadITFile", params = { "fileName" }, method = RequestMethod.GET)
	public @ResponseBody void downloadITFile(HttpServletResponse response,@RequestParam String fileName){
		iTDeclarationServiceImpl.downloadITFormFile(response, fileName);
		
	}
	
	//editing ITDeclarationForm
	@RequestMapping(value="/editItForm",method = RequestMethod.PUT)
	public @ResponseBody Long editITDeclarationForm(@RequestBody ITDeclarationFormDTO itFormDTO,HttpServletResponse httpServletResponse) {
		Long itFormId = null;
		try {
			itFormId = iTDeclarationServiceImpl.editItDeclareForm(itFormDTO);
		} catch (UnAuthorisedITFormEdit e) {
			httpServletResponse.setStatus(httpServletResponse.SC_FORBIDDEN);
			
		} catch (MailCantSendException e) {
			
		
		} 
		return itFormId;	
	}
	
	@RequestMapping(value="/isItFormEditable",params={"itFormId"},method = RequestMethod.POST)
	public @ResponseBody void isItFormEditable(@RequestParam Long itFormId,HttpServletResponse httpServletResponse) {
		try {
			iTDeclarationServiceImpl.isItFormEditable(itFormId);
		} catch (UnAuthorisedITFormEdit e) {
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}
		
	}
	
}
