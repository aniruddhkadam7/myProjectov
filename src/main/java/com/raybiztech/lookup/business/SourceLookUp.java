package com.raybiztech.lookup.business;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class SourceLookUp implements Serializable {

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

    @Override
    public int hashCode() {
        return new HashCodeBuilder(1997, 13).append(SourceLookUpId).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof SourceLookUp) {
            SourceLookUp sourceLookUp = (SourceLookUp) obj;
            return new EqualsBuilder()
                    .append(SourceLookUpId, sourceLookUp.getSourceLookUpId()).isEquals();
        }
        return false;
    }
}
