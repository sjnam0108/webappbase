package kr.co.bbmc.models.srs.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.AppSpeedMgr;
import kr.co.bbmc.models.srs.CntState;

import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
@Transactional
@Component
public class AppSpeedMgrDaoImpl implements AppSpeedMgrDao {

    @Autowired
    private SessionFactory sessionFactory;

    
	@Override
	public AppSpeedMgr get(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<AppSpeedMgr> list = session.createCriteria(AppSpeedMgr.class)
				.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public List<AppSpeedMgr> getList(String lc_name) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<AppSpeedMgr> list = session.createCriteria(AppSpeedMgr.class)
				.add(Restrictions.eq("lc_name", lc_name)).list();
		
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AppSpeedMgr> getList() {
		
		return sessionFactory.getCurrentSession().createCriteria(AppSpeedMgr.class).list();
	}

	@Override
	public void saveOrUpdate(AppSpeedMgr AppSpeedMgr) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(AppSpeedMgr);
		
	}

	@Override
	public void delete(AppSpeedMgr AppSpeedMgr) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.delete(session.load(AppSpeedMgr.class, AppSpeedMgr.getId()));
	}

	@Override
	public void delete(List<AppSpeedMgr> AppSpeedMgrs) {
		
		Session session = sessionFactory.getCurrentSession();
		
        for (AppSpeedMgr AppSpeedMgr : AppSpeedMgrs) {
            session.delete(session.load(AppSpeedMgr.class, AppSpeedMgr.getId()));
        }
	}

	@Override
	public DataSourceResult getListAppSpeedMgr(DataSourceRequest request) {
		
		return request.toDataSourceResult(sessionFactory.getCurrentSession(),AppSpeedMgr.class);
	}
	
	

	@Override
	public DataSourceResult getList(DataSourceRequest request, 
			boolean isEffectiveMode, boolean isPlusReviewMode) {
		
		HashMap<String, Class<?>> map = new HashMap<String, Class<?>>();
		
        return request.toEffectiveMemberDataSourceResult(sessionFactory.getCurrentSession(), 
        		AppSpeedMgr.class, map, isEffectiveMode, isPlusReviewMode);
	}

	@Override
	public AppSpeedMgr get(String notice_name) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<AppSpeedMgr> list = session.createCriteria(AppSpeedMgr.class)
				.add(Restrictions.eq("notice_name", notice_name)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AppSpeedMgr> getListByStatus(String status) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(AppSpeedMgr.class)
				.add(Restrictions.eq("status", status)).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AppSpeedMgr> getSelectOption() {
	    	Session session = sessionFactory.getCurrentSession();

    		List<AppSpeedMgr> results = session.createCriteria(AppSpeedMgr.class)
    				.add(Restrictions.eq("lc_name", "")).list();
            
    		return results;
	}
    
}
