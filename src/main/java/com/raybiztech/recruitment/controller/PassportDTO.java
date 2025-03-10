package com.raybiztech.recruitment.controller;

public class PassportDTO {
	private Long employeeId;
    private String extenstion;
    private byte[] passportFrontPage = null;
    private String photoURL;
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public String getExtenstion() {
		return extenstion;
	}
	public void setExtenstion(String extenstion) {
		this.extenstion = extenstion;
	}
	public byte[] getPassportFrontPage() {
		return passportFrontPage;
	}
	public void setPassportFrontPage(byte[] passportFrontPage) {
		this.passportFrontPage = passportFrontPage;
	}
	public String getPhotoURL() {
		return photoURL;
	}
	public void setPhotoURL(String photoURL) {
		this.photoURL = photoURL;
	}
	
	public PassportDTO(String extenstion1, byte[] passportFrontPage, String photoURL1,
            Long employeeId) {
		 this.extenstion = extenstion1;
	        if (passportFrontPage != null) {
	            this.passportFrontPage = passportFrontPage.clone();
	        }
	        this.photoURL = photoURL1;
	        this.employeeId = employeeId;
	}
	
	
    
}
