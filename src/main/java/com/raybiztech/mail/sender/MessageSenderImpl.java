package com.raybiztech.mail.sender;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class MessageSenderImpl implements MessageSender {

	private static final Logger LOGGER = Logger
			.getLogger(MessageSenderImpl.class);
	private JmsTemplate jmsTemplate;

	/**
	 *
	 * @param jmsTemplate1
	 */
	public void setJmsTemplate(JmsTemplate jmsTemplate1) {
		this.jmsTemplate = jmsTemplate1;
	}

	/**
	 *
	 * @param theMessage
	 * @throws JMSException
	 */
	@Override
	public void sendMessage(final String theMessage) throws JMSException {
		MessageCreator messageCreator = new MessageCreator() {
			TextMessage message = null;

			@Override
			public Message createMessage(Session session) throws JMSException {
				message = session.createTextMessage();
				message.setText(theMessage);
				return message;
			}

		};

		jmsTemplate.send(messageCreator);

	}

}
