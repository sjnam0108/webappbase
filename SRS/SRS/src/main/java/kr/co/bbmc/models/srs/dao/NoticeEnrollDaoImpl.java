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
import kr.co.bbmc.models.srs.NoticeEnroll;
import kr.co.bbmc.models.srs.CntState;

import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
@Transactional
@Component
public class NoticeEnrollDaoImpl implements NoticeEnrollDao {

    @Autowired
    private SessionFactory sessionFactory;

    
	@Override
	public NoticeEnroll get(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<NoticeEnroll> list = session.createCriteria(NoticeEnroll.class)
				.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public List<NoticeEnroll> getList(String lc_name) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<NoticeEnroll> list = session.createCriteria(NoticeEnroll.class)
				.add(Restrictions.eq("lc_name", lc_name)).list();
		
		return list;
	}
	@Override
	public List<NoticeEnroll> getListByNoticeName(String notice_name) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<NoticeEnroll> list = session.createCriteria(NoticeEnroll.class)
				.add(Restrictions.eq("notice_name", notice_name)).list();
		
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<NoticeEnroll> getList() {
		
		return sessionFactory.getCurrentSession().createCriteria(NoticeEnroll.class).list();
	}

	@Override
	public void saveOrUpdate(NoticeEnroll noticeEnroll) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(noticeEnroll);
		
	}

	@Override
	public void delete(NoticeEnroll noticeEnroll) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.delete(session.load(NoticeEnroll.class, noticeEnroll.getId()));
	}

	@Override
	public void delete(List<NoticeEnroll> noticeEnrolls) {
		
		Session session = sessionFactory.getCurrentSession();
		
        for (NoticeEnroll noticeEnroll : noticeEnrolls) {
            session.delete(session.load(NoticeEnroll.class, noticeEnroll.getId()));
        }
	}

	@Override
	public DataSourceResult getListNotice(DataSourceRequest request,String type) {
		
		return request.NoticeDataSourceResult(sessionFactory.getCurrentSession(),NoticeEnroll.class, type);
	}
	
	

	@Override
	public DataSourceResult getList(DataSourceRequest request, 
			boolean isEffectiveMode, boolean isPlusReviewMode) {
		
		HashMap<String, Class<?>> map = new HashMap<String, Class<?>>();
		
        return request.toEffectiveMemberDataSourceResult(sessionFactory.getCurrentSession(), 
        		NoticeEnroll.class, map, isEffectiveMode, isPlusReviewMode);
	}

	@Override
	public NoticeEnroll get(String notice_name) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<NoticeEnroll> list = session.createCriteria(NoticeEnroll.class)
				.add(Restrictions.eq("notice_name", notice_name)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NoticeEnroll> getListByStatus(String status) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(NoticeEnroll.class)
				.add(Restrictions.eq("status", status)).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<NoticeEnroll> getSelectOption() {
	    	Session session = sessionFactory.getCurrentSession();

    		List<NoticeEnroll> results = session.createCriteria(NoticeEnroll.class)
    				.add(Restrictions.eq("lc_name", "")).list();
            
    		return results;
	}
    
}
