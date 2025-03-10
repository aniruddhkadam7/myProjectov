package com.raybiztech.recruitment.business;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.raybiztech.appraisals.business.Employee;

public class Panel implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8408877456338601638L;

    private Long panelId;
    private Employee employee;
    private String deptId;
    private PanelLevel panelLevel;
    private String description;
    private String practice;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Panel() {
    }

    public Long getPanelId() {
        return panelId;
    }

    public void setPanelId(Long panelId) {
        this.panelId = panelId;
    }

    public PanelLevel getPanelLevel() {
        return panelLevel;
    }

    public void setPanelLevel(PanelLevel panelLevel) {
        this.panelLevel = panelLevel;
    }

    public String getPractice() {
        return practice;
    }

    public void setPractice(String practice) {
        this.practice = practice;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Panel) {
            Panel panel = (Panel) obj;
            return new EqualsBuilder().append(panelId, panel.getPanelId()).isEquals();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 37).append(panelId).toHashCode();
    }
}
