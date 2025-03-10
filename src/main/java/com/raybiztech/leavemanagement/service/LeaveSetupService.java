package com.raybiztech.leavemanagement.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.leavemanagement.dto.LeaveCategoryDTO;
import com.raybiztech.leavemanagement.dto.LeaveSettingsDTO;
import com.raybiztech.leavemanagement.exceptions.CannotDeleteLeaveCategoryException;
import com.raybiztech.leavemanagement.exceptions.LeaveCategoryAlreadyCreatedException;

public interface LeaveSetupService {

    /**
     * this method will returns the leave summaries of all employees for a
     * financial year
     *
     * @param currentYear
     * @param startIndex
     * @param endIndex
     * @return
     */
    Map<String, Object> getAllEmployeesLeaveSummary(Integer currentYear,
            Integer startIndex, Integer endIndex);

    /**
     * this method will add leave type by management.
     *
     * @param leaveCategoryDTO
     */
    void addLeaveCategory(LeaveCategoryDTO leaveCategoryDTO)
            throws LeaveCategoryAlreadyCreatedException;

    /**
     * updates the leaves settings
     *
     * @param leaveSettingsDTO
     */
    void updateLeaveCalendarSettings(LeaveSettingsDTO leaveSettingsDTO);

    SortedSet<LeaveCategoryDTO> getAllLeaveCategories();

    void deleteLeaveCategory(Long id) throws CannotDeleteLeaveCategoryException;

    void editLeaveCategory(LeaveCategoryDTO leaveCategoryDTO);

    LeaveSettingsDTO getLeaveCalendarSetting();

    List<Integer> getAllCreditedYears();

    Set<EmployeeDTO> getAllEmployeesOfCompany();

    LeaveCategoryDTO getleaveCategory(Long leaveCategoryId);

    Integer getCurrentFinancialYear();

    SortedSet<LeaveCategoryDTO> getlopCategories();

    Map<String, Object> searchLeaveSummaries(Integer financialYear, String search, Integer startIndex, Integer endIndex);

    SortedSet<LeaveCategoryDTO> getEligibleLeaveCategories(Long employeeId);
    
    ByteArrayOutputStream exportLeavereport(Integer financialYear, Integer startIndex, Integer endIndex, String search ,HttpServletResponse response)
			throws IOException;
}
