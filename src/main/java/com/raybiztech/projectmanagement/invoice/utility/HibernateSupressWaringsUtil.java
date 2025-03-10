package com.raybiztech.projectmanagement.invoice.utility;

import java.util.List;

import org.hibernate.Criteria;

public class HibernateSupressWaringsUtil {

	@SuppressWarnings("unchecked")
	public static <T> List<T> listAndCast(Criteria crit) {
		List<T> list = crit.list();
		return list;
	}
	
}
