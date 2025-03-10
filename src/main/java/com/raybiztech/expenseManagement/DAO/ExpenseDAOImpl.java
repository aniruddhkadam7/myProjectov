package com.raybiztech.expenseManagement.DAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.assetmanagement.business.VendorDetails;
import com.raybiztech.date.Date;
import com.raybiztech.expenseManagement.business.CreditCard;
import com.raybiztech.expenseManagement.business.ExpenseCategory;
import com.raybiztech.expenseManagement.business.ExpenseForm;
import com.raybiztech.expenseManagement.business.ExpenseFormAudit;
import com.raybiztech.expenseManagement.business.ExpenseSubCategory;
import com.raybiztech.expenseManagement.business.PaymentForm;
import com.raybiztech.pattern.business.Pattern;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;

@Repository("expenseDaoImpl")

public class ExpenseDAOImpl extends DAOImpl implements ExpenseDAO{
	
	
	Logger logger = Logger.getLogger(ExpenseDAOImpl.class);

	@Override
	public List<ExpenseCategory> getCategoryList() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ExpenseCategory.class);
		criteria.addOrder(Order.desc("id"));
		return criteria.list();
	}

	@Override
	public List<ExpenseSubCategory> getsubCategoryList() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ExpenseSubCategory.class);
		criteria.addOrder(Order.desc("id"));
		return criteria.list();
	}

	@Override
	public List<ExpenseSubCategory> getSubCategories(Long categoryIds) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ExpenseSubCategory.class);
		criteria.createAlias("category", "category");
		criteria.add(Restrictions.eq("category.id", categoryIds));
		// criteria.addOrder(Order.desc("id"));
		return criteria.list();
	}

	@Override
	public Map<String, Object> getExpensesList(Integer startIndex, Integer endIndex, Date fromDate, Date toDate,
			String multipleSearch, Long departmentId, List<Long> categoryIds, List<Long> subCategoryIds, String country,
			String paymentMode) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ExpenseForm.class);
		
		if (fromDate != null && toDate != null) {

			criteria.add(Restrictions.between("expenditureDate", fromDate, toDate));
		}

		
		if (!multipleSearch.isEmpty()) {

			Criterion expenseNumberCriterion = Restrictions.ilike("expenseNumber", multipleSearch, MatchMode.ANYWHERE);

			Criterion searchCriterion = Restrictions.or(
					Restrictions.ilike("voucherNumber", multipleSearch, MatchMode.ANYWHERE),
					Restrictions.ilike("purpose", multipleSearch, MatchMode.ANYWHERE));
			criteria.add(Restrictions.or(expenseNumberCriterion, searchCriterion));

		}
		
		if (departmentId != null) {

			criteria.createAlias("department", "department");
			criteria.add(Restrictions.eq("department.departmentId", departmentId));
		}
		
		
		  if (!categoryIds.isEmpty()) 
		  {
			 
		     criteria.createAlias("category", "category");
		     criteria.add(Restrictions.in("category.id", categoryIds));
		} 
			
		  if(!subCategoryIds.isEmpty()) 
		  { 
			  
		      criteria.createAlias("subCategory", "subCategory");
		      criteria.add(Restrictions.in("subCategory.id", subCategoryIds));
		} 
		
			 

		if (!country.isEmpty()) {

			criteria.add(Restrictions.eq("country", country));
		}

		if (!paymentMode.isEmpty()) {

			criteria.add(Restrictions.eq("paymentMode", paymentMode));
		}

		criteria.addOrder(Order.desc("expenditureDate"));
		criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("voucherNumber")));

		Map<String, Object> expenseMap = getPaginationList(criteria, startIndex, endIndex);
		map.put("list", expenseMap.get("list"));
		map.put("size", expenseMap.get("listSize"));
		
		return map;
	}

	@Override
	public List<Project> getMatchedProjects(String searchStr) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Project.class);
		criteria.add(Restrictions.ilike("projectName", searchStr, MatchMode.ANYWHERE));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		criteria.setMaxResults(10);
		return criteria.list();
	}

	@Override
	public List<VendorDetails> getVendorList(String searchStr) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(VendorDetails.class);
		criteria.add(Restrictions.ilike("vendorName", searchStr, MatchMode.ANYWHERE));
		criteria.add(Restrictions.eq("isExpenseVendor", Boolean.TRUE));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		criteria.setMaxResults(10);
		return criteria.list();
	}

	@Override
	public Map<String, Object> getExpensesYearlyReport(String fromDate, String toDate, Long departmentId,
			Long categoryId, String country) {

		
		String dateCondition = "";
		String whereCondition = " Where 1";

		dateCondition = " and (ex.Expenditure_Date >='" + fromDate + "'and ex.Expenditure_Date <='" + toDate + "') ";

		if (departmentId != null) {
			whereCondition += " and dp.DEPT_ID =" + departmentId;
		}

		if (categoryId != null) {
			whereCondition += " and ca.Id =" + categoryId;
		}

		/*
		 * if(!country.isEmpty()) { whereCondition += " and ex.Country = '" + country +
		 * "'"; }
		 */
		if (!country.isEmpty()) {
			whereCondition += " and ex.Country = '" + country + "'";
		}

		String sql = "SELECT temp.* from (SELECT ex.* from ExpenseManagementForm ex"
				+ " JOIN ExpenseCategory ca ON ex.Category = ca.Id"
				+ " JOIN EMPDEPARTMENT dp ON ex.Department = dp.DEPT_ID" + whereCondition + dateCondition
				+ "order by ex.FormId DESC)" + "as temp";

		// logger.warn(sql);

		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);

		query.addEntity(ExpenseForm.class);

		List<ExpenseForm> expensesList = query.list();
		Map<String, Object> map = new HashMap<>();
		map.put("list", expensesList);
		return map;
	}

	@Override
	public List<ExpenseForm> getMonthWiseExpenseList(String month, String year, Long departmentId, Long categoryId,
			String country) {

		

		String whereCondition = " Where YEAR(ex.Expenditure_Date)='" + year + "' and MONTH(ex.Expenditure_Date) ='"
				+ month + "'";

		if (departmentId != null) {
			whereCondition += " and dp.DEPT_ID =" + departmentId;
		}

		if (categoryId != null) {
			whereCondition += " and ca.Id =" + categoryId;
		}

		if (!country.isEmpty()) {
			whereCondition += " and ex.Country = '" + country + "'";
		}

		String sql = "SELECT ex.* from  ExpenseManagementForm ex" + " JOIN ExpenseCategory ca ON ex.Category = ca.Id"
				+ " JOIN EMPDEPARTMENT dp ON ex.Department = dp.DEPT_ID" + whereCondition
				+ " order by ex.Expenditure_Date DESC";

		// logger.warn(sql);

		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);

		query.addEntity(ExpenseForm.class);

		return query.list();
	}

	@Override
	public Boolean checkForDuplicateCategory(String categoryName) {
		Boolean flag = null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ExpenseCategory.class);
		criteria.add(Restrictions.like("categoryName", categoryName, MatchMode.EXACT));
		if (criteria.list().size() > 0) {
			flag = Boolean.TRUE;
		} else {
			flag = Boolean.FALSE;
		}
		return flag;
	}

	@Override
	public Boolean checkForDuplicateSubCategory(Long categoryId, String subCategoryName) {
		Boolean flag = null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ExpenseSubCategory.class);
		criteria.createAlias("category", "category");
		criteria.add(Restrictions.and(Restrictions.eq("category.id", categoryId),
				Restrictions.like("subCategoryName", subCategoryName, MatchMode.EXACT)));
		if (criteria.list().size() > 0) {
			flag = Boolean.TRUE;
		} else {
			flag = Boolean.FALSE;
		}
		return flag;
	}

	@Override
	public List<ExpenseFormAudit> getExpenseAudit(Long formId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ExpenseFormAudit.class);
		criteria.add(Restrictions.eq("formId", formId));
		criteria.addOrder(Order.desc("id"));
		return criteria.list();
	}

	@Override
	public List<CountryLookUp> getCountries() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CountryLookUp.class);
		return criteria.list();
	}

	@Override
	public String getMaxExpenseNo(String year, String country) {
		String sql = "Select ex.Expense_Number from ExpenseManagementForm ex where"
					+ " YEAR(ex.Expenditure_Date) = '" + year + "'"
					+ " and ex.Country = '" + country + "'"
					+ "and ex.Expense_Number IS NOT NULL"
					+ " order by ex.FormId DESC";
		
		SQLQuery query = (SQLQuery) sessionFactory.getCurrentSession().createSQLQuery(sql).setMaxResults(1);
		
		return (String) query.uniqueResult();
	}

	@Override
	public List<CreditCard> getCardsList() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CreditCard.class);
		criteria.addOrder(Order.desc("cardId"));
		return criteria.list();
	}

	@Override
	public Map<String, Object> getReimbursementExpenseList(Integer startIndex, Integer endIndex, Date fromDate,
			Date toDate, String multipleSearch, Long departmentId, Long categoryId, String country, String paymentMode,
			String paymentStatus, Long creditCardId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ExpenseForm.class);
		criteria.add(Restrictions.eq("isReimbursable", Boolean.TRUE));
		criteria.add(Restrictions.eq("paymentStatus", paymentStatus));
		// System.out.println("fromDate:"+fromDate + " " + "toDate:"+toDate);
		if (fromDate != null && toDate != null) {
			criteria.add(Restrictions.between("expenditureDate", fromDate, toDate));
		}

		// System.out.println("search:"+multipleSearch);
		if (!multipleSearch.isEmpty()) {

			// System.out.println("in if multipleSearch");

			Criterion expenseNumberCriterion = Restrictions.ilike("expenseNumber", multipleSearch, MatchMode.ANYWHERE);

			Criterion searchCriterion = Restrictions.or(
					Restrictions.ilike("voucherNumber", multipleSearch, MatchMode.ANYWHERE),
					Restrictions.ilike("purpose", multipleSearch, MatchMode.ANYWHERE));
			criteria.add(Restrictions.or(expenseNumberCriterion, searchCriterion));

		}
		// System.out.println("deptId:"+departmentId);
		if (departmentId != null) {
			criteria.createAlias("department", "department");
			criteria.add(Restrictions.eq("department.departmentId", departmentId));
		}
		// System.out.println("catId:"+categoryId);
		if (categoryId != null) {
			criteria.createAlias("category", "category");
			criteria.add(Restrictions.eq("category.id", categoryId));
		}

		if (!country.isEmpty()) {
			criteria.add(Restrictions.eq("country", country));
		}

		if (!paymentMode.isEmpty()) {
			criteria.add(Restrictions.eq("paymentMode", paymentMode));
		}

		if (paymentMode.equalsIgnoreCase("Credit Card") && creditCardId != null) {
			criteria.createAlias("creditCardDetails", "creditCardDetails");
			criteria.add(Restrictions.eq("creditCardDetails.cardId", creditCardId));

		}

		// criteria.addOrder(Order.desc("expenditureDate"));
		criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("voucherNumber")));

		// System.out.println("startIndex:"+startIndex);
		// System.out.println("endIndex:"+endIndex);
		Map<String, Object> expenseMap = getPaginationList(criteria, startIndex, endIndex);
		map.put("list", expenseMap.get("list"));
		map.put("size", expenseMap.get("listSize"));

		return map;
	}

	@Override
	public List<PaymentForm> getPaymentList(Long id) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PaymentForm.class);
		criteria.createAlias("expenseId", "expense");
		criteria.add(Restrictions.eq("expense.id", id));
		criteria.addOrder(Order.desc("id"));
		return criteria.list();
	}

	@Override
	public List<ExpenseForm> getTotalExpenseAmount(Date fromDate, Date toDate, String multipleSearch, Long departmentId,
			List<Long> categoryIds,List<Long> subCategoryIds, String country, String paymentMode) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ExpenseForm.class);
		// System.out.println("fromDate:"+fromDate + " " + "toDate:"+toDate);
		if (fromDate != null && toDate != null) {
			criteria.add(Restrictions.between("expenditureDate", fromDate, toDate));
		}

		// System.out.println("search:"+multipleSearch);
		if (!multipleSearch.isEmpty()) {

			// System.out.println("in if multipleSearch");

			Criterion expenseNumberCriterion = Restrictions.ilike("expenseNumber", multipleSearch, MatchMode.ANYWHERE);

			Criterion searchCriterion = Restrictions.or(
					Restrictions.ilike("voucherNumber", multipleSearch, MatchMode.ANYWHERE),
					Restrictions.ilike("purpose", multipleSearch, MatchMode.ANYWHERE));
			criteria.add(Restrictions.or(expenseNumberCriterion, searchCriterion));

		}
		// System.out.println("deptId:"+departmentId);
		if (departmentId != null) {
			criteria.createAlias("department", "department");
			criteria.add(Restrictions.eq("department.departmentId", departmentId));
		}
		// System.out.println("catId:"+categoryId);
		if (!categoryIds.isEmpty()) {
			criteria.createAlias("category", "category");
			criteria.add(Restrictions.in("category.id", categoryIds));
		}
		if(!subCategoryIds.isEmpty()) {
			criteria.createAlias("subCategory", "subCategory");
			criteria.add(Restrictions.in("subCategory.id", subCategoryIds));
			
		}

		if (!country.isEmpty()) {
			criteria.add(Restrictions.eq("country", country));
		}

		if (!paymentMode.isEmpty()) {
			criteria.add(Restrictions.eq("paymentMode", paymentMode));
		}
		return criteria.list();
	}

	@Override
	public String getExpensePattern() {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Pattern.class);
		criteria.add(Restrictions.eq("type", "Expense"));

		List<Pattern> patternList = criteria.list();

		String specificPattern = null;

		for (Pattern patt : patternList) {
			if (patt.getType().equals("Expense")) {
				specificPattern = patt.getSpecificPattern();
			}
		}

		return specificPattern;

	}

	@Override
	public List<ExpenseForm> getCountryWiseExpenseReport(Date fromDate, Date toDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ExpenseForm.class);
		criteria.add(Restrictions.and(Restrictions.ge("expenditureDate", fromDate),
				Restrictions.le("expenditureDate", toDate)));
		return criteria.list();
	}

	@Override
	public List<ExpenseForm> getCountryWiseExpenseList(String key, Date fromDate, Date toDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ExpenseForm.class);
		criteria.add(Restrictions.eq("country", key));
		criteria.add(Restrictions.and(Restrictions.ge("expenditureDate", fromDate),
				Restrictions.le("expenditureDate", toDate)));
		return criteria.list();
	}

	@Override
	public List<ExpenseForm> getCountryCategoryWiseExpenseList(Date startDate, Date endDate, String country,
			String category) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ExpenseForm.class);
		criteria.createAlias("category", "category");
		criteria.add(Restrictions.and(Restrictions.ge("expenditureDate", startDate),
				Restrictions.le("expenditureDate", endDate)));
		criteria.add(Restrictions.and(Restrictions.eq("country", country),
				Restrictions.eq("category.categoryName", category)));
		return criteria.list();
	}

	@Override
	public List<ExpenseForm> getCopiedExpensesList(List<Long> myObjects) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ExpenseForm.class);
		// List<ExpenseForm> list = null;

		criteria.add(Restrictions.in("id", myObjects));
		// list.add((ExpenseForm) criteria.list());

		return criteria.list();
	}

	@Override
	public List<ExpenseSubCategory> getSelectedSubCategories(List<Long> categoryIds) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ExpenseSubCategory.class);
		criteria.createAlias("category", "category");
		if (categoryIds.isEmpty()) {
			criteria.addOrder(Order.desc("category.id"));
		}
		criteria.add(Restrictions.in("category.id", categoryIds));

		return criteria.list();
	}
}
