package com.raybiztech.biometric.quartz;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.biometric.service.BiometricService;

@Component("lateReportingTimeAlert")
public class LateReportingTimeAlert {
	
	@Autowired
	BiometricService biometricService;
	
	Logger logger = Logger.getLogger(LateReportingTimeAlert.class);
	
	public void lateReportingTime() throws ParseException{
		 biometricService
				.updateLateReporting();
	}

}
