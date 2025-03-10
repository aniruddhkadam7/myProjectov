package com.raybiztech.appraisalmanagement.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.appraisalmanagement.builder.DesignationBuilder;
import com.raybiztech.appraisalmanagement.builder.KPIBuilder;
import com.raybiztech.appraisalmanagement.builder.KRABuilder;
import com.raybiztech.appraisalmanagement.business.AppraisalCycle;
import com.raybiztech.appraisalmanagement.business.Designation;
import com.raybiztech.appraisalmanagement.business.DesignationKRAMapping;
import com.raybiztech.appraisalmanagement.business.Frequency;
import com.raybiztech.appraisalmanagement.business.KPI;
import com.raybiztech.appraisalmanagement.business.KRA;
import com.raybiztech.appraisalmanagement.dao.AppraisalDao;
import com.raybiztech.appraisalmanagement.dto.DesignationDto;
import com.raybiztech.appraisalmanagement.dto.DesignationKRAMappingDto;
import com.raybiztech.appraisalmanagement.dto.KPIDto;
import com.raybiztech.appraisalmanagement.dto.KRADto;
import com.raybiztech.appraisalmanagement.dto.SearchQueryParamsInKRA;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.exceptions.CycleNotAccessException;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;

import java.util.HashMap;
import java.util.Map;

@Service("appraisalKraService")
@Transactional
public class AppraisalKraServiceImpl implements AppraisalKraService {

	@Autowired
	AppraisalDao appraisalDao;
	@Autowired
	KRABuilder kraBuilder;
	@Autowired
	KPIBuilder kpiBuilder;
	@Autowired
	DesignationBuilder designationsBuilder;
        @Autowired
        DAO dao;
        @Autowired
        SecurityUtils securityUtils;
	Logger logger = Logger.getLogger(AppraisalKraServiceImpl.class);

	@Override
	public void addKra(KRADto kradto) {
                  appraisalDao.save(kraBuilder.toEntity(kradto));
	}

	@Override
	public List<KRADto> getKras() {
		List<KRA> list = appraisalDao.get(KRA.class);
		return kraBuilder.toDtoList(list);
	}

	@Override
	public void updateKra(KRADto kraDto) {
		KRA kra = kraBuilder.toEditEntity(kraDto);
		appraisalDao.update(kra);
	}

	@Override
	public void deleteKra(Long id) {
		KRA kra = appraisalDao.findBy(KRA.class, id);
		appraisalDao.delete(kra);
	}

	@Override
	public KRADto getKra(Long id) {
		KRA kra = appraisalDao.findBy(KRA.class, id);
		return kraBuilder.toDto(kra);
	}

	@Override
	public void addKpiToKra(Long id, KPIDto kpiDto) {
		KPI kpi = kpiBuilder.toEntity(kpiDto);
		KRA kra = appraisalDao.findBy(KRA.class, id);
		kpi.setKra(kra);
		/*Frequency frequency=appraisalDao.findBy(Frequency.class, id);
		kpi.setFrequency(frequency);*/
		appraisalDao.save(kpi);
	}

	@Override
	public void editKpiToKra(Long id, KPIDto kpiDto) {
		KPI kpi = kpiBuilder.toeditEntity(kpiDto);
		KRA kra = appraisalDao.findBy(KRA.class, id);
		kpi.setKra(kra);
		appraisalDao.saveOrUpdate(kpi);
	}

	@Override
	public KPIDto editkpi(Long kpiId) {

		KPI kp = appraisalDao.findBy(KPI.class, kpiId);

		KPIDto kpidto = kpiBuilder.toDto(kp);

		return kpidto;
	}

	@Override
	public List<KPIDto> getAllKpiForAllKra() {
		List<KPI> list = appraisalDao.get(KPI.class);
		return kpiBuilder.toDtoList(list);
	}

	@Override
	public void deleteKpi(Long kraId, Long kpiid) {
		KPI kpi = appraisalDao.findBy(KPI.class, kpiid);
		appraisalDao.delete(kpi);
	}

	@Override
	public List<DesignationDto> getAllDesignation(Long deptId) {
		//List<Designation> list = appraisalDao.get(Designation.class);
                List<Designation> list = appraisalDao.getDesignationsOfDept(deptId);
		Collections.sort(list, new Comparator<Designation>() {
			public int compare(Designation v1, Designation v2) {
				return v1.getName().compareTo(v2.getName());
			}
		});

		return designationsBuilder.toDtoList(list);
	}

	@Override
	public void designationKraMapping(
			DesignationKRAMappingDto designationKRAMappingDto) {
            
            AppraisalCycle ac=dao.findBy(AppraisalCycle.class,designationKRAMappingDto.getAppraisalCycleDto().getId());
            Date currentDate=new Date();
            
            if(currentDate.equals(ac.getConfigurationPeriod().getMinimum()) || currentDate.isAfter(ac.getConfigurationPeriod().getMinimum())){
                throw new CycleNotAccessException("Cycle is already started");
            }
            else{
		Set<KRA> kraset = new HashSet<KRA>();
		for (KRADto kraDto : designationKRAMappingDto.getKraLookups()) {
			kraset.add(appraisalDao.findBy(KRA.class, kraDto.getId()));
		}
		DesignationKRAMapping designationKRAMap = appraisalDao
				.getAllKrasUnderCyclewithDesignation(designationKRAMappingDto
						.getAppraisalCycleDto().getId(),
						designationKRAMappingDto.getDesignation().getId());
		if (designationKRAMap == null) {
			Designation designation = appraisalDao.findBy(Designation.class,
					designationKRAMappingDto.getDesignation().getId());
			AppraisalCycle appraisalCycle = appraisalDao.findBy(
					AppraisalCycle.class, designationKRAMappingDto
							.getAppraisalCycleDto().getId());
			DesignationKRAMapping designationKRAMapping = new DesignationKRAMapping();
			designationKRAMapping.setDesignation(designation);
			designationKRAMapping.setKraLookups(kraset);
			designationKRAMapping.setCycle(appraisalCycle);
			appraisalDao.save(designationKRAMapping);
		} else {
			DesignationKRAMapping designationKRAMapping = appraisalDao
					.getAllKrasUnderCyclewithDesignation(
							designationKRAMappingDto.getAppraisalCycleDto()
									.getId(), designationKRAMappingDto
									.getDesignation().getId());
			designationKRAMapping.setKraLookups(kraset);
			appraisalDao.saveOrUpdate(designationKRAMapping);
		}
            }
	}

	@Override
	public Boolean isDuplicate(String name,Long departmentId,Long designationId) {
		return appraisalDao.isDuplicateKRA(name, departmentId, designationId);
	}

	@Override
	public Boolean isDuplicateKPI(Long kraId, String kpiName) {
		KRA kra = appraisalDao.findBy(KRA.class, kraId);
		Set kpiList = kra.getKpiLookps();

		Iterator iterator = kpiList.iterator();
		while (iterator.hasNext()) {
			KPI kpi = (KPI) iterator.next();
			if (kpi.getName().equalsIgnoreCase(kpiName)) {
				return true;
			}

		}
		return false;
	}
        
        @Override
        public List<KPIDto> getAllKpiForIndividualKra(Long kraId){
            
            return kpiBuilder.toDtoList(appraisalDao.getAllKpiForIndividualKra(kraId));
        }
        
        @Override
        public List<KRADto> getDesignationWiseKRAs(Long departmentId,Long designationId){
             List<KRA> list=appraisalDao.getDesignationWiseKRAs(departmentId, designationId);
             return kraBuilder.toDtoList(list);
        }
        
        @Override
        public Map<String, Object> searchKRAData(SearchQueryParamsInKRA searchQueryParamsInKRA){
            Map<String,Object> kraMap=appraisalDao.searchKRAData(searchQueryParamsInKRA);
            List<KRA> kraList=(List<KRA>) kraMap.get("list");
            Integer noOfRecards=(Integer) kraMap.get("size");
            List<KRADto> kRADtos=kraBuilder.toDtoList(kraList);
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("list", kRADtos);
            map.put("size", noOfRecards);
            return map;
        }
        @Override
        public void updateKpi(KPIDto kPIDto){
            KPI kpi=kpiBuilder.toUpdateEntity(kPIDto);
            dao.update(kpi);
        }
        @Override
        public Double getDesignationKraPercentage(Long departmentId,Long designationId){
        	return appraisalDao.getDesignationKraPercentage(departmentId, designationId);
        }
        @Override
        public  List<KRADto> kraForIndividual(Long personId){
        	Employee  employee=dao.findBy(Employee.class, personId);
        	List<KRA> kraslist=appraisalDao.krasForIndividual(employee);
        	 return kraBuilder.toDtoList(kraslist);
        }
        @Override
    	public List<Frequency> frequencyList() {
    		
    		return appraisalDao.get(Frequency.class);
    	}
}
