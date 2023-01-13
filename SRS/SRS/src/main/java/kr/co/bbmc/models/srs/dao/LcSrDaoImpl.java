package kr.co.bbmc.models.srs.dao;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.LcSr;




@Transactional
@Component
public class LcSrDaoImpl implements LcSrDao {

    @Autowired
    private SessionFactory sessionFactory;

    
	@Override
	public LcSr get(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<LcSr> list = session.createCriteria(LcSr.class)
				.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}


	@Override
	public List<LcSr> getList(String lc_mac) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<LcSr> list = session.createCriteria(LcSr.class)
				.add(Restrictions.eq("lc_mac", lc_mac)).list()
				;
		
		return list;
	}


	@Override
	public DataSourceResult getList(DataSourceRequest request) {
       	String lc_mac = (String)request.getData().get("lc_mac");
        return request.toDataSourceResult(sessionFactory.getCurrentSession(), LcSr.class, lc_mac);
	}
	@Override
	public DataSourceResult getListGroup(DataSourceRequest request1 , String sr_group) {
       	String lc_mac1 = (String)request1.getData().get("lc_mac");
        return request1.toDataSourceResult(sessionFactory.getCurrentSession(), LcSr.class, lc_mac1 , sr_group);
	}
	

	
	@Override
	public LcSr get(String lc_mac) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<LcSr> list = session.createCriteria(LcSr.class)
				.add(Restrictions.eq("lc_mac", lc_mac)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
	
	@Override
	public LcSr get(String lc_mac,String sr_no) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<LcSr> list = session.createCriteria(LcSr.class)
				.add(Restrictions.eq("lc_mac", lc_mac))
				.add(Restrictions.eq("sr_no", sr_no)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public void saveOrUpdate(LcSr lcSr) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(lcSr);
	}
	
	@Override
	public void saveOrUpdate(List<LcSr> lcSrs) {
		
		Session session = sessionFactory.getCurrentSession();
		
        for (LcSr lcSr : lcSrs) {
        	session.saveOrUpdate(lcSr);

        }
	}

	@Override
	public void delete(LcSr lcSr) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.delete(session.load(LcSr.class, lcSr.getId()));
	}

	@Override
	public void delete(List<LcSr> lcSrs) {
		
		Session session = sessionFactory.getCurrentSession();
		
        for (LcSr lcSr : lcSrs) {
            session.delete(session.load(LcSr.class, lcSr.getId()));
        }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LcSr> getListByStatus(String status) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(LcSr.class)
				.add(Restrictions.eq("status", status)).list();
	}
    
}
