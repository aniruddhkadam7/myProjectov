package com.raybiztech.itdeclaration.builder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
import com.raybiztech.itdeclaration.business.FinanceCycle;
import com.raybiztech.itdeclaration.dao.ITDeclarationDao;
import com.raybiztech.itdeclaration.dto.FinanceCycleDTO;

@Component("iTDeclarationBuilder")
public class ITDeclarationBuilder {

    // Autowiring Security Utils to get Id of Current User
    @Autowired
    SecurityUtils securityUtils;

    // Autowiring DaoImpl to use findBy
    @Autowired
    ITDeclarationDao iTDeclarationDaoImpl;

    // Converting Cycle DTO into Cycle Business/Entity
    public FinanceCycle toEntity(FinanceCycleDTO financeCycleDTO) {
        FinanceCycle financeCycle = new FinanceCycle();

        financeCycle.setCycleName(financeCycleDTO.getCycleName());

		financeCycle.setCreatedBy(securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder());
		financeCycle.setStartDate(toDate("01/"+financeCycleDTO.getStartDate()));
		//financeCycle.setEndDate();
		
		DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
	    Calendar calendar = Calendar.getInstance();
	    try {
			calendar.setTime(dateFormat.parse(financeCycleDTO.getEndDate()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    //financeCycle.setEndDate(toDate()calendar.getActualMaximum(Calendar.DAY_OF_MONTH)+financeCycleDTO.getEndDate());
	    financeCycle.setEndDate(toDate(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)+"/"+financeCycleDTO.getEndDate()));
	    
		Boolean active = financeCycleDTO.getActive()==null ? false : financeCycleDTO.getActive(); 
		financeCycle.setActive(active);
		financeCycle.setCreatedDate(new Second());
		return financeCycle;
	}

	// Converting Updated Cycle DTO into CycleEntity
	public FinanceCycle toEditEntity(FinanceCycleDTO financeCycleDTO) {
		FinanceCycle financeCycle = iTDeclarationDaoImpl.findBy(
				FinanceCycle.class, financeCycleDTO.getCycleId());
		financeCycle.setCycleName(financeCycleDTO.getCycleName());
		Boolean active = financeCycleDTO.getActive()==null ? false : financeCycleDTO.getActive();
		
		if(!financeCycle.getStartDate().toString("dd/MM/yyyy").equals(financeCycleDTO.getStartDate())){
			DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
			Calendar calendar = Calendar.getInstance();
			    try {
					calendar.setTime(dateFormat.parse(financeCycleDTO.getEndDate()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			    financeCycle.setStartDate(toDate("01/"+financeCycleDTO.getStartDate()));
			    financeCycle.setEndDate(toDate(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)+"/"+financeCycleDTO.getEndDate()));
		}

        financeCycle.setActive(active);
        financeCycle.setUpdatedDate(new Second());
       

        return financeCycle;
    }

    // Converting Cycle Buisness into Cycle DTO
    public FinanceCycleDTO toDTO(FinanceCycle financeCycle) {
        FinanceCycleDTO financeCycleDTO = null;
        if (financeCycle != null) {
            financeCycleDTO = new FinanceCycleDTO();
            financeCycleDTO.setCycleId(financeCycle.getCycleId());
            financeCycleDTO.setCycleName(financeCycle.getCycleName());
            financeCycleDTO.setStartDate(financeCycle.getStartDate().toString(
                    "MM/yyyy"));
            financeCycleDTO.setEndDate(financeCycle.getEndDate().toString(
                    "MM/yyyy"));
            financeCycleDTO.setActive(financeCycle.getActive());

        }
        return financeCycleDTO;
    }

    // Converting Cycle Business List into Cycle DTO List
    public List<FinanceCycleDTO> toDTOList(List<FinanceCycle> listFinanceCycles) {
        List<FinanceCycleDTO> listFinanceCycleDTOs = null;
        if (listFinanceCycles != null) {
            listFinanceCycleDTOs = new ArrayList<FinanceCycleDTO>();

            for (FinanceCycle financeCycle : listFinanceCycles) {
                listFinanceCycleDTOs.add(toDTO(financeCycle));
            }

        }
        return listFinanceCycleDTOs;
    }

	// converting String into Ray Biz Tech Date
	public Date toDate(String strDate) {
		Date date = null;
		try {
			date = Date.parse(strDate, "dd/MM/yyyy");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

}
