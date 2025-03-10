package com.raybiztech.recruitment.service;

import java.util.Comparator;

import com.raybiztech.recruitment.business.JobVacancy;

public class VacancyListComparator implements Comparator<JobVacancy> {

    @Override
    public int compare(JobVacancy o1, JobVacancy o2) {
        return o2.getJobVacancyId().compareTo(o1.getJobVacancyId());
    }
}
