package com.raybiztech.appraisalmanagement.dto;

public class KPIDto {
	private Long id;

	private String name;

	private String description;
	private Long frequencyId;
	private String frequency;
	private String target;

	private KRADto kraDto;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyId(Long frequencyId) {
		this.frequencyId = frequencyId;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public KRADto getKraDto() {
		return kraDto;
	}

	public void setKraDto(KRADto kraDto) {
		this.kraDto = kraDto;
	}

}
