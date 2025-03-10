package com.raybiztech.hiveworkpackages.business;

import java.io.Serializable;

public class statuses implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8859942121435056030L;
	
	private Long id;
	private String name;
	private Integer is_closed;
	private Integer is_default;
	private Long  	position;
	private Long default_done_ratio;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getIs_closed() {
		return is_closed;
	}
	public void setIs_closed(Integer is_closed) {
		this.is_closed = is_closed;
	}
	public Integer getIs_default() {
		return is_default;
	}
	public void setIs_default(Integer is_default) {
		this.is_default = is_default;
	}
	public Long getPosition() {
		return position;
	}
	public void setPosition(Long position) {
		this.position = position;
	}
	public Long getDefault_done_ratio() {
		return default_done_ratio;
	}
	public void setDefault_done_ratio(Long default_done_ratio) {
		this.default_done_ratio = default_done_ratio;
	}
	
	
	


}
