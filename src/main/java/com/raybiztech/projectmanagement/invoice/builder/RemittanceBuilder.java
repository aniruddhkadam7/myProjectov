package com.raybiztech.projectmanagement.invoice.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.projectmanagement.business.Client;
import com.raybiztech.projectmanagement.dto.ClientDTO;
import com.raybiztech.projectmanagement.invoice.business.Remittance;
import com.raybiztech.projectmanagement.invoice.dto.BankDTO;
import com.raybiztech.projectmanagement.invoice.dto.CurrencyDTO;
import com.raybiztech.projectmanagement.invoice.dto.RemittanceDTO;
import com.raybiztech.projectmanagement.invoice.dto.RemittanceLocationDTO;
import com.raybiztech.projectmanagement.invoice.lookup.Bank;
import com.raybiztech.projectmanagement.invoice.lookup.Currency;
import com.raybiztech.projectmanagement.invoice.lookup.RemittanceLocation;

@Component("remittanceBuilder")
public class RemittanceBuilder {

	private final DAO dao;

	@Autowired
	public RemittanceBuilder(DAO dao) {
		this.dao = dao;
	}

	public Remittance convertDTOToEntity(RemittanceDTO remittanceDTO) {
		Remittance remittance = null;

		if (remittanceDTO != null) {
			remittance = new Remittance();

			remittance.setBankName(remittanceDTO.getBankName());
			remittance.setCurrencyType(remittanceDTO.getCurrencyType());
			remittance.setLocation(remittanceDTO.getLocation());
			remittance.setId(remittanceDTO.getId());
			remittance.setWireTransferInstructions(remittanceDTO
					.getWireTransferInstructions());
			remittance.setClient(remittanceDTO.getClient());
		}
		return remittance;

	}

	public RemittanceDTO convertEntityToDTO(Remittance remittance) {
		RemittanceDTO remittanceDTO = null;
		if (remittance != null) {
			remittanceDTO = new RemittanceDTO();

			remittanceDTO.setId(remittance.getId());
			remittanceDTO.setBankName(remittance.getBankName());
			remittanceDTO.setCurrencyType(remittance.getCurrencyType());
			remittanceDTO.setLocation(remittance.getLocation());
			remittanceDTO.setWireTransferInstructions(remittance
					.getWireTransferInstructions());
			if (remittance.getClient() != null) {
				Client client = dao
						.findBy(Client.class, remittance.getClient());
				remittanceDTO.setClient(client.getId());
				remittanceDTO.setClientName(client.getName());
			}
		}
		return remittanceDTO;
	}

	public List<RemittanceDTO> convertEntityListToDTOList(
			List<Remittance> remittancelist) {

		List<RemittanceDTO> remittancedtolist = null;
		if (remittancelist != null) {

			remittancedtolist = new ArrayList<RemittanceDTO>();

			for (Remittance remittance : remittancelist) {
				remittancedtolist.add(convertEntityToDTO(remittance));
			}
		}
		return remittancedtolist;

	}

	public List<Remittance> convertDTOListToEntityList(
			List<RemittanceDTO> remittanceDTOlist) {

		List<Remittance> remittancelist = null;
		if (remittanceDTOlist != null) {
			remittancelist = new ArrayList<Remittance>();

			for (RemittanceDTO remittanceDTO : remittanceDTOlist) {
				remittancelist.add(convertDTOToEntity(remittanceDTO));
			}
		}
		return remittancelist;
	}

	public List<BankDTO> covertBankDetailsToDto(List<Bank> bankEntityList) {

		List<BankDTO> bankDtoList = null;
		if (bankEntityList != null) {

			bankDtoList = new ArrayList<BankDTO>();
			for (Bank bank : bankEntityList) {
				BankDTO bankDTO = new BankDTO(bank.getId(), bank.getName());

				bankDtoList.add(bankDTO);
			}
		}
		return bankDtoList;
	}

	public List<CurrencyDTO> convertCurrencyEntitytoDto(
			List<Currency> currencyList) {
		List<CurrencyDTO> currencyDtoList = null;

		if (currencyList != null) {

			currencyDtoList = new ArrayList<CurrencyDTO>();
			for (Currency currency : currencyList) {
				CurrencyDTO currencyDTO = new CurrencyDTO(currency.getId(),
						currency.getType());

				currencyDtoList.add(currencyDTO);
			}
		}

		return currencyDtoList;
	}

	public List<RemittanceLocationDTO> convertLocationDetailstoDTO(
			List<RemittanceLocation> locations) {

		List<RemittanceLocationDTO> locationDTOs = null;
		if (locations != null) {
			locationDTOs = new ArrayList<RemittanceLocationDTO>();
			for (RemittanceLocation location : locations) {
				RemittanceLocationDTO locationDTO = new RemittanceLocationDTO(
						location.getId(), location.getName());
				locationDTOs.add(locationDTO);

			}

		}
		return locationDTOs;

	}

	public Remittance toEditEntity(RemittanceDTO remittanceDTO) {
		Remittance remittance = null;
		if (remittanceDTO != null) {
			remittance = dao.findBy(Remittance.class, remittanceDTO.getId());
			remittance.setBankName(remittanceDTO.getBankName());
			remittance.setCurrencyType(remittanceDTO.getCurrencyType());
			remittance.setLocation(remittanceDTO.getLocation());
			remittance.setWireTransferInstructions(remittanceDTO
					.getWireTransferInstructions());
			remittance.setClient(remittanceDTO.getClient());

		}

		return remittance;

	}

	public List<ClientDTO> getClientList(List<Client> clients) {
		List<ClientDTO> al = new ArrayList<ClientDTO>();
		if (clients != null) {
			for (Client client : clients) {
				ClientDTO clientDTO = new ClientDTO();
				clientDTO.setClientCode(client.getClientCode());
				clientDTO.setId(client.getId());
				clientDTO.setName(client.getName());
				clientDTO.setAddress(client.getAddress());
				clientDTO.setEmail(client.getEmail());
				clientDTO.setPersonName(client.getPersonName());
				clientDTO.setPhone(client.getPhone());
				clientDTO.setDescription(client.getDescription());
				clientDTO.setOrganization(client.getOrganization());
				if (client.getCountry() != null)
					clientDTO.setCountry(client.getCountry().getName());
				al.add(clientDTO);
			}

		}
		return al;
	}
}
