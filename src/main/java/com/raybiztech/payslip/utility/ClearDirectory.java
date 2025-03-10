package com.raybiztech.payslip.utility;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.properties.PropBean;

@Component("clearDirectory")
public class ClearDirectory {
	
	@Autowired
	PropBean propBean;
	
	public void clearPayslipDirectory() throws IOException
	{
		FileUtils.cleanDirectory(new File((String)propBean.getPropData().get("excelLocation")));
		
	}

}
