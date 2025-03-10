package com.raybiztech.projectmanagement.invoice.exception;

import com.raybiztech.projectmanagement.exceptions.ProjectManagementException;


public class InvoiceException {

	public class NotAddedException extends ProjectManagementException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3640190065283688598L;

		public NotAddedException() {

			super();

		}

		public NotAddedException(String message) {
			super(message);
		}

	}

	public class NotUpdatedException extends ProjectManagementException {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8811409298649943818L;

		public NotUpdatedException() {

			super();
		}

		public NotUpdatedException(String message) {
			super(message);
		}

	}
	
	public class PdfNotGenereated extends ProjectManagementException {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8811409298649943818L;

		public PdfNotGenereated() {

			super();
		}

		public PdfNotGenereated(String message) {
			super(message);
		}

	}

}
