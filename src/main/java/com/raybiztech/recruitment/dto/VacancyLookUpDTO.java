package com.raybiztech.recruitment.dto;

import java.io.Serializable;

public class VacancyLookUpDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4242547560118218896L;
    private Long VacancyLookUpId;
    private String name;
    private String jobCode;

    public Long getVacancyLookUpId() {
        return VacancyLookUpId;
    }

    public void setVacancyLookUpId(Long vacancyLookUpId) {
        VacancyLookUpId = vacancyLookUpId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }
    
}
