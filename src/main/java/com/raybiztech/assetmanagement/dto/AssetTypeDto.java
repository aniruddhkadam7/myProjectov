/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.assetmanagement.dto;

import java.io.Serializable;

/**
 *
 * @author anil
 */
public class AssetTypeDto implements Serializable{
    
    private static final long serialVersionUID = -53576622608682824L;
    
    private Long assetTypeId;
    private String assetType;

    public Long getAssetTypeId() {
        return assetTypeId;
    }

    public void setAssetTypeId(Long assetTypeId) {
        this.assetTypeId = assetTypeId;
    }

  

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }
    
    
}
