package com.raybiztech.offerLetter.dao;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.offerLetter.business.OfferLetter;


@Repository("offerLetterDaoImpl")
public class OfferLetterDaoImpl extends DAOImpl implements OfferLetterDao {

	@Override
	public Map<String, Object> getListOfOfferLetters() {
		
		Map<String,Object> map = new HashMap<String, Object>();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(OfferLetter.class);
		criteria.addOrder(Order.desc("id"));
		
		map.put("list", criteria.list());
		map.put("size",criteria.list().size());
		
		return map;
	}
	

}
