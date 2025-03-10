/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.appraisals.dao.kra;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.DesignationKras;
import com.raybiztech.appraisals.business.KPI;
import com.raybiztech.appraisals.business.KRA;
import com.raybiztech.appraisals.business.KraWithWeightage;
import com.raybiztech.appraisals.dao.DAOImpl;

/**
 * 
 * 
 */
@Component("kraDao")
public class KRADaoImpl extends DAOImpl implements KRADao {

	@Override
	public <T extends Serializable> void update(T object) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<KraWithWeightage> getKRAWithWeightages(Long kraId) {

		String hql = "FROM KraWithWeightage E WHERE E.kra.kraId =:kraId";
		Query query = getSessionFactory()
				.getCurrentSession().createQuery(hql);
		query.setParameter("kraId",kraId);
		List<KraWithWeightage> results = query.list();
		return results;
	}
	
	@Override
	public List<KraWithWeightage> removeWithWeightage() {

		String hql = "FROM KraWithWeightage E WHERE E.kra.kraId =null";
		Query query = getSessionFactory()
				.getCurrentSession().createQuery(hql);
		List<KraWithWeightage> results = query.list();
		return results;
	}

	@Override
	public void deleteKPI(KPI kpi) {
		delete(kpi);
	
		KRA kra=kpi.getKra();
		
		kra.getKpis().remove(kpi);
			
		saveOrUpdate(kra);
		
		
		
	}

	@Override
	public int removeKRAWithWeightage(DesignationKras designation) {
		// TODO Auto-generated method stub
		String queryString = "delete from KraWithWeightage where designation.designationKRAsId="+designation.getDesignationKRAsId();
		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
		return query.executeUpdate();
	}

	

}
