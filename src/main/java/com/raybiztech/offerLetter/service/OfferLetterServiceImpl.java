package com.raybiztech.offerLetter.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.exception.FileUploaderUtilException;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.appraisals.utils.FileUploaderUtil;
import com.raybiztech.date.Date;
import com.raybiztech.offerLetter.builder.OfferLetterBuilder;
import com.raybiztech.offerLetter.business.OfferLetter;
import com.raybiztech.offerLetter.dao.OfferLetterDao;
import com.raybiztech.offerLetter.dto.OfferLetterDto;
import com.raybiztech.recruitment.utils.FileUploaderUtility;

@Service("offerLetterServiceImpl")
public class OfferLetterServiceImpl implements OfferLetterService {
	
	
	@Autowired
	PropBean propBean;
	
	@Autowired
	SecurityUtils securityUtils;
	
	@Autowired
	OfferLetterDao offerLetterDaoImpl;
	
	@Autowired
	OfferLetterBuilder offerLetterbuilder;
	
	Logger logger = Logger.getLogger(OfferLetterServiceImpl.class);
	

	@Override
	public void uploadOfferLetterDocument(MultipartFile mpf) {
		
		FileUploaderUtility fileUploaderUtility = new FileUploaderUtility();
		String path = null;
		try {
			path = fileUploaderUtility.uploadOfferLetter(
					mpf, propBean);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		
		OfferLetter offerLetter = new OfferLetter();
		offerLetter.setCreatedBy(employee);
		offerLetter.setOfferLetterdoc(path);
		offerLetter.setCreatedDate(new Date());
		offerLetterDaoImpl.save(offerLetter);
		
	}


	@Override
	public Map<String, Object> getListOfOfferLetters() {
		
		Map<String, Object> map = null;
		
		map = offerLetterDaoImpl.getListOfOfferLetters();
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		//List<OfferLetterDto> list = 
		
		logger.warn("list details"+map.get("size"));
		
		List<OfferLetterDto> dtoList = offerLetterbuilder
				.toDtoList((List) map.get("list"));
		
		logger.warn("resultant list"+dtoList);
		
		
		result.put("list", dtoList );
		result.put("size", map.get("size"));
		
		
		return result;
	}


	@Override
	public void downloadOfferLetter(HttpServletResponse response,
			String fileName) {
		
		String filePath = (String) propBean.getPropData().get("offerLetter");
		
		logger.warn("filepath"+filePath);
		
		try {
			FileUploaderUtil util = new FileUploaderUtil();
			util.downloadOfferLetter(response, fileName, propBean ,filePath);
		} catch (Exception ex) {
			throw new FileUploaderUtilException(
					"Exception occured while uploading a file in Legal "
							+ ex.getMessage(), ex);
		}
		
		
				
	}


	@Override
	public ByteArrayOutputStream exportOfferLettersList() throws Exception {
		
		logger.warn("in service");
		
	    Map<String, Object> map = null;
		
		map = offerLetterDaoImpl.getListOfOfferLetters();
		
		
		List<OfferLetterDto> dtoList = offerLetterbuilder
				.toDtoList((List) map.get("list"));
		
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
		cell0.setCellValue("Offer Letter");
		cell0.setCellStyle(style);

		Cell cell1 = row1.createCell(1);
		cell1.setCellValue("Created By");
		cell1.setCellStyle(style);

		Cell cell2 = row1.createCell(2);
		cell2.setCellValue("Created Date");
		cell2.setCellStyle(style);

		for (OfferLetterDto list : dtoList) {
			Row row = sheet.createRow(rowIndex++);

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(list.getOfferLetterName());

			Cell cel1 = row.createCell(1);
			cel1.setCellValue(list.getCreatedBy());

			Cell cel2 = row.createCell(2);
			cel2.setCellValue(list.getCreatedDate());

			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
		}

		workbook.write(bos);
		bos.flush();
		bos.close();

		return bos;

	}
	
	
	

	

}
