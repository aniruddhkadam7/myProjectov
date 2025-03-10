package com.raybiztech.itdeclaration.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.exceptions.DuplicateActiveCycleException;
import com.raybiztech.itdeclaration.business.FinanceCycle;
import com.raybiztech.itdeclaration.business.ITDeclarationForm;
import com.raybiztech.itdeclaration.business.Investment;
import com.raybiztech.itdeclaration.business.Section;
import com.raybiztech.itdeclaration.exception.DuplicateITDeclareForm;
import com.raybiztech.itdeclaration.exception.NoFinanceCycle;

public interface ITDeclarationDao extends DAO {
	// Defining various methods DB Operations
	void addCycle(FinanceCycle financeCycle);

	void addSection(Section section);

	void editSection(Section section);

	void deleteSection(Long sectionId);

	List<FinanceCycle> getCycles();

	void deleteCycle(Long cycleId);

	void editCycle(FinanceCycle financeCycle);

	List<Investment> getInvestsBySecId(Long sectionId);

	FinanceCycle activeFinanceCycle();

	Long addITDeclareForm(ITDeclarationForm itDeclarationForm)
			throws DuplicateITDeclareForm, NoFinanceCycle;

	ITDeclarationForm isItDeclareFormExist(Long employeeId)
			throws NoFinanceCycle;

	Section sectionFindByNameOrId(String sectionName, Long sectionId);

	Investment investByNameOrId(Long sectionId, String investmentName,
			Long investmentId);

	FinanceCycle cycleFindByNameOrId(String cycleName, Long cycleId);

	FinanceCycle activeOtherThanThis(Long cycleId);

	Map<String, Object> getITDeclarationPaginationList(Integer startIndex,
			Integer endIndex, Long cycleId, String employeeName);

	Map<String, Object> getITDeclarationPaginationListIndividual(
			Integer startIndex, Integer endIndex, Long cycleId, Long employeeId);

	List<Section> getSectionsHavingInvests();

	void deleteNullItFormSec();
}
