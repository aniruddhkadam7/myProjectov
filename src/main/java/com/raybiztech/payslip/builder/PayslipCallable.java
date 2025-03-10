package com.raybiztech.payslip.builder;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.raybiztech.payslip.business.Payslip;
import com.raybiztech.payslip.dto.PayslipRetrieveDto;

public class PayslipCallable implements Callable<PayslipRetrieveDto> {

	private Payslip payslip;

	
	PayslipBuilder payslipBuilder;
	
	Logger logger = Logger.getLogger(PayslipCallable.class);
	
	public PayslipCallable() {
		super();
	}


	public PayslipCallable(Payslip payslip,PayslipBuilder payslipBuilder) {
		this.payslip = payslip;
		this.payslipBuilder = payslipBuilder;
	}

	@Override
	public PayslipRetrieveDto call() throws Exception {
		PayslipRetrieveDto payslipRetrieveDto = payslipBuilder.toDto(payslip);
		return payslipRetrieveDto;
	}

}
