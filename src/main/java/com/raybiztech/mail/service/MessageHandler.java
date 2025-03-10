/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.mail.service;

import com.raybiztech.mail.util.EmailPOJO;

/**
 *
 * @author ramesh
 */
public interface MessageHandler {
    
    void handle(EmailPOJO email);
}
