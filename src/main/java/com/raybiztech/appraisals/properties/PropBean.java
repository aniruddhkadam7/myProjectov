package com.raybiztech.appraisals.properties;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/*
 * To change this template,  choose Tools | Templates
 * and open the template in the editor.
 */
@Component
public class PropBean {

    private static final Logger logger = Logger.getLogger(PropBean.class);
    private Map propData;

    /**
     *
     */
    public PropBean() {
        logger.info("prop bean object is created");

    }

    /**
     *
     * @return
     */
    public Map getPropData() {
        return propData;
    }

    /**
     *
     * @param propData
     */
    public void setPropData(Map propData) {
        this.propData = propData;
    }

}
