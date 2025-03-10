package com.raybiztech.recruitment.controller;

public class PassportBackPageDTO {
	private Long employeeId;
    private String extenstion;
    private byte[] passportBackPage = null;
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
	public byte[] getPassportBackPage() {
		return passportBackPage;
	}
	public void setPassportBackPage(byte[] passportBackPage) {
		this.passportBackPage = passportBackPage;
	}
	public String getPhotoURL() {
		return photoURL;
	}
	public void setPhotoURL(String photoURL) {
		this.photoURL = photoURL;
	}
    
	public PassportBackPageDTO(String extenstion1, byte[] passportBackPage, String photoURL1,
            Long employeeId) {
		 this.extenstion = extenstion1;
	        if (passportBackPage != null) {
	            this.passportBackPage = passportBackPage.clone();
	        }
	        this.photoURL = photoURL1;
	        this.employeeId = employeeId;
	}

}
