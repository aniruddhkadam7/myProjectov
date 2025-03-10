package com.raybiztech.projectmanagement.invoice.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.projectmanagement.invoice.business.CountryAddress;
import com.raybiztech.projectmanagement.invoice.business.Remittance;
import com.raybiztech.projectmanagement.invoice.dto.CountryAddressDTO;
import com.raybiztech.projectmanagement.invoice.dto.RemittanceDTO;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;

@Component("countryAddressBuilder")
public class CountryAddressBuilder {
	
	private final DAO dao;


	@Autowired
	public CountryAddressBuilder(DAO dao) {
		this.dao = dao;
	}
	
	
	public CountryAddress convertDTOToEntity(CountryAddressDTO countryAddressDTO){
		CountryAddress countryAddress=null;
		if(countryAddressDTO !=null){
			countryAddress =new CountryAddress();
			countryAddress.setId(countryAddressDTO.getId());
			countryAddress.setCountry(countryAddressDTO.getCountry());
			countryAddress.setCompanyName(countryAddressDTO.getCompanyName());
			countryAddress.setAddress(countryAddressDTO.getAddress());
					CountryLookUp countryLookUp=dao.findBy(CountryLookUp.class, 
					countryAddressDTO.getCountryLookUpDTO().getId());
			
					countryAddress.setCountryLookUp(countryLookUp);
		}
		return countryAddress;
		
	}
	

	public CountryAddressDTO convertEntityToDTO(CountryAddress countryAddress){
		CountryAddressDTO countryAddressDTO=null;
		if(countryAddress!=null){
			countryAddressDTO=new CountryAddressDTO();
			countryAddressDTO.setId(countryAddress.getId());
			countryAddressDTO.setCountry(countryAddress.getCountry());
			countryAddressDTO.setCompanyName(countryAddress.getCompanyName());
			countryAddressDTO.setAddress(countryAddress.getAddress());
			/*countryAddressDTO.setCountryLookUpDTO(countryAddress.getCountryLookUp());*/
			
		}
		return countryAddressDTO;
	}
	
	public List<CountryAddressDTO> convertEntityListToDTOList(
			List<CountryAddress> addressList){
		List<CountryAddressDTO> addressDetailsList = null;
		if(addressList !=null){
			addressDetailsList =new ArrayList<CountryAddressDTO>();
			for (CountryAddress countryAddress :addressList){
				addressDetailsList.add(convertEntityToDTO(countryAddress));
			}
			
		}
		return addressDetailsList;
	}
	
	public CountryAddress toEditEntity(CountryAddressDTO countryAddressDTO){
		CountryAddress countryAddress =null;
		if(countryAddressDTO !=null){
			countryAddress =dao.findBy(CountryAddress.class, countryAddressDTO.getId());
			countryAddress.setCountry(countryAddressDTO.getCountry());
			countryAddress.setCompanyName(countryAddressDTO.getCompanyName());
			countryAddress.setAddress(countryAddressDTO.getAddress());
		}
		return countryAddress;
	}
			
			

}
