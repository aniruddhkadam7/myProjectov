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
public class SearchQueryParamsInAssetList implements Serializable {

    private static final long serialVersionUID = -2887983957104420L;
    
    private String selectionStatus;
    private Long assetTypeId;
    private Long productId;
    private String multipleSearch;
    private Integer startIndex;
    private Integer endIndex;
    private Boolean searchByEmpName;
    private String status;
    private String dateSelection;
    private String fromDate;
    private String toDate;
    
    

    public String getSelectionStatus() {
        return selectionStatus;
    }

    public void setSelectionStatus(String selectionStatus) {
        this.selectionStatus = selectionStatus;
    }

    public Long getAssetTypeId() {
        return assetTypeId;
    }

    public void setAssetTypeId(Long assetTypeId) {
        this.assetTypeId = assetTypeId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getMultipleSearch() {
        return multipleSearch;
    }

    public void setMultipleSearch(String multipleSearch) {
        this.multipleSearch = multipleSearch;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }

    public Boolean isSearchByEmpName() {
        return searchByEmpName;
    }

    public void setSearchByEmpName(Boolean searchByEmpName) {
        this.searchByEmpName = searchByEmpName;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDateSelection() {
		return dateSelection;
	}

	public void setDateSelection(String dateSelection) {
		this.dateSelection = dateSelection;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
    
    
    
}
