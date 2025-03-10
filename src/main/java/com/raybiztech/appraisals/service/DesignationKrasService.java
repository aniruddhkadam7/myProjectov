package com.raybiztech.appraisals.service;

import java.util.List;

import com.raybiztech.appraisals.dto.KRADTO;

public interface DesignationKrasService {
    List<String> getAllDesignations();

    List<KRADTO> getAllKRAsWithWeightage(String designationName);
}
