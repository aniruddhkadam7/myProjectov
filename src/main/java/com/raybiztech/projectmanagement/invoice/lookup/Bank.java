package com.raybiztech.projectmanagement.invoice.lookup;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author user
 *
 */
public class Bank implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 883551829414102990L;

	private Integer id;
	private String name;

	public Bank() {

	}

	public Bank(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.getId()).toHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Bank) {
			final Bank bank = (Bank) obj;
			return new EqualsBuilder().append(this.id, bank.getId()).isEquals();
		}
		return false;

	}

	@Override
	public String toString() {
		return "Bank [id=" + id + ", name=" + name + "]";
	}

}
