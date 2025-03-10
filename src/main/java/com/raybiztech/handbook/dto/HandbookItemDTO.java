package com.raybiztech.handbook.dto;

import java.io.Serializable;
import java.util.List;

import com.raybiztech.projectmanagement.invoice.dto.CountryLookUpDTO;

public class HandbookItemDTO implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int id;
    private int displayOrder;
    private String title;
    private String description;
    private String pageName;
    private String departmentName;
    private Long departmentId;
    private String type;
    private Long sectionId;
	private String sectionName;
	private String country;
	private List<CountryLookUpDTO> handCountry;
	private String empCountry;
	
	
	
	public List<CountryLookUpDTO> getHandCountry() {
		return handCountry;
	}

	public void setHandCountry(List<CountryLookUpDTO> dtoList) {
		this.handCountry = dtoList;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getSectionId() {
		return sectionId;
	}

	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getEmpCountry() {
		return empCountry;
	}

	public void setEmpCountry(String empCountry) {
		this.empCountry = empCountry;
	}

	
    
}
