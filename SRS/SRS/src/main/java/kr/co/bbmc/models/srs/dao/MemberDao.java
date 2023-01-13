package kr.co.bbmc.models.srs.dao;

import java.util.List;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.Member;

public interface MemberDao {

	// Common
	public Member get(int id);
	public List<Member> getList();
	public void saveOrUpdate(Member member);
	public void delete(Member member);
	public void delete(List<Member> members);

	// for Kendo Grid Remote Read
	public DataSourceResult getList(DataSourceRequest request);
	public DataSourceResult getList(DataSourceRequest request, 
			boolean isEffectiveMode, boolean isPlusReviewMode);

	// for Member specific
	public Member get(String ukid);
	public List<Member> getListByStatus(String status);
}
