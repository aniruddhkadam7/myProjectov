package com.raybiztech.mail.receiver;

import javax.jms.JMSException;
import javax.mail.MessagingException;


public interface MessageReceiver {
    /**
     * 
     * @throws JMSException
     * @throws MessagingException
     */
    public void receiveMessage() throws JMSException, MessagingException;
}
