package com.raybiztech.recruitment.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Status;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.recruitment.business.Candidate;
import com.raybiztech.recruitment.business.JobVacancy;

@Component("readExcelFile")
public class ReadExcelFile {
    
    @Autowired
    DAO dao;
    
    public List<Candidate> readExcelData(String fileName)
            throws NullPointerException {
        List<Candidate> candidateList = new ArrayList<Candidate>();
        
        try {
            // Create the input stream from the xlsx/xls file
            FileInputStream fis = new FileInputStream(fileName);

            // Create Workbook instance for xlsx/xls file input stream
            Workbook workbook = null;
            if (fileName.toLowerCase().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else if (fileName.toLowerCase().endsWith("xls")) {
                workbook = new HSSFWorkbook(fis);
            }

            // Get First sheet from the workbook
            Sheet sheet = workbook.getSheetAt(0);

            // Iterate start from the first sheet of the uploaded excel file
            Iterator<Row> rowIterator = sheet.iterator();
            
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Candidate candidate = new Candidate();
                candidate.setStatus("1");
                candidate.setCandidateInterviewStatus(Status.NEW);
                if (row.getRowNum() == 0) {
                    continue;// skip to read the first row of file
                }

                // For each row, iterate through each coulumns
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    if (cell.getColumnIndex() == 0) {
                        candidate.setFirstName(WordUtils.capitalizeFully(cell.getStringCellValue()));
                        
                    } else if (cell.getColumnIndex() == 1) {
                        candidate.setEmail(cell.getStringCellValue());
                        
                    } else if (cell.getColumnIndex() == 2) {
                        
                        candidate.setExperience(cell.getStringCellValue());
                    } else if (cell.getColumnIndex() == 3) {
                        
                        candidate.setSkill(cell.getStringCellValue());
                    } else if (cell.getColumnIndex() == 4) {
                        Double number = new Double(cell.getNumericCellValue());
                        candidate.setMobile(String.valueOf(number.longValue()));
                    } else if (cell.getColumnIndex() == 5) {
                        String positionVacant = cell.getStringCellValue();
                        JobVacancy jobVacancy = dao.findByJobVacancyByCode(JobVacancy.class, positionVacant);
                        if (jobVacancy.getPositionVacant() != null) {
                            candidate.setAppliedForLookUp(jobVacancy.getPositionVacant());
                            candidate.setAppliedFor(jobVacancy);
                        }
                    }
                }
                candidateList.add(candidate);
                
            }
            
            fis.close();
            
        } catch (FileNotFoundException fnfe) {
        } catch (IOException e) {
        }
        return candidateList;
    }
}
