/*
 * To change this template,  choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.payslip.utility;

import org.apache.log4j.Logger;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

/**
 * 
 * @author ramesh
 */
// @Component("base64Conversion")
public class AES256Encryption {

	String salt;
	final String password;
	private static final Logger LOGGER = Logger
			.getLogger(AES256Encryption.class);

	/**
	 * 
	 * @param userKey
	 * @param saltKey
	 */
	public AES256Encryption(String userKey, String saltKey) {

		password = userKey;
		salt = saltKey;
	}

	/* Encryption Method */
	/**
	 * 
	 * @param message
	 * @return
	 */
	public String encrypt(String message) {
		if (message != null && !("null".equals(message))) {
			TextEncryptor encryptor = Encryptors.text(password, salt);
			LOGGER.debug("Original text: \"" + message + "\"");

			String encryptedText = encryptor.encrypt(message);
			LOGGER.debug("Encrypted text: \"" + encryptedText + "\"");
			return encryptedText;
		}

		return message;
	}

	/* Decryption Method */
	/**
	 * 
	 * @param message
	 * @return
	 */
	public String decrypt(String message) {

		if (message != null && !("null".equals(message))) {
			TextEncryptor decryptor = Encryptors.text(password, salt);
			return decryptor.decrypt(message);
		}

		return message;
	}

}