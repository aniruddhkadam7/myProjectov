package com.raybiztech.leavemanagement.dto;

import java.io.Serializable;
import java.util.List;

import com.raybiztech.appraisals.dto.EmployeeDTO;

public class LeaveSummaryDTO implements Serializable,
		Comparable<LeaveSummaryDTO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3276565470079010578L;
	private Long id;
	private EmployeeDTO employeeDTO;
	private Double AllAvailableLeaves = 0.0;
	private Double AllCreditedLeaves = 0.0;
	private Double AllTakenLeaves = 0.0;
	private Double AllPendingLeaves = 0.0;
	private Double allScheduledLeaves = 0.0;
	private Double carryForwardedLeaves = 0.0;
	private Double calculatedCreditedLeaves = 0.0;
	private Double allLOPPendingLeaves = 0.0;
	private Double allLOPTakenLeaves = 0.0;
	private Double AllCancelAfterApprovalLeaves = 0.0;

	private List<LeaveCategorySummaryDTO> leaveCategorySummaries;

	public List<LeaveCategorySummaryDTO> getLeaveCategorySummaries() {
		return leaveCategorySummaries;
	}

	public void setLeaveCategorySummaries(
			List<LeaveCategorySummaryDTO> leaveCategorySummaries) {
		this.leaveCategorySummaries = leaveCategorySummaries;
	}

	public Double getAllScheduledLeaves() {
		return allScheduledLeaves;
	}

	public void setAllScheduledLeaves(Double allScheduledLeaves) {
		this.allScheduledLeaves = allScheduledLeaves;
	}

	public Double getAllAvailableLeaves() {
		return AllAvailableLeaves;
	}

	public void setAllAvailableLeaves(Double allAvailableLeaves) {
		AllAvailableLeaves = allAvailableLeaves;
	}

	public Double getAllCreditedLeaves() {
		return AllCreditedLeaves;
	}

	public void setAllCreditedLeaves(Double allCreditedLeaves) {
		AllCreditedLeaves = allCreditedLeaves;
	}

	public Double getAllTakenLeaves() {
		return AllTakenLeaves;
	}

	public void setAllTakenLeaves(Double allTakenLeaves) {
		AllTakenLeaves = allTakenLeaves;
	}

	public Double getAllPendingLeaves() {
		return AllPendingLeaves;
	}

	public void setAllPendingLeaves(Double allPendingLeaves) {
		AllPendingLeaves = allPendingLeaves;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EmployeeDTO getEmployeeDTO() {
		return employeeDTO;
	}

	public void setEmployeeDTO(EmployeeDTO employeeDTO) {
		this.employeeDTO = employeeDTO;
	}

	public Double getCarryForwardedLeaves() {
		return carryForwardedLeaves;
	}

	public void setCarryForwardedLeaves(Double carryForwardedLeaves) {
		this.carryForwardedLeaves = carryForwardedLeaves;
	}

	public Double getCalculatedCreditedLeaves() {
		return calculatedCreditedLeaves;
	}

	public void setCalculatedCreditedLeaves(Double calculatedCreditedLeaves) {
		this.calculatedCreditedLeaves = calculatedCreditedLeaves;
	}

	public Double getAllLOPPendingLeaves() {
		return allLOPPendingLeaves;
	}

	public void setAllLOPPendingLeaves(Double allLOPPendingLeaves) {
		this.allLOPPendingLeaves = allLOPPendingLeaves;
	}

	public Double getAllLOPTakenLeaves() {
		return allLOPTakenLeaves;
	}

	public void setAllLOPTakenLeaves(Double allLOPTakenLeaves) {
		this.allLOPTakenLeaves = allLOPTakenLeaves;
	}

	public Double getAllCancelAfterApprovalLeaves() {
		return AllCancelAfterApprovalLeaves;
	}

	public void setAllCancelAfterApprovalLeaves(
			Double allCancelAfterApprovalLeaves) {
		AllCancelAfterApprovalLeaves = allCancelAfterApprovalLeaves;
	}

	@Override
	public String toString() {
		return "LeaveSummaryDTO [id=" + id + ", AllAvailableLeaves="
				+ AllAvailableLeaves + ", AllCreditedLeaves="
				+ AllCreditedLeaves + ", AllTakenLeaves=" + AllTakenLeaves
				+ ", AllPendingLeaves=" + AllPendingLeaves
				+ ", calculatedCreditedLeaves=" + calculatedCreditedLeaves
				+ ", carryForwardedLeaves=" + carryForwardedLeaves + "]";
	}

	@Override
	public int compareTo(LeaveSummaryDTO o) {
		// TODO Auto-generated method stub
		return (int) (this.id - o.id);
	}

}