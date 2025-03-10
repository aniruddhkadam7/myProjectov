package com.raybiztech.recruitment.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.raybiztech.lookup.business.SourceLookUp;
import com.raybiztech.recruitment.dto.SourceLookUpDTO;

@Component("sourceLookUpBuilder")
public class SourceLookUpBuilder {

    public SourceLookUp createSourceLookUpEntity(
            SourceLookUpDTO sourceLookUpDto) {

        SourceLookUp sourceLookUp = null;
        if (sourceLookUpDto != null) {
            sourceLookUp = new SourceLookUp();

            sourceLookUp.setDisplayOrder(sourceLookUpDto.getDisplayOrder());
            sourceLookUp.setSourceName(sourceLookUpDto.getSourceName());
            sourceLookUp.setSourceType(sourceLookUpDto.getSourceType());
            sourceLookUp.setSourceLookUpId(sourceLookUpDto
                    .getSourceLookUpId());

        }
        return sourceLookUp;
    }

    public SourceLookUpDTO createSourceLookUpDTO(
            SourceLookUp sourceLookUp) {

        SourceLookUpDTO sourceLookUpDTO = null;
        if (sourceLookUp != null) {
            sourceLookUpDTO = new SourceLookUpDTO();

            sourceLookUpDTO.setDisplayOrder(sourceLookUp.getDisplayOrder());
            sourceLookUpDTO.setSourceName(sourceLookUp.getSourceName());
            sourceLookUpDTO.setSourceType(sourceLookUp.getSourceType());
            sourceLookUpDTO.setSourceLookUpId(sourceLookUp
                    .getSourceLookUpId());

        }
        return sourceLookUpDTO;
    }

    public List<SourceLookUp> createSourceLookUpEntityList(
            List<SourceLookUpDTO> sourceLookUpDtoList) {

        List<SourceLookUp> sourceLookUpList = null;
        if (sourceLookUpDtoList != null) {
            sourceLookUpList = new ArrayList<SourceLookUp>();
            for (SourceLookUpDTO sourceLookUpDto : sourceLookUpDtoList) {
                SourceLookUp sourceLookUp = new SourceLookUp();
                sourceLookUp.setDisplayOrder(sourceLookUpDto.getDisplayOrder());
                sourceLookUp.setSourceName(sourceLookUpDto.getSourceName());
                sourceLookUp.setSourceType(sourceLookUpDto.getSourceType());
                sourceLookUp.setSourceLookUpId(sourceLookUpDto
                        .getSourceLookUpId());
                sourceLookUpList.add(sourceLookUp);
            }

        }
        return sourceLookUpList;
    }

    public List<SourceLookUpDTO> createSourceLookUpDtoList(
            List<SourceLookUp> sourceLookUpList) {

        List<SourceLookUpDTO> sourceLookUpDtoList = null;
        if (sourceLookUpList != null) {
            sourceLookUpDtoList = new ArrayList<SourceLookUpDTO>();
            for (SourceLookUp sourceLookUp : sourceLookUpList) {
                SourceLookUpDTO sourceLookUpDto = new SourceLookUpDTO();
                sourceLookUpDto.setDisplayOrder(sourceLookUp.getDisplayOrder());
                sourceLookUpDto.setSourceName(sourceLookUp.getSourceName());
                sourceLookUpDto.setSourceType(sourceLookUp.getSourceType());
                sourceLookUpDto.setSourceLookUpId(sourceLookUp
                        .getSourceLookUpId());
                sourceLookUpDtoList.add(sourceLookUpDto);
            }

        }
        return sourceLookUpDtoList;
    }

}
