package com.raybiztech.projectmanagement.invoice.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.projectmanagement.invoice.dto.BankDTO;
import com.raybiztech.projectmanagement.invoice.dto.CurrencyDTO;
import com.raybiztech.projectmanagement.invoice.dto.RemittanceDTO;
import com.raybiztech.projectmanagement.invoice.dto.RemittanceLocationDTO;
import com.raybiztech.projectmanagement.invoice.service.RemittanceService;
import com.raybiztech.projectmanagement.invoice.utility.EntityAlreadyExistsException;

@Controller("remittanceController")
@RequestMapping("/remittance")
public class RemittanceController {

	private final RemittanceService remittanceserviceImpl;

	@Autowired
	public RemittanceController(RemittanceService remittanceserviceImpl) {
		this.remittanceserviceImpl = remittanceserviceImpl;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, List<? extends Object>> getBankandCurrencyDetails() {

		return remittanceserviceImpl.getBankAndCurrencyDetails();
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public void addRemittanceDetails(@RequestBody RemittanceDTO projectdetails) {

		remittanceserviceImpl.addRemittanceDetails(projectdetails);

	}

	@RequestMapping(value = "/", params = { "currency" }, method = RequestMethod.GET)
	public @ResponseBody List<RemittanceDTO> getRemittanceList(
			@RequestParam("currency") String currency) {
		return remittanceserviceImpl.getRemittanceList(currency);
	}

	@RequestMapping(value = "/", params = { "remittanceid" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteRemittance(
			@RequestParam("remittanceid") Integer remittanceid) {

		remittanceserviceImpl.deleteRemittance(remittanceid);

	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	@ResponseBody
	public void updateRemittanceDetails(@RequestBody RemittanceDTO remittancedto) {

		remittanceserviceImpl.updateRemittanceDetails(remittancedto);

	}

	@RequestMapping(value = "/remittanceOf", method = RequestMethod.GET)
	@ResponseBody
	public RemittanceDTO getBankNameAndCurrencyDetails(
			@RequestParam String bankName, @RequestParam String currencyType) {

		return remittanceserviceImpl.getBankNameAndCurrencyDetails(bankName,
				currencyType);
	}

	@RequestMapping(value = "/addBank", method = RequestMethod.POST)
	@ResponseBody
	public void addBank(@RequestBody BankDTO bank) {
		remittanceserviceImpl.addBank(bank);
	}

	@RequestMapping(value = "/deleteBank", params = { "bankId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteBank(@RequestParam("bankId") Integer bankId) {
		remittanceserviceImpl.deletebank(bankId);
	}

	@RequestMapping(value = "/addCurrency", method = RequestMethod.POST)
	@ResponseBody
	public void addBank(@RequestBody CurrencyDTO currency) {
		remittanceserviceImpl.addCurrency(currency);
	}

	@RequestMapping(value = "/deleteCurrency", params = { "currencyId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteCurrency(
			@RequestParam("currencyId") Long currencyId) {
		remittanceserviceImpl.deleteCurrency(currencyId);
	}

	@RequestMapping(value = "/addLocation", method = RequestMethod.POST)
	@ResponseBody
	public void addLocation(@RequestBody RemittanceLocationDTO location,HttpServletResponse httpResponse ) {
		try{
			remittanceserviceImpl.addLocation(location);
		}catch(EntityAlreadyExistsException e){
			httpResponse.setStatus(HttpServletResponse.SC_CONFLICT);
		}
	}

	@RequestMapping(value = "/deleteLocation", params = { "locationId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteLocation(
			@RequestParam("locationId") Long locationId) {
		remittanceserviceImpl.deleteLocation(locationId);
	}
}
