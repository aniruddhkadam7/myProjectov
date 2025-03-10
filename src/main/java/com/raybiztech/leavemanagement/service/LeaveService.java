package com.raybiztech.leavemanagement.service;

import java.util.List;
import java.util.Map;

import com.raybiztech.date.DateRange;
import com.raybiztech.leavemanagement.dto.LeaveApplicationDTO;
import com.raybiztech.leavemanagement.dto.LeaveSummaryDTO;
import com.raybiztech.leavemanagement.dto.SearchCriteriaDTO;
import com.raybiztech.leavemanagement.exceptions.LeaveCannotProcessException;
import com.raybiztech.leavemanagement.exceptions.LeaveNotFoundException;

public interface LeaveService {

    /**
     * This method is used to apply for a leave
     *
     * @param leaveDTO
     */
    void applyLeave(LeaveApplicationDTO leaveDTO)
            throws LeaveCannotProcessException;

    /**
     * This method is used to approve a leave
     *
     * @param id
     * @param adminComments
     */
    void approveLeave(Long id, String adminComments)
            throws LeaveNotFoundException;

    /**
     * This method is used to cancel a leave
     *
     * @param id
     */
    void cancelLeave(Long id) throws LeaveNotFoundException;

    /**
     * This method returns the leaves for an Employee
     *
     * @param startIndex
     * @param endIndex
     * @param employeeId
     * @return
     */
    Map<String, Object> getEmployeeLeaves(Integer startIndex, Integer endIndex, Long employeeId);

    LeaveSummaryDTO getLeaveSummary(Long employeeId);

    boolean isInProbation(Long employeeId);

    Map<String, Object> getAllEmployeeLeaves(Integer startIndex, Integer endIndex, Long managerEmployeeId);

    Map<String, Object> searchEmployees(Integer startIndex, Integer endIndex, SearchCriteriaDTO searchCriteriaDTO);

    void rejectlLeave(Long leaveId) throws LeaveNotFoundException;

    DateRange getMonthPeiod();
    
      void cancelAfterApproval(Long id) ;
      
      boolean checkprojectManagerexits(Long leaveid);
      
      List<Integer> probationPeriod();

	List<Integer> getCuttOffDates();
    

}
