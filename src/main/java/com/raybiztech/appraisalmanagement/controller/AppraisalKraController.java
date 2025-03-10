package com.raybiztech.appraisalmanagement.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.appraisalmanagement.business.Frequency;
import com.raybiztech.appraisalmanagement.dto.DesignationDto;
import com.raybiztech.appraisalmanagement.dto.DesignationKRAMappingDto;
import com.raybiztech.appraisalmanagement.dto.KPIDto;
import com.raybiztech.appraisalmanagement.dto.KRADto;
import com.raybiztech.appraisalmanagement.dto.SearchQueryParamsInKRA;
import com.raybiztech.appraisalmanagement.service.AppraisalKraService;

import java.util.Map;

@Controller
@RequestMapping("/kra")
public class AppraisalKraController {

	@Autowired
	AppraisalKraService appraisalKraService;

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public void addKra(@RequestBody KRADto kradto) {
		appraisalKraService.addKra(kradto);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public List<KRADto> getKras() {
		return appraisalKraService.getKras();
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	@ResponseBody
	public void updateKra(@RequestBody KRADto kraDto) {
		appraisalKraService.updateKra(kraDto);
	}

	@RequestMapping(value = "/", params = { "kraid" }, method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteKra(@RequestParam Long kraid,
			HttpServletResponse htppResponse) {
		appraisalKraService.deleteKra(kraid);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public KRADto getKra(@PathVariable("id") Long id) {
		return appraisalKraService.getKra(id);
	}

	@RequestMapping(value = "/{kraid}/kpi", method = RequestMethod.POST)
	@ResponseBody
	public void addKpiToKra(@PathVariable("kraid") Long id,
			@RequestBody KPIDto kpiDto, HttpServletResponse value) {
		appraisalKraService.addKpiToKra(id, kpiDto);
	}

	@RequestMapping(value = "/{kpiId}/kpi", method = RequestMethod.GET)
	@ResponseBody
	public KPIDto editkpi(@PathVariable("kpiId") Long id) {

		return appraisalKraService.editkpi(id);
	}

	@RequestMapping(value = "/{kraid}/kpi", method = RequestMethod.PUT)
	@ResponseBody
	public void editKpiToKra(@PathVariable("kraid") Long id,
			@RequestBody KPIDto kpiDto) {
		appraisalKraService.editKpiToKra(id, kpiDto);
	}

	@RequestMapping(value = "/kpis", method = RequestMethod.GET)
	@ResponseBody
	public List<KPIDto> getAllKpiForAllKra() {
		return appraisalKraService.getAllKpiForAllKra();
	}

	@RequestMapping(value = "{kraid}/kpi/{kpiid}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteKpi(@PathVariable("kraid") Long kraId,
			@PathVariable("kpiid") Long kpiid,
			HttpServletResponse httpServletResponse) {
		appraisalKraService.deleteKpi(kraId, kpiid);
	}

	@RequestMapping(value = "/designation",params = {"deptId"}, method = RequestMethod.GET)
	@ResponseBody
	public List<DesignationDto> getAllDesignation(@RequestParam Long deptId) {
		return appraisalKraService.getAllDesignation(deptId);
	}

	@RequestMapping(value = "/designingmaping", method = RequestMethod.POST)
	@ResponseBody
	public void designationKraMapping(
			@RequestBody DesignationKRAMappingDto designationKRAMappingDto) {
		appraisalKraService.designationKraMapping(designationKRAMappingDto);
	}

	@RequestMapping(value = "/{kraName}/isDuplicate",params = {"departmentId","designationId"}, method = RequestMethod.GET)
	@ResponseBody
	public Boolean isDuplicate(@PathVariable("kraName") String name,@RequestParam("departmentId") Long departmentId,@RequestParam("designationId") Long designationId) {
		return appraisalKraService.isDuplicate(name,departmentId,designationId);
	}

	@RequestMapping(value = "/{kraId}/{kpiName}", method = RequestMethod.GET)
	public @ResponseBody Boolean isDuplicateKPI(
			@PathVariable("kraId") Long kraId,
			@PathVariable("kpiName") String kpiName) {
		return appraisalKraService.isDuplicateKPI(kraId, kpiName);
	}
        @RequestMapping(value = "/kpisForIndividualKra",params = {"kraId"},method = RequestMethod.GET)
        public @ResponseBody List<KPIDto> kpisForIndividualKra(@RequestParam Long kraId){
            return appraisalKraService.getAllKpiForIndividualKra(kraId);
        }
        
        @RequestMapping(value = "/getDesignationWiseKRAs",params = {"departmentId","designationId"},method = RequestMethod.GET)
        public @ResponseBody List<KRADto> getDesignationWiseKRAs(@RequestParam("departmentId") Long departmentId,@RequestParam("designationId") Long designationId){
            return appraisalKraService.getDesignationWiseKRAs(departmentId,designationId);
        }
        @RequestMapping(value = "/searchKRAData",method = RequestMethod.POST)
        public @ResponseBody Map<String,Object> searchKRAData(@RequestBody SearchQueryParamsInKRA searchQueryParamsInKRA){
            return appraisalKraService.searchKRAData(searchQueryParamsInKRA);
        }
        @RequestMapping(value = "/updateKpi",method = RequestMethod.PUT)
        public @ResponseBody void updateKpi(@RequestBody KPIDto dto){
            appraisalKraService.updateKpi(dto);
        }
        @RequestMapping(value="/designationKraPercentage",params = {"departmentId","designationId"},method=RequestMethod.GET)
        public @ResponseBody Double getDesignationKraPercentage(@RequestParam Long departmentId,@RequestParam Long designationId){
        	return appraisalKraService.getDesignationKraPercentage(departmentId, designationId);
        }
        @RequestMapping(value = "/kraForIndividual",params = {"personId"},method = RequestMethod.GET)
        public @ResponseBody List<KRADto> kraForIndividual(@RequestParam Long personId,HttpServletResponse httpServletResponse){
          //  return appraisalKraService.getAllKpiForIndividualKra(kraId);
        	return appraisalKraService.kraForIndividual(personId);
        }
        @RequestMapping(value="/frequencyList",method=RequestMethod.GET)
    	@ResponseBody public List<Frequency> frequencyList(){
    		return appraisalKraService.frequencyList();
    	}
    	
        
}
