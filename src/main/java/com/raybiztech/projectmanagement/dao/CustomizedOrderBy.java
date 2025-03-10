package com.raybiztech.projectmanagement.dao;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;

public class CustomizedOrderBy extends Order {
    /**
	 * @author : Venkatesh Urlana 
	 */
	private static final long serialVersionUID = 1L;
	private String sqlExpression;
 
    protected CustomizedOrderBy(String sqlExpression) {
        super(sqlExpression, true);
        this.sqlExpression = sqlExpression;
    }
 
 
    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return sqlExpression;
    }
 
    public static Order sqlFormula(String sqlFormula) {
        return new CustomizedOrderBy(sqlFormula);
    }
 
    public String toString() {
        return sqlExpression;
    }
 
}