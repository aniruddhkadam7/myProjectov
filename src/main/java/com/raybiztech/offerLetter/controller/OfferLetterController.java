package com.raybiztech.offerLetter.controller;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.offerLetter.service.OfferLetterService;

@Controller
@RequestMapping("/offerLetterController")
public class OfferLetterController {

	@Autowired
	OfferLetterService offerLetterServiceImpl;
	
	Logger logger = Logger.getLogger(OfferLetterController.class);

	@RequestMapping(value = "/getListOfOfferLetters", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getOfferLetters() {
		return offerLetterServiceImpl.getListOfOfferLetters();
	}
	
	@RequestMapping(value ="/downloadOfferLetter" , params ={"fileName"} , method = RequestMethod.GET)
	public @ResponseBody void downloadOfferLetter(HttpServletResponse response, String fileName){
		
		offerLetterServiceImpl.downloadOfferLetter(response,fileName);
		
	}
	
	@RequestMapping(value = "/exportOfferLettersList", method = RequestMethod.GET)
	public @ResponseBody void exportOfferLettersList(
			HttpServletResponse httpServletResponse) throws Exception {
		
		logger.warn("in export controller");
		
		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition",
				"attachment; filename=\"OfferLettersList.csv\"");

		ByteArrayOutputStream os = offerLetterServiceImpl.exportOfferLettersList(
				);

		httpServletResponse.getOutputStream().write(os.toByteArray());
	}
	


}
