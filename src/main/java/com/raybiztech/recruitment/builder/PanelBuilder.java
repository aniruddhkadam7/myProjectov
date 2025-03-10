/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.recruitment.business.Companies;
import com.raybiztech.recruitment.business.Department;
import com.raybiztech.recruitment.business.Panel;
import com.raybiztech.recruitment.dto.CompanyDTO;
import com.raybiztech.recruitment.dto.DepartmentDTO;
import com.raybiztech.recruitment.dto.PanelDTO;

/**
 *
 * @author hari
 */
@Component("panelBuilder")
public class PanelBuilder {

    @Autowired
    EmployeeBuilder employeeBuilder;

    public List<PanelDTO> convertPanelListToPanelDTOList(List<Panel> panelList) {

        List<PanelDTO> panelDTOs = null;

        if (panelList != null) {
            panelDTOs = new ArrayList<PanelDTO>();

            for (Panel panel : panelList) {
                PanelDTO panelDTO = new PanelDTO();
                panelDTO.setPanelId(panel.getPanelId());
                panelDTO.setEmployeeId(panel.getEmployee().getEmployeeId());
                panelDTO.setFirstName(panel.getEmployee().getFirstName());
                panelDTO.setEmail(panel.getEmployee().getEmail());
                panelDTO.setDepartmentName(panel.getEmployee().getDepartmentName());
                panelDTO.setPhone(panel.getEmployee().getPhone());
                panelDTO.setDesignation(panel.getEmployee().getDesignation());
                panelDTOs.add(panelDTO);
            }
        }

        return panelDTOs;

    }

    public List<DepartmentDTO> convertDepartmentListToDepartmentDTOList(List<Department> departments) {

        List<DepartmentDTO> departmentDTOs = null;
        if (!departments.isEmpty()) {
            departmentDTOs = new ArrayList<DepartmentDTO>();
            for (Department department : departments) {
                DepartmentDTO dTO = new DepartmentDTO();
                dTO.setId(department.getId());
                dTO.setName(department.getName());
                departmentDTOs.add(dTO);
            }
        }
        return departmentDTOs;

    }

    public List<EmployeeDTO> convertPanelListToEmployeeDTOList(List<Panel> panels) {
        List<EmployeeDTO> employeeDTOList = new ArrayList<EmployeeDTO>();

        if (panels != null) {

            for (Panel panel : panels) {
                EmployeeDTO employeeDTO = employeeBuilder.createEmployeeDTO(panel.getEmployee());
                employeeDTOList.add(employeeDTO);
            }
        }

        return employeeDTOList;
    }

    public List<CompanyDTO> convertCompaniesListToCompaniesDTOList(List<Companies>  comapnies) {

        List<CompanyDTO> companiesDTOs = null;
        if (comapnies != null) {
        	companiesDTOs = new ArrayList<CompanyDTO>();
            for (Companies company : comapnies) {
            	CompanyDTO dTO = new CompanyDTO();
                dTO.setCompanyId(company.getCompanyId());
                dTO.setCompanyName(company.getCompanyName());
                companiesDTOs.add(dTO);
            }
        }
        return companiesDTOs;

    }
    
}
