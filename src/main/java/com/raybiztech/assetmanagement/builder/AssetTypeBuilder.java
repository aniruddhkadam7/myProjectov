/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.assetmanagement.builder;

import com.raybiztech.assetmanagement.business.AssetType;
import com.raybiztech.assetmanagement.dto.AssetTypeDto;

/**
 *
 * @author anil
 */
public class AssetTypeBuilder {
    
    public AssetTypeDto ToDTO(AssetType assetType){
        AssetTypeDto assetTypeDto=null;
        if(assetType!=null){
            assetTypeDto=new AssetTypeDto();
            assetTypeDto.setAssetTypeId(assetType.getId());
            assetTypeDto.setAssetType(assetType.getAssetType());
        }
        return assetTypeDto;
        
    }
    
}
