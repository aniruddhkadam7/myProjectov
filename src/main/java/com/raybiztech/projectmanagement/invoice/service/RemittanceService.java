package com.raybiztech.projectmanagement.invoice.service;

import java.util.List;
import java.util.Map;

import com.raybiztech.projectmanagement.invoice.dto.BankDTO;
import com.raybiztech.projectmanagement.invoice.dto.CurrencyDTO;
import com.raybiztech.projectmanagement.invoice.dto.RemittanceLocationDTO;
import com.raybiztech.projectmanagement.invoice.dto.RemittanceDTO;

public interface RemittanceService {

	Map<String, List<? extends Object>> getBankAndCurrencyDetails();

	void addRemittanceDetails(RemittanceDTO projectDetails);

	List<RemittanceDTO> getRemittanceList(String currency);

	void deleteRemittance(Integer remittanceid);

	void updateRemittanceDetails(RemittanceDTO remittanceDTO);

	RemittanceDTO getBankNameAndCurrencyDetails(String CurrencyType,
			String bankName);

	void addBank(BankDTO bankDTO);

	void deletebank(Integer bankID);

	void addCurrency(CurrencyDTO currency);

	void deleteCurrency(Long currencyId);

	void addLocation(RemittanceLocationDTO locationDTO);

	void deleteLocation(Long locationId);
}
