package kr.co.bbmc.models.srs.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;

import kr.co.bbmc.models.srs.NoticeEnroll;
import kr.co.bbmc.models.srs.dao.NoticeEnrollDao;



@Transactional
@Service("NoticeEnrollService")
public class NoticeEnrollServiceImpl implements NoticeEnrollService {

    @Autowired
    private SessionFactory sessionFactory;
   

    
    @Autowired
    private NoticeEnrollDao noticeEnrollDao;

    
	@Override
	public void flush() {

		sessionFactory.getCurrentSession().flush();
	}
   




	//
	// for NoticeEnroll Dao
	//
	
	
	@Override
	public List<NoticeEnroll> getNoticeList(String lc_name) {
		// TODO Auto-generated method stub
		return noticeEnrollDao.getList(lc_name);
	}
	
	@Override
	public List<NoticeEnroll> getNoticeListByNoticeName(String notice_name) {
		// TODO Auto-generated method stub
		return noticeEnrollDao.getListByNoticeName(notice_name);
	}
	
	@Override
	public NoticeEnroll getNoticeEnroll(int id) {
		// TODO Auto-generated method stub
		return noticeEnrollDao.get(id);
	}



	@Override
	public List<NoticeEnroll> getNoticeEnrollList() {
		// TODO Auto-generated method stub
		return noticeEnrollDao.getList();
	}



	@Override
	public void saveOrUpdate(NoticeEnroll noticeEnroll) {
		// TODO Auto-generated method stub
		noticeEnrollDao.saveOrUpdate(noticeEnroll);
	}

	@Override
	public void deleteNoticeEnroll(NoticeEnroll localCtrl) {
		// TODO Auto-generated method stub
		noticeEnrollDao.delete(localCtrl);
	}




	@Override
	public void deleteNoticeEnrolls(List<NoticeEnroll> localCtrls) {
		// TODO Auto-generated method stub
		noticeEnrollDao.delete(localCtrls);
	}


	@Override
	public DataSourceResult getNoticeEnrollList(DataSourceRequest request,String type) {
		// TODO Auto-generated method stub
		return noticeEnrollDao.getListNotice(request,type);
	}



	@Override
	public NoticeEnroll getNoticeEnroll(String notice_name) {
		// TODO Auto-generated method stub
		return noticeEnrollDao.get(notice_name);
	}



	@Override
	public List<NoticeEnroll> getNoticeEnrollListByStatus(String status) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public List<NoticeEnroll> getSelectOption() {
		// TODO Auto-generated method stub
		return noticeEnrollDao.getSelectOption();
	}



}
