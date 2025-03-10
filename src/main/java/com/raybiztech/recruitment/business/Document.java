package com.raybiztech.recruitment.business;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Document implements Serializable {

    private static final long serialVersionUID = -2459340254939779958L;

    private Long documentId;
    
    private Candidate candidateID;

    private DocType docType;

    private String doctokenId;

    private String description;

    
    public String getDoctokenId() {
        return doctokenId;
    }

    public String getDescription() {
        return description;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public void setDoctokenId(String doctokenId) {
        this.doctokenId = doctokenId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DocType getDocType() {
        return docType;
    }

    public void setDocType(DocType docType) {
        this.docType = docType;
    }

    public Candidate getCandidateID() {
        return candidateID;
    }

    public void setCandidateID(Candidate candidateID) {
        this.candidateID = candidateID;
    }
    

    @Override
    public int hashCode() {
        return new HashCodeBuilder(1997, 13).append(doctokenId).append(documentId).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Document) {
            Document document = (Document) obj;
            return new EqualsBuilder().append(doctokenId, document.getDoctokenId()).append(documentId, document.getDocumentId()).isEquals();
        }
        return false;
    }

}
