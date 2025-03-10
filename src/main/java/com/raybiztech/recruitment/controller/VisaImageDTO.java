package com.raybiztech.recruitment.controller;

public class VisaImageDTO {
	 	private Long id;
	    private String extenstion;
	    private byte[] visaDetailsData = null;
	    private String photoURL;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getExtenstion() {
			return extenstion;
		}
		public void setExtenstion(String extenstion) {
			this.extenstion = extenstion;
		}
		public byte[] getVisaDetailsData() {
			return visaDetailsData;
		}
		public void setVisaDetailsData(byte[] visaDetailsData) {
			this.visaDetailsData = visaDetailsData;
		}
		public String getPhotoURL() {
			return photoURL;
		}
		public void setPhotoURL(String photoURL) {
			this.photoURL = photoURL;
		}
		public VisaImageDTO(String extenstion1, byte[] visaDetailsData1, String photoURL1,
	            Long id1) {
			 this.extenstion = extenstion1;
		        if (visaDetailsData1 != null) {
		            this.visaDetailsData = visaDetailsData1.clone();
		        }
		        this.photoURL = photoURL1;
		        this.id = id1;
		}
	
}
