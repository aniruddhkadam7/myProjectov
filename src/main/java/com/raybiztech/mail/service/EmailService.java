package com.raybiztech.mail.service;

import com.raybiztech.mail.util.EmailPOJO;
import com.thoughtworks.xstream.XStream;


public class EmailService {

    /**
     * 
     * @param xml
     * @return
     */
    public EmailPOJO getEmailObj(String xml) {
        XStream xstream = new XStream();
        
        xstream.processAnnotations(com.raybiztech.mail.util.EmailPOJO.class);

        EmailPOJO emailPojo = (EmailPOJO) xstream.fromXML(xml);

        return emailPojo;
    }
}
