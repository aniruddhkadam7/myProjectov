package com.raybiztech.projectmanagement.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.raybiztech.projectmanagement.business.ChangeRequest;
import com.raybiztech.projectmanagement.dao.ResourceManagementDAO;
import com.raybiztech.projectmanagement.dto.ChangeRequestDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@Component("changeRequestBuilder")
public class ChangeRequestBuilder {

    
    @Autowired
    ResourceManagementDAO resourceManagementDAO;
    Logger logger=Logger.getLogger(ChangeRequestBuilder.class);
    
	public ChangeRequest toEntity(ChangeRequestDTO changeRequestDTO) {

		ChangeRequest changeRequest = null;
		if (changeRequestDTO != null) {
                   
                    if(changeRequestDTO.getId()!=null)
                    {
                                
                            changeRequest=resourceManagementDAO.findBy(ChangeRequest.class,changeRequestDTO.getId());
                            changeRequest.setNumbersStatus(changeRequest.getNumbersStatus());
                            changeRequest.setMilestoneStatus(changeRequest.getMilestoneStatus());
                    }
                    else
                    {
			changeRequest = new ChangeRequest();
                        changeRequest.setNumbersStatus(Boolean.FALSE);
                        changeRequest.setMilestoneStatus(Boolean.FALSE);
                    }
			changeRequest.setDescripition(changeRequestDTO.getDescripition());
			changeRequest.setDuration(changeRequestDTO.getDuration());
			changeRequest.setId(changeRequestDTO.getId());
			changeRequest.setName(changeRequestDTO.getName());
			changeRequest.setProjectId(changeRequestDTO.getProjectId());
			
                      
		}

		return changeRequest;
	}

	public ChangeRequestDTO toDto(ChangeRequest changeRequest) {

		ChangeRequestDTO changeRequestDTO = null;
		if (changeRequest != null) {
			changeRequestDTO = new ChangeRequestDTO();
			changeRequestDTO.setId(changeRequest.getId());
			changeRequestDTO.setName(changeRequest.getName());
			changeRequestDTO.setDescripition(changeRequest.getDescripition());
			changeRequestDTO.setDuration(changeRequest.getDuration());
			changeRequestDTO.setProjectId(changeRequest.getProjectId());
			changeRequestDTO.setNumbersStatus(changeRequest.getNumbersStatus());
		}
		return changeRequestDTO;
	}

	public List<ChangeRequestDTO> toListDto(List<ChangeRequest> changeRequests) {

		List<ChangeRequestDTO> changeRequestDTOList = null;
		if (changeRequests != null) {
			changeRequestDTOList = new ArrayList<ChangeRequestDTO>();
			for (ChangeRequest changeRequest : changeRequests) {
				changeRequestDTOList.add(toDto(changeRequest));
			}
		}

		return changeRequestDTOList;

	}

}
