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
import kr.co.bbmc.models.srs.FreezPredic;
import kr.co.bbmc.models.srs.CntState;

import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
@Transactional
@Component
public class FreezPredicImpl implements FreezPredicDao {

    @Autowired
    private SessionFactory sessionFactory;

    
	@Override
	public FreezPredic get(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<FreezPredic> list = session.createCriteria(FreezPredic.class)
				.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public List<FreezPredic> getList(String lc_name) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<FreezPredic> list = session.createCriteria(FreezPredic.class)
				.add(Restrictions.eq("lc_name", lc_name)).list();
		
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FreezPredic> getList() {
		
		return sessionFactory.getCurrentSession().createCriteria(FreezPredic.class).list();
	}

	@Override
	public void saveOrUpdate(FreezPredic FreezPredic) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(FreezPredic);
		
	}

	@Override
	public void delete(FreezPredic FreezPredic) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.delete(session.load(FreezPredic.class, FreezPredic.getId()));
	}

	@Override
	public void delete(List<FreezPredic> FreezPredics) {
		
		Session session = sessionFactory.getCurrentSession();
		
        for (FreezPredic FreezPredic : FreezPredics) {
            session.delete(session.load(FreezPredic.class, FreezPredic.getId()));
        }
	}

	@Override
	public DataSourceResult getListFreezPredic(DataSourceRequest request) {
		
		return request.toDataSourceResult(sessionFactory.getCurrentSession(),FreezPredic.class);
	}
	
	

	@Override
	public DataSourceResult getList(DataSourceRequest request, 
			boolean isEffectiveMode, boolean isPlusReviewMode) {
		
		HashMap<String, Class<?>> map = new HashMap<String, Class<?>>();
		
        return request.toEffectiveMemberDataSourceResult(sessionFactory.getCurrentSession(), 
        		FreezPredic.class, map, isEffectiveMode, isPlusReviewMode);
	}

	@Override
	public FreezPredic get(String lc_name) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<FreezPredic> list = session.createCriteria(FreezPredic.class)
				.add(Restrictions.eq("lc_name", lc_name)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FreezPredic> getListByStatus(String status) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(FreezPredic.class)
				.add(Restrictions.eq("status", status)).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FreezPredic> getSelectOption() {
	    	Session session = sessionFactory.getCurrentSession();

    		List<FreezPredic> results = session.createCriteria(FreezPredic.class)
    				.add(Restrictions.eq("lc_name", "")).list();
            
    		return results;
	}
    
}
