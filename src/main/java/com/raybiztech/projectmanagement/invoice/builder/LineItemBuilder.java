package com.raybiztech.projectmanagement.invoice.builder;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.payslip.utility.AES256Encryption;
import com.raybiztech.projectmanagement.invoice.business.Duration;
import com.raybiztech.projectmanagement.invoice.business.LineItem;
import com.raybiztech.projectmanagement.invoice.business.LineItemAudit;
import com.raybiztech.projectmanagement.invoice.dto.LineItemDTO;
import com.raybiztech.recruitment.utils.DateParser;

@Component("lineItemBuilder")
public class LineItemBuilder {
	private final DAO dao;

	org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(LineItemBuilder.class);

	@Autowired
	public LineItemBuilder(DAO dao) {
		this.dao = dao;
	}

	public Set<LineItem> toEnityList(Set<LineItemDTO> lineItemDTOSet) {

		Set<LineItem> lineItemsSet = null;
		if (lineItemDTOSet != null) {
			lineItemsSet = new HashSet<LineItem>();

			for (LineItemDTO lineItemDTO : lineItemDTOSet) {

				LineItem lineItem = new LineItem();
				lineItem.setComments(lineItemDTO.getComments());
				if (lineItemDTO.getCount() != null) {
					lineItem.setCount(lineItemDTO.getCount());
				}

				if (lineItemDTO.getDuration() != null) {
					lineItem.setDuration(Duration.valueOf(lineItemDTO
							.getDuration()));
				} else {
					lineItem.setDuration(null);
				}
				if (lineItemDTO.getItem() != null) {
					lineItem.setItem(lineItemDTO.getItem());
				}
				if (lineItemDTO.getRate() != null) {
					lineItem.setRate(lineItemDTO.getRate());
				}

				if (lineItemDTO.getEmpId() != null) {
					lineItem.setItem(dao.findBy(Employee.class,
							lineItemDTO.getEmpId()));
				}
				// For Line item the role and description are stored in the
				// description column only
				// if the line item is employee the role gets stored as
				// description
				// if the line item is an item the description itself gets
				// stored as description
				if (lineItemDTO.getRole() != null) {
					lineItem.setDescription(lineItemDTO.getRole());
				}
				if (lineItemDTO.getDescription() != null) {
					lineItem.setDescription(lineItemDTO.getDescription());
				}
				lineItem.setAmount(lineItemDTO.getAmount() != null ? lineItemDTO
						.getAmount() : null);
				
				lineItem.setLineItemAmount(lineItemDTO.getLineItemAmount() !=null ? lineItemDTO.getLineItemAmount(): null);

				if (lineItemDTO.getFromDate() != null) {
					try {
						lineItem.setFromDate(DateParser.toDate(lineItemDTO
								.getFromDate()));
						lineItem.setEndDate(DateParser.toDate(lineItemDTO
								.getEndDate()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				lineItem.setMonthWorkingDays((lineItemDTO.getMonthWorkingDays() != null) ? lineItemDTO
						.getMonthWorkingDays() : null);
				lineItem.setHolidays((lineItemDTO.getHolidays() != null) ? lineItemDTO
						.getHolidays() : null);
				lineItem.setLeaves((lineItemDTO.getLeaves() != null) ? lineItemDTO
						.getLeaves() : null);
				lineItem.setHours((lineItemDTO.getHours() != null) ? lineItemDTO
						.getHours() : null);

				lineItem.setTotalValue((lineItemDTO.getTotalValue() != null) ? lineItemDTO
						.getTotalValue() : null);
				lineItem.setBillableDays((lineItemDTO.getBillableDays() != null) ? lineItemDTO
						.getBillableDays() : null);

				lineItemsSet.add(lineItem);
			}

		}
		return lineItemsSet;

	}

	public Set<LineItemDTO> toDto(Set<LineItem> lineItemset) {
		Set<LineItemDTO> lineItemDTOset = null;
		if (lineItemset != null) {
			lineItemDTOset = new HashSet<LineItemDTO>();
			for (LineItem lineItem : lineItemset) {
				AES256Encryption aes256Encryption = new AES256Encryption(
						String.valueOf(lineItem.getId()), lineItem.getSaltkey());

				LineItemDTO lineitemdto = new LineItemDTO();
				lineitemdto.setId(lineItem.getId());
				lineitemdto.setCount(lineItem.getCount());
				lineitemdto.setComments(lineItem.getComments());
				/*
				 * lineitemdto.setDescription(aes256Encryption.decrypt(lineItem
				 * .getDescription()));
				 */

				lineitemdto
						.setDuration(lineItem.getDuration() != null ? lineItem
								.getDuration().name() : null);
				/*
				 * lineitemdto
				 * .setRole(aes256Encryption.decrypt(lineItem.getRole()));
				 */

				// If the line item is an instance of employee then item is to
				// be given as employee to dto
				if (lineItem.getItem() instanceof Employee) {
					Employee employee = (Employee) lineItem.getItem();

					if (employee != null) {
						lineitemdto.setEmpId(employee.getEmployeeId());
						lineitemdto.setEmpName(employee.getFullName());
						// If the line item is a Employee then description field
						// has to be set to role in lineitem dto

						lineitemdto.setRole(lineItem.getDescription());
					}
				}
				// If the line item is an item then item is to be given as item
				// string to dto
				if (lineItem.getItem() instanceof String) {
					String str = (String) lineItem.getItem();
					lineitemdto.setItem(str);
					// If the line Item is a item then description field has to
					// be set to description attribute in lineitemdto
					lineitemdto.setDescription(lineItem.getDescription());
				}
				lineitemdto.setRate(lineItem.getRate());

				lineitemdto.setAmount(aes256Encryption.decrypt(lineItem
						.getAmount()));
				lineitemdto.setLineItemAmount(aes256Encryption.decrypt(lineItem
						.getLineItemAmount()));
				lineitemdto
						.setFromDate(lineItem.getFromDate() != null ? lineItem
								.getFromDate().toString("dd/MM/yyyy") : null);
				lineitemdto.setEndDate(lineItem.getEndDate() != null ? lineItem
						.getEndDate().toString("dd/MM/yyyy") : null);

				lineitemdto
						.setMonthWorkingDays((lineItem.getMonthWorkingDays() != null) ? lineItem
								.getMonthWorkingDays() : null);
				lineitemdto
						.setHolidays((lineItem.getHolidays() != null) ? lineItem
								.getHolidays() : null);
				lineitemdto.setLeaves((lineItem.getLeaves() != null) ? lineItem
						.getLeaves() : null);

				lineitemdto.setHours((lineItem.getHours() != null) ? lineItem
						.getHours() : null);

				lineitemdto
						.setTotalValue((lineItem.getTotalValue() != null) ? lineItem
								.getTotalValue() : null);
				lineitemdto
						.setBillableDays((lineItem.getBillableDays() != null) ? lineItem
								.getBillableDays() : null);

				lineItemDTOset.add(lineitemdto);
			}

		}
		return lineItemDTOset;
	}

	public LineItem getLineItemEntity(LineItemDTO lineItemDTO) {
		if (lineItemDTO.getId() != null) {
			LineItem line = dao.findBy(LineItem.class, lineItemDTO.getId());
			line.setComments(lineItemDTO.getComments());
			if (lineItemDTO.getCount() != null) {
				line.setCount(lineItemDTO.getCount());
			}
			if (lineItemDTO.getDescription() != null) {
				line.setDescription(lineItemDTO.getDescription());
			}
			if (lineItemDTO.getDuration() != null) {
				line.setDuration(Duration.valueOf(lineItemDTO.getDuration()));
			} else {
				line.setDuration(null);
			}
			if (lineItemDTO.getItem() != null) {
				line.setItem(lineItemDTO.getItem());
			}
			if (lineItemDTO.getRate() != null) {
				line.setRate(lineItemDTO.getRate());
			}

			if (lineItemDTO.getLineItemAmount() != null) {

			}
			if (lineItemDTO.getRole() != null) {
				// line.setRole(lineItemDTO.getRole());
				line.setDescription(lineItemDTO.getRole());
			}

			line.setAmount(lineItemDTO.getAmount() != null ? lineItemDTO
					.getAmount() : null);

			line.setLineItemAmount(lineItemDTO.getLineItemAmount() != null ? lineItemDTO
					.getLineItemAmount() : null);

			line.setMonthWorkingDays((lineItemDTO.getMonthWorkingDays() != null) ? lineItemDTO
					.getMonthWorkingDays() : null);
			line.setHolidays((lineItemDTO.getHolidays() != null) ? lineItemDTO
					.getHolidays() : null);
			line.setLeaves((lineItemDTO.getLeaves() != null) ? lineItemDTO
					.getLeaves() : null);
			line.setTotalValue((lineItemDTO.getTotalValue() != null) ? lineItemDTO
					.getTotalValue() : null);

			line.setHours((lineItemDTO.getHours() != null) ? lineItemDTO
					.getHours() : null);

			line.setBillableDays((lineItemDTO.getBillableDays() != null) ? lineItemDTO
					.getBillableDays() : null);

			try {
				line.setFromDate(lineItemDTO.getFromDate() != null ? DateParser
						.toDate(lineItemDTO.getFromDate()) : null);

				line.setEndDate(lineItemDTO.getEndDate() != null ? DateParser
						.toDate(lineItemDTO.getEndDate()) : null);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return line;

		} else {
			LineItem lineItem = new LineItem();

			lineItem.setComments(lineItemDTO.getComments());
			if (lineItemDTO.getCount() != null) {
				lineItem.setCount(lineItemDTO.getCount());
			}
			if (lineItemDTO.getDescription() != null) {
				lineItem.setDescription(lineItemDTO.getDescription());
			}
			if (lineItemDTO.getDuration() != null) {
				lineItem.setDuration(Duration.valueOf(lineItemDTO.getDuration()));
			} else {
				lineItem.setDuration(null);
			}
			if (lineItemDTO.getItem() != null) {
				lineItem.setItem(lineItemDTO.getItem());
			}
			if (lineItemDTO.getRate() != null) {
				lineItem.setRate(lineItemDTO.getRate());
			}
			if (lineItemDTO.getRole() != null) {
				// lineItem.setRole(lineItemDTO.getRole());
				lineItem.setDescription(lineItemDTO.getRole());
			}
			if (lineItemDTO.getEmpId() != null) {
				lineItem.setItem(dao.findBy(Employee.class,
						lineItemDTO.getEmpId()));
			}

			lineItem.setAmount(lineItemDTO.getAmount() != null ? lineItemDTO
					.getAmount() : null);

			lineItem.setLineItemAmount(lineItemDTO.getLineItemAmount() != null ? lineItemDTO
					.getLineItemAmount() : null);

			lineItem.setMonthWorkingDays((lineItemDTO.getMonthWorkingDays() != null) ? lineItemDTO
					.getMonthWorkingDays() : null);
			lineItem.setHolidays((lineItemDTO.getHolidays() != null) ? lineItemDTO
					.getHolidays() : null);
			lineItem.setLeaves((lineItemDTO.getLeaves() != null) ? lineItemDTO
					.getLeaves() : null);
			lineItem.setHours((lineItemDTO.getHours() != null) ? lineItemDTO
					.getHours() : null);

			lineItem.setTotalValue((lineItemDTO.getTotalValue() != null) ? lineItemDTO
					.getTotalValue() : null);

			lineItem.setBillableDays((lineItemDTO.getBillableDays() != null) ? lineItemDTO
					.getBillableDays() : null);

			try {
				lineItem.setFromDate(lineItemDTO.getFromDate() != null ? DateParser
						.toDate(lineItemDTO.getFromDate()) : null);

				lineItem.setEndDate(lineItemDTO.getEndDate() != null ? DateParser
						.toDate(lineItemDTO.getEndDate()) : null);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return lineItem;
		}

	}

	public Set<LineItem> encryptLineItem(Set<LineItem> lineItems) {

		Set<LineItem> encryptedLineItems = new HashSet<LineItem>();
		for (LineItem item : lineItems) {
			if (item != null) {

				if (item.getSaltkey() == null) {

					String saltKey = KeyGenerators.string().generateKey();
					AES256Encryption aes256Encryption = new AES256Encryption(
							String.valueOf(item.getId()), saltKey);
					item.setSaltkey(saltKey);
					item.setCount(item.getCount());
					item.setMonthWorkingDays(item.getMonthWorkingDays());
					item.setHolidays(item.getHolidays());
					item.setLeaves(item.getLeaves());
					item.setDescription(item.getDescription());
					item.setItem(item.getItem());
					item.setRate(item.getRate());
					item.setAmount(aes256Encryption.encrypt(item.getAmount()));
					item.setLineItemAmount(aes256Encryption.encrypt(item.getLineItemAmount()));
					item.setFromDate(item.getFromDate());
					item.setEndDate(item.getEndDate());
					encryptedLineItems.add(item);
				} else {
					AES256Encryption aes256Encryption = new AES256Encryption(
							String.valueOf(item.getId()), item.getSaltkey());
					item.setCount(item.getCount());
					item.setDescription(item.getDescription());
					item.setMonthWorkingDays(item.getMonthWorkingDays());
					item.setHolidays(item.getHolidays());
					item.setLeaves(item.getLeaves());
					item.setItem(item.getItem());
					item.setRate(item.getRate());
					item.setAmount(aes256Encryption.encrypt(item.getAmount()));
					item.setLineItemAmount(aes256Encryption.encrypt(item.getLineItemAmount()));
					item.setFromDate(item.getFromDate());
					item.setEndDate(item.getEndDate());
					encryptedLineItems.add(item);

				}

			}
		}

		return encryptedLineItems;
	}

	public LineItemAudit getLineItemAudit(LineItem lineItem) {

		LineItemAudit line = null;

		if (lineItem.getId() != null) {
			// AES256Encryption aes256Encryption = new AES256Encryption(
			// String.valueOf(lineItem.getId()), lineItem.getSaltkey());
			line = new LineItemAudit();
			line.setLineItemId(lineItem.getId());
			line.setItemSaltkey(lineItem.getSaltkey());
			line.setCount(lineItem.getCount() != null ? lineItem.getCount()
					: null);

			line.setDescription(lineItem.getDescription() != null ? lineItem
					.getDescription() : null);

			line.setDuration(lineItem.getDuration() != null ? lineItem
					.getDuration().name() : null);

			line.setItem(lineItem.getItem() != null ? lineItem.getItem() : null);

			line.setRate(lineItem.getRate() != null ? lineItem.getRate() : null);

			line.setMonthWorkingDays((lineItem.getMonthWorkingDays() != null) ? lineItem
					.getMonthWorkingDays() : null);

			line.setHolidays((lineItem.getHolidays() != null) ? lineItem
					.getHolidays() : null);

			line.setLeaves((lineItem.getLeaves() != null) ? lineItem
					.getLeaves() : null);

			line.setHours((lineItem.getHours() != null) ? lineItem.getHours()
					: null);

			line.setTotalValue((lineItem.getTotalValue() != null) ? lineItem
					.getTotalValue() : null);

			line.setBillableDays((lineItem.getBillableDays() != null) ? lineItem
					.getBillableDays() : null);

			/*
			 * if (lineItem.getDescription() != null) { //
			 * line.setRole(lineItemDTO.getRole());
			 * line.setDescription(lineItem.getDescription()); }
			 */
			line.setAmount(lineItem.getAmount() != null ? lineItem.getAmount()
					: null);
			
			line.setLineItemAmount(lineItem.getLineItemAmount() != null ? lineItem.getLineItemAmount(): null);

			line.setFromDate(lineItem.getFromDate() != null ? (lineItem
					.getFromDate().toString("dd/MM/yyyy")) : null);

			line.setEndDate(lineItem.getEndDate() != null ? (lineItem
					.getEndDate().toString("dd/MM/yyyy")) : null);

			// return line;

		}/*
		 * else { line = new LineItemAudit(); if (lineItem.getCount() != null) {
		 * line.setCount(lineItem.getCount()); } if (lineItem.getDescription()
		 * != null) { line.setDescription(lineItem.getDescription()); } if
		 * (lineItem.getDuration() != null) {
		 * line.setDuration(lineItem.getDuration().name()); } else {
		 * line.setDuration(null); } if (lineItem.getItem() != null) {
		 * line.setItem(lineItem.getItem()); } if (lineItem.getRate() != null) {
		 * line.setRate(lineItem.getRate()); } if (lineItem.getDescription() !=
		 * null) { // lineItem.setRole(lineItemDTO.getRole());
		 * line.setDescription(lineItem.getDescription()); } if
		 * (lineItem.getItem() != null) { line.setItem(lineItem.getItem()); }
		 * 
		 * lineItem.setAmount(lineItem.getAmount() != null ? lineItem
		 * .getAmount() : null);
		 * 
		 * line.setFromDate(lineItem.getFromDate() != null ? (lineItem
		 * .getFromDate().toString("dd/MM/yyyy")) : null);
		 * 
		 * line.setEndDate(lineItem.getEndDate() != null ? (lineItem
		 * .getEndDate().toString("dd/MM/yyyy")) : null);
		 * 
		 * return line; }
		 */
		return line;

	}
}
