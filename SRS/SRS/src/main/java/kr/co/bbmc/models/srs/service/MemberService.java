package kr.co.bbmc.models.srs.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.Member;


@Transactional
public interface MemberService {

	// Common
	public void flush();
	
	
	//
	// for Member Dao
	//
	// Common
	public Member getMember(int id);
	public List<Member> getMemberList();
	public void saveOrUpdate(Member member);
	public void deleteMember(Member member);
	public void deleteMembers(List<Member> members);

	// for Kendo Grid Remote Read
	public DataSourceResult getMemberList(DataSourceRequest request);
	public DataSourceResult getMemberList(DataSourceRequest request, 
			boolean isEffectiveMode, boolean isPlusReviewMode);

	// for Member specific
	public Member getMember(String ukid);
	public List<Member> getMemberListByStatus(String status);
	

}
