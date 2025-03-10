/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.projectmanagement.business;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.DateRange;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 *
 * @author anil
 */
public class ProjectRequest implements Serializable,Cloneable {

    Logger logger = Logger.getLogger(ProjectRequest.class);
    private static final long serialVersionUID = 1L;
    private Long id;
    private String projectName;
    private Employee projectManager;
    private DateRange period;
    private String description;
    private String status;
    private Client client;
    private ProjectType type;
    private String requiredResources;
    private String newClient;
    private Employee requestedBy;
    private String technology;
    private String organization;
    private String personName;
    private String email;
    private Country country;
    private String address;
    private Boolean intrnalOrNot;
    private String cc;
	private String bcc;
	private Set<ProjectInitiationChecklist> checklist;
	public String comment;
	private Set<ProjectRequestMilestone> projectRequestMilestone;
	private ProjectModel model;
	private String projectContactPerson;
	private String projectContactEmail;
	private String billingContactPerson;
	private String billingContactPersonEmail;
	private String platform;
	private String domain;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Employee getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(Employee projectManager) {
        this.projectManager = projectManager;
    }

    public DateRange getPeriod() {
        return period;
    }

    public void setPeriod(DateRange period) {
        this.period = period;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ProjectType getType() {
        return type;
    }

    public void setType(ProjectType type) {
        this.type = type;
    }

    public String getRequiredResources() {
        return requiredResources;
    }

    public void setRequiredResources(String requiredResources) {
        this.requiredResources = requiredResources;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNewClient() {
        return newClient;
    }

    public void setNewClient(String newClient) {
        this.newClient = newClient;
    }

    public Employee getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(Employee requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public Boolean getIntrnalOrNot() {
		return intrnalOrNot;
	}

	public void setIntrnalOrNot(Boolean intrnalOrNot) {
		this.intrnalOrNot = intrnalOrNot;
	}

    public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	
	public Set<ProjectInitiationChecklist> getChecklist() {
		return checklist;
	}

	public void setChecklist(Set<ProjectInitiationChecklist> checklist) {
		this.checklist = checklist;
	}

	public ProjectModel getModel() {
		return model;
	}

	public void setModel(ProjectModel model) {
		this.model = model;
	}
	

	public String getProjectContactPerson() {
		return projectContactPerson;
	}

	public void setProjectContactPerson(String projectContactPerson) {
		this.projectContactPerson = projectContactPerson;
	}

	public String getProjectContactEmail() {
		return projectContactEmail;
	}

	public void setProjectContactEmail(String projectContactEmail) {
		this.projectContactEmail = projectContactEmail;
	}

	public String getBillingContactPerson() {
		return billingContactPerson;
	}

	public void setBillingContactPerson(String billingContactPerson) {
		this.billingContactPerson = billingContactPerson;
	}

	public String getBillingContactPersonEmail() {
		return billingContactPersonEmail;
	}

	public void setBillingContactPersonEmail(String billingContactPersonEmail) {
		this.billingContactPersonEmail = billingContactPersonEmail;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.projectName);
        hash = 89 * hash + Objects.hashCode(this.projectManager);
        hash = 89 * hash + Objects.hashCode(this.period);
        hash = 89 * hash + Objects.hashCode(this.description);
        hash = 89 * hash + Objects.hashCode(this.status);
        hash = 89 * hash + Objects.hashCode(this.client);
        hash = 89 * hash + Objects.hashCode(this.type);
        hash = 89 * hash + Objects.hashCode(this.requiredResources);
        hash = 89 * hash + Objects.hashCode(this.newClient);
        hash = 89 * hash + Objects.hashCode(this.requestedBy);
        hash = 89 * hash + Objects.hashCode(this.technology);
        hash = 89 * hash + Objects.hashCode(this.organization);
        hash = 89 * hash + Objects.hashCode(this.personName);
        hash = 89 * hash + Objects.hashCode(this.email);
        hash = 89 * hash + Objects.hashCode(this.country);
        hash = 89 * hash + Objects.hashCode(this.address);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProjectRequest other = (ProjectRequest) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.projectName, other.projectName)) {
            return false;
        }
        if (!Objects.equals(this.projectManager, other.projectManager)) {
            return false;
        }
        if (!Objects.equals(this.period, other.period)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        if (!Objects.equals(this.client, other.client)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if (!Objects.equals(this.requiredResources, other.requiredResources)) {
            return false;
        }
        if (!Objects.equals(this.newClient, other.newClient)) {
            return false;
        }
        if (!Objects.equals(this.requestedBy, other.requestedBy)) {
            return false;
        }
        if (!Objects.equals(this.technology, other.technology)) {
            return false;
        }
        if (!Objects.equals(this.organization, other.organization)) {
            return false;
        }
        if (!Objects.equals(this.personName, other.personName)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.country, other.country)) {
            return false;
        }
        if (!Objects.equals(this.address, other.address)) {
            return false;
        }
        return true;
    }

	@Override
    public String toString() {
        return "ProjectRequest{" + "id=" + id + ", projectName=" + projectName + ", projectManager=" + projectManager + ", period=" + period + ", description=" + description + ", status=" + status + ", client=" + client + ", type=" + type + ", requiredResources=" + requiredResources + ", newClient=" + newClient + ", requestedBy=" + requestedBy + ", technology=" + technology + ", organization=" + organization + ", personName=" + personName + ", email=" + email + ", country=" + country + ", address=" + address + '}';
    }

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	// TODO Auto-generated method stub
	return super.clone();
}
	public Set<ProjectRequestMilestone> getProjectRequestMilestone() {
		return projectRequestMilestone;
	}

	public void setProjectRequestMilestone(Set<ProjectRequestMilestone> projectRequestMilestone) {
		this.projectRequestMilestone = projectRequestMilestone;
	}

	
    
    
    
}
