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
import kr.co.bbmc.models.srs.Member;

@Transactional
@Component
public class MemberDaoImpl implements MemberDao {

    @Autowired
    private SessionFactory sessionFactory;

    
	@Override
	public Member get(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Member> list = session.createCriteria(Member.class)
				.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Member> getList() {
		
		return sessionFactory.getCurrentSession().createCriteria(Member.class).list();
	}

	@Override
	public void saveOrUpdate(Member member) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(member);
	}

	@Override
	public void delete(Member member) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.delete(session.load(Member.class, member.getId()));
	}

	@Override
	public void delete(List<Member> members) {
		
		Session session = sessionFactory.getCurrentSession();
		
        for (Member member : members) {
            session.delete(session.load(Member.class, member.getId()));
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
        		Member.class, map, isEffectiveMode, isPlusReviewMode);
	}

	@Override
	public Member get(String ukid) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Member> list = session.createCriteria(Member.class)
				.add(Restrictions.eq("ukid", ukid)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Member> getListByStatus(String status) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(Member.class)
				.add(Restrictions.eq("status", status)).list();
	}
    
}
