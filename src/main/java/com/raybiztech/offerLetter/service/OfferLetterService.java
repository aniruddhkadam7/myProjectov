package com.raybiztech.offerLetter.service;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

public interface OfferLetterService {
	
	void uploadOfferLetterDocument(MultipartFile mpf);
	
	public Map<String, Object> getListOfOfferLetters();
	
	void downloadOfferLetter(HttpServletResponse response, String fileName);
	
	ByteArrayOutputStream exportOfferLettersList() throws Exception;


}
