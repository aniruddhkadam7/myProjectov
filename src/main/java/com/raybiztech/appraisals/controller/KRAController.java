/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.controller;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.appraisals.dto.KPIDTO;
import com.raybiztech.appraisals.dto.KRADTO;
import com.raybiztech.appraisals.service.KRAService;

@Controller
@RequestMapping("/kraController")
public class KRAController {

	Logger log = Logger.getLogger(KRAController.class);
	@Autowired
	private KRAService kraService;

	@RequestMapping(value = "/createKra", method = RequestMethod.POST)
	public @ResponseBody
	void createKRA(@RequestBody KRADTO kradto) throws Exception {
		kraService.createKRA(kradto);
	}

	@RequestMapping(value = "/removeKra", method = RequestMethod.POST)
	public @ResponseBody
	void removeKRA(@RequestBody KRADTO kra) throws Exception {
		kraService.removeKRA(kra);
		
	}

	@RequestMapping(value = "/addKpi", params = {"kraId"}, method = RequestMethod.POST)
	public @ResponseBody
	void addKPI(@RequestBody KPIDTO kpidto, Long kraId) throws Exception {
		kraService.addKPI(kpidto, kraId);
	}

	@RequestMapping(value = "/removeKpi", params = {"kpiId"}, method = RequestMethod.POST)
	public @ResponseBody
	void removeKPI(Long kpiId) throws Exception {
		kraService.removeKPI(kpiId);
	}

	@RequestMapping(value = "/getAllKras", method = RequestMethod.GET)
	public @ResponseBody
	List<KRADTO> getAllKras() {
		return kraService.getAllKras();
	}

	@RequestMapping(value = "/getAllKpis", params = { "kraName" }, method = RequestMethod.GET)
	public @ResponseBody
	Set<KPIDTO> getAllKpis(String kraName) throws Exception {
		return kraService.getAllKpis(kraName);
	}

	@RequestMapping(value = "/checkAlreadyExistingKra", params = { "kraName" }, method = RequestMethod.GET)
	public @ResponseBody
	boolean isExistingKRA(String kraName) throws Exception {
		return kraService.isExistingKRA(kraName);

	}

	@RequestMapping(value = "/checkAlreadyExistingKpisinKra", params = {
			"kraName", "kpiName" }, method = RequestMethod.GET)
	public @ResponseBody
	boolean isExistingKPI(String kraName, String kpiName) throws Exception {
		return kraService.isExistingKPI(kraName, kpiName);
	}

}
