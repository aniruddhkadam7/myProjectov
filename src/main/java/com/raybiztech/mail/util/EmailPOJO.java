package com.raybiztech.mail.util;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("email")
public class EmailPOJO {

	@XStreamAlias("to")
	private String to;
	@XStreamAlias("cc")
	private String cc;
	@XStreamAlias("bcc")
	private String bcc;
	@XStreamAlias("subject")
	private String subject;
	@XStreamAlias("body")
	private String body;

	@XStreamAlias("from")
	private String from;

	@XStreamAlias("path")
	private String path;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "to:" + to + "\ncc:  " + cc + "\nFrom:" + from + "\nbcc:" + bcc
				+ "\nsubject: " + subject + "\nBody : " + body;
	}
}
