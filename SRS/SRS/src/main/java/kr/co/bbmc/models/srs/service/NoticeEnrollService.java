package kr.co.bbmc.models.srs.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.NoticeEnroll;

import kr.co.bbmc.models.srs.CntState;
import kr.co.bbmc.models.srs.LocalCtrl;



@Transactional
public interface NoticeEnrollService {

	// Common
	public void flush();
	

	//
	// for LocaCtrl Dao
	//
	// Common
	public NoticeEnroll getNoticeEnroll(int id);
	public List<NoticeEnroll> getNoticeList(String lc_name);
	public List<NoticeEnroll> getNoticeListByNoticeName(String notice_name);
	public List<NoticeEnroll> getNoticeEnrollList();
	public List<NoticeEnroll> getSelectOption();
	public void saveOrUpdate(NoticeEnroll noticeEnroll);
	public void deleteNoticeEnroll(NoticeEnroll noticeEnroll);
	public void deleteNoticeEnrolls(List<NoticeEnroll> noticeEnrolls);

	// for Kendo Grid Remote Read
	public DataSourceResult getNoticeEnrollList(DataSourceRequest request,String type);


	// for NoticeEnroll specific
	public NoticeEnroll getNoticeEnroll(String notice_name);
	public List<NoticeEnroll> getNoticeEnrollListByStatus(String status);



	
	
}
