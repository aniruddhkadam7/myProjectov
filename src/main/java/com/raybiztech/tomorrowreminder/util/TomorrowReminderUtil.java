package com.raybiztech.tomorrowreminder.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hornetq.utils.json.JSONException;
import org.hornetq.utils.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.raybiztech.dayreminder.dao.DayReminderDao;
import com.raybiztech.recruitment.business.CandidateInterviewCycle;

@Component("TomorrowReminderUtil")
public class TomorrowReminderUtil {
	@Autowired
	  DayReminderDao dayReminderDao;


	Logger logger = Logger.getLogger(TomorrowReminderUtil.class);
	@Transactional
	public void sendTemplateReminderforTomorrow() {
		System.out.println("in whatsapp reminder");

		List<CandidateInterviewCycle> candidateInterviewcycle = dayReminderDao.getWhatsappNumbersofCandidateforTomorrow();
		List<CandidateInterviewCycle> candidateInterviewcycle1 = candidateInterviewcycle;
		List<CandidateInterviewCycle> remove = new ArrayList<CandidateInterviewCycle>();
		
		 //we are checking duplicate date for candidate interview status
		      if (candidateInterviewcycle.size() > 0) {
		    	  for(CandidateInterviewCycle can:candidateInterviewcycle){
		    		  for(CandidateInterviewCycle can1 : candidateInterviewcycle1){
		    			  if(can.getCandidate().getPersonId() == can1.getCandidate().getPersonId()){
		    				  if(can.getInterviewCycleId()> can1.getInterviewCycleId()){
		    					  remove.add(can1);
		    				  }
		    			  }
		    		  }
		    	  }
		      } 
		      if(remove!=null){
		      candidateInterviewcycle.removeAll(remove);
		      }
		
 
		   if(candidateInterviewcycle!=null)
		    {
			for(CandidateInterviewCycle list: candidateInterviewcycle){
				System.out.println("time date:" + list.getInterviewTime() + list.getInterviewDate() + list.getCandidate().getFullName() + list.getInterviewCycleId());
			
                if(list.getCandidate().getNotifications()!=null && list.getCandidate().getNotifications().equalsIgnoreCase("Yes")){
                	if(list.getInterviewResultStatus()==null){
			    String to="";
				if(list.getCandidate().getCountry().getName().equalsIgnoreCase("INDIA")){
					to=91+list.getCandidate().getMobile();
				 }
				if(list.getCandidate().getCountry().getName().equalsIgnoreCase("USA")){
					to=1+list.getCandidate().getMobile();
				 }
				if(list.getCandidate().getCountry().getName().equalsIgnoreCase("CANADA")){
					to=1+list.getCandidate().getMobile();
				 }
				if(list.getCandidate().getCountry().getName().equalsIgnoreCase("PHILIPPINES")){
					to=63+list.getCandidate().getMobile();
				 }
				if(list.getCandidate().getCountry().getName().equalsIgnoreCase("AUSTRALIA")){
					to=61+list.getCandidate().getMobile();
				 }
				
		    URL url = null;
	        System.out.println("in schedule notification");
	        try {
	           
	            url = new URL("https://api.kaleyra.io/v1/HXIN1720279606IN/messages");
	            System.out.println("url is ok");
	        } catch (MalformedURLException e2) {
	            // TODO Auto-generated catch block
	            e2.printStackTrace();
	        }
	        String templateParams ='\"' + list.getCandidate().getFullName()+ '\"' + ',' +'\"' + list.getInterviewDate() + '\"' + ',' + '\"' + list.getInterviewTime() + '\"';
	        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
	        map.put("from", "919206697777");
	        map.put("to", to);
	        map.put("type", "template");
	        map.put("channel", "Whatsapp");
	        map.put("template_name","reminder1_1daybeforeinterview_n");
	        map.put("params",templateParams);
	        map.put("lang_code","en");
	        StringBuilder postData = new StringBuilder();
	        for (Map.Entry param : map.entrySet()) {
	            if (postData.length() != 0)
	                postData.append('&');
	            try {
	                postData.append(URLEncoder.encode((String) param.getKey(), "UTF-8"));
	            } catch (UnsupportedEncodingException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	            postData.append('=');
	            try {
	                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
	            } catch (UnsupportedEncodingException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
	        System.out.println("appending parameters is ok");
	        byte[] postDataBytes = null;
	        try {
	            postDataBytes = postData.toString().getBytes("UTF-8");
	        } catch (UnsupportedEncodingException e2) {
	            // TODO Auto-generated catch block
	            e2.printStackTrace();
	        }
	        HttpURLConnection conn = null;
	        try {
	            conn = (HttpURLConnection)url.openConnection();
	            System.out.println("opening url connection is ok");
	        } catch (IOException e1) {
	            // TODO Auto-generated catch block
	            e1.printStackTrace();
	        }
	        try {
	            conn.setRequestMethod("POST");
	            System.out.println("opening post is ok");
	        } catch (ProtocolException e1) {
	            // TODO Auto-generated catch block
	            e1.printStackTrace();
	        }
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        System.out.println("content type is ok");
	        conn.setRequestProperty("api-key", "A9ecaaba986f12c06b781710430a213cc");
	        System.out.println("api key is ok");
	        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
	        System.out.println("content length is ok");
	        conn.setDoOutput(true);
	        try {
	            conn.getOutputStream().write(postDataBytes);
	            System.out.println("conn output stream is ok");
	        } catch (IOException e2) {
	            // TODO Auto-generated catch block
	            e2.printStackTrace();
	        }
	        try {
	            System.out.println("input stream:" + conn.getErrorStream() + conn.getResponseCode());
	        } catch (IOException e2) {
	            // TODO Auto-generated catch block
	            e2.printStackTrace();
	        }
	        Reader in = null;
	        try {
	            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	            System.out.println("in:" + in);
	        } catch (UnsupportedEncodingException e1) {
	            // TODO Auto-generated catch block
	            e1.printStackTrace();
	        } catch (IOException e1) {
	            // TODO Auto-generated catch block
	            e1.printStackTrace();
	        }
	        StringBuilder sb = new StringBuilder();
	        try {
	            for (int c; (c = in.read()) >= 0;)
	            		sb.append((char)c);
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        String response = sb.toString();
	        System.out.println(response);
	        JSONObject myResponse =null;
	        try {
	            myResponse = new JSONObject(response.toString());
	        } catch (JSONException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	     System.out.println("end of schedule interview");  

		}

		    }
	}
}
}
}
	


	
