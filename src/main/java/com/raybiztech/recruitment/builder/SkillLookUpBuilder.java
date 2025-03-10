package com.raybiztech.recruitment.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.raybiztech.lookup.business.SkillLookUp;
import com.raybiztech.recruitment.dto.SkillLookUpDTO;

@Component("skillLookUpBuilder")
public class SkillLookUpBuilder {

    public SkillLookUp createSkillLookUpEntity(SkillLookUpDTO skillLookUpDto) {
        SkillLookUp skillLookUp = null;
        if (skillLookUpDto != null) {
            skillLookUp = new SkillLookUp();
            skillLookUp.setDiplayOrder(skillLookUpDto.getDiplayOrder());
            skillLookUp.setId(skillLookUpDto.getId());
            skillLookUp.setName(skillLookUpDto.getName());
        }
        return skillLookUp;
    }

    public SkillLookUpDTO createSkillLookUpDTO(SkillLookUp skillLookUp) {
        SkillLookUpDTO skillLookUpDto = null;
        if (skillLookUp != null) {
            skillLookUpDto = new SkillLookUpDTO();
            skillLookUpDto.setDiplayOrder(skillLookUp.getDiplayOrder());
            skillLookUpDto.setId(skillLookUp.getId());
            skillLookUpDto.setName(skillLookUp.getName());
        }
        return skillLookUpDto;
    }

    public List<SkillLookUpDTO> createSkillLookUpDTOList(List<SkillLookUp> skillLookUps) {

        List<SkillLookUpDTO> skillLookUpDtos = null;

        if (skillLookUps != null) {
            skillLookUpDtos = new ArrayList<SkillLookUpDTO>();

            for (SkillLookUp skillLookUp : skillLookUps) {
                SkillLookUpDTO skillLookUpDto = new SkillLookUpDTO();
                skillLookUpDto.setDiplayOrder(skillLookUp.getDiplayOrder());
                skillLookUpDto.setId(skillLookUp.getId());
                skillLookUpDto.setName(skillLookUp.getName());

                skillLookUpDtos.add(skillLookUpDto);

            }

        }
        return skillLookUpDtos;

    }

}
