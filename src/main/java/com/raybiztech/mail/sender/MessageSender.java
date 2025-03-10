package com.raybiztech.mail.sender;

import javax.jms.JMSException;

public interface MessageSender {
    /**
     *
     * @param theMessage
     * @throws JMSException
     */
    void sendMessage(final String theMessage) throws JMSException;
}
