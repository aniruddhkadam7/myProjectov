package com.raybiztech.projectmanagement.invoice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.projectmanagement.business.Client;
import com.raybiztech.projectmanagement.dto.ClientDTO;
import com.raybiztech.projectmanagement.invoice.builder.RemittanceBuilder;
import com.raybiztech.projectmanagement.invoice.business.Remittance;
import com.raybiztech.projectmanagement.invoice.dao.RemittanceDao;
import com.raybiztech.projectmanagement.invoice.dto.BankDTO;
import com.raybiztech.projectmanagement.invoice.dto.CurrencyDTO;
import com.raybiztech.projectmanagement.invoice.dto.RemittanceDTO;
import com.raybiztech.projectmanagement.invoice.dto.RemittanceLocationDTO;
import com.raybiztech.projectmanagement.invoice.lookup.Bank;
import com.raybiztech.projectmanagement.invoice.lookup.Currency;
import com.raybiztech.projectmanagement.invoice.lookup.RemittanceLocation;
import com.raybiztech.projectmanagement.invoice.utility.EntityAlreadyExistsException;
import com.raybiztech.projectmanagement.invoice.utility.InvoiceLookUpKeyConstants;

@Service("remittanceServiceImpl")
public class RemittanceServiceImpl implements RemittanceService {

	private final RemittanceDao remittanceDaoImpl;

	private final RemittanceBuilder remittanceBuilder;

	private final DAO dao;

	@Autowired
	public RemittanceServiceImpl(RemittanceDao remittanceDaoImpl,
			RemittanceBuilder remittanceBuilder, DAO dao) {
		this.remittanceDaoImpl = remittanceDaoImpl;
		this.remittanceBuilder = remittanceBuilder;
		this.dao = dao;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<? extends Object>> getBankAndCurrencyDetails() {

		Map<String, List<? extends Object>> DtoMap = new HashMap<String, List<? extends Object>>();

		Map<String, List<? extends Object>> map = remittanceDaoImpl
				.getBankAndCurrencyDetails();

		List<Bank> bankList = (List<Bank>) map
				.get(InvoiceLookUpKeyConstants.BANK_NAMES);

		List<Currency> currencyTypes = (List<Currency>) map
				.get(InvoiceLookUpKeyConstants.CURRENCY_TYPES);

		List<RemittanceLocation> locations = (List<RemittanceLocation>) map
				.get(InvoiceLookUpKeyConstants.REMITTANCE_LOCATIONS);

		List<Client> clients = (List<Client>) map
				.get(InvoiceLookUpKeyConstants.CLIENT);

		List<BankDTO> BankDtoList = remittanceBuilder
				.covertBankDetailsToDto(bankList);

		List<CurrencyDTO> currencyDtoList = remittanceBuilder
				.convertCurrencyEntitytoDto(currencyTypes);

		List<ClientDTO> clientDTOs = remittanceBuilder.getClientList(clients);

		List<RemittanceLocationDTO> remittanceLocationDTOs = remittanceBuilder
				.convertLocationDetailstoDTO(locations);

		DtoMap.put(InvoiceLookUpKeyConstants.BANK_NAMES, BankDtoList);

		DtoMap.put(InvoiceLookUpKeyConstants.CURRENCY_TYPES, currencyDtoList);

		DtoMap.put(InvoiceLookUpKeyConstants.REMITTANCE_LOCATIONS,
				remittanceLocationDTOs);

		DtoMap.put(InvoiceLookUpKeyConstants.CLIENT, clientDTOs);

		return DtoMap;
	}

	@Override
	public void addRemittanceDetails(RemittanceDTO projectDetails) {

		remittanceDaoImpl.addRemittanceDetails(remittanceBuilder
				.convertDTOToEntity(projectDetails));
	}

	@Override
	public List<RemittanceDTO> getRemittanceList(String currency) {

		return remittanceBuilder.convertEntityListToDTOList(remittanceDaoImpl
				.getRemittanceList(currency));

	}

	@Override
	public void deleteRemittance(Integer remittanceid) {

		dao.delete(dao.findBy(Remittance.class, remittanceid));

	}

	@Override
	public void updateRemittanceDetails(RemittanceDTO remittanceDTO) {

		Remittance remittance = remittanceBuilder.toEditEntity(remittanceDTO);
		dao.update(remittance);

	}

	@Override
	public RemittanceDTO getBankNameAndCurrencyDetails(String CurrencyType,
			String bankName) {
		return remittanceBuilder.convertEntityToDTO(remittanceDaoImpl
				.getBankNameAndCurrencyDetails(CurrencyType, bankName));

	}

	@Override
	public void addBank(BankDTO bankDTO) {
		Bank bank = null;
		if (bankDTO != null) {
			bank = new Bank();
			bank.setId(bankDTO.getId());
			bank.setName(bankDTO.getName().toUpperCase());
		}
		dao.save(bank);

	}

	@Override
	public void deletebank(Integer bankID) {

		Bank bank = dao.findBy(Bank.class, bankID);

		List<Remittance> remittances = dao.getAllOfProperty(Remittance.class,
				"bankName", bank.getName());
		if (remittances.isEmpty()) {
			dao.delete(bank);
		} else {
			throw new EntityAlreadyExistsException("Already Bank "
					+ bank.getName()
					+ " is Used in Remittance,So You can't Delete");
		}

	}

	@Override
	public void addCurrency(CurrencyDTO currency) {

		Currency currency2 = null;
		if (currency != null) {
			currency2 = new Currency();
			currency2.setId(currency.getId());
			String currencyString = currency.getType().toUpperCase();
			currency2.setType(currencyString);
		}
		dao.save(currency2);
	}

	@Override
	public void deleteCurrency(Long currencyId) {
		Currency currency = dao.findBy(Currency.class, currencyId);
		List<Remittance> remittances = dao.getAllOfProperty(Remittance.class,
				"currencyType", currency.getType());
		if (remittances.isEmpty()) {
			dao.delete(currency);
		} else {
			throw new EntityAlreadyExistsException("Currency "
					+ currency.getType()
					+ " Already Used in Remittance,So You can't Delete");
		}

	}

	@Override
	public void addLocation(RemittanceLocationDTO locationDTO) {

		if (locationDTO != null) {
			RemittanceLocation location = new RemittanceLocation();
			location.setId(locationDTO.getId());
			location.setName(locationDTO.getName());
			int size =remittanceDaoImpl.checkLocationExist(locationDTO.getName());
			if(size>0){
				throw new EntityAlreadyExistsException("Location "
						+ location.getName()
						+ " Already Exist"); 
			}else{
			dao.save(location);
			}
		}

	}

	@Override
	public void deleteLocation(Long locationId) {

		RemittanceLocation location = dao.findBy(RemittanceLocation.class,
				locationId);
		List<Remittance> remittances = dao.getAllOfProperty(Remittance.class,
				"location", location.getName());
		if (remittances.isEmpty()) {
			dao.delete(location);
		} else {
			throw new EntityAlreadyExistsException("Location "
					+ location.getName()
					+ " Already Used in Remittance,So You can't Delete");
		}

	}

}
