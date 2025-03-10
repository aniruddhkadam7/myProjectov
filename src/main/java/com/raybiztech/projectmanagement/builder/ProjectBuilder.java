package com.raybiztech.projectmanagement.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.Second;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.ChangeRequest;
import com.raybiztech.projectmanagement.business.Client;
import com.raybiztech.projectmanagement.business.Country;
import com.raybiztech.projectmanagement.business.Milestone;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.business.ProjectAudit;
import com.raybiztech.projectmanagement.business.ProjectModel;
import com.raybiztech.projectmanagement.business.ProjectRequest;
import com.raybiztech.projectmanagement.business.ProjectStatus;
import com.raybiztech.projectmanagement.business.ProjectType;
import com.raybiztech.projectmanagement.dao.AllocationDetailsDTO;
import com.raybiztech.projectmanagement.dao.ResourceManagementDAO;
import com.raybiztech.projectmanagement.dto.ClientDTO;
import com.raybiztech.projectmanagement.dto.CountryDTO;
import com.raybiztech.projectmanagement.dto.ManagerDTO;
import com.raybiztech.projectmanagement.dto.MilestoneDTO;
import com.raybiztech.projectmanagement.dto.ProjectDTO;
import com.raybiztech.projectmanagement.dto.ProjectNameDTO;
import com.raybiztech.projectmanagement.invoice.business.Invoice;
import com.sun.org.apache.xpath.internal.operations.Bool;

@Component("projectBuilder")
public class ProjectBuilder {
	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	DAO dao;
	@Autowired
	ResourceManagementDAO resourceManagementDAO;

	Logger logger = Logger.getLogger(ProjectBuilder.class);

	public Project createProjectEntity(ProjectDTO projectDTO) {

		Project project = new Project();
		project.setId(projectDTO.getId());
		project.setProjectName(projectDTO.getProjectName());
		// project.setProjectManager(employee);
		// project.setProjectManager(projectDTO.getManagerId());
		project.setStatus(ProjectStatus.valueOf(projectDTO.getStatus()
				.toUpperCase()));
		project.setDescription(projectDTO.getDescription());
		try {
			Date fromDate = Date.parse(projectDTO.getStartdate(), "dd/MM/yyyy");
			Date toDate = Date.parse(projectDTO.getEnddate(), "dd/MM/yyyy");
			project.setPeriod(new DateRange(fromDate, toDate));
		} catch (ParseException parseException) {
			logger.error("parse exception came while converitng String into Date");
		}
		return project;
	}

	public Project createProjectEntityForAddProject(ProjectDTO projectDTO,
			Employee employee) {

		Project project = new Project();
		project.setId(projectDTO.getId());
		project.setProjectName(projectDTO.getProjectName());
		project.setProjectManager(employee);
		project.setStatus(ProjectStatus.valueOf(projectDTO.getStatus()
				.toUpperCase()));
		project.setDescription(projectDTO.getDescription());
		project.setInternalOrNot(projectDTO.getIntrnalOrNot());
		try {
			Date toDate = null;
			Date fromDate = Date.parse(projectDTO.getStartdate(), "dd/MM/yyyy");
			if (projectDTO.getEnddate() != null)
				toDate = Date.parse(projectDTO.getEnddate(), "dd/MM/yyyy");
			project.setPeriod(new DateRange(fromDate, toDate));
		} catch (ParseException parseException) {
			logger.error("parse exception came while converitng String into Date");
		}
		project.setType(ProjectType.valueOf(projectDTO.getType()));
		project.setHealth(projectDTO.getHealth());
		project.setCreatedDate(new Date());
		project.setHiveProjectName(projectDTO.getHiveProjectName());
		project.setModel(ProjectModel.valueOf(projectDTO.getModel()));
		if(projectDTO.getProjectRequestId()!=null){
		ProjectRequest requestId=dao.findBy(ProjectRequest.class, projectDTO.getProjectRequestId());
		requestId.setStatus("Approved");
		project.setProjectRequest(requestId);
		}
		/*requestId.setProjectContactPerson(projectDTO.getProjectContactPerson());
		requestId.setProjectContactEmail(projectDTO.getProjectContactEmail());
		requestId.setBillingContactPerson(projectDTO.getBillingContactPerson());
		requestId.setBillingContactPersonEmail(projectDTO.getBillingContactPersonEmail());
		project.setProjectRequest(requestId);
		*/
		
		//four new fields customer contact and billing contact person
		project.setProjectContactPerson(projectDTO.getProjectContactPerson());
		project.setProjectContactEmail(projectDTO.getProjectContactEmail());
		project.setBillingContactPerson(projectDTO.getBillingContactPerson());
		project.setBillingContactPersonEmail(projectDTO.getBillingContactPersonEmail());
		project.setPlatform(projectDTO.getPlatform());
		project.setDomain(projectDTO.getDomain());
		if(projectDTO.getHiveProjectFlag() != null && projectDTO.getHiveProjectFlag()) {
			project.setHiveProjectFlag(Boolean.TRUE);
		}
		else {
			project.setHiveProjectFlag(Boolean.FALSE);
		}
		
		return project;
	}

	public List<ProjectDTO> createProjectDTOList(List<Project> projectList) {
		List<ProjectDTO> listDTO = new ArrayList<ProjectDTO>();
		Iterator<Project> projectIterator = projectList.iterator();
		while (projectIterator.hasNext()) {
			Project project = (Project) projectIterator.next();
			ProjectDTO projectDTO = new ProjectDTO();
			projectDTO.setId(project.getId());
			projectDTO.setProjectName(project.getProjectName());
			projectDTO.setStartdate(project.getPeriod().getMinimum()
					.toString("dd/MM/yyyy"));
			if (project.getPeriod().getMaximum() != null)
				projectDTO.setEnddate(project.getPeriod().getMaximum()
						.toString("dd/MM/yyyy"));
			projectDTO.setStatus(project.getStatus().getProjectStatus());
			projectDTO.setDescription(project.getDescription());
			projectDTO
					.setManagerId(project.getProjectManager().getEmployeeId());
			projectDTO
					.setManagerName(project.getProjectManager().getFullName());
			
			projectDTO.setHealth(project.getHealth());
			projectDTO.setHiveProjectName(project.getHiveProjectName() != null ?
					project.getHiveProjectName() :null);
			
			
			/*Project project = dao.findBy(Project.class, allocationDetailsDTO
					.getProject().getId());*/

			Employee deliveryManager = resourceManagementDAO
					.getDeliveryManagerofProject(project);

			projectDTO
					.setDeliveryManager(deliveryManager != null ? deliveryManager
							.getFullName() : "N/A");

			projectDTO.setCount(new Long(resourceManagementDAO
					.allocationSizeOfProjectForEmployee(project)));
			
			if(project.getProjectCode() == null){
				projectDTO.setProjectCode("N/A");
			}
			else{
			projectDTO.setProjectCode(project.getProjectCode());
			}
			//projectDTO.setDeliveryManager(project.get);

			if (project.getClient() != null)
				projectDTO.setClient(project.getClient().getName());
			if (project.getType() != null)
				projectDTO.setType(project.getType().name());

			listDTO.add(projectDTO);

		}
		return listDTO;
	}

	public List<ProjectDTO> createProjectActiveList(List<Project> projectList) {
		List<ProjectDTO> listDTO = new ArrayList<ProjectDTO>();
		Iterator<Project> projectIterator = projectList.iterator();
		while (projectIterator.hasNext()) {
			Project project =  projectIterator.next();
			ProjectDTO projectDTO = new ProjectDTO();
			projectDTO.setId(project.getId());
			projectDTO.setProjectName(project.getProjectName());
			projectDTO.setStartdate(project.getPeriod().getMinimum()
					.toString("dd/MM/yyyy"));
			if (project.getPeriod().getMaximum() != null) {
				projectDTO.setEnddate(project.getPeriod().getMaximum()
						.toString("dd/MM/yyyy"));
			}
			projectDTO.setStatus(project.getStatus().getProjectStatus());
			projectDTO.setDescription(project.getDescription());
			projectDTO
					.setManagerId(project.getProjectManager().getEmployeeId());
			projectDTO.setManagerName(project.getProjectManager()
					.getFirstName()
					+ " "
					+ project.getProjectManager().getLastName());
			listDTO.add(projectDTO);
		}
		return listDTO;
	}

	public ProjectDTO convertFormProjectToProjecDTO(Project project) {
		ProjectDTO projectDTO = new ProjectDTO();
		projectDTO.setId(project.getId());
		projectDTO.setProjectName(project.getProjectName());
		projectDTO.setStartdate(project.getPeriod().getMinimum()
				.toString("dd/MM/yyyy"));
		if (project.getPeriod().getMaximum() != null)
			projectDTO.setEnddate(project.getPeriod().getMaximum()
					.toString("dd/MM/yyyy"));
		projectDTO.setStatus(project.getStatus().getProjectStatus());
		projectDTO.setDescription(project.getDescription());
		projectDTO.setManagerId(project.getProjectManager().getEmployeeId());
		projectDTO.setManagerName(project.getProjectManager().getFullName());
		projectDTO.setHealth(project.getHealth());
		projectDTO.setIntrnalOrNot(project.getInternalOrNot() != null ? project
				.getInternalOrNot() : false);
		if (project.getClient() != null)
			projectDTO.setClient(project.getClient().getOrganization());
			projectDTO.setClientName(project.getClient().getName());
		
		if (project.getType() != null)
			projectDTO.setType(project.getType().name());
		if(project.getModel() !=null)
			projectDTO.setModel(project.getModel().name());

		projectDTO.setHiveProjectName(project.getHiveProjectName());
		if(project.getProjectRequest()!=null)
		projectDTO.setProjectRequestId(project.getProjectRequest().getId());
		/*if(project.getProjectRequest()!=null){
		projectDTO.setProjectContactPerson(project.getProjectRequest().getProjectContactPerson());
		projectDTO.setProjectContactEmail(project.getProjectRequest().getProjectContactEmail());
		projectDTO.setBillingContactPerson(project.getProjectRequest().getBillingContactPerson());
		projectDTO.setBillingContactPersonEmail(project.getProjectRequest().getBillingContactPersonEmail());
		}*/
		
		//four new fields customer contact and billing contact person
		projectDTO.setProjectContactPerson(project.getProjectContactPerson());
		projectDTO.setProjectContactEmail(project.getProjectContactEmail());
		projectDTO.setBillingContactPerson(project.getBillingContactPerson());
		projectDTO.setBillingContactPersonEmail(project.getBillingContactPersonEmail());
		projectDTO.setPlatform(project.getPlatform());
		projectDTO.setDomain(project.getDomain());
		projectDTO.setHiveProjectFlag(project.getHiveProjectFlag());
		return projectDTO;
	}

	public Project createProjectEntityWithManager(ProjectDTO projectDTO,
			Employee employee) {

		Project project = new Project();
		project.setId(projectDTO.getId());
		project.setProjectName(projectDTO.getProjectName());
		// project.setProjectManager(employee);
		project.setProjectManager(employee);
		project.setStatus(ProjectStatus.valueOf(projectDTO.getStatus()
				.toUpperCase()));
		project.setDescription(projectDTO.getDescription());
		try {
			Date fromDate = Date.parse(projectDTO.getStartdate(), "dd/MM/yyyy");
			Date toDate = Date.parse(projectDTO.getEnddate(), "dd/MM/yyyy");
			project.setPeriod(new DateRange(fromDate, toDate));
		} catch (ParseException parseException) {
			logger.error("parse exception came while converitng String into Date");
		}
		return project;
	}

	public List<ProjectNameDTO> createProjectActiveListByProjectName(
			List<Project> projectList) {
		List<ProjectNameDTO> listDTO = new ArrayList<ProjectNameDTO>();
		Iterator<Project> projectIterator = projectList.iterator();
		while (projectIterator.hasNext()) {
			Project project = (Project) projectIterator.next();
			ProjectNameDTO projectNameDTO = new ProjectNameDTO();

			projectNameDTO.setId(project.getId());
			projectNameDTO.setProjectName(project.getProjectName());
			projectNameDTO.setStartdate(project.getPeriod().getMinimum()
					.toString("dd/MM/yyyy"));
			projectNameDTO.setEnddate(project.getPeriod().getMaximum()
					.toString("dd/MM/yyyy"));
			listDTO.add(projectNameDTO);
		}
		return listDTO;
	}

	public ManagerDTO createManagerDTO(Employee employee) {
		ManagerDTO managerDTO = new ManagerDTO();
		managerDTO.setId(employee.getEmployeeId());
		managerDTO.setFirstName(employee.getFirstName());
		managerDTO.setLastName(employee.getLastName());
		return managerDTO;
	}

	public List<ProjectDTO> createProjectActiveListcount(
			List<AllocationDetailsDTO> projectList) {
		List<ProjectDTO> listDTO = new ArrayList<ProjectDTO>();

		for (AllocationDetailsDTO allocationDetailsDTO : projectList) {
			ProjectDTO projectDTO = new ProjectDTO();
			projectDTO.setId(allocationDetailsDTO.getProject().getId());
			projectDTO.setProjectName(allocationDetailsDTO.getProject()
					.getProjectName());
			if(allocationDetailsDTO.getProject().getProjectRequest()!=null)
			{
			projectDTO.setProjectRequestId(allocationDetailsDTO.getProject().getProjectRequest().getId());
			}
			projectDTO.setStartdate(allocationDetailsDTO.getProject()
					.getPeriod().getMinimum().toString("dd/MM/yyyy"));
			if (allocationDetailsDTO.getProject().getPeriod().getMaximum() != null)
				projectDTO.setEnddate(allocationDetailsDTO.getProject()
						.getPeriod().getMaximum().toString("dd/MM/yyyy"));
			projectDTO.setStatus(allocationDetailsDTO.getProject().getStatus()
					.getProjectStatus());
			projectDTO.setDescription(allocationDetailsDTO.getProject()
					.getDescription());
			projectDTO.setManagerId(allocationDetailsDTO.getProject()
					.getProjectManager().getEmployeeId());
			projectDTO.setManagerName(allocationDetailsDTO.getProject()
					.getProjectManager().getFirstName()
					+ " "
					+ allocationDetailsDTO.getProject().getProjectManager()
							.getLastName());
			projectDTO.setCount(allocationDetailsDTO.getCount());
			projectDTO.setHealth(allocationDetailsDTO.getProject().getHealth());
			if(allocationDetailsDTO.getProject().getProjectCode() == null){
				projectDTO.setProjectCode("N/A");
			}
			else{
			projectDTO.setProjectCode(allocationDetailsDTO.getProject().getProjectCode());
			}
			if (allocationDetailsDTO.getProject().getClient() != null) {
				projectDTO.setClient(allocationDetailsDTO.getProject()
						.getClient().getName());
				projectDTO.setClientId(allocationDetailsDTO.getProject()
						.getClient().getId());

			}
			
			Employee deliveryManager = resourceManagementDAO
					.getDeliveryManagerofProject(allocationDetailsDTO
							.getProject());

			projectDTO
					.setDeliveryManager(deliveryManager != null ? deliveryManager
							.getFullName() : "N/A");

			if (allocationDetailsDTO.getProject().getType() != null)
				projectDTO.setType(allocationDetailsDTO.getProject().getType()
						.name());
			listDTO.add(projectDTO);
		}

		return listDTO;
	}

	public List<ProjectDTO> createProjectActiveListcountForEmployee(
			List<AllocationDetailsDTO> projectList) {
		List<ProjectDTO> listDTO = new ArrayList<ProjectDTO>();

		for (AllocationDetailsDTO allocationDetailsDTO : projectList) {
			ProjectDTO projectDTO = new ProjectDTO();
			projectDTO.setId(allocationDetailsDTO.getProject().getId());
			projectDTO.setProjectName(allocationDetailsDTO.getProject()
					.getProjectName());
			projectDTO.setStartdate(allocationDetailsDTO.getProject()
					.getPeriod().getMinimum().toString("dd/MM/yyyy"));
			if (allocationDetailsDTO.getProject().getPeriod().getMaximum() != null)
				projectDTO.setEnddate(allocationDetailsDTO.getProject()
						.getPeriod().getMaximum().toString("dd/MM/yyyy"));
			projectDTO.setStatus(allocationDetailsDTO.getProject().getStatus()
					.getProjectStatus());
			projectDTO.setDescription(allocationDetailsDTO.getProject()
					.getDescription());
			projectDTO.setHiveProjectName(allocationDetailsDTO.getProject().getHiveProjectName() != null ?
					allocationDetailsDTO.getProject().getHiveProjectName() : null);
			projectDTO.setManagerId(allocationDetailsDTO.getProject()
					.getProjectManager().getEmployeeId());
			projectDTO.setManagerName(allocationDetailsDTO.getProject()
					.getProjectManager().getFirstName()
					+ " "
					+ allocationDetailsDTO.getProject().getProjectManager()
							.getLastName());

			Project project = dao.findBy(Project.class, allocationDetailsDTO
					.getProject().getId());

			Employee deliveryManager = resourceManagementDAO
					.getDeliveryManagerofProject(project);

			projectDTO
					.setDeliveryManager(deliveryManager != null ? deliveryManager
							.getFullName() : "N/A");

			projectDTO.setCount(new Long(resourceManagementDAO
					.allocationSizeOfProjectForEmployee(project)));
			projectDTO.setHealth(allocationDetailsDTO.getProject().getHealth());
			if(allocationDetailsDTO.getProject().getProjectCode() == null){
				projectDTO.setProjectCode("N/A");
			}
			else{
			projectDTO.setProjectCode(allocationDetailsDTO.getProject().getProjectCode());
			}
			if (allocationDetailsDTO.getProject().getClient() != null) {
				projectDTO.setClient(allocationDetailsDTO.getProject()
						.getClient().getName());
				projectDTO.setClientId(allocationDetailsDTO.getProject()
						.getClient().getId());

			}

			if (allocationDetailsDTO.getProject().getType() != null)
				projectDTO.setType(allocationDetailsDTO.getProject().getType()
						.name());
			listDTO.add(projectDTO);
		}

		return listDTO;
	}

	public Project getProjectFromProjectDTO(ProjectDTO projectDTO,
			Project project, Employee employee) {
		project.setDescription(projectDTO.getDescription());
		project.setProjectName(projectDTO.getProjectName());
		project.setStatus(ProjectStatus.valueOf(projectDTO.getStatus()
				.toUpperCase()));
		try {
			Date fromDate = Date.parse(projectDTO.getStartdate(), "dd/MM/yyyy");
			Date toDate = Date.parse(projectDTO.getEnddate(), "dd/MM/yyyy");
			project.setPeriod(new DateRange(fromDate, toDate));
		} catch (ParseException parseException) {
			logger.error("parse exception came while converitng String into Date");
		}
		project.setProjectManager(employee);
		project.setHealth(projectDTO.getHealth());
		project.setType(ProjectType.valueOf(projectDTO.getType()));
		project.setModel(ProjectModel.valueOf(projectDTO.getModel()));
		project.setInternalOrNot(projectDTO.getIntrnalOrNot());
		project.setHiveProjectName(projectDTO.getHiveProjectName());
		
		//four new fields customer contact and billing contact person
		project.setProjectContactPerson(projectDTO.getProjectContactPerson());
		project.setProjectContactEmail(projectDTO.getProjectContactEmail());
		project.setBillingContactPerson(projectDTO.getBillingContactPerson());
		project.setBillingContactPersonEmail(projectDTO.getBillingContactPersonEmail());
		project.setPlatform(projectDTO.getPlatform());
		project.setDomain(projectDTO.getDomain());
		return project;

	}

	public List<ProjectDTO> ConvertProjectAllocationDetailsEntyToDTO(
			Map<Project, AllocationDetails> projectDetails, Long employeeId) {
		List<ProjectDTO> projectDTOList = null;
		if (!projectDetails.isEmpty()) {
			projectDTOList = new ArrayList<ProjectDTO>();
			for (Map.Entry<Project, AllocationDetails> entry : projectDetails
					.entrySet()) {
				// logger.info(entry.getKey() + "/" + entry.getValue());
				Project project = entry.getKey();
				AllocationDetails details = entry.getValue();
				ProjectDTO projectDTO = convertFormProjectToProjecDTO(project);
				projectDTO.setEmployeeId(employeeId);
				projectDTO.setAllocation(details.getPercentage().toString("#0",
						false));
				projectDTO.setBillable(details.getBillable());
				projectDTO.setIsAllocated(details.getIsAllocated());
				projectDTO.setStartdate(details.getPeriod().getMinimum()
						.toString("dd/MM/yyyy"));
				projectDTO.setEnddate(details.getPeriod().getMaximum()
						.toString("dd/MM/yyyy"));
				projectDTO.setProjectStartdate(project.getPeriod().getMinimum()
						.toString("dd/MM/yyyy"));
				if (project.getPeriod().getMaximum() != null)
					projectDTO.setProjectEndDate(project.getPeriod()
							.getMaximum().toString("dd/MM/yyyy"));
				projectDTOList.add(projectDTO);
			}
		}
		return projectDTOList;
	}

	public Client createClientEntity(ClientDTO clientDTO) {
		Client client = new Client();
		client.setClientCode(clientDTO.getClientCode());
		client.setName(clientDTO.getName());
		client.setAddress(clientDTO.getAddress());
		client.setEmail(clientDTO.getEmail());
		client.setPersonName(clientDTO.getPersonName());
		client.setPhone(clientDTO.getPhone());
		client.setDescription(clientDTO.getDescription());
		client.setOrganization(clientDTO.getOrganization());
		client.setGstCode(clientDTO.getGstCode());
		client.setClientStatus(clientDTO.getClientStatus());	
		return client;

	}

	public List<ClientDTO> createClientDTOsList(List<Client> allClients) {
		List<ClientDTO> al = new ArrayList<ClientDTO>();
		if (allClients != null) {
			for (Client client : allClients) {
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
				clientDTO.setGstCode(client.getGstCode());
				if (client.getCountry() != null)
					clientDTO.setCountry(client.getCountry().getName());
				if(client.getClientStatus() != null)
					clientDTO.setClientStatus(client.getClientStatus());
				for (Project project : client.getProjects()) {

					if (project.getType() != null) {

						if (project.getType().equals(ProjectType.FIXEDBID)) {
							clientDTO.setTotalFixedBids(clientDTO
									.getTotalFixedBids() + 1);
							continue;
						}

						if (project.getType().equals(ProjectType.RETAINER))
							clientDTO.setTotalRetainers(clientDTO
									.getTotalRetainers() + 1);
					}

				}
				al.add(clientDTO);
			}

		}
		return al;

	}

	public ClientDTO createClientDTO(Client client) {
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
		clientDTO.setGstCode(client.getGstCode());
		if (client.getCountry() != null) {
			clientDTO.setCountry(client.getCountry().getName());
		}
		if(client.getClientStatus() != null){
			clientDTO.setClientStatus(client.getClientStatus());
		}
		return clientDTO;
	}

	public List<CountryDTO> getCountriesDTOsList(List<Country> countries) {
		List<CountryDTO> countryDTOs = new ArrayList<CountryDTO>();
		for (Country country : countries) {
			CountryDTO countryDTO = new CountryDTO();
			countryDTO.setId(country.getId());
			countryDTO.setName(country.getName());
			countryDTOs.add(countryDTO);

		}
		return countryDTOs;
	}

	public Client getUpdatedClient(Client client, ClientDTO clientDTO) {
		client.setClientCode(clientDTO.getClientCode());
		client.setAddress(clientDTO.getAddress());
		client.setDescription(clientDTO.getDescription());
		client.setEmail(clientDTO.getEmail());
		client.setName(clientDTO.getName());
		client.setOrganization(clientDTO.getOrganization());
		client.setPersonName(clientDTO.getPersonName());
		client.setPhone(clientDTO.getPhone());
		client.setGstCode(clientDTO.getGstCode());
		if(client.getClientStatus() != null){
			client.setClientStatus(clientDTO.getClientStatus());
		}
		
		return client;

	}

	public List<MilestoneDTO> createBillingDTO(List<Milestone> milestones) {

		Iterator<Milestone> iterator = milestones.iterator();
		List<MilestoneDTO> milestoneDTOs = new ArrayList<MilestoneDTO>();
		while (iterator.hasNext()) {
			Milestone milestone = (Milestone) iterator.next();

			MilestoneDTO milestoneDTO = new MilestoneDTO();
			if (milestone.getActualDate() != null) {
				milestoneDTO.setActualDate(milestone.getActualDate().toString(
						"dd/MM/yyyy"));
			}

			milestoneDTO.setBillable(milestone.isBillable());
			milestoneDTO.setComments(milestone.getComments());
			milestoneDTO.setMilestoneNumber(milestone.getMilestoneNumber());
			milestoneDTO.setId(milestone.getId());
			if (milestone.getPlanedDate() != null) {
				milestoneDTO.setPlanedDate(milestone.getPlanedDate().toString(
						"dd/MM/yyyy"));
			}
			milestoneDTO.setProject(milestone.getProject().getProjectName());
			if (milestone.getProject().getType() != null)
				milestoneDTO.setProjectType(milestone.getProject().getType()
						.name());
			if (milestone.getProject().getClient() != null)
				milestoneDTO.setClient(milestone.getProject().getClient()
						.getName());
			milestoneDTO.setTitle(milestone.getTitle());
			milestoneDTO.setProjectId(milestone.getProject().getId());
			milestoneDTO.setMilestonePercentage(milestone
					.getMilestonePercentage());// for milestone percentage in
												// billing reports
			milestoneDTO.setInvoiceStatus(milestone.getInvoiceStatus());
			milestoneDTO.setIsClosed(milestone.isClosed());

			ChangeRequest changeRequest = milestone.getChangeRequest();
			if (changeRequest != null) {
				milestoneDTO.setCrId(changeRequest.getId());
				milestoneDTO.setCrName(changeRequest.getName());
			}
			
			milestoneDTO.setInvoiceReopenFlag(milestone.getInvoiceReopenFlag());
			milestoneDTO.setProjectType(milestone.getProject().getType().name());
			milestoneDTO.setMilestoneTypeFlag(milestone.getMilestoneTypeFlag() != null ? milestone.getMilestoneTypeFlag() : null);
			
			milestoneDTOs.add(milestoneDTO);

		}

		return milestoneDTOs;
	}

	public MilestoneDTO getMilestone(Long milestoneid) {

		Milestone milestone = dao.findBy(Milestone.class, milestoneid);

		MilestoneDTO milestoneDTO = null;
		if (milestone != null) {
			milestoneDTO = new MilestoneDTO();
			milestoneDTO.setId(milestone.getId());
			milestoneDTO.setTitle(milestone.getTitle());
			milestoneDTO
					.setMilestoneNumber((milestone.getMilestoneNumber() != null ? milestone
							.getMilestoneNumber() : null));
			if (milestone.getActualDate() != null) {
				milestoneDTO.setActualDate(milestone.getActualDate().toString(
						"dd/MM/yyyy"));
			}
			milestoneDTO.setPlanedDate(milestone.getPlanedDate().toString(
					"dd/MM/yyyy"));
			milestoneDTO.setBillable(milestone.isBillable());
			milestoneDTO.setComments(milestone.getComments());
			milestoneDTO.setIsClosed(milestone.isClosed());
			milestoneDTO.setInvoiceStatus(milestone.getInvoiceStatus());
			milestoneDTO.setInvoiceReopenFlag(milestone.getInvoiceReopenFlag());
			milestoneDTO.setMilestonePercentage(milestone
					.getMilestonePercentage());
			if (milestone.getChangeRequest() != null) {
				milestoneDTO.setCrId(milestone.getChangeRequest().getId());
				milestoneDTO.setCrName(milestone.getChangeRequest().getName());
			}
		}

		return milestoneDTO;

	}

	public ProjectAudit convertProjectTOProjectAudit(Project project,
			Long projectId, String persistType) {
		Long loggedInEmpId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		ProjectAudit projectAudit = null;
		if (project != null) {
			projectAudit = new ProjectAudit();
			projectAudit.setProjectName(project.getProjectName());
			projectAudit.setDescription(project.getDescription());
			projectAudit.setClient(project.getClient());
			projectAudit.setHealth(project.getHealth());
			projectAudit.setModifiedBy(employee.getFullName());
			projectAudit.setModifiedDate(new Second());
			projectAudit.setPersistType(persistType);
			projectAudit.setProjectId(projectId);
			projectAudit.setProjectManager(project.getProjectManager());
			projectAudit.setStatus(project.getStatus());
			projectAudit.setType(project.getType());
			projectAudit.setPeriod(project.getPeriod());
			// projectAudit.setAllocationDetailsAudit(project.getAllocationDetails()!=null
			// ?
			// convertTOAllocationDetailsAudit(project.getAllocationDetails()):null);
		}
		return projectAudit;

	}

	
	// public Set<AllocationDetailsAudit>
	// convertTOAllocationDetailsAudit(Set<AllocationDetails> details){
	//
	// Set<AllocationDetailsAudit> audits = new
	// HashSet<AllocationDetailsAudit>();
	//
	// for(AllocationDetails ad : details){
	// System.out.println("emp name @@@@@@@@@@:"+ad.getEmployee().getEmployeeId());
	// AllocationDetailsAudit audit = new AllocationDetailsAudit();
	// audit.setBillable(ad.getBillable());
	// audit.setComments(ad.getComments());
	// audit.setEmployee(ad.getEmployee());
	// audit.setIsAllocated(ad.getIsAllocated());
	// audit.setPercentage(ad.getPercentage());
	// audit.setPeriod(ad.getPeriod());
	// audits.add(audit);
	// }
	//
	// return audits;
	//
	// }
}
