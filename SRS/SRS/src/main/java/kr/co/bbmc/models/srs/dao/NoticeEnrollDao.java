package kr.co.bbmc.models.srs.dao;

import java.util.List;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.LcSr;
import kr.co.bbmc.models.srs.NoticeEnroll;

public interface NoticeEnrollDao {

	// Common
	public NoticeEnroll get(int id);
	public List<NoticeEnroll> getList();
	public List<NoticeEnroll> getList(String lc_name);
	public List<NoticeEnroll> getListByNoticeName(String notice_name);
	public void saveOrUpdate(NoticeEnroll noticeEnroll);
	public void delete(NoticeEnroll noticeEnroll);
	public void delete(List<NoticeEnroll> noticeEnroll);

	// for Kendo Grid Remote Read
	public DataSourceResult getListNotice(DataSourceRequest request,String type);
	public DataSourceResult getList(DataSourceRequest request, 
			boolean isEffectiveMode, boolean isPlusReviewMode);

	// for Member specific
	public NoticeEnroll get(String notice_name);
	public List<NoticeEnroll> getListByStatus(String status);
	public List<NoticeEnroll> getSelectOption();
}
