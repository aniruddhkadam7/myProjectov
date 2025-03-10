package com.raybiztech.hrm.security.rest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.PersistLogin;
import com.raybiztech.hrm.security.rest.dao.UserDao;

/**
 *
 * @author hari
 */
@Component
public class TokenUtils {

    public static final String MAGIC_KEY = "obfuscate";

    static Logger logger = Logger.getLogger(TokenUtils.class);

    @Autowired
    UserDao userDaoImpl;

    public static String createToken(UserDetails userDetails) {
        /* Expires in one hour */
        long expires = System.currentTimeMillis() + 1000L * 60 * 60;

        StringBuilder tokenBuilder = new StringBuilder();
        tokenBuilder.append(userDetails.getUsername());
        tokenBuilder.append(":");
        tokenBuilder.append(expires);
        tokenBuilder.append(":");
        tokenBuilder.append(TokenUtils.computeSignature(userDetails, expires));

        return tokenBuilder.toString();
    }

    public static String computeSignature(UserDetails userDetails, long expires) {
        StringBuilder signatureBuilder = new StringBuilder();
        signatureBuilder.append(userDetails.getUsername());
        signatureBuilder.append(":");
        signatureBuilder.append(expires);
        signatureBuilder.append(":");
        signatureBuilder.append(":");
        signatureBuilder.append(TokenUtils.MAGIC_KEY);

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }

        return new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes())));
    }

    public static String getUserNameFromToken(String authToken) {
        if (null == authToken) {
            return null;
        }

        String[] parts = authToken.split(":");
        return parts[0];
    }

    public boolean validateToken(String authToken) {
//        String[] parts = authToken.split(":");
//        long expires = Long.parseLong(parts[1]);
//        String signature = parts[2];
//
//        if (expires < System.currentTimeMillis()) {
//            return false;
//        }
//        logger.info("######Current Signature : " + signature);
//        return signature.equals(TokenUtils.computeSignature(userDetails, expires));
        return userDaoImpl.findByTokenName(PersistLogin.class, authToken) != null;
    }

    public Employee getEmployeeFromTokenUtils(String userName) {
        Employee employee = userDaoImpl.findByEmployeeName(Employee.class, userName);
        return employee;
       
    }
}
