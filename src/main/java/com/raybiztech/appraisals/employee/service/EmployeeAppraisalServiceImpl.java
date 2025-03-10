package com.raybiztech.appraisals.employee.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.raybiztech.appraisals.builder.AppraisalBuilder;
import com.raybiztech.appraisals.builder.CycleBuilder;
import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.builder.KPIBuilder;
import com.raybiztech.appraisals.builder.KPIRatingBuilder;
import com.raybiztech.appraisals.builder.KRABuilder;
import com.raybiztech.appraisals.business.Appraisal;
import com.raybiztech.appraisals.business.Cycle;
import com.raybiztech.appraisals.business.DesignationKras;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.KPI;
import com.raybiztech.appraisals.business.KPIRating;
import com.raybiztech.appraisals.business.KRA;
import com.raybiztech.appraisals.dto.AppraisalDTO;
import com.raybiztech.appraisals.dto.CycleDTO;
import com.raybiztech.appraisals.dto.DesignationKrasDTO;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.dto.KPIDTO;
import com.raybiztech.appraisals.dto.KPIRatingDTO;
import com.raybiztech.appraisals.dto.KRADTO;
import com.raybiztech.appraisals.dto.MyDTO;
import com.raybiztech.appraisals.employee.dao.EmployeeAppraisalDAO;
import com.raybiztech.appraisals.exception.FileUploaderUtilException;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.utils.FileUploaderUtil;

@Service("employeeAppraisalService")
@Transactional
public class EmployeeAppraisalServiceImpl implements EmployeeAppraisalService {
	@Autowired
	EmployeeAppraisalDAO appraisalDAO;
	@Autowired
	AppraisalBuilder appraisalBuilder;
	@Autowired
	CycleBuilder cycleBuilder;

	@Autowired
	KPIRatingBuilder kpiRatingBuilder;

	@Autowired
	EmployeeBuilder employeeBuilder;

	@Autowired
	KPIBuilder kpiBuilder;

	@Autowired
	KRABuilder kraBuilder;

	@Autowired
	private PropBean propBean;

	public static Logger logger = Logger
			.getLogger(EmployeeAppraisalServiceImpl.class);

	@Override
	public AppraisalDTO getEmployeeAppraisalData(Long employeeId) {

		logger.info("in employee service with employee id " + employeeId);

		Appraisal appraisal = appraisalDAO.getEmployeeAppraisalData(employeeId);

		AppraisalDTO appraisalDTO = createAppraisalDTO(appraisal, employeeId);
		logger.info("appraisal dto is " + appraisalDTO);

		return appraisalDTO;
	}

	private AppraisalDTO createAppraisalDTO(Appraisal appraisal, Long employeeId) {
		AppraisalDTO appraisalDTO = null;
		if (appraisal != null) {
			appraisalDTO = new AppraisalDTO();
			appraisalDTO.setId(appraisal.getAppraisalId());

			EmployeeDTO employeeDTO = createEmployeeDTO(
					appraisal.getEmployee(), employeeId);
			logger.info("in employee service impl employee dto object is: "+employeeDTO.getDesignationKrasDTO().getKrasWithWeitage());
			appraisalDTO.setEmployeeDTO(employeeDTO);

			CycleDTO cycleDTO = createCycleDTO(appraisal.getCycle());
			appraisalDTO.setCycleDto(cycleDTO);

		}
		return appraisalDTO;
	}

	private EmployeeDTO createEmployeeDTO(Employee employee, Long employeeId) {
		EmployeeDTO employeeDTO = new EmployeeDTO();
		if (employee != null) {
			employeeDTO.setId(employee.getEmployeeId());
			employeeDTO.setFirstName(employee.getFirstName());
			employeeDTO.setLastName(employee.getLastName());
			employeeDTO.setEmployeeSubmitted(employee.isEmployeeSubmitted());
			employeeDTO.setManagerSubmitted(employee.isManagerSubmitted());
			employeeDTO.setAcknowledged(employee.isAcknowledged());
			employeeDTO.setDesignationKrasDTO(createRoleDTO(
					employee.getDesignationKras(), employeeId));
		}
		return employeeDTO;
	}

	private DesignationKrasDTO createRoleDTO(DesignationKras designation,
			Long employeeId) {
		DesignationKrasDTO designationDto = new DesignationKrasDTO();
		if (designation != null) {
			designationDto = new DesignationKrasDTO();
			designationDto.setId(designation.getDesignationKRAsId());
			designationDto.setDesignationCode(designation.getDesignationCode());
			designationDto.setDesignationName(designation.getDesignationName());
/*
			Set<KraWithWeightage> entityList = designation.getKrasWithWeitage();

			Set<KraWithWeightageDTO> dtoList = new HashSet<KraWithWeightageDTO>();

			for (KraWithWeightage kraWithWeightage : entityList) {
				KraWithWeightageDTO dto = new KraWithWeightageDTO();
				dto.setId(kraWithWeightage.getKraWithWeightageId());
				dto.setWeightage(kraWithWeightage.getWeightage());
				dto.setKradto(createKRADTO(kraWithWeightage.getKra(),
						employeeId));
				logger.info("dto object is :"+dto);
				dtoList.add(dto);
			}
			designationDto.setKrasWithWeitage(dtoList);*/

		}
		return designationDto;
	}

	private KRADTO createKRADTO(KRA kra, Long employeeId) {

		KRADTO kradto = new KRADTO();
		if (kra != null) {
			kradto.setId(kra.getKraId());
			kradto.setKraName(kra.getKraName());
			kradto.setDescription(kra.getDescription());
			kradto.setKpis(getKPIDtoList(kra.getKpis(), employeeId));
		}
		return kradto;
	}

	private Set<KPIDTO> getKPIDtoList(Set<KPI> kpis, Long employeeId) {

		Set<KPIDTO> kpiDtoSet = new HashSet<KPIDTO>();
		if (kpis != null) {
			for (KPI kpi : kpis) {
				KPIDTO kdto = new KPIDTO();
				kdto.setId(kpi.getKpiId());
				kdto.setKpiName(kpi.getKpiName());
				kdto.setDescription(kpi.getDescription());

				List<KPIRating> kpiRatings = (List<KPIRating>) appraisalDAO
						.getKPIRatingsForAnEmployee(employeeId, kpi.getKpiId());
				logger.info("kpi ratings in getkpidtolist is : "+kpiRatings);

				Set<KPIRating> ratings = new HashSet<KPIRating>(kpiRatings);

				kdto.setKpiRatingDTOs(getKPIRatingDtos(ratings));
				logger.info("kpi object in empappraisalser is :...."+kdto.getKpiRatingDTOs());
				
				kpiDtoSet.add(kdto);
			}
		}
		return kpiDtoSet;
	}

	private CycleDTO createCycleDTO(Cycle cycle) {

		CycleDTO cycleDto = new CycleDTO();
		if (cycle != null) {
			cycleDto.setId(cycle.getCycleId());
			cycleDto.setName(cycle.getName());
			cycleDto.setPercentage_Done(cycle.getPercentage_Done());
			cycleDto.setStatus(cycle.getStatus());
			cycleDto.setToDate(cycle.getToDate());
			cycleDto.setFromDate(cycle.getFromDate());
			cycleDto.setDescription(cycle.getDescription());
		}
		return cycleDto;
	}

	private Set<KPIRatingDTO> getKPIRatingDtos(Set<KPIRating> kpiRatings) {

		Set<KPIRatingDTO> kpiRatingDtos = new HashSet<KPIRatingDTO>();

		if (kpiRatings != null) {
			for (KPIRating kpiRating : kpiRatings) {
				KPIRatingDTO kpiRatingDto = createKPIRatingDTO(kpiRating);
				logger.info("dummy file name is:"+kpiRatingDto.getEmployeeDummyFileName());
				kpiRatingDtos.add(kpiRatingDto);
			}
		}
		logger.info("kpiratingdtos object is :"+kpiRatingDtos);
		return kpiRatingDtos;
	}

	private KPIRatingDTO createKPIRatingDTO(KPIRating kpiRating) {
		KPIRatingDTO kpiRatingDTO = null;
		if (kpiRating != null) {
			kpiRatingDTO = new KPIRatingDTO();
			kpiRatingDTO.setId(kpiRating.getKpiRatingId());
			kpiRatingDTO.setName(kpiRating.getKpiRatingName());
			kpiRatingDTO.setEmployee_Rating(kpiRating.getEmployee_Rating());
			kpiRatingDTO.setEmployee_Comment(kpiRating.getEmployee_Comment());
			kpiRatingDTO.setManager_Rating(kpiRating.getManager_Rating());
			kpiRatingDTO.setManage_comment(kpiRating.getManage_comment());
			kpiRatingDTO.setEmployeeFileName(kpiRating.getEmployeeFileName());
			kpiRatingDTO.setManagerFileName(kpiRating.getManagerFileName());
			kpiRatingDTO.setCycle(cycleBuilder.createCycleDTO(kpiRating
					.getCycle()));
			kpiRatingDTO.setEmployee(employeeBuilder
					.createEmployeeDTO(kpiRating.getEmployee()));
			kpiRatingDTO.setKpi(kpiBuilder.createKPIDTO(kpiRating.getKpi()));
			kpiRatingDTO.setEmployeeDummyFileName(kpiRating
					.getEmployeeDummyFileName());
			
			logger.info("employee dummy file name createkpirating dto :"
					+ kpiRating.getEmployeeDummyFileName());
			
			kpiRatingDTO.setManagerDummyFileName(kpiRating
					.getManagerDummyFileName());
			
			logger.info("manager dummy file name createkpirating dto :"
					+ kpiRating.getManagerDummyFileName());
		}
		return kpiRatingDTO;
	}

	@Override
	public void saveKPIRating(MyDTO dto) {
		logger.info("emp dummy name : "+dto.getEmployeeDummyFileName());
		logger.info("emp file name : "+dto.getEmployeeFileName());

		logger.info("manager comments before : " + dto.getManager_Comment());
		logger.info("manager ratings before  : " + dto.getManager_Rating());

		KPIRatingDTO kpiRatingDTO = new KPIRatingDTO();
		kpiRatingDTO.setId(dto.getId());
		kpiRatingDTO.setEmployee_Comment(dto.getEmployee_Comment());
		kpiRatingDTO.setEmployee_Rating(dto.getEmployee_Rating());
		kpiRatingDTO.setManage_comment(dto.getManager_Comment());
		kpiRatingDTO.setManager_Rating(dto.getManager_Rating());
		kpiRatingDTO.setName(dto.getName());
		kpiRatingDTO.setEmployeeFileName(dto.getEmployeeFileName());
		kpiRatingDTO.setManagerFileName(dto.getManagerFileName());
		
		kpiRatingDTO.setEmployeeDummyFileName(dto.getEmployeeDummyFileName());
		
		logger.info("employee dummy file name "
				+ dto.getEmployeeDummyFileName());
		
		kpiRatingDTO.setManagerDummyFileName(dto.getManagerDummyFileName());
		
		logger.info("manager dummy file name " + dto.getManagerDummyFileName());

		KPIRating kpiRating = kpiRatingBuilder
				.createKPIRatingEntity(kpiRatingDTO);
		logger.info("converted into kpirating**********");

		kpiRating.setEmployee(appraisalDAO.findBy(Employee.class,
				dto.getEmployeeId()));
		kpiRating.setCycle(appraisalDAO.findBy(Cycle.class, dto.getCycleId()));
		kpiRating.setKpi(appraisalDAO.findBy(KPI.class, dto.getKpiId()));

		logger.info("manager comments after : " + kpiRating.getManage_comment());
		logger.info("manager ratings after  : " + kpiRating.getManager_Rating());
		logger.info("employee file name after builder is :..."+kpiRating.getEmployeeFileName());
		logger.info("employee dummy file name after builder is :..."+kpiRating.getEmployeeDummyFileName());

		appraisalDAO.saveKPIRating(kpiRating);
	}

	@Override
	public String uploadFile(MultipartFile mpf) {

		String fileName = null;
		try {
			logger.info("testing : " + mpf);
			FileUploaderUtil util = new FileUploaderUtil();
			fileName = util.uploadDocument(mpf, propBean);
			logger.info("dummy name in service " + fileName);
			System.out.println("dummy name in service " + fileName);

		} catch (Exception ex) {
			throw new FileUploaderUtilException(
					"Exception occured while uploading a file in Legal "
							+ ex.getMessage(), ex);
		}
		return fileName;
	}

	@Override
	public void deleteKpiRating(KPIRatingDTO dto) {
		KPIRating kpiRating = kpiRatingBuilder.createKPIRatingEntity(dto);
		kpiRating.setCycle(null);
		kpiRating.setEmployee(null);
		kpiRating.setKpi(null);

		appraisalDAO.delete(kpiRating);
	}

	@Override
	public void submitEmployeeAppraisal(Long employeeId) {
		logger.info("appraisalId in submit employee appraisal : " + employeeId);
		Employee employee = appraisalDAO.findBy(Employee.class, employeeId);
		employee.setEmployeeSubmitted(true);
		appraisalDAO.update(employee);
	}

	@Override
	public void acknowledgeAppraisal(Long employeeId) {
		logger.info("appraisalId in submit acknowledgeAppraisal : "
				+ employeeId);
		Employee employee = appraisalDAO.findBy(Employee.class, employeeId);
		employee.setAcknowledged(true);
		appraisalDAO.update(employee);
	}

	@Override
	public void downloadFile(HttpServletResponse response, String fileName) {
		logger.info("inside download file in service : " + fileName);
		try {
			FileUploaderUtil util = new FileUploaderUtil();
			util.downloadFile(response, fileName, propBean);
		} catch (Exception ex) {
			throw new FileUploaderUtilException(
					"Exception occured while uploading a file in Legal "
							+ ex.getMessage(), ex);
		}
	}
}
