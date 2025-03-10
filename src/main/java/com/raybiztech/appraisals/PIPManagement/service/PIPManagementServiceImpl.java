package com.raybiztech.appraisals.PIPManagement.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.Replace;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.text.ReplaceableString;
import com.raybiztech.appraisals.PIPManagement.builder.PIPAuditBuilder;
import com.raybiztech.appraisals.PIPManagement.builder.PIPBuilder;
import com.raybiztech.appraisals.PIPManagement.business.PIP;
import com.raybiztech.appraisals.PIPManagement.dao.PIPDao;
import com.raybiztech.appraisals.PIPManagement.dto.PIPDTO;
import com.raybiztech.appraisals.PIPManagement.mailNotification.PIPMailNotification;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.observation.exceptions.DuplicateObservationException;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.assetmanagement.service.AssetManagementServiceImpl;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.mailtemplates.util.PerformanceManagementMailConfiguration;
import com.raybiztech.projectmanagement.builder.AuditBuilder;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.projectmanagement.service.ProjectService;
import com.raybiztech.recruitment.utils.DateParser;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.separation.business.ExitFeedBack;

/**
 *
 * @author aprajita
 */

@Service("pipManagementServiceImpl")
@Transactional
public class PIPManagementServiceImpl implements PIPManagementService,
		Cloneable {

	@Autowired
	DAO dao;

	@Autowired
	PIPDao pipDAOImpl;

	@Autowired
	PIPBuilder pipBuilder;

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	AuditBuilder auditBuilder;

	@Autowired
	PIPAuditBuilder pipAuditBuilder;

	@Autowired
	ProjectService projectService;

	@Autowired
	PerformanceManagementMailConfiguration performanceManagementMailConfiguration;

	Logger logger = Logger.getLogger(PIPManagementServiceImpl.class);

	/* add PIP */
	@Override
	public Long addPIP(PIPDTO pipDto) {

		Date fromDate = null;
		Date toDate = null;

		try {
			fromDate = DateParser.toDate(pipDto.getStartDate());
			toDate = DateParser.toDate(pipDto.getEndDate());
		} catch (ParseException ex) {
			java.util.logging.Logger.getLogger(
					AssetManagementServiceImpl.class.getName()).log(
					Level.SEVERE, null, ex);
		}

		DateRange dateRange = new DateRange(fromDate, toDate);

		PIP pip = pipBuilder.convertPIPDtoToPIPEntity(pipDto);
		Boolean duplication = pipDAOImpl.checkForDuplicatePIP(pip, dateRange);

		/*
		 * If there is PIP already given for the time period and employee from
		 * the same manager then we are not saving into data base
		 */
		if (duplication) {
			Long id = (Long) pipDAOImpl.save(pip);
			List<Audit> pipAudit = auditBuilder.auditTOPIPEntity(pip, id,
					"PIP", "CREATED");
			for (Audit audit : pipAudit) {
				pipDAOImpl.save(audit);
			}
			performanceManagementMailConfiguration.sendPIPMailNotification(pip);
			Long ID = pip.getId();

			return ID;
		}
		// throw exception if the PIP is already given
		else {
			throw new DuplicateObservationException(
					"The PIP was already added for this time period.");
		}
	}

	/* get all PIP List with pagination */
	@Override
	public Map<String, Object> getAllPIPList(Long empId, Integer startIndex,
			Integer endIndex, String multiSearch, String searchByEmployee,
			String searchByAdded, String from, String to, String dateSelection,
			String selectionStatus) {

		Date fromDate = null;
		Date toDate = null;

		if (dateSelection.equalsIgnoreCase("Custom")) {
			try {
				fromDate = DateParser.toDate(from);
				toDate = DateParser.toDate(to);
			} catch (ParseException ex) {
				java.util.logging.Logger.getLogger(
						AssetManagementServiceImpl.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		} else {
			Map<String, Date> dateMap = dao.getCustomDates(dateSelection);
			fromDate = dateMap.get("startDate");
			toDate = dateMap.get("endDate");
		}

		DateRange dateRange = new DateRange(fromDate, toDate);

		Employee employee = dao.findBy(Employee.class, empId);
		Map<String, Object> pipMap = null;

		Boolean totalListFlag = false;
		Boolean individualListFalg = false;
		Boolean hierarchyListFlag = false;

		Permission totalList = dao.checkForPermission("PIP List", employee);
		Permission individual = dao.checkForPermission("Individual PIP List",
				employee);
		Permission hierarchy = dao.checkForPermission("Hierarchy PIP List",
				employee);

		if (totalList.getView() && !individual.getView()
				&& !hierarchy.getView()) {
			totalListFlag = true;
		} else if (individual.getView() && totalList.getView()
				&& !hierarchy.getView()) {
			individualListFalg = true;
		} else if (hierarchy.getView() && totalList.getView()
				&& !individual.getView()) {
			hierarchyListFlag = true;
		}

		if (totalListFlag) {
			pipMap = pipDAOImpl.getAllPIPList(startIndex, endIndex,
					multiSearch, searchByEmployee, searchByAdded, dateRange,
					selectionStatus);
		}
		if (hierarchyListFlag) {
			List<Long> manager = projectService.mangerUnderManager(empId);
			pipMap = pipDAOImpl.getAllHierarchyPIPList(manager, startIndex,
					endIndex, multiSearch, searchByEmployee, searchByAdded,
					dateRange, selectionStatus);
		}
		if (individualListFalg) {
			pipMap = pipDAOImpl.getIndividualPIPList(empId, startIndex,
					endIndex);
		}

		List<PIP> pipList = (List) pipMap.get("list");
		Integer noOfRecords = (Integer) pipMap.get("size");
		List<PIPDTO> dtosList = pipBuilder.toDTOList(pipList);
		Map<String, Object> pMap = new HashMap<String, Object>();
		pMap.put("list", dtosList);
		pMap.put("size", noOfRecords);

		return pMap;
	}

	/* to view the PIP details */
	@Override
	public PIPDTO viewPipDetails(Long id) {
		return pipBuilder.convertPIPToPIPDTO(dao.findBy(PIP.class, id));
	}

	/* to update the PIP details */
	@Override
	public Long updatePipDetails(PIPDTO pipDto) {

		Date fromDate = null;
		Date toDate = null;

		try {
			fromDate = DateParser.toDate(pipDto.getStartDate());
			toDate = DateParser.toDate(pipDto.getEndDate());
		} catch (ParseException ex) {
			java.util.logging.Logger.getLogger(
					AssetManagementServiceImpl.class.getName()).log(
					Level.SEVERE, null, ex);
		}

		DateRange dateRange = new DateRange(fromDate, toDate);

		PIP pip = dao.findBy(PIP.class, pipDto.getId());
		PIP oldPIP = new PIP();
		try {
			oldPIP = (PIP) pip.clone();
		} catch (CloneNotSupportedException e) {
			java.util.logging.Logger.getLogger(
					PIPManagementServiceImpl.class.getName()).log(Level.SEVERE,
					null, e);
		}
		PIP pipdetails = pipBuilder.editPIPDtoTOPIP(pipDto);
		Boolean duplicate = pipDAOImpl.checkForDuplicatePIP(pipdetails,
				dateRange);

		/*
		 * If there is PIP already given for the time period and employee from
		 * the same manager then we are saving into data base
		 */
		if (duplicate) {
			dao.update(pipdetails);
			List<Audit> pipAudit = auditBuilder.UpdateAuditTOPIPEntity(
					pipdetails, pipDto.getId(), oldPIP, "PIP", "UPDATED");
			for (Audit audit : pipAudit) {
				pipDAOImpl.save(audit);
			}
			Long Id = pipdetails.getId();
			return Id;
		}
		// throw exception if the PIP is already given
		else {
			throw new DuplicateObservationException(
					"The PIP was already added for this time period.");
		}
	}

	/* to extend the PIP details */
	@Override
	public void extendPip(PIPDTO pipdto) {
		PIP p = dao.findBy(PIP.class, pipdto.getId());
		PIP oldP = new PIP();
		try {
			oldP = (PIP) p.clone();
		} catch (CloneNotSupportedException ce) {
			java.util.logging.Logger.getLogger(
					PIPManagementServiceImpl.class.getName()).log(Level.SEVERE,
					null, ce);
		}
		PIP extendPip = pipBuilder.editPIPDtoTOPIP(pipdto);
		dao.update(extendPip);

		List<Audit> exAudit = auditBuilder.UpdateAuditTOPIPEntity(extendPip,
				pipdto.getId(), oldP, "PIP", "UPDATED");
		for (Audit audit : exAudit) {
			pipDAOImpl.save(audit);
		}
	}

	/* to remove from PIP */
	@Override
	public void removeFromPip(PIPDTO pipdtos) {
		PIP rmpip = dao.findBy(PIP.class, pipdtos.getId());
		PIP oldPip = new PIP();
		try {
			oldPip = (PIP) rmpip.clone();
		} catch (CloneNotSupportedException ce) {
			java.util.logging.Logger.getLogger(
					PIPManagementServiceImpl.class.getName()).log(Level.SEVERE,
					null, ce);
		}
		PIP removePip = pipBuilder.editPIPDtoTOPIP(pipdtos);
		removePip.setPIPFlag(Boolean.FALSE);
		dao.update(removePip);

		List<Audit> emAudit = auditBuilder.UpdateAuditTOPIPEntity(removePip,
				pipdtos.getId(), oldPip, "PIP", "UPDATED");
		for (Audit rmaudit : emAudit) {
			pipDAOImpl.save(rmaudit);
		}
	}

	/* to get the PIP history */
	@Override
	public Map<String, Object> getPIPHistory(Long pipId, String filterName) {
		 /*String dbName = null;
		 if (filterName.equalsIgnoreCase("pipdata")) {
		dbName = filterName;
		 }*/
		Map<String, List<Audit>> map = dao.getAudit(pipId, filterName);
		return pipAuditBuilder.ToPIPAuditDTO(map);
	}

	/*export pip list*/ 
	@Override
	public ByteArrayOutputStream exportPIPListData(Long empId,String dateSelection,
			String selectionStatus, String from, String to,
			String searchByEmployee, String searchByAdded, String multiSearch)throws IOException {
		Date fromDate = null;
		Date toDate = null;

		if (dateSelection.equalsIgnoreCase("Custom")) {
			try {
				fromDate = DateParser.toDate(from);
				toDate = DateParser.toDate(to);
			} catch (ParseException ex) {
				java.util.logging.Logger.getLogger(
						AssetManagementServiceImpl.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		} else {
			Map<String, Date> dateMap = dao.getCustomDates(dateSelection);
			fromDate = dateMap.get("startDate");
			toDate = dateMap.get("endDate");
		}

		DateRange dateRange = new DateRange(fromDate, toDate);

		Employee employee = dao.findBy(Employee.class, empId);
		Map<String, Object> pipMap = null;

		Boolean totalListFlag = false;
		Boolean individualListFalg = false;
		Boolean hierarchyListFlag = false;

		Permission totalList = dao.checkForPermission("PIP List", employee);
		Permission individual = dao.checkForPermission("Individual PIP List",
				employee);
		Permission hierarchy = dao.checkForPermission("Hierarchy PIP List",
				employee);

		if (totalList.getView() && !individual.getView()
				&& !hierarchy.getView()) {
			totalListFlag = true;
		} else if (individual.getView() && totalList.getView()
				&& !hierarchy.getView()) {
			individualListFalg = true;
		} else if (hierarchy.getView() && totalList.getView()
				&& !individual.getView()) {
			hierarchyListFlag = true;
		}

		if (totalListFlag) {
			pipMap = pipDAOImpl.getAllPIPList(null, null,
					multiSearch, searchByEmployee, searchByAdded, dateRange,
					selectionStatus);
		}
		if (hierarchyListFlag) {
			List<Long> manager = projectService.mangerUnderManager(empId);
			pipMap = pipDAOImpl.getAllHierarchyPIPList(manager, null,
					null, multiSearch, searchByEmployee, searchByAdded,
					dateRange, selectionStatus);
		}
		if (individualListFalg) {
			pipMap = pipDAOImpl.getIndividualPIPList(empId, null,
					null);
		}

		List<PIP> pipList = (List) pipMap.get("list");
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int rowindex=1;
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row1 = sheet.createRow(0);
		Font font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 10);
		CellStyle style=workbook.createCellStyle();
		
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);
		
		Cell cell0 = row1.createCell(0);
		cell0.setCellStyle(style);
		cell0.setCellValue("Employee Name");
		
		Cell cell1 = row1.createCell(1);
		cell1.setCellStyle(style);
		cell1.setCellValue("Start Date");
		
		Cell cell2 = row1.createCell(2);
		cell2.setCellStyle(style);
		cell2.setCellValue("End Date");
		
		Cell cell3 = row1.createCell(3);
		cell3.setCellStyle(style);
		cell3.setCellValue("Rating");
		
		Cell cell4 = row1.createCell(4);
		cell4.setCellStyle(style);
		cell4.setCellValue("Reason for PIP");
		
		Cell cell5 = row1.createCell(5);
		cell5.setCellStyle(style);
		cell5.setCellValue("Added by");
		
		for(PIP obj:pipList){
			Row row = sheet.createRow(rowindex++);
			
			Cell cel0 = row.createCell(0);
			cel0.setCellValue(obj.getEmployee().getFullName());
			
			Cell cel1 = row.createCell(1);
			cel1.setCellValue(obj.getStartDate().toString());
			
			Cell cel2 = row.createCell(2);
			cel2.setCellValue(obj.getEndDate().toString());
			
			Cell cel3 = row.createCell(3);
			cel3.setCellValue(obj.getRating());
			
			Cell cel4 = row.createCell(4);
			cel4.setCellValue(obj.getRemarks().replaceAll("\\<[^>]*>",""));
			
			
			Cell cel5 = row.createCell(5);
			if(obj.getUpdatedBy() == null){
				if(obj.getCreatedBy() == null){
					cel5.setCellValue("N/A");
				}
				else{
					Employee employee3 = dao.findBy(Employee.class, obj.getCreatedBy());
					cel5.setCellValue(employee3.getFullName());
				}
			}
			else{
			Employee employee3 = dao.findBy(Employee.class, obj.getUpdatedBy());
			cel5.setCellValue(employee3.getFullName());
			}
			
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
		}
		workbook.write(bos);
		bos.flush();
		bos.close();
		return bos;
	}

	@Override
	public Long savePIPClearnceCertificate(Long employeeId) {
		
		ExitFeedBack exitFeedBack= new ExitFeedBack();
		Employee employee=dao.findBy(Employee.class, employeeId);
		exitFeedBack.setEmployee(employee);
		exitFeedBack.setIsPIP(true);
		Long exitFeedBackId=(Long) dao.save(exitFeedBack);
		
		employee.setStatusName("InActive");
		employee.setReleavingDate(new Date());
		
		dao.update(employee);
		return exitFeedBackId;
	}
}