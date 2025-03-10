package com.raybiztech.contact.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.icu.text.SimpleDateFormat;
import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.checklist.business.ChecklistSection;
import com.raybiztech.contact.business.ContactInfo;
import com.raybiztech.contact.dto.ContactInfoDTO;
import com.raybiztech.date.Second;

@Component("contactInfoBuilder")
public class ContactInfoBuilder {

        @Autowired
        DAO dao;
        @Autowired
        SecurityUtils securityUtils;

        public ContactInfoDTO getDto(ContactInfo entity) {
        	ContactInfoDTO dto = null;
                if (entity != null) {
                        dto = new ContactInfoDTO();
                        dto.setId(entity.getId());
                        dto.setTitle(entity.getTitle());
                        dto.setDescription(entity.getDescription());
                        if (entity.getPageName() != null) {
                                dto.setPageName(entity.getPageName());
                        }
                        dto.setType(entity.getType());
                        dto.setUserName(entity.getUserName());
                        /*
                         * if (entity.getEmpDepartment() == null) {
                         * dto.setDepartmentName(entity.getEmpDepartment().getDepartmentName());
                         * dto.setDepartmentId(entity.getEmpDepartment().getDepartmentId()); }
                         */
                        if (entity.getEmpDepartment() != null) {
                                dto.setDepartmentName(entity.getEmpDepartment().getDepartmentName());
                                dto.setDepartmentId(entity.getEmpDepartment().getDepartmentId());

                        }
                      dto.setUpdatedDate(entity.getUpdatedDate()!=null?entity.getUpdatedDate():null);
                      System.out.println("date up:" + dto.getUpdatedDate());
                        
                }
                return dto;
        }

        public ContactInfo getEntity(ContactInfoDTO dto) {
                Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
                Employee employee = dao.findBy(Employee.class, loggedInEmpId);
                SimpleDateFormat formatter = new SimpleDateFormat("dd:MM:yyyy hh:mm a");
                ContactInfo entity = null;
                if (dto != null) {
                        entity = new ContactInfo();
                        entity.setId(dto.getId());
                        entity.setTitle(dto.getTitle());
                        entity.setDescription(dto.getDescription());
                        if (dto.getPageName() != null) {
                                entity.setPageName(dto.getPageName());
                        }
                        entity.setType(dto.getType());
                        entity.setUserName(employee.getFullName());
                        if (dto.getDepartmentId() != null) {
                                entity.setEmpDepartment(
                                                dto.getDepartmentId() != null ? dao.findBy(EmpDepartment.class, dto.getDepartmentId())
                                                                : dao.findByUniqueProperty(EmpDepartment.class, "departmentName",
                                                                                employee.getDepartmentName()));
                              

                        }
                        String s = formatter.format(new Date());
                        Date date = null;
						try {
							date = formatter.parse(s);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println(date);
                        entity.setCreatedDate(date);
                        entity.setUpdatedDate(date);
                        
                }
                return entity;
        }

        public List<ContactInfoDTO> getDtoList(List<ContactInfo> entityList) {
                List<ContactInfoDTO> dtoList = new ArrayList<ContactInfoDTO>();

                for (ContactInfo entity : entityList) {
                        dtoList.add(getDto(entity));
                }

                return dtoList;
        }

        public List<ContactInfo> getEntityList(List<ContactInfoDTO> dtoList) {
                List<ContactInfo> entityList = new ArrayList<ContactInfo>();

                for (ContactInfoDTO dto : dtoList) {
                        entityList.add(getEntity(dto));
                }

                return entityList;
        }
}