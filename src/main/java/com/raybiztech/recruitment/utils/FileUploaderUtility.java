/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.raybiztech.SQAAudit.business.SQAAuditForm;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.Finance;
import com.raybiztech.appraisals.business.VisaDetails;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.observation.business.Observation;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.itdeclaration.business.ITDeclarationForm;
import com.raybiztech.meetingrequest.business.MeetingRequest;
import com.raybiztech.newsfeed.business.FeedPost;
import com.raybiztech.projectnewsfeed.business.ProjectFeedPost;
import com.raybiztech.recruitment.business.Candidate;
import com.raybiztech.recruitment.business.NewJoinee;
import com.raybiztech.separation.business.ExitFeedBack;
import com.raybiztech.supportmanagement.business.SupportTickets;

/**
 * 
 * @author hari
 */
@Component
public class FileUploaderUtility {

	Logger logger = Logger.getLogger(FileUploaderUtility.class);

	@Autowired
	private PropBean propBean;

	/**
	 * 
	 * @param propBean
	 */
	public FileUploaderUtility(PropBean propBean) {
		this.propBean = propBean;
	}

	/**
     *
     */
	public FileUploaderUtility() {
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

	/**
	 * 
	 * @param mpf
	 * @param propBean
	 * @return
	 * @throws Exception
	 */
	public String uploadDocument(MultipartFile mpf, PropBean propBean)
			throws IOException {
		FileOutputStream fos = null;
		String fileName = null;
		String filePath = null;
		try {
			byte[] bytes = mpf.getBytes();
			logger.debug("after get bytes");

			String fileExt = getFileExtesion(mpf.getOriginalFilename());

			// String fileBit = UUID.randomUUID().toString();
			fileName = "CandidateExcelFile" + "." + fileExt;

			logger.info("Prop imageLocation key value"
					+ propBean.getPropData().get("docLocation"));
			filePath = (String) propBean.getPropData().get("docLocation");

			logger.info("file path and name" + filePath + fileName);

			File someFile = new File(filePath + fileName);
			fos = new FileOutputStream(someFile);
			fos.write(bytes);
			logger.info("success fully uploaded");
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
		return filePath + fileName;
	}

	public String uploadCandidateDocument(Candidate candidate,
			MultipartFile mpf, PropBean propBean) throws IOException {
		FileOutputStream fos = null;
		String fileName = null;
		String filePath = null;
		try {
			byte[] bytes = mpf.getBytes();
			logger.debug("after get bytes");

			String fileExt = getFileExtesion(mpf.getOriginalFilename());

			String fileBit = UUID.randomUUID().toString();
			fileName = candidate.getFirstName() + " " + fileBit + "." + fileExt;

			logger.warn("Prop imageLocation key value"
					+ propBean.getPropData().get("docLocation"));
			filePath = (String) propBean.getPropData().get("docLocation");

			logger.warn("file path and name" + filePath + fileName);

			File someFile = new File(filePath + fileName);
			fos = new FileOutputStream(someFile);
			fos.write(bytes);
			// compressImage(someFile);
			logger.warn("success fully uploaded");
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
		return filePath + fileName;
	}

	/**
	 * 
	 * @param mpf
	 * @param employee
	 * @param propBean
	 * @return
	 * @throws java.io.IOException
	 */
	public Employee uploadImage(MultipartFile mpf, Employee employee,
			PropBean propBean) throws IOException {
		FileOutputStream fos = null;
		String fileName;
		String filePath;
		System.out.println("in upload");
		try {

			this.propBean = propBean;
		System.out.println("prop object" + propBean);
			filePath = propBean.getPropData().get("fileLocation").toString();
			System.out.println("image location" + filePath);

			fileName = employee.getEmployeeId() + "." +"jpeg";
					/*+ getFileExtesion(mpf.getOriginalFilename());*/

			if (!mpf.isEmpty()) {
				byte[] bytes = mpf.getBytes();
				System.out.println("file path and name" + filePath + fileName);
				File someFile = new File(filePath + fileName);
				fos = new FileOutputStream(someFile);
				fos.write(bytes);
				employee.setProfilePicPath(compressImage(someFile));
				System.out.println("profile:" + employee.getProfilePicPath());
				employee.setThumbPicture(generateThumnail(someFile, employee
						.getEmployeeId().toString()));
                System.out.println("thumb:" + employee.getThumbPicture());
				System.out.println("successfully uploaded");

			}

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

		return employee;
	}

	public Employee uploadBase64Image(byte[] bytes, Employee employee,
			PropBean propBean) throws IOException {
		System.out.println("in upload img");
		FileOutputStream fos = null;
		String fileName;
		String filePath;
		EmployeeDTO dto = new EmployeeDTO();
		try {

			this.propBean = propBean;
			filePath = propBean.getPropData().get("fileLocation").toString();
			System.out.println("image location" + filePath);

			fileName = employee.getEmployeeId() + ".jpeg";
			System.out.println("profilepicpath before image upload:" + employee.getProfilePicPath());
			File someFile = new File(filePath + fileName);
			if (!someFile.getParentFile().exists())
				someFile.getParentFile().mkdirs();
			if (!someFile.exists())
				someFile.createNewFile();
			fos = new FileOutputStream(someFile,false);
			fos.write(bytes);
	
			employee.setProfilePicPath(someFile.getAbsolutePath());
			System.out.println("get pp:" + employee.getProfilePicPath());
			employee.setThumbPicture(generateThumnail(someFile, employee
					.getEmployeeId().toString()));
			System.out.println("get t pic:" + employee.getThumbPicture());
			/*dto.setProfilePicPath(employee.getProfilePicPath());
			dto.setThumbPicture(employee.getThumbPicture());
*/
			System.out.println("successfully uploaded");

		} catch (FileNotFoundException fne) {
			logger.error(fne.getMessage(), fne);
		System.out.println("err:" + fne.getMessage());
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

		return employee;
	}

	public String generateThumnail(File file, String name) throws IOException {
		BufferedImage originalImage = ImageIO.read(file);
		Integer width = originalImage.getWidth();
		Integer height = originalImage.getHeight();
		Integer thumnnailMax = 125;
		String thumbnailPath = file.getParent() + "/" + name + "-Thumb.jpg";
		if (width > height) {
			if (width > thumnnailMax) {
				height = Math.round(height * thumnnailMax / width);
				width = thumnnailMax;
			}
		} else {
			if (height > thumnnailMax) {
				width = Math.round(width * thumnnailMax / height);
				height = thumnnailMax;
			}
		}
		ImageIO.write(Scalr.resize(originalImage, Method.ULTRA_QUALITY,
				Mode.AUTOMATIC, width, height, Scalr.OP_ANTIALIAS), "jpg",
				new File(thumbnailPath));
		return thumbnailPath;

	}

	private String compressImage(File file) throws IOException {

		BufferedImage originalImage = ImageIO.read(file);
		Integer width = originalImage.getWidth();
		Integer height = originalImage.getHeight();

		String resizeImagePath = file.getAbsolutePath();

		Integer maxPixels = 500;

		if (width > height) {
			if (width > maxPixels) {
				height = Math.round(height * maxPixels / width);
				width = maxPixels;
			}
		} else {
			if (height > maxPixels) {
				width = Math.round(width * maxPixels / height);
				height = maxPixels;
			}
		}

		ImageIO.write(Scalr.resize(originalImage, Method.ULTRA_QUALITY,
				Mode.AUTOMATIC, width, height, Scalr.OP_ANTIALIAS), "jpg",
				new File(resizeImagePath));

		return resizeImagePath;
	}

	public FeedPost uploadImage(MultipartFile mpf, FeedPost post,
			PropBean propBean) throws IOException {
		FileOutputStream fos = null;
		String fileName;
		String filePath;
		try {

			this.propBean = propBean;

			filePath = propBean.getPropData().get("newsFeedLocation")
					.toString();

			fileName = post.getId() + "."
					+ getFileExtesion(mpf.getOriginalFilename());
			logger.info("In file uploader util################# filePath"
					+ filePath + " ^^^^^^^^^^^^^^fileName " + fileName);
			if (!mpf.isEmpty()) {
				byte[] bytes = mpf.getBytes();
				logger.info("BYTES SIZEEEEE^^^^^^^^^^" + bytes);
				File someFile = new File(filePath + fileName);
				fos = new FileOutputStream(someFile);
				fos.write(bytes);
				compressImage(someFile);
				// generateThumnail(someFile, post.getId().toString());

				post.setPostImageData(filePath + fileName);

			}

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

		return post;
	}

	// /**
	// *

	public ProjectFeedPost uploadImage(MultipartFile mpf, ProjectFeedPost post,
			PropBean propBean) throws IOException {
		FileOutputStream fos = null;
		String fileName;
		String filePath;
		try {

			this.propBean = propBean;

			filePath = propBean.getPropData().get("projectNewsFeed").toString();

			fileName = post.getId() + "."
					+ getFileExtesion(mpf.getOriginalFilename());
			logger.info("In file uploader util################# filePath"
					+ filePath + " ^^^^^^^^^^^^^^fileName " + fileName);
			if (!mpf.isEmpty()) {
				byte[] bytes = mpf.getBytes();
				logger.info("BYTES SIZEEEEE^^^^^^^^^^" + bytes);
				File someFile = new File(filePath + fileName);
				fos = new FileOutputStream(someFile);
				fos.write(bytes);
				compressImage(someFile);
				generateThumnail(someFile, post.getId().toString());

				post.setPostImageData(filePath + fileName);

			}

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

		return post;
	}

	public String uploadFileInObservation(Observation candidate,
			MultipartFile mpf, PropBean propBean) throws IOException {
		FileOutputStream fos = null;
		String fileName = null;
		String filePath = null;
		try {
			byte[] bytes = mpf.getBytes();

			String fileExt = getFileExtesion(mpf.getOriginalFilename());

			// String fileBit = UUID.randomUUID().toString();
			String date = new Date().toString();

			fileName = candidate.getEmployee().getFirstName() + "_" + date
					+ "." + fileExt;

			filePath = propBean.getPropData().get("ObservationFiles")
					.toString();

			File someFile = new File(filePath + fileName);
			fos = new FileOutputStream(someFile);
			fos.write(bytes);
			// compressImage(someFile);

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
		return filePath + fileName;
	}

	// upload suppoert ticket documents
	public String uploadSupportTicketsDocuments(SupportTickets supportTickets,
			MultipartFile mpf, PropBean propBean) throws IOException {

		FileOutputStream fos = null;
		String fileName = null;
		String filePath = null;
		try {
			byte[] bytes = mpf.getBytes();
			logger.debug("after get bytes");

			String fileExt = getFileExtesion(mpf.getOriginalFilename());

			String fileBit = UUID.randomUUID().toString();
			String date = new Date().toString();
			fileName = date + "_" + supportTickets.getId() + fileBit + "."
					+ fileExt;

			logger.info("Prop imageLocation key value"
					+ propBean.getPropData().get("supportDocLocation"));
			filePath = (String) propBean.getPropData()
					.get("supportDocLocation");

			logger.info("file path and name" + filePath + fileName);

			File someFile = new File(filePath + fileName);
			fos = new FileOutputStream(someFile);
			fos.write(bytes);
			// compressImage(someFile);
			logger.info("success fully uploaded");
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
		return filePath + fileName;

	}


	public String uploadRBTCvDocument(Employee employee, MultipartFile mpf,
			PropBean propBean) throws IOException {
		FileOutputStream fos = null;
		String fileName = null;
		String filePath = null;
		try {
			byte[] bytes = mpf.getBytes();
			logger.debug("after get bytes");

			String fileExt = getFileExtesion(mpf.getOriginalFilename());
			fileName = employee.getEmployeeId() + "RBTCV" + "." + fileExt;

			logger.info("Prop imageLocation key value"
					+ propBean.getPropData().get("RBTCV"));
			filePath = (String) propBean.getPropData().get("RBTCV");

			logger.info("file path and name" + filePath + fileName);

			File someFile = new File(filePath + fileName);
			fos = new FileOutputStream(someFile);
			fos.write(bytes);
			// compressImage(someFile);
			logger.info("success fully uploaded");
		} catch (FileNotFoundException fne) {
			logger.info(fne.getMessage(), fne);
		} finally {
			try {
				if (fos != null) {
					fos.flush();
				}
			} catch (IOException ie) {
				logger.info(ie.getMessage(), ie);
			}
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException ie) {
				logger.info(ie.getMessage(), ie);
			}
		}
		return filePath + fileName;
	}
	// For uploading file while creating new joinee
	public String uploadFileForNewJoinee(NewJoinee joinee,
			MultipartFile mpf, PropBean propBean) throws IOException {
		FileOutputStream fos = null;
		String fileName = null;
		String filePath = null;
		try {
			byte[] bytes = mpf.getBytes();
			logger.debug("after get bytes");

			String fileExt = getFileExtesion(mpf.getOriginalFilename());

			String fileBit = UUID.randomUUID().toString();
			String date = new Date().toString();
			fileName = date + "_" + joinee.getId() + fileBit + "."
					+ fileExt;
			//fileName = joinee.getId().toString() + " " + fileBit +"newJoinee." + fileExt;

			logger.info("Prop imageLocation key value"
					+ propBean.getPropData().get("newJoinee"));
			filePath = (String) propBean.getPropData().get("newJoinee");


			File someFile = new File(filePath + fileName);
			fos = new FileOutputStream(someFile);
			fos.write(bytes);
			// compressImage(someFile);
			logger.info("success fully uploaded");
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
		return filePath + fileName;
	}

	//uploading relieving letter
	public String uploadrelievingletter(ExitFeedBack exitFeedBack,
			MultipartFile mpf, PropBean propBean) throws IOException {
		FileOutputStream fos = null;
		String fileName = null;
		String filePath = null;
		try {
			byte[] bytes = mpf.getBytes();
			logger.debug("after get bytes");

			String fileExt = getFileExtesion(mpf.getOriginalFilename());

			String fileBit = UUID.randomUUID().toString();
			String date = new Date().toString();
			fileName = date + "_" + exitFeedBack.getId() + fileBit + "."
					+ fileExt;
		

			logger.info("Prop imageLocation key value"
					+ propBean.getPropData().get("RelievingLetters"));
			filePath = (String) propBean.getPropData().get("RelievingLetters");


			File someFile = new File(filePath + fileName);
			fos = new FileOutputStream(someFile);
			fos.write(bytes);
			// compressImage(someFile);
			logger.info("success fully uploaded");
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
		return filePath + fileName;
	}
	public String uploadItFormDocument(ITDeclarationForm itForm,
			MultipartFile mpf, PropBean propBean) throws IOException {
		logger.info("id"+itForm.getItDeclarationFormId());
		FileOutputStream fos = null;
		String fileName = null;
		String filePath = null;
		try {
			byte[] bytes = mpf.getBytes();
			logger.debug("after get bytes");

			String fileExt = getFileExtesion(mpf.getOriginalFilename());
			String fileBit = UUID.randomUUID().toString();
			String date = new Date().toString();
			fileName = date + "_" + itForm.getItDeclarationFormId() + fileBit + "."
					+ fileExt;

			logger.info("Prop imageLocation key value"
					+ propBean.getPropData().get("itFormLocation"));
			filePath = (String) propBean.getPropData()
					.get("itFormLocation");

			logger.info("file path and name" + filePath + fileName);

			File someFile = new File(filePath + fileName);
			fos = new FileOutputStream(someFile);
			fos.write(bytes);
			logger.info("success fully uploaded");
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
		return filePath + fileName;

	}
	
	public String uploadExitfeedBackFile(ExitFeedBack exitFeedBack,
			MultipartFile mpf, PropBean propBean) throws IOException {
		FileOutputStream fos = null;
		String fileName = null;
		String filePath = null;
		try {
			byte[] bytes = mpf.getBytes();
			logger.debug("after get bytes");

			String fileExt = getFileExtesion(mpf.getOriginalFilename());

			String fileBit = UUID.randomUUID().toString();
			String date = new Date().toString();
			fileName = date + "_" + exitFeedBack.getId() + fileBit + "."
					+ fileExt;
		

			logger.info("Prop imageLocation key value"
					+ propBean.getPropData().get("ExitFeedbackForm"));
			filePath = (String) propBean.getPropData().get("ExitFeedbackForm");


			File someFile = new File(filePath + fileName);
			fos = new FileOutputStream(someFile);
			fos.write(bytes);
			// compressImage(someFile);
			logger.info("success fully uploaded");
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
		return filePath + fileName;
	}
	
	
	
	public String uploadEmployeeFinanceDetails(Finance finance, MultipartFile mpf,
			PropBean propBean) throws IOException {
		FileOutputStream fos = null;
		String fileName = null;
		String filePath = null;
		try {
			byte[] bytes = mpf.getBytes();
			logger.debug("after get bytes");

			String fileExt = getFileExtesion(mpf.getOriginalFilename());
			
			fileName = finance.getEmployee().getEmployeeId() + "FinanceDoc" + "." + fileExt;
		
			logger.info("Prop imageLocation key value"
					+ propBean.getPropData().get("FinanceDocuments"));
			filePath = (String) propBean.getPropData().get("FinanceDocuments");

			logger.info("file path and name" + filePath + fileName);

			File someFile = new File(filePath + fileName);
			fos = new FileOutputStream(someFile);
			fos.write(bytes);
			// compressImage(someFile);
			logger.info("success fully uploaded");
		} catch (FileNotFoundException fne) {
			logger.info(fne.getMessage(), fne);
		} finally {
			try {
				if (fos != null) {
					fos.flush();
				}
			} catch (IOException ie) {
				logger.info(ie.getMessage(), ie);
			}
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException ie) {
				logger.info(ie.getMessage(), ie);
			}
		}
		return filePath + fileName;
	}
	
	
	
	
	//Upload Passport details
	public Employee uploadPassportFrontCopy(MultipartFile mpf,Employee employee,PropBean propBean){
		FileOutputStream fos = null;
		String fileName;
		String filePath;
		try {
			this.propBean = propBean;
			logger.info("prop object" + propBean);
			filePath = propBean.getPropData().get("passportfrontdetails").toString();
			logger.info("passport details" + filePath);
			
			fileName = employee.getEmployeeId() + "."
					+ getFileExtesion(mpf.getOriginalFilename());
			if (!mpf.isEmpty()) {
				byte[] bytes = mpf.getBytes();
				logger.info("file path and name" + filePath + fileName);
				File someFile = new File(filePath + fileName);
				fos = new FileOutputStream(someFile);
				fos.write(bytes);
				employee.setPassportFrontPagePath(someFile.getAbsolutePath());
				logger.info("successfully uploaded");

			}

		} catch (Exception fne) {
			logger.error(fne.getMessage(), fne);
		}
		
		 finally {
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

		return employee;
	}
	//Upload details
	public Employee uploadPassportBackCopy(MultipartFile mpf,Employee employee,PropBean propBean){
		FileOutputStream fos = null;
		String fileName;
		String filePath;
		try {
			this.propBean = propBean;
			filePath = propBean.getPropData().get("passportbackdetails").toString();
			fileName = employee.getEmployeeId() + "."
					+ getFileExtesion(mpf.getOriginalFilename());
			
			if (!mpf.isEmpty()) {
				byte[] bytes = mpf.getBytes();
				File someFile = new File(filePath + fileName);
				fos = new FileOutputStream(someFile);
				fos.write(bytes);
				employee.setPassportBackPagePath(someFile.getAbsolutePath());
			}

		} catch (Exception fne) {
			logger.error(fne.getMessage(), fne);
		}
		
		 finally {
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

		return employee;
	}
	
	//upload visa
	public VisaDetails uploadVisaDetailsCopy(MultipartFile mpf,VisaDetails visadetails,PropBean propBean){
		FileOutputStream fos = null;
		String fileName;
		String filePath;
		try {
			this.propBean = propBean;
			logger.info("prop object" + propBean);
			filePath = propBean.getPropData().get("visadetails").toString();
			logger.info("visa details" + filePath);

			fileName = visadetails.getId() + "."
					+ getFileExtesion(mpf.getOriginalFilename());
			
			if (!mpf.isEmpty()) {
				byte[] bytes = mpf.getBytes();
				logger.info("file path and name" + filePath + fileName);
				File someFile = new File(filePath + fileName);
				fos = new FileOutputStream(someFile);
				fos.write(bytes);
				visadetails.setVisaDetailsPath(someFile.getAbsolutePath());
				visadetails.setVisaThumbPicture(generateThumnail(someFile, visadetails.getId().toString()));
				logger.info("successfully uploaded");

			}

		} catch (Exception fne) {
			logger.error(fne.getMessage(), fne);
		}
		
		 finally {
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

		return visadetails;
	}
	
	
	public String uploadOfferLetter(
			MultipartFile mpf, PropBean propBean) throws IOException {
		FileOutputStream fos = null;
		String fileName = null;
		String filePath = null;
		try {
			byte[] bytes = mpf.getBytes();
			logger.debug("after get bytes");

			String fileExt = getFileExtesion(mpf.getOriginalFilename());

			String fileBit = UUID.randomUUID().toString();
			String date = new Date().toString();
			
			logger.warn("documentname"+mpf.getOriginalFilename());
			fileName = "OfferLetter" + "_" + mpf.getOriginalFilename();
					

			logger.warn("Prop imageLocation key value"
					+ propBean.getPropData().get("offerLetter"));
			filePath = (String) propBean.getPropData().get("offerLetter");

			File someFile = new File(filePath + fileName);
			fos = new FileOutputStream(someFile);
			fos.write(bytes);
			// compressImage(someFile);
			logger.info("success fully uploaded");
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
		return filePath + fileName;
	}

	public String uploadFeedbackForm(MeetingRequest meeting, MultipartFile mpf, PropBean propBean) throws IOException {
		FileOutputStream fos = null;
		String fileName = null;
		String filePath = null;
		try {
		byte[] bytes = mpf.getBytes();
		String fileExt = getFileExtesion(mpf.getOriginalFilename());
		//logger.warn("ext:"+fileExt);
		//String fileBit = UUID.randomUUID().toString();
		//logger.warn("bit:"+fileBit);
		fileName = mpf.getOriginalFilename()  ;
		filePath = (String) propBean.getPropData().get("MeetingRequestFeedbackForm");
		//logger.warn("path:"+filePath);
		File file= new File(filePath + fileName);
		fos = new FileOutputStream(file);
		fos.write(bytes);
		//logger.warn("successfully uploaded");
		}
		catch (FileNotFoundException fne) {
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
		return filePath + fileName;
	}

	public String uploadSQAAuditFiles(SQAAuditForm form, MultipartFile mpf, PropBean propBean) throws IOException {
		FileOutputStream fos = null;
		String fileName = null;
		String filePath = null;
		try {
		byte[] bytes = mpf.getBytes();
		String fileExt = getFileExtesion(mpf.getOriginalFilename());
		fileName = mpf.getOriginalFilename();
		filePath = (String) propBean.getPropData().get("SQAAuditFiles");
		File file = new File(filePath+fileName);
		fos = new FileOutputStream(file);
		fos.write(bytes);
		}
		catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		}finally {
			try {
				if(fos != null) {
					fos.flush();
				}
			}catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
			try {
				if(fos != null) {
					fos.close();
				}
			}catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
			
		}
		return filePath+fileName;
	}
	
}
	
	




