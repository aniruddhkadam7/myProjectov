/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.TimeActivity.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.raybiztech.TimeActivity.business.EmpoloyeeHiveActivity;
import com.raybiztech.TimeActivity.dto.EmpoloyeeHiveActivityDTO;
import com.raybiztech.TimeActivity.dto.EmpoloyeeHiveActivityReport;
import com.raybiztech.TimeActivity.dto.EmpoloyeeHiveActivityTime;

/**
 *
 * @author naresh
 */
@Component("hiveTimeActivityBuilder")
public class HiveTimeActivityBuilder {

    public List<EmpoloyeeHiveActivityDTO> convertEntityToDTO(List<EmpoloyeeHiveActivity> activitys) {
        List<EmpoloyeeHiveActivityDTO> activityDTOList = null;

        if (!activitys.isEmpty()) {
            activityDTOList = new ArrayList<EmpoloyeeHiveActivityDTO>();
            for (EmpoloyeeHiveActivity activity : activitys) {
                EmpoloyeeHiveActivityDTO activityDTO = new EmpoloyeeHiveActivityDTO();
                activityDTO.setId(activity.getId());
                activityDTO.setFirstName(activity.getFirstName());
                activityDTO.setLastName(activity.getLastName());
                activityDTO.setUserName(activity.getUserName());
                activityDTO.setProjectName(activity.getProjectName());
                if (activity.getDate() != null) {
                    activityDTO.setDate(activity.getDate().toString());
                }
                activityDTO.setActivity(activity.getActivity());
                activityDTO.setTask(activity.getTask());
                activityDTO.setTaskType(activity.getTaskType());
                activityDTO.setComments(activity.getComments());
                activityDTO.setHours(activity.getHours());
                activityDTO.setSprintName(activity.getSprintName());
                activityDTO.setTaskId(activity.getTaskId());
                if(activity.getStartDate() != null) {
                activityDTO.setStartDate(activity.getStartDate().toString());
                }
                if(activity.getEndDate() != null) {
                activityDTO.setEndDate(activity.getEndDate().toString());
                }
                if(activity.getProjectIdentifier() != null) {
                	activityDTO.setProjectIdentifier(activity.getProjectIdentifier());
                }
                activityDTOList.add(activityDTO);
            }
        }
        return activityDTOList;

    }

    public List<EmpoloyeeHiveActivityReport> convertEmpoloyeeHiveActivityMonthlyReportDTO(List<EmpoloyeeHiveActivity> activitys) {
        List<EmpoloyeeHiveActivityReport> activityDTOList = null;

        if (!activitys.isEmpty()) {

            activityDTOList = new ArrayList<EmpoloyeeHiveActivityReport>();

            for (EmpoloyeeHiveActivity activity : activitys) {
                EmpoloyeeHiveActivityReport activityReport = new EmpoloyeeHiveActivityReport();
                activityReport.setUserName(activity.getUserName());
              
                List<EmpoloyeeHiveActivityTime> activityTimes=new ArrayList<EmpoloyeeHiveActivityTime>();
                
                
                
                activityReport.setActivityTimes(null);

            }



        }
        return activityDTOList;

    }
    
//    public List<EmpoloyeeHiveActivityReport> convertEmpoloyeeHiveForProjectTimeSheetToDTO(List<EmpoloyeeHiveActivity> activities){
//    	List<EmpoloyeeHiveActivityReport> activityDTOList = new ArrayList<EmpoloyeeHiveActivityReport>();
//    	EmpoloyeeHiveActivityReport activityReport=null;
//    	if(!activities.isEmpty()){
//    		
//    		activityReport=new EmpoloyeeHiveActivityReport();
//    		for(EmpoloyeeHiveActivity activity : activities){
//    			System.out.println("user name:"+activity.getUserName());
//        		activityReport.setUserName(activity.getUserName());
//        		activityReport.setFirstName(activity.getFirstName());
//        		activityReport.setLastName(activity.getLastName());
//        		activityReport.setId(activity.getEmpId());
//        		activityReport.setActivityTimes(projectTimSheetToDTO(activity));
//        		activityDTOList.add(activityReport);
//    		}
//    	}
//    	return activityDTOList;
//    }
//    
//    public List<EmpoloyeeHiveActivityTime> projectTimSheetToDTO(EmpoloyeeHiveActivity activity){
//    	List<EmpoloyeeHiveActivityTime> activityTimes=new ArrayList<EmpoloyeeHiveActivityTime>();
//    	if(activity!=null){
//    		EmpoloyeeHiveActivityTime activityTime=new EmpoloyeeHiveActivityTime();
//    		
//    		activityTime.setEmpId(activity.getEmpId());
//    		activityTime.setHours(String.valueOf(activity.getHours()));
//    		activityTime.setProjectDate(activity.getDate().toString());
//    		//activityTime.setDayofMonth(activity.get);
//    		activityTimes.add(activityTime);
//    		
//    	}
//    	return activityTimes;
//    }
}
