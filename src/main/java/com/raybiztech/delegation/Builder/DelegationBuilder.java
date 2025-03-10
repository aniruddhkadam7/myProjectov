package com.raybiztech.delegation.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.delegation.dto.DelegationDTO;


@Component("delegationBuilder")
public class DelegationBuilder {

	@Autowired
	DAO dao;
	
	//convert to entity object
	public List<Employee> convertDtoToEntity(DelegationDTO delegationDTO)
	{
		
		Set<Long> employeeIds = delegationDTO.getEmployeeId();
		List<Employee> employeeUpdateManagerList=new ArrayList<Employee>();
		
		if(delegationDTO!=null)
		{
			for(Long empId:employeeIds)
			{
				Employee e=dao.findBy(Employee.class, empId);
				Employee manger=dao.findBy(Employee.class, delegationDTO.getManagerId());
				//e.setManager(manger);
				employeeUpdateManagerList.add(e);
			}
		}
		
		return employeeUpdateManagerList;
	}

	public List<EmployeeDTO> convertEntityToDTOList(List<Employee> managerReportees) {

		List<EmployeeDTO> employeesList = new ArrayList<EmployeeDTO>();
		
		for(Employee e:managerReportees)
		{
			EmployeeDTO employeeDTO = new EmployeeDTO();
			employeeDTO.setId(e.getEmployeeId());
			employeeDTO.setFirstName(e.getFirstName());
			employeeDTO.setLastName(e.getLastName());
			employeeDTO.setFullName(e.getFullName());
			employeeDTO.setDepartmentName(e.getDepartmentName());
			employeeDTO.setDesignation(e.getDesignation());
			employeesList.add(employeeDTO);
		}
		
		return employeesList;
	}

	public List<EmployeeDTO> convertEntityToMangersList(List<Employee> mangers) {
		
		List<EmployeeDTO> employeesList = new ArrayList<EmployeeDTO>();
		
		for(Employee e:mangers)
		{
			EmployeeDTO employeeDTO = new EmployeeDTO();
			employeeDTO.setId(e.getEmployeeId());
			employeeDTO.setFirstName(e.getFirstName());
			employeeDTO.setLastName(e.getLastName());
			employeeDTO.setFullName(e.getFullName());
			employeeDTO.setDepartmentName(e.getDepartmentName());
			employeeDTO.setDesignation(e.getDesignation());
			employeeDTO.setRole(e.getEmpRole().getName());
			employeesList.add(employeeDTO);
		}
		
		return employeesList;
	}
	//convert to entity object
		public List<Employee> convertDtoToHrEntity(DelegationDTO delegationDTO)
		{
			
			Set<Long> employeeIds = delegationDTO.getEmployeeId();
			List<Employee> employeeUpdateManagerList=new ArrayList<Employee>();
			
			if(delegationDTO!=null)
			{
				for(Long empId:employeeIds)
				{
					Employee e=dao.findBy(Employee.class, empId);
					Employee manger=dao.findBy(Employee.class, delegationDTO.getManagerId());
					//e.setHrAssociate(manger);
					employeeUpdateManagerList.add(e);
				}
			}
			
			return employeeUpdateManagerList;
		}
}
