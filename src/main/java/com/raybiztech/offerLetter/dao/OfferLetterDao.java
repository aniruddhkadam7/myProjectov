package com.raybiztech.offerLetter.dao;

import java.util.Map;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import com.raybiztech.appraisals.dao.DAO;

public interface OfferLetterDao extends DAO {
	
	public Map<String, Object> getListOfOfferLetters();

}
