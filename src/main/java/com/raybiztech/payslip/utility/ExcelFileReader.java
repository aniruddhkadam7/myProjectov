package com.raybiztech.payslip.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.raybiztech.payslip.dto.PayslipDto;

@Component("excelFileReader")
public class ExcelFileReader {

	public List<PayslipDto> readExcelFile(String path) throws IOException

	{

		List<PayslipDto> excelList = new ArrayList<PayslipDto>();

		FileInputStream fileInputStream = new FileInputStream(new File(path));
		DataFormatter dataFormatter = new DataFormatter();
		Workbook workbook = null;
		FormulaEvaluator formulaEvaluator = null;
		if (path.toLowerCase().endsWith("xlsx")) {
			workbook = new XSSFWorkbook(fileInputStream);
			formulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
		} else if (path.toLowerCase().endsWith("xls")) {
			workbook = new HSSFWorkbook(fileInputStream);
			formulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
		}

		Sheet sheet = workbook.getSheetAt(0);

		Iterator<Row> iterator = sheet.rowIterator();

		while (iterator.hasNext()) {

			Row row = (Row) iterator.next();

			if (row.getRowNum() >= 5) {

				Iterator<Cell> cellIterator = row.cellIterator();

				PayslipDto dto = new PayslipDto();

				while (cellIterator.hasNext()) {

					Cell cell = (Cell) cellIterator.next();

					switch (cell.getColumnIndex()) {

					case 0:
						dto.setSerialNo(cell.getNumericCellValue());
						break;
					case 1:
						formulaEvaluator.evaluate(cell);
						String Datecellvalue = dataFormatter.formatCellValue(
								cell, formulaEvaluator);
						dto.setJoiningDate(Datecellvalue);
						break;
					case 2:
						dto.setEmployeeId(Math.round(cell.getNumericCellValue()));
						break;
					case 3:
						formulaEvaluator.evaluate(cell);
						String cellvalue = dataFormatter.formatCellValue(cell,
								formulaEvaluator);
						dto.setAccountNo(cellvalue);

						break;
					case 4:
						dto.setDesignation(cell.getStringCellValue());
					case 5:
						dto.setName(cell.getStringCellValue());
						break;
					case 6:
						dto.setGrossSalary((long) cell.getNumericCellValue());
						break;

					case 7:
						dto.setVariablePayPercentage((long) cell
								.getNumericCellValue());
						break;
					case 8:
						dto.setVariablePay((long) cell.getNumericCellValue());
						break;
					case 9:
						dto.setGrossSalAfterVariablepay((long) cell
								.getNumericCellValue());
					case 10:
						dto.setBasicSalary((long) cell.getNumericCellValue());
						break;
					case 11:
						dto.setHouseRentAllowance((long) cell
								.getNumericCellValue());
						break;
					case 12:
						dto.setTransportAllowance((long) cell
								.getNumericCellValue());
						break;
					case 13:
						dto.setOtherAllowance((long) cell.getNumericCellValue());
						break;
					case 14:
						dto.setAbsent((int) (cell.getNumericCellValue()));
						break;
					case 15:
						dto.setLossOfPay((int) (cell.getNumericCellValue()));
						break;
					case 16:
						dto.setMedicliam((long) cell.getNumericCellValue());
						break;
					case 17:
						dto.setEsi((long) cell.getNumericCellValue());
						break;
					case 18:
						dto.setEpf((long) cell.getNumericCellValue());
						break;

					case 19:
						dto.setGratuity((long) cell.getNumericCellValue());
						break;
					case 20:
						dto.setAdvArrears((long) cell.getNumericCellValue());
						break;
					case 21:
						dto.setErc((long) cell.getNumericCellValue());
						break;
					case 22:
						dto.setTaxDeductionScheme((long) cell
								.getNumericCellValue());
						break;
					case 23:
						dto.setProfessionalTax((long) cell
								.getNumericCellValue());
						break;
					case 24:
						dto.setMealsCard((long) cell.getNumericCellValue());
						break;
					case 25:
						dto.setDonation((long) cell.getNumericCellValue());
						break;	
					case 26:
						dto.setArrears((long) cell.getNumericCellValue());
						break;
					case 27:
						dto.setIncentive((long) cell.getNumericCellValue());
						break;
					case 28:
						dto.setVpayable((long) cell.getNumericCellValue());
						break;
					case 29:
						dto.setNetSalary((long) cell.getNumericCellValue());
						break;
					case 30:
						dto.setRemarks(cell.getStringCellValue());
						break;
					
					}
				}
				if (dto.getEmployeeId() != 0 && dto.getBasicSalary() != 0
						&& !dto.getAccountNo().equals("")) {
					excelList.add(dto);
				}

			}
		}

		return excelList;

	}

}
