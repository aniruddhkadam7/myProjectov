package com.raybiztech.appraisals.dto;

import java.io.Serializable;

public class KraWithWeightageDTO implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public KraWithWeightageDTO() {
    }

    public KraWithWeightageDTO(KRADTO kradto, Double weightage) {
        this.kradto = kradto;
        this.weightage = weightage;
    }
    
    private Long id;
    
    private KRADTO kradto;

    private Double weightage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public KRADTO getKradto() {
        return kradto;
    }

    public void setKradto(KRADTO kradto) {
        this.kradto = kradto;
    }

    public Double getWeightage() {
        return weightage;
    }

    public void setWeightage(Double weightage) {
        this.weightage = weightage;
    }
    

}
