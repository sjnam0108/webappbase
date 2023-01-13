package kr.co.bbmc.models.srs.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.Member;

import kr.co.bbmc.models.srs.dao.MemberDao;


@Transactional
@Service("memService")
public class MemberServiceImpl implements MemberService {

    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    private MemberDao memDao;


    
	@Override
	public void flush() {

		sessionFactory.getCurrentSession().flush();
	}
    

	
	//
	// for Member Dao
	//

	@Override
	public Member getMember(int id) {
		
		return memDao.get(id);
	}

	@Override
	public List<Member> getMemberList() {
		
		return memDao.getList();
	}

	@Override
	public void saveOrUpdate(Member member) {
		
		memDao.saveOrUpdate(member);
	}

	@Override
	public void deleteMember(Member member) {
		
		memDao.delete(member);
	}

	@Override
	public void deleteMembers(List<Member> members) {
		
		memDao.delete(members);
	}

	@Override
	public DataSourceResult getMemberList(DataSourceRequest request) {
		
		return memDao.getList(request);
	}

	@Override
	public DataSourceResult getMemberList(DataSourceRequest request, 
			boolean isEffectiveMode, boolean isPlusReviewMode) {
		
		return memDao.getList(request, isEffectiveMode, isPlusReviewMode);
	}

	@Override
	public Member getMember(String ukid) {
		
		return memDao.get(ukid);
	}

	@Override
	public List<Member> getMemberListByStatus(String status) {
		
		return memDao.getListByStatus(status);
	}



}
