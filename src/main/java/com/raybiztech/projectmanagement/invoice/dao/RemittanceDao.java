package com.raybiztech.projectmanagement.invoice.dao;

import java.util.List;
import java.util.Map;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.projectmanagement.invoice.business.Remittance;

public interface RemittanceDao extends DAO {

	Map<String, List<? extends Object>> getBankAndCurrencyDetails();

	void addRemittanceDetails(Remittance projectDetails);

	List<Remittance> getRemittanceList(String currency);
    
	Remittance getBankNameAndCurrencyDetails(String CurrencyType, String bankName );
	
	int checkLocationExist(String location);
}
