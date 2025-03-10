package com.raybiztech.appraisals.builder;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.DesignationKras;
import com.raybiztech.appraisals.business.KraWithWeightage;
import com.raybiztech.appraisals.dto.DesignationKrasDTO;
import com.raybiztech.appraisals.dto.KraWithWeightageDTO;

@Component("designationBuilder")
public class DesignationKrasBuilder {

    @Autowired
    KRABuilder kraBuilder;

    public KRABuilder getKraBuilder() {
        return kraBuilder;
    }

    public void setKraBuilder(KRABuilder kraBuilder) {
        this.kraBuilder = kraBuilder;
    }

    public DesignationKrasDTO createRoleDTO(DesignationKras designation) {
        DesignationKrasDTO designationDto = new DesignationKrasDTO();
        if (designation != null) {
            designationDto = new DesignationKrasDTO();
            designationDto.setId(designation.getDesignationKRAsId());
            designationDto.setDesignationCode(designation.getDesignationCode());
            designationDto.setDesignationName(designation.getDesignationName());
/*
            Set<KraWithWeightage> entityList = designation.getKrasWithWeitage();

            Set<KraWithWeightageDTO> dtoList = new HashSet<KraWithWeightageDTO>();

            for (KraWithWeightage kraWithWeightage : entityList) {
                KraWithWeightageDTO dto = new KraWithWeightageDTO();
                dto.setId(kraWithWeightage.getKraWithWeightageId());
                dto.setWeightage(kraWithWeightage.getWeightage());
                dto.setKradto(kraBuilder.createKRADTO(kraWithWeightage.getKra()));
                dtoList.add(dto);
            }
            designationDto.setKrasWithWeitage(dtoList);*/

        }
        return designationDto;
    }

    public DesignationKras createRoleEntity(DesignationKrasDTO designationDto) {
        DesignationKras designation = new DesignationKras();
        if (designation != null) {
            designation.setDesignationKRAsId(designationDto.getId());
            designation.setDesignationCode(designationDto.getDesignationCode());
            designation.setDesignationName(designationDto.getDesignationName());

            Set<KraWithWeightageDTO> dtoList = designationDto.getKrasWithWeitage();

            Set<KraWithWeightage> entityList = new HashSet<KraWithWeightage>();

            for (KraWithWeightageDTO kraWithWeightageDto : dtoList) {
                KraWithWeightage entity = new KraWithWeightage();
                entity.setKraWithWeightageId(kraWithWeightageDto.getId());
                entity.setWeightage(kraWithWeightageDto.getWeightage());
                entity.setKra(kraBuilder.createKRAEntity(kraWithWeightageDto.getKradto()));
                entityList.add(entity);
            }
          /*  designation.setKrasWithWeitage(entityList);*/
        }
        return designation;
    }

    /*public List<DesignationDTO> getDesignationDTOList(List<DesignationKras> designationList) {

     List<DesignationDTO> designationDTOList = new ArrayList<DesignationDTO>();
     for (DesignationKras designation : designationList) {
     DesignationDTO designationDto = new DesignationDTO();
     if (designation != null) {
     designationDto.setId(designation.getId());
     designationDto.setDesignationCode(designation.getDesignationCode());
     designationDto.setDesignationName(designation.getDesignationName());
     designationDto.setKrasWithWeitage(designation.getKrasWithWeitage());
     }
     designationDTOList.add(designationDto);
     }
     return designationDTOList;
     }

     public List<DesignationKras> getDesignationList(List<DesignationDTO> designationDTOList) {

     List<DesignationKras> designationList = new ArrayList<DesignationKras>();
     for (DesignationDTO designationDto : designationDTOList) {
     DesignationKras designation = new DesignationKras();
     if (designationDto != null) {
     designation.setId(designationDto.getId());
     designation.setDesignationCode(designationDto.getDesignationCode());
     designation.setDesignationName(designationDto.getDesignationName());
     designation.setKrasWithWeitage(designationDto.getKrasWithWeitage());
     }
     designationList.add(designation);
     }
     return designationList;
     }*/
}
