package com.raybiztech.appraisals.business;

import java.io.Serializable;

public class DesignationKras implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Long designationKRAsId;
    private String designationCode;
    private String designationName;
    private String comments;
  /*  private Set<KraWithWeightage> krasWithWeitage;*/

    public DesignationKras() {

    }

    public DesignationKras(String designationCode, String designationName) {
        this.designationCode = designationCode;
        this.designationName = designationName;
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

    public Long getDesignationKRAsId() {
		return designationKRAsId;
	}

	public void setDesignationKRAsId(Long designationKRAsId) {
		this.designationKRAsId = designationKRAsId;
	}

/*	public Set<KraWithWeightage> getKrasWithWeitage() {
        return krasWithWeitage;
    }

    public void setKrasWithWeitage(Set<KraWithWeightage> krasWithWeitage) {
        this.krasWithWeitage = krasWithWeitage;
    }*/

}
