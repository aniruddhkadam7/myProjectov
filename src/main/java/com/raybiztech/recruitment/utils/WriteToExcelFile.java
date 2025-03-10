/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.utils;

/**
 *
 * @author hari
 */
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.recruitment.dto.ScheduledCadidateDTO;

@Component("writeToExcelFile")
public class WriteToExcelFile {

    @Autowired
    PropBean propBean;

    public void writeScheduleCandidateListToFile(List<ScheduledCadidateDTO> scheduleCandidatesList) throws Exception {
        Workbook workbook = new HSSFWorkbook();

        Sheet sheet = workbook.createSheet("Scheduled Candidates");

        Iterator<ScheduledCadidateDTO> iterator = scheduleCandidatesList.iterator();

        int rowIndex = 1;
        int serialNum = 0;
        Row row = sheet.createRow(0);

        Cell cell00 = row.createCell(0);
        cell00.setCellValue("S.No");

        Cell cell01 = row.createCell(1);
        cell01.setCellValue("Candidate Name");

        Cell cell02 = row.createCell(2);
        cell02.setCellValue("Email Id");

        Cell cell03 = row.createCell(3);
        cell03.setCellValue("Contact Number");

        Cell cell04 = row.createCell(4);
        cell04.setCellValue("Experience");

        Cell cell05 = row.createCell(5);
        cell05.setCellValue("Skills");

        Cell cell06 = row.createCell(6);
        cell06.setCellValue("Applied For");

        Cell cell07 = row.createCell(7);
        cell07.setCellValue("Interviewer");

        Cell cell08 = row.createCell(8);
        cell08.setCellValue("Interview Timings");

        while (iterator.hasNext()) {
            ScheduledCadidateDTO scdto = iterator.next();
            row = sheet.createRow(rowIndex++);
            
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(++serialNum);

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(scdto.getCandidateName());

            Cell cell2 = row.createCell(2);
            cell2.setCellValue(scdto.getCandidateEmail());

            Cell cell3 = row.createCell(3);
            cell3.setCellValue(scdto.getCandidateMobile());

            Cell cell4 = row.createCell(4);
            cell4.setCellValue(scdto.getExperience());

            Cell cell5 = row.createCell(5);
            cell5.setCellValue(scdto.getCandidateskill());

            Cell cell6 = row.createCell(6);
            cell6.setCellValue(scdto.getAppliedForVacancy());

            Cell cell7 = row.createCell(7);
            cell7.setCellValue(scdto.getInterviewer());

            Cell cell8 = row.createCell(8);
            cell8.setCellValue(scdto.getInterviewDate() + " at " + scdto.getTime());

        }
        String filePath = (String) propBean.getPropData().get("docLocation");
        //write the excel data to file 
        FileOutputStream fos = new FileOutputStream(filePath + "ScheduledCandidates.xls");
        workbook.write(fos);
        fos.close();

    }

}
