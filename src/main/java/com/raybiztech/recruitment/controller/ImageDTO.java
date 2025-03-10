/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.controller;

/**
 *
 * @author hari
 */
public class ImageDTO {

    private Long employeeId;
    private String extenstion;
    private byte[] imageData = null;
    private String photoURL;

    /**
     *
     * @param extenstion1
     * @param imageData1
     * @param photoURL1
     */
    public ImageDTO(String extenstion1, byte[] imageData1, String photoURL1) {
        this.extenstion = extenstion1;
        if (imageData1 != null) {
            this.imageData = imageData1.clone();
        }
        this.photoURL = photoURL1;
    }

    /**
     *
     * @param extenstion1
     * @param imageData1
     * @param photoURL1
     * @param employeeId1
     *
     */
    public ImageDTO(String extenstion1, byte[] imageData1, String photoURL1,
            Long employeeId1) {
        this.extenstion = extenstion1;
        if (imageData1 != null) {
            this.imageData = imageData1.clone();
        }
        this.photoURL = photoURL1;
        this.employeeId = employeeId1;

    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getExtenstion() {
        return extenstion;
    }

    public void setExtenstion(String extenstion) {
        this.extenstion = extenstion;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    @Override
    public String toString() {
        return "ImageDTO{" + "extenstion=" + extenstion + ", imageData="
                + imageData.clone() + '}';
    }

}
