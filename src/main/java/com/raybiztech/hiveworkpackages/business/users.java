package com.raybiztech.hiveworkpackages.business;

import java.io.Serializable;

import com.raybiztech.date.Second;

public class users implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6481632914168020996L;

	private Long id;
	private String login;
	private String firstname;
	private String lastname;
	private String mail;
	private Integer admin;
	private Integer status;
	private Second last_login_on;
	private String language;
	private Integer auth_source_id;
	private Second created_on;
	private Second updated_on;
	private String type;
	private String identity_url;
	private String mail_notification;
	private Integer first_login;
	private Integer force_password_change;
	private Long failed_login_count;
	private Second last_failed_login_on;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public Integer getAdmin() {
		return admin;
	}

	public void setAdmin(Integer admin) {
		this.admin = admin;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Second getLast_login_on() {
		return last_login_on;
	}

	public void setLast_login_on(Second last_login_on) {
		this.last_login_on = last_login_on;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Integer getAuth_source_id() {
		return auth_source_id;
	}

	public void setAuth_source_id(Integer auth_source_id) {
		this.auth_source_id = auth_source_id;
	}

	public Second getCreated_on() {
		return created_on;
	}

	public void setCreated_on(Second created_on) {
		this.created_on = created_on;
	}

	public Second getUpdated_on() {
		return updated_on;
	}

	public void setUpdated_on(Second updated_on) {
		this.updated_on = updated_on;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIdentity_url() {
		return identity_url;
	}

	public void setIdentity_url(String identity_url) {
		this.identity_url = identity_url;
	}

	public String getMail_notification() {
		return mail_notification;
	}

	public void setMail_notification(String mail_notification) {
		this.mail_notification = mail_notification;
	}

	public Integer getFirst_login() {
		return first_login;
	}

	public void setFirst_login(Integer first_login) {
		this.first_login = first_login;
	}

	public Integer getForce_password_change() {
		return force_password_change;
	}

	public void setForce_password_change(Integer force_password_change) {
		this.force_password_change = force_password_change;
	}

	public Long getFailed_login_count() {
		return failed_login_count;
	}

	public void setFailed_login_count(Long failed_login_count) {
		this.failed_login_count = failed_login_count;
	}

	public Second getLast_failed_login_on() {
		return last_failed_login_on;
	}

	public void setLast_failed_login_on(Second last_failed_login_on) {
		this.last_failed_login_on = last_failed_login_on;
	}

}
