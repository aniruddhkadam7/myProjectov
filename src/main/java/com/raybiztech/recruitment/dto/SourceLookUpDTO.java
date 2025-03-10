package com.raybiztech.recruitment.dto;

import java.io.Serializable;

public class SourceLookUpDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6624745838254413366L;
    private Long SourceLookUpId;

    private String sourceName;
    private String sourceType;
    private Integer displayOrder;

    public Long getSourceLookUpId() {
        return SourceLookUpId;

    }

    public void setSourceLookUpId(Long sourceLookUpId) {
        SourceLookUpId = sourceLookUpId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
