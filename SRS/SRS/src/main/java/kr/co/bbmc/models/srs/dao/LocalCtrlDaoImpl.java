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
import kr.co.bbmc.models.srs.LocalCtrl;
import kr.co.bbmc.models.srs.CntState;
import kr.co.bbmc.models.srs.LcSr;

import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
@Transactional
@Component
public class LocalCtrlDaoImpl implements LocalCtrlDao {

    @Autowired
    private SessionFactory sessionFactory;

    
	@Override
	public LocalCtrl get(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<LocalCtrl> list = session.createCriteria(LocalCtrl.class)
				.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public List<LocalCtrl> getList(String lc_mac) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<LocalCtrl> list = session.createCriteria(LocalCtrl.class)
				.add(Restrictions.eq("lc_mac", lc_mac)).list();
		
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LocalCtrl> getList() {
		
		return sessionFactory.getCurrentSession().createCriteria(LocalCtrl.class).list();
	}

	@Override
	public void saveOrUpdate(LocalCtrl localCtrl) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(localCtrl);
		
	}

	@Override
	public void delete(LocalCtrl localCtrl) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.delete(session.load(LocalCtrl.class, localCtrl.getId()));
	}

	@Override
	public void delete(List<LocalCtrl> localCtrls) {
		
		Session session = sessionFactory.getCurrentSession();
		
        for (LocalCtrl localCtrl : localCtrls) {
            session.delete(session.load(LocalCtrl.class, localCtrl.getId()));
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
        		LocalCtrl.class, map, isEffectiveMode, isPlusReviewMode);
	}

	@Override
	public LocalCtrl get(String lc_mac) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<LocalCtrl> list = session.createCriteria(LocalCtrl.class)
				.add(Restrictions.eq("lc_mac", lc_mac)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LocalCtrl> getListByStatus(String status) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(LocalCtrl.class)
				.add(Restrictions.eq("status", status)).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LocalCtrl> getLocalStateCount() {
	    	Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(LocalCtrl.class).setProjection(Projections.projectionList()
                     		.add(Projections.groupProperty("lc_state"))
                            .add(Projections.count("lc_state")));
            List results = criteria.list();
            

    		return results;
	}
    
}
