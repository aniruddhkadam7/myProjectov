package com.raybiztech.appraisals.PIPManagement.dao;

import java.util.List;
import java.util.Map;

import com.raybiztech.appraisals.PIPManagement.business.PIP;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.date.DateRange;

/**
 *
 * @author aprajita
 */
public interface PIPDao extends DAO {

	/* get all PIP List with pagination for admin */
	Map<String, Object> getAllPIPList(Integer startIndex, Integer endIndex,
			String multiSearch, String searchByEmployee, String searchByAdded,
			DateRange dateRange, String selectionStatus);

	/* get PIP List with pagination for manager */
	Map<String, Object> getAllHierarchyPIPList(List<Long> empId,
			Integer startIndex, Integer endIndex, String multiSearch,
			String searchByEmployee, String searchByAdded, DateRange dateRange,
			String selectionStatus);

	/* get all PIP List with pagination for individual */
	Map<String, Object> getIndividualPIPList(Long empId, Integer startIndex,
			Integer endIndex);

	/* to check the duplicate PIP detail for given time period */
	Boolean checkForDuplicatePIP(PIP pip, DateRange dateRange);

}
