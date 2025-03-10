package com.raybiztech.recruitment.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.raybiztech.lookup.business.VacancyLookUp;
import com.raybiztech.recruitment.dto.VacancyLookUpDTO;

@Component("vacancyLookUpBuilder")
public class VacancyLookUpBuilder {

    public List<VacancyLookUp> createVacancyLookUpEntityList(
            List<VacancyLookUpDTO> vacancyLookUpDtoList) {
        List<VacancyLookUp> vacancyLookUpList = null;
        if (vacancyLookUpDtoList != null) {
            vacancyLookUpList = new ArrayList<VacancyLookUp>();
            for (VacancyLookUpDTO vacancyLookUpDto : vacancyLookUpDtoList) {
                VacancyLookUp vacancyLookUp = new VacancyLookUp();
                vacancyLookUp.setName(vacancyLookUpDto.getName());
                vacancyLookUp.setVacancyLookUpId(vacancyLookUpDto
                        .getVacancyLookUpId());
                vacancyLookUp.setJobCode(vacancyLookUpDto.getJobCode());
                vacancyLookUpList.add(vacancyLookUp);
            }
        }
        return vacancyLookUpList;
    }

    public List<VacancyLookUpDTO> createVacancyLookUpDTOList(
            List<VacancyLookUp> vacancyLookUpList) {
        List<VacancyLookUpDTO> vacancyLookUpDtoList = null;
        if (vacancyLookUpList != null) {
            vacancyLookUpDtoList = new ArrayList<VacancyLookUpDTO>();
            for (VacancyLookUp vacancyLookUp : vacancyLookUpList) {
                VacancyLookUpDTO vacancyLookUpDto = new VacancyLookUpDTO();
                vacancyLookUpDto.setName(vacancyLookUp.getName());
                vacancyLookUpDto.setVacancyLookUpId(vacancyLookUp
                        .getVacancyLookUpId());
                vacancyLookUpDto.setJobCode(vacancyLookUp.getJobCode());
                vacancyLookUpDtoList.add(vacancyLookUpDto);
            }
        }
        return vacancyLookUpDtoList;
    }

    public VacancyLookUpDTO createVacancyLookUpDTO(VacancyLookUp vacancyLookUp) {
        VacancyLookUpDTO vacancyLookUpDto = null;
        if (vacancyLookUp != null) {
            vacancyLookUpDto = new VacancyLookUpDTO();

            vacancyLookUpDto.setName(vacancyLookUp.getName());
            vacancyLookUpDto.setVacancyLookUpId(vacancyLookUp
                    .getVacancyLookUpId());
            vacancyLookUpDto.setJobCode(vacancyLookUp.getJobCode());
            
        }
        return vacancyLookUpDto;
    }

}
