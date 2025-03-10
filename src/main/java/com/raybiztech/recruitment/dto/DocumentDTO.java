/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.dto;

import java.io.Serializable;

/**
 *
 * @author naresh
 */
public class DocumentDTO implements Serializable{

    private static final long serialVersionUID = -2459340254939779958L;

    private Long id;

    private String docType;

    private String doctokenId;

    private String description;

    public Long getId() {
        return id;
    }

    public String getDoctokenId() {
        return doctokenId;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDoctokenId(String doctokenId) {
        this.doctokenId = doctokenId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

}
