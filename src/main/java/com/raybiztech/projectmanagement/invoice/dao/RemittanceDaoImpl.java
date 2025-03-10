package com.raybiztech.projectmanagement.invoice.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.projectmanagement.business.Client;
import com.raybiztech.projectmanagement.invoice.business.Remittance;
import com.raybiztech.projectmanagement.invoice.lookup.Bank;
import com.raybiztech.projectmanagement.invoice.lookup.Currency;
import com.raybiztech.projectmanagement.invoice.lookup.RemittanceLocation;
import com.raybiztech.projectmanagement.invoice.utility.InvoiceLookUpKeyConstants;

@Repository("remittanceDaoImpl")
public class RemittanceDaoImpl extends DAOImpl implements RemittanceDao {

	@Transactional
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, List<? extends Object>> getBankAndCurrencyDetails() {

		Map<String, List<? extends Object>> map = new HashMap<String, List<? extends Object>>();

		List<Bank> bankNamesList = getSessionFactory().getCurrentSession()
				.createCriteria(Bank.class).list();

		List<Currency> currencylist = getSessionFactory().getCurrentSession()
				.createCriteria(Currency.class).list();

		List<RemittanceLocation> locations = getSessionFactory()
				.getCurrentSession().createCriteria(RemittanceLocation.class)
				.list();

		List<Client> clients = getSessionFactory().getCurrentSession()
				.createCriteria(Client.class).list();

		map.put(InvoiceLookUpKeyConstants.BANK_NAMES, bankNamesList);
		map.put(InvoiceLookUpKeyConstants.CURRENCY_TYPES, currencylist);
		map.put(InvoiceLookUpKeyConstants.REMITTANCE_LOCATIONS, locations);
		map.put(InvoiceLookUpKeyConstants.CLIENT, clients);
		return map;
	}

	@Override
	public void addRemittanceDetails(Remittance projectDetails) {

		getSessionFactory().getCurrentSession().save(projectDetails);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Remittance> getRemittanceList(String currency) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Remittance.class);
		if (!currency.equalsIgnoreCase("All"))
			criteria.add(Restrictions.eq("currencyType", currency));

		return criteria.list();

	}

	@Override
	public Remittance getBankNameAndCurrencyDetails(String CurrencyType,
			String bankName) {

		return (Remittance) sessionFactory.getCurrentSession()
				.createCriteria(Remittance.class)
				.add(Restrictions.eq("currencyType", CurrencyType))
				.add(Restrictions.eq("bankName", bankName));

	}
	@Override
	public int checkLocationExist(String location){
		int nos = sessionFactory.getCurrentSession()
				.createCriteria(RemittanceLocation.class)
				.add(Restrictions.ilike("name", location)).list().size();
		return nos;
	}

}
