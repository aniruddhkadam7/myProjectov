package com.raybiztech.mailtemplates.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
import com.raybiztech.mailtemplates.business.AccountEmail;
import com.raybiztech.mailtemplates.dto.AccountEmailDto;
import com.raybiztech.payslip.utility.AES256Encryption;
@Component("accountEmailBuilder")
public class AccountEmailBuilder {
	@Autowired
	SecurityUtils securityUtils;
public AccountEmail toEntity(AccountEmailDto emailDTO)
{
	AccountEmail accountEmail=new AccountEmail();
	String saltKey=KeyGenerators.string().generateKey();
	AES256Encryption aes256Encryption=new AES256Encryption(emailDTO.getEmail(), saltKey);
	accountEmail.setSaltKey(saltKey);
	accountEmail.setEmail(emailDTO.getEmail());
	accountEmail.setPassword(aes256Encryption.encrypt(emailDTO.getPassword()));
	accountEmail.setCreatedDate(new Date());
	return accountEmail;
}

public List<AccountEmailDto> toDTOList(List<AccountEmail> accountEmailList)
{
	List<AccountEmailDto> accountEmaiDTOlList=new ArrayList<AccountEmailDto>();
	
	AccountEmailDto accountEmailDto=new AccountEmailDto();
	if(accountEmailList!=null)
	{
		for(AccountEmail accountEmail:accountEmailList)
		{
			//AES256Encryption aes256Encryption=new AES256Encryption(accountEmail.getEmail(),accountEmail.getSaltKey());
			accountEmailDto.setId(accountEmail.getId());
			accountEmailDto.setEmail(accountEmail.getEmail());
			//accountEmailDto.setPassword(aes256Encryption.decrypt(accountEmail.getPassword()));
			accountEmaiDTOlList.add(accountEmailDto);
		}
		
	}
	return accountEmaiDTOlList;
}

public AccountEmail toEditEntity(AccountEmailDto accountEmailDto,String saltKey)
{
	AccountEmail accountEmail=new AccountEmail();
	AES256Encryption aes256Encryption=new AES256Encryption(accountEmailDto.getEmail(), saltKey);
	accountEmail.setId(accountEmailDto.getId());
	accountEmail.setEmail(accountEmailDto.getEmail());
	accountEmail.setPassword(aes256Encryption.encrypt(accountEmailDto.getPassword()));
	accountEmail.setSaltKey(saltKey);
	accountEmail.setCreatedDate(new Date());
	
	return accountEmail;
}






}
