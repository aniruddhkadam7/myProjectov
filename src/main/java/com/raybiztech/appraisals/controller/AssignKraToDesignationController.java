package com.raybiztech.appraisals.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.appraisals.builder.KRABuilder;
import com.raybiztech.appraisals.dto.KRADTO;
import com.raybiztech.appraisals.service.AssignKraToDesignationService;
import com.raybiztech.appraisals.service.DesignationKrasService;

@Controller
@RequestMapping("/assignKraToDesignationController")
@Transactional
public class AssignKraToDesignationController {

    public static Logger logger = Logger
            .getLogger(AssignKraToDesignationController.class);

    @Autowired
    AssignKraToDesignationService kraToRoleService;
    @Autowired
    DesignationKrasService designationKrasService;
    @Autowired
    KRABuilder kraBuilder;

    @RequestMapping(value = "/assignKras", params = { "designationName" }, method = RequestMethod.POST)
    public @ResponseBody
    void assignKras(String designationName,
            @RequestBody KRADTO[] krasWithWeightage) {
        kraToRoleService.assignKras(designationName, krasWithWeightage);
    }

    @RequestMapping(value = "/getAllDesignations", method = RequestMethod.GET)
    public @ResponseBody
    List<String> getAllDesignations() {
        // TODO Auto-generated method stub
        return designationKrasService.getAllDesignations();
    }
    
    @RequestMapping(value = "/getKraWithWeightagesForADesignation", params = { "designationName" }, method = RequestMethod.GET)
    public @ResponseBody
    List<KRADTO> getKraWithWeightagesForADesignation(String designationName) {
        List<KRADTO> list = designationKrasService.getAllKRAsWithWeightage(designationName);
        logger.info("inside getAllKRAsWithWeightage in controller :" +list);
        return list;
    }

}