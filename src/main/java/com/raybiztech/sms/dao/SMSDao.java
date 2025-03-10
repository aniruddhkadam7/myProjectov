package com.raybiztech.sms.dao;

import java.util.List;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.meetingrequest.business.MeetingRequest;

public interface SMSDao extends DAO {

	public List<MeetingRequest> getMeetingsWhichStartsInNextFifteenMinutes();

	public String getMeetingSMSAlertContent(String content);

}
