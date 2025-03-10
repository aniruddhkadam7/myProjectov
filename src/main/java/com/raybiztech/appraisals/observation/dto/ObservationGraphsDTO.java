package com.raybiztech.appraisals.observation.dto;

import java.util.List;

import com.raybiztech.appraisals.observation.business.ObservationGraph;

public class ObservationGraphsDTO {

	public static List<String> series;
	public static List<ObservationGraph> data;

	public List<String> getSeries() {
		return series;
	}

	public void setSeries(List<String> series) {
		this.series = series;
	}

	public List<ObservationGraph> getData() {
		return data;
	}

	public void setData(List<ObservationGraph> data) {
		this.data = data;
	}

}
