package com.raybiztech.appraisals.dto;

import java.io.Serializable;
import java.util.Set;

public class DesignationKrasDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6813331740956337221L;
	private Long id;
	private String designationCode;
	private String designationName;
    private String comments;
	private Set<KraWithWeightageDTO> krasWithWeitage;

	public DesignationKrasDTO() {
	}

	public DesignationKrasDTO(String designationCode, String designationName) {
		this.designationCode = designationCode;
		this.designationName = designationName;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignationCode() {
        return designationCode;
    }

    public void setDesignationCode(String designationCode) {
        this.designationCode = designationCode;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Set<KraWithWeightageDTO> getKrasWithWeitage() {
        return krasWithWeitage;
    }

    public void setKrasWithWeitage(Set<KraWithWeightageDTO> krasWithWeitage) {
        this.krasWithWeitage = krasWithWeitage;
    }

}
