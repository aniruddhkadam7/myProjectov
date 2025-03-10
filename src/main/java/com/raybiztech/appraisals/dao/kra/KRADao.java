/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.appraisals.dao.kra;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Service;

import com.raybiztech.appraisals.business.DesignationKras;
import com.raybiztech.appraisals.business.KPI;
import com.raybiztech.appraisals.business.KraWithWeightage;
import com.raybiztech.appraisals.dao.DAO;

/**
 * 
 * @author hari
 */
@Service
public interface KRADao extends DAO {
	<T extends Serializable> void update(T object);

	List<KraWithWeightage> getKRAWithWeightages(Long kraId);
	List<KraWithWeightage> removeWithWeightage();

	void deleteKPI(KPI kpi);
	
	int removeKRAWithWeightage(DesignationKras designation);
	
}
