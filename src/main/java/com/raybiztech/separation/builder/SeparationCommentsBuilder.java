package com.raybiztech.separation.builder;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
import com.raybiztech.leavemanagement.business.algorithm.DateParser;
import com.raybiztech.separation.business.SeparationComments;
import com.raybiztech.separation.business.SeparationStatus;
import com.raybiztech.separation.dto.SeparationCommentsDTO;

@Component("separationCommentsBuilder")
public class SeparationCommentsBuilder {
	@Autowired
	DAO dao;

	public SeparationComments toEntity(SeparationCommentsDTO separationCommentsDTO) {
		SeparationComments separationComments = null;
		if (separationCommentsDTO != null) {
			separationComments = new SeparationComments();
			separationComments.setComments(separationCommentsDTO.getComments());
			separationComments.setWithdrawComments(separationComments.getWithdrawComments());
			separationComments.setCreatedDate(new Second());
			separationComments.setRelievingDate(stringToDate(separationCommentsDTO.getRelievingDate()));
			separationComments.setEmployee(dao.findBy(Employee.class, separationCommentsDTO.getEmployeeId()));
			separationComments.setEmployee(dao.findBy(Employee.class, separationCommentsDTO.getEmployeeName()));
			separationComments.setStatus(SeparationStatus.valueOf(separationCommentsDTO.getStatus()));
		}
		return separationComments;
	}

	public SeparationCommentsDTO toDTO(SeparationComments separationComments) {
		SeparationCommentsDTO separationCommentsDTO = null;
		if (separationComments != null) {
			separationCommentsDTO = new SeparationCommentsDTO();
			separationCommentsDTO.setCommentId(separationComments.getCommentId());
			separationCommentsDTO.setComments(separationComments.getComments());
			separationCommentsDTO.setWithdrawComments(separationComments.getWithdrawComments());
			separationCommentsDTO.setEmployeeId(separationComments.getEmployee().getEmployeeId());
			separationCommentsDTO.setEmployeeName(separationComments.getEmployee().getEmployeeFullName());
			separationCommentsDTO.setStatus(separationComments.getStatus()!=null?separationComments.getStatus().getSeperationStatus():null);
			separationCommentsDTO.setRelievingDate(separationComments.getRelievingDate() != null
					? separationComments.getRelievingDate().toString("dd/MM/yyyy") : null);
			separationCommentsDTO.setCreatedDate(separationComments.getCreatedDate().toString("dd-MMM-yyyy hh:mm a"));
		}
		return separationCommentsDTO;

	}

	public Set<SeparationCommentsDTO> toDTOSET(Set<SeparationComments> separationComments) {
		Set<SeparationCommentsDTO> separationCommentsDTOList = null;
		if (separationComments != null) {
			separationCommentsDTOList = new HashSet<SeparationCommentsDTO>();
			for (SeparationComments separationComment : separationComments) {
				separationCommentsDTOList.add(toDTO(separationComment));
			}

		}
		return separationCommentsDTOList;
	}

	public Date stringToDate(String date) {
		Date date2 = null;
		try {
			date2 = DateParser.toDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date2;
	}

}
