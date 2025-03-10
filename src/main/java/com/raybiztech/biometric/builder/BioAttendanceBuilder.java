package com.raybiztech.biometric.builder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.TimeActivity.dto.EmpoloyeeHiveActivityTime;
import com.raybiztech.TimeInOffice.business.TimeInOffice;
import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.biometric.business.AttendanceStatus;
import com.raybiztech.biometric.business.BioAttendance;
import com.raybiztech.biometric.dao.BiometricDAO;
import com.raybiztech.biometric.dto.BioAttendanceDTO;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.leavemanagement.business.LeaveStatus;
import com.raybiztech.leavemanagement.dto.LeaveDTO;
import com.raybiztech.recruitment.business.Holidays;

@Component("bioAttendanceBuilder")
public class BioAttendanceBuilder {

    @Autowired
    EmployeeBuilder employeeBuilder;
    @Autowired
    BiometricDAO biometricDAO;
    public static Logger logger = Logger.getLogger(BioAttendanceBuilder.class);

    public BioAttendanceDTO getEmployeeBioAttendanceDTO(
            BioAttendance bioAttendance, Set<Holidays> holidaysSet) {
        BioAttendanceDTO bioAttendanceDTO = new BioAttendanceDTO();

        bioAttendanceDTO.setId(bioAttendance.getId());
        bioAttendanceDTO.setStart(bioAttendance.getAttendanceDate().toString(
                "yyyy-MM-dd"));

        if (bioAttendance.getAttendanceStatus().equals(AttendanceStatus.P)) {
            bioAttendanceDTO.setAttendanceStatus(bioAttendance
                    .getAttendanceStatus());
            bioAttendanceDTO.setColor("#50c7a7");
            bioAttendanceDTO.setTitle("Biometric: "
                    + "\n"
                    + new SimpleDateFormat("hh:mm a").format(
                    bioAttendance.getInTime().getJavaDate()).toString()
                    + " - "
                    + new SimpleDateFormat("hh:mm a").format(
                    bioAttendance.getOutTime().getJavaDate())
                    .toString());

        } else {

            bioAttendanceDTO.setTitle("Absent");
            bioAttendanceDTO.setColor("#f76c51");

        }

        for (Holidays holiday : holidaysSet) {

            if (bioAttendance.getAttendanceDate().equals(holiday.getDate())
                    && bioAttendance.getAttendanceStatus().equals(
                    AttendanceStatus.A)) {

                bioAttendanceDTO.setColor("#F3F3F3");
                bioAttendanceDTO.setTitle("H");
                bioAttendanceDTO.setTextColor("#F3F3F3");
                bioAttendanceDTO.setClassName("hide-event");
            }
        }



        bioAttendanceDTO.setOverlap(false);

        return bioAttendanceDTO;
    }

    public SortedSet<BioAttendanceDTO> getEmployeeAttendanceDTOSet(
            Set<BioAttendance> bioAttendancesSet, Set<Holidays> holidays) {

        SortedSet<BioAttendanceDTO> bioAttendanceDTOs = new TreeSet<BioAttendanceDTO>();
        for (BioAttendance bioAttendance : bioAttendancesSet) {

            if ((bioAttendance.getAttendanceDate().getJavaDate().getDay() != java.util.Calendar.SATURDAY - 1)
                    && (bioAttendance.getAttendanceDate().getJavaDate()
                    .getDay() != java.util.Calendar.SUNDAY - 1)) {

                bioAttendanceDTOs.add(getEmployeeBioAttendanceDTO(
                        bioAttendance, holidays));

            } else if (bioAttendance.getAttendanceStatus().equals(
                    AttendanceStatus.P)) {
                bioAttendanceDTOs.add(getEmployeeBioAttendanceDTO(
                        bioAttendance, holidays));

            }

        }

        return bioAttendanceDTOs;
    }

    public List<EmployeeDTO> getAllAttendanceDTOSet(
            SortedSet<BioAttendance> bioAttendancesSet,
            List<EmployeeDTO> employees, List<LeaveDTO> customLeaveDTOs,
            Set<Holidays> holidays, DateRange dateRange) {
    	
        List<EmployeeDTO> employeeAttendaceDtoSet = new ArrayList<EmployeeDTO>();

        List<Date> monthDates = new ArrayList<Date>();
        Iterator<Date> iterator2 = dateRange.iterator();
        while (iterator2.hasNext()) {
            Date date = (Date) iterator2.next();
            monthDates.add(date);

        }
    	
    	List<Date> holidayDates = new ArrayList<Date>();
    	List<Date> hdayDates = new ArrayList<Date>();
    	for(Holidays hday:holidays){
    		hdayDates.add(hday.getDate());
    	}
 
        holidayDates.clear();
        for (EmployeeDTO employeeDTO : employees) {
        	for(Holidays hdays: holidays){
            	if(employeeDTO.getCountry().equals(hdays.getCountry())){
            		if(!holidayDates.contains(hdays.getDate())){
            			holidayDates.add(hdays.getDate());
            		}
            	}
            }
        	
        	
            List<BioAttendanceDTO> bioAttendanceDTOs = new ArrayList<BioAttendanceDTO>();
            List<String> dates = new ArrayList<String>();
            for (BioAttendance bioAttendance : bioAttendancesSet) {

                if (bioAttendance.getEmployee().getEmployeeId()
                        .equals(employeeDTO.getId())
                        && !dates.contains(bioAttendance.getAttendanceDate()
                        .toString())) {
                //for LOP,Casual/Paid,Holidays COUNT passed employeeDTO to getAllEmployeeBioAttendanceDTO() METHOD
                    BioAttendanceDTO bioAttendanceDTO = getAllEmployeeBioAttendanceDTO(
                            bioAttendance, holidayDates, customLeaveDTOs,employeeDTO);
                    dates.add(bioAttendance.getAttendanceDate().toString());
                    bioAttendanceDTOs.add(bioAttendanceDTO);

                }

            }
            employeeDTO.setDates(dates);
            employeeDTO.setBioAttendanceDtoSet(bioAttendanceDTOs);
            holidayDates.clear();
            employeeAttendaceDtoSet.add(employeeDTO);
        }
        //For Late Coming attendance code.
		List<BioAttendance> employees2 = null;
		employees2 = biometricDAO.getLateComingAttendance(dateRange,hdayDates);
        Map<Long,Long> employeeLateComingMap = new HashMap<Long, Long>();
        Map<Long,Date> employeeAttendenceDateMap = new HashMap<Long,Date>();
       for(BioAttendance bioAttendence : employees2) {
        	if((bioAttendence.getAttendanceDate().getJavaDate().getDay() != java.util.Calendar.SATURDAY - 1)
        		&& (bioAttendence.getAttendanceDate().getJavaDate().getDay() != java.util.Calendar.SUNDAY - 1) ) {
        		
        		Date bioAttendenceDate = null;
            	
            	if(employeeAttendenceDateMap.containsKey(bioAttendence.getEmployee().getEmployeeId()))
            	{
            		
            		bioAttendenceDate=employeeAttendenceDateMap.get(bioAttendence.getEmployee().getEmployeeId());
            	}
            	
            	
            	if(bioAttendenceDate==null || !bioAttendenceDate.equals(bioAttendence.getAttendanceDate()))
            	{
            	
            	employeeAttendenceDateMap.put(bioAttendence.getEmployee().getEmployeeId(), bioAttendence.getAttendanceDate());
            		Long count=1L;
            	if(!employeeLateComingMap.containsKey(bioAttendence.getEmployee().getEmployeeId()))
            	{
            		
            		employeeLateComingMap.put(bioAttendence.getEmployee().getEmployeeId(),count);
            	}
            	else
            	{
            		Long empcount=employeeLateComingMap.get(bioAttendence.getEmployee().getEmployeeId());
            	
            		employeeLateComingMap.put(bioAttendence.getEmployee().getEmployeeId(), empcount+1L);
            		
            	}
          }
            }
        	
        	}
        	
        Set<Long> empIds=new HashSet<Long>();
        for (EmployeeDTO employeeDTO : employeeAttendaceDtoSet) {

            for (BioAttendanceDTO bioAttendanceDTO : employeeDTO
                    .getBioAttendanceDtoSet()) {
            	
                if (bioAttendanceDTO.getColor().equalsIgnoreCase("#993300")
                        && bioAttendanceDTO.getTitle().equalsIgnoreCase(
                        "Absent")) {
                	
                    employeeDTO
                            .setAbsentCount(employeeDTO.getAbsentCount() + 1);
                }
                else
                {
                	
                	if(bioAttendanceDTO.getAttendanceStatus()!=null && !empIds.contains(employeeDTO.getId()))
                	{
                		if(bioAttendanceDTO.getAttendanceStatus().equals(AttendanceStatus.P) ){
                			empIds.add(employeeDTO.getId());
                        	employeeDTO.setLateComingCount(employeeLateComingMap.get(employeeDTO.getId()));
                        }
                	}
                
              
            }
               
            }

            Iterator<Date> iterator = dateRange.iterator();
            while (iterator.hasNext()) {
                Date date = (Date) iterator.next();

                if (!employeeDTO.getDates().contains(date.toString())) {
                    /*
                     * logger.info("missed date of :" + date.toString() +
                     * " of :" + employeeDTO.getId());
                     */
                    BioAttendanceDTO bioAttendanceDTO = new BioAttendanceDTO();

                    bioAttendanceDTO.setId(Long.valueOf(new Integer(date
                            .getDayOfMonth().getValue())));
                    bioAttendanceDTO.setStart(new Integer(date.getDayOfMonth()
                            .getValue()).toString());
                    bioAttendanceDTO.setColor("#84A0C6");
                    bioAttendanceDTO.setOverlap(false);
                    bioAttendanceDTO.setInTime(dateRange.getMinimum().toString());
                    bioAttendanceDTO.setOutTime(dateRange.getMaximum().toString());
                    Integer index = monthDates.indexOf(date);
                    employeeDTO.getBioAttendanceDtoSet().add(index,
                            bioAttendanceDTO);

                }

            }

        }

        return employeeAttendaceDtoSet;
    }

    private BioAttendanceDTO getAllEmployeeBioAttendanceDTO(
            BioAttendance bioAttendance, List<Date> holidayDates,
            List<LeaveDTO> customLeaveDTOs,EmployeeDTO employeedto) {
    	
        BioAttendanceDTO bioAttendanceDTO = new BioAttendanceDTO();
        bioAttendanceDTO.setId(bioAttendance.getId());
        bioAttendanceDTO.setStart(new Integer(bioAttendance.getAttendanceDate()
                .getDayOfMonth().getValue()).toString());

        if ((bioAttendance.getAttendanceDate().getJavaDate().getDay() != java.util.Calendar.SATURDAY - 1)
                && (bioAttendance.getAttendanceDate().getJavaDate().getDay() != java.util.Calendar.SUNDAY - 1)) {
            if (bioAttendance.getAttendanceStatus().toString()
                    .equalsIgnoreCase("P")) {
            	
                bioAttendanceDTO.setAttendanceStatus(bioAttendance
                        .getAttendanceStatus());
                bioAttendanceDTO.setColor("#009900");
                bioAttendanceDTO.setTitle( new SimpleDateFormat("hh:mm a").format(
                        bioAttendance.getInTime().getJavaDate()).toString()
                        + " - "+
                        new SimpleDateFormat("hh:mm a").format(
                        bioAttendance.getOutTime().getJavaDate())
                        .toString());
                bioAttendanceDTO.setInTime(bioAttendance.getInTime().getJavaDate().toString());
                bioAttendanceDTO.setOutTime(bioAttendance.getOutTime().getJavaDate().toString());
                bioAttendanceDTO.setLateReport(bioAttendance.getLateReport()!=null?bioAttendance.getLateReport():null);

            } else {
                bioAttendanceDTO.setAttendanceStatus(bioAttendance
                        .getAttendanceStatus());
                bioAttendanceDTO.setTitle("Absent");
                bioAttendanceDTO.setColor("#993300");

            }
            for (LeaveDTO leaveDTO : customLeaveDTOs) {

                if (leaveDTO.getLeaveDate().equals(
                        bioAttendance.getAttendanceDate())
                        && bioAttendance.getAttendanceStatus().equals(
                        AttendanceStatus.A)
                        && bioAttendance.getEmployee().getEmployeeId()
                        .equals(leaveDTO.getEmployeeDTO().getId())) {
                    // &&
                    // bioAttendance.getAttendanceStatus().equals(AttendanceStatus.A)

                    if (leaveDTO.getStatus()
                            .equals(LeaveStatus.PendingApproval)) {
                        bioAttendanceDTO.setColor("#E63010");
                        bioAttendanceDTO.setTitle(leaveDTO
                                .getLeaveCategoryDTO().getName()
                                .substring(0, 1));
                        // logger.info("pending leave matched :");
                        
                        //for count of leaves written below code
                        if(leaveDTO.getLeaveCategoryDTO().getName().equalsIgnoreCase("Casual") || leaveDTO.getLeaveCategoryDTO().getName().equalsIgnoreCase("Paid")) {
                        	employeedto.setCasualLeaveCount(employeedto.getCasualLeaveCount() + 1);
                        }
                        else if(leaveDTO.getLeaveCategoryDTO().getName().equalsIgnoreCase("Lop")) {
                        	employeedto.setLopLeaveCount(employeedto.getLopLeaveCount() + 1);
                        }
                        //Ends code
                      
                    } 
                    
                    else if (leaveDTO.getStatus()
                            .equals(LeaveStatus.Approved)) {
                        bioAttendanceDTO.setColor("#000099");
                        bioAttendanceDTO.setTitle(leaveDTO
                                .getLeaveCategoryDTO().getName()
                                .substring(0, 1));
                      //for count of leaves written below code
                        if(leaveDTO.getLeaveCategoryDTO().getName().equalsIgnoreCase("Casual") || leaveDTO.getLeaveCategoryDTO().getName().equalsIgnoreCase("Paid")) {
                        	employeedto.setCasualLeaveCount(employeedto.getCasualLeaveCount() + 1);
                        }
                        else if(leaveDTO.getLeaveCategoryDTO().getName().equalsIgnoreCase("Lop")) {
                        	employeedto.setLopLeaveCount(employeedto.getLopLeaveCount() + 1);
                        }
                        //Ends code
                       
                    }
                    
                    //For cancel after approval
                    else if(leaveDTO.getStatus().equals(LeaveStatus.CancelAfterApproval))
                    {
                        bioAttendanceDTO.setColor("#E63010");
                        bioAttendanceDTO.setTitle(leaveDTO
                                .getLeaveCategoryDTO().getName()
                                .substring(0, 1));
                      //for count of leaves written below code
                        if(leaveDTO.getLeaveCategoryDTO().getName().equalsIgnoreCase("Casual") || leaveDTO.getLeaveCategoryDTO().getName().equalsIgnoreCase("Paid")) {
                        	employeedto.setCasualLeaveCount(employeedto.getCasualLeaveCount() + 1);
                        }
                        else if(leaveDTO.getLeaveCategoryDTO().getName().equalsIgnoreCase("Lop")) {
                        	employeedto.setLopLeaveCount(employeedto.getLopLeaveCount() + 1);
                        }
                      //Ends code
                       
                }

                }

            }

        } else {
            if (bioAttendance.getAttendanceStatus().toString()
                    .equalsIgnoreCase("P")) {

                bioAttendanceDTO.setAttendanceStatus(bioAttendance
                        .getAttendanceStatus());
                bioAttendanceDTO.setColor("#009900");
                bioAttendanceDTO.setTitle( new SimpleDateFormat("hh:mm a").format(
                        bioAttendance.getInTime().getJavaDate()).toString()
                        + " - "+
                        new SimpleDateFormat("hh:mm a").format(
                        bioAttendance.getOutTime().getJavaDate())
                        .toString());

            } else {
                bioAttendanceDTO.setColor("#84A0C6");
            }

        }

        if (holidayDates.contains(bioAttendance.getAttendanceDate())) {

            if (bioAttendance.getAttendanceStatus().equals(AttendanceStatus.A)) {
                bioAttendanceDTO.setColor("#FFFFFF");
                bioAttendanceDTO.setTitle("H");
                //for holiday count written below line.
                employeedto.setHolidaysCount(employeedto.getHolidaysCount() + 1);

            } else {
                bioAttendanceDTO.setAttendanceStatus(bioAttendance
                        .getAttendanceStatus());
                bioAttendanceDTO.setColor("#009900");
                bioAttendanceDTO.setTitle( new SimpleDateFormat("hh:mm a").format(
                        bioAttendance.getInTime().getJavaDate()).toString()
                        + " - "+
                        new SimpleDateFormat("hh:mm a").format(
                        bioAttendance.getOutTime().getJavaDate())
                        .toString());
            }

        }
  
       
        bioAttendanceDTO.setOverlap(true);

        return bioAttendanceDTO;
    }

    public SortedSet<BioAttendanceDTO> getEmployeeTimeInOfficeDTOSet(
            List<TimeInOffice> spentTimeHours, Set<Holidays> holidays) {
        SortedSet<BioAttendanceDTO> bioAttendanceDTOs = new TreeSet<BioAttendanceDTO>();
        for (TimeInOffice timeInOffice : spentTimeHours) {
            if (!timeInOffice.getSpentHours().equalsIgnoreCase("0 hr")
                    && !timeInOffice.getSpentHours()
                    .equalsIgnoreCase("0 h:00m")) {

                bioAttendanceDTOs.add(getEmployeeTimeInOffice(timeInOffice));

            }


        }
        return bioAttendanceDTOs;

    }

    public BioAttendanceDTO getEmployeeTimeInOffice(TimeInOffice timeInOffice) {

        BioAttendanceDTO attendanceDTO = new BioAttendanceDTO();
        attendanceDTO.setId(timeInOffice.getId());
        attendanceDTO.setStart(timeInOffice.getDt().toString());
        attendanceDTO.setColor("#986291");
        attendanceDTO.setOverlap(Boolean.FALSE);
        attendanceDTO.setHiveHours(timeInOffice.getSpentHours());
        attendanceDTO.setTitle("Time in office: " + "\n" + timeInOffice.getSpentHours());

        return attendanceDTO;
    }

    public List<BioAttendanceDTO> getEmployeeHiveTimeDTOSet(List<EmpoloyeeHiveActivityTime> spentHiveHours, Set<Holidays> holidays) {
        List<BioAttendanceDTO> bioAttendanceDTOs = new ArrayList<BioAttendanceDTO>();

        for (EmpoloyeeHiveActivityTime activityTime : spentHiveHours) {
            if (activityTime.getpDate() != null) {
                if ((activityTime.getpDate().getJavaDate().getDay() != java.util.Calendar.SATURDAY - 1)
                        && (activityTime.getpDate()
                        .getJavaDate().getDay() != java.util.Calendar.SUNDAY - 1)) {

                    bioAttendanceDTOs.add(getEmployeeHiveTime(activityTime));

                }
            }
        }
        return bioAttendanceDTOs;
    }

    public BioAttendanceDTO getEmployeeHiveTime(EmpoloyeeHiveActivityTime timeInOffice) {

        BioAttendanceDTO attendanceDTO = new BioAttendanceDTO();
        attendanceDTO.setId(timeInOffice.getId());
        if (timeInOffice.getpDate() != null) {
            attendanceDTO.setStart(timeInOffice.getpDate().toString());
        } else {
            attendanceDTO.setStart("-");
        }
        attendanceDTO.setColor("#9cc96b");
        attendanceDTO.setOverlap(Boolean.FALSE);
        attendanceDTO.setHiveHours(timeInOffice.getHours());
        attendanceDTO.setTitle("Hive Time: " + "\n" + timeInOffice.getHours());

        return attendanceDTO;
    }
   
    
    
    
   
}
