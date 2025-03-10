package com.raybiztech.appraisals.utils;

/*
 * To change this template,  choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.raybiztech.appraisals.exception.FileUploaderUtilException;
import com.raybiztech.appraisals.observation.business.Observation;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.recruitment.business.Candidate;

/**
 *
 * @author ramesh
 * @deprecated this is deprecated from 3.1.2 build. this class we should move
 *             into docimaging project.
 */
@Deprecated
@Component
public class FileUploaderUtil {

	private static final Logger logger = Logger
			.getLogger(FileUploaderUtil.class);
	@Autowired
	private PropBean propBean;

	/**
	 *
	 * @param propBean
	 */
	public FileUploaderUtil(PropBean propBean) {
		this.propBean = propBean;
	}

	/**
     *
     */
	public FileUploaderUtil() {
	}

	/**
	 *
	 * @return
	 */
	public PropBean getPropBean() {
		return propBean;
	}

	/**
	 *
	 * @param propBean
	 */
	public void setPropBean(PropBean propBean) {
		this.propBean = propBean;
	}

	/**
	 *
	 * @param imgName
	 * @return
	 */
	public String getFileExtesion(String imgName) {
		String fileExtension = null;
		if (imgName != null) {
			int i = imgName.lastIndexOf('.');
			if (i > 0) {
				fileExtension = imgName.substring(i + 1);

			}
		}
		return fileExtension;
	}

	/**
	 *
	 * @param photoUrl
	 * @return
	 */
	public byte[] getFileByteData(String photoUrl) {
		if (photoUrl != null) {
			try {

				BufferedImage bufferedImage = ImageIO.read(new File(photoUrl));
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				ImageIO.write(bufferedImage, getFileExtesion(photoUrl),
						byteArrayOutputStream);
				return byteArrayOutputStream.toByteArray();

			} catch (IOException e) {
				logger.error(e);
				throw new FileUploaderUtilException(e.getMessage(), e);
			}
		}
		return new byte[] {};
	}

	/**
	 *
	 * @param mpf
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(MultipartFile mpf) throws IOException {

		FileOutputStream fos = null;
		String fileName = null;
		String filePath = null;
		try {
			byte[] bytes = mpf.getBytes();
			logger.debug("ofter get bytes");

			String fileExt = getFileExtesion(mpf.getOriginalFilename());

			String fileBit = UUID.randomUUID().toString();
			fileName = fileBit + "." + fileExt;

			logger.info("propBean value" + propBean);
			logger.info("propBean data  value" + propBean.getPropData());

			logger.info("Prop fileLocation key value"
					+ propBean.getPropData().get("docLocation"));
			filePath = (String) propBean.getPropData().get("docLocation");
			logger.info("file path and name" + filePath + fileName);

			File someFile = new File(filePath + fileName);
			fos = new FileOutputStream(someFile);
			fos.write(bytes);
			logger.info("success fully uploaded");
		} catch (FileNotFoundException fne) {
			logger.error(fne.getMessage(), fne);
			throw new FileUploaderUtilException(fne.getMessage(), fne);
		} finally {
			try {
				if (fos != null) {
					fos.flush();
				}
			} catch (IOException ie) {
				logger.error(ie.getMessage(), ie);
			}
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException ie) {
				logger.error(ie.getMessage(), ie);
			}
		}
		return filePath + fileName;
	}

	public void downloadFile(HttpServletResponse response, String fileName,
			PropBean propBean) throws IOException {

		String filePath = (String) propBean.getPropData().get("docLocation");
		
		logger.warn("filePath"+filePath);

		logger.info("File Name in uploader util : " + fileName);

		String fileNameWithPath = filePath + fileName;

		InputStream fileInputStream = null;
		PrintWriter printWriter = null;
		try {

			File downloadFile = new File(fileNameWithPath);

			fileInputStream = new FileInputStream(downloadFile);
			response.setHeader("Content-Disposition", "attachment;filename="
					+ "\"" + fileName + "\"");
			printWriter = response.getWriter();
			int charcter = -1;
			// Loop to read and write bytes.
			while ((charcter = fileInputStream.read()) != -1) {
				printWriter.print((char) charcter);
			}
			System.out.println("done 2");

		} catch (FileNotFoundException exception) {

		} finally {
			if (fileInputStream != null) {
				fileInputStream.close();
			}
			if (printWriter != null) {
				printWriter.flush();
				printWriter.close();
			}
		}
	}

	public String uploadDocument(MultipartFile mpf, PropBean propBean)
			throws IOException {
		FileOutputStream fos = null;
		String fileName = null;
		String filePath = null;
		String dummyName = null;
		try {
			byte[] bytes = mpf.getBytes();
			logger.debug("after get bytes");

			String fileExt = getFileExtesion(mpf.getOriginalFilename());

			fileName = mpf.getOriginalFilename() + "." + fileExt;

			logger.info("Prop fileLocation key value"
					+ propBean.getPropData().get("docLocation"));
			filePath = (String) propBean.getPropData().get("docLocation");

			logger.info("file path and name" + filePath
					+ mpf.getOriginalFilename());

			dummyName = java.util.UUID.randomUUID().toString();
			dummyName = dummyName + "." + fileExt;

			File someFile = new File(filePath + dummyName);
			fos = new FileOutputStream(someFile);
			fos.write(bytes);
			logger.info("success fully uploaded");
			/*
			 * adding dummy name for file
			 */

			logger.info("generated dummy name  is...." + dummyName);
			System.out.println("generated dummy name  is...." + dummyName);

		} catch (FileNotFoundException fne) {
			logger.error(fne.getMessage(), fne);
		} finally {
			try {
				if (fos != null) {
					fos.flush();
				}
			} catch (IOException ie) {
				logger.error(ie.getMessage(), ie);
			}
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException ie) {
				logger.error(ie.getMessage(), ie);
			}
		}
		// return filePath + fileName;
		return dummyName;
	}

	/**
	 * upload method for visitor message recorod visits
	 *
	 * @deprecated
	 * @param mpf
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public Map<String, String> uploadDocument(MultipartFile mpf, String filePath)
			throws IOException {
		FileOutputStream fos = null;
		String fileName;
		Map<String, String> map = new HashMap<String, String>();
		try {
			byte[] bytes = mpf.getBytes();
			logger.debug("after get bytes");

			String fileExt = getFileExtesion(mpf.getOriginalFilename());

			String fileBit = UUID.randomUUID().toString();
			fileName = fileBit + "." + fileExt;

			map.put("fileBit", fileBit);
			map.put("originalFileName", mpf.getOriginalFilename());
			map.put("fileExt", fileExt);
			map.put("filePath", filePath);

			logger.info("file path and name" + filePath + fileName);

			File someFile = new File(filePath + fileName);
			fos = new FileOutputStream(someFile);
			fos.write(bytes);
			logger.info("success fully uploaded");
		} catch (FileNotFoundException fne) {
			logger.error(fne);
			throw new FileUploaderUtilException(fne.getMessage(), fne);
		} finally {
			try {
				if (fos != null) {
					fos.flush();
				}
			} catch (IOException ie) {
				logger.error(ie.getMessage(), ie);
			}
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException ie) {
				logger.error(ie.getMessage(), ie);
			}
		}
		return map;

	}

	public String uploadImage(MultipartFile mpf, String path)
			throws IOException {
		FileOutputStream fos = null;
		String fileName = null;
		String filePath = null;
		try {
			byte[] bytes = mpf.getBytes();
			logger.debug("after get bytes");

			String fileExt = getFileExtesion(mpf.getOriginalFilename());

			String fileBit = UUID.randomUUID().toString();
			fileName = fileBit + "." + fileExt;

			filePath = path;

			logger.info("file path and name" + filePath + fileName);

			File someFile = new File(filePath + fileName);
			fos = new FileOutputStream(someFile);
			fos.write(bytes);
			logger.info("success fully uploaded");
		} catch (FileNotFoundException fne) {
			logger.error(fne);
			throw new FileUploaderUtilException(fne.getMessage(), fne);

		} finally {
			try {
				if (fos != null) {
					fos.flush();
				}
			} catch (IOException ie) {
				logger.error(ie.getMessage(), ie);
			}
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException ie) {
				logger.error(ie.getMessage(), ie);
			}
		}
		return filePath + fileName;
	}

	public void obsFiledownload(HttpServletResponse response, String fileName,
			PropBean propBean) throws IOException {

		String filePath = (String) propBean.getPropData().get(
				"ObservationFiles");
		String fileNameWithPath = filePath + fileName;
		InputStream fileInputStream = null;
		PrintWriter printWriter = null;
		try {

			File downloadFile = new File(fileNameWithPath);

			fileInputStream = new FileInputStream(downloadFile);
			response.setHeader("Content-Disposition", "attachment;filename="
					+ "\"" + fileName + "\"");
			printWriter = response.getWriter();
			int charcter = -1;
			// Loop to read and write bytes.
			while ((charcter = fileInputStream.read()) != -1) {
				printWriter.print((char) charcter);
			}

		} catch (FileNotFoundException exception) {

		} finally {
			if (fileInputStream != null) {
				fileInputStream.close();
			}
			if (printWriter != null) {
				printWriter.flush();
				printWriter.close();
			}
		}
	}

	// This method is used for we can download the files
	public void downloadUploadedFile(HttpServletResponse response,
			String fileName, String filePath) throws IOException {

		// String filePath = (String) propBean.getPropData().get("docLocation");

		logger.info("File Name in uploader util : " + fileName);

		String fileNameWithPath = filePath + fileName;

		InputStream fileInputStream = null;
		PrintWriter printWriter = null;
		try {

			File downloadFile = new File(fileNameWithPath);

			fileInputStream = new FileInputStream(downloadFile);
			response.setHeader("Content-Disposition", "attachment;filename="
					+ "\"" + fileName + "\"");
			printWriter = response.getWriter();
			int charcter = -1;
			// Loop to read and write bytes.
			while ((charcter = fileInputStream.read()) != -1) {
				printWriter.print((char) charcter);
			}

		} catch (FileNotFoundException exception) {

		} finally {
			if (fileInputStream != null) {
				fileInputStream.close();
			}
			if (printWriter != null) {
				printWriter.flush();
				printWriter.close();
			}
		}
	}

	public void downloadRBTCv(HttpServletResponse response, String fileName,
			PropBean propBean) throws IOException {

		String filePath = (String) propBean.getPropData().get("RBTCV");

		logger.info("File Name in uploader util : " + fileName);

		String fileNameWithPath = filePath + fileName;

		InputStream fileInputStream = null;
		PrintWriter printWriter = null;
		try {

			File downloadFile = new File(fileNameWithPath);

			fileInputStream = new FileInputStream(downloadFile);
			response.setHeader("Content-Disposition", "attachment;filename="
					+ "\"" + fileName + "\"");
			printWriter = response.getWriter();
			int charcter = -1;
			// Loop to read and write bytes.
			while ((charcter = fileInputStream.read()) != -1) {
				printWriter.print((char) charcter);
			}
			System.out.println("done 2");

		} catch (FileNotFoundException exception) {

		} finally {
			if (fileInputStream != null) {
				fileInputStream.close();
			}
			if (printWriter != null) {
				printWriter.flush();
				printWriter.close();
			}
		}
	}
	
	
	public void downloadOfferLetter(HttpServletResponse response, String fileName,
			PropBean propBean ,String filePath) throws IOException {

		//String filePath = (String) propBean.getPropData().get("OfferLetter");
		
		logger.warn("File path in uploader util : " + filePath);

		logger.warn("File Name in uploader util : " + fileName);

		String fileNameWithPath = filePath + fileName;
		
		logger.warn("Full path in uploader util : " + fileNameWithPath);

		InputStream fileInputStream = null;
		PrintWriter printWriter = null;
		try {

			File downloadFile = new File(fileNameWithPath);

			fileInputStream = new FileInputStream(downloadFile);
			response.setHeader("Content-Disposition", "attachment;filename="
					+ "\"" + fileName + "\"");
			printWriter = response.getWriter();
			int charcter = -1;
			// Loop to read and write bytes.
			while ((charcter = fileInputStream.read()) != -1) {
				printWriter.print((char) charcter);
			}
			System.out.println("done 2");

		} catch (FileNotFoundException exception) {

		} finally {
			if (fileInputStream != null) {
				fileInputStream.close();
			}
			if (printWriter != null) {
				printWriter.flush();
				printWriter.close();
			}
		}
	}

	public void downloadFeedbackForm(HttpServletResponse response, String fileName, PropBean propBean) throws IOException {
		
		String filePath = (String) propBean.getPropData().get("MeetingRequestFeedbackForm");
		
		String fileNameWithPath =  filePath + fileName ;
		
		InputStream fileInputStream = null;
		PrintWriter printWriter = null;
		
		try {
			File downloadFile = new File(fileNameWithPath);
			fileInputStream = new FileInputStream(downloadFile);
			response.setHeader("Content-Disposition", "attachment;filename="
					+ "\"" + fileName + "\"");
			printWriter = response.getWriter();
			int character = -1;
			while((character = fileInputStream.read()) != -1) {
				printWriter.print((char) character);
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally {
			if (fileInputStream != null) {
				fileInputStream.close();
			}
			if (printWriter != null) {
				printWriter.flush();
				printWriter.close();
			}
		}
		
	}

	public void downloadSQAAuditFile(HttpServletResponse response, String fileName, PropBean propBean) throws IOException {
		String filePath = (String) propBean.getPropData().get("SQAAuditFiles");
		
		String fileNameWithPath = filePath + fileName;
		
		InputStream fileInputStream = null;
		PrintWriter printWriter = null;
		
		
		try {
			File downloadfile = new File(fileNameWithPath);
			fileInputStream = new FileInputStream(downloadfile);
			response.setHeader("Content-Disposition", "attachment;filename="
					+ "\"" + fileName + "\"");
			printWriter = response.getWriter();
			int character = -1;
			while((character = fileInputStream.read()) != -1) {
				printWriter.print((char)character);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(fileInputStream != null) {
				fileInputStream.close();
			}
			if(printWriter != null) {
				printWriter.flush();
				printWriter.close();
			}
		}
		
	}
	

}
