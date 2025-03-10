package com.raybiztech.recruitment.chart;

import java.util.List;
public class StatusChart {
	
	private StatusChartDetails chart;
	private List<StatusData>  data;
	
	public StatusChartDetails getChart() {
		return chart;
	}
	public void setChart(StatusChartDetails chart) {
		this.chart = chart;
	}
	public List<StatusData> getData() {
		return data;
	}
	public void setData(List<StatusData> data) {
		this.data = data;
	}
	
	
	

}
