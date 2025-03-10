package com.raybiztech.projecttailoring.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.projecttailoring.business.ProcessArea;
import com.raybiztech.projecttailoring.business.ProcessSubHead;
import com.raybiztech.projecttailoring.business.ProjectTailoring;
import com.raybiztech.projecttailoring.dto.ProcessSubHeadDto;

@Repository("projectTailoringDaoImpl")
public class ProjectTailoringDaoImpl extends DAOImpl implements
		ProjectTailoringDao {

	@Autowired
	DAO dao;

	Logger logger = Logger.getLogger(ProjectTailoringDaoImpl.class);

	@Override
	public ProjectTailoring getProjectTailoring(Long projectId) {
		// TODO Auto-generated method stub
		Criteria criteria=getSessionFactory().getCurrentSession().createCriteria(ProjectTailoring.class);
	    criteria.createAlias("project", "project");
		criteria.add(Restrictions.eq("project.id", projectId));
		return (ProjectTailoring) criteria.uniqueResult();
	}

	/*@Override
	public Boolean checkForDuplicateOrder(Long processHeadId, String order) {
		
		Boolean flag;
		
		if(order.equals("")) {
			order = "0";
		}
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(ProcessSubHead.class);
		criteria.createAlias("processHead", "processHead");
		criteria.add(Restrictions.and(Restrictions.eq("processHead.id", processHeadId), Restrictions.eq("order", Long.valueOf(order))));
		
		if(criteria.uniqueResult()!=null) {
			flag = Boolean.TRUE;
		}
		else {
			flag = Boolean.FALSE;
		}
		return flag;
	}
*/
	@Override
	public List<ProcessSubHead> getProcessSubHeadList(ProcessSubHeadDto processsubHeadDto,Long oldOrder) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(ProcessSubHead.class);
		criteria.createAlias("processHead", "processHead");
		criteria.add(Restrictions.and(Restrictions.eq("status", Boolean.TRUE), Restrictions.eq("processHead.id", processsubHeadDto.getCategoryId())));
		
		if(processsubHeadDto.getStatus().equalsIgnoreCase("false")) {
			criteria.add(Restrictions.ge("order", oldOrder));
		}
		else if(oldOrder != null && processsubHeadDto.getOrder() > oldOrder) {
			criteria.add(Restrictions.and(Restrictions.gt("order", oldOrder),Restrictions.le("order", processsubHeadDto.getOrder())));
		}
			else {
				criteria.add(Restrictions.ge("order", processsubHeadDto.getOrder()));
				}
		
		criteria.addOrder(Order.asc("order"));
		return criteria.list();
	}

	@Override
	public List<ProcessArea> getProcessAreas(Long categoryId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProcessArea.class);
		criteria.createAlias("processHead", "category");
		criteria.add(Restrictions.eq("category.id", categoryId));
		return criteria.list();
	}

	@Override
	public Boolean checkforDuplicateDoc(String docName) {
		Boolean flag = null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProcessSubHead.class);
		criteria.add(Restrictions.like("documentName", docName, MatchMode.EXACT));
		if(criteria.list().size() > 0) {
			flag = Boolean.TRUE;
		}
		else {
			flag = Boolean.FALSE;
		}
		return flag;
	}

	@Override
	public Boolean checkDuplicateProcess(String processName) {
		Boolean flag = null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProcessArea.class);
		criteria.add(Restrictions.like("name", processName, MatchMode.EXACT));
		if(criteria.list().size() > 0) {
			flag = Boolean.TRUE;
		}
		else {
			flag = Boolean.FALSE;
		}
		return flag;
	}

	@Override
	public Long getOrderCountOfActiveProcesses(Long categoryId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProcessSubHead.class);
		criteria.createAlias("processHead", "category");
		criteria.add(Restrictions.eq("category.id", categoryId));
		criteria.add(Restrictions.eq("status", Boolean.TRUE));
		return (long) criteria.list().size();
	}

	
}
