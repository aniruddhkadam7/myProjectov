/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.recruitment.builder;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.raybiztech.recruitment.business.DocType;
import com.raybiztech.recruitment.business.Document;
import com.raybiztech.recruitment.dto.DocumentDTO;

/**
 *
 * @author hari
 */
@Component("documentBuilder")
public class DocumentBuilder {
    
    public Set<Document> createDocumentEntitySet(Set<DocumentDTO> documentDTOSet) {
        Set<Document> documentSet = null;
        if (documentDTOSet != null) {
            documentSet = new HashSet<Document>();
            for (DocumentDTO documentDTO : documentDTOSet) {
                Document document = new Document();
                document.setDescription(documentDTO.getDescription());
                document.setDocType(DocType.valueOf(documentDTO.getDocType()));
                document.setDoctokenId(documentDTO.getDoctokenId());
                document.setDocumentId(documentDTO.getId());
                
                documentSet.add(document);
            }
        }
        return documentSet;
    }
    
      public Set<DocumentDTO> createDocumentDTOSet(Set<Document> documentSet) {
        Set<DocumentDTO> documentDTOSet = null;
        if (documentSet != null) {
            documentDTOSet = new HashSet<DocumentDTO>();
            for (Document document : documentSet) {
                DocumentDTO documentDTO = new DocumentDTO();
                documentDTO.setDescription(document.getDescription());
                documentDTO.setDocType(document.getDocType().toString());
                documentDTO.setDoctokenId(document.getDoctokenId());
                documentDTO.setId(document.getDocumentId());
                
                documentDTOSet.add(documentDTO);
            }
        }
        return documentDTOSet;
    }
}
