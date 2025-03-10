package com.raybiztech.mail.receiver;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.mail.service.EmailService;
import com.raybiztech.mail.service.MessageHandler;
import com.raybiztech.mail.util.EmailPOJO;

@Transactional
public class MessageReceiverImpl implements MessageListener {

    private static final Logger LOGGER = Logger
            .getLogger(MessageReceiverImpl.class);

    @Autowired
    MessageHandler messageHandler;
    @Override
    public void onMessage(Message msg) {
        try {
            TextMessage textMessage = (TextMessage) msg;

            String xmlMsg = textMessage.getText();

            EmailPOJO email = new EmailService().getEmailObj(xmlMsg);
           
            messageHandler.handle(email);
            
            

        } catch (JMSException e) {
            LOGGER.error("Error occurred while sending message: " + e, e);
        }
    }
}
