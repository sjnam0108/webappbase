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
import kr.co.bbmc.models.srs.AutoCtrl;
import kr.co.bbmc.models.srs.CntState;
import kr.co.bbmc.models.srs.LcSr;
import kr.co.bbmc.models.srs.NoticeEnroll;

import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
@Transactional
@Component
public class AutoCtrlDaoImpl implements AutoCtrlDao {

    @Autowired
    private SessionFactory sessionFactory;

    
	@Override
	public AutoCtrl get(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<AutoCtrl> list = session.createCriteria(AutoCtrl.class)
				.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public List<AutoCtrl> getList(String lc_mac) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<AutoCtrl> list = session.createCriteria(AutoCtrl.class)
				.add(Restrictions.eq("lc_mac", lc_mac)).list();
		
		return list;
	}
	
	@Override
	public List<AutoCtrl> getListLcName(String lc_name) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<AutoCtrl> list = session.createCriteria(AutoCtrl.class)
				.add(Restrictions.eq("lc_name", lc_name)).list();
		
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AutoCtrl> getList() {
		
		return sessionFactory.getCurrentSession().createCriteria(AutoCtrl.class).list();
	}

	@Override
	public void saveOrUpdate(AutoCtrl autoCtrl) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(autoCtrl);
		
	}

	@Override
	public void delete(AutoCtrl autoCtrl) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.delete(session.load(AutoCtrl.class, autoCtrl.getId()));
	}

	@Override
	public void delete(List<AutoCtrl> autoCtrls) {
		
		Session session = sessionFactory.getCurrentSession();
		
        for (AutoCtrl autoCtrl : autoCtrls) {
            session.delete(session.load(AutoCtrl.class, autoCtrl.getId()));
        }
	}

	@Override
	public DataSourceResult getList(DataSourceRequest request) {
		
		return getList(request, false, false);
	}

	@Override
	public DataSourceResult getList(DataSourceRequest request, 
			boolean isEffectiveMode, boolean isPlusReviewMode) {
		
		HashMap<String, Class<?>> map = new HashMap<String, Class<?>>();
		
        return request.toEffectiveMemberDataSourceResult(sessionFactory.getCurrentSession(), 
        		AutoCtrl.class, map, isEffectiveMode, isPlusReviewMode);
	}

	@Override
	public AutoCtrl get(String lc_mac) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<AutoCtrl> list = session.createCriteria(AutoCtrl.class)
				.add(Restrictions.eq("lc_mac", lc_mac)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AutoCtrl> getListByStatus(String status) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(AutoCtrl.class)
				.add(Restrictions.eq("status", status)).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AutoCtrl> getLocalStateCount() {
	    	Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(AutoCtrl.class).setProjection(Projections.projectionList()
                     		.add(Projections.groupProperty("lc_state"))
                            .add(Projections.count("lc_state")));
            List results = criteria.list();
            

    		return results;
	}
    
}
