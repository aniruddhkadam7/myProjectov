package com.raybiztech.payslip.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.raybiztech.appraisals.properties.PropBean;

public class FileUploader {

	@SuppressWarnings("resource")
	public String uploadFile(MultipartFile file, PropBean propBean)
			throws IOException {
		FileOutputStream fileOutputStream = null;
		String fileName = null;
		String filepath = null;
		try {
			byte[] bytes = file.getBytes();

			String fileExtension = getFileExtension(file.getOriginalFilename());

			fileName = "PayslipExcelSheet" + "." + fileExtension;

			filepath = (String) propBean.getPropData().get("excelLocation");

			File finalFile = new File(filepath + fileName);

			fileOutputStream = new FileOutputStream(finalFile);

			fileOutputStream.write(bytes);

		} catch (FileNotFoundException exception) {

		}

		return filepath + fileName;
	}

	public String getFileExtension(String fileName) {
		String fileExtension = null;
		if (fileName != null) {
			int i = fileName.lastIndexOf('.');
			if (i > 0) {
				fileExtension = fileName.substring(i + 1);

			}
		}
		return fileExtension;
	}

}
