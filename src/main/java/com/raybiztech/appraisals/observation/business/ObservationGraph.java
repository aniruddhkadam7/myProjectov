package com.raybiztech.appraisals.observation.business;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ObservationGraph implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Object x;
	public List<Integer> y;

	public Object getX() {
		return x;
	}

	public void setX(Object x) {
		this.x = x;
	}

	public List<Integer> getY() {
		return y;
	}

	public void setY(List<Integer> y) {
		this.y = y;
	}

}
