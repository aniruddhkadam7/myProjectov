package com.raybiztech.hiveworkpackages.dto;

import java.io.Serializable;

import com.raybiztech.date.Second;

public class typesDto  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5175620456696887289L;
	
	private Long id;
	private String name;
	private Long position;
	private Integer  is_in_roadmap;
	private Integer  in_aggregation;
	private Integer  is_milestone;
 	private Integer is_default;
 	private Long  	color_id;
 	private String created_at;
	private String updated_at;
	private  Integer is_standard;
	private  String attribute_groups;
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
	public Long getPosition() {
		return position;
	}
	public void setPosition(Long position) {
		this.position = position;
	}
	public Integer getIs_in_roadmap() {
		return is_in_roadmap;
	}
	public void setIs_in_roadmap(Integer is_in_roadmap) {
		this.is_in_roadmap = is_in_roadmap;
	}
	public Integer getIn_aggregation() {
		return in_aggregation;
	}
	public void setIn_aggregation(Integer in_aggregation) {
		this.in_aggregation = in_aggregation;
	}
	public Integer getIs_milestone() {
		return is_milestone;
	}
	public void setIs_milestone(Integer is_milestone) {
		this.is_milestone = is_milestone;
	}
	public Integer getIs_default() {
		return is_default;
	}
	public void setIs_default(Integer is_default) {
		this.is_default = is_default;
	}
	public Long getColor_id() {
		return color_id;
	}
	public void setColor_id(Long color_id) {
		this.color_id = color_id;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	public Integer getIs_standard() {
		return is_standard;
	}
	public void setIs_standard(Integer is_standard) {
		this.is_standard = is_standard;
	}
	public String getAttribute_groups() {
		return attribute_groups;
	}
	public void setAttribute_groups(String attribute_groups) {
		this.attribute_groups = attribute_groups;
	}
	
	
	
	
	

}
