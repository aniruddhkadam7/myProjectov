package com.raybiztech.separation.chart;

import java.util.List;

public class SeparationChart {

	private ChartDetails chart;
	private List<SeparationData> data;

	public ChartDetails getChart() {
		return chart;
	}

	public void setChart(ChartDetails chart) {
		this.chart = chart;
	}

	public List<SeparationData> getData() {
		return data;
	}

	public void setData(List<SeparationData> data) {
		this.data = data;
	}

}
