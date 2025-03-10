package com.raybiztech.itdeclaration.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.formula.functions.T;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.appraisals.exceptions.DuplicateActiveCycleException;
import com.raybiztech.itdeclaration.business.FinanceCycle;
import com.raybiztech.itdeclaration.business.ITDeclarationForm;
import com.raybiztech.itdeclaration.business.ITDeclarationFormSections;
import com.raybiztech.itdeclaration.business.Investment;
import com.raybiztech.itdeclaration.business.Section;
import com.raybiztech.itdeclaration.exception.DuplicateITDeclareForm;
import com.raybiztech.itdeclaration.exception.NoFinanceCycle;
import com.raybiztech.leavemanagement.exceptions.MailCantSendException;

@Repository("iTDeclarationDaoImpl")
public class ITDeclarationDaoImpl extends DAOImpl implements ITDeclarationDao {
	// Saving Cycle record
	@Override
	public void addCycle(FinanceCycle financeCycle) {
		FinanceCycle cycle = activeFinanceCycle();
		if (cycle == null) {

			save(financeCycle);
		} else if (financeCycle.getActive() == true) {

			cycle.setActive(false);
			update(cycle);
			save(financeCycle);
		} else {
			save(financeCycle);
		}
	}

	// saving Section Record
	@Override
	public void addSection(Section section) {
		save(section);

	}

	// Getting Cycle Record as List from DB
	public List<FinanceCycle> getCycles() {
		return get(FinanceCycle.class);
	}

	// deleting Cycle Record form DB
	@Override
	public void deleteCycle(Long cycleId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ITDeclarationForm.class);
		criteria.createAlias("financeCycle", "cycle");
		criteria.add(Restrictions.eq("cycle.cycleId", cycleId));
		int size = criteria.list().size();

		if (size > 0) {

		} else {
			delete(findBy(FinanceCycle.class, cycleId));
		}

	}

	// updating Cycle record
	@Override
	public void editCycle(FinanceCycle financeCycle) {

		if (financeCycle.getActive() == true) {

			FinanceCycle cycle = activeOtherThanThis(financeCycle.getCycleId());
			if (cycle != null) {
				cycle.setActive(false);
				update(cycle);
				saveOrUpdate(financeCycle);
			} else {
				saveOrUpdate(financeCycle);
			}

		} else
			saveOrUpdate(financeCycle);

	}

	// deleting Section record
	@Override
	public void deleteSection(Long sectionId) {
		delete(findBy(Section.class, sectionId));

	}

	// updating section record
	@Override
	public void editSection(Section section) {
		saveOrUpdate(section);

	}

	@Override
	public List<Investment> getInvestsBySecId(Long sectionId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Investment.class);

		// criteria.add(Restrictions.eq("sectionId", sectionId));

		criteria.createAlias("section", "sec");
		criteria.setFetchMode("sec", FetchMode.JOIN);
		criteria.add(Restrictions.eq("sec.sectionId", sectionId));
		return criteria.list();
	}

	@Override
	public FinanceCycle activeFinanceCycle() {
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(FinanceCycle.class);
		criteria.add(Restrictions.eq("active", true));
		FinanceCycle financeCycle = (FinanceCycle) criteria.uniqueResult();
		return financeCycle;
	}

	// adding ITDeclaration Form
	@Override
	public Long addITDeclareForm(ITDeclarationForm itDeclarationForm)
			throws DuplicateITDeclareForm, NoFinanceCycle {
		Long itFormId = null;

		ITDeclarationForm form = (ITDeclarationForm) isItDeclareFormExist(itDeclarationForm
				.getEmployee().getEmployeeId());

		if (form == null) {
			itDeclarationForm.setFinanceCycle(activeFinanceCycle());
			itFormId = (Long) save(itDeclarationForm);
		} else {
			throw new DuplicateITDeclareForm(
					"IT Declaration Form Already exist for this employee in this active Cycle");
		}

		return itFormId;

	}

	@Override
	public Map<String, Object> getITDeclarationPaginationList(
			Integer startIndex, Integer endIndex, Long cycleId,
			String employeeName) {

		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(ITDeclarationForm.class);
		criteria.createAlias("financeCycle", "cycle");
		criteria.add(Restrictions.eq("cycle.cycleId", cycleId));
		criteria.addOrder(Order.asc("itDeclarationFormId"));
		if (!employeeName.isEmpty()) {
			criteria.createAlias("employee", "employee");
			criteria.add(Restrictions.like("employee.employeeFullName",
					employeeName, MatchMode.ANYWHERE));
		}
		/*Integer listSize = criteria.list().size();

		if (startIndex != null && endIndex != null) {
			criteria.setFirstResult(startIndex);
			criteria.setMaxResults(endIndex - startIndex);
		}
		List<ITDeclarationForm> listForms = criteria.list();

		Map<String, Object> listmap = new HashMap<String, Object>();
		listmap.put("itDeclarationFormlist", listForms);
		listmap.put("size", listSize);
		return listmap;*/
		Map<String, Object> itFormList = getPaginationList(criteria, startIndex, endIndex);
		
		Long listSize = (Long) itFormList.get("listSize");
		Integer size = Integer.parseInt(listSize.toString());
		itFormList.remove("listSize");
		itFormList.put("itDeclarationFormlist", itFormList.remove("list"));
		itFormList.put("size", size);
		
		return itFormList;
	}

	@Override
	public Section sectionFindByNameOrId(String sectionName, Long sectionId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Section.class);
		criteria.add(Restrictions.eq("sectionName", sectionName));
		if (sectionId != -1) {
			criteria.add(Restrictions.ne("sectionId", sectionId));
		}
		return (Section) criteria.uniqueResult();
	}

	@Override
	public FinanceCycle cycleFindByNameOrId(String cycleName, Long cycleId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				FinanceCycle.class);
		criteria.add(Restrictions.eq("cycleName", cycleName));
		if (cycleId != -1) {
			criteria.add(Restrictions.ne("cycleId", cycleId));
		}
		return (FinanceCycle) criteria.uniqueResult();

	}

	@Override
	public ITDeclarationForm isItDeclareFormExist(Long employeeId)
			throws NoFinanceCycle {
		FinanceCycle cycle = activeFinanceCycle();
		if (cycle != null) {
			Criteria criteria = sessionFactory.getCurrentSession()
					.createCriteria(ITDeclarationForm.class);
			criteria.createAlias("financeCycle", "cycle");

			criteria.add(Restrictions.eq("cycle.cycleId", activeFinanceCycle()
					.getCycleId()));
			criteria.createAlias("employee", "employee");
			criteria.add(Restrictions.eq("employee.employeeId", employeeId));
			return (ITDeclarationForm) criteria.uniqueResult();
		} else {
			throw new NoFinanceCycle("No Active Finanace Cycle Found");
		}

	}

	@Override
	public FinanceCycle activeOtherThanThis(Long cycleId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				FinanceCycle.class);
		criteria.add(Restrictions.ne("cycleId", cycleId));
		criteria.add(Restrictions.eq("active", true));
		FinanceCycle cycle = (FinanceCycle) criteria.uniqueResult();

		return cycle;
	}

	@Override
	public Map<String, Object> getITDeclarationPaginationListIndividual(
			Integer startIndex, Integer endIndex, Long cycleId, Long employeeId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ITDeclarationForm.class);
		criteria.createAlias("employee", "emp");
		criteria.add(Restrictions.eq("emp.employeeId", employeeId));
		criteria.createAlias("financeCycle", "cycle");
		criteria.add(Restrictions.eq("cycle.cycleId", cycleId));
		
		
		Map<String, Object> listMap = getPaginationList(criteria, startIndex, endIndex);
		
		listMap.put("itDeclarationFormlist", listMap.remove("list"));
		listMap.put("size", listMap.remove("listSize"));
		return listMap;
	}

	@Override
	public Investment investByNameOrId(Long sectionId, String investmentName,
			Long investmentId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Investment.class);
		criteria.createAlias("section", "section");
		criteria.add(Restrictions.eq("section.sectionId", sectionId));
		criteria.add(Restrictions.eq("investmentName", investmentName));
		if (investmentId != -1) {
			criteria.add(Restrictions.ne("investmentId", investmentId));
		}
		return (Investment) criteria.uniqueResult();
	}

	@Override
	public List<Section> getSectionsHavingInvests() {
		List<Section> sectionList = get(Section.class);
		List<Section> returnList = new ArrayList<Section>();

		for (Section section : sectionList) {
			int size = section.getInvests().size();
			if (size > 0) {

				returnList.add(section);
			}

		}

		return returnList;
	}

	@Override
	public void deleteNullItFormSec() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ITDeclarationFormSections.class);

	}

}
