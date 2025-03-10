package com.raybiztech.itdeclaration.service;

import java.io.BufferedReader;
import com.mysql.jdbc.PreparedStatement;
import java.sql.ResultSet;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.resource.cci.ResultSet;
import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.Finance;
import com.raybiztech.appraisals.exception.FileUploaderUtilException;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.appraisals.utils.FileUploaderUtil;
import com.raybiztech.employeeprofile.dao.EmployeeProfileDAOI;
import com.raybiztech.itdeclaration.builder.ITDeclarationBuilder;
import com.raybiztech.itdeclaration.builder.ITDeclarationFormBuilder;
import com.raybiztech.itdeclaration.builder.ITDeclarationInvestmentBuilder;
import com.raybiztech.itdeclaration.builder.ITDeclarationSectionBuilder;
import com.raybiztech.itdeclaration.business.FinanceCycle;
import com.raybiztech.itdeclaration.business.ITDeclarationForm;
import com.raybiztech.itdeclaration.business.Investment;
import com.raybiztech.itdeclaration.business.Section;
import com.raybiztech.itdeclaration.dao.ITDeclarationDao;
import com.raybiztech.itdeclaration.dto.FinanceCycleDTO;
import com.raybiztech.itdeclaration.dto.ITDeclarationFormDTO;
import com.raybiztech.itdeclaration.dto.ITDeclarationFormInvestmentsDTO;
import com.raybiztech.itdeclaration.dto.ITDeclarationFormSectionsDTO;
import com.raybiztech.itdeclaration.dto.ITDeclareInfoDTO;
import com.raybiztech.itdeclaration.dto.InvestmentDTO;
import com.raybiztech.itdeclaration.dto.SectionDTO;
import com.raybiztech.itdeclaration.exception.DuplicateITDeclareForm;
import com.raybiztech.itdeclaration.exception.NoFinanceCycle;
import com.raybiztech.itdeclaration.exception.UnAuthorisedITFormEdit;
import com.raybiztech.leavemanagement.exceptions.MailCantSendException;
import com.raybiztech.mailtemplates.util.ITDeclarationMailConfiguration;
import com.raybiztech.recruitment.utils.FileUploaderUtility;
import com.raybiztech.rolefeature.business.Permission;

//Denoting Class as MVC Service
@Service("iTDeclarationServiceImpl")
public class ITDeclarationServiceImpl implements ITDeclarationService {

	/*
	 * Autowiring builder's , service , dao IMPl
	 */
	@Autowired
	ITDeclarationBuilder iTDeclarationBuilder;

	@Autowired
	ITDeclarationSectionBuilder itDeclarationSectionBuilder;

	@Autowired
	ITDeclarationInvestmentBuilder iTDeclarationInvestmentBuilder;

	@Autowired
	ITDeclarationFormBuilder itDeclarationFormBuilder;

	@Autowired
	ITDeclarationDao iTDeclarationDaoImpl;

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	EmployeeProfileDAOI employeeProfileDAOIMPL;

	@Autowired
	PropBean propBean;

	@Autowired
	ITDeclarationMailConfiguration itDeclarationMailConfiguration;

	// adding Cycle record into DB through DAO
	@Override
	public void addCycle(FinanceCycleDTO financeCycleDTO) {
		iTDeclarationDaoImpl.addCycle(iTDeclarationBuilder
				.toEntity(financeCycleDTO));

	}

	// adding section record into DB through DAO
	@Override
	public void addSection(SectionDTO sectionDTO) {
		Section section = itDeclarationSectionBuilder
				.convertSectionDtotoEntity(sectionDTO);
		iTDeclarationDaoImpl.save(section);

	}
	
	
	
	
	
	
	
	

	
	public void processAndStoreFileAsPdf(MultipartFile file, String userId, String year) throws Exception {
	    String content = extractContentFromFile(file);
	    byte[] pdfContent = generatePdf(content);
	    storePdfInDatabase(pdfContent, userId, year);
	}

	private String extractContentFromFile(MultipartFile file) throws IOException {
	    String content = "";
	    String fileName = file.getOriginalFilename();

	    if (fileName != null && fileName.endsWith(".txt")) {
	        content = new String(file.getBytes(), "UTF-8");
	    } else if (fileName != null && fileName.endsWith(".xlsx")) {
	        content = extractContentFromExcel(file);
	    } else {
	        throw new IOException("Unsupported file type: " + fileName);
	    }

	    return content;
	}

	private String extractContentFromExcel(MultipartFile file) throws IOException {
	    StringBuilder content = new StringBuilder();
	    try (InputStream fis = file.getInputStream()) {
	        Workbook workbook = new XSSFWorkbook(fis);
	        Sheet sheet = workbook.getSheetAt(0);
	        for (Row row : sheet) {
	            for (Cell cell : row) {
	                content.append(cell.toString()).append(" ");
	            }
	            content.append("\n");
	        }
	    }
	    return content.toString();
	}

	private byte[] generatePdf(String content) throws DocumentException, IOException {
	    Document document = new Document();
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    PdfWriter.getInstance(document, baos);
	    document.open();
	    document.add(new Paragraph(content));
	    document.close();
	    return baos.toByteArray();
	}

	private void storePdfInDatabase(byte[] pdfContent, String userId, String year) throws SQLException {
	    String sql = "INSERT INTO file_data (user_id, year, pdf_data) VALUES (?, ?, ?)";
	    try (Connection connection = getConnection();
	         java.sql.PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setString(1, userId);
	        statement.setString(2, year);
	        statement.setBytes(3, pdfContent);
	        statement.executeUpdate();
	    } catch (SQLException e) {
	        throw new SQLException("Error storing PDF in database", e);
	    }
	}

	private Connection getConnection() throws SQLException {
	    return null;
	}

	@Override
	public byte[] getPdfFromDatabase(String userId, String year) throws SQLException {
	    String sql = "SELECT pdf_data FROM file_data WHERE user_id = ? AND year = ?";
	    byte[] pdfContent = null;

	    try (Connection connection = getConnection();
	         java.sql.PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setString(1, userId);
	        statement.setString(2, year);

	        try (ResultSet resultSet = statement.executeQuery()) {
	            if (resultSet.next()) {
	                pdfContent = resultSet.getBytes("pdf_data");
	            }
	        }
	    } catch (SQLException e) {
	        throw new SQLException("Error fetching PDF from database", e);
	    }

	    return pdfContent;
	}


    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	
	
	

	// Getting SectionDTO's
	@Override
	public List<SectionDTO> listSectionDTOs() {
		return itDeclarationSectionBuilder.toDTOList(iTDeclarationDaoImpl
				.get(Section.class));
	}

	// getting List Cycle Buisnes's and converting into List Cycle DTO's through
	// Builder
	@Override
	public List<FinanceCycleDTO> getCycleDTOs() {

		return iTDeclarationBuilder.toDTOList(iTDeclarationDaoImpl.getCycles());
	}

	// method to update FinanceCycle Record
	@Override
	public void editCycle(FinanceCycleDTO financeCycleDTO) {
		iTDeclarationDaoImpl.editCycle(iTDeclarationBuilder
				.toEditEntity(financeCycleDTO));
	}

	// method to delete Cycle Record
	@Override
	public void deleteCycle(Long cycleId) {
		iTDeclarationDaoImpl.deleteCycle(cycleId);

	}

	// method to add Investment Record
	@Override
	public void addInvestment(InvestmentDTO investmentDTO) throws Exception {
		Section section = iTDeclarationDaoImpl.findBy(Section.class,
				investmentDTO.getSectionId());
		Set<Investment> investments = section.getInvests();
		Long invLimit = 0l;
		Long dtoMax = investmentDTO.getMaxLimit();

		if (dtoMax == null) {
			investmentDTO.setMaxLimit(0l);
			iTDeclarationDaoImpl.save(iTDeclarationInvestmentBuilder
					.toEntity(investmentDTO));
		} else {
			for (Investment investment : investments) {
				invLimit = invLimit + investment.getMaxLimit();
			}
			invLimit = invLimit + dtoMax;
			if (section.getSectionLimit() >= invLimit) {
				iTDeclarationDaoImpl.save(iTDeclarationInvestmentBuilder
						.toEntity(investmentDTO));
			} else {
				throw new Exception("Amount exceeded max section limit ");
			}

		}

	}

	// method to get InvestmentDTO's
	@Override
	public List<InvestmentDTO> getInvestmentDTOs() {

		return iTDeclarationInvestmentBuilder.toDTOList(iTDeclarationDaoImpl
				.get(Investment.class));
	}

	// method to update Investment record
	@Override
	public void updateInvestment(InvestmentDTO investmentDTO) {
		iTDeclarationDaoImpl.saveOrUpdate(iTDeclarationInvestmentBuilder
				.toUpdateEntity(investmentDTO));
	}

	// method to delete Investment Record
	@Override
	public void deleteInvestment(Long investmentId) {
		iTDeclarationDaoImpl.delete(iTDeclarationDaoImpl.findBy(
				Investment.class, investmentId));
	}

	// method to delete Section Record

	@Override
	public void deleteSection(Long sectionId) {
		iTDeclarationDaoImpl.deleteSection(sectionId);

	}

	// method to update Section Record
	@Override
	public void editSection(SectionDTO sectionDTO) {
		iTDeclarationDaoImpl.editSection(itDeclarationSectionBuilder
				.toEditEntity(sectionDTO));

	}

	@Override
	public List<InvestmentDTO> getInvestsBySecId(Long sectionId) {
		return iTDeclarationInvestmentBuilder.toDTOList(iTDeclarationDaoImpl
				.getInvestsBySecId(sectionId));
	}

	// method to add ITDeclarationForm
	@Override
	public Long addDeclarationForm(ITDeclarationFormDTO declarationFormDTO)
			throws DuplicateITDeclareForm, MailCantSendException,
			NoFinanceCycle {
		ITDeclarationForm itDeclarationForm = itDeclarationFormBuilder
				.toEntity(declarationFormDTO);

		FinanceCycle financeCycle = iTDeclarationDaoImpl.activeFinanceCycle();

		itDeclarationForm.setFinanceCycle(financeCycle);
		// Session session =
		// iTDeclarationDaoImpl.getSessionFactory().getCurrentSession();
		// session.evict(itDeclarationForm);

		Long formId = iTDeclarationDaoImpl.addITDeclareForm(itDeclarationForm);
		itDeclarationMailConfiguration.sendITFormSubmitMail(formId);
		return formId;
	}

	// method to get List of ITDeclarationForm
	@Override
	public List<ITDeclarationFormDTO> getITDeclarationFormDTOs() {

		return itDeclarationFormBuilder.toDTOFormList(iTDeclarationDaoImpl
				.get(ITDeclarationForm.class));
	}

	@Override
	public ITDeclareInfoDTO getEmployeeData() {
		// TODO Auto-generated method stub
		Employee employee = iTDeclarationDaoImpl.findBy(Employee.class,
				securityUtils.getLoggedEmployeeIdforSecurityContextHolder());
		ITDeclareInfoDTO info = new ITDeclareInfoDTO();
		Finance finance = employeeProfileDAOIMPL
				.getEmplopyeeFinanceInfo(employee.getEmployeeId());
		info.setFullName(employee.getFullName());
		info.setEmployeeId(employee.getEmployeeId());
		info.setJoinDate(employee.getJoiningDate().toString("dd/MM/yyyy"));
		FinanceCycle cycle = iTDeclarationDaoImpl.activeFinanceCycle();
		info.setActiveCyle(cycle != null ? cycle.getStartDate().toString(
				"dd/MM/yyyy") : "N/A");
		info.setPan(finance != null ? finance.getPanCardAccountNumber() : "N/A");
		info.setDesignation(employee.getDesignation());

		return info;
	}

	@Override
	public Map<String, Object> getITDeclarationPaginationList(
			Integer startIndex, Integer endIndex, Long cycleId,
			String employeeName) {
		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");

		Permission itDeclareFormList = iTDeclarationDaoImpl.checkForPermission(
				"IT Declaration List", employee);

		Permission individualITDeclareFormList = iTDeclarationDaoImpl
				.checkForPermission("Individual IT Declaration List", employee);
		Map<String, Object> searchITFormList = null;
		if (itDeclareFormList.getView()
				&& !individualITDeclareFormList.getView()) {
			searchITFormList = iTDeclarationDaoImpl
					.getITDeclarationPaginationList(startIndex, endIndex,
							cycleId, employeeName);
		} else if (individualITDeclareFormList.getView()) {
			searchITFormList = iTDeclarationDaoImpl
					.getITDeclarationPaginationListIndividual(startIndex,
							endIndex, cycleId, employee.getEmployeeId());
		}

		List<ITDeclarationFormDTO> listDTOs = itDeclarationFormBuilder
				.toDTOFormList((List<ITDeclarationForm>) searchITFormList
						.get("itDeclarationFormlist"));

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("itformlistsize", searchITFormList.get("size"));
		map.put("itforms", listDTOs);
		return map;
	}

	@Override
	public Boolean isSectionExist(String sectionName, Long sectionId) {
		Section section = iTDeclarationDaoImpl.sectionFindByNameOrId(
				sectionName, sectionId);
		return section != null;
	}

	@Override
	public Boolean isInvestmentExist(Long sectionId, String investmentName,
			Long investmentId) {

		return iTDeclarationDaoImpl.investByNameOrId(sectionId, investmentName,
				investmentId) != null;
	}

	@Override
	public Boolean isCycleExist(String cycleName, Long cycleId) {
		FinanceCycle cycle = iTDeclarationDaoImpl.cycleFindByNameOrId(
				cycleName, cycleId);
		return cycle != null;
	}

	@Override
	public Boolean isItDeclareExist() throws NoFinanceCycle {
		ITDeclarationForm form = iTDeclarationDaoImpl
				.isItDeclareFormExist(securityUtils
						.getLoggedEmployeeIdforSecurityContextHolder());
		return form != null;
	}

	@Override
	public Boolean cycleOtherThanThis(Long cycleId) {

		return iTDeclarationDaoImpl.activeOtherThanThis(cycleId) == null ? false
				: true;
	}

	@Override
	public List<SectionDTO> getSectionsHavingInvests() {
		return itDeclarationSectionBuilder.toDTOList(iTDeclarationDaoImpl
				.getSectionsHavingInvests());
	}

	@Override
	public void downloadITFormFile(HttpServletResponse response, String fileName) {
		try {
			FileUploaderUtil util = new FileUploaderUtil();
			String filePath = (String) propBean.getPropData().get(
					"itFormLocation");
			util.downloadUploadedFile(response, fileName, filePath);
		} catch (Exception ex) {
			throw new FileUploaderUtilException(
					"Exception occured while uploading a file in Legal "
							+ ex.getMessage(), ex);
		}

	}

	public void uploadItFormDocs(MultipartFile mpf, String itDeclarationFormId) {
		if (itDeclarationFormId != null) {
			Long id = Long.parseLong(itDeclarationFormId);
			ITDeclarationForm itForm = iTDeclarationDaoImpl.findBy(
					ITDeclarationForm.class, id);
			FileUploaderUtility fileUploaderUtility = new FileUploaderUtility();
			try {
				String path = fileUploaderUtility.uploadItFormDocument(itForm,
						mpf, propBean);
				itForm.setFilePath(path);
				iTDeclarationDaoImpl.update(itForm);
			} catch (IOException ex) {

			}
		}

	}

	@Override
	public Long editItDeclareForm(ITDeclarationFormDTO itFormDTO)
			throws MailCantSendException, UnAuthorisedITFormEdit {
		// TODO Auto-generated method stub

		ITDeclarationForm itDeclration = iTDeclarationDaoImpl.findBy(
				ITDeclarationForm.class, itFormDTO.getItDeclarationFormId());

		if (itDeclration
				.getEmployee()
				.getEmployeeId()
				.equals(securityUtils
						.getLoggedEmployeeIdforSecurityContextHolder())) {
			ITDeclarationForm declarationForm = itDeclarationFormBuilder
					.toEditEntity(itFormDTO);
			iTDeclarationDaoImpl.saveOrUpdate(declarationForm);
			itDeclarationMailConfiguration.sendITFormSubmitMail(declarationForm
					.getItDeclarationFormId());
			return itFormDTO.getItDeclarationFormId();
		} else {
			throw new UnAuthorisedITFormEdit("UN Authorised User");

		}

	}

	@Override
	public void isItFormEditable(Long itFormId) throws UnAuthorisedITFormEdit {

		ITDeclarationForm itForm = iTDeclarationDaoImpl.findBy(
				ITDeclarationForm.class, itFormId);
		if (!itForm.getEmployee().getEmployeeId().equals(securityUtils.getLoggedEmployeeIdforSecurityContextHolder())
			|| !itForm.getFinanceCycle().getCycleId().equals(iTDeclarationDaoImpl.activeFinanceCycle().getCycleId())) {
			
			throw new UnAuthorisedITFormEdit(
					"You are not Authorised to edit this IT Declaration Form");
		}
		
	}

	public ByteArrayOutputStream exportITDeclarationList(Long employeeId,
			String search) throws IOException {

		Map<String, Object> map = this.getITDeclarationPaginationList(null,
				null, employeeId, search);
		List<ITDeclarationFormDTO> dtos = (List<ITDeclarationFormDTO>) map
				.get("itforms");
		Set<String> heading = new HashSet<String>();

		Map<String, Long> mapvalue = new HashMap<String, Long>();
		// Here I am getting all the form wise section and adding to one list,
		// at the same time I am adding to map where key is fromid+sectionname
		// and value is total investment for that section.
		for (ITDeclarationFormDTO formDTO : dtos) {
			for (ITDeclarationFormSectionsDTO sectionsDTO : formDTO
					.getFormSectionsDTOs()) {
				long value = 0l;
				for (ITDeclarationFormInvestmentsDTO investmentsDTO : sectionsDTO
						.getFormInvestmentDTO()) {
					value = value + investmentsDTO.getCustomAmount();
				}
				if (sectionsDTO.getIsOld()) {
					mapvalue.put(formDTO.getItDeclarationFormId() + ""
							+ sectionsDTO.getSectionName() + "", value);

					heading.add(sectionsDTO.getSectionName());
				} else {
					mapvalue.put(formDTO.getItDeclarationFormId()
							+ "Last Company " + sectionsDTO.getSectionName(),
							value);

					heading.add("Last Company " + sectionsDTO.getSectionName());
				}

			}
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		int rowIndex = 1;
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row1 = sheet.createRow(0);
		Font font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 10);

		CellStyle style = workbook.createCellStyle();

		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);

		Cell cell0 = row1.createCell(0);
		cell0.setCellValue("Employee ID");
		cell0.setCellStyle(style);

		Cell cell1 = row1.createCell(1);
		cell1.setCellValue("Employee Name");
		cell1.setCellStyle(style);

		Cell cell2 = row1.createCell(2);
		cell2.setCellValue("Total");
		cell2.setCellStyle(style);

		// Here I am setting the header of Excel from the list.
		for (Integer i = 0; i <= heading.size() - 1; i++) {
			row1.createCell(i + 3).setCellValue(
					new ArrayList<String>(heading).get(i));
			row1.getCell(i + 3).setCellStyle(style);
		}

		for (ITDeclarationFormDTO dto : dtos) {

			Row row = sheet.createRow(rowIndex++);

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(dto.getEmployeeId());

			Cell cel1 = row.createCell(1);
			cel1.setCellValue(dto.getEmployeeName());
			// Here I am setting the value for Excel from the map based on
			// fromid+sectionname key combination
			for (Integer i = 0; i <= heading.size() - 1; i++) {
				String index = dto.getItDeclarationFormId() + ""
						+ new ArrayList<String>(heading).get(i);
				row.createCell(i + 3).setCellValue(
						mapvalue.get(index) != null ? mapvalue.get(index) : 0l);
			}

			Cell cel2 = row.createCell(2);
			cel2.setCellValue(dto.getGrandTotal());

		}

		workbook.write(bos);
		bos.flush();
		bos.close();
		// for exporting code end here
		return bos;
	}

}
