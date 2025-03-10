package com.raybiztech.appraisalmanagement.service;

import java.util.List;

import com.raybiztech.appraisalmanagement.business.Frequency;
import com.raybiztech.appraisalmanagement.business.KPI;
import com.raybiztech.appraisalmanagement.dto.DesignationDto;
import com.raybiztech.appraisalmanagement.dto.DesignationKRAMappingDto;
import com.raybiztech.appraisalmanagement.dto.KPIDto;
import com.raybiztech.appraisalmanagement.dto.KRADto;
import com.raybiztech.appraisalmanagement.dto.SearchQueryParamsInKRA;
import com.raybiztech.appraisals.dto.KRADTO;

import java.util.Map;

public interface AppraisalKraService {

	void addKra(KRADto kradto);

	List<KRADto> getKras();

	void updateKra(KRADto kraDto);

	void deleteKra(Long id);

	KRADto getKra(Long id);

	void addKpiToKra(Long id, KPIDto kpiDto);

	KPIDto editkpi(Long kpiIid);

	void editKpiToKra(Long id, KPIDto kpiDto);

	List<KPIDto> getAllKpiForAllKra();

	void deleteKpi(Long kraId, Long kpiid);

	List<DesignationDto> getAllDesignation(Long deptId);

	void designationKraMapping(DesignationKRAMappingDto designationKRAMappingDto);

	Boolean isDuplicate(String name, Long departmentId, Long designationId);

	Boolean isDuplicateKPI(Long kraId, String kpiName);

	List<KPIDto> getAllKpiForIndividualKra(Long kraId);

	List<KRADto> getDesignationWiseKRAs(Long departmentId, Long designationId);

	Map<String, Object> searchKRAData(
			SearchQueryParamsInKRA searchQueryParamsInKRA);

	void updateKpi(KPIDto kPIDto);

	Double getDesignationKraPercentage(Long departmentId, Long designationId);

	List<KRADto> kraForIndividual(Long personId);

	public List<Frequency> frequencyList();

}
