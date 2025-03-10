package com.raybiztech.appraisals.business;

import java.io.Serializable;

public class KraWithWeightage implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public KraWithWeightage(){
        
    }
    
    public KraWithWeightage(Long kraWithWeightageId, DesignationKras designation, KRA kra, Double weightage){
        this.kraWithWeightageId = kraWithWeightageId;
        this.designation = designation;
        this.kra = kra;
        this.weightage = weightage;
    }
    
    private Long kraWithWeightageId;
    
    private DesignationKras designation;
    
    private KRA kra;
    
    private Double weightage;

    public Long getKraWithWeightageId() {
		return kraWithWeightageId;
	}

	public void setKraWithWeightageId(Long kraWithWeightageId) {
		this.kraWithWeightageId = kraWithWeightageId;
	}

	public KRA getKra() {
        return kra;
    }

    public void setKra(KRA kra) {
        this.kra = kra;
    }

    public DesignationKras getDesignation() {
        return designation;
    }

    public void setDesignation(DesignationKras designation) {
        this.designation = designation;
    }

    public Double getWeightage() {
        return weightage;
    }

    public void setWeightage(Double weightage) {
        this.weightage = weightage;
    }
    
}
