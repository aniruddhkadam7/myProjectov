package com.raybiztech.leavemanagement.business;

import java.io.Serializable;

public class PayrollCutOffDates implements Serializable {
	 private static final long serialVersionUID = 8075382522652624666L;
	    private Long id;
	    private String cutOffDates;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getCutOffDates() {
			return cutOffDates;
		}
		public void setCutOffDates(String cutOffDates) {
			this.cutOffDates = cutOffDates;
		}



}
