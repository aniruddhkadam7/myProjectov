/*

 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.projectmanagement.builder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.SQAAudit.business.SQAAuditForm;
import com.raybiztech.SQAAudit.business.SQAAuditees;
import com.raybiztech.SQAAudit.business.SQAAuditors;
import com.raybiztech.SQAAudit.utility.AuditeesComparator;
import com.raybiztech.SQAAudit.utility.AuditorsComparator;
import com.raybiztech.achievement.business.Achievement;
import com.raybiztech.achievement.business.AchievementType;
import com.raybiztech.appraisals.PIPManagement.business.PIP;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.EmployeeFamilyInformation;
import com.raybiztech.appraisals.business.TimeSlot;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.commons.Percentage;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.Second;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.projectmanagement.business.Client;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.business.ProjectRequest;
import com.raybiztech.projectmanagement.business.ProjectStatus;
import com.raybiztech.projectmanagement.business.ProjectType;
import com.raybiztech.recruitment.business.JobVacancy;
import com.raybiztech.rolefeature.business.Role;
import com.raybiztech.supportmanagement.business.SupportTickets;
import com.raybiztech.supportmanagement.business.TicketsCategory;
import com.raybiztech.supportmanagement.business.TicketsSubCategory;
import com.raybiztech.supportmanagement.business.Tracker;

/**
 *
 * @author anil
 */
@Component("auditBuilder")
public class AuditBuilder {

	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	DAO dao;

	Logger logger = Logger.getLogger(AuditBuilder.class);

	// create project
	public List<Audit> convertTOProjectEntity(Project project, Long projectId, String tableName, String persisType) {

		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		List<Audit> audits = new ArrayList<Audit>();
		Field[] fields = project.getClass().getDeclaredFields();
		for (Field field : fields) {
			// logger.warn("field name is" + field.getName());
			if (!(field.getName().equalsIgnoreCase("id") || field.getName().equalsIgnoreCase("allocationDetails")
					|| field.getName().equalsIgnoreCase("milestones")
					|| field.getName().equalsIgnoreCase("statusReports") || field.getName().equalsIgnoreCase("feedPost")
					|| field.getName().equalsIgnoreCase("createdDate") || field.getName().equalsIgnoreCase("logger")
					|| field.getName().equalsIgnoreCase("serialVersionUID"))) {
				try {

					Field newValueField = project.getClass().getDeclaredField(field.getName());
					newValueField.setAccessible(true);
					Object currentValue = newValueField.get(project);
					String newValue = null;

					if (currentValue instanceof String) {
						newValue = (String) currentValue;

					} else if (currentValue instanceof Employee) {
						Employee newEmployee = (Employee) currentValue;
						newValue = newEmployee.getEmployeeId().toString();

					} else if (currentValue instanceof ProjectStatus) {
						ProjectStatus newStatus = (ProjectStatus) currentValue;
						newValue = newStatus.getProjectStatus();
					} else if (currentValue instanceof Client) {
						Client newClient = (Client) currentValue;
						newValue = newClient.getName();
					} else if (currentValue instanceof ProjectType) {
						ProjectType newProjectType = (ProjectType) currentValue;
						newValue = newProjectType.getType();
					}
					Audit audit = componentForPOSTData(projectId, tableName, field.getName(), newValue, persisType,
							employee.getEmployeeId(), new Second());
					if (audit != null) {
						audits.add(audit);
					}
					if (currentValue instanceof DateRange) {
						DateRange newDateRnge = (DateRange) currentValue;

						Audit audit1 = componentForPOSTData(projectId, tableName, "projectStartDate",
								newDateRnge.getMinimum().toString(), persisType, employee.getEmployeeId(),
								new Second());
						if (audit1 != null)
							audits.add(audit1);

						if (newDateRnge.getMaximum() != null) {
							Audit audit2 = componentForPOSTData(projectId, tableName, "projectEndDate",
									newDateRnge.getMaximum().toString(), persisType, employee.getEmployeeId(),
									new Second());
							if (audit2 != null)
								audits.add(audit2);

						}

					}

				} catch (NoSuchFieldException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (SecurityException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalArgumentException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
		return audits;
	}

	// POST component for audit
	public Audit componentForPOSTData(Long referenceId, String tableName, String columnName, String newValue,
			String persistType, Long modifiedBy, Second modifiedDate) {
		if (newValue != null && newValue != "") {
			Audit audit = new Audit();
			audit.setReferenceId(referenceId);
			audit.setTableName(tableName);
			audit.setColumnName(columnName);
			audit.setNewValue(newValue);
			audit.setPersistType(persistType);
			audit.setModifiedBy(modifiedBy);
			audit.setModifiedDate(modifiedDate);
			return audit;
		} else {
			return null;
		}
	}

	// Update Project
	public List<Audit> projectUpdateAudit(Project project, Long projectId, Project oldProject, String tableName,
			String persisType) {
		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		List<Audit> audits = new ArrayList<Audit>();
		Field[] fields = project.getClass().getDeclaredFields();
		for (Field field : fields) {
			// logger.warn("field name is" + field.getName());
			if (!(field.getName().equalsIgnoreCase("id") || field.getName().equalsIgnoreCase("allocationDetails")
					|| field.getName().equalsIgnoreCase("milestones")
					|| field.getName().equalsIgnoreCase("statusReports") || field.getName().equalsIgnoreCase("feedPost")
					|| field.getName().equalsIgnoreCase("createdDate") || field.getName().equalsIgnoreCase("logger")
					|| field.getName().equalsIgnoreCase("serialVersionUID"))) {
				try {
					Field oldValueField = oldProject.getClass().getDeclaredField(field.getName());
					oldValueField.setAccessible(true);
					Object existedValue = oldValueField.get(oldProject);

					Field newValueField = project.getClass().getDeclaredField(field.getName());
					newValueField.setAccessible(true);
					Object currentValue = newValueField.get(project);
					String newValue = null, oldValue = null, additionalInfo = null;

					if (currentValue instanceof String) {
						newValue = (String) currentValue;
						oldValue = (String) existedValue;
						// logger.warn("new vlaue:" + newValue);
						// logger.warn("old vlaue:" + oldValue);

					} else if (currentValue instanceof Employee) {
						Employee newEmployee = (Employee) currentValue;
						newValue = newEmployee.getEmployeeId().toString();
						Employee oldEmployee = (Employee) existedValue;
						oldValue = oldEmployee.getEmployeeId().toString();

					} else if (currentValue instanceof ProjectStatus) {
						ProjectStatus newStatus = (ProjectStatus) currentValue;
						newValue = newStatus.getProjectStatus();
						ProjectStatus oldStatus = (ProjectStatus) existedValue;
						oldValue = oldStatus.getProjectStatus();
					} else if (currentValue instanceof Client) {
						Client newClient = (Client) currentValue;
						newValue = newClient.getName();
						Client oldClient = (Client) existedValue;
						oldValue = oldClient.getName();
					} else if (currentValue instanceof ProjectType) {
						ProjectType newProjectType = (ProjectType) currentValue;
						newValue = newProjectType.getType();
						ProjectType oldProjectType = (ProjectType) existedValue;
						oldValue = oldProjectType != null ? oldProjectType.getType() : null;
					}
					Audit audit = componentForAudit(projectId, tableName, field.getName(), oldValue, newValue,
							additionalInfo, persisType, employee.getEmployeeId(), new Second());
					if (audit != null) {
						audits.add(audit);
					}
					if (currentValue instanceof DateRange) {
						DateRange newDateRnge = (DateRange) currentValue;
						DateRange oldDateRnge = (DateRange) existedValue;

						Audit audit1 = componentForAudit(projectId, tableName, "projectStartDate",
								oldDateRnge.getMinimum().toString(), newDateRnge.getMinimum().toString(),
								additionalInfo, persisType, employee.getEmployeeId(), new Second());
						if (audit1 != null)
							audits.add(audit1);

						Audit audit2 = componentForAudit(projectId, tableName, "projectEndDate",
								oldDateRnge.getMaximum() != null ? oldDateRnge.getMaximum().toString() : "",
								newDateRnge.getMaximum() != null ? newDateRnge.getMaximum().toString() : "",
								additionalInfo, persisType, employee.getEmployeeId(), new Second());
						// audit2 = componentForAudit(projectId, tableName,
						// "toDate", "", newDateRnge.getMaximum().toString(),
						// persisType, employee.getEmployeeId(), new Second());
						if (audit2 != null)
							audits.add(audit2);

					}

				} catch (NoSuchFieldException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (SecurityException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalArgumentException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}

		// if (project.getClient() != null) {
		// Audit audit = componentForAudit(project.getId(), "Project", "Client",
		// oldProject.getClient().getName(), project.getClient().getName(),
		// persisType, employee.getFullName(), new Second());
		// if (audit != null) {
		// audits.add(audit);
		// }
		// }
		return audits;

	}

	// Update component for audit
	public Audit componentForAudit(Long referenceId, String tableName, String columnName, String oldValue,
			String newValue, String additionalInfo, String persistType, Long modifiedBy, Second modifiedDate) {
		// logger.warn("condition:" + oldValue != null && newValue != null);
		//logger.warn("in component");
		if (oldValue != null && newValue != null) {
              //  logger.warn("in if");
			if (!oldValue.equalsIgnoreCase(newValue)) {
				//logger.warn("in if equal");
				Audit audit = new Audit();
				audit.setReferenceId(referenceId);
				audit.setTableName(tableName);
				audit.setColumnName(columnName);
				//logger.warn("tableName"+tableName);
				//logger.warn("columnName"+columnName);
				audit.setOldValue(oldValue);
				audit.setNewValue(newValue);
				audit.setAdditionalInfo(additionalInfo);
				audit.setPersistType(persistType);
				audit.setModifiedBy(modifiedBy);
				audit.setModifiedDate(modifiedDate);
				return audit;
			} else {
				//logger.warn("in eles not equal ");
				return null;
			}
		} else if (oldValue != null || newValue != null) {
			// logger.warn("in else if");
			Audit audit = new Audit();
			
			//logger.warn("tableName"+tableName);
			//logger.warn("columnName"+columnName);
			
			audit.setReferenceId(referenceId);
			audit.setTableName(tableName);
			audit.setColumnName(columnName);
			audit.setOldValue(oldValue);
			audit.setNewValue(newValue);
			audit.setPersistType(persistType);
			audit.setModifiedBy(modifiedBy);
			audit.setModifiedDate(modifiedDate);
			return audit;
		} else {
			//logger.warn("last else");
			return null;
		}
	}

	// update employee allocation details
	public List<Audit> updateAllocationDetailsTOAudit(AllocationDetails allocationDetails,
			AllocationDetails existedDetails, Employee allocateEmployee, Long projectId, String tableName,
			String persisType) {

		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		List<Audit> audits = new ArrayList<Audit>();
		Field[] fields = allocationDetails.getClass().getDeclaredFields();
		for (Field field : fields) {
			// logger.warn("field name is" + field.getName());
			if (!(field.getName().equalsIgnoreCase("id") || field.getName().equalsIgnoreCase("project")
					|| field.getName().equalsIgnoreCase("logger")
					|| field.getName().equalsIgnoreCase("serialVersionUID"))) {
				try {
					Field oldValueField = existedDetails.getClass().getDeclaredField(field.getName());
					Object existedValue = oldValueField.get(existedDetails);

					Field newValueField = allocationDetails.getClass().getDeclaredField(field.getName());
					Object currentValue = newValueField.get(allocationDetails);
					String newValue = null, oldValue = null,
							additionalInfo = allocateEmployee.getEmployeeId().toString();

					// if (currentValue instanceof String) {
					// newValue = (String) currentValue;
					// oldValue = (String) existedValue;
					// // logger.warn("new vlaue:" + newValue);
					// // logger.warn("old vlaue:" + oldValue);
					//
					// }
					// if (currentValue instanceof Employee) {
					// Employee newEmployee = (Employee) currentValue;
					// newValue = newEmployee.getEmployeeId().toString();
					// Employee oldEmployee = (Employee) existedValue;
					// oldValue = oldEmployee.getEmployeeId().toString();
					//
					// }
					if (currentValue instanceof Boolean) {
						Boolean newStatus = (Boolean) currentValue;
						newValue = newStatus.toString();
						Boolean oldStatus = (Boolean) existedValue;
						oldValue = oldStatus.toString();
						// additionalInfo =
						// allocateEmployee.getEmployeeId().toString();
					} else if (currentValue instanceof Percentage) {
						Percentage newStatus = (Percentage) currentValue;
						newValue = newStatus.toString("#0", false);
						Percentage oldStatus = (Percentage) existedValue;
						oldValue = oldStatus.toString("#0", false);
						// additionalInfo =
						// allocateEmployee.getEmployeeId().toString();
					}
					Audit audit = componentForAudit(projectId, tableName, field.getName(), oldValue, newValue,
							additionalInfo, persisType, employee.getEmployeeId(), new Second());
					if (audit != null)
						audits.add(audit);

					if (currentValue instanceof DateRange) {
						DateRange newDateRnge = (DateRange) currentValue;
						DateRange oldDateRnge = (DateRange) existedValue;
						// additionalInfo =
						// allocateEmployee.getEmployeeId().toString();

						Audit audit1 = componentForAudit(projectId, tableName, "allocationStartDate",
								oldDateRnge.getMinimum().toString(), newDateRnge.getMinimum().toString(),
								additionalInfo, persisType, employee.getEmployeeId(), new Second());
						if (audit1 != null)
							audits.add(audit1);

						Audit audit2;
						audit2 = componentForAudit(projectId, tableName, "allocationEndDate",
								oldDateRnge.getMaximum().toString(), newDateRnge.getMaximum().toString(),
								additionalInfo, persisType, employee.getEmployeeId(), new Second());
						if (audit2 != null)
							audits.add(audit2);
					}

				} catch (NoSuchFieldException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (SecurityException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalArgumentException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
		return audits;
	}

	// Employee allocation for Project
	public List<Audit> allocationDetailsTOPOSTAudit(AllocationDetails allocationDetails, Employee allocateEmployee,
			Long projectId, String tableName, String persisType) {

		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		List<Audit> audits = new ArrayList<Audit>();
		Field[] fields = allocationDetails.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (!(field.getName().equalsIgnoreCase("id") || field.getName().equalsIgnoreCase("project")
					|| field.getName().equalsIgnoreCase("logger")
					|| field.getName().equalsIgnoreCase("serialVersionUID"))) {
				try {

					Field newValueField = allocationDetails.getClass().getDeclaredField(field.getName());
					Object currentValue = newValueField.get(allocationDetails);
					String newValue = null;

					if (currentValue instanceof String) {
						newValue = (String) currentValue;
						// if(newValue=="")
						// newValue=null;
					} else if (field.getName().equalsIgnoreCase("employee")) {
						// Employee newEmployee = (Employee) currentValue;
						newValue = allocateEmployee.getEmployeeId().toString();

					} else if (currentValue instanceof Boolean) {
						Boolean newStatus = (Boolean) currentValue;
						newValue = newStatus.toString();
					} else if (currentValue instanceof Percentage) {
						Percentage newStatus = (Percentage) currentValue;
						newValue = newStatus.toString("#0", false);
					}
					Audit audit = componentForPOSTData(projectId, tableName, field.getName(), newValue, persisType,
							employee.getEmployeeId(), new Second());
					if (audit != null) {
						audits.add(audit);
					}
					if (currentValue instanceof DateRange) {
						DateRange newDateRnge = (DateRange) currentValue;

						Audit audit1 = componentForPOSTData(projectId, tableName, "allocationStartDate",
								newDateRnge.getMinimum().toString(), persisType, employee.getEmployeeId(),
								new Second());
						if (audit1 != null)
							audits.add(audit1);

						Audit audit2 = componentForPOSTData(projectId, tableName, "allocationEndDate",
								newDateRnge.getMaximum().toString(), persisType, employee.getEmployeeId(),
								new Second());
						if (audit2 != null)
							audits.add(audit2);

					}

				} catch (NoSuchFieldException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (SecurityException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalArgumentException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}

		return audits;
	}

	// during raising a ticket
	public List<Audit> auditTOSupportEntity(SupportTickets tickets, Long id, String tableName, String persisType) {

		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		List<Audit> audits = new ArrayList<Audit>();
		Field[] fields = tickets.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (!(field.getName().equalsIgnoreCase("id") || field.getName().equalsIgnoreCase("serialVersionUID")
					|| field.getName().equalsIgnoreCase("createdDate")
					|| field.getName().equalsIgnoreCase("updatedDate")
					|| field.getName().equalsIgnoreCase("ticketsWatchers")
					|| field.getName().equalsIgnoreCase("ticketsSubCategory")
					|| field.getName().equalsIgnoreCase("updatedBy")
					|| field.getName().equalsIgnoreCase("ticketWatchers"))) {

				try {

					Field newValueField = tickets.getClass().getDeclaredField(field.getName());
					Object currentValue = newValueField.get(tickets);
					String newValue = null;

					if (currentValue instanceof String) {

						newValue = (String) currentValue;

					} else if (currentValue instanceof Employee) {
						Employee newEmployee = (Employee) currentValue;
						newValue = newEmployee.getEmployeeId().toString();

					} else if (currentValue instanceof TicketsSubCategory) {
						TicketsSubCategory newStatus = (TicketsSubCategory) currentValue;
						newValue = newStatus.getSubCategoryName();
					} else if (currentValue instanceof Date) {
						newValue = currentValue.toString();
					} else if (currentValue instanceof Long) {
						newValue = String.valueOf(currentValue);
					} else if (currentValue instanceof Integer) {
						newValue = String.valueOf(currentValue);
					} else if (currentValue instanceof Tracker) {
						Tracker trackername = (Tracker) currentValue;
						newValue = trackername.getName();

					}
					Audit audit = componentForPOSTData(id, tableName, field.getName(), newValue, persisType,
							employee.getEmployeeId(), new Second());
					if (audit != null) {
						audits.add(audit);
					}

				} catch (NoSuchFieldException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (SecurityException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalArgumentException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				}

			}
		}
		return audits;
	}

	// during updating a ticket
	public List<Audit> UpdateAuditTOSupportEntity(SupportTickets tickets, Long id, SupportTickets oldticket,
			String tableName, String persisType) {

		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		List<Audit> audits = new ArrayList<Audit>();
		Field[] fields = tickets.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (!(field.getName().equalsIgnoreCase("id") || field.getName().equalsIgnoreCase("serialVersionUID")
					|| field.getName().equalsIgnoreCase("createdDate")
					|| field.getName().equalsIgnoreCase("updatedDate")
					|| field.getName().equalsIgnoreCase("ticketsWatchers")
					|| field.getName().equalsIgnoreCase("ticketsSubCategory")
					|| field.getName().equalsIgnoreCase("createdBy") || field.getName().equalsIgnoreCase("updatedBy")
					|| field.getName().equalsIgnoreCase("ticketWatchers"))) {

				try {
					Field oldValueField = oldticket.getClass().getDeclaredField(field.getName());
					Object existedValue = oldValueField.get(oldticket);
					String oldvalue = null;

					Field newValueField = tickets.getClass().getDeclaredField(field.getName());
					Object currentValue = newValueField.get(tickets);
					String newValue = null;

					if (currentValue instanceof String) {
						oldvalue = (String) existedValue;
						newValue = (String) currentValue;

					} else if (currentValue instanceof Employee) {
						Employee newEmployee = (Employee) currentValue;

						newValue = newEmployee != null ? newEmployee.getEmployeeId().toString() : null;

						Employee oldEmployee = (Employee) existedValue;
						oldvalue = oldEmployee != null ? oldEmployee.getEmployeeId().toString() : null;

					} /*
						 * else if (currentValue instanceof TicketsSubCategory) { TicketsSubCategory
						 * newStatus = (TicketsSubCategory) currentValue; newValue =
						 * newStatus.getSubCategoryName();
						 * 
						 * TicketsSubCategory oldStatus = (TicketsSubCategory) existedValue; oldvalue =
						 * oldStatus.getSubCategoryName(); }
						 */else if (currentValue instanceof Date) {
						newValue = currentValue != null ? currentValue.toString() : null;

						oldvalue = existedValue != null ? existedValue.toString() : null;
					} else if (currentValue instanceof Long) {
						newValue = currentValue != null ? currentValue.toString() : null;
						oldvalue = existedValue != null ? existedValue.toString() : null;
					} else if (currentValue instanceof Integer) {
						newValue = currentValue != null ? String.valueOf(currentValue) : null;
						oldvalue = existedValue != null ? String.valueOf(existedValue) : null;
					}
					Audit audit = componentForAudit(id, tableName, field.getName(), oldvalue, newValue, null,
							persisType, employee.getEmployeeId(), new Second());

					if (audit != null) {
						audits.add(audit);
					}

				} catch (NoSuchFieldException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (SecurityException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalArgumentException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				}

			}
		}
		return audits;
	}

	// during updating a SupportSubcatagory
	public List<Audit> UpdateAuditBySupportSubcatagory(TicketsSubCategory tickets, TicketsSubCategory oldticket,
			String tableName, String persisType) {

		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		List<Audit> audits = new ArrayList<Audit>();
		Field[] fields = tickets.getClass().getDeclaredFields();
		for (Field field : fields) {

			if (field.getName().equalsIgnoreCase("estimatedTime") || field.getName().equalsIgnoreCase("subCategoryName")
					|| field.getName().equalsIgnoreCase("workFlow")
					|| field.getName().equalsIgnoreCase("levelOfHierarchy")) {

				try {
					Field oldValueField = oldticket.getClass().getDeclaredField(field.getName());
					Object existedValue = oldValueField.get(oldticket);
					String oldvalue = null;

					Field newValueField = tickets.getClass().getDeclaredField(field.getName());
					Object currentValue = newValueField.get(tickets);
					String newValue = null;

					if (currentValue instanceof String) {
						oldvalue = (String) existedValue;
						newValue = (String) currentValue;

					} else if (currentValue instanceof Long) {
						newValue = currentValue != null ? currentValue.toString() : null;
						oldvalue = existedValue != null ? existedValue.toString() : null;
					} else if (currentValue instanceof Boolean) {
						newValue = currentValue != null ? String.valueOf(currentValue) : null;
						oldvalue = existedValue != null ? String.valueOf(existedValue) : null;
					} else if (currentValue instanceof Integer) {

						newValue = currentValue != null ? String.valueOf(currentValue) : null;
						oldvalue = existedValue != null ? String.valueOf(existedValue) : null;
					}
					Audit audit = componentForAudit(oldticket.getId(), tableName, field.getName(), oldvalue, newValue,
							null, persisType, employee.getEmployeeId(), new Second());

					if (audit != null) {
						audits.add(audit);
					}

				} catch (NoSuchFieldException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (SecurityException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalArgumentException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				}

			}
		}
		return audits;
	}

	public List<Audit> auditBySupportSubcatagory(TicketsSubCategory subcatagory, String tableName, String persisType) {

		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		List<Audit> audits = new ArrayList<Audit>();
		Field[] fields = subcatagory.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (!(field.getName().equalsIgnoreCase("id") || field.getName().equalsIgnoreCase("serialVersionUID")
					|| field.getName().equalsIgnoreCase("createdDate")
					|| field.getName().equalsIgnoreCase("ticketsCategory")
					|| field.getName().equalsIgnoreCase("createdBy"))) {

				try {

					Field newValueField = subcatagory.getClass().getDeclaredField(field.getName());
					Object currentValue = newValueField.get(subcatagory);
					String newValue = null;

					if (currentValue instanceof String) {

						newValue = (String) currentValue;

					} /*
						 * else if (currentValue instanceof TicketsCategory) { TicketsCategory newStatus
						 * = (TicketsCategory) currentValue; newValue = newStatus.getCategoryName(); }
						 */
					else if (currentValue instanceof Boolean) {

						newValue = currentValue.toString();
					} else if (currentValue instanceof Integer) {

						newValue = currentValue.toString();
					}
					Audit audit = componentForPOSTData(subcatagory.getId(), tableName, field.getName(), newValue,
							persisType, employee.getEmployeeId(), new Second());
					if (audit != null) {
						audits.add(audit);
					}

				} catch (NoSuchFieldException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (SecurityException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalArgumentException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				}

			}
		}
		return audits;
	}

	// during add PIP data
	public List<Audit> auditTOPIPEntity(PIP pip, Long id, String tableName, String persisType) {
		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		List<Audit> audits = new ArrayList<Audit>();
		Field[] fields = pip.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (!(field.getName().equalsIgnoreCase("id") || field.getName().equalsIgnoreCase("serialVersionUID")
					|| field.getName().equalsIgnoreCase("createdDate")
					|| field.getName().equalsIgnoreCase("updatedDate") || field.getName().equalsIgnoreCase("updatedBy")
					|| field.getName().equalsIgnoreCase("createdBy"))) {
				try {

					Field newValueField = pip.getClass().getDeclaredField(field.getName());
					Object currentValue = newValueField.get(pip);
					String newValue = null;

					if (currentValue instanceof String) {
						newValue = (String) currentValue;
					} else if (currentValue instanceof Employee) {
						Employee newEmployee = (Employee) currentValue;
						newValue = newEmployee.getEmployeeId().toString();
					} else if (currentValue instanceof Date) {
						newValue = currentValue.toString();
					} else if (currentValue instanceof Long) {
						newValue = String.valueOf(currentValue);
					} else if (currentValue instanceof Boolean) {
						newValue = String.valueOf(currentValue);
					}
					Audit audit = componentForPOSTData(id, tableName, field.getName(), newValue, persisType,
							employee.getEmployeeId(), new Second());
					if (audit != null) {
						audits.add(audit);
					}

				} catch (NoSuchFieldException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (SecurityException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalArgumentException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				}

			}
		}
		return audits;
	}

	// during updating a PIP data
	public List<Audit> UpdateAuditTOPIPEntity(PIP pip, Long id, PIP oldPip, String tableName, String persisType) {

		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		List<Audit> audits = new ArrayList<Audit>();
		Field[] fields = pip.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (!(field.getName().equalsIgnoreCase("id") || field.getName().equalsIgnoreCase("serialVersionUID")
					|| field.getName().equalsIgnoreCase("createdDate")
					|| field.getName().equalsIgnoreCase("updatedDate") || field.getName().equalsIgnoreCase("updatedBy")
					|| field.getName().equalsIgnoreCase("employee") || field.getName().equalsIgnoreCase("createdBy"))) {
				try {
					Field oldValueField = oldPip.getClass().getDeclaredField(field.getName());
					Object existedValue = oldValueField.get(oldPip);
					String oldvalue = null;

					Field newValueField = pip.getClass().getDeclaredField(field.getName());
					Object currentValue = newValueField.get(pip);
					String newValue = null;

					if (currentValue instanceof String) {
						oldvalue = (String) existedValue;
						newValue = (String) currentValue;

					} else if (currentValue instanceof Employee) {
						Employee newEmployee = (Employee) currentValue;

						newValue = newEmployee != null ? newEmployee.getEmployeeId().toString() : null;

						Employee oldEmployee = (Employee) existedValue;
						oldvalue = oldEmployee != null ? oldEmployee.getEmployeeId().toString() : null;

					} else if (currentValue instanceof Date) {
						newValue = currentValue != null ? currentValue.toString() : null;

						oldvalue = existedValue != null ? existedValue.toString() : null;
					} else if (currentValue instanceof Long) {
						newValue = currentValue != null ? currentValue.toString() : null;
						oldvalue = existedValue != null ? existedValue.toString() : null;
					} else if (currentValue instanceof Boolean) {
						newValue = currentValue != null ? String.valueOf(currentValue) : null;
						oldvalue = existedValue != null ? existedValue.toString() : null;
					}

					Audit audit = componentForAudit(id, tableName, field.getName(), oldvalue, newValue, null,
							persisType, employee.getEmployeeId(), new Second());

					if (audit != null) {
						audits.add(audit);
					}

				} catch (NoSuchFieldException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (SecurityException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalArgumentException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				}

			}
		}
		return audits;
	}

	// during add achievement data
	public List<Audit> auditToAchievementEntity(Achievement achievement, Long id, String tableName, String persisType) {
		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		List<Audit> audits = new ArrayList<Audit>();
		Field[] fields = achievement.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (!(field.getName().equalsIgnoreCase("id") || field.getName().equalsIgnoreCase("serialVersionUID")
					|| field.getName().equalsIgnoreCase("createdDate")
					|| field.getName().equalsIgnoreCase("updatedDate") || field.getName().equalsIgnoreCase("updatedBy")
					|| field.getName().equalsIgnoreCase("createdBy"))) {
				try {

					Field newValueField = achievement.getClass().getDeclaredField(field.getName());
					Object currentValue = newValueField.get(achievement);
					String newValue = null;

					if (currentValue instanceof String) {
						newValue = (String) currentValue;
					} else if (currentValue instanceof Employee) {
						Employee newEmployee = (Employee) currentValue;
						newValue = newEmployee.getEmployeeId().toString();
					} else if (currentValue instanceof Date) {
						newValue = currentValue.toString();
					} else if (currentValue instanceof Long) {
						newValue = String.valueOf(currentValue);
					} else if (currentValue instanceof Boolean) {
						newValue = String.valueOf(currentValue);
					} else if (currentValue instanceof AchievementType) {
						AchievementType newAchievementType = (AchievementType) currentValue;
						newValue = newAchievementType.getAchievementType();
					}
					Audit audit = componentForPOSTData(id, tableName, field.getName(), newValue, persisType,
							employee.getEmployeeId(), new Second());
					if (audit != null) {
						audits.add(audit);
					}

				} catch (NoSuchFieldException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (SecurityException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalArgumentException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				}

			}
		}
		return audits;
	}

	// during dash board view a Achievement data
	public List<Audit> UpdateAuditToAchievementEntity(Achievement achievement, Long id, Achievement oldAchievement,
			String tableName, String persisType) {

		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		List<Audit> audits = new ArrayList<Audit>();
		Field[] fields = achievement.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (!(field.getName().equalsIgnoreCase("id") || field.getName().equalsIgnoreCase("serialVersionUID")
					|| field.getName().equalsIgnoreCase("createdDate")
					|| field.getName().equalsIgnoreCase("updatedDate") || field.getName().equalsIgnoreCase("updatedBy")
					|| field.getName().equalsIgnoreCase("employee") || field.getName().equalsIgnoreCase("createdBy"))) {
				try {
					Field oldValueField = oldAchievement.getClass().getDeclaredField(field.getName());
					Object existedValue = oldValueField.get(oldAchievement);
					String oldvalue = null;

					Field newValueField = achievement.getClass().getDeclaredField(field.getName());
					Object currentValue = newValueField.get(achievement);
					String newValue = null;

					if (currentValue instanceof String) {
						oldvalue = (String) existedValue;
						newValue = (String) currentValue;

					} else if (currentValue instanceof Employee) {
						Employee newEmployee = (Employee) currentValue;

						newValue = newEmployee != null ? newEmployee.getEmployeeId().toString() : null;

						Employee oldEmployee = (Employee) existedValue;
						oldvalue = oldEmployee != null ? oldEmployee.getEmployeeId().toString() : null;

					} else if (currentValue instanceof Date) {
						newValue = currentValue != null ? currentValue.toString() : null;

						oldvalue = existedValue != null ? existedValue.toString() : null;
					} else if (currentValue instanceof Long) {
						newValue = currentValue != null ? currentValue.toString() : null;
						oldvalue = existedValue != null ? existedValue.toString() : null;
					} else if (currentValue instanceof Boolean) {
						newValue = currentValue != null ? String.valueOf(currentValue) : null;
						oldvalue = existedValue != null ? existedValue.toString() : null;
					} else if (currentValue instanceof AchievementType) {
						AchievementType newAchievement = (AchievementType) currentValue;

						newValue = newAchievement != null ? newAchievement.getAchievementType() : null;

						AchievementType olAchievement = (AchievementType) existedValue;
						oldvalue = olAchievement != null ? olAchievement.getAchievementType() : null;
					}

					Audit audit = componentForAudit(id, tableName, field.getName(), oldvalue, newValue, null,
							persisType, employee.getEmployeeId(), new Second());

					if (audit != null) {
						audits.add(audit);
					}

				} catch (NoSuchFieldException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (SecurityException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalArgumentException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				}

			}
		}
		return audits;
	}

	// during updating a Employee
	public List<Audit> UpdateAuditTOEmployeeEntity(Employee employee, Long id, Employee oldEmployee, String tableName,
			String persisType) {
		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee persistEmployee = dao.findBy(Employee.class, loggedInEmpId);
		List<Audit> audits = new ArrayList<Audit>();
		logger.warn("in UpdateAuditTOEmployeeEntity");
		Field[] fields = employee.getClass().getDeclaredFields();
		for (Field field : fields) {


			if (field.getName().equalsIgnoreCase("firstName") || field.getName().equalsIgnoreCase("lastName")
					|| field.getName().equalsIgnoreCase("manager") || field.getName().equalsIgnoreCase("hrAssociate")
					|| field.getName().equalsIgnoreCase("middleName") || field.getName().equalsIgnoreCase("dob")
					|| field.getName().equalsIgnoreCase("maritalStatus")
					|| field.getName().equalsIgnoreCase("marriageDate")
					|| field.getName().equalsIgnoreCase("currentLocation") || field.getName().equalsIgnoreCase("gender")
					|| field.getName().equalsIgnoreCase("aboutMe")
					|| field.getName().equalsIgnoreCase("employmentTypeName")
					|| field.getName().equalsIgnoreCase("jobTypeName") || field.getName().equalsIgnoreCase("realDob")
					|| field.getName().equalsIgnoreCase("baseLocation")
					|| field.getName().equalsIgnoreCase("departmentName") || field.getName().equalsIgnoreCase("phone")
					|| field.getName().equalsIgnoreCase("alternativeMobile")
					|| field.getName().equalsIgnoreCase("homePhoneNumber")
					|| field.getName().equalsIgnoreCase("officePhoneNumber")
					|| field.getName().equalsIgnoreCase("homeCode") || field.getName().equalsIgnoreCase("workCode")
					|| field.getName().equalsIgnoreCase("emergencyContactName")
					|| field.getName().equalsIgnoreCase("emergencyPhone")
					|| field.getName().equalsIgnoreCase("emergencyRelationShip")
					|| field.getName().equalsIgnoreCase("presentAddress")
					|| field.getName().equalsIgnoreCase("presentCity") || field.getName().equalsIgnoreCase("presentZip")
					|| field.getName().equalsIgnoreCase("presentLandMark")
					|| field.getName().equalsIgnoreCase("permanentAddress")
					|| field.getName().equalsIgnoreCase("permanentCity")
					|| field.getName().equalsIgnoreCase("permanentZip")
					|| field.getName().equalsIgnoreCase("permanentLandMark")
					|| field.getName().equalsIgnoreCase("passportNumber")
					|| field.getName().equalsIgnoreCase("passportExpDate")
					|| field.getName().equalsIgnoreCase("passportIssuedPlace")
					|| field.getName().equalsIgnoreCase("passportIssuedDate")
					|| field.getName().equalsIgnoreCase("employeeFullName")
					|| field.getName().equalsIgnoreCase("personName")
					|| field.getName().equalsIgnoreCase("relationShip")
					|| field.getName().equalsIgnoreCase("contactNumber")
					|| field.getName().equalsIgnoreCase("dateOfBirth") || field.getName().equalsIgnoreCase("bloodgroup")
					|| field.getName().equalsIgnoreCase("skypeId")

					|| field.getName().equalsIgnoreCase("timeSlot") || field.getName().equalsIgnoreCase("empRole")
					|| field.getName().equalsIgnoreCase("role") || field.getName().equalsIgnoreCase("designation")
					|| field.getName().equalsIgnoreCase("releavingDate")
					|| field.getName().equalsIgnoreCase("underNoticeDate")
					|| field.getName().equalsIgnoreCase("underNotice")
					|| field.getName().equalsIgnoreCase("statusName")
					|| field.getName().equalsIgnoreCase("profilePicPath")
					|| field.getName().equalsIgnoreCase("thumbPicture")
					|| field.getName().equalsIgnoreCase("profilePicture")
					|| field.getName().equalsIgnoreCase("comments")) {

				try {
					Field oldValueField = oldEmployee.getClass().getDeclaredField(field.getName());
					Object existedValue = oldValueField.get(oldEmployee);
					String oldvalue = null;

					Field newValueField = employee.getClass().getDeclaredField(field.getName());
					Object currentValue = newValueField.get(employee);
					String newValue = null;

					if (currentValue instanceof String) {
						oldvalue = (String) existedValue;
						newValue = (String) currentValue;

					} else if (currentValue instanceof Employee) {
						Employee newEmployee = (Employee) currentValue;

						newValue = newEmployee != null ? newEmployee.getFullName() : null;

						Employee oldEmp = (Employee) existedValue;
						oldvalue = oldEmp != null ? oldEmp.getFullName() : null;

					} else if (currentValue instanceof Date) {
						newValue = currentValue != null ? currentValue.toString() : null;

						oldvalue = existedValue != null ? existedValue.toString() : null;
					} else if (currentValue instanceof Long) {
						newValue = currentValue != null ? currentValue.toString() : null;
						oldvalue = existedValue != null ? existedValue.toString() : null;
					} else if (currentValue instanceof Integer) {
						newValue = currentValue != null ? String.valueOf(currentValue) : null;
						oldvalue = existedValue != null ? String.valueOf(existedValue) : null;
					} else if (currentValue instanceof Boolean) {
						newValue = currentValue != null ? String.valueOf(currentValue) : null;
						oldvalue = existedValue != null ? String.valueOf(existedValue) : null;
					} else if (currentValue instanceof TimeSlot) {
						TimeSlot newslot = (TimeSlot) currentValue;
						newValue = currentValue != null ? newslot.getName() : null;
						TimeSlot oldslot = (TimeSlot) existedValue;
						oldvalue = existedValue != null ? oldslot.getName() : null;
					} else if (currentValue instanceof Role) {
						Role role = (Role) currentValue;
						newValue = currentValue != null ? role.getName() : null;
						Role oldRole = (Role) existedValue;
						oldvalue = existedValue != null ? oldRole.getName() : null;
					}

					Audit audit = componentForAudit(id, tableName, field.getName(), oldvalue, newValue, null,
							persisType, persistEmployee.getEmployeeId(), new Second());

					if (audit != null) {
						audits.add(audit);
					}

				} catch (NoSuchFieldException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (SecurityException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalArgumentException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				}

			}
		}
		return audits;
	}

	// during adding family details
	public List<Audit> AuditTOEmployeeFamilyEntity(EmployeeFamilyInformation employeeFamilyInformation, Long id,
			String tableName, String persisType) {

		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		List<Audit> audits = new ArrayList<Audit>();
		Field[] fields = employeeFamilyInformation.getClass().getDeclaredFields();
		for (Field field : fields) {
			if ((field.getName().equalsIgnoreCase("personName") || field.getName().equalsIgnoreCase("relationShip")
					|| field.getName().equalsIgnoreCase("contactNumber")
					|| field.getName().equalsIgnoreCase("dateOfBirth"))) {

				try {

					Field newValueField = employeeFamilyInformation.getClass().getDeclaredField(field.getName());
					Object currentValue = newValueField.get(employeeFamilyInformation);
					String newValue = null;

					if (currentValue instanceof String) {

						newValue = (String) currentValue;

					} else if (currentValue instanceof Employee) {
						Employee newEmployee = (Employee) currentValue;
						newValue = newEmployee.getEmployeeId().toString();

					} else if (currentValue instanceof Date) {
						newValue = currentValue.toString();
					} else if (currentValue instanceof Long) {
						newValue = String.valueOf(currentValue);
					} else if (currentValue instanceof Integer) {
						newValue = String.valueOf(currentValue);
					}

					Audit audit = componentForPOSTData(id, tableName, field.getName(), newValue, persisType,
							employee.getEmployeeId(), new Second());
					if (audit != null) {
						audits.add(audit);
					}

				} catch (NoSuchFieldException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (SecurityException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalArgumentException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				}

			}
		}
		return audits;
	}

	// during updating Employee family detail
	public List<Audit> UpdateAuditTOEmployeeFamilyEntity(EmployeeFamilyInformation employeeFamilyInformation, Long id,
			EmployeeFamilyInformation oldEmployeeFamilyInformation, String tableName, String persisType) {

		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		List<Audit> audits = new ArrayList<Audit>();
		Field[] fields = employeeFamilyInformation.getClass().getDeclaredFields();
		for (Field field : fields) {
			if ((field.getName().equalsIgnoreCase("personName") || field.getName().equalsIgnoreCase("relationShip")
					|| field.getName().equalsIgnoreCase("contactNumber")
					|| field.getName().equalsIgnoreCase("dateOfBirth"))) {
				try {
					Field oldValueField = oldEmployeeFamilyInformation.getClass().getDeclaredField(field.getName());
					Object existedValue = oldValueField.get(oldEmployeeFamilyInformation);
					String oldvalue = null;

					Field newValueField = employeeFamilyInformation.getClass().getDeclaredField(field.getName());
					Object currentValue = newValueField.get(employeeFamilyInformation);
					String newValue = null;

					if (currentValue instanceof String) {
						oldvalue = (String) existedValue;
						newValue = (String) currentValue;

					} else if (currentValue instanceof Employee) {
						Employee newEmployee = (Employee) currentValue;

						newValue = newEmployee != null ? newEmployee.getEmployeeId().toString() : null;

						Employee oldEmployee = (Employee) existedValue;
						oldvalue = oldEmployee != null ? oldEmployee.getEmployeeId().toString() : null;

					} else if (currentValue instanceof Date) {
						newValue = currentValue != null ? currentValue.toString() : null;

						oldvalue = existedValue != null ? existedValue.toString() : null;
					} else if (currentValue instanceof Long) {
						newValue = currentValue != null ? currentValue.toString() : null;
						oldvalue = existedValue != null ? existedValue.toString() : null;
					}
					Audit audit = componentForAudit(id, tableName, field.getName(), oldvalue, newValue, null,
							persisType, employee.getEmployeeId(), new Second());

					if (audit != null) {
						audits.add(audit);
					}

				} catch (NoSuchFieldException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (SecurityException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalArgumentException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				}

			}
		}
		return audits;
	}

	public Audit updateProjectRequestAduit(ProjectRequest projectRequest, Long id, ProjectRequest oldProjectRequest,
			String tableName, String persisType) {
		Audit audit = null;
		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);

		Field[] fields = projectRequest.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			if ((field.getName().equalsIgnoreCase("comment")) || (field.getName().equalsIgnoreCase(""))) {
				try {
					Field oldValueField = oldProjectRequest.getClass().getDeclaredField(field.getName());
					Object existedValue = oldValueField.get(oldProjectRequest);
					String oldvalue = null;

					Field newValueField = projectRequest.getClass().getDeclaredField(field.getName());
					Object currentValue = newValueField.get(projectRequest);
					String newValue = null;

					if (currentValue instanceof String) {
						oldvalue = (String) existedValue;
						newValue = (String) currentValue;

					}
					audit = componentForAudit(id, tableName, field.getName(), oldvalue, newValue, null, persisType,
							employee.getEmployeeId(), new Second());
				} catch (NoSuchFieldException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (SecurityException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalArgumentException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				}

			}
		}
		return audit;
	}
	
	public List<Audit> createAuditToJobvacancy(JobVacancy jobvacancy,Long id,String tablename,String persistType)
	{

		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		List<Audit> audits = new ArrayList<Audit>();
		Field[] fields = jobvacancy.getClass().getDeclaredFields();
		for (Field field : fields) {
				if (!field.isAccessible()) {
					
					field.setAccessible(true);
				}
			if (!(field.getName().equalsIgnoreCase("jobVacancyId") || field.getName().equalsIgnoreCase("serialVersionUID")
					)) {
				
				try {

					Field newValueField = jobvacancy.getClass().getDeclaredField(field.getName());
					Object currentValue = newValueField.get(jobvacancy);
					String newValue = null;

					if (currentValue instanceof String) {
						newValue = (String) currentValue;

					}  else if (currentValue instanceof Date) {
						newValue = currentValue.toString();
					} else if (currentValue instanceof Long) {
						newValue = String.valueOf(currentValue);
					} else if (currentValue instanceof Integer) {
						newValue = String.valueOf(currentValue);
					}
					else if (currentValue instanceof Boolean) {
						newValue = String.valueOf(currentValue);
					}
					Audit audit = componentForPOSTData(id, tablename, field.getName(), newValue, persistType,
							employee.getEmployeeId(), new Second());
					if (audit != null) {
						audits.add(audit);
					}

				} catch (NoSuchFieldException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (SecurityException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalArgumentException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				}

			}
		}
		return audits;
	}
	
	public List<Audit> updateAuditToJobVacancy(JobVacancy jobvacancy,Long jobvacancyId,JobVacancy oldJobVacancy,String tableName,String persistype)
	{
		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		List<Audit> audits = new ArrayList<Audit>();
		Field[] fields = jobvacancy.getClass().getDeclaredFields();
		for (Field field : fields) {
				if (!field.isAccessible()) {
					
					field.setAccessible(true);
				}
			if (!(field.getName().equalsIgnoreCase("jobVacancyId") || field.getName().equalsIgnoreCase("serialVersionUID")
					)) {
				try {
					Field oldValueField = oldJobVacancy.getClass().getDeclaredField(field.getName());
					Object existedValue = oldValueField.get(oldJobVacancy);
					String oldvalue = null;

					Field newValueField = jobvacancy.getClass().getDeclaredField(field.getName());
					Object currentValue = newValueField.get(jobvacancy);
					String newValue = null;

					if (currentValue instanceof String) {
						oldvalue = (String) existedValue;
						newValue = (String) currentValue;

					} else if (currentValue instanceof Long) {
						newValue = currentValue != null ? currentValue.toString() : null;
						oldvalue = existedValue != null ? existedValue.toString() : null;
					} else if (currentValue instanceof Boolean) {
						newValue = currentValue != null ? String.valueOf(currentValue) : null;
						oldvalue = existedValue != null ? String.valueOf(existedValue) : null;
					} 
					else if (currentValue instanceof Date) {
						newValue = currentValue != null ? currentValue.toString() : null;

						oldvalue = existedValue != null ? existedValue.toString() : null;
					}
					else if (currentValue instanceof Integer) {

						newValue = currentValue != null ? String.valueOf(currentValue) : null;
						oldvalue = existedValue != null ? String.valueOf(existedValue) : null;
					}
					Audit audit = componentForAudit(jobvacancyId, tableName, field.getName(), oldvalue, newValue,
							null, persistype, employee.getEmployeeId(), new Second());

					if (audit != null) {
						audits.add(audit);
					}

				} catch (NoSuchFieldException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (SecurityException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalArgumentException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				}

			}
		}
		return audits;
	}
	
	
	//New Sqa Audit
	
	public List<Audit> updateAuditToSQAAudit(SQAAuditForm form,Long formId,SQAAuditForm oldForm,String tableName,String persistype)
	{
		
		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		
		List<Audit> audits = new ArrayList<Audit>();
		Field[] fields = form.getClass().getDeclaredFields();
		
		for (Field field : fields) {
				if (!field.isAccessible()) {
					
					field.setAccessible(true);
					
				}
			if (!(field.getName().equalsIgnoreCase("id")  || field.getName().equalsIgnoreCase("serialVersionUID")))
			{
				try {
					Field oldValueField = oldForm.getClass().getDeclaredField(field.getName());
					
					Object existedValue = oldValueField.get(oldForm);
					String oldvalue = null;
					

					Field newValueField = form.getClass().getDeclaredField(field.getName());
					Object currentValue = newValueField.get(form);
					String newValue = null;
				
					if (currentValue instanceof String) {
						//logger.warn("New Value for String "+ currentValue.toString());
						//logger.warn("Old Value for String"+ existedValue.toString());
						if(existedValue != null){
							oldvalue = (String) existedValue;
						}
						newValue = (String) currentValue;

					} else if (currentValue instanceof Long) {
						newValue = currentValue != null ? currentValue.toString() : null;
						oldvalue = existedValue != null ? existedValue.toString() : null;
					} else if (currentValue instanceof Boolean) {
						newValue = currentValue != null ? String.valueOf(currentValue) : null;
						oldvalue = existedValue != null ? String.valueOf(existedValue) : null;
					} 
					else if (currentValue instanceof Date) {
						newValue = currentValue != null ? currentValue.toString() : null;

						oldvalue = existedValue != null ? existedValue.toString() : null;
					}
					else if (currentValue instanceof Integer) {

						newValue = currentValue != null ? String.valueOf(currentValue) : null;
						oldvalue = existedValue != null ? String.valueOf(existedValue) : null;
					}
					else if (currentValue instanceof Second) {
						newValue = currentValue != null ? ((Second) currentValue).toString("hh:mm a") : null;
						
						oldvalue = existedValue != null ? ((Second) existedValue).toString("hh:mm a") : null;
						
					}
					
					else if(currentValue instanceof Employee) {
						Employee newEmp =  currentValue !=null ? (Employee)currentValue : null;
						newValue = newEmp != null ? newEmp.getEmployeeFullName() : null;
						Employee oldEmp =  existedValue !=null ? (Employee)existedValue : null;
						oldvalue = oldEmp != null ? oldEmp.getEmployeeFullName() : null;
						
					}
					else if(currentValue instanceof Set<?>){
						if(field.getName().equalsIgnoreCase("auditors")){
								Set<SQAAuditors> auditorsName = currentValue !=null ? (Set<SQAAuditors>) currentValue : null;
								ArrayList<SQAAuditors> name = new ArrayList<SQAAuditors>(auditorsName);
								Collections.sort(name, new AuditorsComparator());
								if (name != null) {
									Boolean flag = true;
									for (SQAAuditors auditor : name) {
										if (flag) {
											newValue = auditor.getAuditor().getEmployeeFullName();
											flag = false;
										} else {
											newValue = newValue + " , " + auditor.getAuditor().getEmployeeFullName();
										}
									}
								}
							
								Set<SQAAuditors> oldAuditorsName =existedValue != null ?  (Set<SQAAuditors>)  existedValue : null;
								ArrayList<SQAAuditors> name1 = new ArrayList<SQAAuditors>(oldAuditorsName);
								Collections.sort(name1, new AuditorsComparator());
								if (name1 != null) {
									Boolean flag = true;
									for (SQAAuditors auditor : name1) {
										if (flag) {
											oldvalue = auditor.getAuditor().getEmployeeFullName();
											flag = false;
										} else {
											oldvalue = oldvalue + " , " + auditor.getAuditor().getEmployeeFullName();
										}
									}

								}
							
							
						}
						else if(field.getName().equalsIgnoreCase("auditees")){
								Set<SQAAuditees> auditeesName =  currentValue != null ? (Set<SQAAuditees>)currentValue : null;
								ArrayList<SQAAuditees> name1 = new ArrayList<SQAAuditees>(auditeesName);
								Collections.sort(name1, new AuditeesComparator());
								if (name1 != null) {
									Boolean flag = true;
									for (SQAAuditees auditor : name1) {
										if (flag) {
											newValue = auditor.getAuditee().getEmployeeFullName();
											flag = false;
										} else {
											newValue = newValue + " , " + auditor.getAuditee().getEmployeeFullName();
										}
									}

								}
							
								Set<SQAAuditees> oldAuditeesName = existedValue != null ? (Set<SQAAuditees>)  existedValue : null;
								ArrayList<SQAAuditees> name = new ArrayList<SQAAuditees>(oldAuditeesName);
								Collections.sort(name, new AuditeesComparator());
								if(name != null){
									Boolean flag = true;
									for (SQAAuditees auditor : name) {
										if (flag) {
											oldvalue = auditor.getAuditee().getEmployeeFullName();
											flag = false;
										} else {
											oldvalue = oldvalue + " , " + auditor.getAuditee().getEmployeeFullName();
										}
									}
								}
								
						}
					}
					
					Audit audit = componentForAudit(formId, tableName, field.getName(), oldvalue, newValue,
							null, persistype, employee.getEmployeeId(), new Second());

					if (audit != null) {
						audits.add(audit);
					}

				} catch (NoSuchFieldException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (SecurityException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalArgumentException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					//java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
					ex.printStackTrace();
				}

			}
		}
		//logger.warn("Return Audit from updateAuditToSQAAudit() method"+ audits);
		return audits;
	}
	
	
public List<Audit> createAuditToSQAAudit(SQAAuditForm form,Long id,String tablename,String persistType){
		
		//logger.warn("In CreateAuditToSQAAudit()");
		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		List<Audit> audits = new ArrayList<Audit>();
		Field[] fields = form.getClass().getDeclaredFields();
		//logger.warn("Audities List "+audits);
		for (Field field : fields) {
				if (!field.isAccessible()) {
					//logger.warn("not True");
					field.setAccessible(true);
					//logger.warn("After True");
				}
			if (!(field.getName().equalsIgnoreCase("id")|| field.getName().equalsIgnoreCase("serialVersionUID") )) {
				
				try {

					Field newValueField = form.getClass().getDeclaredField(field.getName());
					//logger.warn("NewvalueField "+newValueField);
					Object currentValue = newValueField.get(form);
					//logger.warn("Current Value is "+ currentValue);
					String newValue = null;

					if (currentValue instanceof String) {
						newValue = (String) currentValue;

					}  else if (currentValue instanceof Date) {
						newValue = currentValue.toString();
					} else if (currentValue instanceof Long) {
						newValue = String.valueOf(currentValue);
					} else if (currentValue instanceof Integer) {
						newValue = String.valueOf(currentValue);
					}
					else if (currentValue instanceof Boolean) {
						newValue = String.valueOf(currentValue);
					}
					
					else if (currentValue instanceof Second) {
						newValue = ((Second) currentValue).toString("hh:mm a");
					}
					
					else if(currentValue instanceof Employee){
						Employee emp =(Employee) currentValue;
						newValue = emp.getEmployeeFullName();
					}
					
					else if(currentValue instanceof Set<?>){
						if(field.getName().equalsIgnoreCase("auditors")){
							
							Set<SQAAuditors> auditorsName = (Set<SQAAuditors>) currentValue;
							if (auditorsName != null) {
								Boolean flag = true;
								for (SQAAuditors auditor : auditorsName) {
									if (flag) {
										newValue = auditor.getAuditor().getEmployeeFullName();
										flag = false;
									} else {
										newValue = newValue + " , " + auditor.getAuditor().getEmployeeFullName();
									}
								}

							}
						}
						if(field.getName().equalsIgnoreCase("auditees")){
							
							Set<SQAAuditees> auditeesName = (Set<SQAAuditees>) currentValue;
							if (auditeesName != null) {
								Boolean flag = true;
								for (SQAAuditees auditor : auditeesName) {
									if (flag) {
										newValue = auditor.getAuditee().getEmployeeFullName();
										flag = false;
									} else {
										newValue = newValue + " , " + auditor.getAuditee().getEmployeeFullName();
									}
								}

							}
						}
					}
					
					Audit audit = componentForPOSTData(id, tablename, field.getName(), newValue, persistType,
							employee.getEmployeeId(), new Second());
					if (audit != null) {
						//logger.warn("Audit is not null");
						audits.add(audit);
					}

				} catch (NoSuchFieldException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (SecurityException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalArgumentException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					java.util.logging.Logger.getLogger(AuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
				}

			}
		}
		
		//logger.warn("return audit is "+ audits);
		return audits;
		
	}
	
	
	
}
