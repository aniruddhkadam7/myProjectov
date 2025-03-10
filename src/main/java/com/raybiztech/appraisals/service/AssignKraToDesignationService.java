package com.raybiztech.appraisals.service;

import com.raybiztech.appraisals.dto.KRADTO;

public interface AssignKraToDesignationService {

    void assignKras(String designationName,KRADTO[] krasWithWeightage);
    
}