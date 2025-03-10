package com.raybiztech.projectmanagement.invoice.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

//import javax.jms.Session;

//import javax.jms.Session;
















import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTRImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.appraisals.utils.NumberFormateUtil;
import com.raybiztech.date.Date;
import com.raybiztech.date.DayOfMonth;
import com.raybiztech.date.HourOfDay;
import com.raybiztech.date.MinuteOfHour;
import com.raybiztech.date.MonthOfYear;
import com.raybiztech.date.Second;
import com.raybiztech.date.SecondOfMinute;
import com.raybiztech.date.YearOfEra;
import com.raybiztech.pattern.business.Pattern;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.ChangeRequest;
import com.raybiztech.projectmanagement.business.Client;
import com.raybiztech.projectmanagement.business.Milestone;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.business.ProjectNumbers;
import com.raybiztech.projectmanagement.business.ProjectStatus;
import com.raybiztech.projectmanagement.business.ProjectType;
import com.raybiztech.projectmanagement.invoice.business.CountryAddress;
import com.raybiztech.projectmanagement.invoice.business.Invoice;
import com.raybiztech.projectmanagement.invoice.business.InvoiceAudit;
import com.raybiztech.projectmanagement.invoice.business.InvoiceReminderLog;
import com.raybiztech.projectmanagement.invoice.business.InvoiceSummary;
import com.raybiztech.projectmanagement.invoice.business.InvoiceTracker;
import com.raybiztech.projectmanagement.invoice.business.Remittance;
import com.raybiztech.projectmanagement.invoice.dto.CountryAddressDTO;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;
import com.raybiztech.projectmanagement.invoice.lookup.DiscountTypeLookup;
import com.raybiztech.projectmanagement.invoice.lookup.DurationLookup;
import com.raybiztech.projectmanagement.invoice.lookup.InvoiceStatusLookup;
import com.raybiztech.projectmanagement.invoice.lookup.PaymentTermLookup;
import com.raybiztech.projectmanagement.invoice.lookup.TaxTypeLookup;
import com.raybiztech.projectmanagement.invoice.utility.HibernateSupressWaringsUtil;
import com.raybiztech.projectmanagement.invoice.utility.InvoiceLookUpKeyConstants;
import com.raybiztech.recruitment.utils.DateParser;

@Repository("invoiceDaoImpl")
public class InvoiceDaoImpl extends DAOImpl implements InvoiceDao {

	Logger logger = Logger.getLogger(InvoiceDaoImpl.class);

	@Transactional
	@Override
	public Map<String, Object> getAllLookUps() {

		Map<String, Object> map = new HashMap<String, Object>();

		List<DiscountTypeLookup> discountTypeLookups = HibernateSupressWaringsUtil
				.listAndCast(getSessionFactory().getCurrentSession()
						.createCriteria(DiscountTypeLookup.class));

		List<DurationLookup> durationLookups = HibernateSupressWaringsUtil
				.listAndCast(getSessionFactory().getCurrentSession()
						.createCriteria(DurationLookup.class));

		List<PaymentTermLookup> paymentTermLookups = HibernateSupressWaringsUtil
				.listAndCast(getSessionFactory().getCurrentSession()
						.createCriteria(PaymentTermLookup.class));

		List<TaxTypeLookup> taxTypeLookups = HibernateSupressWaringsUtil
				.listAndCast(getSessionFactory().getCurrentSession()
						.createCriteria(TaxTypeLookup.class));

		List<CountryLookUp> countries = HibernateSupressWaringsUtil
				.listAndCast(getSessionFactory().getCurrentSession()
						.createCriteria(CountryLookUp.class));
		List<InvoiceStatusLookup> status = HibernateSupressWaringsUtil
				.listAndCast(getSessionFactory().getCurrentSession()
						.createCriteria(InvoiceStatusLookup.class));

		// To get all Remittance Details at once Code

		Map<String, List<Remittance>> map1 = new HashMap<String, List<Remittance>>();
		List<Remittance> remittanceList = HibernateSupressWaringsUtil
				.listAndCast(getSessionFactory().getCurrentSession()
						.createCriteria(Remittance.class));

		for (Remittance remittance : remittanceList) {
			if (map1.containsKey(remittance.getBankName())) {
				map1.get(remittance.getBankName()).add(remittance);
			} else {
				List<Remittance> reList = new ArrayList<Remittance>();
				reList.add(remittance);
				map1.put(remittance.getBankName(), reList);
			}

		}

		map.put(InvoiceLookUpKeyConstants.BANK_NAMES, map1);
		map.put(InvoiceLookUpKeyConstants.DISCOUNT_TYPES, discountTypeLookups);
		map.put(InvoiceLookUpKeyConstants.DURATION_TYPES, durationLookups);
		map.put(InvoiceLookUpKeyConstants.PAYMENTTERM_TYPES, paymentTermLookups);
		map.put(InvoiceLookUpKeyConstants.TAX_TYPES, taxTypeLookups);
		map.put(InvoiceLookUpKeyConstants.COUNTRIES, countries);
		map.put(InvoiceLookUpKeyConstants.INVOICESTATUS_TYPES, status);

		return map;

	}

	@Override
	public Map<String, Object> getInvoiceList(String projectType,
			List<String> status, String fromDate, String toDate,
			String multiText, Integer fromIndex, Integer toIndex,
			String datePeriod, Set<Long> projectids, String invoiceCountry,
			Boolean intrnalOrNot) {
			
		// Session session = sessionFactory.getCurrentSession();
		Criteria invoicecriteria = sessionFactory.getCurrentSession()
				.createCriteria(Invoice.class);

		// Criteria invoicecriteria = session.createCriteria(Invoice.class);
		invoicecriteria.createAlias("milsestone", "milestone");
		invoicecriteria.createAlias("milestone.project", "project");
		invoicecriteria.createAlias("project.client", "client");

		/*
		 * invoicecriteria.setFetchMode("milestone", FetchMode.JOIN);
		 * invoicecriteria.setFetchMode("project", FetchMode.JOIN);
		 * invoicecriteria.setFetchMode("client", FetchMode.JOIN);
		 */

		invoicecriteria.addOrder(Order.desc("id"));

		/*
		 * if (!projectids.isEmpty())
		 * invoicecriteria.add(Restrictions.in("project.id", projectids));
		 */

		if (!projectType.equalsIgnoreCase("ALL"))
			invoicecriteria.add(Restrictions.eq("project.type",
					ProjectType.valueOf(projectType)));
		

		if (!invoiceCountry.equalsIgnoreCase("null"))
			invoicecriteria.add(Restrictions.ilike("country", invoiceCountry,
					MatchMode.ANYWHERE));

		if (!status.isEmpty())
			invoicecriteria.add(Restrictions.in("invoiceStatus", status));

		if (!multiText.equalsIgnoreCase("null")
				&& !NumberFormateUtil.isDouble(multiText)) {

			Criterion number = Restrictions.ilike("number", multiText,
					MatchMode.ANYWHERE);

			Criterion clientName = Restrictions.ilike("client.name", multiText,
					MatchMode.ANYWHERE);

			Criterion personName = Restrictions.ilike("client.personName",
					multiText, MatchMode.ANYWHERE);

			Criterion projectName = Restrictions.ilike("project.projectName",
					multiText, MatchMode.ANYWHERE);

			Criterion criterion1 = Restrictions.or(number, clientName);

			Criterion criterion2 = Restrictions.or(personName, projectName);

			invoicecriteria.add(Restrictions.or(criterion1, criterion2));

		}

		Date startDate = null;
		Date endDate = null;

		if (!datePeriod.equalsIgnoreCase("Select Date")) {

			if (datePeriod.equalsIgnoreCase("custom")) {
				startDate = dateConverter(fromDate);
				endDate = dateConverter(toDate);
			} else {
				Map<String, Date> parsedDates = getCustomDates(datePeriod);
				startDate = parsedDates.get("startDate");
				endDate = parsedDates.get("endDate");

			}

			Criterion receivedAmountCriterion = Restrictions.between(
					"invoiceAmountReceviedDate", startDate, endDate);
			Criterion sentAmountCriterion = Restrictions.between(
					"invoiceAmountSentDate", startDate, endDate);

			invoicecriteria.add(Restrictions.or(sentAmountCriterion,
					receivedAmountCriterion));

		}

		/*
		 * if(intrnalOrNot){
		 * invoicecriteria.add(Restrictions.eq("project.internalOrNot",
		 * Boolean.TRUE)); }else {
		 * invoicecriteria.add(Restrictions.eq("project.internalOrNot",
		 * Boolean.FALSE)); }
		 */

		invoicecriteria.add(Restrictions.eq("project.internalOrNot",
				intrnalOrNot));

		// Map<String, Object> ListofInvoiceMap = new HashMap<>();

		// ListofInvoiceMap.put("InvoiceListSize",invoicecriteria.list().size());

		// for exporting from index and end index both are null because of we
		// are exporting what we filtered.
		/*
		 * if (fromIndex != null && toIndex != null) {
		 * invoicecriteria.setFirstResult(fromIndex);
		 * invoicecriteria.setMaxResults(toIndex - fromIndex); }
		 */

		// for pagination performance
		Map<String, Object> listOfInvoiceMap = getPaginationList(
				invoicecriteria, fromIndex, toIndex);
		// Object list = listOfInvoiceMap.get("list");
		listOfInvoiceMap.put("InvoiceListSize",
				listOfInvoiceMap.remove("listSize"));
		listOfInvoiceMap.put("InvoiceList", listOfInvoiceMap.remove("list"));
		return listOfInvoiceMap;

		/*
		 * Map<String, Object> listOfInvoiceMap =
		 * getPaginationList(invoicecriteria, fromIndex, toIndex);
		 * 
		 * Map<String, Object> ListofInvoiceMap = new HashMap<>();
		 * logger.warn(listOfInvoiceMap.get("listSize"));
		 * ListofInvoiceMap.put("InvoiceList",listOfInvoiceMap.get("list"));
		 * ListofInvoiceMap.put("InvoiceListSize",
		 * listOfInvoiceMap.get("listSize"));
		 * logger.warn(listOfInvoiceMap.get("listSize"));
		 * //ListofInvoiceMap.put("InvoiceList", invoicecriteria.list()); return
		 * ListofInvoiceMap;
		 */

	}

	@Override
	public Map<String, Object> getInvoiceAuditList(List<String> status,
			Date fromDate, Date toDate, String multiText, String datePeriod,
			String invoiceCountry) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InvoiceAudit.class);

		if (!invoiceCountry.equalsIgnoreCase("null"))
			criteria.add(Restrictions.ilike("country", invoiceCountry,
					MatchMode.ANYWHERE));

		if (!status.isEmpty())
			criteria.add(Restrictions.in("invoiceStatus", status));

		if (!multiText.equalsIgnoreCase("null")
				&& !NumberFormateUtil.isDouble(multiText)) {

			Criterion number = Restrictions.ilike("invoiceNumber", multiText,
					MatchMode.ANYWHERE);

			Criterion clientName = Restrictions.ilike("clientname", multiText,
					MatchMode.ANYWHERE);

			Criterion projectName = Restrictions.ilike("projectName",
					multiText, MatchMode.ANYWHERE);

			Criterion criterion1 = Restrictions.or(number, clientName);

			criteria.add(Restrictions.or(criterion1, projectName));

		}

		if (fromDate != null && toDate != null) {
			Second startSecondDate = new Second(YearOfEra.valueOf(fromDate
					.getYearOfEra().getValue()), MonthOfYear.valueOf(fromDate
					.getMonthOfYear().getValue()), DayOfMonth.valueOf(fromDate
					.getDayOfMonth().getValue()), HourOfDay.valueOf(0),
					MinuteOfHour.valueOf(0), SecondOfMinute.valueOf(0));

			Second endSecondDate = new Second(YearOfEra.valueOf(toDate
					.getYearOfEra().getValue()), MonthOfYear.valueOf(toDate
					.getMonthOfYear().getValue()), DayOfMonth.valueOf(toDate
					.getDayOfMonth().getValue()), HourOfDay.valueOf(0),
					MinuteOfHour.valueOf(0), SecondOfMinute.valueOf(0));

			criteria.add(Restrictions.between("modifiedDate", startSecondDate,
					endSecondDate));

		}

		criteria.add(Restrictions.ne("persistType", "DELETED"));
		criteria.addOrder(Order.desc("id"));

		Map<String, Object> ListofInvoiceMap = new HashMap<>();
		ListofInvoiceMap.put("InvoiceList", criteria.list());
		return ListofInvoiceMap;

	}

	// For following functionality Previous method was used now we changed it to
	// native SQL
	@Override
	public Map<String, Object> getInvoiceAuditReport(List<String> status,
			String fromDate, String toDate, String multiText,
			String datePeriod, String invoiceCountry, Boolean intrnalOrNot) {

		// Functionality of this method is to get Invoice Audits which are
		// CREATED between selected Date range and that invoice audit sent or
		// received date should match date of Invoice in Invoice Table so that
		// Invoices in selected date range are only selected

		// If status is SENT getting only sent Invoice Audits in Date range
		// If combination is selected than both combination should come below
		// are the conditions which are converted to NATIVE SQL Query

		String statusList = "";
		String a = "";
		String b = "";
		String wherecondition = "Where 1 ";
		String dateCondition = "";
		String multiSearchCondition = "";
		String orderBy = " order by ";

		for (String string : status) {
			statusList = (statusList.equalsIgnoreCase("") ? statusList + "'"
					+ string + "'" : statusList + ",'" + string + "'");
		}

		if (status.contains("SENT") || status.contains("WRITE OFF")) {
			a = "SENT";
		}
		if (status.contains("RECEIVED")
				|| status.contains("PARTIALLY RECEIVED")) {
			b = "RECEIVED";
		}

		if (a.equalsIgnoreCase("SENT") && b.equalsIgnoreCase("RECEIVED")) {

			dateCondition = " and ((ia.`InvoiceSentDate` >='" + fromDate
					+ "' and ia.`InvoiceSentDate`<= '" + toDate
					+ "') or (ia.`InvoiceAmountReceviedDate` >= '" + fromDate
					+ "' and ia.`InvoiceAmountReceviedDate`<= '" + toDate
					+ "')) ";

			wherecondition += " and (i.InvoiceAmountSentDate= ia.`InvoiceSentDate` or "
					+ "i.InvoiceAmountReceviedDate=ia.`InvoiceAmountReceviedDate`)and  i.`invoiceStatus` = ia.`invoiceStatus`";

			orderBy += " temp.`InvoiceAmountReceviedDate` desc ,temp.`InvoiceSentDate` desc";

		} else if (a.equalsIgnoreCase("SENT")) {
			dateCondition = " and ia.`InvoiceSentDate` >= '" + fromDate
					+ "' and ia.`InvoiceSentDate`<= '" + toDate + "'";

			wherecondition += " and (i.InvoiceAmountSentDate= ia.`InvoiceSentDate`)";

			orderBy += " temp.`InvoiceSentDate` desc";

		} else if (b.equalsIgnoreCase("RECEIVED")) {

			dateCondition = " and ia.`InvoiceAmountReceviedDate` >= '"
					+ fromDate + "' and ia.`InvoiceAmountReceviedDate`<= '"
					+ toDate + "'";

			wherecondition += " and (i.InvoiceAmountReceviedDate=ia.`InvoiceAmountReceviedDate`) and i.`invoiceStatus` = ia.`invoiceStatus`";

			orderBy += " temp.`InvoiceAmountReceviedDate` desc";
		}

		String countryCondition = "";

		if (!invoiceCountry.isEmpty()) {
			countryCondition = " and ia.`Country`= " + "'" + invoiceCountry
					+ "'";
		}

		if (!multiText.equalsIgnoreCase("null")) {
			multiSearchCondition = " and (ia.`INVOICENUMBER` like +'%"
					+ multiText + "%' or ia.`Clientname` like +'%" + multiText
					+ "%'  or ia.`ProjectName` like +'%" + multiText + "%' )";
		}

		if (intrnalOrNot) {
			wherecondition += " and p.INTERNAL_TYPE = 1 ";
		} else {
			wherecondition += " and p.INTERNAL_TYPE != 1 ";
		}

		String sql = "select temp.* from (SELECT ia.* FROM INVOICE_AUDIT ia "
				+ "join INVOICE i ON ia.InvoiceId = i.ID "
				+ "join Milestone m ON i.Milestone = m.MilestoneID "
				+ "join PROJECT p ON m.ID = p.ID "
				+ wherecondition
				+ " and ia.`InvoiceStatus` in ("
				+ statusList
				+ ") "
				+ dateCondition
				+ countryCondition
				+ multiSearchCondition
				+ " and `PersistType`!='Deleted' order by ia.ID desc) as temp where 1 group by temp.`InvoiceId`,temp.InvoiceStatus "
				+ orderBy;

		// logger.warn(sql);

		// This will return Object[]
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);

		// Very Important Line converting object[] to over POJO class
		query.addEntity(InvoiceAudit.class);

		List<InvoiceAudit> audits = query.list();

		Map<String, Object> ListofInvoiceMap = new HashMap<>();
		ListofInvoiceMap.put("InvoiceList", audits);
		return ListofInvoiceMap;
	}

	@Override
	public int getInvoicesCountOf(Long clientId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Invoice.class);
		criteria.createAlias("milsestone", "milsestone");
		criteria.createAlias("milsestone.project", "project");
		criteria.createAlias("project.client", "client");
		criteria.add(Restrictions.eq("client.id", clientId));
		Long longCount = (Long) criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		return longCount.intValue();
	}

	@Override
	public Map<String, Object> getProjectInvoices(Long projectId,
			Integer startIndex, Integer endIndex) {
		Map<String, Object> map = new HashMap<>();
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(Invoice.class);
		criteria.createAlias("milsestone", "milsestone");
		criteria.createAlias("milsestone.project", "project");
		criteria.add(Restrictions.eq("project.id", projectId));
		criteria.addOrder(Order.desc("id"));
		Integer listSize = criteria.list().size();
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(endIndex - startIndex);
		map.put("list", criteria.list());
		map.put("listSize", listSize);
		return map;

	}

	@Override
	public List<Remittance> remitanceList(String currencyType, String bankName) {

		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(Remittance.class);
		criteria.add(Restrictions.and(
				Restrictions.ilike("currencyType", currencyType),
				Restrictions.ilike("bankName", bankName)));
		List<Remittance> remittanceList = HibernateSupressWaringsUtil
				.listAndCast(criteria);
		return remittanceList;
	}

	@Override
	public List<InvoiceAudit> getInvoiceAudit(Long id) {

		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(InvoiceAudit.class)
				.add(Restrictions.eq("invoiceId", id));

		return criteria.list();
	}

	// for getting tax related to country

	@Override
	public Set<TaxTypeLookup> getAllTaxRelatedToCuntry(String country) {
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(TaxTypeLookup.class)
				.add(Restrictions.eq("country", country));
		return new HashSet<TaxTypeLookup>(criteria.list());

	}

	// for getting all taxes independent of country
	@Override
	public Set<TaxTypeLookup> getAllTaxes() {
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(TaxTypeLookup.class);
		return new HashSet<TaxTypeLookup>(criteria.list());
	}

	@Override
	public List<Invoice> getAllInvoicesAmountForProject(Project projectId) {

		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(Invoice.class);
		criteria.createAlias("milsestone", "milsestone");
		// criteria.createAlias("milsestone.project", "project");
		criteria.add(Restrictions.eq("milsestone.project", projectId));
		return criteria.list();
	}

	@Override
	public ProjectNumbers getAmountofCr(ChangeRequest changeRequestid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ProjectNumbers.class);
		criteria.add(Restrictions.eq("changeRequest", changeRequestid));
		ProjectNumbers numbers = (ProjectNumbers) criteria.uniqueResult();

		return numbers;
	}

	// for checking how much percentages of milestone raised excluding current
	// invoice

	// changed type of risedPercentage method from Integer to Double due to
	// milestone percentage field allows decimal values.

	@Override
	public Double risedPercentage(Long id, Long invoiceId) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Invoice.class);
		criteria.createAlias("milsestone", "milsestone");
		criteria.add(Restrictions.eq("milsestone.id", id));
		List<Invoice> invoices = criteria.list();
		Double integer = 0.0;
		// here I am not allowing to add current invoice's percentage as it will
		// create issue in case of edit
		// this method is used in many places here if loop is on basis of
		// respective scenario.
		for (Invoice invoice : invoices) {
			if (invoiceId == null || invoice.getId() != invoiceId) {
				if(invoice.getProformaReferenceNo() == null){
					integer = integer + Double.valueOf(invoice.getPercentage());
				}
				else{
					integer = integer;
				}
				

			}
		}

		return integer;
	}

	public Date dateConverter(String datevalue) {

		try {
			Date date = DateParser.toDate(datevalue);
			return date;
		} catch (ParseException ex) {
			java.util.logging.Logger.getLogger(
					RemittanceDaoImpl.class.getName()).log(Level.SEVERE, null,
					ex);
			return null;
		}

	}

	@Override
	public ProjectNumbers getProjectNumbers(Project project) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ProjectNumbers.class);
		criteria.add(Restrictions.eq("project", project));
		criteria.add(Restrictions.isNull("changeRequest"));
		return (ProjectNumbers) criteria.uniqueResult();
	}

	@Override
	public InvoiceSummary getSummaryOfChangeRequest(Project project,
			Client client, ChangeRequest changeRequest) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InvoiceSummary.class);
		criteria.add(Restrictions.eq("project", project))
				.add(Restrictions.eq("client", client))
				.add(Restrictions.eq("changeRequest", changeRequest));
		return (InvoiceSummary) criteria.uniqueResult();
	}

	@Override
	public InvoiceSummary getInvoiceSummary(Project project, Client client) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InvoiceSummary.class);
		criteria.add(Restrictions.eq("project", project))
				.add(Restrictions.eq("client", client))
				.add(Restrictions.isNull("changeRequest"));

		return (InvoiceSummary) criteria.uniqueResult();

	}

	@Override
	public List<Project> getAllProjects() {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(Project.class)
				.add(Restrictions.ne("status", ProjectStatus.NEW));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<InvoiceSummary> getInvoiceSummaryList(Project project,
			Client client) {
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(InvoiceSummary.class);
		if (project != null) {
			criteria.add(Restrictions.eq("project", project));
		}
		if (client != null) {
			criteria.add(Restrictions.eq("client", client));
		}

		return criteria.list();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Invoice> getInvoicesInCollection(List<Long> collection) {
		return sessionFactory.getCurrentSession().createCriteria(Invoice.class)
				.add(Restrictions.in("id", collection)).list();
	}

	@Override
	public List<Project> getFixedAndRetainerTypeProjects() {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(Project.class)
				.add(Restrictions.ne("status", ProjectStatus.NEW));
		Criterion criterion = Restrictions.or(
				Restrictions.eq("type", ProjectType.FIXEDBID),
				Restrictions.eq("type", ProjectType.RETAINER));
		criteria.add(criterion);
		criteria.addOrder(Order.desc("id"));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<Invoice> getinvoiceForMilestone(Milestone milestone) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Invoice.class);
		criteria.add(Restrictions.eq("milsestone", milestone));
		return criteria.list();
	}

	@Override
	public List<Milestone> getclosedBillableMilestoneOfProject(Project project) {
		logger.warn("------------Getting Project Milestone Details------------ ");
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(Milestone.class)
				.add(Restrictions.eq("project", project))
				.add(Restrictions.eq("billable", Boolean.TRUE))
				.add(Restrictions.eq("closed", Boolean.TRUE))
				.add(Restrictions.isNull("changeRequest"));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<ChangeRequest> getChangeRequestOfProject(Long projectId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ChangeRequest.class);
		criteria.add(Restrictions.eq("projectId", projectId));
		return criteria.list();
	}

	@Override
	public List<Milestone> getMilestoneOfCR(ChangeRequest changeRequest) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Milestone.class);
		criteria.add(Restrictions.eq("changeRequest", changeRequest));
		return criteria.list();
	}

	@Override
	public String getLatestInvoiceNumberForClient(Client client, String country) {

		// String sql =
		// "select n.referenceNumber from Invoice n where n.referenceNumber"
		// +
		// " in(select max(cast(i.referenceNumber as int )) as maximumVal from Invoice i"
		// + "    join i.milsestone m"
		// + " join m.project p "
		// + " where p.client= " + client.getId() + ")";

		/*
		 * String sql = "select i.referenceNumber from Invoice i " +
		 * "join i.milsestone m " + "join m.project p where p.client= " +
		 * client.getId() + " order by cast(i.referenceNumber as int ) desc";
		 */

		// here invoice latest number is based on country.
		// 1. if country is india one series of numbers will come.
		// 2.if country is other than the india one series of nubers will come.

		String sql = "select i.referenceNumber from Invoice i "
				+ "join i.milsestone m " + "join m.project p where p.client= "
				+ client.getId()
				+ " order by cast(i.referenceNumber as int ) desc";

		Query query = sessionFactory.getCurrentSession().createQuery(sql)
				.setMaxResults(1);
		return (String) query.uniqueResult();

		/*
		 * Query query = sessionFactory.getCurrentSession().createQuery(sql)
		 * .setMaxResults(1);
		 */

		
	}

	@Override
	public Boolean checkInvoiceNumber(Client client, String referenceNumber ,String country) {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Invoice.class);
		criteria.createAlias("milsestone", "milsestone");
		criteria.createAlias("milsestone.project", "project");
		criteria.createAlias("project.client", "client");
		criteria.add(Restrictions.eq("client.id", client.getId()));
		criteria.add(Restrictions.eq("referenceNumber", referenceNumber));
		/*
		 * criteria.add(Restrictions.ilike("referenceNumber", referenceNumber,
		 * MatchMode.EXACT));
		 */
		Invoice invoice = (Invoice) criteria.uniqueResult();
		return (invoice != null) ? (Boolean.TRUE) : (Boolean.FALSE);
		
		
		/*
		if(country.equalsIgnoreCase("INDIA")){
		criteria.add(Restrictions.and(
				Restrictions.ilike("country", "INDIA", MatchMode.ANYWHERE),
				Restrictions.eq("invoiceNumberFlag", true)));

		criteria.add(Restrictions.eq("referenceNumber", referenceNumber));
		}else{
			criteria.add(Restrictions.and(
					Restrictions.ne("country", "INDIA"),
					Restrictions.eq("invoiceNumberFlag", true)));

			criteria.add(Restrictions.eq("referenceNumber", referenceNumber));
		}

		Invoice invoice = (Invoice) criteria.uniqueResult();
		return (invoice != null) ? (Boolean.TRUE) : (Boolean.FALSE);*/
		
	}
	
	
	
	@Override
	public Boolean checkInvoiceNumberExits( String invoiceReferenceNumber ,String country, String proformaInvoiceFlag) {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Invoice.class);
		if(proformaInvoiceFlag.equalsIgnoreCase("true")){
			if(country.equalsIgnoreCase("INDIA")){
				criteria.add(Restrictions.and(
						Restrictions.ilike("country", "INDIA", MatchMode.ANYWHERE),
						Restrictions.eq("invoiceNumberFlag", true)));
				criteria.add(Restrictions.eq("proformaInvoiceFlag", Boolean.TRUE));
				criteria.add(Restrictions.eq("invoiceReferenceNumber", invoiceReferenceNumber));
				}else{
					criteria.add(Restrictions.and(
							Restrictions.ne("country", "INDIA"),
							Restrictions.eq("invoiceNumberFlag", true)));
					criteria.add(Restrictions.eq("proformaInvoiceFlag", Boolean.TRUE));
					criteria.add(Restrictions.eq("invoiceReferenceNumber", invoiceReferenceNumber));
					
				}

				Invoice invoice = (Invoice) criteria.uniqueResult();
				return (invoice != null) ? (Boolean.TRUE) : (Boolean.FALSE);
		}
		else{
			if(country.equalsIgnoreCase("INDIA")){
				criteria.add(Restrictions.and(
						Restrictions.ilike("country", "INDIA", MatchMode.ANYWHERE),
						Restrictions.eq("invoiceNumberFlag", true)));
				criteria.add(Restrictions.eq("invoiceReferenceNumber", invoiceReferenceNumber));
				}else{
					criteria.add(Restrictions.and(
							Restrictions.ne("country", "INDIA"),
							Restrictions.eq("invoiceNumberFlag", true)));
					criteria.add(Restrictions.eq("invoiceReferenceNumber", invoiceReferenceNumber));
					
				}

				Invoice invoice = (Invoice) criteria.uniqueResult();
				return (invoice != null) ? (Boolean.TRUE) : (Boolean.FALSE);
		}
		
		
	}

	// Used in Invoice Quartz
	@Override
	public List<Invoice> checkInvoiceStatus() {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Invoice.class);
		criteria.add(Restrictions.le("dueDate", new Date()));
		criteria.add(Restrictions.in("invoiceStatus",
				Arrays.asList("RAISED", "SENT", "NOT SENT")));


		return HibernateSupressWaringsUtil.listAndCast(criteria);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getProjectsFor(Employee loggedEmployee,
			List<Long> managerList) {

		// Getting projects allocated to LoggedIn Employee
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AllocationDetails.class);
		criteria.createAlias("project", "project").add(
				Restrictions.eq("isAllocated", Boolean.TRUE));
		criteria.add(Restrictions.eq("employee", loggedEmployee));
		List<Long> projectIds = criteria.setProjection(
				Projections.groupProperty("project.id")).list();

		// Getting projects of LoggedIn Employee Reportee's
		Criteria projectCriteria = sessionFactory.getCurrentSession()
				.createCriteria(Project.class)
				.add(Restrictions.in("projectManager.employeeId", managerList))
				.setProjection(Projections.groupProperty("id"));

		projectIds.addAll(projectCriteria.list());

		return projectIds;

	}

	@Override
	public Long getInvoiceTrackerVersion(Long invoiceId) {
		Long size = (long) sessionFactory.getCurrentSession()
				.createCriteria(InvoiceTracker.class)
				.add(Restrictions.eq("invoiceId", invoiceId)).list().size();
		return size + 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InvoiceTracker> getInvoiceTrackers(Long invoiceId) {

		// select invoiceId,version//
		String sql = "from InvoiceTracker where invoiceId=" + invoiceId;
		return sessionFactory.getCurrentSession().createQuery(sql).list();
	}

	@Override
	public List<InvoiceReminderLog> getReminderLogs(String invoiceId) {

		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(InvoiceReminderLog.class)
				.add(Restrictions.ilike("invoiceId", invoiceId));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Invoice> getInvoicesRaisedBetween(Date fromDate, Date toDate) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Invoice.class);
		criteria.createAlias("milsestone", "milestone");
		criteria.add(Restrictions.between("invoiceDate", fromDate, toDate));
		criteria.addOrder(Order.asc("milestone.project"));
		return criteria.list();
	}

	@Override
	public InvoiceTracker latestInvoiceVersion(Long invoiceId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InvoiceTracker.class);
		criteria.add(Restrictions.eq("invoiceId", invoiceId));
		criteria.addOrder(Order.desc("id"));

		criteria.setMaxResults(1);
		InvoiceTracker tracker = (InvoiceTracker) criteria.uniqueResult();

		return tracker;

	}

	@Override
	public void addAddressDetails(CountryAddress dto) {
		getSessionFactory().getCurrentSession().save(dto);

	}

	@Override
	public List<CountryAddress> getAddressDetailsList() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				CountryAddress.class);
		return criteria.list();
	}

	@Override
	public Map<String, Object> invoiceAuditLog(Date fromDate, Date toDate,
			Integer firstIndex, Integer endIndex) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InvoiceAudit.class);

		Second startSecondDate = new Second(YearOfEra.valueOf(fromDate
				.getYearOfEra().getValue()), MonthOfYear.valueOf(fromDate
				.getMonthOfYear().getValue()), DayOfMonth.valueOf(fromDate
				.getDayOfMonth().getValue()), HourOfDay.valueOf(0),
				MinuteOfHour.valueOf(0), SecondOfMinute.valueOf(0));

		Second endSecondDate = new Second(YearOfEra.valueOf(toDate
				.getYearOfEra().getValue()), MonthOfYear.valueOf(toDate
				.getMonthOfYear().getValue()), DayOfMonth.valueOf(toDate
				.getDayOfMonth().getValue()), HourOfDay.valueOf(23),
				MinuteOfHour.valueOf(59), SecondOfMinute.valueOf(59));

		criteria.add(Restrictions.between("modifiedDate", startSecondDate,
				endSecondDate));
		criteria.addOrder(Order.desc("modifiedDate"));

		Map<String, Object> listOfInvoiceMap = getPaginationList(criteria,
				firstIndex, endIndex);

		return listOfInvoiceMap;
	}

	@Override
	public String getLatestInvoiceNumberForCountry(String country) {
		
		
		String sql = "";
		if (country.equalsIgnoreCase("INDIA")) {
			sql = "select invoiceReferenceNumber from Invoice " + " where country = "
					+ "'INDIA'" + " and invoiceNumberFlag  = " + 1
					+ " order by cast(invoiceReferenceNumber as int ) desc";
		} else {
			sql = "select invoiceReferenceNumber from Invoice " + " where country != "
					+ "'INDIA'" + " and invoiceNumberFlag  = " + 1
					+ " order by cast(invoiceReferenceNumber as int ) desc";
		}


		Query query = sessionFactory.getCurrentSession().createQuery(sql)
				.setMaxResults(1);

		return (String) query.uniqueResult();
		
	}

	@Override
	public List<Invoice> getInvoicesUnderMilestone(Long projectId) {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Invoice.class);
		criteria.createAlias("milsestone", "milsestone");
		criteria.createAlias("milsestone.project", "project");
		criteria.add(Restrictions.eq("project.id", projectId));
		
		return criteria.list();
	}

	@Override
	public List<Invoice> getRaisedInvoices(Long milestoneId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Invoice.class);
		criteria.createAlias("milsestone", "milsestone");
		criteria.add(Restrictions.eq("milsestone.id", milestoneId));
		return criteria.list();
	}

	@Override
	public Boolean checkMilestoneExits(Long milestoneid) {
	
		Criteria criteria =sessionFactory.getCurrentSession().createCriteria(Invoice.class);
		criteria.createAlias("milsestone", "milsestone");
		criteria.add(Restrictions.eq("milsestone.id", milestoneid));
		
		//logger.warn(criteria.list());
	//Invoice invoice = (Invoice) criteria.uniqueResult();
		//return (invoice != null) ? (Boolean.TRUE) : (Boolean.FALSE);
		Boolean invoiceFlag;
		if(criteria.list().size() > 0){
			invoiceFlag = Boolean.TRUE;
		}else{
			invoiceFlag = Boolean.FALSE;
		}
		
		return invoiceFlag;
		
	}

	@Override
	public Map<String, Object> getClientsAuditReport(String fromDate,
			String toDate,String multiText ,String selectionStatus ,Boolean pendingAmountFlag) {
		
		String wherecondition = "Where 1 ";
		String dateCondition = "";
		String multiSearchCondition = "";
		String orderBy = " order by ";
		
		dateCondition = " and ((ia.`InvoiceSentDate` >='" + fromDate
				+ "' and ia.`InvoiceSentDate`<= '" + toDate
				+ "') or (ia.`InvoiceAmountReceviedDate` >= '" + fromDate
				+ "' and ia.`InvoiceAmountReceviedDate`<= '" + toDate
				+ "')) ";

		wherecondition += " and (i.InvoiceAmountSentDate= ia.`InvoiceSentDate` or "
				+ "i.InvoiceAmountReceviedDate=ia.`InvoiceAmountReceviedDate`)and  i.`invoiceStatus` = ia.`invoiceStatus` ";
		
		if (!multiText.equalsIgnoreCase("null")) {
			multiSearchCondition = " and ( ia.`Clientname` like +'%" + multiText
					+ "%' )";
		}
		
		if(selectionStatus.equalsIgnoreCase("Active")){
			wherecondition  += " and  c.Client_Status = " + 1 ; 
		}else if(selectionStatus.equalsIgnoreCase("InActive")){
			wherecondition  += " and  c.Client_Status = " + 0;
		}
		
		if(pendingAmountFlag == Boolean.TRUE){
			wherecondition  += " and ia.InvoiceStatus != 'RECEIVED' " ;
		}


		orderBy += " temp.`InvoiceAmountReceviedDate` desc ,temp.`InvoiceSentDate` desc";
		
		
		String sql = "select temp.* from (SELECT ia.* FROM INVOICE_AUDIT ia "
				+ "join INVOICE i ON ia.InvoiceId = i.ID "
				+ "join Milestone m ON i.Milestone = m.MilestoneID "
				+ "join PROJECT p ON m.ID = p.ID "
				+ "join Client c ON p.Client = c.ID "
				+ wherecondition
				+ dateCondition
				+ multiSearchCondition
				+ " and `PersistType`!='Deleted' order by ia.ID desc) as temp where 1 group by temp.`InvoiceId`,temp.InvoiceStatus "
				+ orderBy;
		
		
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		
		//logger.warn("query"+   query);
		

		// Very Important Line converting object[] to over POJO class
		query.addEntity(InvoiceAudit.class);

		List<InvoiceAudit> audits = query.list();

		Map<String, Object> ListofInvoiceMap = new HashMap<>();
		ListofInvoiceMap.put("InvoiceList", audits);
		return ListofInvoiceMap;


		
	}

	@Override
	public Map<String, Object> getMonthlyAuditReport(String fromDate,
			String toDate, String invoiceStatus ,String country) {
		
		
		String wherecondition = "Where 1 ";
		String a = "";
		String b = "";
		String dateCondition = "";
		String orderBy = " order by ";
		String statuses = "";
		
		List<String> statusList = new ArrayList<String>();
		
		statusList.add("SENT");
		statusList.add("WRITE OFF");
		statusList.add("RECEIVED");
		statusList.add("PARTIALLY RECEIVED");
		
		

		for (String string : statusList) {
			statuses = (statuses.equalsIgnoreCase("") ? statuses + "'"
					+ string + "'" : statuses + ",'" + string + "'");
		}
		
		if (invoiceStatus.contains("SENT") || invoiceStatus.contains("WRITE OFF")) {
			a = "SENT";
		}
		if (invoiceStatus.contains("RECEIVED")
				|| invoiceStatus.contains("PARTIALLY RECEIVED")) {
			b = "RECEIVED";
		}
		
		if (a.equalsIgnoreCase("SENT") && b.equalsIgnoreCase("RECEIVED")) {

			dateCondition = " and ((ia.`InvoiceSentDate` >='" + fromDate
					+ "' and ia.`InvoiceSentDate`<= '" + toDate
					+ "') or (ia.`InvoiceAmountReceviedDate` >= '" + fromDate
					+ "' and ia.`InvoiceAmountReceviedDate`<= '" + toDate
					+ "')) ";

			wherecondition += " and (i.InvoiceAmountSentDate= ia.`InvoiceSentDate` or "
					+ "i.InvoiceAmountReceviedDate=ia.`InvoiceAmountReceviedDate`)and  i.`invoiceStatus` = ia.`invoiceStatus`";

			orderBy += " temp.`InvoiceAmountReceviedDate` desc ,temp.`InvoiceSentDate` desc";

		} else if (a.equalsIgnoreCase("SENT")) {
			logger.warn("in sent ");
			dateCondition = " and ia.`InvoiceSentDate` >= '" + fromDate
					+ "' and ia.`InvoiceSentDate`<= '" + toDate + "'";

			wherecondition += " and (i.InvoiceAmountSentDate= ia.`InvoiceSentDate`)";

			orderBy += " temp.`InvoiceSentDate` desc";

		} else if (b.equalsIgnoreCase("RECEIVED")) {
			logger.warn("in else");

			dateCondition = " and ia.`InvoiceAmountReceviedDate` >= '"
					+ fromDate + "' and ia.`InvoiceAmountReceviedDate`<= '"
					+ toDate + "'";

			wherecondition += " and (i.InvoiceAmountReceviedDate=ia.`InvoiceAmountReceviedDate`) and i.`invoiceStatus` = ia.`invoiceStatus`";

			orderBy += " temp.`InvoiceAmountReceviedDate` desc";
		}
		
		String countryCondition = "";

		if (!country.isEmpty()) {
			countryCondition = " and ia.`Country`= " + "'" + country
					+ "'";
		}

	
		String sql = "select temp.* from (SELECT ia.* FROM INVOICE_AUDIT ia "
				+ "join INVOICE i ON ia.InvoiceId = i.ID "
				+ "join Milestone m ON i.Milestone = m.MilestoneID "
				+ "join PROJECT p ON m.ID = p.ID "
				+ wherecondition
				+ " and ia.`InvoiceStatus` = "
				+ "'" +invoiceStatus +"'"
				+ dateCondition
				+ countryCondition
				+ " and `PersistType`!='Deleted' order by ia.ID desc) as temp where 1 group by temp.`InvoiceId`,temp.InvoiceStatus "
				+ orderBy;
		
		
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		
		//logger.warn("query"+   query);
		// Very Important Line converting object[] to over POJO class
		query.addEntity(InvoiceAudit.class);

		List<InvoiceAudit> audits = query.list();

		Map<String, Object> ListofInvoiceMap = new HashMap<>();
		ListofInvoiceMap.put("InvoiceList", audits);
		return ListofInvoiceMap;
		
	}

	@Override
	public Map<String, Object> getClientsProjectAuditReport(Long clientId,
			String formateddStringstartdate, String formattedEnddate ,Boolean pendingAmountFlag) {
		
		
		String wherecondition = "Where 1 ";
		String dateCondition = "";
		String orderBy = " order by ";
		
		dateCondition = " and ((ia.`InvoiceSentDate` >='" + formateddStringstartdate
				+ "' and ia.`InvoiceSentDate`<= '" + formattedEnddate
				+ "') or (ia.`InvoiceAmountReceviedDate` >= '" + formateddStringstartdate
				+ "' and ia.`InvoiceAmountReceviedDate`<= '" + formattedEnddate
				+ "')) ";

		wherecondition += " and (i.InvoiceAmountSentDate= ia.`InvoiceSentDate` or "
				+ "i.InvoiceAmountReceviedDate=ia.`InvoiceAmountReceviedDate`)and  i.`invoiceStatus` = ia.`invoiceStatus` ";
		
		if(pendingAmountFlag == Boolean.TRUE){
			wherecondition  += " and ia.InvoiceStatus != 'RECEIVED' " ;
		}
		
		orderBy += " temp.`InvoiceAmountReceviedDate` desc ,temp.`InvoiceSentDate` desc";
		
		
		String sql = "select temp.* from (SELECT ia.* FROM INVOICE_AUDIT ia "
				+ "join INVOICE i ON ia.InvoiceId = i.ID "
				+ "join Milestone m ON i.Milestone = m.MilestoneID "
				+ "join PROJECT p ON m.ID = p.ID "
				+ "join Client c ON p.Client = c.ID "
				+ wherecondition
				+ dateCondition
				+ " and c.ID = " + clientId
				+ " and `PersistType`!='Deleted' order by ia.ID desc) as temp where 1 group by temp.`InvoiceId`,temp.InvoiceStatus "
				+ orderBy;
		
		
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		
		//logger.warn("query"+   query);
		// Very Important Line converting object[] to over POJO class
		query.addEntity(InvoiceAudit.class);

		List<InvoiceAudit> audits = query.list();

		Map<String, Object> ListofInvoiceMap = new HashMap<>();
		ListofInvoiceMap.put("InvoiceList", audits);
		return ListofInvoiceMap;
		
	}

	@Override
	public Map<String, Object> getClientProjectInvoice(Long projectId,
			String formateddStringstartdate, String formattedEnddate ,Boolean pendingAmountFlag) {
		String wherecondition = "Where 1 ";
		String dateCondition = "";
		String orderBy = " order by ";
		
		dateCondition = " and ((ia.`InvoiceSentDate` >='" + formateddStringstartdate
				+ "' and ia.`InvoiceSentDate`<= '" + formattedEnddate
				+ "') or (ia.`InvoiceAmountReceviedDate` >= '" + formateddStringstartdate
				+ "' and ia.`InvoiceAmountReceviedDate`<= '" + formattedEnddate
				+ "')) ";

		wherecondition += " and (i.InvoiceAmountSentDate= ia.`InvoiceSentDate` or "
				+ "i.InvoiceAmountReceviedDate=ia.`InvoiceAmountReceviedDate`)and  i.`invoiceStatus` = ia.`invoiceStatus` ";
		
		
		if(pendingAmountFlag == Boolean.TRUE){
			wherecondition  += " and ia.InvoiceStatus != 'RECEIVED' " ;
		}
		
		orderBy += " temp.`InvoiceAmountReceviedDate` desc ,temp.`InvoiceSentDate` desc";
		
		
		String sql = "select temp.* from (SELECT ia.* FROM INVOICE_AUDIT ia "
				+ "join INVOICE i ON ia.InvoiceId = i.ID "
				+ "join Milestone m ON i.Milestone = m.MilestoneID "
				+ "join PROJECT p ON m.ID = p.ID "
				+ "join Client c ON p.Client = c.ID "
				+ wherecondition
				+ dateCondition
				+ " and p.ID = " + projectId
				+ " and `PersistType`!='Deleted' order by ia.ID desc) as temp where 1 group by temp.`InvoiceId`,temp.InvoiceStatus "
				+ orderBy;
		
		
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		
		//logger.warn("query"+   query);

		// Very Important Line converting object[] to over POJO class
		query.addEntity(InvoiceAudit.class);

		List<InvoiceAudit> audits = query.list();

		Map<String, Object> ListofInvoiceMap = new HashMap<>();
		ListofInvoiceMap.put("InvoiceList", audits);
		return ListofInvoiceMap;
		
		
	}

	@Override
	public String getInvoicePattern() {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Pattern.class);
		criteria.add(Restrictions.eq("type", "Invoice"));
		
		List<Pattern>  patternList = criteria.list();
		
		String  specificPattern = null;
		
		for(Pattern patt :patternList){
			if(patt.getType().equals("Invoice")){
		    specificPattern = patt.getSpecificPattern();
			}
		}
		
		return specificPattern;
				
	}
	
	@Override
	public String getLatestProformaInvoice(String country) {
		String sql = "";
		if (country.equalsIgnoreCase("INDIA")) {
			sql = "select invoiceReferenceNumber from Invoice " + " where country = "
					+ "'INDIA'" + " and invoiceNumberFlag  = " + 1 + " and proformaInvoiceFlag = " + 1
					+ " order by cast(invoiceReferenceNumber as int ) desc";
		} else {
			sql = "select invoiceReferenceNumber from Invoice " + " where country != "
					+ "'INDIA'" + " and invoiceNumberFlag  = " + 1 + " and proformaInvoiceFlag = " + 1
					+ " order by cast(invoiceReferenceNumber as int ) desc";
		}


		Query query = sessionFactory.getCurrentSession().createQuery(sql)
				.setMaxResults(1);

		return (String) query.uniqueResult();
		
	}
	
	
	@Override
	public Map<String, Object> getListOfInvoice(String projectType,
			String invoiceType, List<String> status, String fromDate,
			String toDate, String multiText, Integer fromIndex,
			Integer toIndex, String datePeriod, Set<Long> projectids,
			String invoiceCountry, Boolean intrnalOrNot) {


		// Session session = sessionFactory.getCurrentSession();
		Criteria invoicecriteria = sessionFactory.getCurrentSession()
				.createCriteria(Invoice.class);

		// Criteria invoicecriteria = session.createCriteria(Invoice.class);
		invoicecriteria.createAlias("milsestone", "milestone");
		invoicecriteria.createAlias("milestone.project", "project");
		invoicecriteria.createAlias("project.client", "client");

		/*
		 * invoicecriteria.setFetchMode("milestone", FetchMode.JOIN);
		 * invoicecriteria.setFetchMode("project", FetchMode.JOIN);
		 * invoicecriteria.setFetchMode("client", FetchMode.JOIN);
		 */

		invoicecriteria.addOrder(Order.desc("id"));

		/*
		 * if (!projectids.isEmpty())
		 * invoicecriteria.add(Restrictions.in("project.id", projectids));
		 */

		if (!projectType.equalsIgnoreCase("ALL"))
			invoicecriteria.add(Restrictions.eq("project.type",
					ProjectType.valueOf(projectType)));
		
		if(invoiceType.equalsIgnoreCase("PROFORMA"))
			invoicecriteria.add(Restrictions.eq("proformaInvoiceFlag",Boolean.TRUE));
		else if(invoiceType.equalsIgnoreCase("INVOICE"))
			invoicecriteria.add(Restrictions.eq("proformaInvoiceFlag",Boolean.FALSE));

		if (!invoiceCountry.equalsIgnoreCase("null"))
			invoicecriteria.add(Restrictions.ilike("country", invoiceCountry,
					MatchMode.ANYWHERE));

		if (!status.isEmpty())
			invoicecriteria.add(Restrictions.in("invoiceStatus", status));

		if (!multiText.equalsIgnoreCase("null")
				&& !NumberFormateUtil.isDouble(multiText)) {

			Criterion number = Restrictions.ilike("number", multiText,
					MatchMode.ANYWHERE);

			Criterion clientName = Restrictions.ilike("client.name", multiText,
					MatchMode.ANYWHERE);

			Criterion personName = Restrictions.ilike("client.personName",
					multiText, MatchMode.ANYWHERE);

			Criterion projectName = Restrictions.ilike("project.projectName",
					multiText, MatchMode.ANYWHERE);

			Criterion criterion1 = Restrictions.or(number, clientName);

			Criterion criterion2 = Restrictions.or(personName, projectName);

			invoicecriteria.add(Restrictions.or(criterion1, criterion2));

		}

		Date startDate = null;
		Date endDate = null;

		if (!datePeriod.equalsIgnoreCase("Select Date")) {

			if (datePeriod.equalsIgnoreCase("custom")) {
				startDate = dateConverter(fromDate);
				endDate = dateConverter(toDate);
			} else {
				Map<String, Date> parsedDates = getCustomDates(datePeriod);
				startDate = parsedDates.get("startDate");
				endDate = parsedDates.get("endDate");

			}

			Criterion receivedAmountCriterion = Restrictions.between(
					"invoiceAmountReceviedDate", startDate, endDate);
			Criterion sentAmountCriterion = Restrictions.between(
					"invoiceAmountSentDate", startDate, endDate);

			invoicecriteria.add(Restrictions.or(sentAmountCriterion,
					receivedAmountCriterion));

		}

		/*
		 * if(intrnalOrNot){
		 * invoicecriteria.add(Restrictions.eq("project.internalOrNot",
		 * Boolean.TRUE)); }else {
		 * invoicecriteria.add(Restrictions.eq("project.internalOrNot",
		 * Boolean.FALSE)); }
		 */

		invoicecriteria.add(Restrictions.eq("project.internalOrNot",
				intrnalOrNot));

		// Map<String, Object> ListofInvoiceMap = new HashMap<>();

		// ListofInvoiceMap.put("InvoiceListSize",invoicecriteria.list().size());

		// for exporting from index and end index both are null because of we
		// are exporting what we filtered.
		/*
		 * if (fromIndex != null && toIndex != null) {
		 * invoicecriteria.setFirstResult(fromIndex);
		 * invoicecriteria.setMaxResults(toIndex - fromIndex); }
		 */

		// for pagination performance
		Map<String, Object> listOfInvoiceMap = getPaginationList(
				invoicecriteria, fromIndex, toIndex);
		// Object list = listOfInvoiceMap.get("list");
		listOfInvoiceMap.put("InvoiceListSize",
				listOfInvoiceMap.remove("listSize"));
		listOfInvoiceMap.put("InvoiceList", listOfInvoiceMap.remove("list"));
		return listOfInvoiceMap;

		/*
		 * Map<String, Object> listOfInvoiceMap =
		 * getPaginationList(invoicecriteria, fromIndex, toIndex);
		 * 
		 * Map<String, Object> ListofInvoiceMap = new HashMap<>();
		 * logger.warn(listOfInvoiceMap.get("listSize"));
		 * ListofInvoiceMap.put("InvoiceList",listOfInvoiceMap.get("list"));
		 * ListofInvoiceMap.put("InvoiceListSize",
		 * listOfInvoiceMap.get("listSize"));
		 * logger.warn(listOfInvoiceMap.get("listSize"));
		 * //ListofInvoiceMap.put("InvoiceList", invoicecriteria.list()); return
		 * ListofInvoiceMap;
		 */

	
	}
	
	@Override
	public List<Invoice> getInvoiceRefrence(long invoiceId) {
		Criteria invoicecriteria = sessionFactory.getCurrentSession()
				.createCriteria(Invoice.class);
		invoicecriteria.createAlias("proformaReferenceNo", "proformaReferenceNo");
		invoicecriteria.add(Restrictions.eq("proformaReferenceNo.id", invoiceId));
		return invoicecriteria.list();
	}
	

	
	@Override
	public List<InvoiceAudit> getRefrenceInvoice(Long invoiceNum) {
		Criteria invoicecriteria = sessionFactory.getCurrentSession()
				.createCriteria(InvoiceAudit.class);
		invoicecriteria.createAlias("proformaReferenceNo", "proformaReferenceNo");
		invoicecriteria.add(Restrictions.eq("proformaReferenceNo.id", invoiceNum));
		return invoicecriteria.list();
	}

	

}
